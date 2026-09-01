package api;

import dao.CategoriaDAO;
import modelo.Categoria;
import modelo.Usuario;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * Servicio web (API REST) encargado de exponer
 * las operaciones relacionadas con las categorías
 * de productos de la tienda virtual J&M Makeup.
 *
 * Recurso: /api/categorias
 *
 * Rutas expuestas:
 * - GET    /api/categorias        -> lista todas las categorías
 * - GET    /api/categorias/{id}   -> consulta una categoría por id
 * - POST   /api/categorias        -> registra una categoría (admin)
 * - PUT    /api/categorias/{id}   -> actualiza una categoría (admin)
 * - DELETE /api/categorias/{id}   -> elimina una categoría (admin)
 *
 * Proyecto: J&M Makeup
 * Evidencia: GA7-220501096-AA5-EV03
 *
 * @author J&M Makeup
 */
@WebServlet("/api/categorias/*")
public class CategoriaApiServlet extends ApiServlet {

    // DAO encargado de las operaciones sobre categorías.
    private CategoriaDAO categoriaDAO;

    @Override
    public void init() {
        categoriaDAO = new CategoriaDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int idCategoria = obtenerIdDeRuta(request);

        // GET /api/categorias/{id}
        if (idCategoria != -1) {
            Categoria categoria = categoriaDAO.buscarPorId(idCategoria);
            if (categoria == null) {
                responderError(response, HttpServletResponse.SC_NOT_FOUND,
                        "No se encontró la categoría con id " + idCategoria + ".");
                return;
            }
            responderExito(response, "Categoría encontrada.", categoria);
            return;
        }

        // GET /api/categorias
        List<Categoria> categorias = categoriaDAO.listarTodas();
        responderExito(response, "Categorías obtenidas correctamente.", categorias);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Usuario usuario = obtenerUsuarioSesion(request);
        if (!esAdministrador(usuario)) {
            responderError(response, HttpServletResponse.SC_FORBIDDEN,
                    "Solo un administrador puede registrar categorías.");
            return;
        }

        Categoria categoria = leerCuerpo(request, Categoria.class);
        if (categoria == null || categoria.getNombre() == null || categoria.getNombre().trim().isEmpty()) {
            responderError(response, HttpServletResponse.SC_BAD_REQUEST,
                    "Debe enviar el nombre de la categoría.");
            return;
        }

        boolean registrada = categoriaDAO.registrar(categoria);
        if (registrada) {
            responder(response, HttpServletResponse.SC_CREATED,
                    util.ApiRespuesta.exito("Categoría registrada correctamente.", categoria));
        } else {
            responderError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "No fue posible registrar la categoría.");
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Usuario usuario = obtenerUsuarioSesion(request);
        if (!esAdministrador(usuario)) {
            responderError(response, HttpServletResponse.SC_FORBIDDEN,
                    "Solo un administrador puede actualizar categorías.");
            return;
        }

        int idCategoria = obtenerIdDeRuta(request);
        if (idCategoria == -1) {
            responderError(response, HttpServletResponse.SC_BAD_REQUEST,
                    "Debe indicar el id de la categoría en la URL (/api/categorias/{id}).");
            return;
        }

        Categoria existente = categoriaDAO.buscarPorId(idCategoria);
        if (existente == null) {
            responderError(response, HttpServletResponse.SC_NOT_FOUND,
                    "No se encontró la categoría con id " + idCategoria + ".");
            return;
        }

        Categoria categoria = leerCuerpo(request, Categoria.class);
        if (categoria == null) {
            responderError(response, HttpServletResponse.SC_BAD_REQUEST, "Debe enviar los datos de la categoría.");
            return;
        }
        categoria.setIdCategoria(idCategoria);

        boolean actualizada = categoriaDAO.actualizar(categoria);
        if (actualizada) {
            responderExito(response, "Categoría actualizada correctamente.", categoria);
        } else {
            responderError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "No fue posible actualizar la categoría.");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Usuario usuario = obtenerUsuarioSesion(request);
        if (!esAdministrador(usuario)) {
            responderError(response, HttpServletResponse.SC_FORBIDDEN,
                    "Solo un administrador puede eliminar categorías.");
            return;
        }

        int idCategoria = obtenerIdDeRuta(request);
        if (idCategoria == -1) {
            responderError(response, HttpServletResponse.SC_BAD_REQUEST,
                    "Debe indicar el id de la categoría en la URL (/api/categorias/{id}).");
            return;
        }

        boolean eliminada = categoriaDAO.eliminar(idCategoria);
        if (eliminada) {
            responderExito(response, "Categoría eliminada correctamente.", null);
        } else {
            responderError(response, HttpServletResponse.SC_NOT_FOUND,
                    "No se encontró la categoría con id " + idCategoria + ".");
        }
    }
}