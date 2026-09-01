package api;

import dao.MensajeDAO;
import modelo.Mensaje;
import modelo.Usuario;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * Servicio web (API REST) encargado de exponer
 * las operaciones relacionadas con los mensajes
 * enviados desde el formulario de contacto de
 * la tienda virtual J&M Makeup.
 *
 * Recurso: /api/mensajes
 *
 * Rutas expuestas:
 * - POST /api/mensajes -> registra un mensaje de contacto (público)
 * - GET  /api/mensajes -> lista todos los mensajes recibidos (admin)
 *
 * Proyecto: J&M Makeup
 * Evidencia: GA7-220501096-AA5-EV03
 *
 * @author J&M Makeup
 */
@WebServlet("/api/mensajes/*")
public class MensajeApiServlet extends ApiServlet {

    // DAO encargado de los mensajes de contacto.
    private MensajeDAO mensajeDAO;

    @Override
    public void init() {
        mensajeDAO = new MensajeDAO();
    }

    /**
     * GET /api/mensajes -> lista todos los mensajes de contacto.
     * Solo puede consultarlos un administrador.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Usuario usuario = obtenerUsuarioSesion(request);
        if (!esAdministrador(usuario)) {
            responderError(response, HttpServletResponse.SC_FORBIDDEN,
                    "Solo un administrador puede consultar los mensajes de contacto.");
            return;
        }

        List<Mensaje> mensajes = mensajeDAO.listarTodos();
        responderExito(response, "Mensajes obtenidos correctamente.", mensajes);
    }

    /**
     * POST /api/mensajes -> registra un nuevo mensaje de contacto.
     * Es un servicio público: cualquier visitante puede enviarlo.
     * Cuerpo esperado: { "nombre", "correo", "asunto", "contenido" }
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Mensaje mensaje = leerCuerpo(request, Mensaje.class);

        if (mensaje == null || mensaje.getNombre() == null || mensaje.getNombre().trim().isEmpty()
                || mensaje.getCorreo() == null || mensaje.getCorreo().trim().isEmpty()
                || mensaje.getContenido() == null || mensaje.getContenido().trim().isEmpty()) {
            responderError(response, HttpServletResponse.SC_BAD_REQUEST,
                    "Debe enviar al menos el nombre, el correo y el contenido del mensaje.");
            return;
        }

        boolean guardado = mensajeDAO.guardar(mensaje);
        if (guardado) {
            responder(response, HttpServletResponse.SC_CREATED,
                    util.ApiRespuesta.exito("Mensaje enviado correctamente.", mensaje));
        } else {
            responderError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "No fue posible enviar el mensaje.");
        }
    }
}