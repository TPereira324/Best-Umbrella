package com.best_umbrella.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.best_umbrella.backend.model.Aluguer;

@Repository
public interface AluguerRepository extends JpaRepository<Aluguer, Long> {
}

// hello