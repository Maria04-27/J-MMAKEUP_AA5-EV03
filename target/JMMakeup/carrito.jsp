<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="util.Formato" %>
<%@ page import="java.util.List" %>
<%@ page import="modelo.DetalleCarrito" %>

<!DOCTYPE html>
<html lang="es">

<head>
    <!-- CONFIGURACIÓN DE LA PÁGINA -->
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Carrito de compras | J&M Makeup</title>
    <!-- BOOTSTRAP 5 -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- BOOTSTRAP ICONS -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <!-- CSS DEL PROYECTO -->
    <link rel="stylesheet" href="css/estilos.css">
</head>

<body>
<!--  BARRA DE NAVEGACIÓN -->
<nav class="navbar navbar-expand-lg sticky-top">
    <div class="container">
        <!-- LOGO -->
        <a class="navbar-brand" href="index.jsp">
            <img src="img/logo.png" alt="J&M Makeup" height="36">
        </a>
        <!-- BOTÓN MENÚ MÓVIL -->
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#menuPrincipal">
            <span class="navbar-toggler-icon"></span>
        </button>
        <!-- MENÚ PRINCIPAL -->
        <div class="collapse navbar-collapse" id="menuPrincipal">
            <ul class="navbar-nav mx-auto mb-2 mb-lg-0">
                <!-- INICIO -->
                <li class="nav-item">
                    <a class="nav-link" href="index.jsp">Inicio</a>
                </li>
                <!-- PRODUCTOS -->
                <li class="nav-item">
                    <a class="nav-link" href="ProductoServlet?accion=listar">Productos</a>
                </li>
                <!-- OFERTAS -->
                <li class="nav-item">
                    <a class="nav-link" href="ProductoServlet?accion=ofertas">Ofertas</a>
                </li>
                <!-- CONTACTO -->
                <li class="nav-item">
                    <a class="nav-link" href="contacto.jsp">Contacto</a>
                </li>
            </ul>
            <!-- SESIÓN DEL USUARIO -->
            <%
                // Verificamos si hay una sesión activa con un usuario logueado.
                String nombreUsuario = (session != null) ? (String) session.getAttribute("nombreUsuario") : null;
            %>
            <%
                if (nombreUsuario != null) {
            %>
            <!-- USUARIO CON SESIÓN INICIADA -->
            <div class="dropdown me-2">
                <button class="btn btn-outline-pink dropdown-toggle" type="button" data-bs-toggle="dropdown">
                    <i class="bi bi-person-circle"></i>
                    <%= nombreUsuario %>
                </button>
                <ul class="dropdown-menu dropdown-menu-end">
                    <li>
                        <a class="dropdown-item" href="PedidoServlet?accion=misPedidos">
                            <i class="bi bi-bag-check"></i>
                            Mis pedidos
                        </a>
                    </li>
                    <li><hr class="dropdown-divider"></li>
                    <li>
                        <a class="dropdown-item" href="LogoutServlet">
                            <i class="bi bi-box-arrow-right"></i>
                            Cerrar sesión
                        </a>
                    </li>
                </ul>
            </div>
            <%
            } else {
            %>
            <!-- SIN SESIÓN -->
            <a href="login.jsp" class="btn btn-outline-pink me-2">
                <i class="bi bi-person"></i>
                Iniciar sesión
            </a>
            <%
                }
            %>
            <!-- CARRITO -->
            <a href="CarritoServlet?accion=ver" class="btn btn-pink">
                <i class="bi bi-cart3"></i>
                Carrito
            </a>
        </div>
    </div>
</nav>
<!-- ENCABEZADO -->
<section class="container">
    <div class="section-title">
        <h2>Mi carrito de compras</h2>
        <p>Revisa los productos que deseas comprar.</p>
    </div>
</section>
<!-- CONTENIDO PRINCIPAL -->
<main class="container mb-5">
    <!-- MENSAJE DE PRODUCTO AGREGADO -->
    <%
        String mensaje = request.getParameter("mensaje");
        if ("agregado".equals(mensaje)) {
    %>
    <div class="alert alert-success alert-dismissible fade show" role="alert">
        <i class="bi bi-check-circle"></i>
        Producto agregado correctamente al carrito.
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    </div>
    <%
        }
    %>
    <!-- MENSAJE DE PRODUCTO ELIMINADO -->
    <%
        if ("eliminado".equals(mensaje)) {
    %>
    <div class="alert alert-success alert-dismissible fade show" role="alert">
        <i class="bi bi-check-circle"></i>
        Producto eliminado del carrito.
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    </div>
    <%
        }
    %>
    <!--  MENSAJE DE CARRITO VACÍO -->
    <%
        if ("vaciado".equals(mensaje)) {
    %>
    <div class="alert alert-success alert-dismissible fade show" role="alert">
        <i class="bi bi-check-circle"></i>
        El carrito ha sido vaciado correctamente.
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    </div>
    <%
        }
    %>
    <!-- OBTENER CARRITO -->
    <%
        /*
         * Obtiene la lista del carrito
         * enviada por CarritoServlet.
         */
        List<DetalleCarrito> carrito = (List<DetalleCarrito>)
                request.getAttribute("carrito");
    %>
    <%
        /*
         * Verificar si el carrito tiene productos.
         */
        if (carrito != null && !carrito.isEmpty()) {
    %>
    <div class="row">
        <!-- LISTA DE PRODUCTOS -->
        <div class="col-lg-8 mb-4">
            <div class="card shadow-sm">
                <div class="card-body">
                    <h4 class="mb-4">
                        <i class="bi bi-cart3"></i>
                        Productos seleccionados
                    </h4>
                    <!-- RECORRER CARRITO -->
                    <%
                        double total = 0;
                        for (DetalleCarrito item : carrito) {
                            /*
                             * Calcular subtotal.
                             */
                            double subtotal = item.getPrecio()* item.getCantidad();
                            total += subtotal;
                    %>
                    <!-- PRODUCTO -->
                    <div class="row align-items-center border-bottom py-3">
                        <!-- IMAGEN -->
                        <div class="col-md-2 text-center">
                            <i class="bi bi-box" style="font-size: 50px; color: #d53384;"></i>
                        </div>
                        <!-- INFORMACIÓN -->
                        <div class="col-md-4">
                            <h5><%= item.getNombre() %></h5>
                            <p class="text-muted mb-0">Precio unitario: $<%= Formato.moneda(item.getPrecio()) %></p>
                        </div>
                        <!-- ACTUALIZAR CANTIDAD -->
                        <div class="col-md-3">
                            <form action="CarritoServlet" method="post">
                                <input type="hidden" name="accion" value="actualizar">
                                <input type="hidden" name="idProducto" value="<%= item.getIdProducto() %>">
                                <div class="input-group">
                                    <button type="button" class="btn btn-outline-secondary" onclick="disminuirCantidad(this)">-</button>
                                    <input type="number" name="cantidad" class="form-control text-center" value="<%= item.getCantidad() %>" min="1">
                                    <button type="button" class="btn btn-outline-secondary" onclick="aumentarCantidad(this)">+</button>
                                </div>
                                <button type="submit" class="btn btn-sm btn-outline-pink mt-2 w-100">Actualizar</button>
                            </form>
                        </div>
                        <!-- SUBTOTAL -->
                        <div class="col-md-2 text-end">
                            <strong>$<%= Formato.moneda(subtotal) %></strong>
                        </div>
                        <!-- ELIMINAR -->
                        <div class="col-md-1 text-end">
                            <a href="CarritoServlet?accion=eliminar&idProducto=<%= item.getIdProducto() %>" class="btn btn-danger btn-sm" onclick="return confirmarEliminar();">
                                <i class="bi bi-trash"></i>
                            </a>
                        </div>
                    </div>
                    <%
                        }
                    %>
                </div>
            </div>
            <!-- BOTONES INFERIORES -->
            <div class="d-flex justify-content-between mt-4">
                <!-- CONTINUAR COMPRANDO -->
                <a href="ProductoServlet?accion=listar" class="btn btn-outline-pink">
                    <i class="bi bi-arrow-left"></i>
                    Continuar comprando
                </a>
                <!-- VACIAR CARRITO -->
                <a href="CarritoServlet?accion=vaciar" class="btn btn-danger" onclick="return confirmarVaciarCarrito();">
                    <i class="bi bi-trash"></i>
                    Vaciar carrito
                </a>
            </div>
        </div>
        <!-- RESUMEN DE COMPRA -->
        <div class="col-lg-4">
            <div class="card shadow-sm">
                <div class="card-body">
                    <h4 class="mb-4">Resumen de compra</h4>
                    <!-- SUBTOTAL -->
                    <div class="d-flex justify-content-between mb-3">
                        <span>Subtotal</span>
                        <strong>$<%= Formato.moneda(total) %></strong>
                    </div>
                    <!-- ENVÍO -->
                    <div class="d-flex justify-content-between mb-3">
                        <span>Envío</span>
                        <strong>Gratis</strong>
                    </div>
                    <hr>
                    <!-- TOTAL -->
                    <div class="d-flex justify-content-between mb-4">
                        <h5>Total</h5>
                        <h5 style="color: #d53384;">$<%= Formato.moneda(total) %></h5>
                    </div>
                    <!-- REALIZAR PEDIDO -->
                    <div class="d-grid">
                        <a href="PedidoServlet?accion=crear" class="btn btn-pink" onclick="return confirmarPedido();">
                            <i class="bi bi-credit-card"></i>
                            Realizar pedido
                        </a>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <%
    } else {
    %>
    <!-- CARRITO VACÍO -->
    <div class="text-center py-5">
        <i class="bi bi-cart-x" style="font-size: 100px; color: #d53384;"></i>
        <h3 class="mt-4">Tu carrito está vacío</h3>
        <p class="text-muted">Explora nuestros productos y agrega tus favoritos al carrito.</p>
        <a href="ProductoServlet?accion=listar" class="btn btn-pink mt-3">
            <i class="bi bi-bag"></i>
            Ver productos
        </a>
    </div>
    <%
        }
    %>
</main>
<!-- PIE DE PÁGINA -->
<footer>
    <div class="container">
        <p>&copy; 2026 J&M Makeup. Todos los derechos reservados.</p>
        <p>Tu tienda virtual de maquillaje.</p>
    </div>
</footer>
<!--  BOOTSTRAP JAVASCRIPT -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<!-- JAVASCRIPT DEL PROYECTO -->
<script src="js/scripts.js"></script>
<!-- FUNCIONES DEL CARRITO -->
<script>
    /**
     * Aumenta la cantidad del producto.
     */
    function aumentarCantidad(boton) {
        const input = boton.parentElement.querySelector('input[name="cantidad"]');
        let cantidad = parseInt(input.value);
        input.value = cantidad + 1;
    }
    /**
     * Disminuye la cantidad del producto.
     */
    function disminuirCantidad(boton) {
        const input = boton.parentElement.querySelector('input[name="cantidad"]');
        let cantidad = parseInt(input.value);
        if (cantidad > 1) {
            input.value = cantidad - 1;
        }
    }
    /**
     * Confirma la eliminación
     * de un producto.
     */
    function confirmarEliminar() {
        return confirm("¿Deseas eliminar este producto del carrito?");
    }
    /**
     * Confirma si el usuario desea
     * vaciar todo el carrito.
     */
    function confirmarVaciarCarrito() {
        return confirm("¿Estás seguro de que deseas vaciar el carrito?");
    }
    /**
     * Confirma la creación del pedido.
     */
    function confirmarPedido() {
        return confirm("¿Deseas continuar con la realización del pedido?");
    }
</script>
</body>
</html>