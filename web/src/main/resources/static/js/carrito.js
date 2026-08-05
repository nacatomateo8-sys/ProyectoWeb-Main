/**
 * Sistema de Carrito Frontend
 * Maneja agregar productos, visualizar carrito y sincronizar con backend
 */

// Estado del carrito en memoria
let carrito = JSON.parse(localStorage.getItem('carrito')) || [];

/**
 * Agregar producto al carrito
 * @param {string} nombre - Nombre del producto
 * @param {number} precio - Precio unitario
 * @param {number} cantidad - Cantidad a agregar
 */
function addToCart(nombre, precio, cantidad) {
    const existe = carrito.find(item => item.nombre === nombre);
    
    if (existe) {
        existe.cantidad += cantidad;
    } else {
        carrito.push({
            nombre: nombre,
            precio: precio,
            cantidad: cantidad,
            subtotal: precio * cantidad
        });
    }
    
    guardarCarrito();
    actualizarVistaCarrito();
    mostrarToast(`✓ ${nombre} agregado al carrito`);
}

/**
 * Guardar carrito en localStorage
 */
function guardarCarrito() {
    localStorage.setItem('carrito', JSON.stringify(carrito));
    actualizarContadorFAB();
}

/**
 * Actualizar contador en el botón flotante
 */
function actualizarContadorFAB() {
    const count = carrito.reduce((sum, item) => sum + item.cantidad, 0);
    const fabCount = document.getElementById('fabCount');
    if (fabCount) {
        fabCount.textContent = count;
    }
}

/**
 * Actualizar vista del carrito desplegable
 */
function actualizarVistaCarrito() {
    const cartItemsArea = document.getElementById('cartItemsArea');
    const cartTotal = document.getElementById('cartTotal');
    
    if (!cartItemsArea) return;
    
    if (carrito.length === 0) {
        cartItemsArea.innerHTML = '<p style="padding:20px; text-align:center; color:#999;">Tu carrito está vacío</p>';
        cartTotal.textContent = '$0.00';
        return;
    }
    
    let html = '';
    let total = 0;
    
    carrito.forEach((item, index) => {
        const subtotal = item.precio * item.cantidad;
        total += subtotal;
        html += `
            <div class="cart-item">
                <div class="ci-info">
                    <strong>${item.nombre}</strong>
                    <span class="ci-price">$${item.precio.toFixed(2)}</span>
                </div>
                <div class="ci-qty">x${item.cantidad}</div>
                <button class="ci-remove" onclick="removeFromCart(${index})">✕</button>
            </div>
        `;
    });
    
    cartItemsArea.innerHTML = html;
    cartTotal.textContent = '$' + total.toFixed(2);
}

/**
 * Remover producto del carrito
 */
function removeFromCart(index) {
    if (index >= 0 && index < carrito.length) {
        const nombre = carrito[index].nombre;
        carrito.splice(index, 1);
        guardarCarrito();
        actualizarVistaCarrito();
        mostrarToast(`✓ ${nombre} eliminado del carrito`);
    }
}

/**
 * Abrir carrito desplegable
 */
function openCart() {
    const backdrop = document.getElementById('cartBackdrop');
    const drawer = document.getElementById('cartDrawer');
    if (backdrop) backdrop.classList.add('open');
    if (drawer) drawer.classList.add('open');
    actualizarVistaCarrito();
}

/**
 * Cerrar carrito desplegable
 */
function closeCart() {
    const backdrop = document.getElementById('cartBackdrop');
    const drawer = document.getElementById('cartDrawer');
    if (backdrop) backdrop.classList.remove('open');
    if (drawer) drawer.classList.remove('open');
}

/**
 * Mostrar notificación (toast)
 */
function mostrarToast(mensaje) {
    const toast = document.getElementById('toast');
    if (!toast) return;
    
    toast.textContent = mensaje;
    toast.classList.add('show');
    
    setTimeout(() => {
        toast.classList.remove('show');
    }, 3000);
}

/**
 * Inicializar carrito al cargar la página
 */
document.addEventListener('DOMContentLoaded', function() {
    actualizarContadorFAB();
    actualizarVistaCarrito();
});
