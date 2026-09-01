package dao;

import conexion.Conexion;
import modelo.Carrito;
import modelo.DetalleCarrito;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase DAO encargada de gestionar las operaciones
 * relacionadas con el carrito de compras.
 *
 * Permite:
 * - Crear un carrito para un usuario.
 * - Buscar el carrito activo de un usuario.
 * - Consultar un carrito por su identificador.
 * - Actualizar el total del carrito.
 * - Vaciar el carrito.
 *
 * Proyecto: J&M Makeup
 * Evidencia: GA7-220501096-AA3-EV01
 *
 * @author J&M Makeup
 */
public class CarritoDAO {

    /**
     * Crea un nuevo carrito para un usuario.
     *
     * @param idUsuario identificador del usuario
     * @return identificador del carrito creado
     *         o -1 si ocurrió un error
     */
    public int crearCarrito(int idUsuario) {
        String sql = "INSERT INTO carrito (id_usuario, estado, total) VALUES (?, 'activo', 0.00)";
        try (
                Connection conexion = Conexion.conectar();
                PreparedStatement sentencia = conexion.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)
        ) {
            // Asignamos el usuario propietario del carrito
            sentencia.setInt(1, idUsuario);
            // Ejecutamos la inserción
            sentencia.executeUpdate();
            // Obtenemos el ID generado automáticamente
            ResultSet resultado = sentencia.getGeneratedKeys();
            if (resultado.next()) {
                return resultado.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error al crear carrito: " + e.getMessage());
        }
        return -1;
    }
    /**
     * Busca el carrito activo de un usuario.
     *
     * @param idUsuario identificador del usuario
     * @return objeto Carrito si existe,
     *         null si no se encuentra
     */
    public Carrito buscarCarritoActivo(int idUsuario) {
        String sql = "SELECT id_carrito, id_usuario, estado, total FROM carrito WHERE id_usuario = ? AND estado = 'activo' LIMIT 1 ";
        try (
                Connection conexion = Conexion.conectar();
                PreparedStatement sentencia = conexion.prepareStatement(sql)
        ) {
            sentencia.setInt(1, idUsuario);
            ResultSet resultado = sentencia.executeQuery();
            if (resultado.next()) {
                return construirCarrito(resultado);
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar carrito activo: " + e.getMessage());
        }
        return null;
    }
    /**
     * Busca un carrito utilizando su identificador.
     *
     * @param idCarrito identificador del carrito
     * @return objeto Carrito si existe,
     *         null si no se encuentra
     */
    public Carrito buscarPorId(int idCarrito) {
        String sql = "SELECT id_carrito, id_usuario, estado, total FROM carrito WHERE id_carrito = ? ";
        try (
                Connection conexion = Conexion.conectar();
                PreparedStatement sentencia = conexion.prepareStatement(sql)
        ) {
            sentencia.setInt(1, idCarrito);
            ResultSet resultado = sentencia.executeQuery();
            if (resultado.next()) {
                return construirCarrito(resultado);
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar carrito: " + e.getMessage());
        }
        return null;
    }
    /**
     * Actualiza el total de un carrito.
     *
     * @param idCarrito identificador del carrito
     * @param total nuevo valor total
     * @return true si la actualización fue exitosa
     */
    public boolean actualizarTotal(int idCarrito, BigDecimal total) {
        String sql = "UPDATE carrito SET total = ? WHERE id_carrito = ?";
        try (
                Connection conexion = Conexion.conectar();
                PreparedStatement sentencia = conexion.prepareStatement(sql)
        ) {
            sentencia.setBigDecimal(1, total);
            sentencia.setInt(2, idCarrito);
            int filasAfectadas = sentencia.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar total: " + e.getMessage());
            return false;
        }
    }
    /**
     * Cambia el estado del carrito.
     *
     * @param idCarrito identificador del carrito
     * @param estado nuevo estado del carrito
     * @return true si la operación fue exitosa
     */
    public boolean actualizarEstado(int idCarrito, String estado) {
        String sql = "UPDATE carrito SET estado = ? WHERE id_carrito = ?";
        try (
                Connection conexion = Conexion.conectar();
                PreparedStatement sentencia = conexion.prepareStatement(sql)
        ) {
            sentencia.setString(1, estado);
            sentencia.setInt(2, idCarrito);
            int filasAfectadas = sentencia.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar estado del carrito: " + e.getMessage());
            return false;
        }
    }
    /**
     * Vacía todos los productos asociados
     * a un carrito.
     *
     * @param idCarrito identificador del carrito
     * @return true si la operación fue exitosa
     */
    public boolean vaciarCarrito(int idCarrito) {
        String sql = "DELETE FROM detalle_carrito WHERE id_carrito = ?";
        try (
                Connection conexion = Conexion.conectar();
                PreparedStatement sentencia = conexion.prepareStatement(sql)
        ) {
            sentencia.setInt(1, idCarrito);
            sentencia.executeUpdate();
            // Después de eliminar los productos,
            // establecemos nuevamente el total en cero.
            actualizarTotal(idCarrito, BigDecimal.ZERO);
            return true;
        } catch (SQLException e) {
            System.err.println("Error al vaciar carrito: " + e.getMessage());
            return false;
        }
    }
    /**
     * Convierte un resultado de la base de datos
     * en un objeto Carrito.
     *
     * Este método evita repetir código
     * al construir objetos Carrito.
     *
     * @param resultado resultado obtenido de MySQL
     * @return objeto Carrito
     * @throws SQLException si ocurre un error
     */
    private Carrito construirCarrito(ResultSet resultado) throws SQLException {
        Carrito carrito = new Carrito();
        carrito.setIdCarrito(resultado.getInt("id_carrito"));
        carrito.setIdUsuario(resultado.getInt("id_usuario"));
        carrito.setEstado(resultado.getString("estado"));
        carrito.setTotal(resultado.getBigDecimal("total"));
        return carrito;
    }
    /**
     * Lista el detalle de productos de un carrito específico.
     *
     * Este método hace un JOIN entre la tabla detalle_carrito
     * y la tabla producto, para obtener el nombre y precio
     * de cada producto agregado al carrito.
     *
     * @param idCarrito identificador del carrito
     * @return lista de objetos DetalleCarrito con la información
     *         de cada producto agregado
     */
    public List<DetalleCarrito> listarDetalle(int idCarrito) {
        // Lista donde se almacenarán los productos del carrito
        List<DetalleCarrito> lista = new ArrayList<>();
        // Consulta que combina detalle_carrito con producto
        String sql = "SELECT p.id_producto, p.nombre, p.precio, dc.cantidad " + "FROM detalle_carrito dc " + "JOIN producto p ON dc.id_producto = p.id_producto " + "WHERE dc.id_carrito = ?";
        try (
                Connection conexion = Conexion.conectar();
                PreparedStatement sentencia = conexion.prepareStatement(sql)
        ) {
            // Asignamos el id del carrito a consultar
            sentencia.setInt(1, idCarrito);
            ResultSet resultado = sentencia.executeQuery();
            // Recorremos cada fila del resultado
            while (resultado.next()) {
                // Construimos un objeto DetalleCarrito por cada producto
                DetalleCarrito item = new DetalleCarrito(
                        resultado.getInt("id_producto"),
                        resultado.getString("nombre"),
                        resultado.getDouble("precio"),
                        resultado.getInt("cantidad"));
                lista.add(item);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar detalle del carrito: " + e.getMessage());
        }
        return lista;
    }
}