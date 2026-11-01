package com.ies_juan_bosco.tarea_01.repository;

import com.ies_juan_bosco.tarea_01.entity.Aula;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AulaRepository extends JpaRepository<Aula, Long> {
    List<Aula> findByOrdenadores(boolean ordenadores);
    List<Aula> findByCapacidadGreaterThan(Integer capacidad);
    List<Aula> findByOrdenadoresAndCapacidadGreaterThan(boolean ordenadores, Integer capacidad);
}
