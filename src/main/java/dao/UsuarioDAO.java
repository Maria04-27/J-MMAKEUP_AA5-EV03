package dao;

import conexion.Conexion;
import modelo.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Clase DAO encargada de realizar las operaciones
 * relacionadas con los usuarios en la base de datos.
 *
 * DAO significa Data Access Object.
 *
 * Esta clase permite:
 * - Registrar usuarios.
 * - Buscar usuarios por correo.
 * - Validar el inicio de sesión.
 *
 * Proyecto: J&M Makeup
 * Evidencia: GA7-220501096-AA3-EV01
 *
 * @author J&M Makeup
 */
public class UsuarioDAO {
    /**
     * Registra un nuevo usuario en la base de datos.
     *
     * @param usuario objeto Usuario con la información
     *                que se desea registrar
     * @return true si el registro fue exitoso,
     *         false si ocurrió algún error
     */
    public boolean registrar(Usuario usuario) {
        String sql ="INSERT INTO usuario (nombre, correo, password, rol) VALUES (?, ?, ?, ?)";
        try (
                Connection conexion = Conexion.conectar();
                PreparedStatement sentencia = conexion.prepareStatement(sql)
        ) {
            // Asignamos los datos del usuario
            sentencia.setString(1, usuario.getNombre());
            sentencia.setString(2, usuario.getCorreo());
            sentencia.setString(3, usuario.getPassword());
            sentencia.setString(4, usuario.getRol());
            // Ejecutamos el INSERT
            int filasAfectadas = sentencia.executeUpdate();
            // Si se insertó una fila, el registro fue exitoso
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.err.println("Error al registrar usuario: " + e.getMessage()
            );
            return false;
        }
    }
    /**
     * Busca un usuario utilizando su correo electrónico.
     *
     * @param correo correo electrónico del usuario
     * @return objeto Usuario si existe,
     *         null si no se encuentra
     */
    public Usuario buscarPorCorreo(String correo) {
        String sql ="SELECT id_usuario, nombre, correo, password, rol FROM usuario WHERE correo = ?";
        try (
                Connection conexion = Conexion.conectar();
                PreparedStatement sentencia = conexion.prepareStatement(sql)
        ) {
            // Establecemos el correo que se desea consultar
            sentencia.setString(1, correo);
            // Ejecutamos la consulta
            ResultSet resultado = sentencia.executeQuery();
            // Verificamos si encontramos un usuario
            if (resultado.next()) {
                Usuario usuario = new Usuario();
                usuario.setIdUsuario(resultado.getInt("id_usuario"));
                usuario.setNombre(resultado.getString("nombre"));
                usuario.setCorreo(resultado.getString("correo"));
                usuario.setPassword(resultado.getString("password"));
                usuario.setRol(resultado.getString("rol"));
                return usuario;
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar usuario: " + e.getMessage());
        }
        return null;
    }
    /**
     * Valida las credenciales de inicio de sesión.
     *
     * @param correo correo electrónico del usuario
     * @param password contraseña del usuario
     * @return objeto Usuario si las credenciales son correctas,
     *         null si las credenciales no son válidas
     */
    public Usuario iniciarSesion(
            String correo,
            String password
    ) {
        String sql ="SELECT id_usuario, nombre, correo, password, rol FROM usuario WHERE correo = ? AND password = ?";
        try (
                Connection conexion = Conexion.conectar();
                PreparedStatement sentencia = conexion.prepareStatement(sql)
        ) {
            // Asignamos el correo
            sentencia.setString(1, correo);
            // Asignamos la contraseña
            sentencia.setString(2, password);
            // Ejecutamos la consulta
            ResultSet resultado = sentencia.executeQuery();
            // Verificamos si las credenciales son correctas
            if (resultado.next()) {
                Usuario usuario = new Usuario();
                usuario.setIdUsuario(resultado.getInt("id_usuario"));
                usuario.setNombre(resultado.getString("nombre"));
                usuario.setCorreo(resultado.getString("correo"));
                usuario.setPassword(resultado.getString("password"));
                usuario.setRol(resultado.getString("rol"));
                return usuario;
            }
        } catch (SQLException e) {
            System.err.println("Error al iniciar sesión: " + e.getMessage());
        }
        return null;
    }
}