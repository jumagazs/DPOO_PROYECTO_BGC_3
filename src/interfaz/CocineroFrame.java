package interfaz;

import javax.swing.*;
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

        JPanel panel = new JPanel(new GridLayout(3,1));

        JButton btnPreparar = new JButton("Preparar Pedido");

        panel.add(btnPreparar);

        add(panel);
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
    }
}