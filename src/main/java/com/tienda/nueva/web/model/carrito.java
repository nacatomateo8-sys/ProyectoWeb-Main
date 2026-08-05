package com.tienda.nueva.web.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Entity
@Table(name = "carrito", uniqueConstraints = {@UniqueConstraint(columnNames = {"usuario_id", "producto_id"})})
public class carrito { // Cambiado a Mayúscula según buenas prácticas

 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
private int id;

 @Column(name = "usuario_id", nullable = false)
  private int usuarioId; 

@ManyToOne
 @JoinColumn(name = "producto_id", nullable = false)
 private Producto producto;

 @Column(nullable = false)
 private int cantidad = 1;

 @Column(nullable = false, updatable = false)
 private LocalDateTime agregado = LocalDateTime.now();

 // Constructor vacío obligatorio para JPA
 public carrito() {} 

 // Constructor mapeado
public carrito(int usuarioId, Producto producto, int cantidad) {
this.usuarioId = usuarioId;
 this.producto = producto;
 this.cantidad = cantidad;
 }
 // Método helper para obtener el subtotal calculado usando BigDecimal de forma precisa
 public BigDecimal getSubtotal() {
 if (producto != null && producto.getPrecio() != null) {
 return BigDecimal.valueOf(this.cantidad).multiply(producto.getPrecio());
 }
 return BigDecimal.ZERO;
 }

 // ============================================================
 // GETTERS Y SETTERS
 // ============================================================
 public int getId() { return id; }
public void setId(int id) { this.id = id; }

 public int getUsuarioId() { return usuarioId; }
 public void setUsuarioId(int usuarioId) { this.usuarioId = usuarioId; }

 public Producto getProducto() { return producto; }
 public void setProducto(Producto producto) { this.producto = producto; }

 public int getCantidad() { return cantidad; }
 public void setCantidad(int cantidad) { this.cantidad = cantidad; }

public LocalDateTime getAgregado() { return agregado; }
 public void setAgregado(LocalDateTime agregado) { this.agregado = agregado; }
}