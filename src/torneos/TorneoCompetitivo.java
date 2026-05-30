package torneos;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.Map;

import juegos.JuegoMesa;
import juegos.JuegoMesaPrestamo;
import usuarios.Usuario;

public class TorneoCompetitivo extends Torneo {
	

	private double tarifa;
	
	public TorneoCompetitivo(String id, int cupos, JuegoMesaPrestamo juego, DayOfWeek dia, double tarifa) {
		super(id,cupos, juego, dia);
		this.tarifa = tarifa;
	}
	
	public double getPremio() {
		double total = 0;
		for (Map.Entry<String, Integer> e : inscritosRegulares.entrySet()) {
			String login = e.getKey();
			if(!this.empleadosInscritos.contains(login)) {
				total += e.getValue()*tarifa;
			}
		}
		for (Map.Entry<String, Integer> e : inscritosFanaticos.entrySet()) {
			String login = e.getKey();
			if(!this.empleadosInscritos.contains(login)) {
				total += e.getValue()*tarifa;
			}
		}
		return total;
	}
	
	public double getTarifa() {
	    return tarifa;
	}
	
	

}
