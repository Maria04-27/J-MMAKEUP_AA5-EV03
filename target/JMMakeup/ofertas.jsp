<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="modelo.Producto" %>
<%@ page import="util.Formato" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Ofertas | J&M Makeup</title>
    <!-- Bootstrap -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Bootstrap Icons -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <!-- CSS del proyecto -->
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
        <!-- BOTÓN PARA MÓVILES -->
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#menuPrincipal">
            <span class="navbar-toggler-icon"></span>
        </button>
        <!-- MENÚ -->
        <div class="collapse navbar-collapse" id="menuPrincipal">
            <ul class="navbar-nav mx-auto">
                <li class="nav-item">
                    <a class="nav-link" href="index.jsp">Inicio</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="ProductoServlet?accion=listar">Productos</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link active" href="ProductoServlet?accion=ofertas">Ofertas</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="contacto.jsp">Contacto</a>
                </li>
            </ul>
            <!-- SESIÓN DEL USUARIO -->
            <%
                String nombreUsuario = (session != null) ? (String) session.getAttribute("nombreUsuario") : null;
            %>
            <%
                if (nombreUsuario != null) {
            %>
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
            <a href="login.jsp" class="btn btn-outline-pink me-2">
                <i class="bi bi-person"></i>
                Iniciar sesión
            </a>
            <%
                }
            %>
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
        <h2>Ofertas especiales</h2>
        <p>Aprovecha nuestras promociones de maquillaje.</p>
    </div>
</section>
<!-- PRODUCTOS EN OFERTA -->
<main class="container mb-5">
    <%
        // Obtenemos la lista de ofertas enviada por ProductoServlet.
        List<Producto> productos = (List<Producto>) request.getAttribute("productos");
    %>
    <%
        if (productos != null && !productos.isEmpty()) {
    %>
    <div class="row g-4">
        <%
            for (Producto producto : productos) {
        %>
        <!-- PRODUCTO EN OFERTA -->
        <div class="col-md-6 col-lg-4">
            <div class="card offer-card">
                <div class="card-body text-center">
                    <!-- IMAGEN -->
                    <%
                        if (producto.getImagen() != null && !producto.getImagen().isEmpty()) {
                    %>
                    <img src="img/<%= producto.getImagen() %>" alt="<%= producto.getNombre() %>" class="img-fluid mb-3">
                    <%
                    } else {
                    %>
                    <i class="bi bi-stars" style="font-size:70px; color:#d53384;"></i>
                    <%
                        }
                    %>
                    <h4 class="mt-3"><%= producto.getNombre() %></h4>
                    <p class="text-muted"><%= producto.getDescripcion() %></p>
                    <!-- PRECIOS -->
                    <p>
                        <span class="old-price">$<%= Formato.moneda(producto.getPrecio()) %></span>
                        <br>
                        <span class="offer-price">$<%= Formato.moneda(producto.getPrecioOferta()) %></span>
                    </p>
                    <a href="ProductoServlet?accion=ver&id=<%= producto.getIdProducto() %>" class="btn btn-pink">Ver producto</a>
                </div>
            </div>
        </div>
        <%
            }
        %>
    </div>
    <%
    } else {
    %>
    <!-- SIN OFERTAS -->
    <div class="text-center py-5">
        <i class="bi bi-tag" style="font-size: 100px; color: #d53384;"></i>
        <h3 class="mt-4">No hay ofertas disponibles por el momento</h3>
        <p class="text-muted">Vuelve pronto para ver nuestras promociones.</p>
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
<!--  BOOTSTRAP JAVASCRIPT-->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<!-- JAVASCRIPT DEL PROYECTO -->
<script src="js/scripts.js"></script>
</body>
</html>