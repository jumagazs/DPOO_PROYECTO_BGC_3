package usuarios;

import java.util.ArrayList;
import java.util.List;

import juegos.*;
import mesas.*;
import pedidos.*;
import prestamos.*;
import productos.*;
import sugerencias.*;
import usuarios.*;
import ventas.*;

public class Mesero extends Empleado {
	
	private List<String> juegosDificiles;
	

	public Mesero(String login, String contrasena) {
		super(login, contrasena);
		this.juegosDificiles = new ArrayList<>();
	}
	
	
    //RF 18 


    public Pedido registrarPedido(Mesa mesa, String idPedido, String fecha) throws Exception {
        if (!mesa.isOcupada())
            throw new Exception("La mesa debe estar ocupada.");
        return new Pedido(idPedido, fecha, mesa);
    	
    }
    
    public void agregarJuegoDificil(String idJuego) {
        if (idJuego != null && !idJuego.isEmpty() && !this.juegosDificiles.contains(idJuego)) {
            this.juegosDificiles.add(idJuego);
        }
    }
    
    public boolean puedeExplicar(String idJuego) {
    		return this.juegosDificiles.contains(idJuego);
    }
    
    public Prestamo realizarPrestamo(JuegoMesaPrestamo juego, Mesa mesa, String idPrestamo, String fecha, boolean fueExplicado,Cliente cliente) {
        return new Prestamo(idPrestamo, fecha, fueExplicado, juego, cliente,mesa);
    }
    

    
    @Override
    public String toString() {
        if (this.juegosDificiles.isEmpty()) {
            return "login\t" + this.getLogin() + "|contrasena\t" + this.getContrasena();
        }
        return "login\t" + this.getLogin() + "|contrasena\t" + this.getContrasena() + "|conocidos\t" + String.join(",", this.juegosDificiles);
    }
}
