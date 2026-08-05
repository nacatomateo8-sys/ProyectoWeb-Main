package com.tienda.nueva.web.controller;

import com.tienda.nueva.web.model.Producto;
import com.tienda.nueva.web.repository.ProductoRepositorio;
import com.tienda.nueva.web.service.CarritoModeloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/productos")
public class ApiProductoController {

    @Autowired
    private ProductoRepositorio productoRepositorio;

    @Autowired
    private CarritoModeloService carritoModeloService;

    // Obtener todos los productos
    @GetMapping
    public ResponseEntity<List<Producto>> obtenerTodos() {
        List<Producto> productos = productoRepositorio.findAll();
        return ResponseEntity.ok(productos);
    }

    // Obtener un producto por ID
    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerPorId(@PathVariable int id) {
        Optional<Producto> producto = productoRepositorio.findById(id);
        return producto.map(ResponseEntity::ok)
                       .orElse(ResponseEntity.notFound().build());
    }

    // Agregar producto al carrito desde el carrito.js
    @PostMapping("/agregar-carrito")
    public ResponseEntity<String> agregarAlCarrito(
            @RequestParam int id,
            @RequestParam(defaultValue = "1") int cantidad) {
        try {
            carritoModeloService.agregarProducto(id, cantidad);
            return ResponseEntity.ok("Producto agregado al carrito");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}
