package modelo;
import java.math.BigDecimal;
/**
 * Clase modelo que representa un producto de la tienda virtual J&M Makeup.
 *
 * Esta clase contiene los atributos y métodos necesarios para
 * representar los productos almacenados en la base de datos.
 *
 * Proyecto: J&M Makeup
 * Evidencia: GA7-220501096-AA3-EV01
 *
 * @author J&M Makeup
 */
public class Producto {
    // Identificador único del producto
    private int idProducto;
    // Nombre del producto
    private String nombre;
    // Descripción detallada del producto
    private String descripcion;
    // Precio del producto
    private BigDecimal precio;
    // Cantidad disponible en inventario
    private int stock;
    // Identificador de la categoría a la que pertenece el producto
    private int idCategoria;
    // Ruta o nombre del archivo de imagen del producto
    private String imagen;
    // Precio de oferta del producto (null si no está en oferta)
    private BigDecimal precioOferta;
    /**
     * Constructor vacío.
     *
     * Se utiliza para crear un objeto Producto
     * sin asignar valores inicialmente.
     */
    public Producto() {
    }
    /**
     * Constructor completo.
     *
     * @param idProducto identificador del producto
     * @param nombre nombre del producto
     * @param descripcion descripción del producto
     * @param precio precio del producto
     * @param stock cantidad disponible
     * @param idCategoria identificador de la categoría
     * @param imagen ruta o nombre del archivo de imagen
     * @param precioOferta precio de oferta, o null si no aplica
     */
    public Producto(int idProducto, String nombre, String descripcion, BigDecimal precio, int stock, int idCategoria, String imagen, BigDecimal precioOferta) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
        this.idCategoria = idCategoria;
        this.imagen = imagen;
        this.precioOferta = precioOferta;
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

    // Getter y Setter de la descripción
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    // Getter y Setter del precio
    public BigDecimal getPrecio() {
        return precio;
    }
    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }
    // Getter y Setter del stock
    public int getStock() {
        return stock;
    }
    public void setStock(int stock) {
        this.stock = stock;
    }
    // Getter y Setter del identificador de categoría
    public int getIdCategoria() {
        return idCategoria;
    }
    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }
    // Getter y Setter de la imagen
    public String getImagen() {
        return imagen;
    }
    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
    // Getter y Setter del precio de oferta
    public BigDecimal getPrecioOferta() {
        return precioOferta;
    }
    public void setPrecioOferta(BigDecimal precioOferta) {
        this.precioOferta = precioOferta;
    }
    /**
     * Indica si el producto tiene una oferta activa.
     *
     * Se considera en oferta si tiene un precio de oferta
     * asignado y este es menor al precio normal.
     *
     * @return true si el producto está en oferta
     */
    public boolean tieneOferta() {
        return precioOferta != null && precioOferta.compareTo(precio) < 0;
    }
    /**
     * Obtiene el precio real que debe pagar el cliente.
     *
     * Si el producto tiene una oferta activa, retorna
     * el precio de oferta; de lo contrario, el precio normal.
     *
     * @return precio efectivo del producto
     */
    public BigDecimal getPrecioEfectivo() {
        return tieneOferta() ? precioOferta : precio;
    }
    /**
     * Método toString.
     *
     * Permite representar la información del producto
     * en forma de texto.
     *
     * @return información del producto
     */
    @Override
    public String toString() {
        return "Producto{" + "idProducto=" + idProducto + ", nombre='" + nombre + '\'' + ", descripcion='" + descripcion + '\'' + ", precio=" + precio + ", stock=" + stock + ", idCategoria=" + idCategoria + ", imagen='" + imagen + '\'' + ", precioOferta=" + precioOferta + '}';
    }
}