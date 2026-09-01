package dao;

import conexion.Conexion;
import modelo.Producto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase DAO encargada de realizar las operaciones
 * relacionadas con los productos de la tienda virtual
 * J&M Makeup en la base de datos.
 *
 * DAO significa Data Access Object.
 *
 * Esta clase permite:
 * - Registrar productos.
 * - Consultar todos los productos.
 * - Consultar los productos en oferta.
 * - Buscar un producto por su identificador.
 * - Actualizar productos.
 * - Eliminar productos.
 *
 * Proyecto: J&M Makeup
 * Evidencia: GA7-220501096-AA3-EV01
 *
 * @author J&M Makeup
 */
public class ProductoDAO {

    /**
     * Registra un nuevo producto en la base de datos.
     *
     * @param producto objeto Producto que se desea registrar
     * @return true si el registro fue exitoso,
     *         false si ocurrió un error
     */
    public boolean registrar(Producto producto) {
        String sql = "INSERT INTO producto (nombre, descripcion, precio, stock, id_categoria, imagen, precio_oferta) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (
                Connection conexion = Conexion.conectar();
                PreparedStatement sentencia = conexion.prepareStatement(sql)
        ) {
            sentencia.setString(1, producto.getNombre());
            sentencia.setString(2, producto.getDescripcion());
            sentencia.setBigDecimal(3, producto.getPrecio());
            sentencia.setInt(4, producto.getStock());
            sentencia.setInt(5, producto.getIdCategoria());
            sentencia.setString(6, producto.getImagen());
            // Precio de oferta (puede ser null)
            sentencia.setBigDecimal(7, producto.getPrecioOferta());
            int filasAfectadas = sentencia.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.err.println("Error al registrar producto: " + e.getMessage());
            return false;
        }
    }

    /**
     * Obtiene todos los productos registrados
     * en la base de datos.
     *
     * @return lista de productos
     */
    public List<Producto> listarTodos() {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT id_producto, nombre, descripcion, precio, stock, id_categoria, imagen, precio_oferta FROM producto ORDER BY id_producto DESC ";
        try (
                Connection conexion = Conexion.conectar();
                PreparedStatement sentencia = conexion.prepareStatement(sql);
                ResultSet resultado = sentencia.executeQuery()
        ) {
            while (resultado.next()) {
                productos.add(construirProducto(resultado));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar productos: " + e.getMessage());
        }
        return productos;
    }

    /**
     * Obtiene los productos que tienen
     * una oferta activa (precio_oferta menor al precio normal).
     *
     * @return lista de productos en oferta
     */
    public List<Producto> listarOfertas() {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT id_producto, nombre, descripcion, precio, stock, id_categoria, imagen, precio_oferta " +
                "FROM producto WHERE precio_oferta IS NOT NULL AND precio_oferta < precio ORDER BY id_producto DESC";
        try (
                Connection conexion = Conexion.conectar();
                PreparedStatement sentencia = conexion.prepareStatement(sql);
                ResultSet resultado = sentencia.executeQuery()
        ) {
            while (resultado.next()) {
                productos.add(construirProducto(resultado));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar ofertas: " + e.getMessage());
        }
        return productos;
    }

    /**
     * Busca un producto por su identificador.
     *
     * @param idProducto identificador del producto
     * @return objeto Producto si existe,
     *         null si no se encuentra
     */
    public Producto buscarPorId(int idProducto) {
        String sql = "SELECT id_producto, nombre, descripcion, precio, stock, id_categoria, imagen, precio_oferta FROM producto WHERE id_producto = ?";
        try (
                Connection conexion = Conexion.conectar();
                PreparedStatement sentencia = conexion.prepareStatement(sql)
        ) {
            sentencia.setInt(1, idProducto);
            ResultSet resultado = sentencia.executeQuery();
            if (resultado.next()) {
                return construirProducto(resultado);
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar producto: " + e.getMessage());
        }
        return null;
    }

    /**
     * Actualiza la información de un producto.
     *
     * @param producto producto con la información actualizada
     * @return true si la actualización fue exitosa,
     *         false si ocurrió un error
     */
    public boolean actualizar(Producto producto) {
        String sql = "UPDATE producto SET nombre = ?, descripcion = ?, precio = ?, stock = ?, id_categoria = ?, imagen = ?, precio_oferta = ? WHERE id_producto = ?";
        try (
                Connection conexion = Conexion.conectar();
                PreparedStatement sentencia = conexion.prepareStatement(sql)
        ) {
            sentencia.setString(1, producto.getNombre());
            sentencia.setString(2, producto.getDescripcion());
            sentencia.setBigDecimal(3, producto.getPrecio());
            sentencia.setInt(4, producto.getStock());
            sentencia.setInt(5, producto.getIdCategoria());
            sentencia.setString(6, producto.getImagen());
            sentencia.setBigDecimal(7, producto.getPrecioOferta());
            sentencia.setInt(8, producto.getIdProducto());
            int filasAfectadas = sentencia.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar producto: " + e.getMessage());
            return false;
        }
    }

    /**
     * Elimina un producto de la base de datos.
     *
     * @param idProducto identificador del producto
     * @return true si la eliminación fue exitosa,
     *         false si ocurrió un error
     */
    public boolean eliminar(int idProducto) {
        String sql = " DELETE FROM producto WHERE id_producto = ? ";
        try (
                Connection conexion = Conexion.conectar();
                PreparedStatement sentencia = conexion.prepareStatement(sql)
        ) {
            sentencia.setInt(1, idProducto);
            int filasAfectadas = sentencia.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar producto: " + e.getMessage());
            return false;
        }
    }

    /**
     * Convierte un resultado de MySQL
     * en un objeto Producto.
     *
     * Este método permite reutilizar el código
     * utilizado para construir objetos Producto.
     *
     * @param resultado resultado obtenido de la base de datos
     * @return objeto Producto
     * @throws SQLException si ocurre un error
     */
    private Producto construirProducto(ResultSet resultado) throws SQLException {
        Producto producto = new Producto();
        producto.setIdProducto(resultado.getInt("id_producto"));
        producto.setNombre(resultado.getString("nombre"));
        producto.setDescripcion(resultado.getString("descripcion"));
        producto.setPrecio(resultado.getBigDecimal("precio"));
        producto.setStock(resultado.getInt("stock"));
        producto.setIdCategoria(resultado.getInt("id_categoria"));
        producto.setImagen(resultado.getString("imagen"));
        // Precio de oferta (puede venir null desde la base de datos)
        producto.setPrecioOferta(resultado.getBigDecimal("precio_oferta"));
        return producto;
    }
}