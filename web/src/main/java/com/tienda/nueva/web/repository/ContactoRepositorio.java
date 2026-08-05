package com.tienda.nueva.web.repository;
import com.tienda.nueva.web.model.contacto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactoRepositorio extends JpaRepository<contacto, Long> {
}