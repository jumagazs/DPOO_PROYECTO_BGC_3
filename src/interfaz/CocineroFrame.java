package interfaz;

import javax.swing.*;

import juegos.*;

import java.awt.*;

import modelo.Cafe;
import modelo.GestorPersistencia;

public class CocineroFrame extends JFrame {

    private Cafe cafe;
    private String login;
    private GestorPersistencia gp;

    public CocineroFrame(Cafe cafe, String login, GestorPersistencia gp) {
        this.cafe = cafe;
        this.login = login;
        this.gp = gp;

        setTitle("Cocinero - " + login);
        setSize(400,300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        addWindowListener(new CierreVentana(cafe, gp)); 

        JPanel panel = new JPanel(new GridLayout(0, 1, 6, 6));

        JButton btnPreparar = new JButton("Preparar Pedido");
        JButton btnVerTurnos   = new JButton("Ver mis turnos ");
        JButton btnCambioGen   = new JButton("Solicitar cambio de turno general ");
        JButton btnIntercambio = new JButton("Solicitar intercambio de turno");
        JButton btnVerFav      = new JButton("Ver favoritos ");
        JButton btnAgregarFav  = new JButton("Agregar favorito ");
        JButton btnEliminarFav = new JButton("Eliminar favorito ");
        
        panel.add(btnPreparar);   
        panel.add(btnVerTurnos);
        panel.add(btnCambioGen);  
        panel.add(btnIntercambio);
        panel.add(btnVerFav);      
        panel.add(btnAgregarFav);  
        panel.add(btnEliminarFav);
 

        panel.add(btnPreparar);

        add(new JScrollPane(panel));
        setVisible(true);

        btnPreparar.addActionListener(e -> {
            try {
                String idPedido = JOptionPane.showInputDialog("ID Pedido:");

                cafe.prepararPedido(login, idPedido);

                gp.guardarTodo(cafe);

                JOptionPane.showMessageDialog(this, "Pedido preparado");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        });
        
     //  Ver turnos (RF12) 
        btnVerTurnos.addActionListener(e -> {
            try {
                StringBuilder sb = new StringBuilder();
                cafe.consultarTurnoEmpleado(login).forEach(t -> sb.append(t).append("\n"));
                JOptionPane.showMessageDialog(this, sb.toString(), "Mis turnos",JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
        });
 
        //  Cambio de turno general (RF13) 
        btnCambioGen.addActionListener(e -> {
            try {
                String idTurno = JOptionPane.showInputDialog("ID turno a cambiar:");
                cafe.solicitarCambioTurnoGeneral(login, idTurno);
                JOptionPane.showMessageDialog(this, "Solicitud de cambio enviada.");
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
        });
 
        //  Intercambio de turno (RF13) 
        btnIntercambio.addActionListener(e -> {
            try {
                String idTurno = JOptionPane.showInputDialog("ID de tu turno:");
                String loginDestino = JOptionPane.showInputDialog("Login del otro empleado:");
                String idTurnoDestino = JOptionPane.showInputDialog("ID del turno del otro:");
                cafe.solicitarIntercambioTurno(login, idTurno, loginDestino, idTurnoDestino);
                JOptionPane.showMessageDialog(this, "Solicitud de intercambio enviada.");
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
        });
 
        //  Favoritos (RF16) 
        btnVerFav.addActionListener(e -> {
            try {
                StringBuilder sb = new StringBuilder();
                for (JuegoMesa j : cafe.consultarFavoritos(login)) {
                	sb.append(j).append("\n");
                }
                JOptionPane.showMessageDialog(this, sb.toString());
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
        });
 
        btnAgregarFav.addActionListener(e -> {
            try {
                String idJuego = JOptionPane.showInputDialog("ID juego:");
                cafe.agregarJuegoFavoritoAUsuario(login, idJuego);
                JOptionPane.showMessageDialog(this, "Favorito agregado.");
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
        });
 
        btnEliminarFav.addActionListener(e -> {
            try {
                String idJuego = JOptionPane.showInputDialog("ID juego:");
                cafe.eliminarJuegoFavoritoDeUsuario(login, idJuego);
                JOptionPane.showMessageDialog(this, "Favorito eliminado.");
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
        });
    }
}