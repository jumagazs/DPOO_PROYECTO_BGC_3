package interfaz;

import modelo.Cafe;
import modelo.GestorPersistencia;
import usuarios.*;

import javax.swing.*;

public class MainAdmin {

    public static void main(String[] args) {

        try {
            Cafe cafe = new Cafe();
            GestorPersistencia gp = new GestorPersistencia("datos/datos.txt");

            gp.cargarTodo(cafe);

            String user = JOptionPane.showInputDialog("Login Admin:");
            String pass = JOptionPane.showInputDialog("Contraseña:");

            Usuario u = cafe.iniciarSesion(user, pass);

            if (u instanceof Administrador) {
                new AdminFrame(cafe, user, gp);
            } else {
                JOptionPane.showMessageDialog(null, "No es administrador");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
}