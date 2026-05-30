package ventas;

import juegos.JuegoMesaVenta;

public class DetalleVenta {
    private int cantidad;
    private double subtotal;
    private JuegoMesaVenta juego;

    public DetalleVenta(int cantidad, JuegoMesaVenta juego) {
        this.cantidad = cantidad;
        this.juego = juego;
        this.subtotal = cantidad * juego.getPrecioVenta();
    }

    public int getCantidad() {
        return cantidad;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public JuegoMesaVenta getJuego() {
        return juego;
    }
}
