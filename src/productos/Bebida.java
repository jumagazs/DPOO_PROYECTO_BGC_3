package productos;

public class Bebida extends ProductoMenu {
    private boolean alcoholica;
    private boolean caliente;

    public Bebida(String idProducto, String nombre, double precio, boolean disponible, boolean alcoholica, boolean caliente) {
        super(idProducto, nombre, precio, disponible);
        this.alcoholica = alcoholica;
        this.caliente = caliente;
    }

    public boolean isAlcoholica() {
        return alcoholica;
    }

    public boolean isCaliente() {
        return caliente;
    }
    
    @Override
    public String toString() {
        return super.toString()
            + "|alcoholica\t" + this.alcoholica
            + "|caliente\t" + this.caliente;
    }
}
