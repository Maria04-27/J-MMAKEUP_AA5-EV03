package controlador;

import dao.CarritoDAO;
import dao.ProductoDAO;
import dao.DetalleCarritoDAO;
import modelo.Carrito;
import modelo.Producto;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
/**
 * Servlet encargado de gestionar las operaciones
 * relacionadas con el carrito de compras de J&M Makeup.
 *
 * Permite:
 * - Consultar el carrito activo.
 * - Crear un carrito para el usuario.
 * - Agregar productos.
 * - Eliminar productos.
 * - Vaciar el carrito.
 * - Actualizar cantidades.
 *
 * Proyecto: J&M Makeup
 * Evidencia: GA7-220501096-AA3-EV01
 *
 * @author J&M Makeup
 */
@WebServlet("/CarritoServlet")
public class CarritoServlet extends HttpServlet {
    // DAO encargado de gestionar el carrito.
    private CarritoDAO carritoDAO;
    // DAO encargado de consultar los productos.
    private ProductoDAO productoDAO;
    // DAO encargado de gestionar los productos dentro del carrito.
    private DetalleCarritoDAO detalleCarritoDAO;
    /**
     * Inicializa los objetos DAO
     * cuando se inicia el Servlet.
     */
    @Override
    public void init() {
        carritoDAO = new CarritoDAO();
        productoDAO = new ProductoDAO();
        detalleCarritoDAO = new DetalleCarritoDAO();
    }

    /**
     * Procesa las solicitudes HTTP GET.
     *
     * Permite consultar el carrito, agregar productos
     * (desde el catálogo), eliminar productos y vaciar el carrito.
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
        // mostramos el carrito.
        if (accion == null || accion.isEmpty()) {
            mostrarCarrito(request, response);
            return;
        }
        switch (accion) {
            case "ver": mostrarCarrito(request, response);
                break;
            case "agregar": agregarProducto(request, response);
                break;
            case "eliminar": eliminarProducto(request, response);
                break;
            case "vaciar": vaciarCarrito(request, response);
                break;
            default: mostrarCarrito(request, response);
                break;
        }
    }

    /**
     * Procesa las solicitudes HTTP POST.
     *
     * Se utiliza para agregar productos
     * y actualizar cantidades.
     *
     * @param request solicitud HTTP
     * @param response respuesta HTTP
     * @throws ServletException si ocurre un error
     * @throws IOException si ocurre un error
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Configuramos UTF-8.
        request.setCharacterEncoding("UTF-8");
        // Obtenemos la acción enviada.
        String accion = request.getParameter("accion");
        if ("agregar".equals(accion)) {
            agregarProducto(request, response);
        } else if ("actualizar".equals(accion)) {
            actualizarCantidad(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/CarritoServlet?accion=ver");
        }
    }

    /**
     * Muestra el carrito activo del usuario.
     *
     * @param request solicitud HTTP
     * @param response respuesta HTTP
     * @throws ServletException si ocurre un error
     * @throws IOException si ocurre un error
     */
    private void mostrarCarrito(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Obtenemos la sesión actual.
        HttpSession session = request.getSession(false);

        // Verificamos si el usuario inició sesión.
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        // Obtenemos el objeto Usuario de la sesión.
        modelo.Usuario usuario = (modelo.Usuario)
                session.getAttribute("usuario");

        // Buscamos el carrito activo (datos generales: id, estado, total).
        Carrito carrito = carritoDAO.buscarCarritoActivo(usuario.getIdUsuario());

        // Lista de productos dentro del carrito (lo que realmente
        // se muestra en la tabla del JSP).
        java.util.List<modelo.DetalleCarrito> detalle = new java.util.ArrayList<>();

        // Solo consultamos el detalle si el usuario ya tiene un carrito.
        if (carrito != null) {
            detalle = carritoDAO.listarDetalle(carrito.getIdCarrito());
        }

        // Enviamos el DETALLE (la lista de productos) al JSP,
        // que es lo que el for del JSP recorre.
        request.setAttribute("carrito", detalle);

        // Mostramos la página del carrito.
        request.getRequestDispatcher("carrito.jsp").forward(request, response);
    }

    /**
     * Agrega un producto al carrito.
     *
     * @param request solicitud HTTP
     * @param response respuesta HTTP
     * @throws IOException si ocurre un error
     */
    private void agregarProducto(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        // Verificamos la sesión del usuario.
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        try {
            // Obtenemos el usuario de la sesión.
            modelo.Usuario usuario = (modelo.Usuario)
                    session.getAttribute("usuario");
            // Obtenemos el ID del producto.
            // Se usa "idProducto" para que coincida con el nombre
            // enviado desde productos.jsp y carrito.jsp.
            int idProducto = Integer.parseInt(request.getParameter("idProducto"));
            // Obtenemos la cantidad.
            int cantidad = 1;
            String cantidadParametro = request.getParameter("cantidad");
            if (cantidadParametro != null && !cantidadParametro.isEmpty()) {
                cantidad = Integer.parseInt(cantidadParametro);
            }
            // Consultamos el producto.
            Producto producto = productoDAO.buscarPorId(idProducto);
            // Verificamos que el producto exista.
            if (producto == null) {
                response.sendRedirect(request.getContextPath() + "/ProductoServlet?accion=listar");
                return;
            }
            // Verificamos que exista stock disponible.
            if (producto.getStock() < cantidad) {
                response.sendRedirect(request.getContextPath() + "/ProductoServlet?accion=listar");
                return;
            }
            // Buscamos el carrito activo.
            Carrito carrito = carritoDAO.buscarCarritoActivo(usuario.getIdUsuario());
            // Si el usuario no tiene carrito,
            // creamos uno nuevo.
            if (carrito == null) {
                int idCarrito = carritoDAO.crearCarrito(usuario.getIdUsuario());
                carrito = carritoDAO.buscarPorId(idCarrito);
            }
            // Agregamos el producto al carrito.
            // Si el producto ya estaba, el DAO suma la cantidad;
            // si no estaba, lo inserta como una fila nueva.
            // Pasamos también el precio actual del producto,
            // para guardarlo junto al detalle del carrito.
            // Usamos el precio efectivo (de oferta si aplica, o normal si no)
            detalleCarritoDAO.agregarProducto(carrito.getIdCarrito(), idProducto, cantidad, producto.getPrecioEfectivo());
            actualizarTotalCarrito(carrito.getIdCarrito());
            response.sendRedirect(request.getContextPath() + "/CarritoServlet?accion=ver&mensaje=agregado");
        } catch (NumberFormatException e) {
            System.err.println("Datos inválidos del carrito: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/ProductoServlet?accion=listar");
        }
    }

    /**
     * Actualiza la cantidad de un producto
     * dentro del carrito.
     *
     * @param request solicitud HTTP
     * @param response respuesta HTTP
     * @throws IOException si ocurre un error
     */
    private void actualizarCantidad(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        // Verificamos la sesión del usuario.
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        try {
            // Obtenemos el usuario de la sesión.
            modelo.Usuario usuario = (modelo.Usuario)
                    session.getAttribute("usuario");
            // Obtenemos el producto y la nueva cantidad enviados desde el formulario.
            int idProducto = Integer.parseInt(request.getParameter("idProducto"));
            int cantidad = Integer.parseInt(request.getParameter("cantidad"));
            // Buscamos el carrito activo del usuario.
            Carrito carrito = carritoDAO.buscarCarritoActivo(usuario.getIdUsuario());
            if (carrito != null) {
                // Actualizamos la cantidad del producto dentro del carrito.
                detalleCarritoDAO.actualizarCantidad(carrito.getIdCarrito(), idProducto, cantidad);
                actualizarTotalCarrito(carrito.getIdCarrito());
            }
        } catch (NumberFormatException e) {
            System.err.println("Datos inválidos al actualizar cantidad: " + e.getMessage());
        }
        response.sendRedirect(request.getContextPath() + "/CarritoServlet?accion=ver");
    }

    /**
     * Elimina un producto del carrito.
     *
     * @param request solicitud HTTP
     * @param response respuesta HTTP
     * @throws IOException si ocurre un error
     */
    private void eliminarProducto(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        // Verificamos la sesión del usuario.
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        try {
            // Obtenemos el usuario de la sesión.
            modelo.Usuario usuario = (modelo.Usuario)
                    session.getAttribute("usuario");
            // Obtenemos el producto a eliminar, enviado por parámetro en la URL.
            int idProducto = Integer.parseInt(request.getParameter("idProducto"));
            // Buscamos el carrito activo del usuario.
            Carrito carrito = carritoDAO.buscarCarritoActivo(usuario.getIdUsuario());
            if (carrito != null) {
                // Eliminamos el producto del carrito.
                detalleCarritoDAO.eliminarProducto(carrito.getIdCarrito(), idProducto);
                actualizarTotalCarrito(carrito.getIdCarrito());
            }
        } catch (NumberFormatException e) {
            System.err.println("Datos inválidos al eliminar producto: " + e.getMessage());
        }
        response.sendRedirect(request.getContextPath() + "/CarritoServlet?accion=ver&mensaje=eliminado");
    }

    /**
     * Vacía completamente el carrito.
     *
     * @param request solicitud HTTP
     * @param response respuesta HTTP
     * @throws IOException si ocurre un error
     */
    private void vaciarCarrito(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        // Obtenemos la sesión.
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        try {
            // Obtenemos el usuario.
            modelo.Usuario usuario = (modelo.Usuario)
                    session.getAttribute("usuario");
            // Buscamos el carrito activo.
            Carrito carrito = carritoDAO.buscarCarritoActivo(usuario.getIdUsuario());
            // Si existe un carrito,
            // eliminamos sus productos.
            if (carrito != null) {
                carritoDAO.vaciarCarrito(carrito.getIdCarrito());
            }
        } catch (Exception e) {
            System.err.println("Error al vaciar carrito: " + e.getMessage());
        }
        // Regresamos al carrito.
        response.sendRedirect(request.getContextPath() + "/CarritoServlet?accion=ver&mensaje=vaciado");
    }
    /**
     * Recalcula y actualiza el total del carrito
     * a partir de los productos que contiene.
     *
     * Se debe llamar después de agregar, actualizar
     * o eliminar productos del carrito.
     *
     * @param idCarrito identificador del carrito
     */
    private void actualizarTotalCarrito(int idCarrito) {
        // Consultamos los productos actuales del carrito.
        java.util.List<modelo.DetalleCarrito> detalle = carritoDAO.listarDetalle(idCarrito);
        // Sumamos el subtotal de cada producto.
        java.math.BigDecimal total = java.math.BigDecimal.ZERO;
        for (modelo.DetalleCarrito item : detalle) {
            java.math.BigDecimal subtotal = java.math.BigDecimal.valueOf(item.getPrecio())
                    .multiply(java.math.BigDecimal.valueOf(item.getCantidad()));
            total = total.add(subtotal);
        }
        // Guardamos el nuevo total en la base de datos.
        carritoDAO.actualizarTotal(idCarrito, total);
    }
}