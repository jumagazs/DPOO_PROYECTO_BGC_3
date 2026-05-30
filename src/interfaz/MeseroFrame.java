package interfaz;

import javax.swing.*;
import java.awt.*;

import modelo.Cafe;
import modelo.GestorPersistencia;

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

        JPanel panel = new JPanel(new GridLayout(6,1));

        JButton btnVerTurnos = new JButton("Ver Turnos");
        JButton btnPedido = new JButton("Registrar Pedido");
        JButton btnPrestamo = new JButton("Prestar Juego");

        panel.add(btnVerTurnos);
        panel.add(btnPedido);
        panel.add(btnPrestamo);

        add(panel);
        setVisible(true);

        btnVerTurnos.addActionListener(e -> {
            try {
                StringBuilder sb = new StringBuilder();
                cafe.consultarTurnoEmpleado(login)
                    .forEach(t -> sb.append(t).append("\n"));

                JOptionPane.showMessageDialog(this, sb.toString());

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        });

        btnPedido.addActionListener(e -> {
            try {
                String mesa = JOptionPane.showInputDialog("ID Mesa:");
                cafe.registrarPedidoMesero(login, mesa);

                gp.guardarTodo(cafe);

                JOptionPane.showMessageDialog(this, "Pedido creado");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        });

        btnPrestamo.addActionListener(e -> {
            try {
                String idJuego = JOptionPane.showInputDialog("ID Juego:");
                String idMesa = JOptionPane.showInputDialog("ID Mesa:");
                String idCliente = JOptionPane.showInputDialog("Cliente:");

                cafe.prestamoDeJuegos(idJuego, login, idMesa, idCliente);

                gp.guardarTodo(cafe);

                JOptionPane.showMessageDialog(this, "Préstamo hecho");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        });
    }
}