package controlador;

import dao.MensajeDAO;
import modelo.Mensaje;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Servlet encargado de procesar el envío
 * del formulario de contacto en la aplicación J&M Makeup.
 *
 * Este controlador recibe los datos enviados
 * desde contacto.jsp y utiliza MensajeDAO
 * para guardar la información en MySQL.
 *
 * Proyecto: J&M Makeup
 * Evidencia: GA7-220501096-AA4-EV03
 *
 * @author J&M Makeup
 */
@WebServlet("/ContactoServlet")
public class ContactoServlet extends HttpServlet {

    // Objeto encargado de realizar las operaciones
    // de mensajes de contacto en la base de datos.
    private MensajeDAO mensajeDAO;

    /**
     * Método ejecutado cuando se inicia el Servlet.
     *
     * Inicializa el objeto MensajeDAO.
     */
    @Override
    public void init() {
        mensajeDAO = new MensajeDAO();
    }

    /**
     * Procesa el formulario de contacto enviado
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

        // Obtenemos los datos enviados desde contacto.jsp
        String nombre = request.getParameter("nombre");

        String correo = request.getParameter("correo");

        String asunto = request.getParameter("asunto");

        String contenido = request.getParameter("mensaje");

        // Validamos que los campos obligatorios
        // no estén vacíos.
        if (nombre == null || nombre.trim().isEmpty() || correo == null || correo.trim().isEmpty()
                || asunto == null || asunto.trim().isEmpty()
                || contenido == null || contenido.trim().isEmpty()) {
            request.setAttribute("mensajeError", "Todos los campos son obligatorios.");
            request.getRequestDispatcher("contacto.jsp").forward(request, response);
            return;
        }

        // Eliminamos espacios innecesarios
        // al inicio y al final de los datos.
        nombre = nombre.trim();
        correo = correo.trim();
        asunto = asunto.trim();
        contenido = contenido.trim();

        // Validamos que el correo tenga un formato básico válido.
        if (!correo.contains("@") || !correo.contains(".")) {
            request.setAttribute("mensajeError", "Ingrese un correo electrónico válido.");
            request.getRequestDispatcher("contacto.jsp").forward(request, response);
            return;
        }

        // Creamos un nuevo objeto Mensaje con los datos recibidos.
        Mensaje mensaje = new Mensaje(nombre, correo, asunto, contenido);

        // Guardamos el mensaje en MySQL.
        boolean guardado = mensajeDAO.guardar(mensaje);

        // Verificamos el resultado del guardado.
        if (guardado) {
            // Si se guardó correctamente,
            // mostramos un mensaje de confirmación.
            request.setAttribute("mensajeExito", "Gracias por contactarnos. Tu mensaje ha sido recibido.");
        } else {
            // Si ocurrió un error,
            // informamos al usuario.
            request.setAttribute("mensajeError", "No fue posible enviar tu mensaje. Intenta nuevamente.");
        }
        request.getRequestDispatcher("contacto.jsp").forward(request, response);
    }

    /**
     * Procesa solicitudes HTTP GET.
     *
     * Permite acceder directamente
     * al formulario de contacto.
     *
     * @param request solicitud HTTP
     * @param response respuesta HTTP
     * @throws ServletException si ocurre un error
     * @throws IOException si ocurre un error de entrada o salida
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Redirigimos al formulario de contacto.
        response.sendRedirect(request.getContextPath() + "/contacto.jsp");
    }
}