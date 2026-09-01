package modelo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Clase modelo que representa un pedido realizado
 * por un usuario en la tienda virtual J&M Makeup.
 *
 * El pedido se genera cuando el usuario finaliza
 * el proceso de compra de los productos seleccionados.
 *
 * Proyecto: J&M Makeup
 * Evidencia: GA7-220501096-AA3-EV01
 *
 * @author J&M Makeup
 */
public class Pedido {

    // Identificador único del pedido
    private int idPedido;

    // Identificador del usuario que realiza el pedido
    private int idUsuario;

    // Identificador del carrito relacionado con el pedido
    private int idCarrito;

    // Fecha y hora en que se realizó el pedido
    private LocalDateTime fechaPedido;

    // Estado actual del pedido
    // Ejemplos: pendiente, pagado, enviado, entregado o cancelado
    private String estado;

    // Método de pago seleccionado por el usuario
    // Ejemplos: efectivo, tarjeta, transferencia
    private String metodoPago;

    // Valor total del pedido
    private BigDecimal total;

    /**
     * Constructor vacío.
     *
     * Se utiliza para crear un objeto Pedido
     * sin asignar valores inicialmente.
     */
    public Pedido() {
    }

    /**
     * Constructor completo.
     *
     * @param idPedido identificador del pedido
     * @param idUsuario identificador del usuario
     * @param idCarrito identificador del carrito
     * @param fechaPedido fecha y hora del pedido
     * @param estado estado actual del pedido
     * @param metodoPago método de pago seleccionado
     * @param total valor total del pedido
     */
    public Pedido(int idPedido, int idUsuario, int idCarrito, LocalDateTime fechaPedido, String estado, String metodoPago, BigDecimal total) {
        this.idPedido = idPedido;
        this.idUsuario = idUsuario;
        this.idCarrito = idCarrito;
        this.fechaPedido = fechaPedido;
        this.estado = estado;
        this.metodoPago = metodoPago;
        this.total = total;
    }

    // Getter y Setter del identificador del pedido
    public int getIdPedido() {
        return idPedido;
    }
    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    // Getter y Setter del identificador del usuario
    public int getIdUsuario() {
        return idUsuario;
    }
    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    // Getter y Setter del identificador del carrito
    public int getIdCarrito() {
        return idCarrito;
    }
    public void setIdCarrito(int idCarrito) {
        this.idCarrito = idCarrito;
    }

    // Getter y Setter de la fecha del pedido
    public LocalDateTime getFechaPedido() {
        return fechaPedido;
    }
    public void setFechaPedido(LocalDateTime fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    // Getter y Setter del estado
    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }

    // Getter y Setter del método de pago
    public String getMetodoPago() {
        return metodoPago;
    }
    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
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
     * del pedido en formato de texto.
     *
     * @return información del pedido
     */
    @Override
    public String toString() {
        return "Pedido{" + "idPedido=" + idPedido + ", idUsuario=" + idUsuario + ", idCarrito=" + idCarrito + ", fechaPedido=" + fechaPedido + ", estado='" + estado + '\'' + ", metodoPago='" + metodoPago + '\'' + ", total=" + total + '}';
    }
}