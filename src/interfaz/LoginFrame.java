package interfaz;

import javax.swing.*;
import modelo.Cafe;
import modelo.GestorPersistencia;

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

        // LOGIN
        btnLogin.addActionListener(e -> {
            try {
                String login = txtLogin.getText();
                String pass = new String(txtPass.getPassword());

                if (!cafe.getUsuarios().containsKey(login)) {
                    throw new Exception("Usuario no existe");
                }

                if (!cafe.getUsuarios().get(login).getContrasena().equals(pass)) {
                    throw new Exception("Contraseña incorrecta");
                }

                new ClienteFrame(cafe, login, gp);
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

                gp.guardarTodo(cafe);

                JOptionPane.showMessageDialog(this, "Usuario registrado correctamente");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        });
    }
}