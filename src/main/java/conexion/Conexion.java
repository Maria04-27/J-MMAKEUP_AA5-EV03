package conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase encargada de establecer y gestionar
 * la conexión entre la aplicación J&M Makeup
 * y la base de datos MySQL.
 *
 * Proyecto: J&M Makeup
 * Evidencia: GA7-220501096-AA3-EV01
 *
 * @author J&M Makeup
 */
public class Conexion {

    // URL de conexión a la base de datos MySQL.
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/jmmakeup?useSSL=false&serverTimezone=America/Bogota&allowPublicKeyRetrieval=true";

    // Usuario configurado en MySQL.
    private static final String USUARIO = "root";

    // Contraseña del usuario de MySQL.
    private static final String PASSWORD = "";

    // Bloque estático: se ejecuta una sola vez,
    // cuando la clase se carga por primera vez.
    // Registra manualmente el driver de MySQL,
    // necesario en algunos entornos de Tomcat
    // donde el auto-registro del driver no funciona.
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("No se encontró el driver de MySQL: " + e.getMessage());
        }
    }

    /**
     * Establece una conexión con la base de datos MySQL.
     *
     * @return objeto Connection con la conexión establecida
     * @throws SQLException si ocurre un error al conectar
     */
    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, PASSWORD);
    }

    /**
     * Cierra la conexión con la base de datos.
     *
     * @param conexion conexión que se desea cerrar
     */
    public static void cerrar(Connection conexion) {
        if (conexion != null) {
            try {
                conexion.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }
}