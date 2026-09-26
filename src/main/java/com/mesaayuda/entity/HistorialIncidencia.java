package com.mesaayuda.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "historial_incidencia")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HistorialIncidencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "incidencia_id", nullable = false)
    private Incidencia incidencia;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(nullable = false, length = 100)
    private String accion;

    @Column(length = 30)
    private String estadoAnterior;

    @Column(length = 30)
    private String estadoNuevo;

    @Column(nullable = false)
    private LocalDateTime fechaHora;

    @Column(length = 255)
    private String observacion;
}