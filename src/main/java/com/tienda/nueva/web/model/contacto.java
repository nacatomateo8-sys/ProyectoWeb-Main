package com.tienda.nueva.web.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "contactos") // Define el nombre de la tabla en la base de datos
public class contacto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Hace que el ID sea autoincremental
    private Integer id;

    private String nombre;
    private String email;
    private String asunto;
    private String mensaje;

    // Constructor vacío (Obligatorio para JPA)
    public contacto() {}

    // Constructor con parámetros (incluyendo el ID opcional si lo necesitas)
    public contacto(String nombre, String email, String asunto, String mensaje) {
        this.nombre = nombre;
        this.email = email;
        this.asunto = asunto;
        this.mensaje = mensaje;
    }

    // Getters y Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAsunto() { return asunto; }
    public void setAsunto(String asunto) { this.asunto = asunto; }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
}