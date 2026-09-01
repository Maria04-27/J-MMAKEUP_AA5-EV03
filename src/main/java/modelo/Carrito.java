package modelo;

import java.math.BigDecimal;

/**
 * Clase modelo que representa el carrito de compras
 * de un usuario en la tienda virtual J&M Makeup.
 *
 * El carrito permite asociar un usuario con los productos
 * que desea comprar antes de realizar el pedido.
 *
 * Proyecto: J&M Makeup
 * Evidencia: GA7-220501096-AA3-EV01
 *
 * @author J&M Makeup
 */
public class Carrito {
    // Identificador único del carrito
    private int idCarrito;

    // Identificador del usuario propietario del carrito
    private int idUsuario;

    // Estado actual del carrito
    // Ejemplos: activo, comprado o abandonado
    private String estado;

    // Valor total de los productos incluidos en el carrito
    private BigDecimal total;

    /**
     * Constructor vacío.
     *
     * Se utiliza para crear un objeto Carrito
     * sin asignar valores inicialmente.
     */
    public Carrito() {
    }

    /**
     * Constructor completo.
     *
     * @param idCarrito identificador del carrito
     * @param idUsuario identificador del usuario
     * @param estado estado actual del carrito
     * @param total valor total del carrito
     */
    public Carrito(int idCarrito, int idUsuario, String estado, BigDecimal total) {
        this.idCarrito = idCarrito;
        this.idUsuario = idUsuario;
        this.estado = estado;
        this.total = total;
    }

    // Getter y Setter del identificador del carrito
    public int getIdCarrito() {
        return idCarrito;
    }
    public void setIdCarrito(int idCarrito) {
        this.idCarrito = idCarrito;
    }

    // Getter y Setter del identificador del usuario
    public int getIdUsuario() {
        return idUsuario;
    }
    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    // Getter y Setter del estado
    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }

    // Getter y Setter del total
    public BigDecimal getTotal() {
        return total;
    }
    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    /**
     * Método toString.
     *
     * Permite representar la información principal
     * del carrito en formato de texto.
     *
     * @return información del carrito
     */
    @Override
    public String toString() {
        return "Carrito{" + "idCarrito=" + idCarrito + ", idUsuario=" + idUsuario + ", estado='" + estado + '\'' + ", total=" + total + '}';
    }
}