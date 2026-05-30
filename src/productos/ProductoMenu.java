package productos;

public abstract class ProductoMenu {
    private String idProducto;
    private String nombre;
    private double precio;
    private boolean disponible;

    public ProductoMenu(String idProducto, String nombre, double precio, boolean disponible) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.precio = precio;
        this.disponible = disponible;
    }

    public String getIdProducto() {
        return idProducto;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }
    
    @Override
    public String toString() {
        return "id\t" + this.idProducto + "|nombre\t" + this.nombre + "|precio\t" + this.precio + "|disponible\t" + this.disponible;
    }
    
    
}
