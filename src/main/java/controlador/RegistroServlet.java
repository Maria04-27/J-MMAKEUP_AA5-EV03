package controlador;

import dao.UsuarioDAO;
import modelo.Usuario;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Servlet encargado de procesar el registro
 * de nuevos usuarios en la aplicación J&M Makeup.
 *
 * Este controlador recibe los datos enviados
 * desde registro.jsp y utiliza UsuarioDAO
 * para guardar la información en MySQL.
 *
 * Proyecto: J&M Makeup
 * Evidencia: GA7-220501096-AA3-EV01
 *
 * @author J&M Makeup
 */
@WebServlet("/RegistroServlet")
public class RegistroServlet extends HttpServlet {

    // Objeto encargado de realizar las operaciones
    // de usuarios en la base de datos.
    private UsuarioDAO usuarioDAO;

    /**
     * Método ejecutado cuando se inicia el Servlet.
     *
     * Inicializa el objeto UsuarioDAO.
     */
    @Override
    public void init() {
        usuarioDAO = new UsuarioDAO();
    }

    /**
     * Procesa el formulario de registro enviado
     * mediante el método HTTP POST.
     *
     * @param request solicitud enviada por el navegador
     * @param response respuesta enviada al navegador
     * @throws ServletException si ocurre un error del Servlet
     * @throws IOException si ocurre un error de entrada o salida
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Configuramos la codificación UTF-8
        // para permitir caracteres especiales.
        request.setCharacterEncoding("UTF-8");

        // Obtenemos los datos enviados desde registro.jsp
        String nombre = request.getParameter("nombre");

        String correo = request.getParameter("correo");

        String password = request.getParameter("password");

        String confirmarPassword = request.getParameter("confirmarPassword");

        // Validamos que los campos obligatorios
        // no estén vacíos.
        if (nombre == null || nombre.trim().isEmpty() || correo == null || correo.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            request.setAttribute("mensaje", "Todos los campos son obligatorios.");
            request.getRequestDispatcher("registro.jsp").forward(request, response);
            return;
        }

        // Eliminamos espacios innecesarios
        // al inicio y al final de los datos.
        nombre = nombre.trim();
        correo = correo.trim();

        // Validamos que las contraseñas coincidan.
        if (confirmarPassword != null && !password.equals(confirmarPassword)) {

            request.setAttribute("mensaje", "Las contraseñas no coinciden.");
            request.getRequestDispatcher("registro.jsp").forward(request, response);
            return;
        }

        // Verificamos si el correo electrónico
        // ya está registrado.
        Usuario usuarioExistente = usuarioDAO.buscarPorCorreo(correo);
        if (usuarioExistente != null) {
            request.setAttribute("mensaje", "El correo electrónico ya está registrado.");
            request.getRequestDispatcher("registro.jsp").forward(request, response);
            return;
        }

        // Creamos un nuevo objeto Usuario.
        Usuario usuario = new Usuario();

        // Asignamos los datos del formulario.
        usuario.setNombre(nombre);
        usuario.setCorreo(correo);
        usuario.setPassword(password);

        // Todos los usuarios que se registren
        // tendrán inicialmente el rol de cliente.
        usuario.setRol("cliente");

        // Registramos el usuario en MySQL.
        boolean registrado = usuarioDAO.registrar(usuario);

        // Verificamos el resultado del registro.
        if (registrado) {

            // Si el registro fue exitoso,
            // enviamos un mensaje al login.
            response.sendRedirect(request.getContextPath() + "/login.jsp?registro=exitoso");
        } else {
            // Si ocurrió un error,
            // regresamos al formulario de registro.
            request.setAttribute("mensaje", "No fue posible registrar el usuario.");
            request.getRequestDispatcher("registro.jsp").forward(request, response);
        }
    }
    /**
     * Procesa solicitudes HTTP GET.
     *
     * Permite acceder directamente
     * al formulario de registro.
     *
     * @param request solicitud HTTP
     * @param response respuesta HTTP
     * @throws ServletException si ocurre un error
     * @throws IOException si ocurre un error de entrada o salida
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Redirigimos al formulario de registro.
        response.sendRedirect(request.getContextPath() + "/registro.jsp");
    }
}