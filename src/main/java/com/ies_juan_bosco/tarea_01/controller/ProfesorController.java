package com.ies_juan_bosco.tarea_01.controller;

import com.ies_juan_bosco.tarea_01.entity.Profesor;
import com.ies_juan_bosco.tarea_01.repository.ProfesorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProfesorController {
    @Autowired
    private final ProfesorRepository profesorRepository;

    public ProfesorController(ProfesorRepository profesorRepository) {
        this.profesorRepository = profesorRepository;
    }

    @GetMapping("/profesor")
    public List<Profesor> findAll() {
        return profesorRepository.findAll();
    }

    @GetMapping("/profesor/{id}")
    public ResponseEntity<Profesor> findById(@PathVariable Long id) {
        return profesorRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/profesor")
    public ResponseEntity<Profesor> create(@RequestBody Profesor profesor) {
        profesor.setId(null);
        return ResponseEntity.status(HttpStatus.CREATED).body(profesorRepository.save(profesor));
    }

    @PutMapping("/profesor/{id}")
    public ResponseEntity<Profesor> update(@PathVariable Long id, @RequestBody Profesor profesor) {
        if (!profesorRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        profesor.setId(id);
        return ResponseEntity.ok(profesorRepository.save(profesor));
    }

    @DeleteMapping("/profesor/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        if (!profesorRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        profesorRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/profesor/{id}/{correo}")
    public ResponseEntity<Profesor> updateCorreo(@PathVariable Long id, @PathVariable String correo) {
        return profesorRepository.findById(id)
                .map(profesor -> {
                    profesor.setCorreo(correo);
                    return ResponseEntity.ok(profesorRepository.save(profesor));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
