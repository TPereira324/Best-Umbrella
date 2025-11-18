package com.best_umbrella.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import com.best_umbrella.backend.model.Multa;
import com.best_umbrella.backend.model.Utilizador;
import com.best_umbrella.backend.model.Aluguer;

@Repository
public interface MultaRepository extends JpaRepository<Multa, Integer> {
    List<Multa> findByUtilizador(Utilizador utilizador);
    List<Multa> findByAluguer(Aluguer aluguer);
    List<Multa> findByEstado(String estado);
}
// hello