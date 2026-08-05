package com.tienda.nueva.web.repository;


import org.springframework.stereotype.Repository;//importar la anotación @Repository

import com.tienda.nueva.web.model.Producto;

import org.springframework.data.jpa.repository.JpaRepository;//importar la anotación @Repository

@Repository
public interface ProductoRepositorio extends JpaRepository<Producto, Integer> {
    // Aquí va vacío por ahora
}