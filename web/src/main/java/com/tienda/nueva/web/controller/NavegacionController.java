package com.tienda.nueva.web.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NavegacionController {

    // Método privado para compartir el estado del usuario logueado con las plantillas HTML
    private void agregarAtributosSesion(HttpSession session, Model model) {
        model.addAttribute("usuarioLogueado", session.getAttribute("usuarioLogueado"));
    }

    // 1. RUTA RAÍZ (/) - Se ejecuta al entrar directamente a http://localhost:8080/
    @GetMapping("/")
    public String home(HttpSession session, Model model) {
        agregarAtributosSesion(session, model);
        return "index"; // Busca index.html
    }

    // 2. RUTA INDEX (/index) - Por si se digita manualmente en la barra de direcciones
    @GetMapping("/index")
    public String index(HttpSession session, Model model) {
        agregarAtributosSesion(session, model);
        return "index"; // Busca index.html
    }

    // 3. RUTA NOSOTROS (/nosotros) - Carga la historia y valores del taller familiar
    @GetMapping("/nosotros")
    public String nosotros(HttpSession session, Model model) {
        agregarAtributosSesion(session, model);
        return "nosotros"; // Busca nosotros.html en la carpeta templates
    }

    // 4. RUTA CATÁLOGO (/catalogo) - Muestra los productos disponibles
    @GetMapping("/catalogo")
    public String catalogo(HttpSession session, Model model) {
        agregarAtributosSesion(session, model);
        return "catalogo"; // Busca catalogo.html
    }

    // 5. RUTA CONTACTO (/contacto) - Formulario o datos alternos de atención
    @GetMapping("/contacto")
    public String contacto(HttpSession session, Model model) {
        agregarAtributosSesion(session, model);
        return "contacto"; // Busca contacto.html
    }

    // 6. RUTA UBICACIÓN (/ubicacion) - Muestra el mapa de Sangolquí y horarios del taller
    @GetMapping("/ubicacion")
    public String ubicacion(HttpSession session, Model model) {
        agregarAtributosSesion(session, model);
        return "ubicacion"; // Busca ubicacion.html en la carpeta templates
    }
}