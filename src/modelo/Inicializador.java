package modelo;

import usuarios.*;
import juegos.*;
import productos.*;
import java.util.Arrays;

public class Inicializador {

    public static void cargarDatos(Cafe cafe) {

        try {
            // ADMIN
            Administrador admin = new Administrador("admin1", "123");
            cafe.getUsuarios().put("admin1", admin);

            // CLIENTE
            cafe.registrarCliente("cliente1", "123");

            // MESERO
            cafe.registrarMesero("admin1", "mesero1", "123");

            // COCINERO
            cafe.registrarCocinero("admin1", "cocinero1", "123");

            // ======================
            // MENU
            // ======================
            cafe.agregarBebida("admin1", "Café", 5000, true, false, true);
            cafe.agregarBebida("admin1", "Cerveza", 8000, true, true, false);
            cafe.agregarPasteleria("admin1", "Torta", 6000, true, Arrays.asList("gluten"));

            // ======================
            // JUEGOS
            // ======================
            cafe.agregarJuegoPrestamo(
                "admin1", "Catan", 1995, "Kosmos", "Estrategia",
                3, 4, false, true, true, true, 0, "Nuevo"
            );

            cafe.agregarJuegoVenta(
                "admin1", "UNO", 2000, "Mattel", "Cartas",
                2, 6, false, true, true,
                30000, 10, 10000
            );

            // ======================
            // TORNEO
            // ======================
            cafe.crearTorneoAmistoso(
                "admin1", "JP1", 4,
                java.time.DayOfWeek.MONDAY,
                0.2
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}