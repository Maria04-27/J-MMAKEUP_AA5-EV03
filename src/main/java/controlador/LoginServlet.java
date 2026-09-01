package controlador;

import dao.UsuarioDAO;
import modelo.Usuario;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
/**
 * Servlet encargado de procesar el inicio de sesión
 * de los usuarios de la aplicación J&M Makeup.
 *
 * Este controlador recibe el correo y la contraseña
 * enviados desde el formulario de login.jsp.
 *
 * Posteriormente consulta UsuarioDAO para validar
 * las credenciales en la base de datos.
 *
 * Proyecto: J&M Makeup
 * Evidencia: GA7-220501096-AA3-EV01
 *
 * @author J&M Makeup
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    // Objeto DAO encargado de consultar los usuarios
    private UsuarioDAO usuarioDAO;
    /**
     * Método que se ejecuta cuando se crea el Servlet.
     *
     * Inicializa el objeto UsuarioDAO.
     */
    @Override
    public void init() {
        usuarioDAO = new UsuarioDAO();
    }
    /**
     * Procesa las solicitudes HTTP POST.
     *
     * Este método se ejecuta cuando el usuario
     * envía el formulario de inicio de sesión.
     *
     * @param request solicitud enviada por el navegador
     * @param response respuesta enviada al navegador
     * @throws ServletException si ocurre un error del Servlet
     * @throws IOException si ocurre un error de entrada o salida
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Establecemos la codificación UTF-8
        request.setCharacterEncoding("UTF-8");
        // Obtenemos los datos enviados desde login.jsp
        String correo = request.getParameter("correo");
        String password = request.getParameter("password");
        // Validamos que los campos no estén vacíos
        if (correo == null || correo.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            request.setAttribute("mensaje", "Debe ingresar el correo y la contraseña.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }
        // Eliminamos espacios innecesarios
        correo = correo.trim();
        // Consultamos el usuario en la base de datos
        Usuario usuario = usuarioDAO.iniciarSesion(correo, password);
        // Verificamos si las credenciales son correctas
        if (usuario != null) {
            // Creamos una sesión para el usuario
            HttpSession session = request.getSession();
            // Guardamos el objeto usuario en la sesión
            session.setAttribute("usuario", usuario);
            // Guardamos también el nombre
            // para mostrarlo en la interfaz
            session.setAttribute("nombreUsuario", usuario.getNombre());
            // Guardamos el rol del usuario
            session.setAttribute("rolUsuario", usuario.getRol());
            // Redirigimos al inicio
            response.sendRedirect(request.getContextPath() + "/index.jsp");
        } else {
            // Si las credenciales son incorrectas,
            // mostramos un mensaje de error
            request.setAttribute("mensaje", "Correo o contraseña incorrectos.");
            // Regresamos al formulario de login
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }

    /**
     * Procesa solicitudes HTTP GET.
     *
     * Permite acceder directamente al Servlet
     * y mostrar la página de inicio de sesión.
     *
     * @param request solicitud HTTP
     * @param response respuesta HTTP
     * @throws ServletException si ocurre un error
     * @throws IOException si ocurre un error de entrada o salida
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Redirigimos al formulario de inicio de sesión
        response.sendRedirect(request.getContextPath() + "/login.jsp"
        );
    }
}