package usuarios;

import java.time.LocalDateTime;

public class Turno {

    private String idTurno;
    private LocalDateTime horaInicio;
    private LocalDateTime horaFin;
    private String dia;
    private Empleado empleado;

    public Turno(String idTurno, LocalDateTime horaInicio, LocalDateTime horaFin, String dia, Empleado empleado) {
        this.idTurno = idTurno;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.dia = dia;
        this.empleado = empleado;
    }

    public LocalDateTime getHoraInicio() {
        return horaInicio;
    }

    public LocalDateTime getHoraFin() {
        return horaFin;
    }

    public String getDia() {
        return dia;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

	public String getIdTurno() {
		return idTurno;
	}
    
    @Override
    public String toString() {
        return "idTurno\t" + this.idTurno + "|inicio\t" + this.horaInicio + "|fin\t" + this.horaFin + "|dia\t" + this.dia + "|empleado\t" + this.empleado.getLogin();
    }
    
    
}