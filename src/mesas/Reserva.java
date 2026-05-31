package mesas;
import java.time.LocalDateTime;
import usuarios.Cliente;

public class Reserva {

    private String idReserva;
    private Cliente cliente;
    private Mesa mesa;
    private LocalDateTime fechaReserva;
    private int cantidadPersonas;

    public Reserva(String idReserva,
                   Cliente cliente,
                   Mesa mesa,
                   LocalDateTime fechaReserva,
                   int cantidadPersonas) {

        this.idReserva = idReserva;
        this.cliente = cliente;
        this.mesa = mesa;
        this.fechaReserva = fechaReserva;
        this.cantidadPersonas = cantidadPersonas;
    }

    public String getIdReserva() {
        return idReserva;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Mesa getMesa() {
        return mesa;
    }

    public LocalDateTime getFechaReserva() {
        return fechaReserva;
    }

    public int getCantidadPersonas() {
        return cantidadPersonas;
    }

    @Override
    public String toString() {

        return "id\t" + idReserva
                + "|cliente\t" + cliente.getLogin()
                + "|mesa\t" + mesa.getIdMesa()
                + "|fecha\t" + fechaReserva
                + "|personas\t" + cantidadPersonas;
    }
}