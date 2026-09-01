package api;

import dao.CarritoDAO;
import dao.PedidoDAO;
import modelo.Carrito;
import modelo.DetalleCarrito;
import modelo.Pedido;
import modelo.Usuario;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Servicio web (API REST) encargado de exponer
 * las operaciones relacionadas con los pedidos
 * de la tienda virtual J&M Makeup.
 *
 * Todas las rutas de este servicio requieren que el usuario
 * tenga una sesión activa (haber iniciado sesión).
 *
 * Recurso: /api/pedidos
 *
 * Rutas expuestas:
 * - GET  /api/pedidos       -> lista los pedidos del usuario autenticado
 * - GET  /api/pedidos/{id}  -> consulta un pedido específico
 * - POST /api/pedidos       -> crea un pedido a partir del carrito activo {metodoPago}
 * - PUT  /api/pedidos/{id}  -> actualiza el estado de un pedido (admin) {estado}
 *
 * Proyecto: J&M Makeup
 * Evidencia: GA7-220501096-AA5-EV03
 *
 * @author J&M Makeup
 */
@WebServlet("/api/pedidos/*")
public class PedidoApiServlet extends ApiServlet {

    // DAO encargado de los pedidos.
    private PedidoDAO pedidoDAO;
    // DAO encargado del carrito, necesario para generar el pedido.
    private CarritoDAO carritoDAO;

    @Override
    public void init() {
        pedidoDAO = new PedidoDAO();
        carritoDAO = new CarritoDAO();
    }

    /**
     * GET /api/pedidos       -> pedidos del usuario autenticado
     *                           (o todos los pedidos si es administrador y envía ?todos=true)
     * GET /api/pedidos/{id}  -> pedido específico
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Usuario usuario = obtenerUsuarioSesion(request);
        if (usuario == null) {
            responderError(response, HttpServletResponse.SC_UNAUTHORIZED,
                    "Debe iniciar sesión para consultar pedidos.");
            return;
        }

        int idPedido = obtenerIdDeRuta(request);

        // GET /api/pedidos/{id}
        if (idPedido != -1) {
            Pedido pedido = pedidoDAO.buscarPorId(idPedido);
            if (pedido == null) {
                responderError(response, HttpServletResponse.SC_NOT_FOUND,
                        "No se encontró el pedido con id " + idPedido + ".");
                return;
            }
            // Un cliente solo puede consultar sus propios pedidos.
            if (!esAdministrador(usuario) && pedido.getIdUsuario() != usuario.getIdUsuario()) {
                responderError(response, HttpServletResponse.SC_FORBIDDEN,
                        "No tiene permiso para consultar este pedido.");
                return;
            }
            responderExito(response, "Pedido encontrado.", pedido);
            return;
        }

        // GET /api/pedidos?todos=true (solo administradores)
        String todos = request.getParameter("todos");
        if ("true".equalsIgnoreCase(todos) && esAdministrador(usuario)) {
            responderExito(response, "Pedidos obtenidos correctamente.", pedidoDAO.listarTodos());
            return;
        }

        // GET /api/pedidos -> pedidos propios
        List<Pedido> pedidos = pedidoDAO.listarPorUsuario(usuario.getIdUsuario());
        responderExito(response, "Pedidos obtenidos correctamente.", pedidos);
    }

    /**
     * POST /api/pedidos -> genera un pedido a partir del carrito
     * activo del usuario autenticado y vacía dicho carrito,
     * reproduciendo la misma lógica de PedidoServlet.
     * Cuerpo esperado: { "metodoPago": "tarjeta" }
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Usuario usuario = obtenerUsuarioSesion(request);
        if (usuario == null) {
            responderError(response, HttpServletResponse.SC_UNAUTHORIZED,
                    "Debe iniciar sesión para generar un pedido.");
            return;
        }

        DatosPedido datos = leerCuerpo(request, DatosPedido.class);
        if (datos == null || datos.metodoPago == null || datos.metodoPago.trim().isEmpty()) {
            responderError(response, HttpServletResponse.SC_BAD_REQUEST,
                    "Debe indicar el método de pago (metodoPago).");
            return;
        }

        Carrito carrito = carritoDAO.buscarCarritoActivo(usuario.getIdUsuario());
        if (carrito == null) {
            responderError(response, HttpServletResponse.SC_BAD_REQUEST,
                    "El usuario no tiene un carrito activo.");
            return;
        }

        List<DetalleCarrito> detalle = carritoDAO.listarDetalle(carrito.getIdCarrito());
        if (detalle.isEmpty()) {
            responderError(response, HttpServletResponse.SC_BAD_REQUEST, "El carrito está vacío.");
            return;
        }

        BigDecimal total = BigDecimal.ZERO;
        for (DetalleCarrito item : detalle) {
            total = total.add(BigDecimal.valueOf(item.getPrecio()).multiply(BigDecimal.valueOf(item.getCantidad())));
        }
        if (total.compareTo(BigDecimal.ZERO) <= 0) {
            responderError(response, HttpServletResponse.SC_BAD_REQUEST, "El total del pedido no es válido.");
            return;
        }

        Pedido pedido = new Pedido();
        pedido.setIdUsuario(usuario.getIdUsuario());
        pedido.setIdCarrito(carrito.getIdCarrito());
        pedido.setFechaPedido(LocalDateTime.now());
        pedido.setEstado("pendiente");
        pedido.setMetodoPago(datos.metodoPago);
        pedido.setTotal(total);

        int idPedido = pedidoDAO.registrar(pedido);
        if (idPedido <= 0) {
            responderError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "No fue posible registrar el pedido.");
            return;
        }

        // El contenido del carrito ya forma parte del pedido: se vacía.
        carritoDAO.vaciarCarrito(carrito.getIdCarrito());

        pedido.setIdPedido(idPedido);
        responder(response, HttpServletResponse.SC_CREATED,
                util.ApiRespuesta.exito("Pedido registrado correctamente.", pedido));
    }

    /**
     * PUT /api/pedidos/{id} -> actualiza el estado de un pedido.
     * Solo puede hacerlo un administrador.
     * Cuerpo esperado: { "estado": "enviado" }
     */
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Usuario usuario = obtenerUsuarioSesion(request);
        if (!esAdministrador(usuario)) {
            responderError(response, HttpServletResponse.SC_FORBIDDEN,
                    "Solo un administrador puede actualizar el estado de un pedido.");
            return;
        }

        int idPedido = obtenerIdDeRuta(request);
        if (idPedido == -1) {
            responderError(response, HttpServletResponse.SC_BAD_REQUEST,
                    "Debe indicar el id del pedido en la URL (/api/pedidos/{id}).");
            return;
        }

        Pedido existente = pedidoDAO.buscarPorId(idPedido);
        if (existente == null) {
            responderError(response, HttpServletResponse.SC_NOT_FOUND,
                    "No se encontró el pedido con id " + idPedido + ".");
            return;
        }

        DatosPedido datos = leerCuerpo(request, DatosPedido.class);
        if (datos == null || datos.estado == null || datos.estado.trim().isEmpty()) {
            responderError(response, HttpServletResponse.SC_BAD_REQUEST, "Debe indicar el nuevo estado.");
            return;
        }

        boolean actualizado = pedidoDAO.actualizarEstado(idPedido, datos.estado);
        if (actualizado) {
            existente.setEstado(datos.estado);
            responderExito(response, "Estado del pedido actualizado correctamente.", existente);
        } else {
            responderError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "No fue posible actualizar el estado del pedido.");
        }
    }

    /**
     * Clase interna utilizada para deserializar el cuerpo JSON
     * recibido al crear un pedido o actualizar su estado.
     */
    private static class DatosPedido {
        String metodoPago;
        String estado;
    }
}
