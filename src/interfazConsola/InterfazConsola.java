package interfazConsola;

import java.time.LocalDateTime;
import java.util.*;
import juegos.*;
import modelo.Cafe;
import modelo.GestorPersistencia;
import modelo.Informe;
import pedidos.*;
import prestamos.*;
import productos.*;
import usuarios.*;
import ventas.*;

public class InterfazConsola {

	public static void main(String[] args) throws Exception {
	    Cafe cafe = new Cafe();
	    GestorPersistencia gestor = new GestorPersistencia("datos/datosTest.txt");
	    gestor.cargarTodo(cafe);
	    Scanner sc = new Scanner(System.in);

	    int opcionInicio = -1;
	    while (opcionInicio != 0) {
	        System.out.println("\n--- BIENVENIDO ---");
	        System.out.println("1. Iniciar sesión");
	        System.out.println("2. Registrarse como cliente");
	        System.out.println("0. Salir");
	        try {
	            opcionInicio = Integer.parseInt(sc.nextLine());
	        } catch (NumberFormatException e) {
	            System.out.println("Debes ingresar un número.");
	            continue;
	        }

	        if (opcionInicio == 1) {
	            System.out.println("Login: ");
	            String login = sc.nextLine();
	            System.out.println("Contraseña: ");
	            String contrasena = sc.nextLine();
	            try {
	                Usuario usuario = cafe.iniciarSesion(login, contrasena);
	                if (usuario instanceof Administrador) {
	                    menuAdmin(cafe, sc, login);
	                } else if (usuario instanceof Mesero) {
	                    menuMesero(cafe, sc, login);
	                } else if (usuario instanceof Cocinero) {
	                    menuCocinero(cafe, sc, login);
	                } else if (usuario instanceof Cliente) {
	                    menuCliente(cafe, sc, login);
	                }
	            } catch (Exception e) {
	                System.out.println("Error: " + e.getMessage());
	            }

	        } else if (opcionInicio == 2) {
	            System.out.println("Login: ");
	            String login = sc.nextLine();
	            System.out.println("Contraseña: ");
	            String contrasena = sc.nextLine();
	            cafe.registrarCliente(login, contrasena);
	            System.out.println("Cliente registrado exitosamente.");
	        }
	    }

	    gestor.guardarTodo(cafe);
	    sc.close();
	}


    private static void menuAdmin(Cafe cafe, Scanner sc, String login) throws Exception {
        int opcion = -1;
        while (opcion != 0) {
        	 try {
            System.out.println("\n--- MENU ADMINISTRADOR ---");
            System.out.println("1. Registrar mesero");
            System.out.println("2. Registrar cocinero");
            System.out.println("3. Agregar juego préstamo");
            System.out.println("4. Agregar juego venta");
            System.out.println("5. Agregar turno a empleado");
            System.out.println("6. Aprobar cambio de turno");
            System.out.println("7. Rechazar cambio de turno");
            System.out.println("8. Agregar bebida");
            System.out.println("9. Agregar pastelería");
            System.out.println("10. Aprobar sugerencia plato");
            System.out.println("11. Rechazar sugerencia plato");
            System.out.println("12. Cambiar estado juego préstamo");
            System.out.println("13. Eliminar juego préstamo");
            System.out.println("14. Eliminar juego venta");
            System.out.println("15. Agregar juegos dificiles a un empleado");
            System.out.println("16. Informe diario");
            System.out.println("17. Informe semanal");
            System.out.println("18. Informe mensual");
            System.out.println("19. Ver inventario detallado préstamo");
            System.out.println("20. Ver inventario detallado venta");
            System.out.println("21. Crear torneo amistoso");
            System.out.println("22. Crear torneo competitivo");
            System.out.println("23. Eliminar torneo");
            System.out.println("24. Otorgar premio torneo amistoso");
            System.out.println("25. Ver torneos");
            System.out.println("26. Mover juego venta a préstamo");
            System.out.println("27. Reparar juego");
            System.out.println("28. Marcar juego como robado");
            System.out.println("29. Reabastecer stock");
            



            System.out.println("0. Salir");
            String entrada = sc.nextLine();
            try {
                opcion = Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.println("Error: debes ingresar un número.");
                opcion = -1;
                continue;
            }

            if (opcion == 1) {
                System.out.println("Login mesero: ");
                String l = sc.nextLine();
                System.out.println("Contraseña: ");
                String c = sc.nextLine();
                cafe.registrarMesero(login, l, c);
                System.out.println("Mesero registrado.");

            } else if (opcion == 2) {
                System.out.println("Login cocinero: ");
                String l = sc.nextLine();
                System.out.println("Contraseña: ");
                String c = sc.nextLine();
                cafe.registrarCocinero(login, l, c);
                System.out.println("Cocinero registrado.");

            } else if (opcion == 3) {
                System.out.println("Nombre: ");
                String nombre = sc.nextLine();
                System.out.println("Año publicación: ");
                int anio = Integer.parseInt(sc.nextLine());
                System.out.println("Editor: ");
                String editor = sc.nextLine();
                System.out.println("Categoria (Cartas/Tablero/Accion): ");
                String categoria = sc.nextLine();
                System.out.println("Min jugadores: ");
                int min = Integer.parseInt(sc.nextLine());
                System.out.println("Max jugadores: ");
                int max = Integer.parseInt(sc.nextLine());
                System.out.println("Es dificil (true/false): ");
                boolean dificil = Boolean.parseBoolean(sc.nextLine());
                System.out.println("Juegan menores 5 (true/false): ");
                boolean menores5 = Boolean.parseBoolean(sc.nextLine());
                System.out.println("Juegan menores 18 (true/false): ");
                boolean menores18 = Boolean.parseBoolean(sc.nextLine());
                System.out.println("Estado (Nuevo/Bueno/Falta una pieza): ");
                String estado = sc.nextLine();
                cafe.agregarJuegoPrestamo(login, nombre, anio, editor, categoria, min, max, dificil, menores5, menores18, true, 0, estado);
                System.out.println("Juego préstamo agregado.");

            } else if (opcion == 4) {
                System.out.println("Nombre: ");
                String nombre = sc.nextLine();
                System.out.println("Año publicación: ");
                int anio = Integer.parseInt(sc.nextLine());
                System.out.println("Editor: ");
                String editor = sc.nextLine();
                System.out.println("Categoria (Cartas/Tablero/Accion): ");
                String categoria = sc.nextLine();
                System.out.println("Min jugadores: ");
                int min = Integer.parseInt(sc.nextLine());
                System.out.println("Max jugadores: ");
                int max = Integer.parseInt(sc.nextLine());
                System.out.println("Es dificil (true/false): ");
                boolean dificil = Boolean.parseBoolean(sc.nextLine());
                System.out.println("Juegan menores 5 (true/false): ");
                boolean menores5 = Boolean.parseBoolean(sc.nextLine());
                System.out.println("Juegan menores 18 (true/false): ");
                boolean menores18 = Boolean.parseBoolean(sc.nextLine());
                System.out.println("Precio venta: ");
                double precio = Double.parseDouble(sc.nextLine());
                System.out.println("Stock: ");
                int stock = Integer.parseInt(sc.nextLine());
                System.out.println("Costo base: ");
                double costo = Double.parseDouble(sc.nextLine());
                cafe.agregarJuegoVenta(login, nombre, anio, editor, categoria, min, max, dificil, menores5, menores18, precio, stock, costo);
                System.out.println("Juego venta agregado.");

            } else if (opcion == 5) {
                System.out.println("Login empleado: ");
                String idEmpleado = sc.nextLine();
                System.out.println("ID turno: ");
                String idTurno = sc.nextLine();
                System.out.println("Dia (LUNES/MARTES/...): ");
                String dia = sc.nextLine();
                System.out.println("Hora inicio (yyyy-MM-ddTHH:mm): ");
                LocalDateTime inicio = LocalDateTime.parse(sc.nextLine());
                System.out.println("Hora fin (yyyy-MM-ddTHH:mm): ");
                LocalDateTime fin = LocalDateTime.parse(sc.nextLine());
                Empleado emp = (Empleado) cafe.getUsuarios().get(idEmpleado);
                Turno turno = new Turno(idTurno, inicio, fin, dia, emp);
                cafe.agregarTurno(login, idEmpleado, turno);
                System.out.println("Turno asignado.");
            } else if (opcion == 6) {
                System.out.println("ID solicitud: ");
                String idSolicitud = sc.nextLine();
                cafe.aprobarCambioTurno(login, idSolicitud);
                System.out.println("Solicitud aprobada.");

            } else if (opcion == 7) {
                System.out.println("ID solicitud: ");
                String idSolicitud = sc.nextLine();
                cafe.rechazarCambioTurno(login, idSolicitud);
                System.out.println("Solicitud rechazada.");

            } else if (opcion == 8) {
                System.out.println("Nombre: ");
                String nombre = sc.nextLine();
                System.out.println("Precio: ");
                double precio = Double.parseDouble(sc.nextLine());
                System.out.println("Es alcoholica (true/false): ");
                boolean alcoholica = Boolean.parseBoolean(sc.nextLine());
                System.out.println("Es caliente (true/false): ");
                boolean caliente = Boolean.parseBoolean(sc.nextLine());
                cafe.agregarBebida(login, nombre, precio, true, alcoholica, caliente);
                System.out.println("Bebida agregada.");

            } else if (opcion == 9) {
                System.out.println("Nombre: ");
                String nombre = sc.nextLine();
                System.out.println("Precio: ");
                double precio = Double.parseDouble(sc.nextLine());
                System.out.println("Alergenos (separados por coma): ");
                List<String> alergenos = Arrays.asList(sc.nextLine().split(","));
                cafe.agregarPasteleria(login, nombre, precio, true, alergenos);
                System.out.println("Pastelería agregada.");

            } else if (opcion == 10) {
                System.out.println("ID sugerencia: ");
                String idSugerencia = sc.nextLine();
                cafe.aprobarSugerenciaPlato(login, idSugerencia);
                System.out.println("Sugerencia aprobada.");

            } else if (opcion == 11) {
                System.out.println("ID sugerencia: ");
                String idSugerencia = sc.nextLine();
                cafe.rechazarSugerenciaPlato(login, idSugerencia);
                System.out.println("Sugerencia rechazada.");

            } else if (opcion == 12) {
                System.out.println("ID juego préstamo: ");
                String idJuego = sc.nextLine();
                System.out.println("Nuevo estado: ");
                String estado = sc.nextLine();
                cafe.cambiarEstadoJuego(login, idJuego, estado);
                System.out.println("Estado actualizado.");

            } else if (opcion == 13) {
                System.out.println("ID juego préstamo: ");
                String idJuego = sc.nextLine();
                cafe.eliminarJuegoPrestamo(login, idJuego);
                System.out.println("Juego préstamo eliminado.");

            } else if (opcion == 14) {
                System.out.println("ID juego venta: ");
                String idJuego = sc.nextLine();
                cafe.eliminarJuegoVenta(login, idJuego);
                System.out.println("Juego venta eliminado.");
            } else if (opcion == 15) {
                System.out.println("Login mesero: ");
                String idMesero = sc.nextLine();
                System.out.println("ID juego: ");
                String idJuego = sc.nextLine();
                cafe.agregarJuegoConocidoMesero(login, idMesero, idJuego);
                System.out.println("Juego conocido agregado al mesero.");
     
            } else if (opcion == 16) {
                Informe inf = cafe.consultarInforme(login, "diaria");
                System.out.println("Ventas juegos: " + inf.totalJuegos + " | Impuestos: " + inf.impuestosJuegos);
                System.out.println("Ventas comida: " + inf.totalComida + " | Impuestos: " + inf.impuestosComida + " | Propinas: " + inf.propinasComida);
            } else if (opcion == 17) {
                Informe inf = cafe.consultarInforme(login, "semanal");
                System.out.println("Ventas juegos: " + inf.totalJuegos + " | Impuestos: " + inf.impuestosJuegos);
                System.out.println("Ventas comida: " + inf.totalComida + " | Impuestos: " + inf.impuestosComida + " | Propinas: " + inf.propinasComida);
            } else if (opcion == 18) {
                Informe inf = cafe.consultarInforme(login, "mensual");
                System.out.println("Ventas juegos: " + inf.totalJuegos + " | Impuestos: " + inf.impuestosJuegos);
                System.out.println("Ventas comida: " + inf.totalComida + " | Impuestos: " + inf.impuestosComida + " | Propinas: " + inf.propinasComida);
            } else if (opcion == 19) {
                Map<String, List<Prestamo>> historial = cafe.consultarInventarioPrestamo(login);
                for (Map.Entry<String, List<Prestamo>> entry : historial.entrySet()) {
                    JuegoMesaPrestamo j = cafe.getJuegosPrestamo().get(entry.getKey());
                    System.out.println(j.toString());
                    System.out.println("  Historial:");
                    for (Prestamo p : entry.getValue()) {
                        System.out.println("  - ID: " + p.getIdPrestamo()
                            + " | Usuario: " + p.getUsuario().getLogin()
                            + " | Fecha: " + p.getFechaPrestamo()
                            + " | Devuelto: " + p.fueDevuelto());
                    }
                }
            } else if (opcion == 20) {
                Map<String, JuegoMesaVenta> inventario = cafe.consultarInventarioVenta(login);
                for (JuegoMesaVenta j : inventario.values()) {
                    System.out.println(j.toString());
                }
            } else if (opcion == 21) {
                // Crear torneo amistoso
                System.out.println("ID juego: ");
                String idJuego = sc.nextLine();
                System.out.println("N° participantes: ");
                int num = Integer.parseInt(sc.nextLine());
                System.out.println("Día (MONDAY/TUESDAY/...): ");
                java.time.DayOfWeek dia = java.time.DayOfWeek.valueOf(sc.nextLine().toUpperCase());
                System.out.println("Premio descuento (ej. 0.15 para 15%): ");
                double desc = Double.parseDouble(sc.nextLine());
                cafe.crearTorneoAmistoso(login, idJuego, num, dia, desc);
                System.out.println("Torneo amistoso creado.");

            } else if (opcion == 22) {
                // Crear torneo competitivo
                System.out.println("ID juego: ");
                String idJuego = sc.nextLine();
                System.out.println("N° participantes: ");
                int num = Integer.parseInt(sc.nextLine());
                System.out.println("Día (MONDAY/TUESDAY/...): ");
                java.time.DayOfWeek dia = java.time.DayOfWeek.valueOf(sc.nextLine().toUpperCase());
                System.out.println("Tarifa de entrada: ");
                double tarifa = Double.parseDouble(sc.nextLine());
                cafe.crearTorneoCompetitivo(login, idJuego, num, dia, tarifa);
                System.out.println("Torneo competitivo creado.");

            } else if (opcion == 23) {
                // Eliminar torneo
                System.out.println("ID torneo: ");
                cafe.eliminarTorneo(login, sc.nextLine());
                System.out.println("Torneo eliminado.");

            } else if (opcion == 24) {
                // Otorgar premio amistoso
                System.out.println("ID torneo: ");
                String idT = sc.nextLine();
                System.out.println("Login del ganador: ");
                String idGanador = sc.nextLine();
                cafe.otorgarPremioTorneoAmistoso(login, idT, idGanador);
                System.out.println("Premio otorgado.");

            } else if (opcion == 25) {
                // Ver torneos
                for (torneos.Torneo t : cafe.getTorneos()) {
                    String tipo = (t instanceof torneos.TorneoAmistoso) ? "AMISTOSO" : "COMPETITIVO";
                    System.out.println("ID: " + t.getId() + " | Tipo: " + tipo
                        + " | Juego: " + t.getJuego().getNombre() + " | Día: " + t.getDia()
                        + " | CuposFan: " + t.getCuposFanaticos() + " | CuposReg: " + t.getCuposRegulares());
                    if (t instanceof torneos.TorneoCompetitivo) {
                        System.out.println("  Premio actual: " + ((torneos.TorneoCompetitivo) t).getPremio());
                    }
                }

            } else if (opcion == 26) {
                // Mover juego venta a préstamo
                System.out.println("ID juego venta: ");
                cafe.moverJuegoVentaAPrestamo(login, sc.nextLine());
                System.out.println("Juego movido a préstamo.");

            } else if (opcion == 27) {
                // Reparar juego
                System.out.println("ID juego préstamo dañado: ");
                String idJP = sc.nextLine();
                System.out.println("ID juego venta fuente: ");
                String idJV = sc.nextLine();
                cafe.repararJuego(login, idJP, idJV);
                System.out.println("Juego reparado.");

            } else if (opcion == 28) {
                // Marcar robado
                System.out.println("ID juego préstamo: ");
                cafe.marcarJuegoRobado(login, sc.nextLine());
                System.out.println("Juego marcado como desaparecido.");

            } else if (opcion == 29) {
                // re stock
                System.out.println("ID juego venta: ");
                String idJV = sc.nextLine();
                System.out.println("Cantidad: ");
                int cant = Integer.parseInt(sc.nextLine());
                cafe.reabastecerJuegoVenta(login, idJV, cant);
                System.out.println("Stock actualizado.");
            }
            
        	 }catch (Exception e) {
                 System.out.println("Error: " + e.getMessage());
             }
        }
    }


    private static void menuMesero(Cafe cafe, Scanner sc, String login) throws Exception {
        int opcion = -1;
        while (opcion != 0) {
        	try {
            System.out.println("\n--- MENU MESERO ---");
            System.out.println("1. Registrar pedido a mesa");
            System.out.println("2. Prestar juego a cliente");
            System.out.println("3. Devolver juego");
            System.out.println("4. Consultar turno");
            System.out.println("5. Solicitar cambio de turno general");
            System.out.println("6. Solicitar intercambio de turno");
            System.out.println("7. Ver favoritos");
            System.out.println("8. Agregar favorito");
            System.out.println("9. Comprar con descuento empleado");
            System.out.println("10. Sugerir plato");
            System.out.println("11. Pedir préstamo (RF15)");
            System.out.println("12. Eliminar favorito");
            System.out.println("13. Inscribirse a torneo");
            System.out.println("14. Desinscribirse de torneo");
            System.out.println("15. Ver torneos");
            System.out.println("0. Salir");
            String entrada = sc.nextLine();
            try {
                opcion = Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.println("Error: debes ingresar un número.");
                opcion = -1;
                continue;
            };

            if (opcion == 1) {
                System.out.println("ID mesa: ");
                String idMesa = sc.nextLine();
                Pedido pedido = cafe.registrarPedidoMesero(login, idMesa);
                System.out.println("Pedido creado: " + pedido.getIdPedido());
                System.out.println("ID producto a agregar (enter para terminar): ");
                String idProducto = sc.nextLine();
                while (!idProducto.isEmpty()) {
                    System.out.println("Cantidad: ");
                    int cantidad = Integer.parseInt(sc.nextLine());
                    cafe.agregarProductoAPedido(pedido, idProducto, cantidad);
                    System.out.println("ID producto (enter para terminar): ");
                    idProducto = sc.nextLine();
                }
                cafe.confirmarPedido(pedido);
                System.out.println("Pedido confirmado | Total: " + pedido.getTotal());

            } else if (opcion == 2) {
                System.out.println("ID juego: ");
                String idJuego = sc.nextLine();
                System.out.println("ID mesa: ");
                String idMesa = sc.nextLine();
                System.out.println("Login cliente: ");
                String idCliente = sc.nextLine();
                Prestamo p = cafe.prestamoDeJuegos(idJuego, login, idMesa, idCliente);
                System.out.println("Préstamo realizado: " + p.getIdPrestamo());

            } else if (opcion == 3) {
                System.out.println("ID préstamo: ");
                String idPrestamo = sc.nextLine();
                cafe.devolverJuego(idPrestamo);
                System.out.println("Juego devuelto.");

            } else if (opcion == 4) {
            	for (Turno t : cafe.consultarTurnoEmpleado(login)) {
            	    System.out.println(t.toString());
            	}

            } else if (opcion == 5) {
                System.out.println("ID turno a cambiar: ");
                String idTurno = sc.nextLine();
                SolicitudCambioTurno sc2 = cafe.solicitarCambioTurnoGeneral(login, idTurno);
                System.out.println("Solicitud creada: " + sc2.getIdSolicitud());
            } else if (opcion == 6) {
                System.out.println("ID turno a intercambiar: ");
                String idTurno = sc.nextLine();
                System.out.println("Login empleado destino: ");
                String loginDestino = sc.nextLine();
                System.out.println("ID turno del destino: ");
                String idTurnoDestino = sc.nextLine();
                SolicitudCambioTurno sc2 = cafe.solicitarIntercambioTurno(login, idTurno, loginDestino, idTurnoDestino);
                System.out.println("Solicitud creada: " + sc2.getIdSolicitud());

            } else if (opcion == 7) {
                for (JuegoMesa j : cafe.consultarFavoritos(login)) {
                    System.out.println(j.toString());
                }

            } else if (opcion == 8) {
                System.out.println("ID juego: ");
                String idJuego = sc.nextLine();
                cafe.agregarJuegoFavoritoAUsuario(login, idJuego);
                System.out.println("Favorito agregado.");
            } else if (opcion == 9) {
                // Comprar con descuento empleado (RF14)
                System.out.println("ID juego venta: ");
                String idJuego = sc.nextLine();
                System.out.println("Cantidad: ");
                int cant = Integer.parseInt(sc.nextLine());
                VentaJuego v = cafe.comprarJuegoConDescuento(login, idJuego, cant, 0, "");
                System.out.println("Compra con 20% descuento | Total: " + v.getTotal());

            } else if (opcion == 10) {
                // Sugerir plato (RF17)
                System.out.println("Nombre del plato: ");
                String nombre = sc.nextLine();
                System.out.println("Precio: ");
                double precio = Double.parseDouble(sc.nextLine());
                System.out.println("Es alcoholica (true/false): ");
                boolean alc = Boolean.parseBoolean(sc.nextLine());
                System.out.println("Es caliente (true/false): ");
                boolean cal = Boolean.parseBoolean(sc.nextLine());
                System.out.println("Alergenos (separados por coma): ");
                List<String> alerg = Arrays.asList(sc.nextLine().split(","));
                System.out.println("Tipo (BEBIDA/PASTELERIA): ");
                String tipo = sc.nextLine();
                cafe.sugerirPlato(login, nombre, precio, alc, cal, alerg, tipo);
                System.out.println("Sugerencia enviada.");

            } else if (opcion == 11) {
                // Pedir préstamo fuera de turno (RF15)
                System.out.println("ID juego: ");
                String idJuego = sc.nextLine();
                Prestamo p = cafe.solicitarPrestamoJuegoFlexible(login, idJuego);
                if (!p.isFueExplicado() && cafe.getJuegosPrestamo().get(idJuego).isEsDificil()) {
                    System.out.println("Advertencia: no hay mesero capacitado, puede ser difícil de entender.");
                }
                System.out.println("Préstamo: " + p.getIdPrestamo());

            } else if (opcion == 12) {
                // Eliminar favorito
                System.out.println("ID juego: ");
                cafe.eliminarJuegoFavoritoDeUsuario(login, sc.nextLine());
                System.out.println("Favorito eliminado.");

            } else if (opcion == 13) {
                // Inscribirse a torneo
                System.out.println("ID torneo: ");
                String idTorneo = sc.nextLine();
                System.out.println("Cupos (1-3): ");
                int cupos = Integer.parseInt(sc.nextLine());
                cafe.inscribirUsuarioTorneo(login, idTorneo, cupos);
                System.out.println("Inscrito.");

            } else if (opcion == 14) {
                // Desinscribirse
                System.out.println("ID torneo: ");
                cafe.desinscribirUsuarioTorneo(login, sc.nextLine());
                System.out.println("Desinscrito.");

            } else if (opcion == 15) {
                // Ver torneos
                for (torneos.Torneo t : cafe.getTorneos()) {
                    System.out.println("ID: " + t.getId() + " | Juego: " + t.getJuego().getNombre()
                        + " | Día: " + t.getDia());
                }
            }
        }catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        }
    }


    private static void menuCocinero(Cafe cafe, Scanner sc, String login) throws Exception {
        int opcion = -1;
        while (opcion != 0) {
        	try {
            System.out.println("\n--- MENU COCINERO ---");
            System.out.println("1. Preparar pedido");
            System.out.println("2. Consultar turno");
            System.out.println("3. Solicitar cambio de turno general");
            System.out.println("4. Solicitar intercambio de turno");
            System.out.println("5. Ver favoritos");
            System.out.println("6. Agregar favorito");
            System.out.println("0. Salir");
            String entrada = sc.nextLine();
            try {
                opcion = Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.println("Error: debes ingresar un número.");
                opcion = -1;
                continue;
            }

            if (opcion == 1) {
                System.out.println("ID pedido: ");
                String idPedido = sc.nextLine();
                cafe.prepararPedido(login, idPedido);
                System.out.println("Pedido preparado.");

            } else if (opcion == 2) {
            	for (Turno t : cafe.consultarTurnoEmpleado(login)) {
            	    System.out.println(t.toString());
            	}

            } else if (opcion == 3) {
                System.out.println("ID turno a cambiar: ");
                String idTurno = sc.nextLine();
                SolicitudCambioTurno sc2 = cafe.solicitarCambioTurnoGeneral(login, idTurno);
                System.out.println("Solicitud creada: " + sc2.getIdSolicitud());

            } else if (opcion == 4) {
                System.out.println("ID turno a intercambiar: ");
                String idTurno = sc.nextLine();
                System.out.println("Login empleado destino: ");
                String loginDestino = sc.nextLine();
                System.out.println("ID turno del destino: ");
                String idTurnoDestino = sc.nextLine();
                SolicitudCambioTurno sc2 = cafe.solicitarIntercambioTurno(login, idTurno, loginDestino, idTurnoDestino);
                System.out.println("Solicitud creada: " + sc2.getIdSolicitud());

            } else if (opcion == 5) {
                for (JuegoMesa j : cafe.consultarFavoritos(login)) {
                    System.out.println(j.toString());
                }

            } else if (opcion == 6) {
                System.out.println("ID juego: ");
                String idJuego = sc.nextLine();
                cafe.agregarJuegoFavoritoAUsuario(login, idJuego);
                System.out.println("Favorito agregado.");
            }
            
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }


    private static void menuCliente(Cafe cafe, Scanner sc, String login) throws Exception {
        int opcion = -1;
        while (opcion != 0) {
        	try {
            System.out.println("\n--- MENU CLIENTE ---");
            System.out.println("1. Ver catálogo juegos préstamo");
            System.out.println("2. Ver menú");
            System.out.println("3. Comprar juego");
            System.out.println("4. Ver favoritos");
            System.out.println("5. Agregar favorito");
            System.out.println("6. Eliminar favorito");
            System.out.println("7. Tomar mesa (RF4)");
            System.out.println("8. Solicitar préstamo (RF5)");
            System.out.println("9. Devolver juego (RF6)");
            System.out.println("10. Realizar pedido cafetería (RF8)");
            System.out.println("11. Ver puntos de fidelidad (RF10)");
            System.out.println("12. Ver torneos disponibles");
            System.out.println("13. Inscribirse a torneo");
            System.out.println("14. Desinscribirse de torneo");
            System.out.println("15. Comprar con descuento de torneo");
            System.out.println("16. Liberar mesa");
            System.out.println("0. Salir");
            String entrada = sc.nextLine();
            try {
                opcion = Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.println("Error: debes ingresar un número.");
                opcion = -1;
                continue;
            }

            if (opcion == 1) {
                for (JuegoMesaPrestamo j : cafe.consultarCatalogoJuegosPrestamo()) System.out.println(j.toString());

            } else if (opcion == 2) {
                for (ProductoMenu p : cafe.consultarMenu()) System.out.println(p.toString());

            } else if (opcion == 3) {
                System.out.println("ID juego venta: ");
                String idJuego = sc.nextLine();
                System.out.println("Cantidad: ");
                int cantidad = Integer.parseInt(sc.nextLine());
                System.out.println("Puntos a usar (0 si ninguno): ");
                int puntos = Integer.parseInt(sc.nextLine());
                System.out.println("Código descuento (enter si ninguno): ");
                String codigo = sc.nextLine();
                VentaJuego v = cafe.comprarJuegoConDescuento(login, idJuego, cantidad, puntos, codigo);
                System.out.println("Compra realizada | Total: " + v.getTotal() + " | Puntos: " + v.getPuntosGenerados());

            } else if (opcion == 4) {
                for (JuegoMesa j : cafe.consultarFavoritos(login)) System.out.println(j.toString());

            } else if (opcion == 5) {
                System.out.println("ID juego: ");
                cafe.agregarJuegoFavoritoAUsuario(login, sc.nextLine());
                System.out.println("Favorito agregado.");

            } else if (opcion == 6) {
                System.out.println("ID juego: ");
                cafe.eliminarJuegoFavoritoDeUsuario(login, sc.nextLine());
                System.out.println("Favorito eliminado.");



            } else if (opcion == 7) {
                System.out.println("Cantidad de personas: ");
                int personas = Integer.parseInt(sc.nextLine());
                System.out.println("¿Hay jóvenes (5-18)? (true/false): ");
                boolean jovenes = Boolean.parseBoolean(sc.nextLine());
                System.out.println("¿Hay niños (<5)? (true/false): ");
                boolean ninos = Boolean.parseBoolean(sc.nextLine());
                Cliente c = (Cliente) cafe.getUsuarios().get(login);
                mesas.Mesa m = cafe.asignarMesaACliente(c, personas, jovenes, ninos);
                System.out.println("Mesa asignada: " + m.getIdMesa());

            } else if (opcion == 8) {
                System.out.println("ID juego: ");
                String idJuego = sc.nextLine();
                Prestamo p = cafe.solicitarPrestamoJuegoFlexible(login, idJuego);
                if (!p.isFueExplicado() && cafe.getJuegosPrestamo().get(idJuego).isEsDificil()) {
                    System.out.println("Advertencia: no hay mesero capacitado, puede ser difícil de entender.");
                }
                System.out.println("Préstamo: " + p.getIdPrestamo());

            } else if (opcion == 9) {
                System.out.println("ID préstamo: ");
                cafe.devolverJuego(sc.nextLine());
                System.out.println("Juego devuelto.");

            } else if (opcion == 10) {
                System.out.println("ID mesa: ");
                String idMesa = sc.nextLine();
                String loginMesero = null;
                for (Usuario u : cafe.getUsuarios().values()) {
                    if (u instanceof Mesero) { loginMesero = u.getLogin(); break; }
                }
                if (loginMesero == null) throw new Exception("No hay meseros disponibles.");
                Pedido pedido = cafe.registrarPedidoMesero(loginMesero, idMesa);
                System.out.println("Pedido creado: " + pedido.getIdPedido());
                System.out.println("ID producto (enter para terminar): ");
                String idProd = sc.nextLine();
                while (!idProd.isEmpty()) {
                    System.out.println("Cantidad: ");
                    int cant = Integer.parseInt(sc.nextLine());
                    cafe.agregarProductoAPedido(pedido, idProd, cant);
                    System.out.println("ID producto (enter para terminar): ");
                    idProd = sc.nextLine();
                }
                cafe.confirmarPedido(pedido);
                System.out.println("Pedido confirmado | Total: " + pedido.getTotal());

            } else if (opcion == 11) {
                Cliente c = (Cliente) cafe.getUsuarios().get(login);
                System.out.println("Puntos: " + c.getPuntosFidelidad());
                System.out.println("Descuento de torneo activo: " + c.getPorcentajeDescuentoTorneo());

            } else if (opcion == 12) {
                for (torneos.Torneo t : cafe.getTorneos()) {
                    System.out.println("ID: " + t.getId() + " | Juego: " + t.getJuego().getNombre()
                        + " | Día: " + t.getDia() + " | CuposFan: " + t.getCuposFanaticos()
                        + " | CuposReg: " + t.getCuposRegulares());
                }

            } else if (opcion == 13) {
                System.out.println("ID torneo: ");
                String idTorneo = sc.nextLine();
                System.out.println("Cupos a tomar (1-3): ");
                int cupos = Integer.parseInt(sc.nextLine());
                cafe.inscribirUsuarioTorneo(login, idTorneo, cupos);
                System.out.println("Inscrito.");

            } else if (opcion == 14) {
                System.out.println("ID torneo: ");
                cafe.desinscribirUsuarioTorneo(login, sc.nextLine());
                System.out.println("Desinscrito.");

            } else if (opcion == 15) {
                System.out.println("ID juego venta: ");
                String idJuego = sc.nextLine();
                System.out.println("Cantidad: ");
                int cant = Integer.parseInt(sc.nextLine());
                VentaJuego v = cafe.comprarJuegoConDescuentoTorneo(login, idJuego, cant);
                System.out.println("Compra con descuento torneo | Total: " + v.getTotal());
            } else if (opcion == 16) {
                cafe.liberarMesa(login);
                System.out.println("Mesa liberada. ¡Hasta pronto!");
            }
            
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        }
    }
}
