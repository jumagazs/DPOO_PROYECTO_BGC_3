package modelo;

import java.util.HashMap;

import java.util.Map;
import juegos.*;
import mesas.*;
import pedidos.*;
import prestamos.*;
import productos.*;
import sugerencias.*;
import usuarios.*;
import ventas.*;
import torneos.*;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;
import juegos.JuegoMesaVenta;

import java.time.DayOfWeek;
import java.time.LocalDateTime;


public class Cafe {
    private Map<String, Usuario> usuarios;
    private Map<String, JuegoMesaPrestamo> juegosPrestamo;
    private Map<String, ProductoMenu> menu;
    private Map<String, Mesa> mesas;
    private List<Prestamo> prestamos;
    private int consecutivoPrestamos;
    private int consecutivoJuegosPrestamos;
    private List<Pedido> pedidos;
    private int consecutivoPedidos;
    private Map<String, JuegoMesaVenta> juegosVenta;
    private List<VentaJuego> ventas;
    private int consecutivoVentas;
    private int consecutivoJuegosVentas;
    private List<SolicitudCambioTurno> solicitudesCambioTurno;
    private int consecutivoSolicitudes;
    private List<SugerenciaPlato> sugerencias;
    private int consecutivoSugerencias;
    private int consecutivoProductos;
    private int consecutivoTurnos;
    private int capacidadMaximaCafe;
    private List<Torneo> torneos;
    private int consecutivoTorneos;

    

    public Cafe() {
        this.usuarios = new HashMap<>();
        this.juegosPrestamo = new HashMap<>();
        this.menu = new HashMap<>();
        this.mesas = new HashMap<>();
        this.prestamos = new ArrayList<>();
        this.consecutivoPrestamos = 1;
        this.pedidos = new ArrayList<>();
        this.consecutivoPedidos = 1;
        this.juegosVenta = new HashMap<>();
        this.ventas = new ArrayList<>();
        this.consecutivoVentas = 1;
        this.solicitudesCambioTurno = new ArrayList<>();
        this.consecutivoSolicitudes = 1;
        this.sugerencias = new ArrayList<>();
        this.consecutivoSugerencias = 1;
        this.consecutivoTurnos = 1;
        this.consecutivoJuegosPrestamos = 1;
        this.consecutivoJuegosVentas = 1;
        this.consecutivoProductos = 1;
        this.capacidadMaximaCafe = 40;
        this.torneos = new ArrayList<>();
        this.consecutivoTorneos = 1;
    }

    public Cliente registrarCliente(String login, String contrasena) throws Exception {
        if (usuarios.containsKey(login)) {
            throw new Exception("Ya existe un usuario registrado con ese login.");
        }
        if (login.contains(":") || login.contains(",") || login.contains("|") || login.contains("\t")) {
            throw new Exception("El login contiene caracteres reservados.");
        }

        Cliente nuevoCliente = new Cliente(login, contrasena);
        usuarios.put(login, nuevoCliente);
        return nuevoCliente;
    }

    public Usuario iniciarSesion(String login, String contrasena) throws Exception {
        if (!usuarios.containsKey(login)) {
            throw new Exception("El usuario no existe.");
        }

        Usuario usuario = usuarios.get(login);

        if (!usuario.getContrasena().equals(contrasena)) {
            throw new Exception("La contraseña es incorrecta.");
        }

        return usuario;
    }
    
    public void agregarJuegoPrestamo(JuegoMesaPrestamo juego) {
        juegosPrestamo.put(juego.getIdJuegoPrestamo(), juego);
    }

    public Collection<JuegoMesaPrestamo> consultarCatalogoJuegosPrestamo() {
        return juegosPrestamo.values();
    }

    public Map<String, Usuario> getUsuarios() {
        return usuarios;
    }
    
    public void agregarProductoMenu(ProductoMenu producto) {
        menu.put(producto.getIdProducto(), producto);
    }

    public Collection<ProductoMenu> consultarMenu() {
        return menu.values();
    }
    
    public void agregarMesa(Mesa mesa) {
        mesas.put(mesa.getIdMesa(), mesa);
    }

    public Collection<Mesa> getMesas() {
        return mesas.values();
    }

    public Mesa asignarMesaACliente(Cliente cliente, int cantidadPersonas, boolean hayJovenes, boolean hayNinos) throws Exception {
        int personasActuales = 0;
        for (Mesa m : mesas.values()) {
            if (m.isOcupada()) personasActuales += m.getPersonasActuales();
        }
        if (personasActuales + cantidadPersonas > capacidadMaximaCafe) {
            throw new Exception("El café está lleno. Capacidad máxima alcanzada.");
        }
    	
    		Mesa mejorMesa = null;

        for (Mesa mesa : mesas.values()) {
            if (!mesa.isOcupada() && mesa.getCapacidad() >= cantidadPersonas) {
                if (mejorMesa == null || mesa.getCapacidad() < mejorMesa.getCapacidad()) {
                    mejorMesa = mesa;
                }
            }
        }

        if (mejorMesa == null) {
            throw new Exception("No hay mesas disponibles para esa cantidad de personas.");
        }
        mejorMesa.asignarMesa(cantidadPersonas,hayJovenes,hayNinos,cliente);
        return mejorMesa;
    }
    
    public void liberarMesa(String login) throws Exception {
        Cliente cliente = validarCliente(login);
        
        Mesa mesaDelCliente = null;
        for (Mesa mesa : mesas.values()) {
            if (mesa.isOcupada() && mesa.getOcupante() != null 
                    && mesa.getOcupante().getLogin().equals(login)) {
                mesaDelCliente = mesa;
                break;
            }
        }
        
        if (mesaDelCliente == null) {
            throw new Exception("El cliente no tiene una mesa asignada.");
        }
        
        if (mesaDelCliente.getJuegosPrestadosActivos() > 0) {
            throw new Exception("Debe devolver todos los juegos antes de liberar la mesa.");
        }
        
        mesaDelCliente.liberarMesa();
    }
    

    
    public List<Prestamo> getPrestamos() {
        return prestamos;
    }
    
    public void devolverJuego(String idPrestamo) throws Exception {
        Prestamo prestamoEncontrado = null;
        for (Prestamo prestamo : prestamos) {
            if (prestamo.getIdPrestamo().equals(idPrestamo)) {
                prestamoEncontrado = prestamo;
                break;
            }
        }
        if (prestamoEncontrado == null) {
            throw new Exception("No existe un préstamo con ese id.");
        }
        prestamoEncontrado.devolver();
        Mesa m = prestamoEncontrado.getMesa();
        if (m != null) {
            m.devolverJuego();
            if (prestamoEncontrado.getJuego().getCategoria().equalsIgnoreCase("Accion")) {
                boolean aunHayAccion = false;
                for (Prestamo p : prestamos) {
                    if (!p.fueDevuelto() && p.getMesa() != null
                            && p.getMesa().getIdMesa().equals(m.getIdMesa())
                            && p.getJuego().getCategoria().equalsIgnoreCase("Accion")) {
                        aunHayAccion = true;
                        break;
                    }
                }
                if (!aunHayAccion) {
                	m.setTieneJuegoAccion(false);
                }
            }
        }
    }
    
    
    public void agregarProductoAPedido(Pedido pedido, String idProducto, int cantidad) throws Exception {
        if (pedido == null) {
            throw new Exception("El pedido no existe.");
        }

        if (!menu.containsKey(idProducto)) {
            throw new Exception("El producto no existe en el menú.");
        }

        if (cantidad <= 0) {
            throw new Exception("La cantidad debe ser mayor que cero.");
        }
        
        ProductoMenu producto = menu.get(idProducto);

        if (!producto.isDisponible()) {
            throw new Exception("El producto no está disponible.");
        }
        
        Mesa mesa = pedido.getMesa();
        
        /// Restricción alcohol y calientes
        if (producto instanceof Bebida) {
            Bebida b = (Bebida) producto;
            if (b.isAlcoholica() && (mesa.isHayJovenes() || mesa.isHayNinos())) {
                throw new Exception("No se puede servir alcohol en una mesa con menores.");
            }
            if (b.isCaliente() && mesa.isTieneJuegoAccion())
                throw new Exception("No se puede servir bebida caliente en mesa con juego de Acción.");
            if (b.isCaliente()) mesa.setTieneBebidaCaliente(true);
        }
        
        //Restricción alérgenos
        if (producto instanceof Pasteleria) {
            Pasteleria p = (Pasteleria) producto;
            if (p.getAlergenos() != null && !p.getAlergenos().isEmpty()) {
                System.out.println("Aviso: el producto " + p.getNombre()
                    + " contiene alérgenos: " + String.join(", ", p.getAlergenos()));
            }
        }
        

        DetallePedido detalle = new DetallePedido(cantidad, producto);
        pedido.agregarDetalle(detalle);
    }
    
    public void confirmarPedido(Pedido pedido) throws Exception {
        if (pedido == null) {
            throw new Exception("El pedido no existe.");
        }
        pedido.confirmar();
    }
    
    public void agregarJuegoVenta(JuegoMesaVenta juego) {
        juegosVenta.put(juego.getIdJuegoVenta(), juego);
    }
    
    
    
    //requermiento 11 //
    public void agregarJuegoFavoritoAUsuario(String login, String idJuego) throws Exception{
    		Usuario usuario = validarUsuario(login);
        JuegoMesaPrestamo juego = validarJuegoPrestamo(idJuego);
        usuario.agregarJuegoFavorito(juegosPrestamo.get(idJuego));
    }
    public List<JuegoMesa> consultarFavoritos(String login) throws Exception {
    		return validarUsuario(login).getJuegosFavoritos();
    }

    //requerimiento 12 //
    public List<Turno> consultarTurnoEmpleado(String login) throws Exception {

    		Empleado empleado = validarEmpleado(login);
        return empleado.consultarTurnos();
    }

    //requermiento 13 //
    
    public SolicitudCambioTurno solicitarCambioTurnoGeneral(String login,String idTurno) throws Exception {
        Empleado emp =  validarEmpleado(login);
        String id = "SC" + consecutivoSolicitudes++;
        SolicitudCambioTurno sc = emp.solicitarCambioTurnoGeneral(id, idTurno);
        solicitudesCambioTurno.add(sc);
        return sc;
    }
    
    public SolicitudCambioTurno solicitarIntercambioTurno(String login, String idTurno, String loginDestino, String idTurnoDestino) throws Exception {

        Empleado emp = validarEmpleado(login);
        Empleado destino = validarEmpleado(loginDestino);
        String id = "SC" + consecutivoSolicitudes++;
        SolicitudCambioTurno sc = emp.solicitarIntercambioTurno(id, idTurno, destino, idTurnoDestino);
        solicitudesCambioTurno.add(sc);
        return sc;
    }

    //metodos requerimiento 14 //
    public VentaJuego comprarJuegoConDescuento(String login, String idJuego, int cantidad, int puntosAUsar, String codigoDescuento) throws Exception {

        Usuario usuario = validarUsuario(login);

        JuegoMesaVenta juego = validarJuegoVenta(idJuego);

        juego.reducirStock(cantidad);

        String fechaActual = LocalDateTime.now().toString();

        String idVenta = "V" + consecutivoVentas++;
        VentaJuego venta = new VentaJuego(idVenta, fechaActual, usuario);

        DetalleVenta detalle = new DetalleVenta(cantidad, juego);
        venta.agregarDetalle(detalle);

        double descuento = 0;


        if (usuario instanceof Cliente) {
            descuento = ((Cliente) usuario).calcularDescuento(codigoDescuento, puntosAUsar, this.usuarios.values());
        } else if (usuario instanceof Empleado) {
            if (puntosAUsar > 0) {
                throw new Exception("Los empleados no pueden usar puntos.");
            }
            descuento = ((Empleado) usuario).calcularDescuento();
        }
        
        venta.calcularValores(0.19, descuento, puntosAUsar);
        if (usuario instanceof Cliente) { 
        	((Cliente) usuario).agregarPuntos(venta.getPuntosGenerados());
        }
        ventas.add(venta);
        
        return venta;
    }

        //requerimiento 15

    public Prestamo solicitarPrestamoJuegoFlexible(String login, String idJuego) throws Exception {

    	 	Usuario usuario = validarUsuario(login);
    	    JuegoMesaPrestamo juego = validarJuegoPrestamo(idJuego);
    	    
    	    boolean fueExplicado = false;
    	    if (juego.isEsDificil()) {
    	        fueExplicado = hayMeseroCapacitadoParaJuego(idJuego); 
    	        if (!fueExplicado) {
    	        }
    	    }

        if (!juego.isDisponible()) {
            throw new Exception("El juego no está disponible.");
        }
        Mesa mesaDelUsuario = null;

        if (usuario instanceof Cliente) {
            for (Mesa mesa : mesas.values()) {
                if (mesa.isOcupada() && mesa.getOcupante() != null
                        && mesa.getOcupante().getLogin().equals(login)) {
                    mesaDelUsuario = mesa;
                    break;
                }
            }
            if (mesaDelUsuario == null) {
                throw new Exception("El cliente debe tener una mesa asignada.");
            }
            if (mesaDelUsuario.getJuegosPrestadosActivos() >= 2) {
                throw new Exception("La mesa ya tiene 2 juegos prestados.");
            }        if (juego.getMinJugadores() > mesaDelUsuario.getPersonasActuales()
                    || juego.getMaxJugadores() < mesaDelUsuario.getPersonasActuales()) {
                throw new Exception("El número de personas no es adecuado para jugar.");
            }
            if (mesaDelUsuario.isHayJovenes() && !juego.isJueganMenores18()) {
                throw new Exception("No pueden jugarlo menores de 18.");
            }
            if (mesaDelUsuario.isHayNinos() && !juego.isJueganMenores5()) {
                throw new Exception("No pueden jugarlo menores de 5.");
            }
            if (mesaDelUsuario.isTieneBebidaCaliente() && juego.getCategoria().equalsIgnoreCase("Accion")) {
                throw new Exception("Hay bebida caliente en la mesa, no se permite juego de Acción.");
            }
    		}else if (usuario instanceof Empleado) {
            Empleado emp = (Empleado) usuario;
            if (emp.estaEnTurno()) {
                boolean hayClientes = false;
                for (Mesa mesa : mesas.values()) {
                    if (mesa.isOcupada()) { hayClientes = true; break; }
                }
                if (hayClientes) {
                    throw new Exception("El empleado no puede pedir préstamos en turno mientras haya clientes.");
                }
            }     
        } else if (usuario instanceof Administrador) {
                throw new Exception("El administrador no puede solicitar préstamos.");
            }
            
            
            
            

        String fechaActual = LocalDateTime.now().toString();

        String idPrestamo = "PR" + consecutivoPrestamos++;

        Prestamo prestamo = new Prestamo(
            idPrestamo,
            fechaActual,
            fueExplicado,
            juego,
            usuario,
            mesaDelUsuario);

        prestamos.add(prestamo);

        juego.setDisponible(false);
        if (mesaDelUsuario != null) {
            mesaDelUsuario.nuevoJuegoPrestado();
            if (juego.getCategoria().equalsIgnoreCase("Accion")) {
                mesaDelUsuario.setTieneJuegoAccion(true);
            }
        }

        return prestamo;
    }
    
    private boolean hayMeseroCapacitadoParaJuego(String idJuego) {
        for (Usuario u : usuarios.values()) {
            if (u instanceof Mesero) {
                Mesero m = (Mesero) u;
                if (m.puedeExplicar(idJuego)) {
                    return true;
                }
            }
        }
        return false;
    }

    //requerimiento 17 //

    public SugerenciaPlato sugerirPlato(String loginEmpleado, String nombrePropuesto, 
            double precio, boolean esAlcoholica, boolean esCaliente, List<String> alergenos, String tipo) throws Exception {
    		Empleado empleado = validarEmpleado(loginEmpleado);
        String idSugerencia = "SP" + consecutivoSugerencias++;
        SugerenciaPlato sugerencia = empleado.sugerirPlato(idSugerencia, nombrePropuesto, precio, esAlcoholica, esCaliente, alergenos, tipo);
        sugerencias.add(sugerencia);
        return sugerencia;
    }
    


    	//Requerimiento 18 
    
    public Pedido registrarPedidoMesero(String login, String idMesa) throws Exception {
    	
    		Mesero mesero = validarMesero(login);
        Mesa mesa = validarMesa(idMesa);
        String idPedido = "PED" + consecutivoPedidos++;
        Pedido pedido = mesero.registrarPedido(mesa, idPedido, LocalDateTime.now().toString());
        pedidos.add(pedido);
        return pedido;
    }
    
    //REquerimiento 19 prestamo de empleado a cliente
    
    public Prestamo prestamoDeJuegos(String idJuego, String idMesero, String idMesa, String idCliente) throws Exception {
        Mesero mesero = validarMesero(idMesero);
        Cliente cliente = validarCliente(idCliente);
        Mesa mesa = validarMesa(idMesa);
        JuegoMesaPrestamo juegoMesaPrestamo = validarJuegoPrestamo(idJuego);

        if (mesa.getJuegosPrestadosActivos() >= 2) {
	            throw new Exception("La mesa ya tiene 2 juegos prestados (máximo permitido).");
        }
	    	
	    	if(!juegoMesaPrestamo.isDisponible()) {
	    		throw new Exception("No está disponible el juego");
	    	}
	    	if(juegoMesaPrestamo.getMinJugadores() > mesa.getPersonasActuales() || juegoMesaPrestamo.getMaxJugadores() < mesa.getPersonasActuales()) {
	    		throw new Exception("El número de personas no es adecuado para jugar");
	    	}
	    	
	    	if (juegoMesaPrestamo.isEsDificil() && !((Mesero) mesero).puedeExplicar(idJuego)) {
	    	    System.out.println("Advertencia: ningún mesero puede explicar este juego.");
	    	}
	    	if( mesa.isHayJovenes() && !juegoMesaPrestamo.isJueganMenores18()) {
	    		throw new Exception("No pueden jugarlo menores de 18");
	    	}
	    	if( mesa.isHayNinos() && !juegoMesaPrestamo.isJueganMenores5()) {
	    		throw new Exception("No pueden jugarlo menores de 5");
	    	}
	    	
        if (mesa.isTieneBebidaCaliente() && juegoMesaPrestamo.getCategoria().equalsIgnoreCase("Accion"))
            throw new Exception("No se puede prestar un juego de Acción: hay una bebida caliente en la mesa.");
	    	
	    	String idPrestamo = "PR" + consecutivoPrestamos++;
	    	boolean fueExplicado = juegoMesaPrestamo.isEsDificil() && ((Mesero) mesero).puedeExplicar(idJuego);
	    	Prestamo prestamo = ((Mesero) mesero).realizarPrestamo(juegoMesaPrestamo, mesa, idPrestamo, LocalDateTime.now().toString(), fueExplicado,(Cliente) cliente);
	    	juegoMesaPrestamo.setDisponible(false);
	    	prestamos.add(prestamo);
	    	
	    	mesa.nuevoJuegoPrestado();
	    	if (juegoMesaPrestamo.getCategoria().equalsIgnoreCase("Accion")) {
	    	    mesa.setTieneJuegoAccion(true);
	    	}
    		
	    	return prestamo;
    	
    }
    
    //RF 20
    
    public void prepararPedido(String idCocinero, String idPedido) throws Exception {
    	
        Cocinero cocinero = validarCocinero(idCocinero);
        Pedido pedido = validarPedido(idPedido);
      
        cocinero.atenderPedidos(pedido);
        
    }
    
    //Acciones admin
    
    // RF21
    
    public void agregarJuegoPrestamo(String idAdmin,String nombre, int anioPublicacion, String editorJuego, String categoria, int minJugadores, int maxJugadores, boolean esDificil, boolean jueganMenores5, boolean jueganMenores18, boolean disponible, int vecesPrestado, String estado) throws Exception{
        Usuario admin = validarAdmin(idAdmin);
        String idJuegoPrestamo = "JP" + this.consecutivoJuegosPrestamos++;
        JuegoMesaPrestamo juegoMesaPrestamo = ((Administrador) admin).agregarJuegoPrestamo(nombre, anioPublicacion, editorJuego, categoria, minJugadores, maxJugadores, esDificil, jueganMenores5, jueganMenores18, idJuegoPrestamo, disponible, vecesPrestado, estado);
        this.juegosPrestamo.put(idJuegoPrestamo, juegoMesaPrestamo);
    }
    
    public void agregarJuegoVenta(String idAdmin,String nombre, int anioPublicacion, String editorJuego, String categoria, int minJugadores, int maxJugadores, boolean esDificil, boolean jueganMenores5, boolean jueganMenores18 , double precioVenta, int cantidadStock, double costoBase) throws Exception{

        Usuario admin = validarAdmin(idAdmin);
        String idJuegoVenta = "JV" + this.consecutivoJuegosVentas++;
        JuegoMesaVenta juegoMesaVenta = ((Administrador) admin).agregarJuegoVenta(nombre, anioPublicacion, editorJuego, categoria, minJugadores, maxJugadores, esDificil, jueganMenores5, jueganMenores18, idJuegoVenta,precioVenta,cantidadStock, costoBase);
        this.juegosVenta.put(idJuegoVenta, juegoMesaVenta);
    }
    
    
    public void registrarMesero(String idAdmin, String login, String contrasena) throws Exception {

        if (usuarios.containsKey(login)) {
        		throw new Exception("Ya existe un usuario con ese login.");
        }
            
        Usuario admin = validarAdmin(idAdmin);
        Mesero mesero = ((Administrador) admin).registrarMesero(login, contrasena);
        this.usuarios.put(login, mesero);
    }

    public void registrarCocinero(String idAdmin, String login, String contrasena) throws Exception {
        if (usuarios.containsKey(login)) {
            throw new Exception("Ya existe un usuario con ese login.");
        }
        Usuario admin = validarAdmin(idAdmin);
        Cocinero cocinero = ((Administrador) admin).registrarCocinero(login, contrasena);
        this.usuarios.put(login, cocinero);
    }

    public void agregarTurno(String idAdmin, String idEmpleado, Turno turno) throws Exception {
        Administrador admin = validarAdmin(idAdmin);
        Empleado empleado = validarEmpleado(idEmpleado);
        admin.asignarTurno(empleado, turno);
    }

    public void aprobarCambioTurno(String idAdmin, String idSolicitud) throws Exception {
    		
    		Administrador admin = validarAdmin(idAdmin);
        SolicitudCambioTurno solicitud = validarSolicitud(idSolicitud);

        int meseros = 0;
        int	cocineros = 0;
        for (Usuario u : usuarios.values()) {
            if (u instanceof Mesero) meseros++;
            if (u instanceof Cocinero) cocineros++;
        }
        
        admin.aprobarCambioTurno(solicitud, meseros, cocineros, consecutivoTurnos++);
    }

    public void rechazarCambioTurno(String idAdmin, String idSolicitud) throws Exception {
    		Administrador admin = validarAdmin(idAdmin);
        SolicitudCambioTurno solicitud = validarSolicitud(idSolicitud);
       
        admin.rechazarCambioTurno(solicitud);
    }

    public void agregarBebida(String idAdmin, String nombre, double precio, boolean disponible, boolean alcoholica, boolean caliente) throws Exception {
        Usuario admin = validarAdmin(idAdmin);
        String idProducto = "B" + consecutivoProductos++;

        Bebida bebida = ((Administrador) admin).registrarBebida(idProducto, nombre, precio, disponible, alcoholica, caliente);
        this.menu.put(idProducto, bebida);
    }

    public void agregarPasteleria(String idAdmin, String nombre, double precio, boolean disponible, List<String> alergenos) throws Exception {
        Usuario admin = validarAdmin(idAdmin);
        String idProducto = "P" + consecutivoProductos++;
        Pasteleria pasteleria = ((Administrador) admin).registrarPasteleria(idProducto, nombre, precio, disponible, alergenos);
        this.menu.put(idProducto, pasteleria);
    }

    public void aprobarSugerenciaPlato(String idAdmin, String idSugerencia) throws Exception {
    		Administrador admin = validarAdmin(idAdmin);
    	    SugerenciaPlato sugerencia = validarSugerencia(idSugerencia);
    	    ProductoMenu plato = admin.aprobarSugerenciaPlato(sugerencia, "P" + consecutivoProductos++);
    	    this.menu.put(plato.getIdProducto(), plato);
        
    }
    
    public void rechazarSugerenciaPlato(String idAdmin, String idSugerencia) throws Exception {
    	Administrador admin = validarAdmin(idAdmin);
        SugerenciaPlato sugerencia = validarSugerencia(idSugerencia);
        admin.rechazarSugerenciaPlato(sugerencia);
    }
  
    
    public void cambiarEstadoJuego(String idAdmin, String idJuego, String nuevoEstado) throws Exception {
    		Administrador admin = validarAdmin(idAdmin);
        JuegoMesaPrestamo juego = validarJuegoPrestamo(idJuego);
        admin.cambiarEstadoJuego(juego, nuevoEstado);
    }

    public void eliminarJuegoPrestamo(String idAdmin, String idJuego) throws Exception {
    		Administrador admin = validarAdmin(idAdmin);
        admin.eliminarJuegoPrestamo(this.juegosPrestamo, idJuego);
    }

    public void eliminarJuegoVenta(String idAdmin, String idJuego) throws Exception {
    		Administrador admin = validarAdmin(idAdmin);
        admin.eliminarJuegoVenta(this.juegosVenta, idJuego);
    }
    
    public Informe consultarInforme(String idAdmin, String granularidad) throws Exception {
    		Administrador admin = validarAdmin(idAdmin);
        return admin.consultarInforme(this.ventas, this.pedidos, granularidad);
    }
    
    public Map<String, JuegoMesaPrestamo> getJuegosPrestamo() {
        return this.juegosPrestamo;
    }

    public Map<String, JuegoMesaVenta> getJuegosVenta() {
        return this.juegosVenta;
    }

    public List<VentaJuego> getVentas() {
        return this.ventas;
    }

    public List<Pedido> getPedidos() {
        return this.pedidos;
    }
    
    public void agregarJuegoConocidoMesero(String idAdmin, String idMesero, String idJuego) throws Exception {
    		Administrador admin = validarAdmin(idAdmin);
        Mesero mesero = validarMesero(idMesero);
        validarJuegoPrestamo(idJuego);
        admin.agregarJuegoDificil(mesero, idJuego);
    }
    
    public Map<String, List<Prestamo>> consultarInventarioPrestamo(String idAdmin) throws Exception {
    		validarAdmin(idAdmin);
        Map<String, List<Prestamo>> historial = new HashMap<>();
        for (JuegoMesaPrestamo j : this.juegosPrestamo.values()) {
            List<Prestamo> prestamosPorJuego = new ArrayList<>();
            for (Prestamo p : this.prestamos) {
                if (p.getJuego().getIdJuegoPrestamo().equals(j.getIdJuegoPrestamo())) {
                    prestamosPorJuego.add(p);
                }
            }
            historial.put(j.getIdJuegoPrestamo(), prestamosPorJuego);
        }
        return historial;
    }
    
    public Map<String, JuegoMesaVenta> consultarInventarioVenta(String idAdmin) throws Exception {
    		validarAdmin(idAdmin);
        return this.juegosVenta;
    }

	public Map<String, ProductoMenu> getMenu() {
		return menu;
	}

	public List<SugerenciaPlato> getSugerencias() {
		return sugerencias;
	}
	
	
	public int getCapacidadMaximaCafe() {
		return capacidadMaximaCafe;
	}

	public void setCapacidadMaximaCafe(int capacidadMaximaCafe) {
		this.capacidadMaximaCafe = capacidadMaximaCafe;
	}
	

	public List<SolicitudCambioTurno> getSolicitudesCambioTurno() {
		return solicitudesCambioTurno;
	}

	public void sincronizarConsecutivos() {
	    this.consecutivoJuegosPrestamos = this.juegosPrestamo.size() + 1;
	    this.consecutivoJuegosVentas = this.juegosVenta.size() + 1;
	    this.consecutivoPrestamos = this.prestamos.size() + 1;
	    this.consecutivoPedidos = this.pedidos.size() + 1;
	    this.consecutivoVentas = this.ventas.size() + 1;
	    this.consecutivoSolicitudes = this.solicitudesCambioTurno.size() + 1;
	    this.consecutivoProductos = this.menu.size() + 1;
	    this.consecutivoSugerencias = this.sugerencias.size() + 1;
	    this.consecutivoTorneos = this.torneos.size() + 1;
	    this.consecutivoTurnos = 1;
	    for (Usuario u : this.usuarios.values()) {
	        if (u instanceof Empleado) {
	            this.consecutivoTurnos += ((Empleado) u).getTurnos().size();
	        }
	    }
	}
    
	public void eliminarJuegoFavoritoDeUsuario(String login, String idJuego) throws Exception {
		Usuario usuario = validarUsuario(login);
	    JuegoMesaPrestamo juego = validarJuegoPrestamo(idJuego);
	    usuario.eliminarJuegoFavorito(juego);
	}
    
	public void moverJuegoVentaAPrestamo(String idAdmin, String idJuegoVenta) throws Exception {
	    Administrador admin = validarAdmin(idAdmin);
	    JuegoMesaVenta juegoVenta = validarJuegoVenta(idJuegoVenta);
	    String nuevoId = "JP" + consecutivoJuegosPrestamos++;
	    JuegoMesaPrestamo nuevo = admin.moverVentaAPrestamo(juegoVenta, nuevoId);
	    juegosPrestamo.put(nuevoId, nuevo);
	}

	public void repararJuego(String idAdmin, String idJuegoPrestamo, String idJuegoVenta) throws Exception {
	    Administrador admin = validarAdmin(idAdmin);
	    JuegoMesaPrestamo juegoPrestamo = validarJuegoPrestamo(idJuegoPrestamo);
	    JuegoMesaVenta juegoVenta = validarJuegoVenta(idJuegoVenta);
	    admin.repararJuego(juegoPrestamo, juegoVenta);
	}

	public void marcarJuegoRobado(String idAdmin, String idJuegoPrestamo) throws Exception {
	    Administrador admin = validarAdmin(idAdmin);
	    JuegoMesaPrestamo juego = validarJuegoPrestamo(idJuegoPrestamo);
	    admin.marcarJuegoRobado(juego);
	}

	public void reabastecerJuegoVenta(String idAdmin, String idJuegoVenta, int cantidad) throws Exception {
	    Administrador admin = validarAdmin(idAdmin);
	    JuegoMesaVenta juego = validarJuegoVenta(idJuegoVenta);
	    admin.reabastecerJuegoVenta(juego, cantidad);
	}
	
	private Administrador validarAdmin(String idAdmin) throws Exception {
	    if (!usuarios.containsKey(idAdmin)) {
	        throw new Exception("El usuario no existe.");
	    }
	    if (!(usuarios.get(idAdmin) instanceof Administrador)) {
	        throw new Exception("El usuario no es un administrador.");
	    }
	    return (Administrador) usuarios.get(idAdmin);
	}
	
	private JuegoMesaPrestamo validarJuegoPrestamo(String idJuego) throws Exception {
	    if (!juegosPrestamo.containsKey(idJuego)) {
	        throw new Exception("El juego no existe en el inventario de préstamo.");
	    }
	    return juegosPrestamo.get(idJuego);
	}

	private JuegoMesaVenta validarJuegoVenta(String idJuego) throws Exception {
	    if (!juegosVenta.containsKey(idJuego)) {
	        throw new Exception("El juego no existe en el inventario de venta.");
	    }
	    return juegosVenta.get(idJuego);
	}
	
	private Usuario validarUsuario(String login) throws Exception {
	    if (!usuarios.containsKey(login)) {
	        throw new Exception("El usuario no existe.");
	    }
	    return usuarios.get(login);
	}

	private Mesero validarMesero(String login) throws Exception {
	    Usuario u = validarUsuario(login);
	    if (!(u instanceof Mesero)) {
	        throw new Exception("El usuario no es un mesero.");
	    }
	    return (Mesero) u;
	}

	private Cocinero validarCocinero(String login) throws Exception {
	    Usuario u = validarUsuario(login);
	    if (!(u instanceof Cocinero)) {
	        throw new Exception("El usuario no es un cocinero.");
	    }
	    return (Cocinero) u;
	}

	private Cliente validarCliente(String login) throws Exception {
	    Usuario u = validarUsuario(login);
	    if (!(u instanceof Cliente)) {
	        throw new Exception("El usuario no es un cliente.");
	    }
	    return (Cliente) u;
	}

	private Empleado validarEmpleado(String login) throws Exception {
	    Usuario u = validarUsuario(login);
	    if (!(u instanceof Empleado)) {
	        throw new Exception("El usuario no es un empleado.");
	    }
	    return (Empleado) u;
	}

	private Mesa validarMesa(String idMesa) throws Exception {
	    if (!mesas.containsKey(idMesa)) {
	        throw new Exception("La mesa no existe.");
	    }
	    return mesas.get(idMesa);
	}

	private SolicitudCambioTurno validarSolicitud(String idSolicitud) throws Exception {
	    for (SolicitudCambioTurno s : solicitudesCambioTurno) {
	        if (s.getIdSolicitud().equals(idSolicitud)) return s;
	    }
	    throw new Exception("La solicitud no existe.");
	}

	private SugerenciaPlato validarSugerencia(String idSugerencia) throws Exception {
	    for (SugerenciaPlato s : sugerencias) {
	        if (s.getIdSugerencia().equals(idSugerencia)) return s;
	    }
	    throw new Exception("La sugerencia no existe.");
	}

	private Pedido validarPedido(String idPedido) throws Exception {
	    for (Pedido p : pedidos) {
	        if (p.getIdPedido().equals(idPedido)) return p;
	    }
	    throw new Exception("El pedido no existe.");
	}
	
	private Torneo validarTorneo(String idTorneo) throws Exception {
	    for (Torneo t : torneos) {
	        if (t.getId().equals(idTorneo)) return t;
	    }
	    throw new Exception("El torneo no existe.");
	}
	
	public List<Torneo> getTorneos() {
	    return torneos;
	}

	public void agregarTorneo(Torneo torneo) {
	    torneos.add(torneo);
	}
	
	//PARA PAGAR CON CODIGO DE TORNEO
	
	public VentaJuego comprarJuegoConDescuentoTorneo(String login, String idJuego, int cantidad) 
	        throws Exception {

	    Cliente cliente = validarCliente(login);   
	    JuegoMesaVenta juego = validarJuegoVenta(idJuego);

	    juego.reducirStock(cantidad);

	    String idVenta = "V" + consecutivoVentas++;
	    VentaJuego venta = new VentaJuego(idVenta, LocalDateTime.now().toString(), cliente);
	    venta.agregarDetalle(new DetalleVenta(cantidad, juego));

	    double descuento = cliente.consumirDescuentoTorneo();   

	    venta.calcularValores(0.19, descuento, 0);   
	    cliente.agregarPuntos(venta.getPuntosGenerados());
	    ventas.add(venta);

	    return venta;
	}
	
	private int calcularCuposMaximos(JuegoMesaPrestamo juego) {
	    int copias = 0;
	    String nombre = juego.getNombre();
	    for (JuegoMesaPrestamo j : juegosPrestamo.values()) {
	        if (j.getNombre().equals(nombre) 
	            && !j.getEstado().equalsIgnoreCase("Desaparecido")) {
	            copias++;
	        }
	    }
	    return copias * juego.getMaxJugadores();
	}
	
	public TorneoAmistoso crearTorneoAmistoso(String idAdmin, String idJuego, int numParticipantes,DayOfWeek dia, double premioDescuento) throws Exception {
		Administrador admin = validarAdmin(idAdmin);
		JuegoMesaPrestamo juego = validarJuegoPrestamo(idJuego);
		
		int cuposMaximos = calcularCuposMaximos(juego);
		if (numParticipantes > cuposMaximos) {
			throw new Exception("No hay suficientes copias del juego. Máximo permitido: " 
			+ cuposMaximos + ", solicitado: " + numParticipantes + ".");
		}
		
		String id = "TA" + consecutivoTorneos++;
		TorneoAmistoso torneo = (TorneoAmistoso) admin.crearTorneoAmistoso(id, numParticipantes, juego, dia, premioDescuento);
		torneos.add(torneo);
		return torneo;
		}
		
		public TorneoCompetitivo crearTorneoCompetitivo(String idAdmin, String idJuego, int numParticipantes, DayOfWeek dia, double tarifa) throws Exception {
		Administrador admin = validarAdmin(idAdmin);
		JuegoMesaPrestamo juego = validarJuegoPrestamo(idJuego);
		
		if (tarifa <= 0) {
			throw new Exception("La tarifa debe ser mayor que cero.");
		}
		
		int cuposMaximos = calcularCuposMaximos(juego);
		if (numParticipantes > cuposMaximos) {
			throw new Exception("No hay suficientes copias del juego. Máximo permitido: " + cuposMaximos + ".");
		}
		
		String id = "TC" + consecutivoTorneos++;
		TorneoCompetitivo torneo = (TorneoCompetitivo) admin.crearTorneoCompetitivo(id, numParticipantes, juego, dia, tarifa);
		torneos.add(torneo);
		return torneo;
		}
		
		public void eliminarTorneo(String idAdmin, String idTorneo) throws Exception {
		    validarAdmin(idAdmin);
		    Torneo torneo = validarTorneo(idTorneo);
		    torneos.remove(torneo);
		}
		
		public void inscribirUsuarioTorneo(String login, String idTorneo, int cupos) throws Exception {
		    Usuario usuario = validarUsuario(login);
		    Torneo torneo = validarTorneo(idTorneo);

		    if (usuario instanceof Empleado) {
		        if (((Empleado) usuario).tieneTurnoElDia(torneo.getDia())) {
		            throw new Exception("El empleado tiene un turno asignado el día del torneo.");
		        }
		    } else if (usuario instanceof Administrador) {
		        throw new Exception("El administrador no puede inscribirse en torneos.");
		    }

		    torneo.inscribirUsuario(usuario, cupos);
		}
		
		public void desinscribirUsuarioTorneo(String login, String idTorneo) throws Exception {
		    Usuario usuario = validarUsuario(login);
		    Torneo torneo = validarTorneo(idTorneo);
		    torneo.eliminarUsuario(usuario);
		}
		
		public void otorgarPremioTorneoAmistoso(String idAdmin, String idTorneo, String idGanador) throws Exception {
		    Administrador admin = validarAdmin(idAdmin);
		    Torneo torneo = validarTorneo(idTorneo);

		    if (!(torneo instanceof TorneoAmistoso)) {
		        throw new Exception("El torneo no es amistoso, no aplica premio de descuento.");
		    }

		    Cliente ganador = validarCliente(idGanador);

		    if (!torneo.getInscritosRegulares().containsKey(idGanador) && !torneo.getInscritosFanaticos().containsKey(idGanador)) {
		        throw new Exception("El ganador no estaba inscrito en este torneo.");
		    }

		    admin.otorgarPremioAmistoso((TorneoAmistoso) torneo, ganador);
		}
    
}

