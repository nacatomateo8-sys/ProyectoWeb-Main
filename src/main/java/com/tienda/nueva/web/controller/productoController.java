package com.tienda.nueva.web.controller;

import com.tienda.nueva.web.model.Producto;
import com.tienda.nueva.web.repository.ProductoRepositorio;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class productoController {

    @Autowired
    private ProductoRepositorio productoRepositorio;

    // Helper interno para gestionar el carrito en la sesión
    @SuppressWarnings("unchecked")
    private List<Producto> obtenerCarrito(HttpSession session) {
        List<Producto> carrito = (List<Producto>) session.getAttribute("carrito");
        if (carrito == null) {
            carrito = new ArrayList<>();
            session.setAttribute("carrito", carrito);
        }
        return carrito;
    }

    // 1. AGREGAR PRODUCTOS AL CARRITO
    @PostMapping("/agregar")
    public String agregarAlCarrito(@RequestParam int id,
            @RequestParam(defaultValue = "1") int cantidad,
            HttpSession session) {
        List<Producto> carrito = obtenerCarrito(session);

        boolean existe = false;
        for (Producto prod : carrito) {
            if (prod.getId() == id) {
                prod.setCantidadSeleccionada(prod.getCantidadSeleccionada() + cantidad);
                existe = true;
                break;
            }
        }

        if (!existe) {
            Producto productoBD = productoRepositorio.findById(id).orElse(null);
            if (productoBD != null) {
                productoBD.setCantidadSeleccionada(cantidad);
                carrito.add(productoBD);
            }
        }
        return "redirect:/catalogo";
    }

    // El método viejo @GetMapping("/carrito") ha sido ELIMINADO de aquí 
    // para que no choque con CarritoWebController y compile perfectamente.

    // 2. ELIMINAR UN PRODUCTO ESPECÍFICO DEL CARRITO
    @GetMapping("/eliminar")
    public String eliminarDelCarrito(@RequestParam int index, HttpSession session) {
        List<Producto> carrito = obtenerCarrito(session);
        if (index >= 0 && index < carrito.size()) {
            carrito.remove(index);
        }
        return "redirect:/carrito";
    }

    // 3. VACIAR 

    @GetMapping("/vaciar")
    public String vaciarCarrito(HttpSession session) {
        obtenerCarrito(session).clear();
        return "redirect:/carrito";
    }
}