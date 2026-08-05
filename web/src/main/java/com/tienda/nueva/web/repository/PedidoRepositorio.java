package com.tienda.nueva.web.repository;

import com.tienda.nueva.web.model.pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidoRepositorio extends JpaRepository<pedido, Long> {
}