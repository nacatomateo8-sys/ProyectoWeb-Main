package com.tienda.nueva.web.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "usuarios")
public class usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer activo;
    private String direccion;
    private String email;
    private String nombre;
    private String password;
    private String rol;
    private String telefono;
    
    @Column(name = "creado", columnDefinition = "DATETIME(6)")
    private LocalDateTime creado;
    
    private String username;

    // --- CONSTRUCTOR ---
    public usuario() {}

    // --- GETTERS Y SETTERS ---
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getActivo() { return activo; }
    public void setActivo(Integer activo) { this.activo = activo; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public LocalDateTime getCreado() { return creado; }
    public void setCreado(LocalDateTime creado) { this.creado = creado; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
}