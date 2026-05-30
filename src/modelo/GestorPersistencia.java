package modelo;

import java.util.Map;
import juegos.*;
import mesas.*;
import pedidos.*;
import prestamos.*;
import productos.*;
import sugerencias.*;
import usuarios.*;
import ventas.*;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;
import juegos.JuegoMesaVenta;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDateTime;

import java.time.DayOfWeek;
import torneos.*;

public class GestorPersistencia {

    private String rutaArchivo;

    public GestorPersistencia(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }
    
    private Turno buscarTurnoPorId(Cafe cafe, String idTurno) {
        if (idTurno == null || idTurno.equals("null")) {
            return null;
        }

        for (Usuario u : cafe.getUsuarios().values()) {
            if (u instanceof Empleado) {
                Empleado emp = (Empleado) u;
                for (Turno t : emp.getTurnos()) {
                    if (t.getIdTurno().equals(idTurno)) {
                        return t;
                    }
                }
            }
        }

        return null;
    }
    
    private String mapaAString(Map<String, Integer> mapa) {
        String resultado = "";
        for (String login : mapa.keySet()) {
            if (!resultado.isEmpty()) {
                resultado += ",";
            }
            resultado += login + ":" + mapa.get(login);
        }
        return resultado;
    }

    private String listaAString(List<String> lista) {
        return String.join(",", lista);
    }

    private void cargarMapaInscritos(String texto, Torneo torneo, boolean fanaticos) {
        if (texto == null || texto.isEmpty()) {
            return;
        }

        String[] entradas = texto.split(",");
        for (String entrada : entradas) {
            String[] partes = entrada.split(":");
            String login = partes[0];
            int cupos = Integer.parseInt(partes[1]);

            if (fanaticos) {
                torneo.agregarInscritoFanatico(login, cupos);
            } else {
                torneo.agregarInscritoRegular(login, cupos);
            }
        }
    }
    
    // GUARDAR

    public void guardarTodo(Cafe cafe) throws Exception {
        PrintWriter pw = new PrintWriter(new FileWriter(this.rutaArchivo));


        pw.println("[administradores]");
        for (Usuario u : cafe.getUsuarios().values()) {
            if (u instanceof Administrador) {
                pw.println(u.toString());
            }
        }

        pw.println("[clientes]");
        for (Usuario u : cafe.getUsuarios().values()) {
            if (u instanceof Cliente) {
                pw.println(u.toString());
            }
        }

        pw.println("[meseros]");
        for (Usuario u : cafe.getUsuarios().values()) {
            if (u instanceof Mesero) {
                pw.println(u.toString());
            }
        }

        pw.println("[cocineros]");
        for (Usuario u : cafe.getUsuarios().values()) {
            if (u instanceof Cocinero) {
                pw.println(u.toString());
            }
        }

        pw.println("[juegosPrestamo]");
        for (JuegoMesaPrestamo j : cafe.getJuegosPrestamo().values()) {
            pw.println(j.toString());
        }

        pw.println("[juegosVenta]");
        for (JuegoMesaVenta j : cafe.getJuegosVenta().values()) {
            pw.println(j.toString());
        }
        
        pw.println("[menu]");
        for (ProductoMenu p : cafe.getMenu().values()) {
            pw.println(p.toString());
        }

        pw.println("[mesas]");
        for (Mesa m : cafe.getMesas()) {
            pw.println(m.toString());
        }

        pw.println("[prestamos]");
        for (Prestamo p : cafe.getPrestamos()) {
            pw.println(p.toString());
        }

        pw.println("[ventas]");
        for (VentaJuego v : cafe.getVentas()) {
            pw.println(v.toString());
        }

        pw.println("[pedidos]");
        for (Pedido p : cafe.getPedidos()) {
            pw.println(p.toString());
        }
        
        pw.println("[turnos]");
        for (Usuario u : cafe.getUsuarios().values()) {
            if (u instanceof Empleado) {
                for (Turno t : ((Empleado) u).getTurnos()) {
                    pw.println(t.toString());
                }
            }
        }
        pw.println("[sugerencias]");
        for (SugerenciaPlato s : cafe.getSugerencias()) {
            pw.println(s.toString());
        }

        pw.println("[solicitudes]");
        for (SolicitudCambioTurno s : cafe.getSolicitudesCambioTurno()) {
            pw.println(s.toString());
        }
        
        pw.println("[torneos]");
        for (Torneo t : cafe.getTorneos()) {
            String tipo = "";
            double valor = 0;

            if (t instanceof TorneoAmistoso) {
                tipo = "AMISTOSO";
                valor = ((TorneoAmistoso) t).getPremioDescuento();
            } else if (t instanceof TorneoCompetitivo) {
                tipo = "COMPETITIVO";
                valor = ((TorneoCompetitivo) t).getTarifa();
            }

            pw.println(
                "id\t" + t.getId()
                + "|tipo\t" + tipo
                + "|capacidad\t" + t.getCapacidad()
                + "|idJuego\t" + t.getJuego().getIdJuegoPrestamo()
                + "|dia\t" + t.getDia()
                + "|valor\t" + valor
                + "|cuposFanaticos\t" + t.getCuposFanaticos()
                + "|cuposRegulares\t" + t.getCuposRegulares()
                + "|inscritosFanaticos\t" + mapaAString(t.getInscritosFanaticos())
                + "|inscritosRegulares\t" + mapaAString(t.getInscritosRegulares())
                + "|empleadosInscritos\t" + listaAString(t.getEmpleadosInscritos())
            );
        }
        
        pw.close();
    }

    // Escrinir
    


    public void cargarTodo(Cafe cafe) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(this.rutaArchivo));
        String linea;
        String seccionActual = "";

        while ((linea = br.readLine()) != null) {
        	
            try {
                
            
            if (linea.startsWith("[")) {
                seccionActual = linea.trim();
            } else if (!linea.trim().isEmpty()) {
                String[] p = linea.split("\\|");

                if (seccionActual.equals("[administradores]")) {
                    String login = p[0].split("\t")[1];
                    String contrasena = p[1].split("\t")[1];
                    cafe.getUsuarios().put(login, new Administrador(login, contrasena));

                } else if (seccionActual.equals("[clientes]")) {
                    String login = p[0].split("\t")[1];
                    String contrasena = p[1].split("\t")[1];
                    int puntos = Integer.parseInt(p[2].split("\t")[1]);
                    Cliente c = new Cliente(login, contrasena);
                    c.agregarPuntos(puntos);
                    if (p.length > 3) {
                        double descTorneo = Double.parseDouble(p[3].split("\t")[1]);
                        if (descTorneo > 0) c.otorgarDescuentoTorneo(descTorneo);
                    }
                    cafe.getUsuarios().put(login, c);

                } else if (seccionActual.equals("[meseros]")) {
                    String login = p[0].split("\t")[1];
                    String contrasena = p[1].split("\t")[1];
                    Mesero mesero = new Mesero(login, contrasena);
                    if (p.length > 2) {
                        String[] parteConocidos = p[2].split("\t");
                        if (parteConocidos.length > 1 && !parteConocidos[1].isEmpty()) {
                            for (String idJuego : parteConocidos[1].split(",")) {
                                if (!idJuego.isEmpty()) {
                                    mesero.agregarJuegoDificil(idJuego);
                                }
                            }
                        }
                    }
                    cafe.getUsuarios().put(login, mesero);
                } else if (seccionActual.equals("[cocineros]")) {
                    String login = p[0].split("\t")[1];
                    String contrasena = p[1].split("\t")[1];
                    cafe.getUsuarios().put(login, new Cocinero(login, contrasena));
                } else if (seccionActual.equals("[turnos]")) {
                    String idTurno = p[0].split("\t")[1];
                    LocalDateTime inicio = LocalDateTime.parse(p[1].split("\t")[1]);
                    LocalDateTime fin = LocalDateTime.parse(p[2].split("\t")[1]);
                    String dia = p[3].split("\t")[1];
                    String loginEmpleado = p[4].split("\t")[1];
                    Empleado emp = (Empleado) cafe.getUsuarios().get(loginEmpleado);
                    emp.asignarTurno(new Turno(idTurno, inicio, fin, dia, emp));
                } else if (seccionActual.equals("[juegosPrestamo]")) {
                    String id = p[0].split("\t")[1];
                    String nombre = p[1].split("\t")[1];
                    int anio = Integer.parseInt(p[2].split("\t")[1]);
                    String editor = p[3].split("\t")[1];
                    String categoria = p[4].split("\t")[1];
                    int min = Integer.parseInt(p[5].split("\t")[1]);
                    int max = Integer.parseInt(p[6].split("\t")[1]);
                    boolean dificil = Boolean.parseBoolean(p[7].split("\t")[1]);
                    boolean menores5 = Boolean.parseBoolean(p[8].split("\t")[1]);
                    boolean menores18 = Boolean.parseBoolean(p[9].split("\t")[1]);
                    boolean disponible = Boolean.parseBoolean(p[10].split("\t")[1]);
                    int veces = Integer.parseInt(p[11].split("\t")[1]);
                    String estado = p[12].split("\t")[1];
                    cafe.getJuegosPrestamo().put(id, new JuegoMesaPrestamo(nombre, anio, editor, categoria, min, max, dificil, menores5, menores18, id, disponible, veces, estado));

                } else if (seccionActual.equals("[juegosVenta]")) {
                    String id = p[0].split("\t")[1];
                    String nombre = p[1].split("\t")[1];
                    int anio = Integer.parseInt(p[2].split("\t")[1]);
                    String editor = p[3].split("\t")[1];
                    String categoria = p[4].split("\t")[1];
                    int min = Integer.parseInt(p[5].split("\t")[1]);
                    int max = Integer.parseInt(p[6].split("\t")[1]);
                    boolean dificil = Boolean.parseBoolean(p[7].split("\t")[1]);
                    boolean menores5 = Boolean.parseBoolean(p[8].split("\t")[1]);
                    boolean menores18 = Boolean.parseBoolean(p[9].split("\t")[1]);
                    double precio = Double.parseDouble(p[10].split("\t")[1]);
                    int stock = Integer.parseInt(p[11].split("\t")[1]);
                    double costo = Double.parseDouble(p[12].split("\t")[1]);
                    cafe.getJuegosVenta().put(id, new JuegoMesaVenta(nombre, anio, editor, categoria, min, max, dificil, menores5, menores18, id, precio, stock, costo));
              
                
                    

                } 
                else if (seccionActual.equals("[menu]")) {

                    String id = p[0].split("\t")[1];
                    String nombre = p[1].split("\t")[1];
                    double precio = Double.parseDouble(p[2].split("\t")[1]);
                    boolean disponible = Boolean.parseBoolean(p[3].split("\t")[1]);

                    if (id.startsWith("B")) {

                        boolean alcoholica = false;
                        boolean caliente = false;

                        if (p.length > 4)
                            alcoholica = Boolean.parseBoolean(p[4].split("\t")[1]);

                        if (p.length > 5)
                            caliente = Boolean.parseBoolean(p[5].split("\t")[1]);

                        cafe.agregarProductoMenu(
                            new Bebida(id, nombre, precio, disponible, alcoholica, caliente)
                        );

                    } else {

                        List<String> alerg = new ArrayList<>();

                        if (p.length > 4) {
                            String[] datos = p[4].split("\t");
                            if (datos.length > 1 && !datos[1].isEmpty()) {
                                for (String a : datos[1].split(",")) {
                                    alerg.add(a);
                                }
                            }
                        }

                        cafe.agregarProductoMenu(
                            new Pasteleria(id, nombre, precio, disponible, alerg)
                        );
                    }
                }
                
                else if (seccionActual.equals("[mesas]")) {
                    String id = p[0].split("\t")[1];
                    int capacidad = Integer.parseInt(p[1].split("\t")[1]);
                    Mesa m = new Mesa(id, capacidad);
                    if (p.length > 2) {
                        boolean ocupada = Boolean.parseBoolean(p[2].split("\t")[1]);
                        int personas = Integer.parseInt(p[3].split("\t")[1]);
                        boolean jovenes = Boolean.parseBoolean(p[4].split("\t")[1]);
                        boolean ninos = Boolean.parseBoolean(p[5].split("\t")[1]);
                        int juegosAct = Integer.parseInt(p[6].split("\t")[1]);
                        boolean bebidaCaliente = Boolean.parseBoolean(p[7].split("\t")[1]);
                        boolean juegoAccion = Boolean.parseBoolean(p[8].split("\t")[1]);
                        String loginOcupante = p[9].split("\t")[1];

                        Cliente ocupante = null;
                        if (!loginOcupante.equals("null")) {
                            Usuario u = cafe.getUsuarios().get(loginOcupante);
                            if (u instanceof Cliente) ocupante = (Cliente) u;
                        }
                        if (ocupada) {
                            m.asignarMesa(personas, jovenes, ninos, ocupante);
                        }
                        for (int i = 0; i < juegosAct; i++) m.nuevoJuegoPrestado();
                        m.setTieneBebidaCaliente(bebidaCaliente);
                        m.setTieneJuegoAccion(juegoAccion);
                    }
                    cafe.agregarMesa(m);

                } else if (seccionActual.equals("[prestamos]")) {
                    String id = p[0].split("\t")[1];
                    String fechaPrestamo = p[1].split("\t")[1];
                    String fechaDevolucion = p[2].split("\t")[1];
                    boolean explicado = Boolean.parseBoolean(p[3].split("\t")[1]);
                    String idJuego = p[4].split("\t")[1];
                    String loginUsuario = p[5].split("\t")[1];
                    String idMesa = p[6].split("\t")[1];

                    JuegoMesaPrestamo juego = cafe.getJuegosPrestamo().get(idJuego);
                    Usuario usuario = cafe.getUsuarios().get(loginUsuario);
                    Mesa mesa = null;
                    
                    if (!idMesa.equals("null")) {
                        for (Mesa mm : cafe.getMesas()) {
                            if (mm.getIdMesa().equals(idMesa)) { mesa = mm; break; }
                        }
                    }
                    Prestamo prestamo = new Prestamo(id, fechaPrestamo, explicado, juego, usuario, mesa);
                    if (!fechaDevolucion.equals("null")) {
                        prestamo.registrarDevolucion(fechaDevolucion);
                    }
                    
                    cafe.getPrestamos().add(prestamo);

                } else if (seccionActual.equals("[ventas]")) {
                    String id = p[0].split("\t")[1];
                    String fecha = p[1].split("\t")[1];
                    String loginComprador = p[2].split("\t")[1];
                    double subTotal = Double.parseDouble(p[3].split("\t")[1]);
                    double impuesto = Double.parseDouble(p[4].split("\t")[1]);
                    double total = Double.parseDouble(p[5].split("\t")[1]);
                    int puntos = Integer.parseInt(p[6].split("\t")[1]);
                    Usuario comprador = cafe.getUsuarios().get(loginComprador);
                    VentaJuego venta = new VentaJuego(id, fecha, comprador);
                    venta.setSubTotal(subTotal);
                    venta.setImpuesto(impuesto);
                    venta.setTotal(total);
                    venta.setPuntosGenerados(puntos);
                    cafe.getVentas().add(venta);

                } else if (seccionActual.equals("[pedidos]")) {
                    String id = p[0].split("\t")[1];
                    String fecha = p[1].split("\t")[1];
                    String idMesa = p[2].split("\t")[1];
                    double subtotal = Double.parseDouble(p[3].split("\t")[1]);
                    double impuesto = Double.parseDouble(p[4].split("\t")[1]);
                    double propina = Double.parseDouble(p[5].split("\t")[1]);
                    double total = Double.parseDouble(p[6].split("\t")[1]);
                    String estado = p[7].split("\t")[1];
                    Mesa mesa = null;
                    for (Mesa m : cafe.getMesas()) {
                        if (m.getIdMesa().equals(idMesa)) {
                            mesa = m;
                            break;
                        }
                    }
                    Pedido pedido = new Pedido(id, fecha, mesa);
                    pedido.setSubtotal(subtotal);
                    pedido.setImpuestoConsumo(impuesto);
                    pedido.setPropina(propina);
                    pedido.setTotal(total);
                    pedido.setEstado(estado);
                    cafe.getPedidos().add(pedido);
                } else if (seccionActual.equals("[sugerencias]")) {
                    String id = p[0].split("\t")[1];
                    String nombre = p[1].split("\t")[1];
                    LocalDateTime fecha = LocalDateTime.parse(p[2].split("\t")[1]);
                    String estado = p[3].split("\t")[1];
                    String loginEmp = p[4].split("\t")[1];
                    double precio = Double.parseDouble(p[5].split("\t")[1]);
                    boolean alc = Boolean.parseBoolean(p[6].split("\t")[1]);
                    boolean cal = Boolean.parseBoolean(p[7].split("\t")[1]);
                    String[] alergTokens = p[8].split("\t");
                    List<String> alerg = new ArrayList<>();
                    if (alergTokens.length > 1 && !alergTokens[1].isEmpty()) {
                        for (String a : alergTokens[1].split(",")) alerg.add(a);
                    }
                    String tipo = p[9].split("\t")[1];
                    Empleado emp = (Empleado) cafe.getUsuarios().get(loginEmp);
                    SugerenciaPlato sug = new SugerenciaPlato(id, nombre, fecha, estado, emp,
                            precio, alc, cal, alerg, tipo);
                    cafe.getSugerencias().add(sug);
                }else if (seccionActual.equals("[solicitudes]")) {
                    String id = p[0].split("\t")[1];
                    String tipo = p[1].split("\t")[1];
                    LocalDateTime fecha = LocalDateTime.parse(p[2].split("\t")[1]);
                    boolean aprobada = Boolean.parseBoolean(p[3].split("\t")[1]);
                    String loginSol = p[4].split("\t")[1];
                    String idTurnoSol = p[5].split("\t")[1];
                    String loginDest = p[6].split("\t")[1];
                    String idTurnoDest = p[7].split("\t")[1];

                    Empleado sol = (Empleado) cafe.getUsuarios().get(loginSol);
                    Empleado dest = loginDest.equals("null") ? null
                            : (Empleado) cafe.getUsuarios().get(loginDest);
                    Turno turnoSol = buscarTurnoPorId(cafe, idTurnoSol);

                    Turno turnoDest = null;
                    if (dest != null && !idTurnoDest.equals("null")) {
                        turnoDest = buscarTurnoPorId(cafe, idTurnoDest);
                    }
                    SolicitudCambioTurno sc = new SolicitudCambioTurno(id, tipo, fecha, sol, dest,
                            turnoSol, turnoDest);
                    if (aprobada) sc.aprobar();
                    cafe.getSolicitudesCambioTurno().add(sc);
                }else if (seccionActual.equals("[torneos]")) {
                    String id = p[0].split("\t")[1];
                    String tipo = p[1].split("\t")[1];
                    int capacidad = Integer.parseInt(p[2].split("\t")[1]);
                    String idJuego = p[3].split("\t")[1];
                    DayOfWeek dia = DayOfWeek.valueOf(p[4].split("\t")[1]);
                    double valor = Double.parseDouble(p[5].split("\t")[1]);
                    int cuposFanaticos = Integer.parseInt(p[6].split("\t")[1]);
                    int cuposRegulares = Integer.parseInt(p[7].split("\t")[1]);

                    JuegoMesaPrestamo juego = cafe.getJuegosPrestamo().get(idJuego);

                    Torneo torneo;
                    if (tipo.equals("AMISTOSO")) {
                        torneo = new TorneoAmistoso(id, capacidad, juego, dia, valor);
                    } else {
                        torneo = new TorneoCompetitivo(id, capacidad, juego, dia, valor);
                    }

                    torneo.setCuposFanaticos(cuposFanaticos);
                    torneo.setCuposRegulares(cuposRegulares);

                    String inscritosFanaticos = p[8].split("\t").length > 1 ? p[8].split("\t")[1] : "";
                    cargarMapaInscritos(inscritosFanaticos, torneo, true);

                    String inscritosRegulares = p[9].split("\t").length > 1 ? p[9].split("\t")[1] : "";
                    cargarMapaInscritos(inscritosRegulares, torneo, false);

                    String empleados = p[10].split("\t").length > 1 ? p[10].split("\t")[1] : "";
                    if (!empleados.isEmpty()) {
                        for (String login : empleados.split(",")) {
                            torneo.agregarEmpleadoInscrito(login);
                        }
                    }

                    cafe.agregarTorneo(torneo);
                }
                
                
            }
	        } catch (Exception e) {
	            System.out.println("Error linea: [" + linea + "] - " + e.getMessage());
	            throw e;
	        }
        }
        cafe.sincronizarConsecutivos();
        br.close();
    }
}