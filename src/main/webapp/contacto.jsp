<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="util.Formato" %>
<!DOCTYPE html>
<html lang="es">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Contacto | J&M Makeup</title>
    <!-- Bootstrap -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Bootstrap Icons -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <!-- CSS -->
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
        <!-- BOTÓN MÓVIL -->
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
                    <a class="nav-link" href="ofertas.jsp">Ofertas</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link active" href="contacto.jsp">Contacto</a>
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
        <h2>Contáctanos</h2>
        <p>Estamos aquí para ayudarte.</p>
    </div>
</section>
<!-- INFORMACIÓN DE CONTACTO -->
<main class="container mb-5">
    <div class="row g-4">
        <!-- INFORMACIÓN -->
        <div class="col-lg-5">
            <div class="contact-card">
                <div class="card-body">
                    <h4 class="mb-4">Información de contacto</h4>
                    <div class="mb-4">
                        <i class="bi bi-envelope-fill" style="color:#d53384;"></i>
                        <strong>Correo electrónico</strong>
                        <p class="text-muted">contacto@jmmakeup.com</p>
                    </div>
                    <div class="mb-4">
                        <i class="bi bi-telephone-fill" style="color:#d53384;"></i>
                        <strong>Teléfono</strong>
                        <p class="text-muted">+57 300 000 0000</p>
                    </div>
                    <div class="mb-4">
                        <i class="bi bi-geo-alt-fill" style="color:#d53384;"></i>
                        <strong>Ubicación</strong>
                        <p class="text-muted">Colombia</p>
                    </div>
                    <div>
                        <i class="bi bi-clock-fill" style="color:#d53384;"></i>
                        <strong>Horario de atención</strong>
                        <p class="text-muted">
                            Lunes a viernes
                            <br>
                            8:00 a.m. - 5:00 p.m.
                        </p>
                    </div>
                </div>
            </div>
        </div>
        <!-- FORMULARIO -->
        <div class="col-lg-7">
            <div class="contact-form">
                <div class="card-body">
                    <h4 class="mb-4">Envíanos un mensaje</h4>

                    <%-- Mensaje de éxito enviado desde ContactoServlet --%>
                    <% if (request.getAttribute("mensajeExito") != null) { %>
                    <div class="alert alert-success" role="alert">
                        <%= request.getAttribute("mensajeExito") %>
                    </div>
                    <% } %>

                    <%-- Mensaje de error enviado desde ContactoServlet --%>
                    <% if (request.getAttribute("mensajeError") != null) { %>
                    <div class="alert alert-danger" role="alert">
                        <%= request.getAttribute("mensajeError") %>
                    </div>
                    <% } %>

                    <!-- El formulario ahora envía los datos por POST al ContactoServlet -->
                    <form action="ContactoServlet" method="post">
                        <!-- NOMBRE -->
                        <div class="mb-3">
                            <label for="nombre" class="form-label">Nombre</label>
                            <input type="text" id="nombre" name="nombre" class="form-control" placeholder="Escribe tu nombre" required>
                        </div>
                        <!-- CORREO -->
                        <div class="mb-3">
                            <label for="correo" class="form-label">Correo electrónico</label>
                            <input type="email" id="correo" name="correo" class="form-control" placeholder="correo@ejemplo.com" required>
                        </div>
                        <!-- ASUNTO -->
                        <div class="mb-3">
                            <label for="asunto" class="form-label">Asunto</label>
                            <input type="text" id="asunto" name="asunto" class="form-control" placeholder="Asunto del mensaje" required>
                        </div>
                        <!-- MENSAJE -->
                        <div class="mb-4">
                            <label for="mensaje" class="form-label">Mensaje</label>
                            <textarea id="mensaje" name="mensaje" class="form-control" rows="5" placeholder="Escribe tu mensaje" required></textarea>
                        </div>
                        <!-- BOTÓN -->
                        <div class="d-grid">
                            <button type="submit" class="btn btn-pink">
                                <i class="bi bi-send"></i>
                                Enviar mensaje
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</main>
<!-- PIE DE PÁGINA -->
<footer>
    <div class="container">
        <p>&copy; 2026 J&M Makeup. Todos los derechos reservados.</p>
        <p>Tu tienda virtual de maquillaje.</p>
    </div>
</footer>
<!-- Bootstrap -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<!-- JAVASCRIPT DEL PROYECTO -->
<script src="js/scripts.js"></script>
</body>
</html>