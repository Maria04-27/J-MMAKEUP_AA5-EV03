<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="util.Formato" %>
<%@ page import="java.util.List" %>
<%@ page import="modelo.Producto" %>

<!DOCTYPE html>
<html lang="es">

<head>
    <!-- CONFIGURACIÓN DE LA PÁGINA -->
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Productos | J&M Makeup</title>
    <!-- BOOTSTRAP 5 -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- BOOTSTRAP ICONS -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <!-- CSS DEL PROYECTO -->
    <link rel="stylesheet" href="css/estilos.css">
</head>
<body>
<!-- BARRA DE NAVEGACIÓN -->
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
        <!-- MENÚ -->
        <div class="collapse navbar-collapse" id="menuPrincipal">
            <!-- OPCIONES -->
            <ul class="navbar-nav mx-auto mb-2 mb-lg-0">
                <li class="nav-item">
                    <a class="nav-link" href="index.jsp">Inicio</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link active" href="ProductoServlet?accion=listar">Productos</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="ProductoServlet?accion=ofertas">Ofertas</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="contacto.jsp">Contacto</a>
                </li>
            </ul>
            <!-- BUSCADOR -->
            <form class="d-flex me-3" role="search" action="ProductoServlet" method="get">
                <input type="hidden" name="accion" value="buscar">
                <input class="form-control" type="search" name="busqueda" placeholder="Buscar producto..." aria-label="Buscar producto">
                <button class="btn btn-pink ms-2" type="submit">
                    <i class="bi bi-search"></i>
                </button>
            </form>
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
<!-- ENCABEZADO DEL CATÁLOGO -->
<section class="container">
    <div class="section-title">
        <h2>Nuestros productos</h2>
        <p>Descubre nuestra colección de maquillaje y encuentra tus productos favoritos.</p>
    </div>
</section>
<!--  CONTENEDOR PRINCIPAL -->
<main class="container mb-5">
    <!--  FILTROS -->
    <div class="row mb-4">
        <!-- BUSCADOR VISUAL -->
        <div class="col-md-6 mb-3">
            <div class="input-group">
                <span class="input-group-text">
                    <i class="bi bi-search"></i>
                </span>
                <input type="text" class="form-control" placeholder="Buscar producto..." onkeyup="buscarProducto(this.value)">
            </div>
        </div>
        <!-- CATEGORÍA -->
        <div class="col-md-6 mb-3">
            <select class="form-select" id="filtroCategoria">
                <option value="">Todas las categorías</option>
                <option value="Rostro">Rostro</option>
                <option value="Ojos">Ojos</option>
                <option value="Labios">Labios</option>
            </select>
        </div>
    </div>
    <!-- MENSAJE DE ÉXITO -->
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
    <!-- MENSAJE DE ERROR -->
    <%
        String error = request.getParameter("error");
        if ("producto".equals(error)) {
    %>
    <div class="alert alert-danger alert-dismissible fade show" role="alert">
        <i class="bi bi-exclamation-triangle"></i>
        No fue posible agregar el producto al carrito.
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    </div>
    <%
        }
    %>
    <!-- LISTA DE PRODUCTOS -->
    <div class="row g-4">
        <%
            /*
             * Obtiene la lista de productos enviada
             * por ProductoServlet.
             */
            List<Producto> productos = (List<Producto>)
                    request.getAttribute("productos");
            /*
             * Verifica si existen productos.
             */
            if (productos != null && !productos.isEmpty()) {
                /*
                 * Recorre cada producto.
                 */
                for (Producto producto : productos) {
        %>
        <!-- TARJETA DE PRODUCTO -->
        <div class="col-sm-6 col-md-4 col-lg-3">
            <div class="product-card">
                <!-- IMAGEN -->
                <%
                    if (producto.getImagen() != null && !producto.getImagen().isEmpty()) {
                %>
                <img src="img/<%= producto.getImagen() %>" alt="<%= producto.getNombre() %>" class="img-fluid">
                <%
                } else {
                %>
                <div class="text-center p-5">
                    <i class="bi bi-box" style="font-size: 80px; color: #d53384;"></i>
                </div>
                <%
                    }
                %>
                <!-- INFORMACIÓN -->
                <div class="card-body">
                    <!-- NOMBRE -->
                    <h5 class="product-name"><%= producto.getNombre() %></h5>
                    <!-- DESCRIPCIÓN -->
                    <p class="product-description"><%= producto.getDescripcion() %></p>
                    <!-- PRECIO -->
                    <p class="product-price">$<%= Formato.moneda(producto.getPrecio()) %></p>
                    <!-- STOCK -->
                    <%
                        if (producto.getStock() > 0) {
                    %>
                    <p class="text-success">
                        <i class="bi bi-check-circle"></i>
                        Disponible
                    </p>
                    <%
                    } else {
                    %>
                    <p class="text-danger">
                        <i class="bi bi-x-circle"></i>
                        Agotado
                    </p>
                    <%
                        }
                    %>
                    <!-- BOTÓN AGREGAR AL CARRITO -->
                    <%
                        if (producto.getStock() > 0) {
                    %>
                    <a href="CarritoServlet?accion=agregar&idProducto=<%= producto.getIdProducto() %>" class="btn btn-pink w-100">
                        <i class="bi bi-cart-plus"></i>
                        Agregar al carrito
                    </a>
                    <%
                    } else {
                    %>
                    <button type="button" class="btn btn-secondary w-100" disabled>Producto agotado</button>
                    <%
                        }
                    %>
                </div>
            </div>
        </div>
        <%
            }
        } else {
        %>
        <!-- SIN PRODUCTOS -->
        <div class="col-12">
            <div class="alert alert-info text-center">
                <i class="bi bi-info-circle" style="font-size: 30px;"></i>
                <p class="mt-2 mb-0">Actualmente no hay productos disponibles.</p>
            </div>
        </div>
        <%
            }
        %>
    </div>
    <!-- BOTÓN CARRITO -->
    <div class="text-center mt-5">
        <a href="CarritoServlet?accion=ver" class="btn btn-outline-pink">
            <i class="bi bi-cart3"></i>
            Ver mi carrito
        </a>
    </div>
</main>
<!-- PIE DE PÁGINA -->
<footer>
    <div class="container">
        <p>&copy; 2026 J&M Makeup. Todos los derechos reservados.</p>
        <p>Tu tienda virtual de maquillaje.</p>
    </div>
</footer>
<!-- BOOTSTRAP JAVASCRIPT -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<!-- JAVASCRIPT DEL PROYECTO -->
<script src="js/scripts.js"></script>
</body>
</html>