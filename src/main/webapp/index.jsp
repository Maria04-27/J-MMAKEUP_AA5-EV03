<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="dao.ProductoDAO" %>
<%@ page import="modelo.Producto" %>
<%@ page import="util.Formato" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <!-- Configuración básica -->
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <!-- Título de la página -->
    <title>J&M Makeup | Tienda de Maquillaje</title>
    <!-- BOOTSTRAP 5-->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- BOOTSTRAP ICONS-->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <!-- HOJA DE ESTILOS DEL PROYECTO-->
    <link rel="stylesheet" href="css/estilos.css">
</head>
<body>
<!-- BARRA DE NAVEGACIÓN-->
<nav class="navbar navbar-expand-lg sticky-top">
    <div class="container">
        <!-- LOGO -->
        <a class="navbar-brand" href="index.jsp">
            <img src="img/logo.png" alt="J&M Makeup" height="36">
        </a>
        <!-- BOTÓN MENÚ PARA MÓVILES -->
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#menuPrincipal">
            <span class="navbar-toggler-icon"></span>
        </button>
        <!-- MENÚ PRINCIPAL -->
        <div class="collapse navbar-collapse" id="menuPrincipal">
            <!-- OPCIONES DEL MENÚ -->
            <ul class="navbar-nav mx-auto mb-2 mb-lg-0">
                <li class="nav-item">
                    <a class="nav-link active" href="index.jsp">Inicio</a>
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
            <!-- BUSCADOR -->
            <form class="d-flex me-3" role="search" action="ProductoServlet" method="get">
                <input type="hidden" name="accion" value="buscar">
                <input class="form-control" type="search" name="busqueda" placeholder="Buscar..." aria-label="Buscar productos">
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
<!-- BANNER PRINCIPAL -->
<section class="hero">
    <div class="container">
        <div class="row align-items-center">
            <!-- TEXTO DEL BANNER -->
            <div class="col-lg-6">
                <h1>Realza tu belleza con J&M Makeup</h1>
                <p>Descubre nuestra colección de maquillaje y encuentra los productos perfectos para resaltar tu estilo.</p>
                <a href="ProductoServlet?accion=listar" class="btn btn-pink">
                    Ver productos
                    <i class="bi bi-arrow-right"></i>
                </a>
            </div>
            <!-- IMAGEN DEL BANNER -->
            <div class="col-lg-6 text-center">
                <img src="img/banner.png" class="img-fluid" alt="Productos de maquillaje">
            </div>
        </div>
    </div>
</section>
<!-- CATEGORÍAS -->
<section class="container">
    <div class="section-title">
        <h2>Explora nuestras categorías</h2>
        <p>Encuentra todo lo que necesitas para crear tu look ideal.</p>
    </div>
    <div class="row g-4">
        <!-- CATEGORÍA 1 -->
        <div class="col-md-4">
            <div class="product-card text-center p-4">
                <i class="bi bi-palette" style="font-size: 60px; color: #d53384;"></i>
                <h5 class="mt-3">Rostro</h5>
                <p>Bases, correctores, polvos y rubores.</p>
                <a href="ProductoServlet?accion=listar" class="btn btn-outline-pink">Ver productos</a>
            </div>
        </div>
        <!-- CATEGORÍA 2 -->
        <div class="col-md-4">
            <div class="product-card text-center p-4">
                <i class="bi bi-eye" style="font-size: 60px; color: #d53384;"></i>
                <h5 class="mt-3">Ojos</h5>
                <p>Sombras, máscaras y delineadores.</p>
                <a href="ProductoServlet?accion=listar" class="btn btn-outline-pink">Ver productos</a>
            </div>
        </div>
        <!-- CATEGORÍA 3 -->
        <div class="col-md-4">
            <div class="product-card text-center p-4">
                <i class="bi bi-heart" style="font-size: 60px; color: #d53384;"></i>
                <h5 class="mt-3">Labios</h5>
                <p>Labiales, brillos y delineadores.</p>
                <a href="ProductoServlet?accion=listar" class="btn btn-outline-pink">Ver productos</a>
            </div>
        </div>
    </div>
</section>
<!-- PRODUCTOS DESTACADOS-->
<section class="container">
    <div class="section-title">
        <h2>Productos destacados</h2>
        <p>Descubre algunos de nuestros productos favoritos.</p>
    </div>
    <%
        // Consultamos los productos reales del catálogo
        // y mostramos los primeros 3 como destacados.
        ProductoDAO productoDAO = new ProductoDAO();
        List<Producto> destacados = productoDAO.listarTodos();
    %>
    <div class="row g-4">
        <%
            int contador = 0;
            for (Producto producto : destacados) {
                if (contador >= 3) {
                    break;
                }
                contador++;
        %>
        <!-- PRODUCTO -->
        <div class="col-md-4">
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
                <div class="card-body">
                    <h5 class="product-name"><%= producto.getNombre() %></h5>
                    <p class="product-description"><%= producto.getDescripcion() %></p>
                    <p class="product-price">$<%= Formato.moneda(producto.getPrecio()) %></p>
                    <a href="ProductoServlet?accion=ver&id=<%= producto.getIdProducto() %>" class="btn btn-pink">Ver producto</a>
                </div>
            </div>
        </div>
        <%
            }
        %>
    </div>
    <!-- BOTÓN VER TODOS LOS PRODUCTOS -->
    <div class="text-center mt-5">
        <a href="ProductoServlet?accion=listar" class="btn btn-outline-pink">
            Ver todos los productos
            <i class="bi bi-arrow-right"></i>
        </a>
    </div>
</section>
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