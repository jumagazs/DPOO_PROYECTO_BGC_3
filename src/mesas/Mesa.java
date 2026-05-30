package mesas;

import usuarios.*;

public class Mesa {
    private String idMesa;
    private int capacidad;
    private boolean ocupada;
    private int personasActuales;
    private boolean hayNinos;  
    private boolean hayJovenes;
    private int juegosPrestadosActivos;
    private boolean tieneBebidaCaliente;
    private boolean tieneJuegoAccion;
    private Cliente ocupante;

    public Mesa(String idMesa, int capacidad) {
        this.idMesa = idMesa;
        this.capacidad = capacidad;
        this.ocupada = false;
        this.personasActuales = 0;
        this.hayJovenes = false;
        this.hayNinos = false;
        this.juegosPrestadosActivos = 0;
        this.tieneBebidaCaliente = false;
        this.tieneJuegoAccion = false;
    }

    public String getIdMesa() {
        return idMesa;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public boolean isOcupada() {
        return ocupada;
    }

    public int getPersonasActuales() {
        return personasActuales;
    }

    public void asignarMesa(int cantidadPersonas,boolean hayJovenes,boolean hayNinos, Cliente cliente) throws Exception {
        if (ocupada) {
            throw new Exception("La mesa ya está ocupada.");
        }
        if (cantidadPersonas > capacidad) {
            throw new Exception("La cantidad de personas supera la capacidad de la mesa.");
        }

        this.ocupada = true;
        this.personasActuales = cantidadPersonas;
        this.hayJovenes = hayJovenes;
        this.hayNinos = hayNinos;
        this.ocupante = cliente;
    }

    public void liberarMesa() {
        this.ocupada = false;
        this.personasActuales = 0;
        this.hayJovenes = false;
        this.hayNinos = false;
        this.juegosPrestadosActivos = 0;
        this.tieneBebidaCaliente = false;
        this.tieneJuegoAccion = false;
        this.ocupante = null;
    }

	public boolean isHayNinos() {
		return hayNinos;
	}

	public void setHayNinos(boolean hayNinos) {
		this.hayNinos = hayNinos;
	}

	public boolean isHayJovenes() {
		return hayJovenes;
	}

	public void setHayJovenes(boolean hayJovenes) {
		this.hayJovenes = hayJovenes;
	}
	
	@Override
	public String toString() {
	    return "id\t" + this.idMesa + "|capacidad\t" + this.capacidad
	         + "|ocupada\t" + this.ocupada + "|personas\t" + this.personasActuales
	         + "|jovenes\t" + this.hayJovenes + "|ninos\t" + this.hayNinos
	         + "|juegosActivos\t" + this.juegosPrestadosActivos
	         + "|bebidaCaliente\t" + this.tieneBebidaCaliente
	         + "|juegoAccion\t" + this.tieneJuegoAccion
	         + "|ocupante\t" + (ocupante == null ? "null" : ocupante.getLogin());
	}

	public boolean isTieneBebidaCaliente() {
		return tieneBebidaCaliente;
	}

	public void setTieneBebidaCaliente(boolean tieneBebidaCaliente) {
		this.tieneBebidaCaliente = tieneBebidaCaliente;
	}

	public boolean isTieneJuegoAccion() {
		return tieneJuegoAccion;
	}

	public void setTieneJuegoAccion(boolean tieneJuegoAccion) {
		this.tieneJuegoAccion = tieneJuegoAccion;
	}

	public int getJuegosPrestadosActivos() {
		return juegosPrestadosActivos;
	}
    
	public void nuevoJuegoPrestado() {
		this.juegosPrestadosActivos++;
	}
	
	public void devolverJuego() {
		if(this.juegosPrestadosActivos > 0) {
			this.juegosPrestadosActivos--;
		}
	}

	public Usuario getOcupante() {
		return ocupante;
	}

	public void setOcupante(Cliente ocupante) {
		this.ocupante = ocupante;
	}
	
	
    
}
