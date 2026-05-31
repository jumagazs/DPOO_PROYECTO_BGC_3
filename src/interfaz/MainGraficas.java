package interfaz;

import modelo.Cafe;
import modelo.GestorPersistencia;

public class MainGraficas {

    public static void main(String[] args) {

        try {

            Cafe cafe = new Cafe();

            GestorPersistencia gp =
                    new GestorPersistencia("datos/datos.txt");

            gp.cargarTodo(cafe);

            new GraficasFrame(cafe);

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}