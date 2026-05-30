package usuarios;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import juegos.*;
import mesas.*;
import modelo.Informe;
import pedidos.*;
import prestamos.*;
import productos.*;
import sugerencias.*;
import torneos.*;
import usuarios.*;
import ventas.*;


public class Administrador extends Usuario {


	public Administrador(String login, String contrasena) {
		super(login, contrasena);
	}
	
	public JuegoMesaPrestamo agregarJuegoPrestamo(String nombre, int anioPublicacion, String editorJuego, String categoria, int minJugadores, int maxJugadores, boolean esDificil, boolean jueganMenores5, boolean jueganMenores18,  String idJuegoPrestamo, boolean disponible, int vecesPrestado, String estado) {
		JuegoMesaPrestamo juegoMesaPrestamo = new JuegoMesaPrestamo( nombre, anioPublicacion, editorJuego, categoria, minJugadores, maxJugadores,  esDificil, jueganMenores5,  jueganMenores18,   idJuegoPrestamo,  disponible,  vecesPrestado,  estado);
		return juegoMesaPrestamo;
	}
	
	public JuegoMesaVenta agregarJuegoVenta(String nombre, int anioPublicacion, String editorJuego, String categoria, int minJugadores, int maxJugadores, boolean esDificil, boolean jueganMenores5, boolean jueganMenores18 , String idJuegoVenta, double precioVenta, int cantidadStock, double costoBase) throws Exception{
		JuegoMesaVenta juegoMesaVenta = new JuegoMesaVenta( nombre, anioPublicacion, editorJuego, categoria, minJugadores, maxJugadores, esDificil, jueganMenores5, jueganMenores18, idJuegoVenta,precioVenta,cantidadStock, costoBase);
		return juegoMesaVenta;
	}
	
	public Mesero registrarMesero(String login,String contraseña) {
		Mesero mesero = new Mesero(login,contraseña);
		return mesero;
	}
	
	public Cocinero registrarCocinero(String login,String contraseña) {
		Cocinero cocinero = new Cocinero(login,contraseña);
		return cocinero;
	}
	
	public Bebida registrarBebida(String idProducto, String nombre, double precio, boolean disponible, boolean alcoholica, boolean caliente) {
		Bebida bebida = new Bebida( idProducto,  nombre,  precio,  disponible,  alcoholica,  caliente);
		return bebida;
	}
	
	public Pasteleria registrarPasteleria(String idProducto, String nombre, double precio, boolean disponible, List<String> alergenos) {
		Pasteleria pasteleria = new Pasteleria( idProducto,  nombre,  precio,  disponible,  alergenos);
		return pasteleria;
	}
	
	public void asignarTurno(Empleado empleado, Turno turno) {
	    empleado.asignarTurno(turno);
	}
	
	public void aprobarCambioTurno(SolicitudCambioTurno solicitud, int meseros, int cocineros, int consecutivo) throws Exception {
	    if (solicitud.getEmpleadoDestino() != null) {
	        Turno turnoSolicitante = solicitud.getTurnoSolicitante();
	        Turno turnoDestino = solicitud.getTurnoDestino();
	        turnoDestino.setEmpleado(solicitud.getSolicitante());
	        turnoSolicitante.setEmpleado(solicitud.getEmpleadoDestino());
	        solicitud.getSolicitante().eliminarTurno(turnoSolicitante.getIdTurno());
	        solicitud.getSolicitante().asignarTurno(turnoDestino);
	        solicitud.getEmpleadoDestino().eliminarTurno(turnoDestino.getIdTurno());
	        solicitud.getEmpleadoDestino().asignarTurno(turnoSolicitante);
	    } else {
	        if (solicitud.getSolicitante() instanceof Mesero && meseros < 3) {
	            throw new Exception("No se puede aprobar, quedarían menos de 2 meseros.");
	        }
	        if (solicitud.getSolicitante() instanceof Cocinero && cocineros < 2) {
	            throw new Exception("No se puede aprobar, quedaría menos de 1 cocinero.");
	        }
	        Turno turnoActual = solicitud.getTurnoSolicitante();
	        String nuevoId = "T" + consecutivo;
	        Turno nuevoTurno = new Turno(nuevoId, turnoActual.getHoraInicio().plusMonths(1), turnoActual.getHoraFin().plusMonths(1), turnoActual.getDia(), solicitud.getSolicitante());
	        solicitud.getSolicitante().eliminarTurno(turnoActual.getIdTurno());
	        solicitud.getSolicitante().asignarTurno(nuevoTurno);
	    }
	    solicitud.aprobar();
	}

	        


	public void rechazarCambioTurno(SolicitudCambioTurno solicitud) {
	    solicitud.rechazar();
	}
	

	public ProductoMenu  aprobarSugerenciaPlato(SugerenciaPlato sugerenciaPlato,String idSugerencia) {
		sugerenciaPlato.setEstadoSugerencia("APROBADA");
	    if (sugerenciaPlato.getTipo().equals("BEBIDA"))
	        return new Bebida(idSugerencia, sugerenciaPlato.getNombrePropuesto(), sugerenciaPlato.getPrecio(), 
	                true, sugerenciaPlato.isEsAlcoholica(), sugerenciaPlato.isEsCaliente());
	    else
	        return new Pasteleria(idSugerencia, sugerenciaPlato.getNombrePropuesto(), sugerenciaPlato.getPrecio(), 
	                true, sugerenciaPlato.getAlergenos());
	}
	
	public void rechazarSugerenciaPlato(SugerenciaPlato sugerencia) {
	    sugerencia.setEstadoSugerencia("RECHAZADA");
	}
	
	public void cambiarEstadoJuego(JuegoMesaPrestamo juego, String nuevoEstado) {
	    juego.setEstado(nuevoEstado);
	}

	public void eliminarJuegoPrestamo(Map<String, JuegoMesaPrestamo> juegosPrestamo, String idJuego) throws Exception {
	    if (!juegosPrestamo.containsKey(idJuego)) {
	        throw new Exception("El juego no existe en el inventario de préstamo.");
	    }
	    juegosPrestamo.remove(idJuego);
	}

	public void eliminarJuegoVenta(Map<String, JuegoMesaVenta> juegosVenta, String idJuego) throws Exception {
	    if (!juegosVenta.containsKey(idJuego)) {
	        throw new Exception("El juego no existe en el inventario de venta.");
	    }
	    juegosVenta.remove(idJuego);
	}
	
	public Informe consultarInforme(List<VentaJuego> ventas, List<Pedido> pedidos, String granularidad) {
	    LocalDateTime ahora = LocalDateTime.now();
	    Informe inf = new Informe();
	    inf.granularidad = granularidad;
	    
	    java.time.format.DateTimeFormatter formatterFecha = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd");
	    
	    for (VentaJuego v : ventas) {
	    		LocalDateTime fecha = parsearFecha(v.getFecha());
	        if (fecha != null && dentroDeGranularidad(fecha, ahora, granularidad)) {
	            inf.totalJuegos += v.getTotal();
	            inf.impuestosJuegos += v.getImpuesto();
	        }
	    }
	    for (Pedido p : pedidos) {
	    		LocalDateTime fecha = parsearFecha(p.getFecha());
	        if (fecha != null && dentroDeGranularidad(fecha, ahora, granularidad)) {
	            inf.totalComida += p.getTotal();
	            inf.impuestosComida += p.getImpuestoConsumo();
	            inf.propinasComida += p.getPropina();
	        }
	    }
	    return inf;
	}
	
	private LocalDateTime parsearFecha(String fecha) {
	    try {
	        return LocalDateTime.parse(fecha);
	    } catch (Exception e) {
	        try {
	            return java.time.LocalDate.parse(fecha)
	                .atStartOfDay();
	        } catch (Exception e2) {
	            return null;
	        }
	    }
	}

	private boolean dentroDeGranularidad(LocalDateTime fecha, LocalDateTime ahora, String granularidad) {
	    if (granularidad.equals("diaria")) {
	        return fecha.toLocalDate().equals(ahora.toLocalDate());
	    } else if (granularidad.equals("semanal")) {
	        return fecha.isAfter(ahora.minusDays(7));
	    } else if (granularidad.equals("mensual")) {
	        return fecha.getMonth() == ahora.getMonth() && fecha.getYear() == ahora.getYear();
	    }
	    return false;
	}
	
	public void agregarJuegoDificil(Mesero mesero, String idJuego) {
	    mesero.agregarJuegoDificil(idJuego);
	}
	
	public JuegoMesaPrestamo moverVentaAPrestamo(JuegoMesaVenta juegoVenta,String idJuegoPrestamo) throws Exception {
	    if (juegoVenta.getCantidadStock() <= 0) {
	        throw new Exception("No hay stock de ese juego para mover a préstamo.");
	    }
	    juegoVenta.reducirStock(1);
	    return new JuegoMesaPrestamo(
	        juegoVenta.getNombre(),
	        juegoVenta.getAnioPublicacion(), 
	        juegoVenta.getEditorJuego(), 
	        juegoVenta.getCategoria(),
	        juegoVenta.getMinJugadores(),
	        juegoVenta.getMaxJugadores(),
	        juegoVenta.isEsDificil(),
	        juegoVenta.isJueganMenores5(),
	        juegoVenta.isJueganMenores18(),
	        idJuegoPrestamo,
	        true,
	        0,
	        "Nuevo");
	}
	
	
	public void repararJuego(JuegoMesaPrestamo juegoDanado, JuegoMesaVenta juegoFuente) throws Exception {
	    if (juegoFuente.getCantidadStock() <= 0) {
	        throw new Exception("No hay stock de ese juego para reparar.");
	    }
	    if (!juegoDanado.getNombre().equals(juegoFuente.getNombre())) {
	        throw new Exception("No se puede reparar con un juego distinto.");
	    }
	    juegoFuente.reducirStock(1);
	    juegoDanado.setEstado("Nuevo");
	    juegoDanado.setDisponible(true);
	}
	
	
	public void marcarJuegoRobado(JuegoMesaPrestamo juego) {
	    juego.setEstado("Desaparecido");
	    juego.setDisponible(false);
	}
	
	public void reabastecerJuegoVenta(JuegoMesaVenta juego, int cantidad) throws Exception {
	    if (cantidad <= 0) throw new Exception("Cantidad debe ser positiva.");
	    juego.aumentarStock(cantidad);
	}
	
	public Torneo crearTorneoAmistoso(String id, int cupos, JuegoMesaPrestamo juego, DayOfWeek dia, double descuento) {
		return new TorneoAmistoso(id,  cupos,  juego, dia,descuento);
	}
	
	public Torneo crearTorneoCompetitivo(String id, int cupos, JuegoMesaPrestamo juego, 
            DayOfWeek dia, double tarifa) {
	return new TorneoCompetitivo(id, cupos, juego, dia, tarifa);
	}
	
	public void otorgarPremioAmistoso(TorneoAmistoso torneo, Cliente ganador) throws Exception {
	torneo.otorgarPremio(ganador);
	}
	
	@Override
	public String toString() {
	    return "login\t" + this.login + "|contrasena\t" + this.contrasena;
	}
	
	
	
	
}
