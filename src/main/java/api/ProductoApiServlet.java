package api;

import dao.ProductoDAO;
import modelo.Producto;
import modelo.Usuario;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * Servicio web (API REST) encargado de exponer
 * las operaciones relacionadas con los productos
 * de la tienda virtual J&M Makeup.
 *
 * Recurso: /api/productos
 *
 * Rutas expuestas:
 * - GET    /api/productos            -> lista todos los productos
 * - GET    /api/productos/ofertas    -> lista los productos en oferta
 * - GET    /api/productos/{id}       -> consulta un producto por id
 * - POST   /api/productos            -> registra un producto (admin)
 * - PUT    /api/productos/{id}       -> actualiza un producto (admin)
 * - DELETE /api/productos/{id}       -> elimina un producto (admin)
 *
 * Proyecto: J&M Makeup
 * Evidencia: GA7-220501096-AA5-EV03
 *
 * @author J&M Makeup
 */
@WebServlet("/api/productos/*")
public class ProductoApiServlet extends ApiServlet {

    // DAO encargado de las operaciones sobre productos.
    private ProductoDAO productoDAO;

    @Override
    public void init() {
        productoDAO = new ProductoDAO();
    }

    /**
     * Atiende las peticiones GET del recurso productos.
     * Es un servicio público: no requiere sesión, ya que
     * el catálogo debe poder consultarse sin haber iniciado sesión.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pathInfo = request.getPathInfo();

        // GET /api/productos/ofertas
        if (pathInfo != null && pathInfo.equals("/ofertas")) {
            List<Producto> ofertas = productoDAO.listarOfertas();
            responderExito(response, "Productos en oferta obtenidos correctamente.", ofertas);
            return;
        }

        int idProducto = obtenerIdDeRuta(request);

        // GET /api/productos/{id}
        if (idProducto != -1) {
            Producto producto = productoDAO.buscarPorId(idProducto);
            if (producto == null) {
                responderError(response, HttpServletResponse.SC_NOT_FOUND,
                        "No se encontró el producto con id " + idProducto + ".");
                return;
            }
            responderExito(response, "Producto encontrado.", producto);
            return;
        }

        // GET /api/productos
        List<Producto> productos = productoDAO.listarTodos();
        responderExito(response, "Productos obtenidos correctamente.", productos);
    }

    /**
     * Atiende las peticiones POST del recurso productos.
     * Registra un nuevo producto. Requiere sesión de administrador.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Usuario usuario = obtenerUsuarioSesion(request);
        if (!esAdministrador(usuario)) {
            responderError(response, HttpServletResponse.SC_FORBIDDEN,
                    "Solo un administrador puede registrar productos.");
            return;
        }

        Producto producto = leerCuerpo(request, Producto.class);
        if (producto == null || producto.getNombre() == null || producto.getNombre().trim().isEmpty()
                || producto.getPrecio() == null) {
            responderError(response, HttpServletResponse.SC_BAD_REQUEST,
                    "Debe enviar al menos el nombre y el precio del producto.");
            return;
        }

        boolean registrado = productoDAO.registrar(producto);
        if (registrado) {
            responder(response, HttpServletResponse.SC_CREATED,
                    util.ApiRespuesta.exito("Producto registrado correctamente.", producto));
        } else {
            responderError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "No fue posible registrar el producto.");
        }
    }

    /**
     * Atiende las peticiones PUT del recurso productos.
     * Actualiza un producto existente. Requiere sesión de administrador.
     */
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Usuario usuario = obtenerUsuarioSesion(request);
        if (!esAdministrador(usuario)) {
            responderError(response, HttpServletResponse.SC_FORBIDDEN,
                    "Solo un administrador puede actualizar productos.");
            return;
        }

        int idProducto = obtenerIdDeRuta(request);
        if (idProducto == -1) {
            responderError(response, HttpServletResponse.SC_BAD_REQUEST,
                    "Debe indicar el id del producto en la URL (/api/productos/{id}).");
            return;
        }

        Producto existente = productoDAO.buscarPorId(idProducto);
        if (existente == null) {
            responderError(response, HttpServletResponse.SC_NOT_FOUND,
                    "No se encontró el producto con id " + idProducto + ".");
            return;
        }

        Producto producto = leerCuerpo(request, Producto.class);
        if (producto == null) {
            responderError(response, HttpServletResponse.SC_BAD_REQUEST, "Debe enviar los datos del producto.");
            return;
        }
        producto.setIdProducto(idProducto);

        boolean actualizado = productoDAO.actualizar(producto);
        if (actualizado) {
            responderExito(response, "Producto actualizado correctamente.", producto);
        } else {
            responderError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "No fue posible actualizar el producto.");
        }
    }

    /**
     * Atiende las peticiones DELETE del recurso productos.
     * Elimina un producto existente. Requiere sesión de administrador.
     */
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Usuario usuario = obtenerUsuarioSesion(request);
        if (!esAdministrador(usuario)) {
            responderError(response, HttpServletResponse.SC_FORBIDDEN,
                    "Solo un administrador puede eliminar productos.");
            return;
        }

        int idProducto = obtenerIdDeRuta(request);
        if (idProducto == -1) {
            responderError(response, HttpServletResponse.SC_BAD_REQUEST,
                    "Debe indicar el id del producto en la URL (/api/productos/{id}).");
            return;
        }

        boolean eliminado = productoDAO.eliminar(idProducto);
        if (eliminado) {
            responderExito(response, "Producto eliminado correctamente.", null);
        } else {
            responderError(response, HttpServletResponse.SC_NOT_FOUND,
                    "No se encontró el producto con id " + idProducto + ".");
        }
    }
}
