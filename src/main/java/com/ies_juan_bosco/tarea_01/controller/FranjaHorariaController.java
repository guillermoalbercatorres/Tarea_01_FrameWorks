package com.ies_juan_bosco.tarea_01.controller;

import com.ies_juan_bosco.tarea_01.entity.FranjaHoraria;
import com.ies_juan_bosco.tarea_01.repository.FranjaHorariaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.List;

@RestController
public class FranjaHorariaController {
    @Autowired
    private final FranjaHorariaRepository franjaHorariaRepository;

    public FranjaHorariaController(FranjaHorariaRepository franjaHorariaRepository) {
        this.franjaHorariaRepository = franjaHorariaRepository;
    }

    @GetMapping("/franja-horaria")
    public List<FranjaHoraria> findAll() {
        return franjaHorariaRepository.findAll();
    }

    @GetMapping("/franja-horaria/{id}")
    public ResponseEntity<FranjaHoraria> findById(@PathVariable Long id) {
        return franjaHorariaRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/franja-horaria")
    public ResponseEntity<FranjaHoraria> create(@RequestBody FranjaHoraria franjaHoraria) {
        franjaHoraria.setId(null);
        return ResponseEntity.status(HttpStatus.CREATED).body(franjaHorariaRepository.save(franjaHoraria));
    }

    @PutMapping("/franja-horaria/{id}")
    public ResponseEntity<FranjaHoraria> update(@PathVariable Long id, @RequestBody FranjaHoraria franjaHoraria) {
        if (!franjaHorariaRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        franjaHoraria.setId(id);
        return ResponseEntity.ok(franjaHorariaRepository.save(franjaHoraria));
    }

    @DeleteMapping("/franja-horaria/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        if (!franjaHorariaRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        franjaHorariaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // PATCH /franja-horaria/{id}/{horaInicio}/{horaFin}, formato HH:mm
    @PatchMapping("/franja-horaria/{id}/{horaInicio}/{horaFin}")
    public ResponseEntity<FranjaHoraria> updateHoras(@PathVariable Long id,
                                                     @PathVariable LocalTime horaInicio,
                                                     @PathVariable LocalTime horaFin) {
        return franjaHorariaRepository.findById(id)
                .map(franja -> {
                    franja.setHoraInicio(horaInicio);
                    franja.setHoraFin(horaFin);
                    return ResponseEntity.ok(franjaHorariaRepository.save(franja));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
