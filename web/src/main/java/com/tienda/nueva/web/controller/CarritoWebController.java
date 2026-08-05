package com.tienda.nueva.web.controller;

import com.tienda.nueva.web.model.*;
import com.tienda.nueva.web.repository.*;
import com.tienda.nueva.web.service.CarritoModeloService;
import com.tienda.nueva.web.service.CarritoObservador;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/carrito")
public class CarritoWebController implements CarritoObservador {

    @Autowired
    private CarritoModeloService carritoModeloService; 

    @Autowired
    private PedidoRepositorio pedidoRepository; 

    private List<carrito> productosVisuales = new ArrayList<>();
    private BigDecimal totalVisual = BigDecimal.ZERO;

    @Autowired
    public void init() {
        carritoModeloService.registrarObservador(this); 
    }

    @Override
    public void mapearCambiosAVista(List<carrito> items, BigDecimal total) {
        this.productosVisuales = items;
        this.totalVisual = total;
    }

    // 1. VISUALIZAR EL CARRITO CON IVA Y ENVÍO
    @GetMapping
    public String verCarrito(HttpSession session, Model model) {
        model.addAttribute("usuarioLogueado", session.getAttribute("usuarioLogueado"));

        this.productosVisuales = carritoModeloService.getItems();
        this.totalVisual = carritoModeloService.getTotal();

        BigDecimal subtotal = this.totalVisual;
        BigDecimal iva = subtotal.multiply(new BigDecimal("0.15"));
        BigDecimal envio = subtotal.compareTo(BigDecimal.ZERO) > 0 ? new BigDecimal("15.00") : BigDecimal.ZERO;
        BigDecimal totalFinal = subtotal.add(iva).add(envio);

        subtotal = subtotal.setScale(2, RoundingMode.HALF_UP);
        iva = iva.setScale(2, RoundingMode.HALF_UP);
        envio = envio.setScale(2, RoundingMode.HALF_UP);
        totalFinal = totalFinal.setScale(2, RoundingMode.HALF_UP);

        model.addAttribute("carrito", this.productosVisuales);
        model.addAttribute("subtotal", subtotal);
        model.addAttribute("iva", iva);
        model.addAttribute("envio", envio);
        model.addAttribute("total", totalFinal);

        return "carrito"; 
    }

    // 2. PROCESAR PAGO (GUARDADO EN CASCADA AUTOMÁTICO EN MYSQL)
    @PostMapping("/pagar")
    public String procesarPago(HttpSession session, RedirectAttributes redirectAttributes) {
        usuario user = (usuario) session.getAttribute("usuarioLogueado");
        
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "Debes iniciar sesión para finalizar tu compra.");
            return "redirect:/login"; 
        }

        List<carrito> itemsCarrito = carritoModeloService.getItems(); 
        if (itemsCarrito == null || itemsCarrito.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Tu carrito está vacío.");
            return "redirect:/catalogo"; 
        }

        // Instanciamos el pedido con la nueva estructura
        pedido nuevoPedido = new pedido();
        nuevoPedido.setFecha(LocalDateTime.now());
        nuevoPedido.setUsuario(user); // Seteamos el objeto completo usuario sin problemas

        BigDecimal subtotal = BigDecimal.ZERO;
        for (carrito item : itemsCarrito) {
            subtotal = subtotal.add(item.getSubtotal());
        }

        BigDecimal iva = subtotal.multiply(new BigDecimal("0.15"));
        BigDecimal envio = new BigDecimal("15.00");
        BigDecimal totalCalculado = subtotal.add(iva).add(envio);

        // Asignamos los BigDecimals exactos que añadiste a tu entidad pedido
        nuevoPedido.setSubtotal(subtotal.setScale(2, RoundingMode.HALF_UP));
        nuevoPedido.setIva(iva.setScale(2, RoundingMode.HALF_UP));
        nuevoPedido.setEnvio(envio.setScale(2, RoundingMode.HALF_UP));
        nuevoPedido.setTotal(totalCalculado.setScale(2, RoundingMode.HALF_UP));

        // Mapeamos los ítems agregándolos bidireccionalmente usando tu método 'agregarDetalle'
        for (carrito item : itemsCarrito) {
            Detallepedido detalle = new Detallepedido();
            detalle.setProducto(item.getProducto());
            detalle.setCantidad(item.getCantidad());
            detalle.setPrecioUnit(item.getProducto().getPrecio().doubleValue());
            
            // Llama a tu método helper que se encargará de enlazar el pedido automáticamente
            nuevoPedido.agregarDetalle(detalle);
        }

        // GUARDADO ÚNICO: Al usar cascade = CascadeType.ALL, se inserta el pedido y todos sus detalles juntos
        pedido pedidoGuardado = pedidoRepository.save(nuevoPedido);

        // Limpiar el estado de tu servicio en memoria
        carritoModeloService.vaciarCarrito();

        redirectAttributes.addFlashAttribute("exito", "¡Tu pedido en Puntada Creativa ha sido procesado con éxito!");
        
        return "redirect:/carrito/confirmacion/" + pedidoGuardado.getId();
    }

    // 3. PÁGINA DE CONFIRMACIÓN SEGURA
    @GetMapping("/confirmacion/{id}")
    public String confirmacionPedido(@PathVariable Long id, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        usuario user = (usuario) session.getAttribute("usuarioLogueado");
        if (user == null) {
            return "redirect:/login";
        }

        Optional<pedido> pedidoOpt = pedidoRepository.findById(id);
        if (pedidoOpt.isPresent()) {
            pedido ped = pedidoOpt.get();

            // Validación usando la relación orientada a objetos (Long vs int)
            if (ped.getUsuario().getId() != user.getId()) { 
                redirectAttributes.addFlashAttribute("error", "No tienes autorización para visualizar este pedido.");
                return "redirect:/catalogo";
            }

            model.addAttribute("pedido", ped);
            model.addAttribute("usuarioLogueado", user);
            return "orden"; 
        }

        return "redirect:/catalogo";
    }
}