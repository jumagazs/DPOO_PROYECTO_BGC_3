package interfaz;

import modelo.Cafe;
import modelo.GestorPersistencia;

public class MainCliente {

    public static void main(String[] args) {

        try {
            Cafe cafe = new Cafe();

            GestorPersistencia gp = new GestorPersistencia("datos/datos.txt");

            gp.cargarTodo(cafe);

            new LoginFrame(cafe, gp);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}