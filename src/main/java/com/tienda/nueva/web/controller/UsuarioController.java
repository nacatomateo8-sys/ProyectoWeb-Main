package com.tienda.nueva.web.controller;

import com.tienda.nueva.web.model.usuario; 
import com.tienda.nueva.web.repository.UsuarioRepositorio; 
import jakarta.servlet.http.HttpSession; 
import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.stereotype.Controller; 
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.Optional; 

@Controller 
public class UsuarioController {

    @Autowired 
    private UsuarioRepositorio usuarioRepository;

    // --- RUTA PROTEGIDA ---
   @GetMapping("/usuario/historial")
public String verHistorial(HttpSession session, org.springframework.ui.Model model) {
    usuario user = (usuario) session.getAttribute("usuarioLogueado");
    
    // 1. Seguridad
    if (user == null) {
        return "redirect:/login";
    }
    
    // 2. Carga de datos (Asumiendo que tienes un repositorio de pedidos)
    // model.addAttribute("pedidos", pedidoRepository.findByUsuarioId(user.getId()));
    
    return "historial_compras"; 
}

    // --- LOGIN ---
    @GetMapping("/login") 
    public String mostrarLogin() {
        return "login"; 
    }

    @PostMapping("/login/async")
    @ResponseBody
    public String procesarLoginAsync(@RequestParam("username") String usernameOrEmail,
                                     @RequestParam("password") String password,
                                     HttpSession session) {
        try {
            Optional<usuario> userOpt = usuarioRepository.findByEmail(usernameOrEmail);
            if (userOpt.isEmpty()) { 
                userOpt = usuarioRepository.findByUsername(usernameOrEmail); 
            }

            if (userOpt.isPresent() && userOpt.get().getPassword().equals(password)) {
                session.setAttribute("usuarioLogueado", userOpt.get());
                return "OK"; 
            }
            return "ERROR_CREDENCIALES";
        } catch (Exception e) {
            return "ERROR_CONEXION";
        }
    }

    // --- REGISTRO ---
    @PostMapping("/registro/async")
    @ResponseBody
    public String procesarRegistroAsync(@RequestParam("nombre") String nombre,
                                        @RequestParam("username") String username,
                                        @RequestParam("email") String email,
                                        @RequestParam("password") String password,
                                        HttpSession session) {
        try {
            if (usuarioRepository.findByEmail(email).isPresent()) return "EMAIL_REPETIDO";
            if (usuarioRepository.findByUsername(username).isPresent()) return "USERNAME_REPETIDO";

            usuario nuevoUsuario = new usuario();
            nuevoUsuario.setNombre(nombre);
            nuevoUsuario.setUsername(username);
            nuevoUsuario.setEmail(email);
            nuevoUsuario.setPassword(password);
            nuevoUsuario.setActivo(1);
            nuevoUsuario.setRol("CLIENTE");
            nuevoUsuario.setCreado(LocalDateTime.now());

            usuario usuarioGuardado = usuarioRepository.save(nuevoUsuario);
            session.setAttribute("usuarioLogueado", usuarioGuardado);
            return "historial_compras";
        } catch (Exception e) {
            return "ERROR_BD";
        }
    }

    @GetMapping("/logout") 
    public String cerrarSesion(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}