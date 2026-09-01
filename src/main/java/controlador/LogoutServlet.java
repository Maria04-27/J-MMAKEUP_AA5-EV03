package controlador;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Servlet encargado de cerrar la sesión
 * del usuario en la aplicación J&M Makeup.
 *
 * Proyecto: J&M Makeup
 * Evidencia: GA7-220501096-AA3-EV01
 *
 * @author J&M Makeup
 */
@WebServlet("/LogoutServlet")
public class LogoutServlet extends HttpServlet {

    /**
     * Procesa las solicitudes HTTP GET.
     *
     * Invalida la sesión actual y redirige
     * al usuario a la página de inicio.
     *
     * @param request solicitud HTTP
     * @param response respuesta HTTP
     * @throws ServletException si ocurre un error
     * @throws IOException si ocurre un error de entrada o salida
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Obtenemos la sesión actual, si existe.
        HttpSession session = request.getSession(false);
        // Si existe una sesión, la invalidamos
        // (esto elimina todos los datos guardados en ella).
        if (session != null) {
            session.invalidate();
        }
        // Redirigimos al usuario a la página de inicio.
        response.sendRedirect(request.getContextPath() + "/index.jsp");
    }
}