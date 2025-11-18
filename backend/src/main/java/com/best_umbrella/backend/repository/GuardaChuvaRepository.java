package com.best_umbrella.backend.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.best_umbrella.backend.model.GuardaChuva;
import java.util.List;

public interface GuardaChuvaRepository extends JpaRepository<GuardaChuva, Integer> {
    GuardaChuva findByCodigoQr(String codigoQr);

    List<GuardaChuva> findByPontodeAluguerIsNotNull();
}
// hello