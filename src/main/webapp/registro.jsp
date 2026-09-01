<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="util.Formato" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <!-- CONFIGURACIÓN DE LA PÁGINA -->
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Crear cuenta | J&M Makeup</title>
    <!-- BOOTSTRAP 5 -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- BOOTSTRAP ICONS -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <!-- CSS DEL PROYECTO -->
    <link rel="stylesheet" href="css/estilos.css">
</head>

<body>
<!-- BARRA DE NAVEGACIÓN -->
<nav class="navbar navbar-expand-lg">
    <div class="container">
        <!-- LOGO -->
        <a class="navbar-brand" href="index.jsp">
            <img src="img/logo.png" alt="J&M Makeup" height="36">
        </a>
        <!-- BOTÓN INICIO -->
        <a href="index.jsp" class="btn btn-outline-pink">
            <i class="bi bi-house"></i>
            Inicio
        </a>
    </div>
</nav>
<!-- FORMULARIO DE REGISTRO-->
<main>
    <div class="container">
        <div class="form-container" style="max-width: 650px;">
            <!-- ICONO -->
            <div class="text-center mb-3">
                <i class="bi bi-person-plus-fill" style="font-size: 70px; color: #d53384;"></i>
            </div>
            <!-- TÍTULO -->
            <h2>Crear una cuenta</h2>
            <p class="text-center text-muted mb-4">Regístrate en J&M Makeup y disfruta de nuestros productos.</p>
            <!-- MENSAJE DE ERROR -->
            <%
                String error = request.getParameter("error");
                if ("correo".equals(error)) {
            %>
            <div class="alert alert-danger" role="alert">
                <i class="bi bi-exclamation-triangle"></i>
                El correo electrónico ya está registrado.
            </div>
            <%
                }
                if ("registro".equals(error)) {
            %>
            <div class="alert alert-danger" role="alert">
                <i class="bi bi-exclamation-triangle"></i>
                No fue posible completar el registro. Intenta nuevamente.
            </div>
            <%
                }
            %>

            <!-- MENSAJE DE ÉXITO -->
            <%
                String mensaje = request.getParameter("mensaje");
                if ("exito".equals(mensaje)) {
            %>
            <div class="alert alert-success" role="alert">
                <i class="bi bi-check-circle"></i>
                Registro exitoso. Ahora puedes iniciar sesión.
            </div>
            <%
                }
            %>
            <!-- FORMULARIO -->
            <form action="RegistroServlet" method="post" onsubmit="return validarRegistroCompleto();">
                <!-- NOMBRE -->
                <div class="mb-3">
                    <label for="nombre" class="form-label">Nombre completo</label>
                    <div class="input-group">
                        <span class="input-group-text">
                            <i class="bi bi-person"></i>
                        </span>
                        <input type="text" class="form-control" id="nombre" name="nombre" placeholder="Ingrese su nombre completo" required>
                    </div>
                </div>


                <!-- CORREO -->
                <div class="mb-3">
                    <label for="correo" class="form-label">Correo electrónico</label>
                    <div class="input-group">
                        <span class="input-group-text">
                            <i class="bi bi-envelope"></i>
                        </span>
                        <input type="email" class="form-control" id="correo" name="correo" placeholder="correo@ejemplo.com" required>
                    </div>
                </div>
                <!-- TELÉFONO -->
                <div class="mb-3">
                    <label for="telefono" class="form-label">Teléfono</label>
                    <div class="input-group">
                        <span class="input-group-text">
                            <i class="bi bi-telephone"></i>
                        </span>
                        <input type="tel" class="form-control" id="telefono" name="telefono" placeholder="Ingrese su número de teléfono">
                    </div>
                </div>
                <!-- DIRECCIÓN -->
                <div class="mb-3">
                    <label for="direccion" class="form-label">Dirección</label>
                    <div class="input-group">
                        <span class="input-group-text">
                            <i class="bi bi-geo-alt"></i>
                        </span>
                        <input type="text" class="form-control" id="direccion" name="direccion" placeholder="Ingrese su dirección">
                    </div>
                </div>
                <!-- CONTRASEÑA -->
                <div class="mb-3">
                    <label for="password" class="form-label">Contraseña</label>
                    <div class="input-group">
                        <span class="input-group-text">
                            <i class="bi bi-lock"></i>
                        </span>
                        <input type="password" class="form-control" id="password" name="password" placeholder="Ingrese una contraseña" required>
                        <button type="button" class="btn btn-outline-secondary" onclick="mostrarPasswordRegistro()">
                            <i id="iconoPasswordRegistro" class="bi bi-eye"></i>
                        </button>
                    </div>
                    <small class="text-muted">La contraseña debe tener mínimo 6 caracteres.</small>
                </div>
                <!-- CONFIRMAR CONTRASEÑA -->
                <div class="mb-3">
                    <label for="confirmarPassword" class="form-label">Confirmar contraseña</label>
                    <div class="input-group">
                        <span class="input-group-text">
                            <i class="bi bi-lock-fill"></i>
                        </span>
                        <input type="password" class="form-control" id="confirmarPassword" name="confirmarPassword" placeholder="Repita su contraseña" required>
                    </div>
                </div>
                <!-- ACEPTAR TÉRMINOS -->
                <div class="form-check mb-4">
                    <input class="form-check-input" type="checkbox" id="terminos" name="terminos" required>
                    <label class="form-check-label" for="terminos">Acepto los términos y condiciones de J&M Makeup.</label>
                </div>
                <!-- BOTÓN REGISTRARSE -->
                <div class="d-grid">
                    <button type="submit" class="btn btn-pink">
                        <i class="bi bi-person-plus"></i>
                        Crear cuenta
                    </button>
                </div>
            </form>
            <!-- ENLACE A LOGIN -->
            <div class="text-center mt-4">
                <p>
                    ¿Ya tienes una cuenta?
                    <a href="login.jsp" style="color: #d53384;">Iniciar sesión</a>
                </p>
            </div>
            <!-- VOLVER AL INICIO -->
            <div class="text-center">
                <a href="index.jsp" class="text-decoration-none" style="color: #666666;">
                    <i class="bi bi-arrow-left"></i>
                    Volver al inicio
                </a>
            </div>
        </div>
    </div>
</main>
<!-- PIE DE PÁGINA -->
<footer>
    <div class="container">
        <p>&copy; 2026 J&M Makeup. Todos los derechos reservados.</p>
    </div>
</footer>
<!-- BOOTSTRAP JAVASCRIPT -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<!-- JAVASCRIPT DEL PROYECTO -->
<script src="js/scripts.js"></script>
<!-- VALIDACIÓN DEL FORMULARIO DE REGISTRO -->
<script>
    /**
     * Valida los datos antes de enviarlos
     * al RegistroServlet.
     */
    function validarRegistroCompleto() {
        const nombre = document.getElementById("nombre").value.trim();
        const correo = document.getElementById("correo").value.trim();
        const password = document.getElementById("password").value;
        const confirmarPassword = document.getElementById("confirmarPassword").value;
        const terminos = document.getElementById("terminos");
        /* Validar nombre */
        if (nombre === "") {
            alert("Por favor, ingrese su nombre completo.");
            return false;
        }
        /* Validar correo */
        if (correo === "") {
            alert("Por favor, ingrese su correo electrónico.");
            return false;
        }
        /* Validar contraseña */
        if (password.length < 6) {
            alert("La contraseña debe tener mínimo 6 caracteres.");
            return false;
        }
        /* Confirmar contraseña */
        if (password !== confirmarPassword) {
            alert("Las contraseñas no coinciden.");
            return false;
        }
        /* Validar términos */
        if (!terminos.checked) {
            alert("Debe aceptar los términos y condiciones.");
            return false;
        }
        return true;
    }
    /**
     * Muestra u oculta la contraseña.
     */
    function mostrarPasswordRegistro() {
        const password = document.getElementById("password");
        const icono = document.getElementById("iconoPasswordRegistro");
        if (password.type === "password") {
            password.type = "text";
            icono.classList.remove("bi-eye");
            icono.classList.add("bi-eye-slash");
        } else {
            password.type = "password";
            icono.classList.remove("bi-eye-slash");
            icono.classList.add("bi-eye");
        }
    }
</script>
</body>
</html>