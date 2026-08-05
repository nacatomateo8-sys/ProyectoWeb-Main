package com.tienda.nueva.web.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pedido")
public class pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime fecha;
    private BigDecimal subtotal;
    private BigDecimal iva;
    private BigDecimal envio;
    private BigDecimal total;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private usuario usuario;

    // Relación en cascada: al guardar el pedido, se guardan sus detalles automáticamente
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Detallepedido> detalles = new ArrayList<>();

    public pedido() {}

    // Helpers para asegurar la relación bidireccional
    public void agregarDetalle(Detallepedido detalle) {
        detalles.add(detalle);
        detalle.setPedido(this);
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
    public BigDecimal getIva() { return iva; }
    public void setIva(BigDecimal iva) { this.iva = iva; }
    public BigDecimal getEnvio() { return envio; }
    public void setEnvio(BigDecimal envio) { this.envio = envio; }
    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }
    public usuario getUsuario() { return usuario; }
    public void setUsuario(usuario usuario) { this.usuario = usuario; }
    public List<Detallepedido> getDetalles() { return detalles; }
    public void setDetalles(List<Detallepedido> detalles) { this.detalles = detalles; }
}