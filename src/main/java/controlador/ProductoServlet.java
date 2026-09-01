package controlador;

import dao.ProductoDAO;
import modelo.Producto;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * Servlet encargado de controlar las operaciones
 * relacionadas con los productos de la tienda J&M Makeup.
 *
 * Este controlador permite:
 * - Mostrar todos los productos.
 * - Consultar un producto por su identificador.
 * - Registrar un producto.
 * - Actualizar un producto.
 * - Eliminar un producto.
 *
 * Proyecto: J&M Makeup
 * Evidencia: GA7-220501096-AA3-EV01
 *
 * @author J&M Makeup
 */
@WebServlet("/ProductoServlet")
public class ProductoServlet extends HttpServlet {
    // Objeto DAO encargado de realizar las operaciones
    // relacionadas con los productos.
    private ProductoDAO productoDAO;
    /**
     * Método ejecutado cuando se inicia el Servlet.
     *
     * Inicializa el objeto ProductoDAO.
     */
    @Override
    public void init() {
        productoDAO = new ProductoDAO();
    }

    /**
     * Procesa las solicitudes HTTP GET.
     *
     * Permite consultar y mostrar los productos
     * almacenados en la base de datos.
     *
     * @param request solicitud enviada por el navegador
     * @param response respuesta enviada al navegador
     * @throws ServletException si ocurre un error del Servlet
     * @throws IOException si ocurre un error de entrada o salida
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Obtenemos la acción enviada en la URL.
        String accion = request.getParameter("accion");

        // Si no se especifica ninguna acción,
        // mostramos todos los productos.
        if (accion == null || accion.isEmpty()) {
            listarProductos(request, response);
            return;
        }

        // Ejecutamos la acción correspondiente.
        switch (accion) {
            case "listar": listarProductos(request, response);
                break;
            case "ofertas": listarOfertas(request, response);
                break;
            case "ver": verProducto(request, response);
                break;
            case "eliminar": eliminarProducto(request, response);
                break;
            default:
                listarProductos(request, response);
                break;
        }
    }

    /**
     * Procesa las solicitudes HTTP POST.
     *
     * Se utiliza principalmente para registrar
     * o actualizar productos.
     *
     * @param request solicitud enviada por el navegador
     * @param response respuesta enviada al navegador
     * @throws ServletException si ocurre un error del Servlet
     * @throws IOException si ocurre un error de entrada o salida
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Configuramos UTF-8 para admitir caracteres especiales.
        request.setCharacterEncoding("UTF-8");
        // Obtenemos la acción del formulario.
        String accion = request.getParameter("accion");

        if ("registrar".equals(accion)) {
            registrarProducto(request, response);
        } else if ("actualizar".equals(accion)) {
            actualizarProducto(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/ProductoServlet?accion=listar");
        }
    }

    /**
     * Consulta todos los productos registrados
     * y los envía a productos.jsp.
     *
     * @param request solicitud HTTP
     * @param response respuesta HTTP
     * @throws ServletException si ocurre un error
     * @throws IOException si ocurre un error de entrada o salida
     */
    private void listarProductos(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Consultamos los productos mediante el DAO.
        List<Producto> productos = productoDAO.listarTodos();

        // Guardamos la lista como atributo de la solicitud.
        request.setAttribute("productos", productos);

        // Enviamos los datos a la página JSP.
        request.getRequestDispatcher("productos.jsp").forward(request, response);
    }
    /**
     * Consulta los productos que tienen
     * una oferta activa y los envía a ofertas.jsp.
     *
     * @param request solicitud HTTP
     * @param response respuesta HTTP
     * @throws ServletException si ocurre un error
     * @throws IOException si ocurre un error de entrada o salida
     */
    private void listarOfertas(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Consultamos las ofertas mediante el DAO.
        List<Producto> ofertas = productoDAO.listarOfertas();

        // Guardamos la lista como atributo de la solicitud.
        request.setAttribute("productos", ofertas);

        // Enviamos los datos a la página JSP.
        request.getRequestDispatcher("ofertas.jsp").forward(request, response);
    }

    /**
     * Consulta un producto específico.
     *
     * @param request solicitud HTTP
     * @param response respuesta HTTP
     * @throws ServletException si ocurre un error
     * @throws IOException si ocurre un error de entrada o salida
     */
    private void verProducto(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Obtenemos el ID enviado en la URL.
        String idParametro = request.getParameter("id");

        // Validamos que se haya enviado el ID.
        if (idParametro == null || idParametro.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/ProductoServlet?accion=listar");
            return;
        }

        try {
            // Convertimos el ID de String a entero.
            int idProducto = Integer.parseInt(idParametro);

            // Buscamos el producto mediante el DAO.
            Producto producto = productoDAO.buscarPorId(idProducto);

            // Guardamos el producto en la solicitud.
            request.setAttribute("producto", producto);

            // Enviamos la información a la página.
            request.getRequestDispatcher("detalle-producto.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            // Si el ID no es válido,
            // regresamos al listado.
            response.sendRedirect(request.getContextPath() + "/ProductoServlet?accion=listar");
        }
    }

    /**
     * Registra un nuevo producto.
     *
     * @param request solicitud HTTP
     * @param response respuesta HTTP
     * @throws IOException si ocurre un error
     */
    private void registrarProducto(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {

            // Obtenemos los datos del formulario.
            String nombre = request.getParameter("nombre");

            String descripcion = request.getParameter("descripcion");

            String precio = request.getParameter("precio");

            String stock = request.getParameter("stock");

            String idCategoria = request.getParameter("id_categoria");

            // Creamos un objeto Producto.
            Producto producto = new Producto();

            // Asignamos la información.
            producto.setNombre(nombre);
            producto.setDescripcion(descripcion);
            producto.setPrecio(new java.math.BigDecimal(precio));
            producto.setStock(Integer.parseInt(stock));
            producto.setIdCategoria(Integer.parseInt(idCategoria));

            // Registramos el producto.
            boolean registrado = productoDAO.registrar(producto);

            // Redirigimos al listado.
            response.sendRedirect(request.getContextPath() + "/ProductoServlet?accion=listar");
        } catch (Exception e) {
            System.err.println("Error al registrar producto: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/ProductoServlet?accion=listar");
        }
    }

    /**
     * Actualiza un producto existente.
     *
     * @param request solicitud HTTP
     * @param response respuesta HTTP
     * @throws IOException si ocurre un error
     */
    private void actualizarProducto(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {

            // Obtenemos los datos enviados.
            int idProducto = Integer.parseInt(request.getParameter("id_producto"));
            String nombre = request.getParameter("nombre");
            String descripcion = request.getParameter("descripcion");
            String precio = request.getParameter("precio");
            String stock = request.getParameter("stock");
            String idCategoria = request.getParameter("id_categoria");

            // Creamos el objeto Producto.
            Producto producto = new Producto();
            producto.setIdProducto(idProducto);
            producto.setNombre(nombre);
            producto.setDescripcion(descripcion);
            producto.setPrecio(new java.math.BigDecimal(precio));
            producto.setStock(Integer.parseInt(stock));
            producto.setIdCategoria(Integer.parseInt(idCategoria));

            // Actualizamos el producto.
            productoDAO.actualizar(producto);

            // Regresamos al listado.
            response.sendRedirect(request.getContextPath() + "/ProductoServlet?accion=listar");
        } catch (Exception e) {
            System.err.println("Error al actualizar producto: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/ProductoServlet?accion=listar");
        }
    }

    /**
     * Elimina un producto de la base de datos.
     *
     * @param request solicitud HTTP
     * @param response respuesta HTTP
     * @throws IOException si ocurre un error
     */
    private void eliminarProducto(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            // Obtenemos el ID del producto.
            int idProducto = Integer.parseInt(request.getParameter("id"));

            // Eliminamos el producto mediante el DAO.
            productoDAO.eliminar(idProducto);
        } catch (Exception e) {
            System.err.println("Error al eliminar producto: " + e.getMessage());
        }
        // Regresamos al listado de productos.
        response.sendRedirect(request.getContextPath() + "/ProductoServlet?accion=listar");
    }
}