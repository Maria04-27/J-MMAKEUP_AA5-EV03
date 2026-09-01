<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="util.Formato" %>
<%@ page import="java.util.List" %>
<%@ page import="modelo.Pedido" %>

<!DOCTYPE html>
<html lang="es">

<head>
    <!-- CONFIGURACIÓN DE LA PÁGINA -->
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mis pedidos | J&M Makeup</title>
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

<!-- ENCABEZADO -->
<section class="container">
    <div class="section-title">
        <h2>Mis pedidos</h2>
        <p>Consulta el historial de tus compras.</p>
    </div>
</section>

<!-- CONTENIDO PRINCIPAL -->
<main class="container mb-5">
    <%
        // Obtenemos la lista de pedidos enviada por PedidoServlet.
        List<Pedido> pedidos = (List<Pedido>) request.getAttribute("pedidos");
    %>
    <%
        if (pedidos != null && !pedidos.isEmpty()) {
    %>
    <div class="card shadow-sm">
        <div class="card-body">
            <div class="table-responsive">
                <table class="table align-middle">
                    <thead>
                    <tr>
                        <th>N.° Pedido</th>
                        <th>Fecha</th>
                        <th>Estado</th>
                        <th>Método de pago</th>
                        <th>Total</th>
                        <th></th>
                    </tr>
                    </thead>
                    <tbody>
                    <%
                        for (Pedido pedido : pedidos) {
                    %>
                    <tr>
                        <td>#<%= pedido.getIdPedido() %></td>
                        <td><%= pedido.getFechaPedido() %></td>
                        <td>
                            <span class="badge bg-warning text-dark"><%= pedido.getEstado() %></span>
                        </td>
                        <td><%= pedido.getMetodoPago() %></td>
                        <td>$<%= Formato.moneda(pedido.getTotal()) %></td>
                        <td class="text-end">
                            <a href="PedidoServlet?accion=ver&id=<%= pedido.getIdPedido() %>" class="btn btn-sm btn-outline-pink">
                                Ver detalle
                            </a>
                        </td>
                    </tr>
                    <%
                        }
                    %>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
    <%
    } else {
    %>
    <!-- SIN PEDIDOS -->
    <div class="text-center py-5">
        <i class="bi bi-bag-x" style="font-size: 100px; color: #d53384;"></i>
        <h3 class="mt-4">Aún no tienes pedidos</h3>
        <p class="text-muted">Explora nuestros productos y realiza tu primera compra.</p>
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