package util;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Clase utilitaria para formatear valores monetarios
 * con el formato colombiano (puntos como separador de miles,
 * sin decimales).
 *
 * Proyecto: J&M Makeup
 * Evidencia: GA7-220501096-AA3-EV01
 *
 * @author J&M Makeup
 */
public class Formato {

    /**
     * Formatea un valor BigDecimal como moneda colombiana.
     * Ejemplo: 50000 -> "50.000"
     *
     * @param valor valor a formatear
     * @return texto formateado
     */
    public static String moneda(BigDecimal valor) {
        if (valor == null) {
            return "0";
        }
        NumberFormat formato = NumberFormat.getInstance(new Locale("es", "CO"));
        formato.setMaximumFractionDigits(0);
        return formato.format(valor);
    }

    /**
     * Formatea un valor double como moneda colombiana.
     * Se usa para los precios guardados en DetalleCarrito.
     *
     * @param valor valor a formatear
     * @return texto formateado
     */
    public static String moneda(double valor) {
        NumberFormat formato = NumberFormat.getInstance(new Locale("es", "CO"));
        formato.setMaximumFractionDigits(0);
        return formato.format(valor);
    }
}