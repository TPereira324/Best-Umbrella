package com.best_umbrella.backend.service;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.best_umbrella.backend.model.Utilizador;
import com.best_umbrella.backend.repository.UtilizadorRepository;

@Service
public class UtilizadorService {

    private final UtilizadorRepository utilizadorRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UtilizadorService(UtilizadorRepository utilizadorRepository, PasswordEncoder passwordEncoder) {
        this.utilizadorRepository = utilizadorRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Utilizador> findAll() {
        return utilizadorRepository.findAll();
    }

    public Optional<Utilizador> findById(Long id) {
        return utilizadorRepository.findById(id.intValue());
    }

    public Utilizador findByEmail(String email) {
        return utilizadorRepository.findByEmail(email);
    }

    public Utilizador save(Utilizador utilizador) {
        if (utilizador.getDataRegisto() == null) {
            utilizador.setDataRegisto(LocalDateTime.now());
        }
        if (utilizador.getEmail() != null) {
            utilizador.setEmail(utilizador.getEmail().trim().toLowerCase());
        }
        if (utilizador.getPassword() != null && !utilizador.getPassword().isBlank()) {
            if (!isBCryptHash(utilizador.getPassword())) {
                utilizador.setPassword(passwordEncoder.encode(utilizador.getPassword()));
            }
        }
        return utilizadorRepository.save(utilizador);
    }

    public void deleteById(Long id) {
        utilizadorRepository.deleteById(id.intValue());
    }

    private boolean isBCryptHash(String value) {
        if (value == null) return false;
        return (value.startsWith("$2a$") || value.startsWith("$2b$") || value.startsWith("$2y$"))
                && value.length() >= 60;
    }
}
//hello