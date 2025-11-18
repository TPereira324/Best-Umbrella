package com.best_umbrella.backend.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.best_umbrella.backend.model.Aluguer;
import com.best_umbrella.backend.model.Multa;
import com.best_umbrella.backend.model.Utilizador;
import com.best_umbrella.backend.repository.AluguerRepository;
import com.best_umbrella.backend.repository.MultaRepository;
import com.best_umbrella.backend.repository.UtilizadorRepository;

@Service
public class MultaService {

    private final MultaRepository multaRepository;
    private final AluguerRepository aluguerRepository;
    private final UtilizadorRepository utilizadorRepository;

    @Autowired
    public MultaService(MultaRepository multaRepository,
                        AluguerRepository aluguerRepository,
                        UtilizadorRepository utilizadorRepository) {
        this.multaRepository = multaRepository;
        this.aluguerRepository = aluguerRepository;
        this.utilizadorRepository = utilizadorRepository;
    }

    public List<Multa> findAll() {
        return multaRepository.findAll();
    }

    public Optional<Multa> findById(Integer id) {
        return multaRepository.findById(id);
    }

    public Multa save(Multa multa) {
        if (multa.getDataEmissao() == null) {
            multa.setDataEmissao(LocalDateTime.now());
        }
        if (multa.getEstado() == null) {
            multa.setEstado("PENDENTE");
        }
        if (multa.getMoeda() == null) {
            multa.setMoeda("EUR");
        }
        return multaRepository.save(multa);
    }

    public void deleteById(Integer id) {
        multaRepository.deleteById(id);
    }

    public List<Multa> findByUtilizadorId(Integer utilizadorId) {
        Optional<Utilizador> utilizador = utilizadorRepository.findById(utilizadorId);
        return utilizador.map(multaRepository::findByUtilizador).orElseGet(List::of);
    }

    public List<Multa> findByAluguerId(Long aluguerId) {
        Optional<Aluguer> aluguer = aluguerRepository.findById(aluguerId);
        return aluguer.map(multaRepository::findByAluguer).orElseGet(List::of);
    }

    public Multa criarMultaParaAluguer(Long aluguerId, Double valor, String motivo, String descricao, String moeda) {
        Aluguer aluguer = aluguerRepository.findById(aluguerId)
                .orElseThrow(() -> new IllegalArgumentException("Aluguer não encontrado"));
        Multa multa = new Multa();
        multa.setAluguer(aluguer);
        multa.setUtilizador(aluguer.getUtilizador());
        multa.setValor(valor);
        multa.setMotivo(motivo);
        multa.setDescricao(descricao);
        multa.setEstado("PENDENTE");
        multa.setMoeda(moeda != null ? moeda : "EUR");
        multa.setDataEmissao(LocalDateTime.now());
        multa.setDataVencimento(LocalDateTime.now().plusDays(15));
        return multaRepository.save(multa);
    }

    public Optional<Multa> pagarMulta(Integer multaId) {
        Optional<Multa> multaOpt = multaRepository.findById(multaId);
        multaOpt.ifPresent(m -> {
            if (!"PENDENTE".equalsIgnoreCase(m.getEstado())) {
                throw new IllegalStateException("A multa deve estar PENDENTE para pagar");
            }
            m.setEstado("PAGO");
            m.setDataPagamento(LocalDateTime.now());
            multaRepository.save(m);
        });
        return multaOpt;
    }

    public Optional<Multa> cancelarMulta(Integer multaId) {
        Optional<Multa> multaOpt = multaRepository.findById(multaId);
        multaOpt.ifPresent(m -> {
            if (!"PENDENTE".equalsIgnoreCase(m.getEstado())) {
                throw new IllegalStateException("A multa deve estar PENDENTE para cancelar");
            }
            m.setEstado("CANCELADO");
            multaRepository.save(m);
        });
        return multaOpt;
    }
}