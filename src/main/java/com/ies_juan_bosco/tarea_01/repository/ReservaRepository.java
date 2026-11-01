package com.ies_juan_bosco.tarea_01.repository;

import com.ies_juan_bosco.tarea_01.entity.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    boolean existsByFechaAndAulaIdAndFranjaHorariaId(LocalDate fecha, Long aulaId, Long franjaHorariaId);
}
