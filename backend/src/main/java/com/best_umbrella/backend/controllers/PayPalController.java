package com.best_umbrella.backend.controllers;

import com.best_umbrella.backend.config.PayPalProperties;
import com.best_umbrella.backend.service.PayPalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/paypal")
/**
 * Controlador de integração com PayPal.
 *
 * Endpoints:
 * - GET  /api/paypal/client-id  → devolve clientId (config)
 * - GET  /api/paypal/config      → devolve config (modo, baseUrl, clientId mascarado)
 * - POST /api/paypal/order       → cria uma ordem (value, currency)
 * - POST /api/paypal/capture/{orderId} → captura uma ordem existente
 */
public class PayPalController {

    private final PayPalService service;
    private final PayPalProperties props;

    public PayPalController(PayPalService service, PayPalProperties props) {
        this.service = service;
        this.props = props;
    }

    @GetMapping("/client-id")
    public ResponseEntity<Map<String, String>> getClientId() {
        return ResponseEntity.ok(Map.of("clientId", props.getClientId()));
    }

    @GetMapping("/config")
    public ResponseEntity<Map<String, String>> getConfig() {
        String clientId = props.getClientId();
        String masked = clientId != null && clientId.length() > 10
                ? clientId.substring(0, 6) + "..." + clientId.substring(clientId.length() - 4)
                : clientId;
        return ResponseEntity.ok(Map.of(
                "mode", props.getMode(),
                "baseUrl", props.getBaseUrl(),
                "clientIdMasked", masked
        ));
    }

    @GetMapping("/order")
    public ResponseEntity<Map<String, Object>> createOrderGet(
            @RequestParam(defaultValue = "10.00") String value,
            @RequestParam(defaultValue = "EUR") String currency
    ) {
        Map<String, Object> order = service.createOrder(value, currency);
        return ResponseEntity.ok(order);
    }

    @PostMapping("/order")
    public ResponseEntity<Map<String, Object>> createOrder(
            @RequestParam(defaultValue = "10.00") String value,
            @RequestParam(defaultValue = "EUR") String currency
    ) {
        Map<String, Object> order = service.createOrder(value, currency);
        return ResponseEntity.ok(order);
    }

    @PostMapping("/capture/{orderId}")
    public ResponseEntity<Map<String, Object>> captureOrder(@PathVariable String orderId) {
        Map<String, Object> captured = service.captureOrder(orderId);
        return ResponseEntity.ok(captured);
    }
}
// hello world