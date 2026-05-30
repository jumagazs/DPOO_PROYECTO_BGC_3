package interfaz;

import modelo.Cafe;
import modelo.GestorPersistencia;
import usuarios.*;

import javax.swing.*;

public class MainCocinero {

    public static void main(String[] args) {

        try {
            Cafe cafe = new Cafe();
            GestorPersistencia gp = new GestorPersistencia("datos/datos.txt");

            gp.cargarTodo(cafe);

            String user = JOptionPane.showInputDialog("Login Cocinero:");
            String pass = JOptionPane.showInputDialog("Contraseña:");

            Usuario u = cafe.iniciarSesion(user, pass);

            if (u instanceof Cocinero) {
                new CocineroFrame(cafe, user, gp);
            } else {
                JOptionPane.showMessageDialog(null, "No es cocinero");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
}