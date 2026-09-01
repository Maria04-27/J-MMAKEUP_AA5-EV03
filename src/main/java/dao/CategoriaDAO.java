package dao;

import conexion.Conexion;
import modelo.Categoria;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase DAO encargada de realizar las operaciones
 * relacionadas con las categorías de productos de la
 * tienda virtual J&M Makeup en la base de datos.
 *
 * DAO significa Data Access Object.
 *
 * Esta clase permite:
 * - Registrar categorías.
 * - Consultar todas las categorías.
 * - Buscar una categoría por su identificador.
 * - Actualizar categorías.
 * - Eliminar categorías.
 *
 * Proyecto: J&M Makeup
 * Evidencia: GA7-220501096-AA5-EV03
 *
 * @author J&M Makeup
 */
public class CategoriaDAO {

    /**
     * Registra una nueva categoría en la base de datos.
     *
     * @param categoria objeto Categoria que se desea registrar
     * @return true si el registro fue exitoso,
     *         false si ocurrió un error
     */
    public boolean registrar(Categoria categoria) {
        String sql = "INSERT INTO categoria (nombre, descripcion) VALUES (?, ?)";
        try (
                Connection conexion = Conexion.conectar();
                PreparedStatement sentencia = conexion.prepareStatement(sql)
        ) {
            sentencia.setString(1, categoria.getNombre());
            sentencia.setString(2, categoria.getDescripcion());
            int filasAfectadas = sentencia.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.err.println("Error al registrar categoría: " + e.getMessage());
            return false;
        }
    }

    /**
     * Obtiene todas las categorías registradas
     * en la base de datos.
     *
     * @return lista de categorías
     */
    public List<Categoria> listarTodas() {
        List<Categoria> categorias = new ArrayList<>();
        String sql = "SELECT id_categoria, nombre, descripcion FROM categoria ORDER BY nombre ASC";
        try (
                Connection conexion = Conexion.conectar();
                PreparedStatement sentencia = conexion.prepareStatement(sql);
                ResultSet resultado = sentencia.executeQuery()
        ) {
            while (resultado.next()) {
                categorias.add(construirCategoria(resultado));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar categorías: " + e.getMessage());
        }
        return categorias;
    }

    /**
     * Busca una categoría por su identificador.
     *
     * @param idCategoria identificador de la categoría
     * @return objeto Categoria si existe,
     *         null si no se encuentra
     */
    public Categoria buscarPorId(int idCategoria) {
        String sql = "SELECT id_categoria, nombre, descripcion FROM categoria WHERE id_categoria = ?";
        try (
                Connection conexion = Conexion.conectar();
                PreparedStatement sentencia = conexion.prepareStatement(sql)
        ) {
            sentencia.setInt(1, idCategoria);
            ResultSet resultado = sentencia.executeQuery();
            if (resultado.next()) {
                return construirCategoria(resultado);
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar categoría: " + e.getMessage());
        }
        return null;
    }

    /**
     * Actualiza la información de una categoría.
     *
     * @param categoria categoría con la información actualizada
     * @return true si la actualización fue exitosa,
     *         false si ocurrió un error
     */
    public boolean actualizar(Categoria categoria) {
        String sql = "UPDATE categoria SET nombre = ?, descripcion = ? WHERE id_categoria = ?";
        try (
                Connection conexion = Conexion.conectar();
                PreparedStatement sentencia = conexion.prepareStatement(sql)
        ) {
            sentencia.setString(1, categoria.getNombre());
            sentencia.setString(2, categoria.getDescripcion());
            sentencia.setInt(3, categoria.getIdCategoria());
            int filasAfectadas = sentencia.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar categoría: " + e.getMessage());
            return false;
        }
    }

    /**
     * Elimina una categoría de la base de datos.
     *
     * @param idCategoria identificador de la categoría
     * @return true si la eliminación fue exitosa,
     *         false si ocurrió un error
     */
    public boolean eliminar(int idCategoria) {
        String sql = "DELETE FROM categoria WHERE id_categoria = ?";
        try (
                Connection conexion = Conexion.conectar();
                PreparedStatement sentencia = conexion.prepareStatement(sql)
        ) {
            sentencia.setInt(1, idCategoria);
            int filasAfectadas = sentencia.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar categoría: " + e.getMessage());
            return false;
        }
    }

    /**
     * Convierte un resultado de MySQL
     * en un objeto Categoria.
     *
     * @param resultado resultado obtenido de la base de datos
     * @return objeto Categoria
     * @throws SQLException si ocurre un error
     */
    private Categoria construirCategoria(ResultSet resultado) throws SQLException {
        Categoria categoria = new Categoria();
        categoria.setIdCategoria(resultado.getInt("id_categoria"));
        categoria.setNombre(resultado.getString("nombre"));
        categoria.setDescripcion(resultado.getString("descripcion"));
        return categoria;
    }
}