package com.tienda.nueva.web.repository;

import com.tienda.nueva.web.model.Detallepedido; // Cambiado a 'P' mayúscula
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetallePedidoRepositorio extends JpaRepository<Detallepedido, Long> { // Cambiado a 'P' mayúscula
    
}