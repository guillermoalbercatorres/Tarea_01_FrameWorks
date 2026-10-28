package com.ies_juan_bosco.tarea_01.controller;

import com.ies_juan_bosco.tarea_01.entity.Reserva;
import com.ies_juan_bosco.tarea_01.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
public class ReservaController {
    @Autowired
    private final ReservaRepository reservaRepository;

    public ReservaController(ReservaRepository reservaRepository) {
        this.reservaRepository = reservaRepository;
    }

    @GetMapping("/reserva")
    public List<Reserva> findAll() {
        return reservaRepository.findAll();
    }

    @GetMapping("/reserva/{id}")
    public ResponseEntity<Reserva> findById(@PathVariable Long id) {
        return reservaRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/reserva")
    public ResponseEntity<Reserva> create(@RequestBody Reserva reserva) {
        reserva.setId(null);
        return ResponseEntity.status(HttpStatus.CREATED).body(reservaRepository.save(reserva));
    }

    @PutMapping("/reserva/{id}")
    public ResponseEntity<Reserva> update(@PathVariable Long id, @RequestBody Reserva reserva) {
        if (!reservaRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        reserva.setId(id);
        return ResponseEntity.ok(reservaRepository.save(reserva));
    }

    @DeleteMapping("/reserva/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        if (!reservaRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        reservaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/reserva/{id}/{fecha}")
    public ResponseEntity<Reserva> updateFecha(@PathVariable Long id, @PathVariable LocalDate fecha) {
        return reservaRepository.findById(id)
                .map(reserva -> {
                    reserva.setFecha(fecha);
                    return ResponseEntity.ok(reservaRepository.save(reserva));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
