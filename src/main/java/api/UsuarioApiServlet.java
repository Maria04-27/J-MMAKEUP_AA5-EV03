package api;

import dao.UsuarioDAO;
import modelo.Usuario;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Servicio web (API REST) encargado de exponer
 * las operaciones relacionadas con los usuarios
 * de la tienda virtual J&M Makeup: registro,
 * inicio de sesión, cierre de sesión y perfil.
 *
 * Recurso: /api/usuarios
 *
 * Rutas expuestas:
 * - POST /api/usuarios/registro -> registra un nuevo usuario
 * - POST /api/usuarios/login    -> inicia sesión y crea la sesión HTTP
 * - POST /api/usuarios/logout   -> cierra la sesión activa
 * - GET  /api/usuarios/perfil   -> consulta el usuario autenticado
 *
 * Proyecto: J&M Makeup
 * Evidencia: GA7-220501096-AA5-EV03
 *
 * @author J&M Makeup
 */
@WebServlet("/api/usuarios/*")
public class UsuarioApiServlet extends ApiServlet {

    // DAO encargado de las operaciones sobre usuarios.
    private UsuarioDAO usuarioDAO;

    @Override
    public void init() {
        usuarioDAO = new UsuarioDAO();
    }

    /**
     * Atiende GET /api/usuarios/perfil
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pathInfo = request.getPathInfo();

        if (pathInfo != null && pathInfo.equals("/perfil")) {
            Usuario usuario = obtenerUsuarioSesion(request);
            if (usuario == null) {
                responderError(response, HttpServletResponse.SC_UNAUTHORIZED,
                        "No hay una sesión activa. Debe iniciar sesión primero.");
                return;
            }
            // Ocultamos la contraseña antes de responder, por seguridad.
            usuario.setPassword(null);
            responderExito(response, "Perfil obtenido correctamente.", usuario);
            return;
        }

        responderError(response, HttpServletResponse.SC_NOT_FOUND,
                "Recurso no encontrado. Rutas disponibles: /api/usuarios/perfil");
    }

    /**
     * Atiende POST /api/usuarios/registro y POST /api/usuarios/login
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pathInfo = request.getPathInfo();

        if (pathInfo == null) {
            responderError(response, HttpServletResponse.SC_NOT_FOUND,
                    "Rutas disponibles: /api/usuarios/registro, /api/usuarios/login, /api/usuarios/logout");
            return;
        }

        switch (pathInfo) {
            case "/registro":
                registrar(request, response);
                break;
            case "/login":
                iniciarSesion(request, response);
                break;
            case "/logout":
                cerrarSesion(request, response);
                break;
            default:
                responderError(response, HttpServletResponse.SC_NOT_FOUND,
                        "Rutas disponibles: /api/usuarios/registro, /api/usuarios/login, /api/usuarios/logout");
        }
    }

    /**
     * Registra un nuevo usuario en el sistema.
     *
     * @param request solicitud HTTP (espera un JSON con nombre, correo, password y rol opcional)
     * @param response respuesta HTTP
     * @throws IOException si ocurre un error de entrada o salida
     */
    private void registrar(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Usuario usuario = leerCuerpo(request, Usuario.class);

        if (usuario == null || usuario.getNombre() == null || usuario.getNombre().trim().isEmpty()
                || usuario.getCorreo() == null || usuario.getCorreo().trim().isEmpty()
                || usuario.getPassword() == null || usuario.getPassword().trim().isEmpty()) {
            responderError(response, HttpServletResponse.SC_BAD_REQUEST,
                    "Debe enviar nombre, correo y contraseña.");
            return;
        }

        // Verificamos que el correo no esté registrado previamente.
        if (usuarioDAO.buscarPorCorreo(usuario.getCorreo().trim()) != null) {
            responderError(response, HttpServletResponse.SC_CONFLICT,
                    "Ya existe un usuario registrado con ese correo.");
            return;
        }

        // Si no se envía un rol, se asigna "cliente" por defecto.
        if (usuario.getRol() == null || usuario.getRol().trim().isEmpty()) {
            usuario.setRol("cliente");
        }

        boolean registrado = usuarioDAO.registrar(usuario);
        if (registrado) {
            usuario.setPassword(null);
            responder(response, HttpServletResponse.SC_CREATED,
                    util.ApiRespuesta.exito("Usuario registrado correctamente.", usuario));
        } else {
            responderError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "No fue posible registrar el usuario.");
        }
    }

    /**
     * Valida las credenciales del usuario y crea la sesión HTTP,
     * de la misma forma en que lo hace LoginServlet.
     *
     * @param request solicitud HTTP (espera un JSON con correo y password)
     * @param response respuesta HTTP
     * @throws IOException si ocurre un error de entrada o salida
     */
    private void iniciarSesion(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Usuario credenciales = leerCuerpo(request, Usuario.class);

        if (credenciales == null || credenciales.getCorreo() == null || credenciales.getCorreo().trim().isEmpty()
                || credenciales.getPassword() == null || credenciales.getPassword().trim().isEmpty()) {
            responderError(response, HttpServletResponse.SC_BAD_REQUEST,
                    "Debe enviar el correo y la contraseña.");
            return;
        }

        Usuario usuario = usuarioDAO.iniciarSesion(credenciales.getCorreo().trim(), credenciales.getPassword());
        if (usuario == null) {
            responderError(response, HttpServletResponse.SC_UNAUTHORIZED, "Correo o contraseña incorrectos.");
            return;
        }

        // Creamos la sesión, igual que LoginServlet.
        HttpSession sesion = request.getSession();
        sesion.setAttribute("usuario", usuario);
        sesion.setAttribute("nombreUsuario", usuario.getNombre());
        sesion.setAttribute("rolUsuario", usuario.getRol());

        usuario.setPassword(null);
        responderExito(response, "Inicio de sesión exitoso.", usuario);
    }

    /**
     * Cierra la sesión activa del usuario.
     *
     * @param request solicitud HTTP
     * @param response respuesta HTTP
     * @throws IOException si ocurre un error de entrada o salida
     */
    private void cerrarSesion(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession sesion = request.getSession(false);
        if (sesion != null) {
            sesion.invalidate();
        }
        responderExito(response, "Sesión cerrada correctamente.", null);
    }
}