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

    @GetMapping
    public String verCarrito(HttpSession session, Model model) {
        model.addAttribute("usuarioLogueado", session.getAttribute("usuarioLogueado"));

        this.productosVisuales = carritoModeloService.getItems();
        this.totalVisual = carritoModeloService.getTotal();

        BigDecimal subtotal = this.totalVisual;
        BigDecimal iva = subtotal.multiply(new BigDecimal("0.15"));
        BigDecimal envio = subtotal.compareTo(BigDecimal.ZERO) > 0 ? new BigDecimal("15.00") : BigDecimal.ZERO;
        BigDecimal totalFinal = subtotal.add(iva).add(envio);

        model.addAttribute("carrito", this.productosVisuales);
        model.addAttribute("subtotal", subtotal.setScale(2, RoundingMode.HALF_UP));
        model.addAttribute("iva", iva.setScale(2, RoundingMode.HALF_UP));
        model.addAttribute("envio", envio.setScale(2, RoundingMode.HALF_UP));
        model.addAttribute("total", totalFinal.setScale(2, RoundingMode.HALF_UP));

        return "carrito";
    }

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

        pedido nuevoPedido = new pedido();
        nuevoPedido.setFecha(LocalDateTime.now());
        nuevoPedido.setUsuario(user);

        BigDecimal subtotal = BigDecimal.ZERO;
        for (carrito item : itemsCarrito) {
            subtotal = subtotal.add(item.getSubtotal());
        }

        BigDecimal iva = subtotal.multiply(new BigDecimal("0.15"));
        BigDecimal envio = new BigDecimal("15.00");
        BigDecimal totalCalculado = subtotal.add(iva).add(envio);

        nuevoPedido.setSubtotal(subtotal.setScale(2, RoundingMode.HALF_UP));
        nuevoPedido.setIva(iva.setScale(2, RoundingMode.HALF_UP));
        nuevoPedido.setEnvio(envio.setScale(2, RoundingMode.HALF_UP));
        nuevoPedido.setTotal(totalCalculado.setScale(2, RoundingMode.HALF_UP));

        for (carrito item : itemsCarrito) {
            Detallepedido detalle = new Detallepedido();
            detalle.setProducto(item.getProducto());
            detalle.setCantidad(item.getCantidad());
            detalle.setPrecioUnit(item.getProducto().getPrecio().doubleValue());
            nuevoPedido.agregarDetalle(detalle);
        }

        pedido pedidoGuardado = pedidoRepository.save(nuevoPedido);
        carritoModeloService.vaciarCarrito();

        redirectAttributes.addFlashAttribute("exito", "¡Tu pedido ha sido procesado!");
        return "redirect:/carrito/confirmacion/" + pedidoGuardado.getId();
    }

    @GetMapping("/confirmacion/{id}")
    public String confirmacionPedido(@PathVariable Long id, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        usuario user = (usuario) session.getAttribute("usuarioLogueado");
        if (user == null) return "redirect:/login";

        Optional<pedido> pedidoOpt = pedidoRepository.findById(id);
        if (pedidoOpt.isPresent()) {
            pedido ped = pedidoOpt.get();
            // Corrección: uso de .equals() para objetos Long
            if (!ped.getUsuario().getId().equals(user.getId())) {
                redirectAttributes.addFlashAttribute("error", "No autorizado.");
                return "redirect:/catalogo";
            }
            model.addAttribute("pedido", ped);
            return "orden";
        }
        return "redirect:/catalogo";
    }

    @GetMapping("/confirmar")
    public String confirmarCompra(HttpSession session, Model model) {
        usuario user = (usuario) session.getAttribute("usuarioLogueado");
        if (user == null) return "redirect:/login";
        model.addAttribute("usuarioLogueado", user);
        return "confirmar";
    }
}