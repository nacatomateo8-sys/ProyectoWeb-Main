package com.tienda.nueva.web.controller;

import com.tienda.nueva.web.model.*;
import com.tienda.nueva.web.repository.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    @Autowired
    private PedidoRepositorio pedidoRepo;

    @PostMapping("/finalizar")
    @Transactional 
    public String finalizarCompra(@RequestBody pedido nuevoPedido, HttpSession session) {
        usuario cliente = (usuario) session.getAttribute("usuarioLogueado");
        if (cliente == null) return "ERROR_SESION";
        
        nuevoPedido.setUsuario(cliente);
        nuevoPedido.setFecha(LocalDateTime.now());

        // Asegurar la relación bidireccional antes de guardar
        if (nuevoPedido.getDetalles() != null) {
            for (Detallepedido detalle : nuevoPedido.getDetalles()) {
                detalle.setPedido(nuevoPedido);
            }
        }

        // JPA guarda el pedido y sus detalles gracias al CascadeType.ALL
        pedidoRepo.save(nuevoPedido);
        return "OK";
    }
}