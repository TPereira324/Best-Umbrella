package com.best_umbrella.backend.service;

import com.best_umbrella.backend.config.PayPalProperties;
import com.paypal.core.PayPalEnvironment;
import com.paypal.core.PayPalHttpClient;
import com.paypal.http.HttpResponse;
import com.paypal.orders.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class PayPalService {

    private final PayPalProperties props;
    private final PayPalHttpClient client;

    public PayPalService(PayPalProperties props) {
        this.props = props;
        PayPalEnvironment environment = "live".equalsIgnoreCase(props.getMode())
                ? new PayPalEnvironment.Live(props.getClientId(), props.getClientSecret())
                : new PayPalEnvironment.Sandbox(props.getClientId(), props.getClientSecret());
        this.client = new PayPalHttpClient(environment);
    }

    // Método removido - o SDK v2 gerencia automaticamente a autenticação

    public Map<String, Object> createOrder(String value, String currency) {
        try {
            // Criar o request da ordem usando o SDK v2
            OrderRequest orderRequest = new OrderRequest();
            orderRequest.checkoutPaymentIntent("CAPTURE");

            // Contexto da aplicação para melhor UX e retorno controlado na demo
            ApplicationContext appCtx = new ApplicationContext()
                    .brandName("Best Umbrella")
                    .landingPage("LOGIN")
                    .userAction("PAY_NOW")
                    .returnUrl("http://10.0.2.2:8080/success.html")
                    .cancelUrl("http://10.0.2.2:8080/paypal.html");
            orderRequest.applicationContext(appCtx);
            
            // Configurar unidade de compra
            List<PurchaseUnitRequest> purchaseUnits = new ArrayList<>();
            purchaseUnits.add(new PurchaseUnitRequest()
                    .amountWithBreakdown(new AmountWithBreakdown()
                            .currencyCode(currency)
                            .value(value)));
            orderRequest.purchaseUnits(purchaseUnits);
            
            // Executar request
            OrdersCreateRequest request = new OrdersCreateRequest().requestBody(orderRequest);
            HttpResponse<Order> response = client.execute(request);
            
            Order order = response.result();
            
            // Extrair link de aprovação
            String approveLink = null;
            if (order.links() != null) {
                for (LinkDescription link : order.links()) {
                    if ("approve".equals(link.rel())) {
                        approveLink = link.href();
                        break;
                    }
                }
            }
            
            // Retornar resultado
            Map<String, Object> result = new HashMap<>();
            result.put("orderId", order.id());
            result.put("approveLink", approveLink);
            return result;
            
        } catch (IOException e) {
            throw new RuntimeException("Erro ao criar ordem no PayPal: " + e.getMessage(), e);
        }
    }

    public Map<String, Object> captureOrder(String orderId) {
        try {
            // Capturar ordem usando o SDK v2
            OrdersCaptureRequest request = new OrdersCaptureRequest(orderId);
            HttpResponse<Order> response = client.execute(request);
            
            Order order = response.result();
            
            // Converter para Map para manter compatibilidade com o controller
            Map<String, Object> result = new HashMap<>();
            result.put("id", order.id());
            result.put("status", order.status());
            
            if (order.purchaseUnits() != null && !order.purchaseUnits().isEmpty()) {
                PurchaseUnit purchaseUnit = order.purchaseUnits().get(0);
                if (purchaseUnit.payments() != null && purchaseUnit.payments().captures() != null 
                    && !purchaseUnit.payments().captures().isEmpty()) {
                    Capture capture = purchaseUnit.payments().captures().get(0);
                    result.put("captureId", capture.id());
                    result.put("captureStatus", capture.status());
                    if (capture.amount() != null) {
                        result.put("amount", capture.amount().value());
                        result.put("currency", capture.amount().currencyCode());
                    }
                }
            }
            
            return result;
            
        } catch (IOException e) {
            throw new RuntimeException("Erro ao capturar ordem no PayPal: " + orderId + " - " + e.getMessage(), e);
        }
    }
}