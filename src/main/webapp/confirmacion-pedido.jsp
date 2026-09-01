<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="util.Formato" %>
<%@ page import="modelo.Pedido" %>

<!DOCTYPE html>
<html lang="es">

<head>
    <!-- CONFIGURACIÓN DE LA PÁGINA -->
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Pedido confirmado | J&M Makeup</title>
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
    </div>
</nav>

<!-- CONTENIDO PRINCIPAL -->
<main class="container mb-5">
    <%
        // Obtenemos el pedido enviado por PedidoServlet.
        Pedido pedido = (Pedido) request.getAttribute("pedido");
    %>
    <%
        if (pedido != null) {
    %>
    <div class="row justify-content-center py-5">
        <div class="col-md-7">
            <div class="card shadow-sm text-center">
                <div class="card-body p-5">
                    <!-- ICONO DE ÉXITO -->
                    <i class="bi bi-check-circle-fill" style="font-size: 80px; color: #28a745;"></i>
                    <h2 class="mt-4">¡Pedido confirmado!</h2>
                    <p class="text-muted">Gracias por tu compra en J&M Makeup.</p>
                    <hr class="my-4">
                    <!-- DETALLES DEL PEDIDO -->
                    <div class="text-start">
                        <p>
                            <strong>Número de pedido:</strong>
                            #<%= pedido.getIdPedido() %>
                        </p>
                        <p>
                            <strong>Fecha:</strong>
                            <%= pedido.getFechaPedido() %>
                        </p>
                        <p>
                            <strong>Estado:</strong>
                            <span class="badge bg-warning text-dark"><%= pedido.getEstado() %></span>
                        </p>
                        <p>
                            <strong>Método de pago:</strong>
                            <%= pedido.getMetodoPago() %>
                        </p>
                        <p class="mb-0">
                            <strong>Total pagado:</strong>
                            <span style="color: #d53384; font-size: 1.2rem;">
                                $<%= Formato.moneda(pedido.getTotal()) %>
                            </span>
                        </p>
                    </div>
                    <hr class="my-4">
                    <!-- BOTONES -->
                    <div class="d-grid gap-2">
                        <a href="index.jsp" class="btn btn-pink">
                            <i class="bi bi-house"></i>
                            Volver al inicio
                        </a>
                        <a href="PedidoServlet?accion=misPedidos" class="btn btn-outline-pink">
                            <i class="bi bi-bag-check"></i>
                            Ver mis pedidos
                        </a>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <%
    } else {
    %>
    <!-- PEDIDO NO ENCONTRADO -->
    <div class="text-center py-5">
        <i class="bi bi-exclamation-circle" style="font-size: 100px; color: #d53384;"></i>
        <h3 class="mt-4">No se encontró el pedido</h3>
        <a href="index.jsp" class="btn btn-pink mt-3">
            <i class="bi bi-house"></i>
            Volver al inicio
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