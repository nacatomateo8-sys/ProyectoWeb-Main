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

    @GetMapping("/login") 
    public String mostrarLogin() {
        return "login"; 
    }

    // LOGIN ASÍNCRONO OPTIMIZADO (Acepta Correo o Username)
    @PostMapping("/login/async")
    @ResponseBody
    public String procesarLoginAsync(@RequestParam("username") String usernameOrEmail,
                                     @RequestParam("password") String password,
                                     HttpSession session) {
        try {
            // 1. Intenta buscar primero por Correo Electrónico
            Optional<usuario> userOpt = usuarioRepository.findByEmail(usernameOrEmail);
            
            // 2. Si no lo encuentra por Email, intenta buscar por Username
            if (userOpt.isEmpty()) { 
                userOpt = usuarioRepository.findByUsername(usernameOrEmail); 
            }

            // 3. Verifica si el usuario existe y si la contraseña coincide
            if (userOpt.isPresent() && userOpt.get().getPassword().equals(password)) {
                // Guarda el objeto completo de la base de datos en la sesión HTTP
                session.setAttribute("usuarioLogueado", userOpt.get());
                return "OK"; 
            }
            return "ERROR_CREDENCIALES";
        } catch (Exception e) {
            return "ERROR_CONEXION";
        }
    }

    // REGISTRO ASÍNCRONO CORREGIDO Y SEGURO (Previene el ERROR_BD)
    @PostMapping("/registro/async")
    @ResponseBody
    public String procesarRegistroAsync(@RequestParam("nombre") String nombre,
                                        @RequestParam("username") String username,
                                        @RequestParam("email") String email,
                                        @RequestParam("password") String password,
                                        HttpSession session) {
        try {
            // Validación de duplicados por Email
            if (usuarioRepository.findByEmail(email).isPresent()) {
                return "EMAIL_REPETIDO";
            }
            
            // Validación de duplicados por Username
            if (usuarioRepository.findByUsername(username).isPresent()) {
                return "USERNAME_REPETIDO";
            }

            // Instancia y mapeo del modelo
            usuario nuevoUsuario = new usuario();
            nuevoUsuario.setNombre(nombre);
            nuevoUsuario.setUsername(username);
            nuevoUsuario.setEmail(email);
            nuevoUsuario.setPassword(password);
            
            // ASIGNACIONES OBLIGATORIAS PARA EVITAR QUE RECHACE LA INSERCIÓN MYSQL:
            nuevoUsuario.setActivo(1);                     // Asigna estado 1 (Activo)
            nuevoUsuario.setRol("CLIENTE");                // Asigna rol por defecto
            nuevoUsuario.setCreado(LocalDateTime.now());   // Asigna marca de tiempo compatible con datetime(6)

            // Guarda el nuevo usuario en MySQL de forma exitosa
            usuario usuarioGuardado = usuarioRepository.save(nuevoUsuario);
            
            // Inicia sesión automáticamente tras registrarse
            session.setAttribute("usuarioLogueado", usuarioGuardado);
            return "OK";
        } catch (Exception e) {
            // Imprime detalladamente el error en la terminal negra de tu VS Code para auditar la BD
            System.out.println("❌ ERROR REAL EN LA BASE DE DATOS: " + e.getMessage());
            e.printStackTrace(); 
            return "ERROR_BD";
        }
    }

    @GetMapping("/logout") 
    public String cerrarSesion(HttpSession session) {
        session.invalidate(); // Destruye la sesión por completo de manera segura
        return "redirect:/";
    }
}