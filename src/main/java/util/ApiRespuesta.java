package util;

/**
 * Clase que representa el formato estándar de respuesta
 * utilizado por todos los servicios web (API REST) de
 * la tienda virtual J&M Makeup.
 *
 * Todas las respuestas de la API siguen la misma estructura,
 * lo que facilita su consumo desde cualquier cliente
 * (aplicación web, móvil, Postman, etc.).
 *
 * Estructura de la respuesta:
 * {
 *   "exito": true,
 *   "mensaje": "Descripción del resultado",
 *   "datos": { ... } // puede ser un objeto, una lista o null
 * }
 *
 * Proyecto: J&M Makeup
 * Evidencia: GA7-220501096-AA5-EV03
 *
 * @author J&M Makeup
 *
 * @param <T> tipo de dato que se envía en la respuesta
 */
public class ApiRespuesta<T> {

    // Indica si la operación fue exitosa o no.
    private boolean exito;

    // Mensaje descriptivo del resultado de la operación.
    private String mensaje;

    // Datos que se retornan en la respuesta (puede ser null).
    private T datos;

    /**
     * Constructor completo.
     *
     * @param exito indica si la operación fue exitosa
     * @param mensaje mensaje descriptivo del resultado
     * @param datos datos que se retornan en la respuesta
     */
    public ApiRespuesta(boolean exito, String mensaje, T datos) {
        this.exito = exito;
        this.mensaje = mensaje;
        this.datos = datos;
    }

    /**
     * Crea una respuesta exitosa con datos.
     *
     * @param mensaje mensaje descriptivo
     * @param datos datos que se retornan
     * @return respuesta exitosa
     */
    public static <T> ApiRespuesta<T> exito(String mensaje, T datos) {
        return new ApiRespuesta<>(true, mensaje, datos);
    }

    /**
     * Crea una respuesta exitosa sin datos.
     *
     * @param mensaje mensaje descriptivo
     * @return respuesta exitosa
     */
    public static <T> ApiRespuesta<T> exito(String mensaje) {
        return new ApiRespuesta<>(true, mensaje, null);
    }

    /**
     * Crea una respuesta de error.
     *
     * @param mensaje mensaje descriptivo del error
     * @return respuesta de error
     */
    public static <T> ApiRespuesta<T> error(String mensaje) {
        return new ApiRespuesta<>(false, mensaje, null);
    }

    // Getter y Setter de exito
    public boolean isExito() {
        return exito;
    }
    public void setExito(boolean exito) {
        this.exito = exito;
    }

    // Getter y Setter de mensaje
    public String getMensaje() {
        return mensaje;
    }
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    // Getter y Setter de datos
    public T getDatos() {
        return datos;
    }
    public void setDatos(T datos) {
        this.datos = datos;
    }
}