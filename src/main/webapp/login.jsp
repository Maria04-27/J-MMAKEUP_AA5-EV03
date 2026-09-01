<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="util.Formato" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <!-- Configuración básica -->
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <!-- Título de la página -->
    <title>Iniciar sesión | J&M Makeup</title>
    <!-- BOOTSTRAP 5-->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- BOOTSTRAP ICONS -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <!-- HOJA DE ESTILOS DEL PROYECTO -->
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
        <!-- BOTÓN MENÚ PARA MÓVILES -->
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#menuPrincipal">
            <span class="navbar-toggler-icon"></span>
        </button>
        <!-- MENÚ PRINCIPAL -->
        <div class="collapse navbar-collapse" id="menuPrincipal">
            <ul class="navbar-nav mx-auto mb-2 mb-lg-0">
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
                    <a class="nav-link" href="contacto.jsp">Contacto</a>
                </li>
            </ul>
        </div>
    </div>
</nav>

<!-- SECCIÓN DE INICIO DE SESIÓN -->
<main class="container">
    <div class="row justify-content-center py-5">
        <div class="col-md-5">
            <div class="card shadow-sm">
                <div class="card-body p-4">
                    <!-- TÍTULO -->
                    <h3 class="text-center mb-4">
                        <i class="bi bi-person-circle" style="color: #d53384;"></i>
                        Iniciar sesión
                    </h3>

                    <!-- MENSAJE DE ERROR -->
                    <%
                        String error = request.getParameter("error");
                        if ("credenciales".equals(error)) {
                    %>
                    <div class="alert alert-danger" role="alert">
                        Correo o contraseña incorrectos. Inténtalo nuevamente.
                    </div>
                    <%
                        }
                    %>

                    <!-- FORMULARIO DE LOGIN -->
                    <form action="LoginServlet" method="post">
                        <input type="hidden" name="accion" value="login">
                        <!-- CORREO -->
                        <div class="mb-3">
                            <label for="correo" class="form-label">Correo electrónico</label>
                            <input type="email" class="form-control" id="correo" name="correo" required>
                        </div>
                        <!-- CONTRASEÑA -->
                        <div class="mb-3">
                            <label for="password" class="form-label">Contraseña</label>
                            <input type="password" class="form-control" id="password" name="password" required>
                        </div>
                        <!-- BOTÓN DE INGRESO -->
                        <div class="d-grid">
                            <button type="submit" class="btn btn-pink">
                                Iniciar sesión
                            </button>
                        </div>
                    </form>

                    <!-- ENLACE A REGISTRO -->
                    <p class="text-center mt-3 mb-0">
                        ¿No tienes cuenta?
                        <a href="registro.jsp">Regístrate aquí</a>
                    </p>
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

<!-- BOOTSTRAP JAVASCRIPT-->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<!-- JAVASCRIPT DEL PROYECTO-->
<script src="js/scripts.js"></script>
</body>
</html>