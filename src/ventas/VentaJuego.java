package ventas;

import java.util.ArrayList;
import java.util.List;

import usuarios.*;

public class VentaJuego {
    private String idVenta;
    private String fecha;
    private double impuesto;
    private double propina;
    private double subTotal;
    private double total;
    private Usuario comprador;
    private int puntosGenerados;
    private int puntosUsados;
    private boolean descuentoAplicado;
    private List<DetalleVenta> detalles;

    public VentaJuego(String idVenta, String fecha, Usuario comprador) {
        this.idVenta = idVenta;
        this.fecha = fecha;
        this.comprador = comprador;
        this.impuesto = 0;
        this.propina = 0;
        this.subTotal = 0;
        this.total = 0;
        this.puntosGenerados = 0;
        this.puntosUsados = 0;
        this.descuentoAplicado = false;
        this.detalles = new ArrayList<>();
    }

    public String getIdVenta() {
        return idVenta;
    }

    public String getFecha() {
        return fecha;
    }

    public double getImpuesto() {
        return impuesto;
    }

    public double getPropina() {
        return propina;
    }

    public double getSubTotal() {
        return subTotal;
    }

    public double getTotal() {
        return total;
    }

    public Usuario getComprador() {
        return comprador;
    }

    public int getPuntosGenerados() {
        return puntosGenerados;
    }

    public List<DetalleVenta> getDetalles() {
        return detalles;
    }

    public void agregarDetalle(DetalleVenta detalle) {
        detalles.add(detalle);
        subTotal += detalle.getSubtotal();
        total = subTotal + impuesto + propina;
    }

    public void calcularValores(double porcentajeImpuesto, double descuento, int puntosAUsar) {
    		double subtotalConDescuento = this.subTotal * (1 - descuento);
        impuesto = subtotalConDescuento * porcentajeImpuesto;
        double totalAntesdePuntos = subtotalConDescuento + impuesto;
        double totalFinal = totalAntesdePuntos - puntosAUsar;
        total = Math.max(totalFinal, 0);
        puntosGenerados = (int) (total * 0.01);
    }

    public void setTotal(double total) {
        this.total = total;
    }
    
    @Override
    public String toString() {
        return "id\t" + this.idVenta + "|fecha\t" + this.fecha + "|comprador\t" + this.comprador.getLogin() + "|subtotal\t" + this.subTotal + "|impuesto\t" + this.impuesto + "|total\t" + this.total + "|puntos\t" + this.puntosGenerados;
    }
    
    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }

    public void setImpuesto(double impuesto) {
        this.impuesto = impuesto;
    }

    public void setPuntosGenerados(int puntosGenerados) {
        this.puntosGenerados = puntosGenerados;
    }
}
