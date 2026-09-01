<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="util.Formato" %>
<%@ page import="modelo.Producto" %>

<!DOCTYPE html>
<html lang="es">

<head>
    <!-- CONFIGURACIÓN DE LA PÁGINA -->
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <%
        // Obtenemos el producto enviado por ProductoServlet.
        Producto producto = (Producto) request.getAttribute("producto");
    %>
    <title><%= (producto != null) ? producto.getNombre() : "Producto" %> | J&M Makeup</title>
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
            <ul class="navbar-nav mx-auto mb-2 mb-lg-0">
                <li class="nav-item">
                    <a class="nav-link" href="index.jsp">Inicio</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="ProductoServlet?accion=listar">Productos</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="ProductoServlet?accion=ofertas">Ofertas</a>
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

<!-- CONTENIDO PRINCIPAL -->
<main class="container mb-5">
    <%
        if (producto != null) {
    %>
    <!-- MIGAS DE PAN -->
    <nav aria-label="breadcrumb" class="mt-4">
        <ol class="breadcrumb">
            <li class="breadcrumb-item"><a href="index.jsp">Inicio</a></li>
            <li class="breadcrumb-item"><a href="ProductoServlet?accion=listar">Productos</a></li>
            <li class="breadcrumb-item active"><%= producto.getNombre() %></li>
        </ol>
    </nav>

    <div class="row g-4 mt-2">
        <!-- IMAGEN DEL PRODUCTO -->
        <div class="col-md-5">
            <div class="card shadow-sm">
                <%
                    if (producto.getImagen() != null && !producto.getImagen().isEmpty()) {
                %>
                <img src="img/<%= producto.getImagen() %>" alt="<%= producto.getNombre() %>" class="img-fluid">
                <%
                } else {
                %>
                <div class="text-center p-5">
                    <i class="bi bi-box" style="font-size: 150px; color: #d53384;"></i>
                </div>
                <%
                    }
                %>
            </div>
        </div>
        <!-- INFORMACIÓN DEL PRODUCTO -->
        <div class="col-md-7">
            <h2><%= producto.getNombre() %></h2>
            <p class="text-muted"><%= producto.getDescripcion() %></p>
            <h3 style="color: #d53384;">$<%= Formato.moneda(producto.getPrecio()) %></h3>
            <!-- STOCK -->
            <%
                if (producto.getStock() > 0) {
            %>
            <p class="text-success">
                <i class="bi bi-check-circle"></i>
                Disponible (<%= producto.getStock() %> unidades)
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
            <!-- FORMULARIO PARA AGREGAR AL CARRITO -->
            <%
                if (producto.getStock() > 0) {
            %>
            <form action="CarritoServlet" method="post" class="mt-4">
                <input type="hidden" name="accion" value="agregar">
                <input type="hidden" name="idProducto" value="<%= producto.getIdProducto() %>">
                <div class="row align-items-center g-2" style="max-width: 300px;">
                    <div class="col-4">
                        <input type="number" name="cantidad" class="form-control" value="1" min="1" max="<%= producto.getStock() %>">
                    </div>
                    <div class="col-8">
                        <button type="submit" class="btn btn-pink w-100">
                            <i class="bi bi-cart-plus"></i>
                            Agregar al carrito
                        </button>
                    </div>
                </div>
            </form>
            <%
            } else {
            %>
            <button type="button" class="btn btn-secondary mt-4" disabled>Producto agotado</button>
            <%
                }
            %>
        </div>
    </div>
    <%
    } else {
    %>
    <!-- PRODUCTO NO ENCONTRADO -->
    <div class="text-center py-5">
        <i class="bi bi-exclamation-circle" style="font-size: 100px; color: #d53384;"></i>
        <h3 class="mt-4">Producto no encontrado</h3>
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

<!-- BOOTSTRAP JAVASCRIPT -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<!-- JAVASCRIPT DEL PROYECTO -->
<script src="js/scripts.js"></script>
</body>
</html>