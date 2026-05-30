package productos;

import java.util.List;

public class Pasteleria extends ProductoMenu {
    private List<String> alergenos;

    public Pasteleria(String idProducto, String nombre, double precio, boolean disponible, List<String> alergenos) {
        super(idProducto, nombre, precio, disponible);
        this.alergenos = alergenos;
    }

    public List<String> getAlergenos() {
        return alergenos;
    }
    
    @Override
    public String toString() {
        return super.toString() + "|alergenos\t" + String.join(",", this.alergenos);
    }
}
