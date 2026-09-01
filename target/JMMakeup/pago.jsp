<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="util.Formato" %>
<%@ page import="java.util.List" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="modelo.DetalleCarrito" %>

<!DOCTYPE html>
<html lang="es">

<head>
    <!-- CONFIGURACIÓN DE LA PÁGINA -->
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Pago | J&M Makeup</title>
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
        <!-- MENÚ -->
        <div class="ms-auto">
            <a href="CarritoServlet?accion=ver" class="btn btn-outline-pink">
                <i class="bi bi-cart3"></i>
                Volver al carrito
            </a>
        </div>
    </div>
</nav>
<!-- ENCABEZADO -->
<section class="container">
    <div class="section-title">
        <h2>Finalizar compra</h2>
        <p>Selecciona tu método de pago y confirma tu pedido.</p>
    </div>
</section>
<!-- CONTENIDO PRINCIPAL -->
<main class="container mb-5">
    <div class="row g-4">
        <!-- INFORMACIÓN DEL CLIENTE -->
        <div class="col-lg-7">
            <div class="card shadow-sm">
                <div class="card-body">
                    <h4 class="mb-4">
                        <i class="bi bi-person"></i>
                        Información del cliente
                    </h4>
                    <!-- NOMBRE -->
                    <div class="mb-3">
                        <label for="nombreCliente" class="form-label">Nombre completo</label>
                        <input type="text" class="form-control" id="nombreCliente" name="nombreCliente" placeholder="Ingrese su nombre completo" required>
                    </div>
                    <!-- CORREO -->
                    <div class="mb-3">
                        <label for="correoCliente" class="form-label">Correo electrónico</label>
                        <input type="email" class="form-control" id="correoCliente" name="correoCliente" placeholder="correo@ejemplo.com" required>
                    </div>
                    <!-- TELÉFONO -->
                    <div class="mb-3">
                        <label for="telefonoCliente" class="form-label">Teléfono</label>
                        <input type="tel" class="form-control" id="telefonoCliente" name="telefonoCliente" placeholder="Ingrese su teléfono" required>
                    </div>
                    <!-- DIRECCIÓN -->
                    <div class="mb-4">
                        <label for="direccionCliente" class="form-label">Dirección de entrega</label>
                        <textarea class="form-control" id="direccionCliente" name="direccionCliente" rows="3" placeholder="Ingrese la dirección donde recibirá su pedido" required></textarea>
                    </div>
                    <hr>
                    <!-- MÉTODO DE PAGO -->
                    <h4 class="mb-4">
                        <i class="bi bi-credit-card"></i>
                        Método de pago
                    </h4>
                    <form action="PedidoServlet" method="post" onsubmit="return validarPago();">
                        <!-- ACCIÓN -->
                        <input type="hidden" name="accion" value="confirmar">
                        <!-- OPCIONES DE PAGO -->
                        <div class="mb-4">
                            <!-- TARJETA -->
                            <div class="form-check mb-3">
                                <input class="form-check-input" type="radio" name="metodoPago" id="tarjeta" value="Tarjeta" onchange="mostrarMetodoPago('tarjeta')" required>
                                <label class="form-check-label" for="tarjeta">
                                    <i class="bi bi-credit-card"></i>
                                    Tarjeta de crédito o débito
                                </label>
                            </div>
                            <!-- PSE -->
                            <div class="form-check mb-3">
                                <input class="form-check-input" type="radio" name="metodoPago" id="pse" value="PSE" onchange="mostrarMetodoPago('pse')">
                                <label class="form-check-label" for="pse">
                                    <i class="bi bi-bank"></i>
                                    PSE
                                </label>
                            </div>
                            <!-- NEQUI -->
                            <div class="form-check mb-3">
                                <input class="form-check-input" type="radio" name="metodoPago" id="nequi" value="Nequi" onchange="mostrarMetodoPago('nequi')">
                                <label class="form-check-label" for="nequi">
                                    <i class="bi bi-phone"></i>
                                    Nequi
                                </label>
                            </div>
                            <!-- CONTRAENTREGA -->
                            <div class="form-check mb-3">
                                <input class="form-check-input" type="radio" name="metodoPago" id="contraentrega" value="Contraentrega" onchange="mostrarMetodoPago('contraentrega')">
                                <label class="form-check-label" for="contraentrega">
                                    <i class="bi bi-cash-coin"></i>
                                    Pago contraentrega
                                </label>
                            </div>
                        </div>
                        <!-- DATOS DE TARJETA -->
                        <div id="datosTarjeta" class="payment-method">
                            <h5>Datos de la tarjeta</h5>
                            <div class="mb-3">
                                <label class="form-label">Número de tarjeta</label>
                                <input type="text" class="form-control" name="numeroTarjeta" id="numeroTarjeta" placeholder="0000 0000 0000 0000" maxlength="19">
                            </div>
                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label class="form-label">Fecha de vencimiento</label>
                                    <input type="text" class="form-control" name="fechaVencimiento" id="fechaVencimiento" placeholder="MM/AA">
                                </div>
                                <div class="col-md-6 mb-3">
                                    <label class="form-label">CVV</label>
                                    <input type="password" class="form-control" name="cvv" id="cvv" placeholder="123" maxlength="3">
                                </div>
                            </div>
                        </div>
                        <!-- DATOS PSE -->
                        <div id="datosPSE" class="payment-method">
                            <h5>Pago mediante PSE</h5>
                            <div class="mb-3">
                                <label class="form-label">Seleccione su banco</label>
                                <select class="form-select" name="banco" id="banco">
                                    <option value="">Seleccione un banco</option>
                                    <option value="Bancolombia">Bancolombia</option>
                                    <option value="Davivienda">Davivienda</option>
                                    <option value="Banco de Bogotá">Banco de Bogotá</option>
                                    <option value="BBVA">BBVA</option>
                                    <option value="Banco de Occidente">Banco de Occidente</option>
                                </select>
                            </div>
                        </div>
                        <!--  DATOS NEQUI -->
                        <div id="datosNequi" class="payment-method">
                            <h5>Pago mediante Nequi</h5>
                            <div class="mb-3">
                                <label class="form-label">Número de celular</label>
                                <input type="tel" class="form-control" name="numeroNequi" id="numeroNequi" placeholder="3001234567">
                            </div>
                        </div>
                        <!--  CONTRAENTREGA -->
                        <div id="datosContraentrega" class="payment-method">
                            <div class="alert alert-info">
                                <i class="bi bi-info-circle"></i>
                                Recibirás tu pedido y realizarás el pago al momento de la entrega.
                            </div>
                        </div>
                        <!-- TÉRMINOS -->
                        <div class="form-check mb-4 mt-4">
                            <input class="form-check-input" type="checkbox" id="aceptarPedido" required>
                            <label class="form-check-label" for="aceptarPedido">Confirmo que los datos proporcionados son correctos y deseo realizar el pedido.</label>
                        </div>
                        <!-- BOTÓN CONFIRMAR -->
                        <div class="d-grid">
                            <button type="submit" class="btn btn-pink btn-lg">
                                <i class="bi bi-check-circle"></i>
                                Confirmar pedido
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <!-- RESUMEN DEL PEDIDO -->
        <div class="col-lg-5">
            <div class="card shadow-sm">
                <div class="card-body">
                    <h4 class="mb-4">
                        <i class="bi bi-receipt"></i>
                        Resumen del pedido
                    </h4>
                    <!-- OBTENEMOS LOS DATOS ENVIADOS POR PedidoServlet -->
                    <%
                        List<DetalleCarrito> detalle = (List<DetalleCarrito>) request.getAttribute("detalle");
                        BigDecimal total = (BigDecimal) request.getAttribute("total");
                        if (total == null) {
                            total = BigDecimal.ZERO;
                        }
                    %>
                    <!-- LISTA DE PRODUCTOS -->
                    <%
                        if (detalle != null) {
                            for (DetalleCarrito item : detalle) {
                    %>
                    <div class="d-flex justify-content-between align-items-center mb-3">
                        <div>
                            <strong><%= item.getNombre() %></strong>
                            <br>
                            <small class="text-muted">
                                <%= item.getCantidad() %> x $<%= Formato.moneda(item.getPrecio()) %>
                            </small>
                        </div>
                        <span>$<%= Formato.moneda(item.getPrecio() * item.getCantidad()) %></span>
                    </div>
                    <%
                            }
                        }
                    %>
                    <hr>
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
                    <div class="d-flex justify-content-between">
                        <h5>Total</h5>
                        <h5 style="color: #d53384;">$<%= Formato.moneda(total) %></h5>
                    </div>
                </div>
            </div>
            <!-- SEGURIDAD -->
            <div class="card shadow-sm mt-4">
                <div class="card-body text-center">
                    <i class="bi bi-shield-check" style="font-size: 45px; color: #d53384;"></i>
                    <h5 class="mt-3">Compra segura</h5>
                    <p class="text-muted">Tus datos son tratados de manera segura y confidencial.</p>
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

<!-- BOOTSTRAP JAVASCRIPT -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<!-- JAVASCRIPT DEL PROYECTO -->
<script src="js/scripts.js"></script>

<!-- FUNCIONES DEL MÉTODO DE PAGO -->
<script>
    /**
     * Muestra los campos correspondientes
     * al método de pago seleccionado.
     */
    function mostrarMetodoPago(metodo) {
        /*
         * Ocultar todos los métodos.
         */
        document.querySelectorAll(".payment-method").forEach(
            function(elemento) {
                elemento.style.display = "none";
            }
        );
        /*
         * Mostrar el método seleccionado.
         */
        if (metodo === "tarjeta") {
            document.getElementById("datosTarjeta").style.display = "block";
        }
        if (metodo === "pse") {
            document.getElementById("datosPSE").style.display = "block";
        }
        if (metodo === "nequi") {
            document.getElementById("datosNequi").style.display = "block";
        }
        if (metodo === "contraentrega") {
            document.getElementById("datosContraentrega").style.display = "block";
        }
    }
    /**
     * Valida el formulario de pago.
     */
    function validarPago() {
        const metodoSeleccionado = document.querySelector('input[name="metodoPago"]:checked');
        /*
         * Verificar método de pago.
         */
        if (!metodoSeleccionado) {
            alert("Por favor, seleccione un método de pago.");
            return false;
        }
        /*
         * Obtener el método.
         */
        const metodo = metodoSeleccionado.value;
        /*
         * Validar tarjeta.
         */
        if (metodo === "Tarjeta") {
            const numeroTarjeta = document.getElementById("numeroTarjeta").value.trim();
            const fecha = document.getElementById("fechaVencimiento").value.trim();
            const cvv = document.getElementById("cvv").value.trim();
            if (numeroTarjeta === "" || fecha === "" || cvv === "") {
                alert("Complete todos los datos de la tarjeta.");
                return false;
            }
        }
        /*
         * Validar PSE.
         */
        if (metodo === "PSE") {
            const banco = document.getElementById("banco").value;
            if (banco === "") {
                alert("Seleccione su banco.");
                return false;
            }
        }
        /*
         * Validar Nequi.
         */
        if (metodo === "Nequi") {
            const numeroNequi = document.getElementById("numeroNequi").value.trim();
            if (numeroNequi === "") {
                alert("Ingrese el número de celular asociado a Nequi.");
                return false;
            }
        }
        /*
         * Confirmar pedido.
         */
        return confirm("¿Deseas confirmar tu pedido?");
    }
    /*
     * Ocultar todos los métodos
     * al cargar la página.
     */
    document.addEventListener("DOMContentLoaded", function() {
            document.querySelectorAll(".payment-method").forEach(
                function(elemento) {
                    elemento.style.display = "none";
                }
            );
        }
    );
</script>
</body>
</html>