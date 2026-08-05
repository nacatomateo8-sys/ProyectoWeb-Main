package com.tienda.nueva.web.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tienda.nueva.web.model.carrito; // Usando tu entidad original
import com.tienda.nueva.web.model.Producto;
import com.tienda.nueva.web.repository.ProductoRepositorio;

@Service
public class CarritoModeloService {

    @Autowired
    private ProductoRepositorio productoRepositorio;

    // Estado interno utilizando tu Entidad estructurada en minúscula
    private final List<carrito> items = new ArrayList<>();
    private BigDecimal total = BigDecimal.ZERO;

    private final List<CarritoObservador> observadores = new ArrayList<>();

    public void registrarObservador(CarritoObservador observador) {
        this.observadores.add(observador);
    }

    public void notificarObservadores() {
        for (CarritoObservador obs : observadores) {
            // Envía la lista de tipo 'carrito' de manera segura
            obs.mapearCambiosAVista(new ArrayList<>(items), total);
        }
    }

    // Regla de Negocio: Agregar un elemento al Carrito utilizando la Entidad
    public void agregarProducto(int productoId, int cantidad) {
        boolean existe = false;
        for (carrito item : items) {
            if (item.getProducto().getId() == productoId) {
                item.setCantidad(item.getCantidad() + cantidad);
                existe = true;
                break;
            }
        }
        
        if (!existe) {
            Producto productoBD = productoRepositorio.findById(productoId).orElse(null);
            if (productoBD != null) {
                // Instanciamos tu entidad carrito asignando un usuario genérico (ID: 1)
                carrito nuevoItem = new carrito(1, productoBD, cantidad);
                items.add(nuevoItem);
            }
        }
        
        actualizarTotal();
        notificarObservadores(); // Notificación tras el cambio
    }

    public void eliminarProducto(int index) {
        if (index >= 0 && index < items.size()) {
            items.remove(index);
        }
        actualizarTotal();
        notificarObservadores();
    }

    public void vaciarCarrito() {
        items.clear();
        actualizarTotal();
        notificarObservadores();
    }

    // CORRECCIÓN AQUÍ: Se cambió la sintaxis de '::' por expresiones Lambda comunes
    private void actualizarTotal() {
        this.total = items.stream()
            .map(item -> item.getSubtotal()) // Evita errores de mapeo directo si el editor falla
            .reduce(BigDecimal.ZERO, (a, b) -> a.add(b));
    }

    public List<carrito> getItems() { return new ArrayList<>(items); }
    public BigDecimal getTotal() { return total; }
}