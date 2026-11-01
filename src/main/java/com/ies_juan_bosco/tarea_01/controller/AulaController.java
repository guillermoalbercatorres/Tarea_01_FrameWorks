package com.ies_juan_bosco.tarea_01.controller;

import com.ies_juan_bosco.tarea_01.entity.Aula;
import com.ies_juan_bosco.tarea_01.repository.AulaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AulaController {


    //Inyección de dependencias.
    // Spring se encarga de crear el objeto y destruirlo cuando sea necesario (ciclo de vida del objeto).
    @Autowired
    private final AulaRepository aulaRepository;

    public AulaController(AulaRepository aulaRepository) {
        this.aulaRepository = aulaRepository;
    }

    @GetMapping("/aula")
    public List<Aula> findAll() {
        return aulaRepository.findAll();
    }

    @GetMapping("/aula/{id}")
    public ResponseEntity<Aula> findById(@PathVariable Long id) {

        return aulaRepository.findById(id)
                .map(ResponseEntity::ok)    //Si el alumno existe en la BD
                .orElseGet(() -> ResponseEntity.notFound().build()); //Si el alumno no existe en la BD
    }

    @DeleteMapping("/aula/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        if (!aulaRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        aulaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    //PUT http://localhost:8080/aula/5
    @PutMapping("/aula/{id}")
    public ResponseEntity<Aula> update(@PathVariable Long id, @RequestBody Aula aula) {
        if (!aulaRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        aula.setId(id);
        return ResponseEntity.ok(aulaRepository.save(aula));
    }

    //POST http://localhost:8080/aula
    @PostMapping("/aula")
    public ResponseEntity<Aula> create(@RequestBody Aula aula) {
        return ResponseEntity.status(HttpStatus.CREATED).body(aulaRepository.save(aula));
    }

    //PATCH modifica la capacidad de un aula concreta.
    @PatchMapping("/aula/{id}/{capacidad}")
    public ResponseEntity<Aula> updateCapacidad(@PathVariable Long id, @PathVariable Integer capacidad) {
        return aulaRepository.findById(id)
                .map(aula -> {    //Si el alumno existe en la BD ejecuta .map
                    aula.setCapacidad(capacidad);
                    return ResponseEntity.ok(aulaRepository.save(aula));
                })
                .orElseGet(() -> {      //Si el alumno no existe en la BD ejecuta .orElseGet
                    return ResponseEntity.notFound().build();
                });
    }



}
