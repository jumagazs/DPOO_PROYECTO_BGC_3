package usuarios;

import java.util.Collection;

public class Cliente extends Usuario {
		private int puntosFidelidad;
		private double porcentajeDescuentoTorneo;
		
	public Cliente(String login, String contrasena) {
			super(login, contrasena);
			this.puntosFidelidad = 0;
			this.porcentajeDescuentoTorneo = 0;

		}
	
	public int getPuntosFidelidad() {
        return puntosFidelidad;
    }

    public void agregarPuntos(int puntos) {
        this.puntosFidelidad += puntos;
    }

    public void usarPuntos(int puntos) throws Exception {
        if (puntos > puntosFidelidad) {
            throw new Exception("El cliente no tiene suficientes puntos de fidelidad.");
        }
        this.puntosFidelidad -= puntos;
    }
    

	
	@Override
	public String toString() {
	    return "login\t" + this.login + "|contrasena\t" + this.contrasena + "|puntos\t" + this.puntosFidelidad + "|descuentoTorneo\t" + this.porcentajeDescuentoTorneo;
	}
	
	public double calcularDescuento(String codigoDescuento, int puntosAUsar, Collection<Usuario> usuarios) throws Exception {
	    if (codigoDescuento != null && !codigoDescuento.isEmpty()) {
	        boolean codigoValido = false;
	        for (Usuario u : usuarios) {
	            if (u instanceof Empleado && ((Empleado) u).getCodigoDescuento().equals(codigoDescuento)) {
	                codigoValido = true;
	                break;
	            }
	        }
	        if (!codigoValido) {
	            throw new Exception("El código de descuento no es válido.");
	        }
	        if (puntosAUsar > 0) {
	            throw new Exception("El descuento y los puntos no son acumulables.");
	        }
	        return 0.10;
	    } else if (puntosAUsar > 0) {
	        this.usarPuntos(puntosAUsar);
	    }
	    return 0;
	}
	    
    public void otorgarDescuentoTorneo(double porcentaje) throws Exception {
        if (this.porcentajeDescuentoTorneo > 0) {
            throw new Exception("El cliente ya tiene un descuento de torneo activo (no acumulable).");
        }
        this.porcentajeDescuentoTorneo = porcentaje;
    }
	
    public double consumirDescuentoTorneo() throws Exception {
        if (this.porcentajeDescuentoTorneo == 0) {
            throw new Exception("El cliente no tiene descuento de torneo activo.");
        }
        double pct = this.porcentajeDescuentoTorneo;
        this.porcentajeDescuentoTorneo = 0;  
        return pct;
    }

	public double getPorcentajeDescuentoTorneo() {
		return porcentajeDescuentoTorneo;
	}
    
    

    
}
