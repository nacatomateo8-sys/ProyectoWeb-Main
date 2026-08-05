package com.tienda.nueva.web.model;

import jakarta.persistence.*;

@Entity
@Table(name = "detallepedido")
public class Detallepedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "id_pedido", nullable = false)
    private pedido pedido;

    @ManyToOne
    @JoinColumn(name = "id_producto", nullable = false)
    private Producto producto;

    private int cantidad;
    private double precioUnit;

    public Detallepedido() {}

    // Getters y Setters corregidos y unificados
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public pedido getPedido() { return pedido; }
    public void setPedido(pedido pedido) { this.pedido = pedido; }

    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public double getPrecioUnit() { return precioUnit; }
    public void setPrecioUnit(double precioUnit) { this.precioUnit = precioUnit; }
}