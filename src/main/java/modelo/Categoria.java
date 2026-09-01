package modelo;
/**
 * Clase modelo que representa una categoría de productos
 * de la tienda virtual J&M Makeup.
 *
 * Una categoría permite organizar los productos
 * de maquillaje dentro de la aplicación.
 *
 * Proyecto: J&M Makeup
 * Evidencia: GA7-220501096-AA3-EV01
 *
 * @author J&M Makeup
 */
public class Categoria {
    // Identificador único de la categoría
    private int idCategoria;
    // Nombre de la categoría
    private String nombre;
    // Descripción de la categoría
    private String descripcion;
    /**
     * Constructor vacío.
     *
     * Se utiliza para crear un objeto Categoria
     * sin asignar valores inicialmente.
     */
    public Categoria() {
    }
    /**
     * Constructor completo.
     *
     * @param idCategoria identificador de la categoría
     * @param nombre nombre de la categoría
     * @param descripcion descripción de la categoría
     */
    public Categoria(int idCategoria, String nombre, String descripcion) {
        this.idCategoria = idCategoria;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }
    // Getter y Setter del identificador de la categoría
    public int getIdCategoria() {
        return idCategoria;
    }
    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
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
    /**
     * Método toString.
     *
     * Permite representar la información de la categoría
     * en formato de texto.
     *
     * @return información de la categoría
     */
    @Override
    public String toString() {
        return "Categoria{" + "idCategoria=" + idCategoria + ", nombre='" + nombre + '\'' + ", descripcion='" + descripcion + '\'' + '}';
    }
}