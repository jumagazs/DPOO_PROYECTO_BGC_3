package pruebas;

import java.time.LocalDateTime;
import java.util.*;
import juegos.*;
import mesas.*;
import pedidos.*;
import prestamos.*;
import productos.*;
import usuarios.*;
import ventas.*;
import modelo.*;
import sugerencias.*;
import java.time.DayOfWeek;
import torneos.*;

public class PruebasAutomaticas {

    public static void main(String[] args) {
        try {
            Cafe cafe = new Cafe();
            GestorPersistencia gestor = new GestorPersistencia("datos/datos.txt");
            gestor.cargarTodo(cafe);

            // ---- RF1 - Registrar cliente ----
            try {
                cafe.registrarCliente("cliente1", "1234");
                System.out.println("RF1 OK - Cliente registrado");
            } catch (Exception e) {
                System.out.println("RF1 ERROR: " + e.getMessage());
            }
            try {
                cafe.registrarCliente("cliente1", "1234");
                System.out.println("RF1 ERROR - Debió bloquear duplicado");
            } catch (Exception e) {
                System.out.println("RF1 OK - Duplicado bloqueado: " + e.getMessage());
            }

            // ---- RF2 - Iniciar sesion ----
            try {
                cafe.iniciarSesion("cliente1", "1234");
                System.out.println("RF2 OK - Sesion iniciada");
            } catch (Exception e) {
                System.out.println("RF2 ERROR: " + e.getMessage());
            }
            try {
                cafe.iniciarSesion("cliente1", "wrong");
                System.out.println("RF2 ERROR - Debio bloquear contrasena incorrecta");
            } catch (Exception e) {
                System.out.println("RF2 OK - Contrasena incorrecta bloqueada: " + e.getMessage());
            }

            // ---- RF Admin 1 - Registrar empleados ----
            try {
                cafe.registrarMesero("admin", "mesero1", "1234");
                cafe.registrarMesero("admin", "mesero2", "1234");
                cafe.registrarCocinero("admin", "cocinero1", "1234");
                System.out.println("RF Admin 1 OK - Mesero y cocinero registrados");
            } catch (Exception e) {
                System.out.println("RF Admin 1 ERROR: " + e.getMessage());
            }

            // ---- RF Admin 2 - Agregar turnos ----
            try {
                Empleado mesero1 = (Empleado) cafe.getUsuarios().get("mesero1");
                Empleado cocinero1 = (Empleado) cafe.getUsuarios().get("cocinero1");
                Turno turnoMesero = new Turno("T1", LocalDateTime.of(2026, 4, 20, 8, 0), LocalDateTime.of(2026, 4, 20, 16, 0), "LUNES", mesero1);
                Turno turnoCocinero = new Turno("T2", LocalDateTime.of(2026, 4, 20, 8, 0), LocalDateTime.of(2026, 4, 20, 16, 0), "LUNES", cocinero1);
                cafe.agregarTurno("admin", "mesero1", turnoMesero);
                cafe.agregarTurno("admin", "cocinero1", turnoCocinero);
                System.out.println("RF Admin 2 OK - Turnos asignados");
            } catch (Exception e) {
                System.out.println("RF Admin 2 ERROR: " + e.getMessage());
            }

            // ---- RF12 - Consultar turno ----
            try {
                List<Turno> turnos = cafe.consultarTurnoEmpleado("mesero1");
                System.out.println("RF12 OK - Turnos del mesero: " + turnos.size());
                for (Turno t : turnos) {
                    System.out.println("  " + t.toString());
                }
            } catch (Exception e) {
                System.out.println("RF12 ERROR: " + e.getMessage());
            }

            // ---- RF Admin 5 - Registrar juegos ----
            try {
                cafe.agregarJuegoPrestamo("admin", "Catan", 1995, "Kosmos", "Tablero", 3, 4, false, false, true, true, 0, "Bueno");
                cafe.agregarJuegoPrestamo("admin", "UNO", 1971, "Mattel", "Cartas", 2, 10, false, true, true, true, 0, "Bueno");
                cafe.agregarJuegoPrestamo("admin", "Twilight Imperium", 2017, "FFG", "Tablero", 3, 6, true, false, false, true, 0, "Nuevo");
                cafe.agregarJuegoVenta("admin", "Terraforming Mars", 2016, "FryxGames", "Tablero", 1, 5, false, false, true, 180000, 5, 120000);
                System.out.println("RF Admin 5 OK - Juegos registrados");
            } catch (Exception e) {
                System.out.println("RF Admin 5 ERROR: " + e.getMessage());
            }
            
         // ---- RF Admin 3 - Juego conocido mesero ----
            try {
                String idJuegoDificil = null;
                for (Map.Entry<String, JuegoMesaPrestamo> entry : cafe.getJuegosPrestamo().entrySet()) {
                    if (entry.getValue().getNombre().equals("Twilight Imperium")) {
                        idJuegoDificil = entry.getKey();
                        break;
                    }
                }
                cafe.agregarJuegoConocidoMesero("admin", "mesero1", idJuegoDificil);
                System.out.println("RF Admin 3 OK - Juego conocido agregado a mesero");
            } catch (Exception e) {
                System.out.println("RF Admin 3 ERROR: " + e.getMessage());
            }   

         // ---- RF Admin 3 - Ver inventario detallado ----
            try {
                Map<String, List<Prestamo>> historial = cafe.consultarInventarioPrestamo("admin");
                System.out.println("RF Admin 3 OK - Inventario detallado:");
                for (Map.Entry<String, List<Prestamo>> entry : historial.entrySet()) {
                    JuegoMesaPrestamo j = cafe.getJuegosPrestamo().get(entry.getKey());
                    System.out.println("  " + j.getNombre() + " | Estado: " + j.getEstado() + " | Veces prestado: " + j.getVecesPrestado());
                    for (Prestamo p : entry.getValue()) {
                        System.out.println("  - " + p.getIdPrestamo() + " | " + p.getUsuario().getLogin() + " | " + p.getFechaPrestamo() + " | Devuelto: " + p.fueDevuelto());
                    }
                }
            } catch (Exception e) {
                System.out.println("RF Admin 3 ERROR: " + e.getMessage());
            }

            // ---- RF Admin 6 - Gestionar menu ----
            try {
                cafe.agregarBebida("admin", "Cafe Latte", 8500, true, false, true);
                cafe.agregarBebida("admin", "Cerveza", 9000, true, true, false);
                cafe.agregarPasteleria("admin", "Torta Chocolate", 9000, true, Arrays.asList("Gluten", "Leche"));
                System.out.println("RF Admin 6 OK - Menu agregado");
            } catch (Exception e) {
                System.out.println("RF Admin 6 ERROR: " + e.getMessage());
            }

            // ---- RF3 - Catalogo juegos ----
            try {
                System.out.println("\nRF3 - Catalogo juegos prestamo:");
                for (JuegoMesaPrestamo j : cafe.consultarCatalogoJuegosPrestamo()) {
                    System.out.println("  " + j.getNombre() + " | Disponible: " + j.isDisponible());
                }
                System.out.println("RF3 OK");
            } catch (Exception e) {
                System.out.println("RF3 ERROR: " + e.getMessage());
            }

            // ---- RF7 - Consultar menu ----
            try {
                System.out.println("\nRF7 - Menu:");
                for (ProductoMenu p : cafe.consultarMenu()) {
                    System.out.println("  " + p.toString());
                }
                System.out.println("RF7 OK");
            } catch (Exception e) {
                System.out.println("RF7 ERROR: " + e.getMessage());
            }

            // ---- RF4 - Asignar mesa ----
            Mesa mesaAsignada = null;
            try {
                Cliente cliente1 = (Cliente) cafe.getUsuarios().get("cliente1");
                mesaAsignada = cafe.asignarMesaACliente(cliente1, 3, false, false);
                System.out.println("RF4 OK - Mesa asignada: " + mesaAsignada.getIdMesa());
            } catch (Exception e) {
                System.out.println("RF4 ERROR: " + e.getMessage());
            }

            // ---- RF5/RF19 - Prestamo mesero a cliente ----
            Prestamo prestamo1 = null;
            try {
                String idJuego = null;
                for (Map.Entry<String, JuegoMesaPrestamo> entry : cafe.getJuegosPrestamo().entrySet()) {
                    if (entry.getValue().getNombre().equals("Catan")) {
                        idJuego = entry.getKey();
                        break;
                    }
                }
                prestamo1 = cafe.prestamoDeJuegos(idJuego, "mesero1", mesaAsignada.getIdMesa(), "cliente1");
                System.out.println("RF19 OK - Prestamo realizado: " + prestamo1.getIdPrestamo());
            } catch (Exception e) {
                System.out.println("RF19 ERROR: " + e.getMessage());
            }

            // ---- RF19 - Prestamo juego dificil con advertencia ----
            try {
                String idJuegoDificil = null;
                for (Map.Entry<String, JuegoMesaPrestamo> entry : cafe.getJuegosPrestamo().entrySet()) {
                    if (entry.getValue().getNombre().equals("Twilight Imperium")) {
                        idJuegoDificil = entry.getKey();
                        break;
                    }
                }
                cafe.prestamoDeJuegos(idJuegoDificil, "mesero1", mesaAsignada.getIdMesa(), "cliente1");
                System.out.println("RF19 OK - Prestamo juego dificil con advertencia si aplica");
            } catch (Exception e) {
                System.out.println("RF19 ERROR: " + e.getMessage());
            }

            // ---- RF6 - Devolver juego ----
            try {
                cafe.devolverJuego(prestamo1.getIdPrestamo());
                System.out.println("RF6 OK - Juego devuelto");
            } catch (Exception e) {
                System.out.println("RF6 ERROR: " + e.getMessage());
            }
            try {
                cafe.devolverJuego(prestamo1.getIdPrestamo());
                System.out.println("RF6 ERROR - Debio bloquear devolucion duplicada");
            } catch (Exception e) {
                System.out.println("RF6 OK - Devolucion duplicada bloqueada: " + e.getMessage());
            }

            // ---- RF8/RF18 - Pedido cafeteria ----
            Pedido pedido1 = null;
            try {
                pedido1 = cafe.registrarPedidoMesero("mesero1", mesaAsignada.getIdMesa());
                String idBebida = null;
                for (Map.Entry<String, ProductoMenu> entry : cafe.getMenu().entrySet()) {
                    if (entry.getValue().getNombre().equals("Cafe Latte")) {
                        idBebida = entry.getKey();
                        break;
                    }
                }
                cafe.agregarProductoAPedido(pedido1, idBebida, 2);
                cafe.confirmarPedido(pedido1);
                System.out.println("RF8 OK - Pedido | Total: " + pedido1.getTotal());
            } catch (Exception e) {
                System.out.println("RF8 ERROR: " + e.getMessage());
            }

            // ---- RF20 - Cocinero prepara pedido ----
            try {
                cafe.prepararPedido("cocinero1", pedido1.getIdPedido());
                System.out.println("RF20 OK - Pedido preparado por cocinero");
            } catch (Exception e) {
                System.out.println("RF20 ERROR: " + e.getMessage());
            }

            // ---- RF9 - Comprar juego ----
            try {
                String idJuegoVenta = null;
                for (Map.Entry<String, JuegoMesaVenta> entry : cafe.getJuegosVenta().entrySet()) {
                    if (entry.getValue().getNombre().equals("Terraforming Mars")) {
                        idJuegoVenta = entry.getKey();
                        break;
                    }
                }
                VentaJuego venta = cafe.comprarJuegoConDescuento("cliente1", idJuegoVenta, 1, 0, "");
                System.out.println("RF9 OK - Compra | Total: " + venta.getTotal() + " | Puntos: " + venta.getPuntosGenerados());
            } catch (Exception e) {
                System.out.println("RF9 ERROR: " + e.getMessage());
            }

            // ---- RF10 - Puntos fidelidad ----
            try {
                Cliente cliente1 = (Cliente) cafe.getUsuarios().get("cliente1");
                System.out.println("RF10 - Puntos actuales: " + cliente1.getPuntosFidelidad());
                String idJuegoVenta = null;
                for (Map.Entry<String, JuegoMesaVenta> entry : cafe.getJuegosVenta().entrySet()) {
                    if (entry.getValue().getNombre().equals("Terraforming Mars")) {
                        idJuegoVenta = entry.getKey();
                        break;
                    }
                }
                VentaJuego ventaConPuntos = cafe.comprarJuegoConDescuento("cliente1", idJuegoVenta, 1, cliente1.getPuntosFidelidad(), "");
                System.out.println("RF10 OK - Compra con puntos | Total: " + ventaConPuntos.getTotal());
            } catch (Exception e) {
                System.out.println("RF10 ERROR: " + e.getMessage());
            }

            // ---- RF14 - Compra empleado con descuento ----
            try {
                String idJuegoVenta = null;
                for (Map.Entry<String, JuegoMesaVenta> entry : cafe.getJuegosVenta().entrySet()) {
                    if (entry.getValue().getNombre().equals("Terraforming Mars")) {
                        idJuegoVenta = entry.getKey();
                        break;
                    }
                }
                VentaJuego ventaEmp = cafe.comprarJuegoConDescuento("mesero1", idJuegoVenta, 1, 0, "");
                System.out.println("RF14 OK - Compra empleado 20% descuento | Total: " + ventaEmp.getTotal());
            } catch (Exception e) {
                System.out.println("RF14 ERROR: " + e.getMessage());
            }

            // ---- RF11/RF16 - Favoritos ----
            try {
                String idJuego = null;
                for (Map.Entry<String, JuegoMesaPrestamo> entry : cafe.getJuegosPrestamo().entrySet()) {
                    if (entry.getValue().getNombre().equals("UNO")) {
                        idJuego = entry.getKey();
                        break;
                    }
                }
                cafe.agregarJuegoFavoritoAUsuario("cliente1", idJuego);
                cafe.agregarJuegoFavoritoAUsuario("mesero1", idJuego);
                System.out.println("RF11/RF16 OK - Favorito agregado");
                for (JuegoMesa j : cafe.consultarFavoritos("cliente1")) {
                    System.out.println("  Favorito cliente: " + j.getNombre());
                }
                for (JuegoMesa j : cafe.consultarFavoritos("mesero1")) {
                    System.out.println("  Favorito mesero: " + j.getNombre());
                }
            } catch (Exception e) {
                System.out.println("RF11/RF16 ERROR: " + e.getMessage());
            }

         // ---- RF13 - Solicitar intercambio de turno ----
            try {
                cafe.registrarMesero("admin", "mesero3", "1234");
                Empleado mesero3 = (Empleado) cafe.getUsuarios().get("mesero3");
                Turno turnoMesero3 = new Turno("T3", LocalDateTime.of(2026, 4, 21, 8, 0), LocalDateTime.of(2026, 4, 21, 16, 0), "MARTES", mesero3);
                cafe.agregarTurno("admin", "mesero3", turnoMesero3);
                SolicitudCambioTurno sc2 = cafe.solicitarIntercambioTurno("mesero1", "T1", "mesero3", "T3");
                System.out.println("RF13 OK - Solicitud intercambio creada: " + sc2.getIdSolicitud());
                cafe.aprobarCambioTurno("admin", sc2.getIdSolicitud());
                System.out.println("RF13 OK - Intercambio aprobado");
                System.out.println("  Turno mesero1: " + cafe.consultarTurnoEmpleado("mesero1").get(0).getDia());
                System.out.println("  Turno mesero3: " + cafe.consultarTurnoEmpleado("mesero3").get(0).getDia());
            } catch (Exception e) {
                System.out.println("RF13 intercambio ERROR: " + e.getMessage());
            }

         // ---- RF13 - Cambio de turno general ----
            try {
                Empleado mesero2 = (Empleado) cafe.getUsuarios().get("mesero2");
                Turno turnoMesero2 = new Turno("T4", LocalDateTime.of(2026, 4, 22, 8, 0), LocalDateTime.of(2026, 4, 22, 16, 0), "MIERCOLES", mesero2);
                cafe.agregarTurno("admin", "mesero2", turnoMesero2);
                SolicitudCambioTurno sc = cafe.solicitarCambioTurnoGeneral("mesero2", "T4");
                System.out.println("RF13 OK - Solicitud creada: " + sc.getIdSolicitud());
                cafe.aprobarCambioTurno("admin", sc.getIdSolicitud());
                System.out.println("RF13 OK - Solicitud aprobada");
            } catch (Exception e) {
                System.out.println("RF13 ERROR: " + e.getMessage());
            }

            // ---- RF17 - Sugerir plato ----
            try {
                cafe.sugerirPlato("mesero1", "Brownie", 7000, false, true, Arrays.asList("Gluten"), "PASTELERIA");
                System.out.println("RF17 OK - Sugerencia enviada");
            } catch (Exception e) {
                System.out.println("RF17 ERROR: " + e.getMessage());
            }

            // ---- RF Admin 6 - Aprobar sugerencia ----
            try {
                String idSugerencia = cafe.getSugerencias().get(0).getIdSugerencia();
                cafe.aprobarSugerenciaPlato("admin", idSugerencia);
                System.out.println("RF Admin 6 OK - Sugerencia aprobada y plato en menu");
            } catch (Exception e) {
                System.out.println("RF Admin 6 ERROR: " + e.getMessage());
            }

            // ---- RF Admin 4 - Cambiar estado juego ----
            try {
                String idJuego = cafe.getJuegosPrestamo().keySet().iterator().next();
                cafe.cambiarEstadoJuego("admin", idJuego, "Falta una pieza");
                System.out.println("RF Admin 4 OK - Estado juego cambiado");
            } catch (Exception e) {
                System.out.println("RF Admin 4 ERROR: " + e.getMessage());
            }
            
         // ---- RF Admin 4 - Ver inventario venta ----
            try {
                Map<String, JuegoMesaVenta> inventario = cafe.consultarInventarioVenta("admin");
                System.out.println("RF Admin 4 OK - Inventario venta:");
                for (JuegoMesaVenta j : inventario.values()) {
                    System.out.println("  " + j.getNombre() + " | Stock: " + j.getCantidadStock() + " | Precio: " + j.getPrecioVenta());
                }
            } catch (Exception e) {
                System.out.println("RF Admin 4 ERROR: " + e.getMessage());
            }
            
	         // RF15 - Empleado fuera de turno puede pedir préstamo
            try {
                cafe.agregarJuegoPrestamo("admin", "JuegoParaRF15", 2020, "Test", "Cartas",
                    2, 4, false, true, true, true, 0, "Nuevo");
                String idJuegoLibre = null;
                for (Map.Entry<String, JuegoMesaPrestamo> entry : cafe.getJuegosPrestamo().entrySet()) {
                    if (entry.getValue().getNombre().equals("JuegoParaRF15")) {
                        idJuegoLibre = entry.getKey(); break;
                    }
                }
                Prestamo pEmp = cafe.solicitarPrestamoJuegoFlexible("mesero2", idJuegoLibre);
                System.out.println("RF15 OK - Empleado pidió préstamo fuera de turno: " + pEmp.getIdPrestamo());
            } catch (Exception e) {
                System.out.println("RF15 ERROR: " + e.getMessage());
            }
	         
	      // REGLA: alcohol con menores 
	      try {
	          cafe.registrarCliente("familia", "1234");
	          Cliente fam = (Cliente) cafe.getUsuarios().get("familia");
	          Mesa mesaFamilia = cafe.asignarMesaACliente(fam, 2, true, true);
	          Pedido pedFam = cafe.registrarPedidoMesero("mesero1", mesaFamilia.getIdMesa());
	          String idCerveza = null;
	          for (Map.Entry<String, ProductoMenu> e : cafe.getMenu().entrySet()) {
	              if (e.getValue().getNombre().equals("Cerveza")) { idCerveza = e.getKey(); break; }
	          }
	          cafe.agregarProductoAPedido(pedFam, idCerveza, 1);
	          System.out.println("REGLA alcohol ERROR - Debió bloquear");
	      } catch (Exception e) {
	          System.out.println("REGLA alcohol OK - Bloqueado: " + e.getMessage());
	      }
	   // REGLA: máximo 2 juegos por mesa
	      try {
	    	    Mesa mesaCliente1 = null;
	    	    for (Mesa m : cafe.getMesas()) {
	    	        if (m.isOcupada() && m.getOcupante() != null
	    	                && m.getOcupante().getLogin().equals("cliente1")) {
	    	            mesaCliente1 = m; break;
	    	        }
	    	    }
	    	    if (mesaCliente1 == null) {
	    	        System.out.println("REGLA 2 juegos ERROR - mesa cliente1 no encontrada");
	    	    } else {
	    	        List<String> disponibles = new ArrayList<>();
	    	        for (Map.Entry<String, JuegoMesaPrestamo> entry : cafe.getJuegosPrestamo().entrySet()) {
	    	            if (entry.getValue().isDisponible() && entry.getValue().isJueganMenores18()) {
	    	                disponibles.add(entry.getKey());
	    	                if (disponibles.size() == 3) break;
	    	            }
	    	        }
	    	        while (disponibles.size() < 3) {
	    	            String nombreTmp = "JuegoExtra" + disponibles.size();
	    	            cafe.agregarJuegoPrestamo("admin", nombreTmp, 2020, "X", "Cartas",
	    	                1, 10, false, true, true, true, 0, "Nuevo");
	    	            for (Map.Entry<String, JuegoMesaPrestamo> entry : cafe.getJuegosPrestamo().entrySet()) {
	    	                if (entry.getValue().getNombre().equals(nombreTmp)) {
	    	                    disponibles.add(entry.getKey()); break;
	    	                }
	    	            }
	    	        }
	    	        cafe.prestamoDeJuegos(disponibles.get(0), "mesero1", mesaCliente1.getIdMesa(), "cliente1");
	    	        cafe.prestamoDeJuegos(disponibles.get(1), "mesero1", mesaCliente1.getIdMesa(), "cliente1");
	    	        cafe.prestamoDeJuegos(disponibles.get(2), "mesero1", mesaCliente1.getIdMesa(), "cliente1");
	    	        System.out.println("REGLA 2 juegos ERROR - Debió bloquear");
	    	    }
	    	} catch (Exception e) {
	    	    System.out.println("REGLA 2 juegos OK - Bloqueado: " + e.getMessage());
	    	}

	   // RF Admin 2 - Rechazar cambio de turno
	   try {
	       Empleado m2 = (Empleado) cafe.getUsuarios().get("mesero2");
	       Turno turnoX = new Turno("TX",
	           LocalDateTime.of(2026, 4, 23, 8, 0),
	           LocalDateTime.of(2026, 4, 23, 16, 0),
	           "JUEVES", m2);
	       cafe.agregarTurno("admin", "mesero2", turnoX);
	       SolicitudCambioTurno scRechazo = cafe.solicitarCambioTurnoGeneral("mesero2", "TX");
	       cafe.rechazarCambioTurno("admin", scRechazo.getIdSolicitud());
	       System.out.println("RF Admin 2 OK - Cambio de turno rechazado");
	   } catch (Exception e) {
	       System.out.println("RF Admin 2 rechazar ERROR: " + e.getMessage());
	   }

	   // RF Admin 6 - Rechazar sugerencia
	   try {
	       cafe.sugerirPlato("mesero1", "PlatoRechazado", 1000, false, false,
	           new ArrayList<String>(), "BEBIDA");
	       String idSugRechazo = cafe.getSugerencias()
	           .get(cafe.getSugerencias().size() - 1).getIdSugerencia();
	       cafe.rechazarSugerenciaPlato("admin", idSugRechazo);
	       System.out.println("RF Admin 6 OK - Sugerencia rechazada");
	   } catch (Exception e) {
	       System.out.println("RF Admin 6 rechazar ERROR: " + e.getMessage());
	   }

	   // RF Admin - Marcar juego robado
	   try {
	       String idJuegoRobado = null;
	       for (Map.Entry<String, JuegoMesaPrestamo> entry : cafe.getJuegosPrestamo().entrySet()) {
	           if (entry.getValue().getNombre().equals("UNO")) { idJuegoRobado = entry.getKey(); break; }
	       }
	       cafe.marcarJuegoRobado("admin", idJuegoRobado);
	       System.out.println("RF Admin OK - Juego marcado como robado. Estado: "
	           + cafe.getJuegosPrestamo().get(idJuegoRobado).getEstado());
	   } catch (Exception e) {
	       System.out.println("RF Admin robado ERROR: " + e.getMessage());
	   }

	   // RF Admin - Reabastecer stock
	   try {
	       String idReab = null;
	       for (Map.Entry<String, JuegoMesaVenta> entry : cafe.getJuegosVenta().entrySet()) {
	           idReab = entry.getKey(); break;
	       }
	       int stockAntes = cafe.getJuegosVenta().get(idReab).getCantidadStock();
	       cafe.reabastecerJuegoVenta("admin", idReab, 5);
	       int stockDespues = cafe.getJuegosVenta().get(idReab).getCantidadStock();
	       System.out.println("RF Admin OK - Reabastecimiento. Antes: " + stockAntes + " Después: " + stockDespues);
	   } catch (Exception e) {
	       System.out.println("RF Admin reabastecer ERROR: " + e.getMessage());
	   }

	   // RF Admin 4 - Eliminar juego venta
	   try {
	       
	       cafe.agregarJuegoVenta("admin", "JuegoParaBorrar", 2020, "Ed", "Cartas",
	           2, 4, false, true, true, 10000, 1, 5000);
	       String idBorrar = null;
	       for (Map.Entry<String, JuegoMesaVenta> e : cafe.getJuegosVenta().entrySet()) {
	           if (e.getValue().getNombre().equals("JuegoParaBorrar")) {
	               idBorrar = e.getKey(); break;
	           }
	       }
	       cafe.eliminarJuegoVenta("admin", idBorrar);
	       System.out.println("RF Admin 4 OK - Juego venta eliminado");
	   } catch (Exception e) {
	       System.out.println("RF Admin 4 eliminar ERROR: " + e.getMessage());
	   }
	   
	   //PRUEBAS TORNEO
	   
	   // ---- Torneos crear e inscribir
	   try {
	       String idJuegoTorneo = null;

	       for (Map.Entry<String, JuegoMesaPrestamo> entry : cafe.getJuegosPrestamo().entrySet()) {
	           if (entry.getValue().getNombre().equals("UNO")) {
	               idJuegoTorneo = entry.getKey();
	               break;
	           }
	       }

	       JuegoMesaPrestamo juego = cafe.getJuegosPrestamo().get(idJuegoTorneo);

	       Torneo torneo = new TorneoAmistoso("TOR_TEST", 5, juego, DayOfWeek.FRIDAY, 0.1);
	       cafe.agregarTorneo(torneo);

	       System.out.println("RF Torneo OK - Torneo creado");

	       Usuario cliente = cafe.getUsuarios().get("cliente1");
	       torneo.inscribirUsuario(cliente, 2);

	       System.out.println("RF Torneo OK - Usuario inscrito");

	   } catch (Exception e) {
	       System.out.println("RF Torneo ERROR: " + e.getMessage());
	   }
	   
	// ---- Torneos bloqueo por cupos
	   try {
	       Usuario cliente = cafe.getUsuarios().get("cliente1");

	       Torneo torneo = cafe.getTorneos().get(cafe.getTorneos().size() - 1);

	       torneo.inscribirUsuario(cliente, 100);

	       System.out.println("RF Torneo ERROR - Debió bloquear por cupos");

	   } catch (Exception e) {
	       System.out.println("RF Torneo OK - Bloqueo por cupos: " + e.getMessage());
	   }
	   
	// ---- Torneos carga desde persistencia
	   try {
	       System.out.println("TORNEOS CARGADOS ");

	       for (Torneo t : cafe.getTorneos()) {
	           System.out.println("ID: " + t.getId());
	           System.out.println("Juego: " + t.getJuego().getNombre());
	           System.out.println("Dia: " + t.getDia());
	           System.out.println("Fanaticos: " + t.getInscritosFanaticos());
	           System.out.println("Regulares: " + t.getInscritosRegulares());
	       }

	       System.out.println("RF Torneos OK - Carga correcta");

	   } catch (Exception e) {
	       System.out.println("RF Torneos ERROR: " + e.getMessage());
	   }
	   
	// ============================================
	   //               PRUEBAS DE TORNEOS
	   // ============================================

	   // ---- T01 - Crear torneo amistoso ----
	   String idJuegoTorneoAmistoso = null;
	   try {
	       for (Map.Entry<String, JuegoMesaPrestamo> entry : cafe.getJuegosPrestamo().entrySet()) {
	           if (entry.getValue().getNombre().equals("Catan")) {
	               idJuegoTorneoAmistoso = entry.getKey(); break;
	           }
	       }
	       TorneoAmistoso ta = cafe.crearTorneoAmistoso("admin", idJuegoTorneoAmistoso, 4, DayOfWeek.SATURDAY, 0.15);
	       System.out.println("T01 OK - Torneo amistoso creado: " + ta.getId() 
	           + " | cuposFan: " + ta.getCuposFanaticos() + " | cuposReg: " + ta.getCuposRegulares());
	   } catch (Exception e) {
	       System.out.println("T01 ERROR: " + e.getMessage());
	   }

	   // ---- T02 - Crear torneo competitivo ----
	   String idJuegoTorneoCompet = null;
	   try {
	       cafe.agregarJuegoPrestamo("admin", "Carcassonne", 2000, "Z-Man", "Tablero",
	           2, 5, false, true, true, true, 0, "Nuevo");
	       cafe.agregarJuegoPrestamo("admin", "Carcassonne", 2000, "Z-Man", "Tablero",
	           2, 5, false, true, true, true, 0, "Nuevo");
	       for (Map.Entry<String, JuegoMesaPrestamo> entry : cafe.getJuegosPrestamo().entrySet()) {
	           if (entry.getValue().getNombre().equals("Carcassonne")) {
	               idJuegoTorneoCompet = entry.getKey(); break;
	           }
	       }
	       TorneoCompetitivo tc = cafe.crearTorneoCompetitivo("admin", idJuegoTorneoCompet, 8, DayOfWeek.SUNDAY, 20000);
	       System.out.println("T02 OK - Torneo competitivo creado: " + tc.getId()
	           + " | tarifa: " + tc.getTarifa() + " | cuposMax: 2 copias x 5 jug = 10");
	   } catch (Exception e) {
	       System.out.println("T02 ERROR: " + e.getMessage());
	   }

	   // ---- T03 - Crear torneo con cupos insuficientes ----
	   try {
	       cafe.crearTorneoAmistoso("admin", idJuegoTorneoAmistoso, 100, DayOfWeek.SATURDAY, 0.10);
	       System.out.println("T03 ERROR - Debió bloquear por copias insuficientes");
	   } catch (Exception e) {
	       System.out.println("T03 OK - Bloqueo por cupos: " + e.getMessage());
	   }

	   // ---- T04 - Crear competitivo con tarifa cero ----
	   try {
	       cafe.crearTorneoCompetitivo("admin", idJuegoTorneoCompet, 4, DayOfWeek.SUNDAY, 0);
	       System.out.println("T04 ERROR - Debió bloquear tarifa <= 0");
	   } catch (Exception e) {
	       System.out.println("T04 OK - Bloqueo tarifa cero: " + e.getMessage());
	   }

	   // ---- T05 - Inscribir cliente regular (sin ser fanático) ----
	   try {
	       cafe.registrarCliente("torneoCli1", "1234");
	       cafe.registrarCliente("torneoCli2", "1234");
	       cafe.registrarCliente("torneoCli3", "1234");
	       cafe.inscribirUsuarioTorneo("torneoCli1", "TA1", 2);
	       Torneo t = cafe.getTorneos().get(0);
	       System.out.println("T05 OK - Cliente regular inscrito | cuposReg restantes: " 
	           + t.getCuposRegulares() + " | cuposFan: " + t.getCuposFanaticos());
	   } catch (Exception e) {
	       System.out.println("T05 ERROR: " + e.getMessage());
	   }

	   // ---- T06 - Inscribir fanático toma cupo fanático ----
	   try {
	       cafe.agregarJuegoFavoritoAUsuario("torneoCli2", idJuegoTorneoAmistoso);
	       cafe.inscribirUsuarioTorneo("torneoCli2", "TA1", 1);
	       Torneo t = cafe.getTorneos().get(0);
	       System.out.println("T06 OK - Fanático inscrito en cupo fanático | cuposFan: " 
	           + t.getCuposFanaticos() + " | inscritosFan: " + t.getInscritosFanaticos());
	   } catch (Exception e) {
	       System.out.println("T06 ERROR: " + e.getMessage());
	   }

	   // ---- T07 - Inscribir más de 3 cupos por usuario ----
	   try {
	       cafe.inscribirUsuarioTorneo("torneoCli3", "TA1", 5);
	       System.out.println("T07 ERROR - Debió bloquear por límite de 3 cupos");
	   } catch (Exception e) {
	       System.out.println("T07 OK - Bloqueo por límite 3 cupos: " + e.getMessage());
	   }

	   // ---- T08 - Inscribir mismo usuario dos veces ----
	   try {
	       cafe.inscribirUsuarioTorneo("torneoCli1", "TA1", 1);
	       System.out.println("T08 ERROR - Debió bloquear doble inscripción");
	   } catch (Exception e) {
	       System.out.println("T08 OK - Bloqueo doble inscripción: " + e.getMessage());
	   }

	   // ---- T09 - Desinscribir libera cupos ----
	   try {
	       Torneo t = cafe.getTorneos().get(0);
	       int regAntes = t.getCuposRegulares();
	       cafe.desinscribirUsuarioTorneo("torneoCli1", "TA1");
	       System.out.println("T09 OK - Cliente desinscrito | cuposReg antes: " 
	           + regAntes + " | después: " + t.getCuposRegulares());
	   } catch (Exception e) {
	       System.out.println("T09 ERROR: " + e.getMessage());
	   }

	   // ---- T10 - Empleado sin turno ese día puede inscribirse ----
	   try {
	       cafe.inscribirUsuarioTorneo("mesero1", "TA1", 1);
	       Torneo t = cafe.getTorneos().get(0);
	       System.out.println("T10 OK - Empleado inscrito (sin turno sábado) | empleadosInscritos: " 
	           + t.getEmpleadosInscritos());
	   } catch (Exception e) {
	       System.out.println("T10 ERROR: " + e.getMessage());
	   }

	   // ---- T11 - Empleado con turno ese día NO puede inscribirse ----
	   try {
	       cafe.registrarMesero("admin", "meseroSabado", "1234");
	       Empleado meseroSab = (Empleado) cafe.getUsuarios().get("meseroSabado");
	       Turno turnoSab = new Turno("TSAB",
	           LocalDateTime.of(2026, 5, 2, 8, 0),
	           LocalDateTime.of(2026, 5, 2, 16, 0),
	           "SABADO", meseroSab);
	       cafe.agregarTurno("admin", "meseroSabado", turnoSab);
	       cafe.inscribirUsuarioTorneo("meseroSabado", "TA1", 1);
	       System.out.println("T11 ERROR - Debió bloquear, empleado tiene turno sábado");
	   } catch (Exception e) {
	       System.out.println("T11 OK - Bloqueo empleado con turno: " + e.getMessage());
	   }

	   // ---- T12 - Admin no puede inscribirse ----
	   try {
	       cafe.inscribirUsuarioTorneo("admin", "TA1", 1);
	       System.out.println("T12 ERROR - Debió bloquear admin");
	   } catch (Exception e) {
	       System.out.println("T12 OK - Bloqueo admin: " + e.getMessage());
	   }

	// ---- T13 - Premio competitivo no cuenta empleados ----
	   try {
	       cafe.inscribirUsuarioTorneo("torneoCli1", "TC1", 2);
	       cafe.inscribirUsuarioTorneo("torneoCli3", "TC1", 1);
	       cafe.inscribirUsuarioTorneo("mesero2", "TC1", 1);
	       
	       TorneoCompetitivo tc = null;
	       for (Torneo t : cafe.getTorneos()) {
	           if (t.getId().equals("TC1")) {
	               tc = (TorneoCompetitivo) t;
	               break;
	           }
	       }
	       
	       double premio = tc.getPremio();
	       System.out.println("T13 OK - Premio competitivo: " + premio + " (esperado: 60000)");
	   } catch (Exception e) {
	       System.out.println("T13 ERROR: " + e.getMessage());
	   }

	   // ---- T14 - Otorgar premio amistoso al ganador ----
	   try {
	       cafe.otorgarPremioTorneoAmistoso("admin", "TA1", "torneoCli2");
	       Cliente ganador = (Cliente) cafe.getUsuarios().get("torneoCli2");
	       System.out.println("T14 OK - Premio otorgado | descuento: " 
	           + ganador.getPorcentajeDescuentoTorneo());
	   } catch (Exception e) {
	       System.out.println("T14 ERROR: " + e.getMessage());
	   }

	   // ---- T15 - Otorgar premio a cliente con descuento activo ----
	   try {
	       cafe.otorgarPremioTorneoAmistoso("admin", "TA1", "torneoCli2");
	       System.out.println("T15 ERROR - Debió bloquear, ya tenía descuento");
	   } catch (Exception e) {
	       System.out.println("T15 OK - Bloqueo descuento acumulable: " + e.getMessage());
	   }

	   // ---- T16 - Otorgar premio a no inscrito ----
	   try {
	       cafe.otorgarPremioTorneoAmistoso("admin", "TA1", "torneoCli3");
	       System.out.println("T16 ERROR - Debió bloquear, no estaba inscrito");
	   } catch (Exception e) {
	       System.out.println("T16 OK - Bloqueo no inscrito: " + e.getMessage());
	   }

	   // ---- T17 - Otorgar premio en torneo competitivo ----
	   try {
	       cafe.otorgarPremioTorneoAmistoso("admin", "TC2", "torneoCli1");
	       System.out.println("T17 ERROR - Debió bloquear, no es amistoso");
	   } catch (Exception e) {
	       System.out.println("T17 OK - Bloqueo torneo competitivo: " + e.getMessage());
	   }

	   // ---- T18 - Comprar con descuento de torneo (consume el descuento) ----
	   try {
	       String idJuegoVentaTorneo = null;
	       for (Map.Entry<String, JuegoMesaVenta> entry : cafe.getJuegosVenta().entrySet()) {
	           if (entry.getValue().getCantidadStock() > 0) {
	               idJuegoVentaTorneo = entry.getKey(); break;
	           }
	       }
	       Cliente ganador = (Cliente) cafe.getUsuarios().get("torneoCli2");
	       double descAntes = ganador.getPorcentajeDescuentoTorneo();
	       VentaJuego v = cafe.comprarJuegoConDescuentoTorneo("torneoCli2", idJuegoVentaTorneo, 1);
	       double descDespues = ganador.getPorcentajeDescuentoTorneo();
	       System.out.println("T18 OK - Compra con descuento torneo | total: " + v.getTotal() 
	           + " | desc antes: " + descAntes + " | después: " + descDespues);
	   } catch (Exception e) {
	       System.out.println("T18 ERROR: " + e.getMessage());
	   }

	   // ---- T19 - Comprar con descuento torneo cuando no hay descuento ----
	   try {
	       String idJuegoVentaTorneo = null;
	       for (Map.Entry<String, JuegoMesaVenta> entry : cafe.getJuegosVenta().entrySet()) {
	           if (entry.getValue().getCantidadStock() > 0) {
	               idJuegoVentaTorneo = entry.getKey(); break;
	           }
	       }
	       cafe.comprarJuegoConDescuentoTorneo("torneoCli2", idJuegoVentaTorneo, 1);
	       System.out.println("T19 ERROR - Debió bloquear, descuento ya consumido");
	   } catch (Exception e) {
	       System.out.println("T19 OK - Bloqueo descuento ya consumido: " + e.getMessage());
	   }

	   // ---- T20 - Eliminar torneo ----
	   try {
	       int antes = cafe.getTorneos().size();
	       cafe.eliminarTorneo("admin", "TA1");
	       int despues = cafe.getTorneos().size();
	       System.out.println("T20 OK - Torneo eliminado | antes: " + antes + " | después: " + despues);
	   } catch (Exception e) {
	       System.out.println("T20 ERROR: " + e.getMessage());
	   }

	   // ---- T21 - Eliminar torneo inexistente ----
	   try {
	       cafe.eliminarTorneo("admin", "TA999");
	       System.out.println("T21 ERROR - Debió bloquear torneo inexistente");
	   } catch (Exception e) {
	       System.out.println("T21 OK - Bloqueo torneo inexistente: " + e.getMessage());
	   }
	   
        // ---- RF Admin 7  Informe 
        try {
            System.out.println("\nRF Admin 7 - Informe diario:");
            Informe inf = cafe.consultarInforme("admin", "diaria");
            System.out.println("  Ventas juegos: " + inf.totalJuegos + " | Impuestos: " + inf.impuestosJuegos);
            System.out.println("  Ventas comida: " + inf.totalComida + " | Impuestos: " + inf.impuestosComida + " | Propinas: " + inf.propinasComida);
            System.out.println("RF Admin 7 OK");
        } catch (Exception e) {
            System.out.println("RF Admin 7 ERROR: " + e.getMessage());
        }
        //gestor.guardarTodo(cafe);
        
        System.out.println("\nPruebas finalizadas y datos guardados.");

        } catch (Exception e) {
            System.out.println("Error general: " + e.getMessage());
        }
    }
}