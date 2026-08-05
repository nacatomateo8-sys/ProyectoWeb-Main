package com.tienda.nueva.web.repository;

import com.tienda.nueva.web.model.pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PedidoRepositorio extends JpaRepository<pedido, Long> {
    
    // Esta es la clave: Spring Data JPA permite navegar por las propiedades del objeto usuario
    // Busca en la tabla 'pedido' -> columna 'usuario' -> columna 'email'
    List<pedido> findByUsuarioEmail(String email);

    
}