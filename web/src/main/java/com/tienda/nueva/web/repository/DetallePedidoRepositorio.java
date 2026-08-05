package com.tienda.nueva.web.repository;

import com.tienda.nueva.web.model.Detallepedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetallePedidoRepositorio extends JpaRepository<Detallepedido, Long> {
    // Si no tienes errores subrayados en rojo dentro del editor, todo está perfecto.
}