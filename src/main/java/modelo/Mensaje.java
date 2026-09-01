package modelo;

import java.sql.Timestamp;

/**
 * Clase modelo que representa un mensaje enviado
 * desde el formulario de contacto de J&M Makeup.
 *
 * Esta clase contiene los atributos y métodos necesarios
 * para almacenar la información de los mensajes enviados
 * por los visitantes de la tienda.
 *
 * Proyecto: J&M Makeup
 * Evidencia: GA7-220501096-AA4-EV03
 *
 * @author J&M Makeup
 */
public class Mensaje {
    // Identificador único del mensaje
    private int idMensaje;
    // Nombre de la persona que escribe
    private String nombre;
    // Correo electrónico de contacto
    private String correo;
    // Asunto del mensaje
    private String asunto;
    // Contenido del mensaje
    private String contenido;
    // Fecha y hora en que se envió el mensaje
    private Timestamp fechaEnvio;

    /**
     * Constructor vacío.
     * Se utiliza para crear un objeto Mensaje sin datos iniciales.
     */
    public Mensaje() {
    }

    /**
     * Constructor usado al recibir los datos
     * del formulario de contacto, antes de guardarlos.
     *
     * @param nombre nombre de quien escribe
     * @param correo correo electrónico
     * @param asunto asunto del mensaje
     * @param contenido contenido del mensaje
     */
    public Mensaje(String nombre, String correo, String asunto, String contenido) {
        this.nombre = nombre;
        this.correo = correo;
        this.asunto = asunto;
        this.contenido = contenido;
    }

    /**
     * Constructor completo.
     *
     * @param idMensaje identificador del mensaje
     * @param nombre nombre de quien escribe
     * @param correo correo electrónico
     * @param asunto asunto del mensaje
     * @param contenido contenido del mensaje
     * @param fechaEnvio fecha y hora de envío
     */
    public Mensaje(int idMensaje, String nombre, String correo,
                   String asunto, String contenido, Timestamp fechaEnvio) {
        this.idMensaje = idMensaje;
        this.nombre = nombre;
        this.correo = correo;
        this.asunto = asunto;
        this.contenido = contenido;
        this.fechaEnvio = fechaEnvio;
    }

    // Getter y Setter del identificador
    public int getIdMensaje() {
        return idMensaje;
    }
    public void setIdMensaje(int idMensaje) {
        this.idMensaje = idMensaje;
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

    // Getter y Setter del asunto
    public String getAsunto() {
        return asunto;
    }
    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    // Getter y Setter del contenido
    public String getContenido() {
        return contenido;
    }
    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    // Getter y Setter de la fecha de envío
    public Timestamp getFechaEnvio() {
        return fechaEnvio;
    }
    public void setFechaEnvio(Timestamp fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }

    /**
     * Método toString para representar la información
     * principal del mensaje.
     *
     * @return información del mensaje
     */
    @Override
    public String toString() {
        return "Mensaje{" + "idMensaje=" + idMensaje + ", nombre='" + nombre + '\'' +
                ", correo='" + correo + '\'' + ", asunto='" + asunto + '\'' + '}';
    }
}