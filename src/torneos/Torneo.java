package torneos;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



import juegos.*;
import mesas.*;
import pedidos.*;
import prestamos.*;
import productos.*;
import sugerencias.*;
import usuarios.*;
import ventas.*;

public abstract class Torneo {
	
	protected String id;
	protected int capacidad;
	protected int cuposFanaticos;
	protected int cuposRegulares;
	protected JuegoMesaPrestamo juego;
	protected DayOfWeek dia;
	
	protected Map<String,Integer> inscritosRegulares;
	protected Map<String,Integer> inscritosFanaticos;
	
	protected List<String> empleadosInscritos;
	public Torneo(String id,int cupos, JuegoMesaPrestamo juego, DayOfWeek dia) {
		super();
		this.id = id;
		this.capacidad = cupos;
		this.cuposFanaticos = (int) Math.ceil(cupos*0.2);
		this.cuposRegulares = cupos - cuposFanaticos;
		this.juego = juego;
		this.dia = dia;
		this.inscritosFanaticos = new HashMap<String, Integer>();
		this.inscritosRegulares = new HashMap<String, Integer>();
		this.empleadosInscritos = new ArrayList<String>();
		
	}
	
	public void inscribirUsuario(Usuario usuario, int cupos) throws Exception {
		
		if (cupos < 1 || cupos > 3) {
		    throw new Exception("Se permiten entre 1 y 3 cupos por usuario.");
		}
		
		if(this.inscritosFanaticos.get(usuario.getLogin()) != null || this.inscritosRegulares.get(usuario.getLogin()) != null) {
			throw new Exception("El usuario ya está inscrito");
		}
		
		boolean inscrito = false;
		
		if (esFanatico(usuario) && cupos <= cuposFanaticos) {
		    cuposFanaticos -= cupos;
		    inscritosFanaticos.put(usuario.getLogin(), cupos);
		    inscrito = true;
		}
		
		if(!inscrito) {
			if(cupos > this.cuposRegulares) {
				throw new Exception("No hay cupos suficientes");
			} else {
				this.cuposRegulares -= cupos;
				this.inscritosRegulares.put(usuario.getLogin(), cupos);
			}
		}
		
		if(usuario instanceof Empleado) {
			this.empleadosInscritos.add(usuario.getLogin());
		}
		
	}
	
	private boolean esFanatico(Usuario usuario) {
	    for (JuegoMesa jm : usuario.getJuegosFavoritos()) {
	        if (jm.getNombre().equals(this.juego.getNombre())) return true;
	    }
	    return false;
	}
	
	public void eliminarUsuario(Usuario usuario) throws Exception {
		String login = usuario.getLogin();
		if(inscritosRegulares.containsKey(login)) {
			this.cuposRegulares += this.inscritosRegulares.get(login);
			this.inscritosRegulares.remove(login);
			
		} else if (inscritosFanaticos.containsKey(login) ) {
			this.cuposFanaticos += this.inscritosFanaticos.get(login);
			this.inscritosFanaticos.remove(login);

		} else {
			throw new Exception("El usuario no está inscrito");
		}
		
		 empleadosInscritos.remove(login);
	}

	public int getCuposFanaticos() {
		return cuposFanaticos;
	}

	public int getCuposRegulares() {
		return cuposRegulares;
	}

	public JuegoMesaPrestamo getJuego() {
		return juego;
	}

	public DayOfWeek getDia() {
		return dia;
	}
	
	

	public int getCapacidad() {
		return capacidad;
	}

	public Map<String, Integer> getInscritosRegulares() {
		return inscritosRegulares;
	}

	public Map<String, Integer> getInscritosFanaticos() {
		return inscritosFanaticos;
	}

	public String getId() {
		return id;
	}
	
	public List<String> getEmpleadosInscritos() {
	    return empleadosInscritos;
	}

	public void agregarInscritoRegular(String login, int cupos) {
	    inscritosRegulares.put(login, cupos);
	}

	public void agregarInscritoFanatico(String login, int cupos) {
	    inscritosFanaticos.put(login, cupos);
	}

	public void agregarEmpleadoInscrito(String login) {
	    empleadosInscritos.add(login);
	}

	public void setCuposFanaticos(int cuposFanaticos) {
	    this.cuposFanaticos = cuposFanaticos;
	}

	public void setCuposRegulares(int cuposRegulares) {
	    this.cuposRegulares = cuposRegulares;
	}
	

}
