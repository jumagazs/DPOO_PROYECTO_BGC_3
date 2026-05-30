package interfaz;

import modelo.Cafe;
import modelo.GestorPersistencia;
import usuarios.*;

import javax.swing.*;

public class MainMesero {

    public static void main(String[] args) {

        try {
            Cafe cafe = new Cafe();
            GestorPersistencia gp = new GestorPersistencia("datos/datos.txt");

            gp.cargarTodo(cafe);

            String user = JOptionPane.showInputDialog("Login Mesero:");
            String pass = JOptionPane.showInputDialog("Contraseña:");

            Usuario u = cafe.iniciarSesion(user, pass);

            if (u instanceof Mesero) {
                new MeseroFrame(cafe, user, gp);
            } else {
                JOptionPane.showMessageDialog(null, "No es mesero");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
}