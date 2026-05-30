package torneos;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

import juegos.JuegoMesa;
import juegos.JuegoMesaPrestamo;
import usuarios.Cliente;
import usuarios.Usuario;

public class TorneoAmistoso extends Torneo {
	
	private double premioDescuento;

	public TorneoAmistoso(String id,int cupos, JuegoMesaPrestamo juego, DayOfWeek dia, double premioDescuento) {
		super(id,cupos, juego, dia);
		this.premioDescuento = premioDescuento;
	}
	
	public void otorgarPremio(Cliente ganador) throws Exception {
	    ganador.otorgarDescuentoTorneo(this.premioDescuento);
	}
	
	public double getPremioDescuento() {
	    return premioDescuento;
	}




}
