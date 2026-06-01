package interfaz;

import javax.swing.*;
import modelo.Cafe;
import modelo.GestorPersistencia;
import usuarios.*;

public class LoginFrame extends JFrame {

    private Cafe cafe;
    private GestorPersistencia gp;

    public LoginFrame(Cafe cafe, GestorPersistencia gp) {
        this.cafe = cafe;
        this.gp = gp;

        setTitle("Login");
        setSize(300,250);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JTextField txtLogin = new JTextField();
        JPasswordField txtPass = new JPasswordField();

        JButton btnLogin = new JButton("Login");
        JButton btnRegistro = new JButton("Registrarse");

        panel.add(new JLabel("Login:"));
        panel.add(txtLogin);
        panel.add(new JLabel("Contraseña:"));
        panel.add(txtPass);
        panel.add(btnLogin);
        panel.add(btnRegistro);

        add(panel);
        setVisible(true);

        btnLogin.addActionListener(e -> {
            try {
                String l = txtLogin.getText();
                String pass = new String(txtPass.getPassword());
                Usuario u = cafe.iniciarSesion(l, pass);
                if (u instanceof Administrador) {
                    new AdminFrame(cafe, l, gp);
                } else if (u instanceof Mesero) {
                    new MeseroFrame(cafe, l, gp);
                } else if (u instanceof Cocinero) {
                    new CocineroFrame(cafe, l, gp);
                } else if (u instanceof Cliente) {
                    new ClienteFrame(cafe, l, gp);
                }
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        });

        btnRegistro.addActionListener(e -> {
            try {
                String login = txtLogin.getText();
                String pass = new String(txtPass.getPassword());

                if (login.isEmpty() || pass.isEmpty()) {
                    throw new Exception("Campos vacíos");
                }

                cafe.registrarCliente(login, pass);

                JOptionPane.showMessageDialog(this, "Usuario registrado correctamente");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        });
    }
}