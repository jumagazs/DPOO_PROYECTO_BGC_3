package sugerencias;

import java.time.LocalDateTime;
import java.util.List;

import usuarios.Empleado;

public class SugerenciaPlato {

    private String idSugerencia;
    private String nombrePropuesto;
    private LocalDateTime fechaSugerencia;
    private String estadoSugerencia;
    private Empleado empleado;
    private double precio;
    private boolean esAlcoholica;
    private boolean esCaliente;
    private List<String> alergenos;
    private String tipo;


    public SugerenciaPlato(String idSugerencia, String nombrePropuesto, LocalDateTime fechaSugerencia,
			String estadoSugerencia, Empleado empleado, double precio, boolean esAlcoholica,
			boolean esCaliente, List<String> alergenos, String tipo) {
		this.idSugerencia = idSugerencia;
		this.nombrePropuesto = nombrePropuesto;
		this.fechaSugerencia = fechaSugerencia;
		this.estadoSugerencia = estadoSugerencia;
		this.empleado = empleado;
		this.precio = precio;
		this.esAlcoholica = esAlcoholica;
		this.esCaliente = esCaliente;
		this.alergenos = alergenos;
		this.tipo = tipo;
	}

	public String getIdSugerencia() {
        return idSugerencia;
    }

    public String getNombrePropuesto() {
        return nombrePropuesto;
    }

    public LocalDateTime getFechaSugerencia() {
        return fechaSugerencia;
    }

    public String getEstadoSugerencia() {
        return estadoSugerencia;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

	public void setIdSugerencia(String idSugerencia) {
		this.idSugerencia = idSugerencia;
	}

	public void setNombrePropuesto(String nombrePropuesto) {
		this.nombrePropuesto = nombrePropuesto;
	}

	public void setFechaSugerencia(LocalDateTime fechaSugerencia) {
		this.fechaSugerencia = fechaSugerencia;
	}

	public void setEstadoSugerencia(String estadoSugerencia) {
		this.estadoSugerencia = estadoSugerencia;
	}

	public void setEmpleado(Empleado empleado) {
		this.empleado = empleado;
	}

	public double getPrecio() {
		return precio;
	}

	public boolean isEsAlcoholica() {
		return esAlcoholica;
	}

	public boolean isEsCaliente() {
		return esCaliente;
	}

	public List<String> getAlergenos() {
		return alergenos;
	}

	public String getTipo() {
		return tipo;
	}
	
	@Override
	public String toString() {
	    String alerg = (alergenos == null) ? "" : String.join(",", alergenos);
	    return "id\t" + this.idSugerencia
	         + "|nombre\t" + this.nombrePropuesto
	         + "|fecha\t" + this.fechaSugerencia.toString()
	         + "|estado\t" + this.estadoSugerencia
	         + "|empleado\t" + this.empleado.getLogin()
	         + "|precio\t" + this.precio
	         + "|alcoholica\t" + this.esAlcoholica
	         + "|caliente\t" + this.esCaliente
	         + "|alergenos\t" + alerg
	         + "|tipo\t" + this.tipo;
	}
	

    
    
}