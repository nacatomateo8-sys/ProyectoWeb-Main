package com.tienda.nueva.web.repository;

import org.springframework.stereotype.Repository;
import com.tienda.nueva.web.model.usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

@Repository
public interface UsuarioRepositorio extends JpaRepository<usuario, Integer> {
    
    // Método JPA necesario para el login y control de duplicados por correo
    Optional<usuario> findByEmail(String email);

    // CORRECCIÓN: Método indispensable para el Login Mixto y validación de duplicados por nombre de usuario
    Optional<usuario> findByUsername(String username);
}