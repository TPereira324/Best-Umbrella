package com.best_umbrella.backend.repository;

import com.best_umbrella.backend.model.Noti;
import com.best_umbrella.backend.model.Utilizador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotiRepository extends JpaRepository<Noti, Long> {
    List<Noti> findByUtilizador(Utilizador utilizador);
    List<Noti> findByTipo(String tipo);
    List<Noti> findByEstado(String estado);
}
//hello