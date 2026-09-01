package controlador;

import dao.CarritoDAO;
import dao.PedidoDAO;
import modelo.Carrito;
import modelo.DetalleCarrito;
import modelo.Pedido;
import modelo.Usuario;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
/**
 * Servlet encargado de gestionar las operaciones
 * relacionadas con los pedidos de J&M Makeup.
 *
 * Permite:
 * - Mostrar el formulario de finalización de compra.
 * - Registrar un nuevo pedido.
 * - Consultar un pedido.
 * - Consultar los pedidos del usuario.
 *
 * Proyecto: J&M Makeup
 * Evidencia: GA7-220501096-AA3-EV01
 *
 * @author J&M Makeup
 */
@WebServlet("/PedidoServlet")
public class PedidoServlet extends HttpServlet {
    // DAO encargado de gestionar los pedidos.
    private PedidoDAO pedidoDAO;
    // DAO encargado de gestionar los carritos.
    private CarritoDAO carritoDAO;
    /**
     * Inicializa los objetos DAO
     * cuando se inicia el Servlet.
     */
    @Override
    public void init() {
        pedidoDAO = new PedidoDAO();
        carritoDAO = new CarritoDAO();
    }

    /**
     * Procesa las solicitudes HTTP GET.
     *
     * Permite mostrar la información de un pedido
     * o consultar los pedidos del usuario.
     *
     * @param request solicitud HTTP
     * @param response respuesta HTTP
     * @throws ServletException si ocurre un error
     * @throws IOException si ocurre un error de entrada o salida
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Obtenemos la acción solicitada.
        String accion = request.getParameter("accion");
        // Si no se especifica una acción,
        // mostramos el formulario de compra.
        if (accion == null || accion.isEmpty()) {
            mostrarFinalizarCompra(request, response);
            return;
        }
        switch (accion) {
            case "finalizar": mostrarFinalizarCompra(request, response);
                break;
            case "crear": mostrarFinalizarCompra(request, response);
                break;
            case "ver": verPedido(request, response);
                break;
            case "misPedidos": listarMisPedidos(request, response);
                break;
            default: mostrarFinalizarCompra(request, response);
                break;
        }
    }

    /**
     * Procesa las solicitudes HTTP POST.
     *
     * Se utiliza para registrar un nuevo pedido.
     *
     * @param request solicitud HTTP
     * @param response respuesta HTTP
     * @throws ServletException si ocurre un error
     * @throws IOException si ocurre un error de entrada o salida
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Configuramos UTF-8.
        request.setCharacterEncoding("UTF-8");
        // Obtenemos la acción enviada.
        String accion = request.getParameter("accion");
        if ("confirmar".equals(accion)) {
            registrarPedido(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/PedidoServlet?accion=finalizar");
        }
    }

    /**
     * Muestra la pantalla para finalizar la compra.
     *
     * Envía a pago.jsp tanto el objeto Carrito como
     * la lista real de productos (detalle), para que
     * la página pueda mostrar el resumen correcto.
     *
     * @param request solicitud HTTP
     * @param response respuesta HTTP
     * @throws ServletException si ocurre un error
     * @throws IOException si ocurre un error de entrada o salida
     */
    private void mostrarFinalizarCompra(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Verificamos la sesión del usuario.
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        // Obtenemos el usuario de la sesión.
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        // Buscamos el carrito activo.
        Carrito carrito = carritoDAO.buscarCarritoActivo(usuario.getIdUsuario());
        // Si no tiene carrito, lo regresamos al catálogo.
        if (carrito == null) {
            response.sendRedirect(request.getContextPath() + "/ProductoServlet?accion=listar");
            return;
        }
        // Consultamos los productos reales del carrito.
        List<DetalleCarrito> detalle = carritoDAO.listarDetalle(carrito.getIdCarrito());
        // Si el carrito está vacío, no tiene sentido pagar.
        if (detalle.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/CarritoServlet?accion=ver");
            return;
        }
        // Calculamos el total real sumando cada producto.
        BigDecimal total = calcularTotal(detalle);
        // Enviamos los datos a la página de pago.
        request.setAttribute("detalle", detalle);
        request.setAttribute("total", total);
        // Mostramos la página de pago.
        request.getRequestDispatcher("pago.jsp").forward(request, response);
    }

    /**
     * Registra un nuevo pedido.
     *
     * @param request solicitud HTTP
     * @param response respuesta HTTP
     * @throws IOException si ocurre un error
     */
    private void registrarPedido(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        // Obtenemos la sesión actual.
        HttpSession session = request.getSession(false);
        // Verificamos que el usuario
        // haya iniciado sesión.
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        try {
            // Obtenemos el usuario de la sesión.
            Usuario usuario = (Usuario) session.getAttribute("usuario");
            // Obtenemos el método de pago
            // seleccionado por el cliente.
            // Se usa "metodoPago" para coincidir con el name
            // del formulario en pago.jsp.
            String metodoPago = request.getParameter("metodoPago");
            // Validamos el método de pago.
            if (metodoPago == null || metodoPago.trim().isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/PedidoServlet?accion=finalizar");
                return;
            }
            // Buscamos el carrito activo.
            Carrito carrito = carritoDAO.buscarCarritoActivo(usuario.getIdUsuario());
            // Verificamos que exista un carrito.
            if (carrito == null) {
                response.sendRedirect(request.getContextPath() + "/CarritoServlet?accion=ver");
                return;
            }

            // Calculamos el total real a partir de los productos,
            // en lugar de confiar en la columna total del carrito.
            List<DetalleCarrito> detalle = carritoDAO.listarDetalle(carrito.getIdCarrito());
            BigDecimal total = calcularTotal(detalle);
            // Validamos que el total sea válido.
            if (total.compareTo(BigDecimal.ZERO) <= 0) {
                response.sendRedirect(request.getContextPath() + "/CarritoServlet?accion=ver");
                return;
            }

            // Creamos el objeto Pedido.
            Pedido pedido = new Pedido();
            // Asignamos el usuario.
            pedido.setIdUsuario(usuario.getIdUsuario());
            // Asignamos el carrito.
            pedido.setIdCarrito(carrito.getIdCarrito());
            // Asignamos la fecha actual.
            pedido.setFechaPedido(LocalDateTime.now());
            // Estado inicial del pedido.
            pedido.setEstado("pendiente");
            // Método de pago seleccionado.
            pedido.setMetodoPago(metodoPago);
            // Total de la compra.
            pedido.setTotal(total);
            // Registramos el pedido.
            int idPedido = pedidoDAO.registrar(pedido);
            // Verificamos si se registró correctamente.
            if (idPedido > 0) {
                // Vaciamos el carrito, ya que su contenido
                // pasó a formar parte del pedido.
                carritoDAO.vaciarCarrito(carrito.getIdCarrito());
                // Enviamos el ID del pedido
                // a la página de confirmación.
                response.sendRedirect(request.getContextPath() + "/PedidoServlet?accion=ver&id=" + idPedido);
            } else {
                // Si ocurrió un error,
                // regresamos al carrito.
                response.sendRedirect(request.getContextPath() + "/CarritoServlet?accion=ver");
            }

        } catch (Exception e) {
            System.err.println("Error al registrar pedido: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/CarritoServlet?accion=ver");
        }
    }

    /**
     * Consulta un pedido específico.
     *
     * @param request solicitud HTTP
     * @param response respuesta HTTP
     * @throws ServletException si ocurre un error
     * @throws IOException si ocurre un error
     */
    private void verPedido(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Verificamos la sesión.
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        try {
            // Obtenemos el ID del pedido.
            int idPedido = Integer.parseInt(request.getParameter("id"));

            // Buscamos el pedido.
            Pedido pedido = pedidoDAO.buscarPorId(idPedido);

            // Enviamos el pedido a la JSP.
            request.setAttribute("pedido", pedido);

            // Mostramos la confirmación.
            request.getRequestDispatcher("confirmacion-pedido.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            // Si el ID no es válido,
            // regresamos al carrito.
            response.sendRedirect(request.getContextPath() + "/CarritoServlet?accion=ver");
        }
    }

    /**
     * Consulta todos los pedidos realizados
     * por el usuario que inició sesión.
     *
     * @param request solicitud HTTP
     * @param response respuesta HTTP
     * @throws ServletException si ocurre un error
     * @throws IOException si ocurre un error
     */
    private void listarMisPedidos(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Verificamos la sesión.
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // Obtenemos el usuario.
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        // Consultamos sus pedidos.
        List<Pedido> pedidos = pedidoDAO.listarPorUsuario(usuario.getIdUsuario());

        // Enviamos la lista a la JSP.
        request.setAttribute("pedidos", pedidos);

        // Mostramos la página de pedidos.
        request.getRequestDispatcher("mis-pedidos.jsp").forward(request, response);
    }

    /**
     * Calcula el total sumando el subtotal
     * de cada producto del carrito.
     *
     * @param detalle lista de productos del carrito
     * @return total calculado
     */
    private BigDecimal calcularTotal(List<DetalleCarrito> detalle) {
        BigDecimal total = BigDecimal.ZERO;
        for (DetalleCarrito item : detalle) {
            BigDecimal subtotal = BigDecimal.valueOf(item.getPrecio())
                    .multiply(BigDecimal.valueOf(item.getCantidad()));
            total = total.add(subtotal);
        }
        return total;
    }
}