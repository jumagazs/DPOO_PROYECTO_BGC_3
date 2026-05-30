package pedidos;

import productos.ProductoMenu;

public class DetallePedido {
    private int cantidad;
    private double subtotal;
    private ProductoMenu producto;

    public DetallePedido(int cantidad, ProductoMenu producto) {
        this.cantidad = cantidad;
        this.producto = producto;
        this.subtotal = cantidad * producto.getPrecio();
    }

    public int getCantidad() {
        return cantidad;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public ProductoMenu getProducto() {
        return producto;
    }
}
