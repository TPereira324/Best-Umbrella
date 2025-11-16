package com.best_umbrella.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.best_umbrella.backend.model.Utilizador;

@Repository
public interface UtilizadorRepository extends JpaRepository<Utilizador, Long> {
    Utilizador findByEmail(String email);
    java.util.List<Utilizador> findByAlertaChuvaAtivoTrue();
}