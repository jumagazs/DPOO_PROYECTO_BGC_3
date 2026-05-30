package usuarios;

import juegos.*;
import mesas.*;
import pedidos.*;
import prestamos.*;
import productos.*;
import sugerencias.*;
import usuarios.*;
import ventas.*;

public class Cocinero extends Empleado {

	public Cocinero(String login, String contrasena) {
		super(login, contrasena);
		// TODO Auto-generated constructor stub
	}
	
	public void atenderPedidos(Pedido pedido) {
		pedido.prepararPedido();
	}
	
	@Override
	public String toString() {
	    return "login\t" + this.login + "|contrasena\t" + this.contrasena;
	}

}
