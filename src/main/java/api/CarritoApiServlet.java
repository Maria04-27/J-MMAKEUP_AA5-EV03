package api;

import dao.CarritoDAO;
import dao.DetalleCarritoDAO;
import dao.ProductoDAO;
import modelo.Carrito;
import modelo.DetalleCarrito;
import modelo.Producto;
import modelo.Usuario;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Servicio web (API REST) encargado de exponer
 * las operaciones relacionadas con el carrito de compras
 * de la tienda virtual J&M Makeup.
 *
 * Todas las rutas de este servicio requieren que el usuario
 * tenga una sesión activa (haber iniciado sesión).
 *
 * Recurso: /api/carrito
 *
 * Rutas expuestas:
 * - GET    /api/carrito             -> consulta el carrito activo (con su detalle y total)
 * - POST   /api/carrito             -> agrega un producto al carrito {idProducto, cantidad}
 * - PUT    /api/carrito/{idProducto}-> actualiza la cantidad de un producto {cantidad}
 * - DELETE /api/carrito/{idProducto}-> elimina un producto del carrito
 * - DELETE /api/carrito             -> vacía por completo el carrito
 *
 * Proyecto: J&M Makeup
 * Evidencia: GA7-220501096-AA5-EV03
 *
 * @author J&M Makeup
 */
@WebServlet("/api/carrito/*")
public class CarritoApiServlet extends ApiServlet {

    // DAO encargado del carrito.
    private CarritoDAO carritoDAO;
    // DAO encargado de los productos dentro del carrito.
    private DetalleCarritoDAO detalleCarritoDAO;
    // DAO encargado de consultar los productos.
    private ProductoDAO productoDAO;

    @Override
    public void init() {
        carritoDAO = new CarritoDAO();
        detalleCarritoDAO = new DetalleCarritoDAO();
        productoDAO = new ProductoDAO();
    }

    /**
     * GET /api/carrito -> consulta (o crea si no existe) el carrito
     * activo del usuario autenticado, junto con su detalle.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Usuario usuario = obtenerUsuarioSesion(request);
        if (usuario == null) {
            responderError(response, HttpServletResponse.SC_UNAUTHORIZED,
                    "Debe iniciar sesión para consultar el carrito.");
            return;
        }

        Carrito carrito = obtenerOCrearCarrito(usuario.getIdUsuario());
        List<DetalleCarrito> detalle = carritoDAO.listarDetalle(carrito.getIdCarrito());

        Map<String, Object> datos = new HashMap<>();
        datos.put("carrito", carrito);
        datos.put("detalle", detalle);

        responderExito(response, "Carrito obtenido correctamente.", datos);
    }

    /**
     * POST /api/carrito -> agrega un producto al carrito.
     * Cuerpo esperado: { "idProducto": 1, "cantidad": 2 }
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Usuario usuario = obtenerUsuarioSesion(request);
        if (usuario == null) {
            responderError(response, HttpServletResponse.SC_UNAUTHORIZED,
                    "Debe iniciar sesión para agregar productos al carrito.");
            return;
        }

        ItemCarrito item = leerCuerpo(request, ItemCarrito.class);
        if (item == null || item.idProducto <= 0 || item.cantidad <= 0) {
            responderError(response, HttpServletResponse.SC_BAD_REQUEST,
                    "Debe enviar un idProducto y una cantidad mayores a cero.");
            return;
        }

        Producto producto = productoDAO.buscarPorId(item.idProducto);
        if (producto == null) {
            responderError(response, HttpServletResponse.SC_NOT_FOUND, "El producto indicado no existe.");
            return;
        }

        Carrito carrito = obtenerOCrearCarrito(usuario.getIdUsuario());

        boolean agregado = detalleCarritoDAO.agregarProducto(
                carrito.getIdCarrito(), item.idProducto, item.cantidad, producto.getPrecioEfectivo());

        if (!agregado) {
            responderError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "No fue posible agregar el producto al carrito.");
            return;
        }

        actualizarTotalCarrito(carrito);
        List<DetalleCarrito> detalle = carritoDAO.listarDetalle(carrito.getIdCarrito());
        responderExito(response, "Producto agregado al carrito.", detalle);
    }

    /**
     * PUT /api/carrito/{idProducto} -> actualiza la cantidad de un producto.
     * Cuerpo esperado: { "cantidad": 3 }
     */
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Usuario usuario = obtenerUsuarioSesion(request);
        if (usuario == null) {
            responderError(response, HttpServletResponse.SC_UNAUTHORIZED,
                    "Debe iniciar sesión para modificar el carrito.");
            return;
        }

        int idProducto = obtenerIdDeRuta(request);
        if (idProducto == -1) {
            responderError(response, HttpServletResponse.SC_BAD_REQUEST,
                    "Debe indicar el id del producto en la URL (/api/carrito/{idProducto}).");
            return;
        }

        ItemCarrito item = leerCuerpo(request, ItemCarrito.class);
        if (item == null || item.cantidad <= 0) {
            responderError(response, HttpServletResponse.SC_BAD_REQUEST,
                    "Debe enviar una cantidad mayor a cero.");
            return;
        }

        Carrito carrito = carritoDAO.buscarCarritoActivo(usuario.getIdUsuario());
        if (carrito == null) {
            responderError(response, HttpServletResponse.SC_NOT_FOUND, "El usuario no tiene un carrito activo.");
            return;
        }

        boolean actualizado = detalleCarritoDAO.actualizarCantidad(carrito.getIdCarrito(), idProducto, item.cantidad);
        if (!actualizado) {
            responderError(response, HttpServletResponse.SC_NOT_FOUND,
                    "El producto indicado no está en el carrito.");
            return;
        }

        actualizarTotalCarrito(carrito);
        List<DetalleCarrito> detalle = carritoDAO.listarDetalle(carrito.getIdCarrito());
        responderExito(response, "Cantidad actualizada correctamente.", detalle);
    }

    /**
     * DELETE /api/carrito/{idProducto} -> elimina un producto del carrito.
     * DELETE /api/carrito              -> vacía el carrito completo.
     */
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Usuario usuario = obtenerUsuarioSesion(request);
        if (usuario == null) {
            responderError(response, HttpServletResponse.SC_UNAUTHORIZED,
                    "Debe iniciar sesión para modificar el carrito.");
            return;
        }

        Carrito carrito = carritoDAO.buscarCarritoActivo(usuario.getIdUsuario());
        if (carrito == null) {
            responderError(response, HttpServletResponse.SC_NOT_FOUND, "El usuario no tiene un carrito activo.");
            return;
        }

        int idProducto = obtenerIdDeRuta(request);

        if (idProducto == -1) {
            // DELETE /api/carrito -> vaciar todo el carrito
            carritoDAO.vaciarCarrito(carrito.getIdCarrito());
            responderExito(response, "Carrito vaciado correctamente.", null);
            return;
        }

        // DELETE /api/carrito/{idProducto}
        boolean eliminado = detalleCarritoDAO.eliminarProducto(carrito.getIdCarrito(), idProducto);
        if (!eliminado) {
            responderError(response, HttpServletResponse.SC_NOT_FOUND,
                    "El producto indicado no está en el carrito.");
            return;
        }

        actualizarTotalCarrito(carrito);
        responderExito(response, "Producto eliminado del carrito.", null);
    }

    /**
     * Busca el carrito activo del usuario; si no existe,
     * crea uno nuevo automáticamente. Reutiliza la misma
     * lógica que CarritoServlet.
     *
     * @param idUsuario identificador del usuario
     * @return carrito activo (existente o recién creado)
     */
    private Carrito obtenerOCrearCarrito(int idUsuario) {
        Carrito carrito = carritoDAO.buscarCarritoActivo(idUsuario);
        if (carrito == null) {
            int idCarrito = carritoDAO.crearCarrito(idUsuario);
            carrito = carritoDAO.buscarPorId(idCarrito);
        }
        return carrito;
    }

    /**
     * Recalcula y guarda el total del carrito
     * sumando el subtotal de cada producto.
     *
     * @param carrito carrito que se desea actualizar
     */
    private void actualizarTotalCarrito(Carrito carrito) {
        List<DetalleCarrito> detalle = carritoDAO.listarDetalle(carrito.getIdCarrito());
        BigDecimal total = BigDecimal.ZERO;
        for (DetalleCarrito item : detalle) {
            total = total.add(BigDecimal.valueOf(item.getPrecio()).multiply(BigDecimal.valueOf(item.getCantidad())));
        }
        carritoDAO.actualizarTotal(carrito.getIdCarrito(), total);
    }

    /**
     * Clase interna utilizada únicamente para deserializar
     * el cuerpo JSON enviado al agregar o actualizar
     * un producto del carrito.
     */
    private static class ItemCarrito {
        int idProducto;
        int cantidad;
    }
}