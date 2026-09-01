package dao;

import conexion.Conexion;
import modelo.Mensaje;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase DAO encargada de realizar las operaciones
 * relacionadas con los mensajes de contacto en la base de datos.
 *
 * DAO significa Data Access Object.
 *
 * Esta clase permite:
 * - Guardar los mensajes enviados desde el formulario de contacto.
 *
 * Proyecto: J&M Makeup
 * Evidencia: GA7-220501096-AA4-EV03
 *
 * @author J&M Makeup
 */
public class MensajeDAO {
    /**
     * Guarda un nuevo mensaje de contacto en la base de datos.
     *
     * @param mensaje objeto Mensaje con la información
     *                que se desea guardar
     * @return true si el mensaje se guardó correctamente,
     *         false si ocurrió algún error
     */
    public boolean guardar(Mensaje mensaje) {
        String sql = "INSERT INTO mensaje_contacto (nombre, correo, asunto, contenido) VALUES (?, ?, ?, ?)";
        try (
                Connection conexion = Conexion.conectar();
                PreparedStatement sentencia = conexion.prepareStatement(sql)
        ) {
            // Asignamos los datos del mensaje
            sentencia.setString(1, mensaje.getNombre());
            sentencia.setString(2, mensaje.getCorreo());
            sentencia.setString(3, mensaje.getAsunto());
            sentencia.setString(4, mensaje.getContenido());
            // Ejecutamos el INSERT
            int filasAfectadas = sentencia.executeUpdate();
            // Si se insertó una fila, el mensaje se guardó correctamente
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.err.println("Error al guardar mensaje de contacto: " + e.getMessage());
            return false;
        }
    }

    /**
     * Obtiene todos los mensajes de contacto
     * registrados en la base de datos.
     *
     * Se utiliza en el panel de administración
     * para revisar los mensajes enviados por los clientes.
     *
     * @return lista de mensajes de contacto
     */
    public List<Mensaje> listarTodos() {
        List<Mensaje> mensajes = new ArrayList<>();
        String sql = "SELECT id_mensaje, nombre, correo, asunto, contenido, fecha_envio " +
                "FROM mensaje_contacto ORDER BY fecha_envio DESC";
        try (
                Connection conexion = Conexion.conectar();
                PreparedStatement sentencia = conexion.prepareStatement(sql);
                ResultSet resultado = sentencia.executeQuery()
        ) {
            while (resultado.next()) {
                Mensaje mensaje = new Mensaje();
                mensaje.setIdMensaje(resultado.getInt("id_mensaje"));
                mensaje.setNombre(resultado.getString("nombre"));
                mensaje.setCorreo(resultado.getString("correo"));
                mensaje.setAsunto(resultado.getString("asunto"));
                mensaje.setContenido(resultado.getString("contenido"));
                mensaje.setFechaEnvio(resultado.getTimestamp("fecha_envio"));
                mensajes.add(mensaje);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar mensajes de contacto: " + e.getMessage());
        }
        return mensajes;
    }
}