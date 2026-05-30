package prestamos;

import java.time.LocalDateTime;

import juegos.JuegoMesaPrestamo;
import mesas.Mesa;
import usuarios.*;

public class Prestamo {
    private String idPrestamo;
    private String fechaPrestamo;
    private String fechaDevolucion;
    private boolean fueExplicado;
    private JuegoMesaPrestamo juego;
    private Usuario usuario;
    private Mesa mesa;

    public Prestamo(String idPrestamo, String fechaPrestamo, boolean fueExplicado,
                    JuegoMesaPrestamo juego, Usuario usuario, Mesa mesa) {
        this.idPrestamo = idPrestamo;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaDevolucion = null;
        this.fueExplicado = fueExplicado;
        this.juego = juego;
        this.usuario = usuario;
        this.mesa = mesa;
    }

    public String getIdPrestamo() {
        return idPrestamo;
    }

    public String getFechaPrestamo() {
        return fechaPrestamo;
    }

    public String getFechaDevolucion() {
        return fechaDevolucion;
    }

    public boolean isFueExplicado() {
        return fueExplicado;
    }

    public JuegoMesaPrestamo getJuego() {
        return juego;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void registrarDevolucion(String fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }

    public boolean fueDevuelto() {
        return fechaDevolucion != null;
    }
    
    public Mesa getMesa() {
		return mesa;
	}

	@Override
    public String toString() {
		String idMesa = (mesa == null) ? "null" : mesa.getIdMesa();
        return "id\t" + this.idPrestamo + "|fechaPrestamo\t" + this.fechaPrestamo + "|fechaDevolucion\t" + this.fechaDevolucion + "|explicado\t" + this.fueExplicado + "|juego\t" + this.juego.getIdJuegoPrestamo() + "|usuario\t" 
        			+ this.usuario.getLogin() + "|mesa\t" + idMesa;
    }
    
    public void devolver() throws Exception {
        if (this.fueDevuelto()) {
            throw new Exception("Ese préstamo ya fue devuelto.");
        }
        this.fechaDevolucion = LocalDateTime.now().toString();
        this.juego.setDisponible(true);
        this.juego.aumentarVecesPrestado();
    }
}
