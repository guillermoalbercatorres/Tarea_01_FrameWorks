package com.ies_juan_bosco.tarea_01.repository;

import com.ies_juan_bosco.tarea_01.entity.Profesor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfesorRepository extends JpaRepository<Profesor, Long> {
}
