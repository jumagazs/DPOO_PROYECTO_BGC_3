package usuarios;

import java.util.ArrayList;
import java.util.List;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import sugerencias.*;

public abstract class Empleado extends Usuario {

    private List<Turno> turnos;
    private List<SolicitudCambioTurno> solicitudes;
	private static final double DESCUENTO_EMPLEADO = 0.20;

	private String codigoDescuento;

    public Empleado(String login, String contrasena) {
        super(login, contrasena);
        this.solicitudes = new ArrayList<>();
        this.codigoDescuento= "EMP-" + login.hashCode();
        this.turnos = new ArrayList<>();
    }

   // Requeriminto 12 //
    public List<Turno> consultarTurnos() throws Exception {
        if (this.turnos.isEmpty()) {
            throw new Exception("El empleado no tiene turnos asignados.");
        }
        return this.turnos;
    }

    public void asignarTurno(Turno turno) {
        this.turnos.add(turno);
    }
	//requermiento 13//
    public SolicitudCambioTurno solicitarCambioTurnoGeneral(String idSolicitud, String idTurno) throws Exception {
        Turno turno = this.getTurno(idTurno);
        SolicitudCambioTurno solicitud = new SolicitudCambioTurno(idSolicitud, "GENERAL", LocalDateTime.now(), this, null, turno, null);
        this.solicitudes.add(solicitud);
        return solicitud;
    }
    
    public SolicitudCambioTurno solicitarIntercambioTurno(String idSolicitud, String idTurno, Empleado destino, String idTurnoDestino) throws Exception {
        Turno turno = this.getTurno(idTurno);
        Turno turnoDestino = destino.getTurno(idTurnoDestino);
        SolicitudCambioTurno solicitud = new SolicitudCambioTurno(idSolicitud, "INTERCAMBIO", LocalDateTime.now(), this, destino, turno, turnoDestino);
        this.solicitudes.add(solicitud);
        return solicitud;
    }

    public List<SolicitudCambioTurno> getSolicitudes() {
        return solicitudes;
    }

	public String getCodigoDescuento() {
		return codigoDescuento;
	}
    
	public double getDescuentoEMP() { 
		return DESCUENTO_EMPLEADO;
	}


	public Turno getTurno(String idTurno) throws Exception {
        for (Turno t : this.turnos) {
            if (t.getIdTurno().equals(idTurno)) {
                return t;
            }
        }
        throw new Exception("El turno no existe.");
    }

	
	public SugerenciaPlato sugerirPlato(String idSugerencia, String nombrePropuesto, double precio, boolean esAlcoholica, boolean esCaliente, List<String> alergenos, String tipo) throws Exception {
	    if (nombrePropuesto == null || nombrePropuesto.trim().isEmpty()) {
	        throw new Exception("El nombre del plato no puede estar vacío.");
	    }
	    SugerenciaPlato sugerencia = new SugerenciaPlato(idSugerencia, nombrePropuesto, LocalDateTime.now(), "PENDIENTE", this, precio, esAlcoholica, esCaliente, alergenos, tipo);
	    return sugerencia;
	}
	
	public boolean estaEnTurno() {
	    LocalDateTime ahora = LocalDateTime.now();
	    for (Turno t : this.turnos) {
	        if (!ahora.isBefore(t.getHoraInicio()) && !ahora.isAfter(t.getHoraFin())) {
	            return true;
	        }
	    }
	    return false;
	}
	
    public void eliminarTurno(String idTurno) throws Exception {
        Turno encontrado = null;
        for (Turno t : this.turnos) {
            if (t.getIdTurno().equals(idTurno)) {
                encontrado = t;
                break;
            }
        }
        if (encontrado == null) {
            throw new Exception("El turno no existe.");
        }
        this.turnos.remove(encontrado);
    }
    
    public boolean tieneTurnos() {
        return !this.turnos.isEmpty();
    }

	public List<Turno> getTurnos() {
		return turnos;
	}

	public void setSolicitudes(List<SolicitudCambioTurno> solicitudes) {
		this.solicitudes = solicitudes;
	}
    
	public double calcularDescuento() {
	    return this.DESCUENTO_EMPLEADO;
	}
	
	public boolean tieneTurnoElDia(DayOfWeek dia) {
	    for (Turno t : this.turnos) {
	        if (t.getHoraInicio().getDayOfWeek().equals(dia)) {
	        		return true;
	        }
	    }
	    return false;
	}
	
	
	
}