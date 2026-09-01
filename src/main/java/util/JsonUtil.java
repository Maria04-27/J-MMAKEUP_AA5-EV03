package util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonDeserializer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Clase utilitaria encargada de centralizar la configuración
 * de Gson utilizada por todos los servicios web (API REST)
 * de la tienda virtual J&M Makeup.
 *
 * Se encarga de:
 * - Registrar un adaptador para LocalDateTime, ya que Gson
 *   no lo soporta de forma nativa.
 * - Entregar una única instancia de Gson reutilizable
 *   por todos los Servlets de la API.
 *
 * Proyecto: J&M Makeup
 * Evidencia: GA7-220501096-AA5-EV03
 *
 * @author J&M Makeup
 */
public class JsonUtil {

    // Formato de fecha y hora utilizado en las respuestas JSON.
    private static final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    // Instancia única de Gson configurada con el adaptador
    // de fechas, compartida por todos los servicios.
    private static final Gson GSON = construirGson();

    /**
     * Construye la instancia de Gson utilizada
     * por toda la API, registrando el adaptador
     * necesario para el tipo LocalDateTime.
     *
     * @return instancia de Gson configurada
     */
    private static Gson construirGson() {
        GsonBuilder builder = new GsonBuilder();
        // Serializador: convierte LocalDateTime en texto.
        JsonSerializer<LocalDateTime> serializador =
                (fecha, tipo, contexto) -> new JsonPrimitive(fecha.format(FORMATO_FECHA));
        // Deserializador: convierte el texto recibido en LocalDateTime.
        JsonDeserializer<LocalDateTime> deserializador =
                (elemento, tipo, contexto) -> LocalDateTime.parse(elemento.getAsString(), FORMATO_FECHA);
        builder.registerTypeAdapter(LocalDateTime.class, serializador);
        builder.registerTypeAdapter(LocalDateTime.class, deserializador);
        return builder.setPrettyPrinting().create();
    }

    /**
     * Obtiene la instancia de Gson configurada
     * para ser utilizada por los servicios web.
     *
     * @return instancia de Gson
     */
    public static Gson getGson() {
        return GSON;
    }
}