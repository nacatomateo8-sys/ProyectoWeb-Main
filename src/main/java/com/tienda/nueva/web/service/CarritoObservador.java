package com.tienda.nueva.web.service;

import com.tienda.nueva.web.model.carrito; // <--- Importa en minúscula
import java.math.BigDecimal;
import java.util.List;

public interface CarritoObservador {
    // La lista debe ser de 'carrito' (en minúscula) para que coincida perfectamente
    void mapearCambiosAVista(List<carrito> items, BigDecimal total);
}