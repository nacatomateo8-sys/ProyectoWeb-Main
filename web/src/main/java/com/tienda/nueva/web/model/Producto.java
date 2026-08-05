package com.tienda.nueva.web.model;
import java.math.BigDecimal;
import java.util.List;
import jakarta.persistence.*;

@Entity
@Table(name = "productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    // Mapea el atributo 'category' a la columna 'categoria' de la base de datos
    @Column(name = "categoria", length = 80)
    private String category;

    @Column(length = 255)
    private String imagen;

    // Tinyint(1) se mapea comúnmente como un boolean en Java
    @Column(nullable = false)
    private boolean disponible = true;

    // Relación con los detalles de los pedidos
    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL)
    private List<Detallepedido> detalles; 

    // Este campo no existe en la BD; se usa solo en la lógica de la app (ej. añadir al carrito)
    @Transient
    private int cantidadSeleccionada = 1;

    /* Constructores */
    public Producto() {}

    public Producto(String nombre, String descripcion, BigDecimal  precio, String category, String imagen, boolean disponible) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.category = category;
        this.imagen = imagen;
        this.disponible = disponible;
    }

    /* Getters y Setters */
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public BigDecimal  getPrecio() { return precio; }
    public void setPrecio(BigDecimal  precio) { this.precio = precio; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getImagen() { return imagen; }
    public void setImagen(String imagen) { this.imagen = imagen; }

    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }

    public List<Detallepedido> getDetalles() { return detalles; }
    public void setDetalles(List<Detallepedido> detalles) { this.detalles = detalles; }

    public int getCantidadSeleccionada() { return cantidadSeleccionada; }
    public void setCantidadSeleccionada(int cantidadSeleccionada) {
        this.cantidadSeleccionada = cantidadSeleccionada;
    }
}