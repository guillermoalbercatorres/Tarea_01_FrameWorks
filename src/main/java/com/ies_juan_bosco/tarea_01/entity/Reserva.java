package com.ies_juan_bosco.tarea_01.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "reservas")
public class Reserva {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate fecha;
    private LocalDate fechaInsercion;

    @ManyToOne
    @JoinColumn(name = "profesor_id", nullable = false)
    private Profesor profesor;

    @ManyToOne
    @JoinColumn(name = "aula_id", nullable = false)
    private Aula aula;

    @ManyToOne
    @JoinColumn(name = "franja_horaria_id", nullable = false)
    private FranjaHoraria franjaHoraria;

    public LocalDate getFechaInsercion() {
        return fechaInsercion;
    }

    public void setFechaInsercion(LocalDate fechaInsercion) {
        this.fechaInsercion = fechaInsercion;
    }

    public Reserva() {}

    public Reserva(LocalDate fecha, LocalDate fechaInsercion, Profesor profesor, Aula aula, FranjaHoraria franjaHoraria) {
        this.fecha = fecha;
        this.fechaInsercion = fechaInsercion;
        this.profesor = profesor;
        this.aula = aula;
        this.franjaHoraria = franjaHoraria;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    public Profesor getProfesor() { return profesor; }
    public void setProfesor(Profesor profesor) { this.profesor = profesor; }
    public Aula getAula() { return aula; }
    public void setAula(Aula aula) { this.aula = aula; }
    public FranjaHoraria getFranjaHoraria() { return franjaHoraria; }
    public void setFranjaHoraria(FranjaHoraria franjaHoraria) { this.franjaHoraria = franjaHoraria; }
}
