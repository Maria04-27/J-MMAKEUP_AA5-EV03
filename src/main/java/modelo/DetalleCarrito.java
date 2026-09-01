package modelo;

/**
 * Clase modelo que representa un producto
 * dentro del carrito de compras.
 * A diferencia de la clase Carrito (que representa
 * el carrito en general), esta clase representa
 * cada producto individual agregado a él.
 * Proyecto: J&M Makeup
 * Evidencia: GA7-220501096-AA3-EV01
 * @author J&M Makeup
 */
public class DetalleCarrito {

    // Identificador del producto agregado al carrito
    private int idProducto;
    // Nombre del producto
    private String nombre;
    // Precio unitario del producto
    private double precio;
    // Cantidad seleccionada por el usuario
    private int cantidad;

    /**
     * Constructor vacío.
     * Se utiliza para crear un objeto sin datos iniciales.
     */
    public DetalleCarrito() {
    }

    /**
     * Constructor completo.
     *
     * @param idProducto identificador del producto
     * @param nombre nombre del producto
     * @param precio precio unitario
     * @param cantidad cantidad seleccionada
     */
    public DetalleCarrito(int idProducto, String nombre, double precio, int cantidad) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.precio = precio;
        this.cantidad = cantidad;
    }

    // Getter y Setter del identificador del producto
    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    // Getter y Setter del nombre
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    // Getter y Setter del precio
    public double getPrecio() {
        return precio;
    }
    public void setPrecio(double precio) {
        this.precio = precio;
    }
    // Getter y Setter de la cantidad
    public int getCantidad() {
        return cantidad;
    }
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}