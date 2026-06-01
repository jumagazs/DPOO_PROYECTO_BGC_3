package interfaz;

import modelo.Cafe;
import modelo.GestorPersistencia;


public class Main {

    public static void main(String[] args) {
        GestorPersistencia gp = new GestorPersistencia("datos/datosGraficas.txt");
        Cafe cafe = new Cafe();
        try {
            gp.cargarTodo(cafe);
        } catch (Exception e) {
            System.err.println("Advertencia: no se pudo cargar datos: " + e.getMessage());
        }

        javax.swing.SwingUtilities.invokeLater(() -> new LoginFrame(cafe, gp));
    }
}