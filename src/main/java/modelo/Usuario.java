package modelo;

/**
 * Clase modelo que representa un usuario del sistema J&M Makeup.
 *
 * Esta clase contiene los atributos y métodos necesarios
 * para almacenar la información de los usuarios registrados
 * en la aplicación.
 *
 * Proyecto: J&M Makeup
 * Evidencia: GA7-220501096-AA3-EV01
 *
 * @author J&M Makeup
 */
public class Usuario {
    // Identificador único del usuario
    private int idUsuario;
    // Nombre completo del usuario
    private String nombre;
    // Correo electrónico utilizado para iniciar sesión
    private String correo;
    // Contraseña del usuario
    private String password;
    // Rol del usuario dentro del sistema
    private String rol;
    /**
     * Constructor vacío.
     * Se utiliza para crear un objeto Usuario sin datos iniciales.
     */
    public Usuario() {
    }
    /**
     * Constructor completo.
     *
     * @param idUsuario identificador del usuario
     * @param nombre nombre completo
     * @param correo correo electrónico
     * @param password contraseña
     * @param rol rol del usuario
     */
    public Usuario(int idUsuario, String nombre, String correo,
                   String password, String rol) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.correo = correo;
        this.password = password;
        this.rol = rol;
    }
    // Getter y Setter del identificador
    public int getIdUsuario() {
        return idUsuario;
    }
    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }
    // Getter y Setter del nombre
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    // Getter y Setter del correo
    public String getCorreo() {
        return correo;
    }
    public void setCorreo(String correo) {
        this.correo = correo;
    }
    // Getter y Setter de la contraseña
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    // Getter y Setter del rol
    public String getRol() {
        return rol;
    }
    public void setRol(String rol) {
        this.rol = rol;
    }
    /**
     * Método toString para representar la información
     * principal del usuario.
     *
     * Por seguridad, no se muestra la contraseña.
     *
     * @return información del usuario
     */
    @Override
    public String toString() {
        return "Usuario{" + "idUsuario=" + idUsuario + ", nombre='" + nombre + '\'' + ", correo='" + correo + '\'' + ", rol='" + rol + '\'' + '}';
    }
}