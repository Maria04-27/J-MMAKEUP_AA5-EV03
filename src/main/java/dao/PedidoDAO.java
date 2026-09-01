package dao;

import conexion.Conexion;
import modelo.Pedido;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase DAO encargada de gestionar las operaciones
 * relacionadas con los pedidos realizados por los clientes.
 *
 * DAO significa Data Access Object.
 *
 * Esta clase permite:
 * - Registrar pedidos.
 * - Consultar un pedido por su identificador.
 * - Consultar los pedidos de un usuario.
 * - Actualizar el estado de un pedido.
 *
 * Proyecto: J&M Makeup
 * Evidencia: GA7-220501096-AA3-EV01
 *
 * @author J&M Makeup
 */
public class PedidoDAO {
    /**
     * Registra un nuevo pedido en la base de datos.
     *
     * @param pedido objeto Pedido que contiene
     *               la información de la compra
     * @return identificador del pedido creado
     *         o -1 si ocurrió un error
     */
    public int registrar(Pedido pedido) {
        String sql ="INSERT INTO pedido (id_usuario, id_carrito, fecha_pedido, estado, metodo_pago, total) VALUES (?, ?, ?, ?, ?, ?)";
        try (
                Connection conexion = Conexion.conectar();
                PreparedStatement sentencia = conexion.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)
        ) {
            // Asignamos el usuario que realiza el pedido
            sentencia.setInt(1, pedido.getIdUsuario());
            // Asignamos el carrito asociado
            sentencia.setInt(2, pedido.getIdCarrito());
            // Convertimos LocalDateTime a Timestamp
            if (pedido.getFechaPedido() != null) {
                sentencia.setTimestamp(3, Timestamp.valueOf(pedido.getFechaPedido()));
            } else {
                // Si no se proporciona fecha,
                // MySQL utilizará la fecha actual
                sentencia.setTimestamp(3, Timestamp.valueOf(java.time.LocalDateTime.now()));
            }
            // Estado inicial del pedido
            sentencia.setString(4, pedido.getEstado());
            // Método de pago seleccionado
            sentencia.setString(5, pedido.getMetodoPago());
            // Total de la compra
            sentencia.setBigDecimal(6, pedido.getTotal());
            // Ejecutamos la inserción
            sentencia.executeUpdate();
            // Obtenemos el ID generado automáticamente
            ResultSet resultado = sentencia.getGeneratedKeys();
            if (resultado.next()) {
                return resultado.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error al registrar pedido: " + e.getMessage());
        }
        return -1;
    }

    /**
     * Busca un pedido utilizando su identificador.
     *
     * @param idPedido identificador del pedido
     * @return objeto Pedido si existe,
     *         null si no se encuentra
     */
    public Pedido buscarPorId(int idPedido) {
        String sql = "SELECT id_pedido, id_usuario, id_carrito, fecha_pedido, estado, metodo_pago, total FROM pedido WHERE id_pedido = ?";
        try (
                Connection conexion = Conexion.conectar();
                PreparedStatement sentencia = conexion.prepareStatement(sql)
        ) {
            // Asignamos el ID del pedido
            sentencia.setInt(1, idPedido);
            // Ejecutamos la consulta
            ResultSet resultado = sentencia.executeQuery();
            if (resultado.next()) {
                return construirPedido(resultado);
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar pedido: " + e.getMessage());
        }
        return null;
    }
    /**
     * Obtiene todos los pedidos realizados
     * por un usuario.
     *
     * @param idUsuario identificador del usuario
     * @return lista de pedidos
     */
    public List<Pedido> listarPorUsuario(int idUsuario) {
        List<Pedido> pedidos = new ArrayList<>();
        String sql = "SELECT id_pedido, id_usuario, id_carrito, fecha_pedido, estado, metodo_pago, total FROM pedido WHERE id_usuario = ? ORDER BY fecha_pedido DESC ";
        try (
                Connection conexion = Conexion.conectar();
                PreparedStatement sentencia = conexion.prepareStatement(sql)
        ) {
            // Asignamos el usuario
            sentencia.setInt(1, idUsuario);
            // Ejecutamos la consulta
            ResultSet resultado = sentencia.executeQuery();
            // Recorremos los pedidos encontrados
            while (resultado.next()) {
                Pedido pedido = construirPedido(resultado);
                pedidos.add(pedido);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar pedidos del usuario: " + e.getMessage());
        }
        return pedidos;
    }
    /**
     * Actualiza el estado de un pedido.
     *
     * @param idPedido identificador del pedido
     * @param estado nuevo estado del pedido
     * @return true si la actualización fue exitosa
     */
    public boolean actualizarEstado(int idPedido, String estado) {
        String sql = "UPDATE pedido SET estado = ? WHERE id_pedido = ?";
        try (
                Connection conexion = Conexion.conectar();
                PreparedStatement sentencia = conexion.prepareStatement(sql)
        ) {
            // Asignamos el nuevo estado
            sentencia.setString(1, estado);
            // Asignamos el pedido
            sentencia.setInt(2, idPedido);
            // Ejecutamos la actualización
            int filasAfectadas = sentencia.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar estado del pedido: " + e.getMessage());
            return false;
        }
    }
    /**
     * Obtiene todos los pedidos registrados
     * en la base de datos.
     *
     * @return lista con todos los pedidos
     */
    public List<Pedido> listarTodos() {
        List<Pedido> pedidos = new ArrayList<>();
        String sql = "SELECT id_pedido, id_usuario, id_carrito, fecha_pedido, estado, metodo_pago, total FROM pedido ORDER BY fecha_pedido DESC";
        try (
                Connection conexion = Conexion.conectar();
                PreparedStatement sentencia = conexion.prepareStatement(sql);
                ResultSet resultado = sentencia.executeQuery()
        ) {
            // Recorremos los resultados
            while (resultado.next()) {
                Pedido pedido = construirPedido(resultado);
                pedidos.add(pedido);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar pedidos: " + e.getMessage());
        }
        return pedidos;
    }
    /**
     * Convierte un resultado de MySQL
     * en un objeto Pedido.
     *
     * Este método permite reutilizar el código
     * utilizado para construir objetos Pedido.
     *
     * @param resultado resultado obtenido de la base de datos
     * @return objeto Pedido
     * @throws SQLException si ocurre un error
     */
    private Pedido construirPedido(ResultSet resultado) throws SQLException {
        Pedido pedido = new Pedido();
        // ID del pedido
        pedido.setIdPedido(resultado.getInt("id_pedido"));
        // ID del usuario
        pedido.setIdUsuario(resultado.getInt("id_usuario"));
        // ID del carrito
        pedido.setIdCarrito(resultado.getInt("id_carrito"));
        // Fecha y hora del pedido
        Timestamp fecha = resultado.getTimestamp("fecha_pedido");
        if (fecha != null) {

            pedido.setFechaPedido(fecha.toLocalDateTime());
        }
        // Estado del pedido
        pedido.setEstado(resultado.getString("estado"));
        // Método de pago
        pedido.setMetodoPago(resultado.getString("metodo_pago"));
        // Total del pedido
        pedido.setTotal(resultado.getBigDecimal("total"));
        return pedido;
    }
}