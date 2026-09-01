package api;

import modelo.Usuario;
import util.ApiRespuesta;
import util.JsonUtil;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Clase base abstracta para todos los Servlets que conforman
 * la API REST (servicios web) de la tienda virtual J&M Makeup.
 *
 * Centraliza las operaciones comunes a todos los servicios:
 * - Lectura del cuerpo (body) de la petición en formato JSON.
 * - Envío de respuestas en formato JSON, con el código
 *   de estado HTTP correspondiente.
 * - Validación de la sesión del usuario autenticado.
 * - Validación del rol de administrador.
 *
 * De esta forma, cada Servlet de la API (ProductoApiServlet,
 * CategoriaApiServlet, UsuarioApiServlet, CarritoApiServlet,
 * PedidoApiServlet y MensajeApiServlet) se enfoca únicamente
 * en su propia lógica de negocio.
 *
 * Proyecto: J&M Makeup
 * Evidencia: GA7-220501096-AA5-EV03
 *
 * @author J&M Makeup
 */
public abstract class ApiServlet extends HttpServlet {

    /**
     * Lee el cuerpo (body) de la petición HTTP y lo
     * convierte en un objeto Java del tipo indicado.
     *
     * @param request solicitud HTTP recibida
     * @param clase clase del objeto que se desea obtener
     * @return objeto construido a partir del JSON recibido,
     *         o null si el cuerpo está vacío o mal formado
     * @throws IOException si ocurre un error de lectura
     */
    protected <T> T leerCuerpo(HttpServletRequest request, Class<T> clase) throws IOException {
        StringBuilder json = new StringBuilder();
        try (BufferedReader lector = request.getReader()) {
            String linea;
            while ((linea = lector.readLine()) != null) {
                json.append(linea);
            }
        }
        if (json.length() == 0) {
            return null;
        }
        try {
            return JsonUtil.getGson().fromJson(json.toString(), clase);
        } catch (Exception e) {
            System.err.println("Error al leer el cuerpo de la petición: " + e.getMessage());
            return null;
        }
    }

    /**
     * Envía una respuesta en formato JSON al cliente,
     * junto con el código de estado HTTP correspondiente.
     *
     * @param response respuesta HTTP
     * @param estadoHttp código de estado HTTP (200, 201, 400, etc.)
     * @param cuerpo objeto ApiRespuesta que se desea enviar
     * @throws IOException si ocurre un error de escritura
     */
    protected void responder(HttpServletResponse response, int estadoHttp, ApiRespuesta<?> cuerpo)
            throws IOException {
        response.setStatus(estadoHttp);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try (PrintWriter escritor = response.getWriter()) {
            escritor.write(JsonUtil.getGson().toJson(cuerpo));
        }
    }

    /**
     * Envía una respuesta exitosa (200 OK) con datos.
     *
     * @param response respuesta HTTP
     * @param mensaje mensaje descriptivo
     * @param datos datos que se retornan
     * @throws IOException si ocurre un error de escritura
     */
    protected void responderExito(HttpServletResponse response, String mensaje, Object datos)
            throws IOException {
        responder(response, HttpServletResponse.SC_OK, ApiRespuesta.exito(mensaje, datos));
    }

    /**
     * Envía una respuesta de error con el código de estado indicado.
     *
     * @param response respuesta HTTP
     * @param estadoHttp código de estado HTTP (400, 401, 403, 404, 500, etc.)
     * @param mensaje mensaje descriptivo del error
     * @throws IOException si ocurre un error de escritura
     */
    protected void responderError(HttpServletResponse response, int estadoHttp, String mensaje)
            throws IOException {
        responder(response, estadoHttp, ApiRespuesta.error(mensaje));
    }

    /**
     * Obtiene el usuario autenticado a partir de la sesión HTTP.
     *
     * @param request solicitud HTTP
     * @return objeto Usuario si existe una sesión activa,
     *         null si el usuario no ha iniciado sesión
     */
    protected Usuario obtenerUsuarioSesion(HttpServletRequest request) {
        HttpSession sesion = request.getSession(false);
        if (sesion == null) {
            return null;
        }
        return (Usuario) sesion.getAttribute("usuario");
    }

    /**
     * Verifica si el usuario autenticado tiene el rol
     * de administrador ("admin") dentro del sistema.
     *
     * @param usuario usuario autenticado
     * @return true si el usuario es administrador
     */
    protected boolean esAdministrador(Usuario usuario) {
        return usuario != null && "admin".equalsIgnoreCase(usuario.getRol());
    }

    /**
     * Obtiene el identificador numérico que viene a continuación
     * del recurso en la URL (pathInfo), por ejemplo /api/productos/5
     * retorna 5.
     *
     * @param request solicitud HTTP
     * @return identificador numérico, o -1 si no se especificó
     *         o no es un número válido
     */
    protected int obtenerIdDeRuta(HttpServletRequest request) {
        String pathInfo = request.getPathInfo(); // ejemplo: "/5"
        if (pathInfo == null || pathInfo.equals("/")) {
            return -1;
        }
        String valor = pathInfo.replace("/", "");
        try {
            return Integer.parseInt(valor);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}