package com.tienda.nueva.web.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "administradores")
public class administrador {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // <-- Cambiado de 'int' a 'Long' para solucionar el error del Repositorio

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String rol = "admin";

    @Column(name = "ultimo_acceso")
    private LocalDateTime ultimoAcceso;

    @Column(nullable = false, updatable = false)
    private LocalDateTime creado = LocalDateTime.now();

    public administrador() {}

    public administrador(String nombre, String email, String password, String rol) {
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.rol = rol;
    }

    // Getters y Setters corregidos con Long
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public LocalDateTime getUltimoAcceso() { return ultimoAcceso; }
    public void setUltimoAcceso(LocalDateTime ultimoAcceso) { this.ultimoAcceso = ultimoAcceso; }

    public LocalDateTime getCreado() { return creado; }
    public void setCreado(LocalDateTime creado) { this.creado = creado; }
}