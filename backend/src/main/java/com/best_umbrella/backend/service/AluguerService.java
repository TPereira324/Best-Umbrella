package com.best_umbrella.backend.service;

import com.best_umbrella.backend.model.Aluguer;
import com.best_umbrella.backend.model.GuardaChuva;
import com.best_umbrella.backend.model.PontodeAluguer;
import com.best_umbrella.backend.model.Utilizador;
import com.best_umbrella.backend.repository.AluguerRepository;
import com.best_umbrella.backend.repository.GuardaChuvaRepository;
import com.best_umbrella.backend.repository.PontodeAluguerRepository;
import com.best_umbrella.backend.repository.UtilizadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AluguerService {

    private final AluguerRepository aluguerRepository;
    private final UtilizadorRepository utilizadorRepository;
    private final GuardaChuvaRepository guardaChuvaRepository;
    private final PontodeAluguerRepository pontodeAluguerRepository;

    @Autowired
    public AluguerService(AluguerRepository aluguerRepository,
                          UtilizadorRepository utilizadorRepository,
                          GuardaChuvaRepository guardaChuvaRepository,
                          PontodeAluguerRepository pontodeAluguerRepository) {
        this.aluguerRepository = aluguerRepository;
        this.utilizadorRepository = utilizadorRepository;
        this.guardaChuvaRepository = guardaChuvaRepository;
        this.pontodeAluguerRepository = pontodeAluguerRepository;
    }

    public List<Aluguer> findAll() {
        return aluguerRepository.findAll();
    }

    public Optional<Aluguer> findById(Long id) {
        return aluguerRepository.findById(id);
    }

    public Aluguer iniciarAluguer(Long utilizadorId, Integer guardaChuvaId, Integer pontoInicioId) {
        Utilizador utilizador = utilizadorRepository.findById(utilizadorId)
                .orElseThrow(() -> new IllegalArgumentException("Utilizador não encontrado"));
        GuardaChuva guardaChuva = guardaChuvaRepository.findById(Long.valueOf(guardaChuvaId))
                .orElseThrow(() -> new IllegalArgumentException("Guarda-chuva não encontrado"));
        PontodeAluguer pontoInicio = pontodeAluguerRepository.findById(Long.valueOf(pontoInicioId))
                .orElseThrow(() -> new IllegalArgumentException("Ponto de aluguer (início) não encontrado"));

        if (guardaChuva.getEstado() != null && !guardaChuva.getEstado().equalsIgnoreCase("DISPONIVEL")) {
            throw new IllegalStateException("Guarda-chuva não está disponível para aluguer");
        }

        Aluguer aluguer = new Aluguer();
        aluguer.setUtilizador(utilizador);
        aluguer.setGuardaChuva(guardaChuva);
        aluguer.setPontoInicio(pontoInicio);
        aluguer.setDataInicio(LocalDateTime.now());
        aluguer.setEstado("ATIVO");
        aluguer.setCusto(null);

        // Atualiza estado do guarda-chuva
        guardaChuva.setEstado("ALUGADO");
        // Quando inicia, o guarda-chuva sai do ponto
        guardaChuva.setPontodeAluguer(null);

        guardaChuvaRepository.save(guardaChuva);
        return aluguerRepository.save(aluguer);
    }

    public Aluguer terminarAluguer(Long aluguerId, Integer pontoFimId) {
        Aluguer aluguer = aluguerRepository.findById(aluguerId)
                .orElseThrow(() -> new IllegalArgumentException("Aluguer não encontrado"));
        if (aluguer.getEstado() == null || !aluguer.getEstado().equalsIgnoreCase("ATIVO")) {
            throw new IllegalStateException("Aluguer não está ativo");
        }

        PontodeAluguer pontoFim = pontodeAluguerRepository.findById(Long.valueOf(pontoFimId))
                .orElseThrow(() -> new IllegalArgumentException("Ponto de aluguer (fim) não encontrado"));

        aluguer.setPontoFim(pontoFim);
        aluguer.setDataFim(LocalDateTime.now());
        aluguer.setEstado("TERMINADO");

        // Cálculo de custo simples para demo (sem cobrança real)
        if (aluguer.getDataInicio() != null && aluguer.getDataFim() != null) {
            long minutos = Duration.between(aluguer.getDataInicio(), aluguer.getDataFim()).toMinutes();
            // Para demonstração beta, custo é 0.0 independentemente da duração
            aluguer.setCusto(0.0);
        } else {
            aluguer.setCusto(0.0);
        }

        // Atualiza guarda-chuva de volta para disponível no ponto final
        GuardaChuva gc = aluguer.getGuardaChuva();
        if (gc != null) {
            gc.setEstado("DISPONIVEL");
            gc.setPontodeAluguer(pontoFim);
            guardaChuvaRepository.save(gc);
        }

        return aluguerRepository.save(aluguer);
    }
}