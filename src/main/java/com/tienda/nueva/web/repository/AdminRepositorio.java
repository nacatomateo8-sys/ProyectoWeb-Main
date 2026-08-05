package com.tienda.nueva.web.repository;

import com.tienda.nueva.web.model.administrador; // <-- Con 'A' mayúscula
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepositorio extends JpaRepository<administrador, Long> {
    // Aquí puedes añadir métodos personalizados más adelante si lo necesitas, por ejemplo:
    // Optional<Administrador> findByEmail(String email);
}