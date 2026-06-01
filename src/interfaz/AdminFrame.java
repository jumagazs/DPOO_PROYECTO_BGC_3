package interfaz;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDateTime;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import modelo.Cafe;
import modelo.GestorPersistencia;
import modelo.Informe;

import juegos.JuegoMesaPrestamo;
import juegos.JuegoMesaVenta;
import prestamos.Prestamo;
import usuarios.Empleado;
import usuarios.Turno;

public class AdminFrame extends JFrame {

    private Cafe cafe;
    private String login;
    private GestorPersistencia gp;

    public AdminFrame(Cafe cafe, String login, GestorPersistencia gp) {
        this.cafe = cafe;
        this.login = login;
        this.gp = gp;

        setTitle("Admin - " + login);
        setSize(620, 480);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        addWindowListener(new CierreVentana(cafe, gp)); 

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Personal", panelPersonal());
        tabs.addTab("Inv. prestamo", panelInventarioPrestamo());
        tabs.addTab("Inv. venta", panelInventarioVenta());
        tabs.addTab("Menu", panelMenu());
        tabs.addTab("Torneos", panelTorneos());
        tabs.addTab("Informes/Graficas", panelInformes());

        add(tabs);
        setVisible(true);
    }

    private JPanel grid() { return new JPanel(new GridLayout(0, 1, 8, 8)); }

    private boolean confirmar(String msg) {
        return JOptionPane.showConfirmDialog(this, msg, "Confirmar",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }

    private void mostrarTexto(String texto, String titulo) {
        JTextArea area = new JTextArea(texto, 20, 45);
        area.setEditable(false);
        JOptionPane.showMessageDialog(this, new JScrollPane(area), titulo,
                JOptionPane.INFORMATION_MESSAGE);
    }

    private JPanel panelPersonal() {
        JPanel p = grid();

        JButton bMesero = new JButton("Registrar mesero");
        JButton bCocinero = new JButton("Registrar cocinero");
        JButton bTurno = new JButton("Asignar turno");
        JButton bAprobar = new JButton("Aprobar cambio de turno");
        JButton bRechazar = new JButton("Rechazar cambio de turno");
        JButton bJuegoDificil = new JButton("Agregar juego dificil a mesero");

        p.add(bMesero);
        p.add(bCocinero);
        p.add(bTurno);
        p.add(bAprobar); 
        p.add(bRechazar); 
        p.add(bJuegoDificil);

        bMesero.addActionListener(e -> {
            try {
                String l = JOptionPane.showInputDialog("Login mesero:");
                String c = JOptionPane.showInputDialog("Contrasena:");
                cafe.registrarMesero(login, l, c);
                JOptionPane.showMessageDialog(this, "Mesero registrado.");
            } catch (Exception ex) { 
            	JOptionPane.showMessageDialog(this, ex.getMessage());
            	}
        });

        bCocinero.addActionListener(e -> {
            try {
                String l = JOptionPane.showInputDialog("Login cocinero:");
                String c = JOptionPane.showInputDialog("Contrasena:");
                cafe.registrarCocinero(login, l, c);
                JOptionPane.showMessageDialog(this, "Cocinero registrado.");
            } catch (Exception ex) {
            	JOptionPane.showMessageDialog(this, ex.getMessage());
            	}
        });

        bTurno.addActionListener(e -> {
            try {
                String idEmpleado = JOptionPane.showInputDialog("Login empleado:");
                String dia = JOptionPane.showInputDialog("Dia (LUNES/MARTES/...):");
                LocalDateTime inicio = LocalDateTime.parse(
                        JOptionPane.showInputDialog("Hora inicio (yyyy-MM-ddTHH:mm):"));
                LocalDateTime fin = LocalDateTime.parse(
                        JOptionPane.showInputDialog("Hora fin (yyyy-MM-ddTHH:mm):"));
                cafe.agregarTurno(login, idEmpleado, dia,inicio,fin);
                JOptionPane.showMessageDialog(this, "Turno asignado.");
            } catch (Exception ex) { 
            	JOptionPane.showMessageDialog(this, ex.getMessage());
            	}
        });

        bAprobar.addActionListener(e -> {
            try {
                String id = JOptionPane.showInputDialog("ID solicitud:");
                cafe.aprobarCambioTurno(login, id);
                JOptionPane.showMessageDialog(this, "Solicitud aprobada.");
            } catch (Exception ex) { 
            	JOptionPane.showMessageDialog(this, ex.getMessage()); 
            	}
        });

        bRechazar.addActionListener(e -> {
            try {
                String id = JOptionPane.showInputDialog("ID solicitud:");
                cafe.rechazarCambioTurno(login, id);
                JOptionPane.showMessageDialog(this, "Solicitud rechazada.");
            } catch (Exception ex) {
            	JOptionPane.showMessageDialog(this, ex.getMessage());
            	}
        });

        bJuegoDificil.addActionListener(e -> {
            try {
                String idMesero = JOptionPane.showInputDialog("Login mesero:");
                String idJuego = JOptionPane.showInputDialog("ID juego:");
                cafe.agregarJuegoConocidoMesero(login, idMesero, idJuego);
                JOptionPane.showMessageDialog(this, "Juego conocido agregado al mesero.");
            } catch (Exception ex) {
            	JOptionPane.showMessageDialog(this, ex.getMessage());
            	}
        });

        return p;
    }

    // INVENTARIO PRESTAMO 
    private JPanel panelInventarioPrestamo() {
        JPanel p = grid();

        JButton bAgregar = new JButton("Agregar juego prestamo");
        JButton bEstado = new JButton("Cambiar estado juego");
        JButton bEliminar = new JButton("Eliminar juego prestamo");
        JButton bRobado = new JButton("Marcar juego como robado");
        JButton bReparar = new JButton("Reparar juego");
        JButton bMover = new JButton("Mover juego venta a prestamo");
        JButton bVer = new JButton("Ver inventario detallado");

        p.add(bAgregar);
        p.add(bEstado); 
        p.add(bEliminar);
        p.add(bRobado);
        p.add(bReparar); 
        p.add(bMover); 
        p.add(bVer);

        bAgregar.addActionListener(e -> {
            try {
                String nombre = JOptionPane.showInputDialog("Nombre:");
                int anio = Integer.parseInt(JOptionPane.showInputDialog("Anio publicacion:"));
                String editor = JOptionPane.showInputDialog("Editor:");
                String categoria = JOptionPane.showInputDialog("Categoria (Cartas/Tablero/Accion):");
                int min = Integer.parseInt(JOptionPane.showInputDialog("Min jugadores:"));
                int max = Integer.parseInt(JOptionPane.showInputDialog("Max jugadores:"));
                boolean dificil = confirmar("Es dificil?");
                boolean menores5 = confirmar("Juegan menores de 5?");
                boolean menores18 = confirmar("Juegan menores de 18?");
                String estado = JOptionPane.showInputDialog("Estado (Nuevo/Bueno/Falta una pieza):");
                cafe.agregarJuegoPrestamo(login, nombre, anio, editor, categoria, min, max,
                        dificil, menores5, menores18, true, 0, estado);
                JOptionPane.showMessageDialog(this, "Juego prestamo agregado.");
            } catch (Exception ex) { 
            	JOptionPane.showMessageDialog(this, ex.getMessage()); 
            	}
        });

        bEstado.addActionListener(e -> {
            try {
                String idJuego = JOptionPane.showInputDialog("ID juego prestamo:");
                String estado = JOptionPane.showInputDialog("Nuevo estado:");
                cafe.cambiarEstadoJuego(login, idJuego, estado);
                JOptionPane.showMessageDialog(this, "Estado actualizado.");
            } catch (Exception ex) { 
            	JOptionPane.showMessageDialog(this, ex.getMessage());
            	}
        });

        bEliminar.addActionListener(e -> {
            try {
                String idJuego = JOptionPane.showInputDialog("ID juego prestamo:");
                cafe.eliminarJuegoPrestamo(login, idJuego);
                JOptionPane.showMessageDialog(this, "Juego prestamo eliminado.");
            } catch (Exception ex) {
            	JOptionPane.showMessageDialog(this, ex.getMessage()); 
            	}
        });

        bRobado.addActionListener(e -> {
            try {
                String idJuego = JOptionPane.showInputDialog("ID juego prestamo:");
                cafe.marcarJuegoRobado(login, idJuego);
                JOptionPane.showMessageDialog(this, "Juego marcado como desaparecido.");
            } catch (Exception ex) {
            	JOptionPane.showMessageDialog(this, ex.getMessage());
            	}
        });

        bReparar.addActionListener(e -> {
            try {
                String idJP = JOptionPane.showInputDialog("ID juego prestamo danado:");
                String idJV = JOptionPane.showInputDialog("ID juego venta fuente:");
                cafe.repararJuego(login, idJP, idJV);
                JOptionPane.showMessageDialog(this, "Juego reparado.");
            } catch (Exception ex) {
            	JOptionPane.showMessageDialog(this, ex.getMessage()); 
            	}
        });

        bMover.addActionListener(e -> {
            try {
                String idJV = JOptionPane.showInputDialog("ID juego venta:");
                cafe.moverJuegoVentaAPrestamo(login, idJV);
                JOptionPane.showMessageDialog(this, "Juego movido a prestamo.");
            } catch (Exception ex) { 
            	JOptionPane.showMessageDialog(this, ex.getMessage()); 
            	}
        });

        bVer.addActionListener(e -> {
            try {
                StringBuilder sb = new StringBuilder();
                Map<String, List<Prestamo>> historial = cafe.consultarInventarioPrestamo(login);
                for (Map.Entry<String, List<Prestamo>> entry : historial.entrySet()) {
                    JuegoMesaPrestamo j = cafe.getJuegosPrestamo().get(entry.getKey());
                    sb.append(j).append("\n  Historial:\n");
                    for (Prestamo pr : entry.getValue()) {
                        sb.append("  - ID: ").append(pr.getIdPrestamo())
                          .append(" | Usuario: ").append(pr.getUsuario().getLogin())
                          .append(" | Fecha: ").append(pr.getFechaPrestamo())
                          .append(" | Devuelto: ").append(pr.fueDevuelto()).append("\n");
                    }
                }
                mostrarTexto(sb.toString(), "Inventario prestamo");
            } catch (Exception ex) { 
            	JOptionPane.showMessageDialog(this, ex.getMessage()); 
            	}
        });

        return p;
    }

    // INVENTARIO VENTA 
    private JPanel panelInventarioVenta() {
        JPanel p = grid();

        JButton bAgregar = new JButton("Agregar juego venta");
        JButton bEliminar = new JButton("Eliminar juego venta");
        JButton bReabastecer = new JButton("Reabastecer stock");
        JButton bVer = new JButton("Ver inventario detallado");

        p.add(bAgregar);
        p.add(bEliminar);
        p.add(bReabastecer); 
        p.add(bVer);

        bAgregar.addActionListener(e -> {
            try {
                String nombre = JOptionPane.showInputDialog("Nombre:");
                int anio = Integer.parseInt(JOptionPane.showInputDialog("Anio publicacion:"));
                String editor = JOptionPane.showInputDialog("Editor:");
                String categoria = JOptionPane.showInputDialog("Categoria (Cartas/Tablero/Accion):");
                int min = Integer.parseInt(JOptionPane.showInputDialog("Min jugadores:"));
                int max = Integer.parseInt(JOptionPane.showInputDialog("Max jugadores:"));
                boolean dificil = confirmar("Es dificil?");
                boolean menores5 = confirmar("Juegan menores de 5?");
                boolean menores18 = confirmar("Juegan menores de 18?");
                double precio = Double.parseDouble(JOptionPane.showInputDialog("Precio venta:"));
                int stock = Integer.parseInt(JOptionPane.showInputDialog("Stock:"));
                double costo = Double.parseDouble(JOptionPane.showInputDialog("Costo base:"));
                cafe.agregarJuegoVenta(login, nombre, anio, editor, categoria, min, max,
                        dificil, menores5, menores18, precio, stock, costo);
                JOptionPane.showMessageDialog(this, "Juego venta agregado.");
            } catch (Exception ex) { 
            	JOptionPane.showMessageDialog(this, ex.getMessage());
            	}
        });

        bEliminar.addActionListener(e -> {
            try {
                String idJuego = JOptionPane.showInputDialog("ID juego venta:");
                cafe.eliminarJuegoVenta(login, idJuego);
                JOptionPane.showMessageDialog(this, "Juego venta eliminado.");
            } catch (Exception ex) { 
            	JOptionPane.showMessageDialog(this, ex.getMessage()); 
            	}
        });

        bReabastecer.addActionListener(e -> {
            try {
                String idJV = JOptionPane.showInputDialog("ID juego venta:");
                int cant = Integer.parseInt(JOptionPane.showInputDialog("Cantidad:"));
                cafe.reabastecerJuegoVenta(login, idJV, cant);
                JOptionPane.showMessageDialog(this, "Stock actualizado.");
            } catch (Exception ex) { 
            	JOptionPane.showMessageDialog(this, ex.getMessage());
            	}
        });

        bVer.addActionListener(e -> {
            try {
                StringBuilder sb = new StringBuilder();
                Map<String, JuegoMesaVenta> inv = cafe.consultarInventarioVenta(login);
                for (JuegoMesaVenta j : inv.values()) sb.append(j).append("\n");
                mostrarTexto(sb.toString(), "Inventario venta");
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
        });

        return p;
    }

    // MENU 
    private JPanel panelMenu() {
        JPanel p = grid();

        JButton bBebida = new JButton("Agregar bebida");
        JButton bPasteleria = new JButton("Agregar pasteleria");
        JButton bAprobar = new JButton("Aprobar sugerencia plato");
        JButton bRechazar = new JButton("Rechazar sugerencia plato");

        p.add(bBebida);
        p.add(bPasteleria);
        p.add(bAprobar);
        p.add(bRechazar);

        bBebida.addActionListener(e -> {
            try {
                String nombre = JOptionPane.showInputDialog("Nombre:");
                double precio = Double.parseDouble(JOptionPane.showInputDialog("Precio:"));
                boolean alcoholica = confirmar("Es alcoholica?");
                boolean caliente = confirmar("Es caliente?");
                cafe.agregarBebida(login, nombre, precio, true, alcoholica, caliente);
                JOptionPane.showMessageDialog(this, "Bebida agregada.");
            } catch (Exception ex) { 
            	JOptionPane.showMessageDialog(this, ex.getMessage()); 
            	}
        });

        bPasteleria.addActionListener(e -> {
            try {
                String nombre = JOptionPane.showInputDialog("Nombre:");
                double precio = Double.parseDouble(JOptionPane.showInputDialog("Precio:"));
                String al = JOptionPane.showInputDialog("Alergenos (separados por coma, vacio si ninguno):");
                List<String> alergenos = (al == null || al.isBlank())
                        ? new ArrayList<>() : Arrays.asList(al.split(","));
                cafe.agregarPasteleria(login, nombre, precio, true, alergenos);
                JOptionPane.showMessageDialog(this, "Pasteleria agregada.");
            } catch (Exception ex) {
            	JOptionPane.showMessageDialog(this, ex.getMessage());
            	}
        });

        bAprobar.addActionListener(e -> {
            try {
                String id = JOptionPane.showInputDialog("ID sugerencia:");
                cafe.aprobarSugerenciaPlato(login, id);
                JOptionPane.showMessageDialog(this, "Sugerencia aprobada.");
            } catch (Exception ex) {
            	JOptionPane.showMessageDialog(this, ex.getMessage());
            	}
        });

        bRechazar.addActionListener(e -> {
            try {
                String id = JOptionPane.showInputDialog("ID sugerencia:");
                cafe.rechazarSugerenciaPlato(login, id);
                JOptionPane.showMessageDialog(this, "Sugerencia rechazada.");
            } catch (Exception ex) { 
            	JOptionPane.showMessageDialog(this, ex.getMessage());
            	}
        });

        return p;
    }

    //TORNEOS 
    private JPanel panelTorneos() {
        JPanel p = grid();

        JButton bAmistoso = new JButton("Crear torneo amistoso");
        JButton bCompetitivo = new JButton("Crear torneo competitivo");
        JButton bEliminar = new JButton("Eliminar torneo");
        JButton bPremio = new JButton("Otorgar premio amistoso");
        JButton bVer = new JButton("Ver torneos");

        p.add(bAmistoso);
        p.add(bCompetitivo); 
        p.add(bEliminar); 
        p.add(bPremio); 
        p.add(bVer);

        bAmistoso.addActionListener(e -> {
            try {
                String idJuego = JOptionPane.showInputDialog("ID juego:");
                int num = Integer.parseInt(JOptionPane.showInputDialog("N participantes:"));
                DayOfWeek dia = DayOfWeek.valueOf(
                        JOptionPane.showInputDialog("Dia (MONDAY/TUESDAY/...):").toUpperCase());
                double desc = Double.parseDouble(
                        JOptionPane.showInputDialog("Premio descuento (ej. 0.15 = 15%):"));
                cafe.crearTorneoAmistoso(login, idJuego, num, dia, desc);
                JOptionPane.showMessageDialog(this, "Torneo amistoso creado.");
            } catch (Exception ex) { 
            	JOptionPane.showMessageDialog(this, ex.getMessage());
            	}
        });

        bCompetitivo.addActionListener(e -> {
            try {
                String idJuego = JOptionPane.showInputDialog("ID juego:");
                int num = Integer.parseInt(JOptionPane.showInputDialog("N participantes:"));
                DayOfWeek dia = DayOfWeek.valueOf(
                        JOptionPane.showInputDialog("Dia (MONDAY/TUESDAY/...):").toUpperCase());
                double tarifa = Double.parseDouble(JOptionPane.showInputDialog("Tarifa de entrada:"));
                cafe.crearTorneoCompetitivo(login, idJuego, num, dia, tarifa);
                JOptionPane.showMessageDialog(this, "Torneo competitivo creado.");
            } catch (Exception ex) { 
            	JOptionPane.showMessageDialog(this, ex.getMessage()); 
            	}
        });

        bEliminar.addActionListener(e -> {
            try {
                String id = JOptionPane.showInputDialog("ID torneo:");
                cafe.eliminarTorneo(login, id);
                JOptionPane.showMessageDialog(this, "Torneo eliminado.");
            } catch (Exception ex) { 
            	JOptionPane.showMessageDialog(this, ex.getMessage()); 
            	}
        });

        bPremio.addActionListener(e -> {
            try {
                String idT = JOptionPane.showInputDialog("ID torneo:");
                String idGanador = JOptionPane.showInputDialog("Login del ganador:");
                cafe.otorgarPremioTorneoAmistoso(login, idT, idGanador);
                JOptionPane.showMessageDialog(this, "Premio otorgado.");
            } catch (Exception ex) { 
            	JOptionPane.showMessageDialog(this, ex.getMessage());
            	}
        });

        bVer.addActionListener(e -> {
            try {
                StringBuilder sb = new StringBuilder();
                for (torneos.Torneo t : cafe.getTorneos()) {
                    String tipo = (t instanceof torneos.TorneoAmistoso) ? "AMISTOSO" : "COMPETITIVO";
                    sb.append("ID: ").append(t.getId()).append(" | Tipo: ").append(tipo)
                      .append(" | Juego: ").append(t.getJuego().getNombre())
                      .append(" | Dia: ").append(t.getDia())
                      .append(" | CuposFan: ").append(t.getCuposFanaticos())
                      .append(" | CuposReg: ").append(t.getCuposRegulares()).append("\n");
                    if (t instanceof torneos.TorneoCompetitivo) {
                        sb.append("  Premio actual: ")
                          .append(((torneos.TorneoCompetitivo) t).getPremio()).append("\n");
                    }
                }
                mostrarTexto(sb.toString(), "Torneos");
            } catch (Exception ex) { 
            	JOptionPane.showMessageDialog(this, ex.getMessage());
            	}
        });

        return p;
    }

    // INFORMES / GRAFICAS 
    private JPanel panelInformes() {
        JPanel p = grid();

        JButton bDiario = new JButton("Informe diario");
        JButton bSemanal = new JButton("Informe semanal");
        JButton bMensual = new JButton("Informe mensual");
        JButton bGraficas = new JButton("Ver graficas");

        p.add(bDiario); 
        p.add(bSemanal);
        p.add(bMensual);
        p.add(bGraficas);

        bDiario.addActionListener(e -> mostrarInforme("diaria"));
        bSemanal.addActionListener(e -> mostrarInforme("semanal"));
        bMensual.addActionListener(e -> mostrarInforme("mensual"));
        bGraficas.addActionListener(e -> new GraficasFrame(cafe));

        return p;
    }

    private void mostrarInforme(String granularidad) {
        try {
            Informe inf = cafe.consultarInforme(login, granularidad);
            String txt = "Ventas juegos: " + inf.totalJuegos + " | Impuestos: " + inf.impuestosJuegos
                    + "\nVentas comida: " + inf.totalComida + " | Impuestos: " + inf.impuestosComida
                    + " | Propinas: " + inf.propinasComida;
            JOptionPane.showMessageDialog(this, txt, "Informe " + granularidad,
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
        	JOptionPane.showMessageDialog(this, ex.getMessage());
        	}
    }
}
