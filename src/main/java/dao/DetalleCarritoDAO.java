package dao;

import conexion.Conexion;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Clase DAO encargada de gestionar los productos
 * dentro de un carrito de compras (tabla detalle_carrito).
 *
 * Permite:
 * - Agregar un producto al carrito.
 * - Actualizar la cantidad de un producto.
 * - Eliminar un producto del carrito.
 *
 * Proyecto: J&M Makeup
 * Evidencia: GA7-220501096-AA3-EV01
 *
 * @author J&M Makeup
 */
public class DetalleCarritoDAO {

    /**
     * Agrega un producto al carrito.
     *
     * Si el producto ya existe en el carrito,
     * se suma la cantidad en lugar de duplicar la fila.
     *
     * @param idCarrito identificador del carrito
     * @param idProducto identificador del producto
     * @param cantidad cantidad a agregar
     * @param precio precio unitario del producto al momento de agregarlo
     * @return true si la operación fue exitosa
     */
    public boolean agregarProducto(int idCarrito, int idProducto, int cantidad, BigDecimal precio) {
        // Primero verificamos si el producto ya está en el carrito.
        String sqlBuscar = "SELECT cantidad FROM detalle_carrito WHERE id_carrito = ? AND id_producto = ?";
        try (
                Connection conexion = Conexion.conectar();
                PreparedStatement sentencia = conexion.prepareStatement(sqlBuscar)
        ) {
            sentencia.setInt(1, idCarrito);
            sentencia.setInt(2, idProducto);
            ResultSet resultado = sentencia.executeQuery();

            if (resultado.next()) {
                // Ya existe: sumamos la cantidad nueva a la existente.
                int cantidadActual = resultado.getInt("cantidad");
                return actualizarCantidad(idCarrito, idProducto, cantidadActual + cantidad);
            } else {
                // No existe: insertamos una fila nueva, incluyendo el precio.
                String sqlInsertar = "INSERT INTO detalle_carrito (id_carrito, id_producto, cantidad, precio) VALUES (?, ?, ?, ?)";
                try (PreparedStatement insertar = conexion.prepareStatement(sqlInsertar)) {
                    insertar.setInt(1, idCarrito);
                    insertar.setInt(2, idProducto);
                    insertar.setInt(3, cantidad);
                    insertar.setBigDecimal(4, precio);
                    insertar.executeUpdate();
                    return true;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al agregar producto al carrito: " + e.getMessage());
            return false;
        }
    }

    /**
     * Actualiza la cantidad de un producto
     * dentro del carrito.
     *
     * @param idCarrito identificador del carrito
     * @param idProducto identificador del producto
     * @param cantidad nueva cantidad
     * @return true si la actualización fue exitosa
     */
    public boolean actualizarCantidad(int idCarrito, int idProducto, int cantidad) {
        String sql = "UPDATE detalle_carrito SET cantidad = ? WHERE id_carrito = ? AND id_producto = ?";
        try (
                Connection conexion = Conexion.conectar();
                PreparedStatement sentencia = conexion.prepareStatement(sql)
        ) {
            sentencia.setInt(1, cantidad);
            sentencia.setInt(2, idCarrito);
            sentencia.setInt(3, idProducto);
            int filasAfectadas = sentencia.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar cantidad: " + e.getMessage());
            return false;
        }
    }

    /**
     * Elimina un producto del carrito.
     *
     * @param idCarrito identificador del carrito
     * @param idProducto identificador del producto
     * @return true si la eliminación fue exitosa
     */
    public boolean eliminarProducto(int idCarrito, int idProducto) {
        String sql = "DELETE FROM detalle_carrito WHERE id_carrito = ? AND id_producto = ?";
        try (
                Connection conexion = Conexion.conectar();
                PreparedStatement sentencia = conexion.prepareStatement(sql)
        ) {
            sentencia.setInt(1, idCarrito);
            sentencia.setInt(2, idProducto);
            int filasAfectadas = sentencia.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar producto del carrito: " + e.getMessage());
            return false;
        }
    }
}