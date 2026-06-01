package interfaz;

import javax.swing.*;

import juegos.JuegoMesa;

import java.awt.*;

import modelo.Cafe;
import modelo.GestorPersistencia;
import pedidos.Pedido;
import prestamos.Prestamo;
import ventas.VentaJuego;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MeseroFrame extends JFrame {

    private Cafe cafe;
    private String login;
    private GestorPersistencia gp;

    public MeseroFrame(Cafe cafe, String login, GestorPersistencia gp) {
        this.cafe = cafe;
        this.login = login;
        this.gp = gp;

        setTitle("Mesero - " + login);
        setSize(500,400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        addWindowListener(new CierreVentana(cafe, gp));        

        JPanel panel = new JPanel(new GridLayout(0,1,6,1));
        
        JButton btnPreparar    = new JButton("Preparar Pedido");
        JButton btnVerTurnos = new JButton("Ver Turnos");
        JButton btnPedido = new JButton("Registrar Pedido");
        JButton btnPrestamo = new JButton("Prestar Juego");
        JButton btnDevolver    = new JButton("Devolver juego");
        JButton btnCambioGen   = new JButton("Solicitar cambio de turno general ");
        JButton btnIntercambio = new JButton("Solicitar intercambio de turno ");
        JButton btnComprar     = new JButton("Comprar juego con descuento empleado ");
        JButton btnPrestamoFOT = new JButton("Préstamo fuera de turno ");
        JButton btnVerFav      = new JButton("Ver favoritos ");
        JButton btnAgregarFav  = new JButton("Agregar favorito ");
        JButton btnEliminarFav = new JButton("Eliminar favorito ");
        JButton btnSugerir     = new JButton("Sugerir plato ");
        JButton btnVerTorneos  = new JButton("Ver torneos");
        JButton btnInscribir   = new JButton("Inscribirse a torneo");
        JButton btnDesinscribir= new JButton("Desinscribirse de torneo");
        
        panel.add(btnPreparar);
        panel.add(btnVerTurnos);   
        panel.add(btnPedido);      
        panel.add(btnPrestamo);
        panel.add(btnDevolver);    
        panel.add(btnCambioGen);   
        panel.add(btnIntercambio);
        panel.add(btnComprar);     
        panel.add(btnPrestamoFOT); 
        panel.add(btnVerFav);
        panel.add(btnAgregarFav);  
        panel.add(btnEliminarFav); 
        panel.add(btnSugerir);
        panel.add(btnVerTorneos);  
        panel.add(btnInscribir);   
        panel.add(btnDesinscribir);
        
        add(new JScrollPane(panel));
        setVisible(true);

     // RF20 - Preparar pedido
        btnPreparar.addActionListener(e -> {
            try {
                String idPedido = JOptionPane.showInputDialog("ID Pedido:");
                cafe.prepararPedido(login, idPedido);
                JOptionPane.showMessageDialog(this, "Pedido preparado");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        });
 
        // RF12 - Ver turnos
        btnVerTurnos.addActionListener(e -> {
            try {
                StringBuilder sb = new StringBuilder();
                cafe.consultarTurnoEmpleado(login).forEach(t -> sb.append(t).append("\n"));
                JOptionPane.showMessageDialog(this, sb.toString(), "Mis turnos", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) { 
            	JOptionPane.showMessageDialog(this, ex.getMessage()); 
            	}
        });
 
        // RF13 - Cambio de turno general
        btnCambioGen.addActionListener(e -> {
            try {
                String idTurno = JOptionPane.showInputDialog("ID turno a cambiar:");
                cafe.solicitarCambioTurnoGeneral(login, idTurno);
                JOptionPane.showMessageDialog(this, "Solicitud de cambio enviada.");
            } catch (Exception ex) { 
            	JOptionPane.showMessageDialog(this, ex.getMessage());
            	}
        });
 
        // RF13 - Intercambio de turno
        btnIntercambio.addActionListener(e -> {
            try {
                String idTurno = JOptionPane.showInputDialog("ID de tu turno:");
                String loginDestino = JOptionPane.showInputDialog("Login del otro empleado:");
                String idTurnoDestino = JOptionPane.showInputDialog("ID del turno del otro:");
                cafe.solicitarIntercambioTurno(login, idTurno, loginDestino, idTurnoDestino);
                JOptionPane.showMessageDialog(this, "Solicitud de intercambio enviada.");
            } catch (Exception ex) {
            	JOptionPane.showMessageDialog(this, ex.getMessage()); 
            	}
        });
 
        // RF14 - Comprar juego con 20% descuento empleado
        btnComprar.addActionListener(e -> {
            try {
                String idJuego = JOptionPane.showInputDialog("ID juego venta:");
                int cantidad = Integer.parseInt(JOptionPane.showInputDialog("Cantidad:"));
                VentaJuego v = cafe.comprarJuegoConDescuento(login, idJuego, cantidad, 0, "");
                JOptionPane.showMessageDialog(this, "Compra realizada | Total: " + v.getTotal());
            } catch (Exception ex) { 
            	JOptionPane.showMessageDialog(this, ex.getMessage());
            	}
        });
 
        // RF15 - Prestamo fuera de turno
        btnPrestamoFOT.addActionListener(e -> {
            try {
                String idJuego = JOptionPane.showInputDialog("ID juego:");
                Prestamo p = cafe.solicitarPrestamoJuegoFlexible(login, idJuego);
                JOptionPane.showMessageDialog(this, "Prestamo: " + p.getIdPrestamo());
            } catch (Exception ex) { 
            	JOptionPane.showMessageDialog(this, ex.getMessage());
            	}
        });
 
        // RF16 - Favoritos
        btnVerFav.addActionListener(e -> {
            try {
                StringBuilder sb = new StringBuilder();
                for (JuegoMesa j : cafe.consultarFavoritos(login)) {
                	sb.append(j).append("\n");
                }
                JOptionPane.showMessageDialog(this, sb.toString());
            } catch (Exception ex) { 
            	JOptionPane.showMessageDialog(this, ex.getMessage()); 
            	}
        });
 
        btnAgregarFav.addActionListener(e -> {
            try {
                String idJuego = JOptionPane.showInputDialog("ID juego:");
                cafe.agregarJuegoFavoritoAUsuario(login, idJuego);
                JOptionPane.showMessageDialog(this, "Favorito agregado.");
            } catch (Exception ex) { 
            	JOptionPane.showMessageDialog(this, ex.getMessage());
            	}
        });
 
        btnEliminarFav.addActionListener(e -> {
            try {
                String idJuego = JOptionPane.showInputDialog("ID juego:");
                cafe.eliminarJuegoFavoritoDeUsuario(login, idJuego);
                JOptionPane.showMessageDialog(this, "Favorito eliminado.");
            } catch (Exception ex) {
            	JOptionPane.showMessageDialog(this, ex.getMessage());
            	}
        });
 
        // RF17 - Sugerir plato
        btnSugerir.addActionListener(e -> {
            try {
                String nombre = JOptionPane.showInputDialog("Nombre del plato:");
                double precio = Double.parseDouble(JOptionPane.showInputDialog("Precio:"));
                boolean alc = JOptionPane.showConfirmDialog(this, "Es alcoholico?",
                        "Sugerencia", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
                boolean cal = JOptionPane.showConfirmDialog(this, "Es caliente?",
                        "Sugerencia", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
                String alStr = JOptionPane.showInputDialog("Alergenos (separados por coma, vacio si ninguno):");
                List<String> alergenos;
                if (alStr == null || alStr.isBlank()) {
                    alergenos = new ArrayList<>();
                } else {
                    alergenos = Arrays.asList(alStr.split(","));
                }
                String tipo = JOptionPane.showInputDialog("Tipo (Bebida/Pasteleria):");
                cafe.sugerirPlato(login, nombre, precio, alc, cal, alergenos, tipo);
                JOptionPane.showMessageDialog(this, "Sugerencia enviada.");
            } catch (Exception ex) { 
            	JOptionPane.showMessageDialog(this, ex.getMessage());
            	}
        });
 
        // Torneos (RF E-T1)
        btnVerTorneos.addActionListener(e -> {
            StringBuilder sb = new StringBuilder();
            for (torneos.Torneo t : cafe.getTorneos()) {
                sb.append("ID: ").append(t.getId())
                  .append(" | Juego: ").append(t.getJuego().getNombre())
                  .append(" | Dia: ").append(t.getDia()).append("\n");
            }
            JOptionPane.showMessageDialog(this, sb.toString());
        });
 
        btnInscribir.addActionListener(e -> {
            try {
                String idTorneo = JOptionPane.showInputDialog("ID torneo:");
                int cupos = Integer.parseInt(JOptionPane.showInputDialog("Cupos (1-3):"));
                cafe.inscribirUsuarioTorneo(login, idTorneo, cupos);
                JOptionPane.showMessageDialog(this, "Inscrito.");
            } catch (Exception ex) {
            	JOptionPane.showMessageDialog(this, ex.getMessage());
            	}
        });
 
        btnDesinscribir.addActionListener(e -> {
            try {
                String idTorneo = JOptionPane.showInputDialog("ID torneo:");
                cafe.desinscribirUsuarioTorneo(login, idTorneo);
                JOptionPane.showMessageDialog(this, "Desinscrito.");
            } catch (Exception ex) { 
            	JOptionPane.showMessageDialog(this, ex.getMessage());
            	}
        });
    }
}