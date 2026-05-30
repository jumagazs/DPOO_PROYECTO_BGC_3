package pruebasJUnit;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import juegos.JuegoMesaPrestamo;
import juegos.JuegoMesaVenta;
import modelo.Cafe;
import modelo.Informe;
import pedidos.Pedido;
import productos.ProductoMenu;
import sugerencias.SugerenciaPlato;
import usuarios.Administrador;
import usuarios.Cliente;
import usuarios.Empleado;
import usuarios.Turno;

public class RFAdminTest {

    private Cafe cafe;

    @BeforeEach
    public void setup() throws Exception {
        cafe = new Cafe();
        cafe.getUsuarios().put("admin", new Administrador("admin", "admin"));

        cafe.registrarCliente("cliente1", "1234");
        cafe.registrarMesero("admin", "meseroBase", "1234");
        cafe.registrarCocinero("admin", "cocineroBase", "1234");

        cafe.agregarMesa(new mesas.Mesa("M1", 4));

        cafe.agregarJuegoPrestamo("admin", "Ajedrez", 1971, "Ubisoft", "Juego de mesa",
                2, 10, false, true, true, true, 0, "Bueno");

        cafe.agregarJuegoVenta("admin", "Fornite", 2016, "Sony", "Accion",
                1, 5, false, false, true, 180000, 5, 120000);

        cafe.agregarBebida("admin", "Milo", 8500, true, false, true);
    }

    @Test
    public void testRFAdmin1RegistrarEmpleados() throws Exception {
        cafe.registrarMesero("admin", "mesero1", "1234");
        cafe.registrarCocinero("admin", "cocinero1", "1234");

        assertNotNull(cafe.getUsuarios().get("mesero1"));
        assertNotNull(cafe.getUsuarios().get("cocinero1"));
    }

    @Test
    public void testRFAdmin2GestionarTurnos() throws Exception {
        Empleado mesero = (Empleado) cafe.getUsuarios().get("meseroBase");

        Turno turno = new Turno("T1",
                LocalDateTime.of(2026, 4, 20, 8, 0),
                LocalDateTime.of(2026, 4, 20, 16, 0),
                "DOMINGO",
                mesero);

        cafe.agregarTurno("admin", "meseroBase", turno);

        assertEquals(1, cafe.consultarTurnoEmpleado("meseroBase").size());
        assertEquals("T1", cafe.consultarTurnoEmpleado("meseroBase").get(0).getIdTurno());
    }

    @Test
    public void testRFAdmin3GestionarInventarioPrestamo() throws Exception {
        String idJuego = cafe.getJuegosPrestamo().keySet().iterator().next();

        cafe.cambiarEstadoJuego("admin", idJuego, "Falta una pieza");

        JuegoMesaPrestamo juego = cafe.getJuegosPrestamo().get(idJuego);

        assertEquals("Falta una pieza", juego.getEstado());

        cafe.marcarJuegoRobado("admin", idJuego);

        assertEquals("Desaparecido", juego.getEstado());
    }

    @Test
    public void testRFAdmin4GestionarInventarioVenta() throws Exception {
        String idJuegoVenta = cafe.getJuegosVenta().keySet().iterator().next();

        JuegoMesaVenta juego = cafe.getJuegosVenta().get(idJuegoVenta);
        int stockAntes = juego.getCantidadStock();

        cafe.reabastecerJuegoVenta("admin", idJuegoVenta, 3);

        assertEquals(stockAntes + 3, juego.getCantidadStock());

        cafe.eliminarJuegoVenta("admin", idJuegoVenta);

        assertFalse(cafe.getJuegosVenta().containsKey(idJuegoVenta));
    }

    @Test
    public void testRFAdmin5RegistrarJuegosNuevos() throws Exception {
        cafe.agregarJuegoPrestamo("admin", "Parques", 1995, "Nintendo", "Juego de mesa",
                3, 4, false, false, true, true, 0, "Nuevo");

        cafe.agregarJuegoVenta("admin", "Repo", 2017, "Microsoft", "Accion",
                2, 4, false, true, true, 150000, 4, 90000);

        boolean encontro1 = false;
        for (JuegoMesaPrestamo j : cafe.getJuegosPrestamo().values()) {
            if (j.getNombre().equals("Parques")) {
                encontro1 = true;
            }
        }

        boolean encontro2 = false;
        for (JuegoMesaVenta j : cafe.getJuegosVenta().values()) {
            if (j.getNombre().equals("Repo")) {
                encontro2 = true;
            }
        }

        assertTrue(encontro1);
        assertTrue(encontro2);
    }

    @Test
    public void testRFAdmin6GestionarMenuCafe() throws Exception {
        cafe.agregarBebida("admin", "Pastel de pollo", 9000, true, true, false);
        cafe.agregarPasteleria("admin", "Torta 3 leches", 9000, true, Arrays.asList("leche", "huevo"));

        assertTrue(cafe.getMenu().size() >= 3);

        SugerenciaPlato sugerencia = cafe.sugerirPlato(
                "meseroBase",
                "Brazo de reina",
                7000,
                false,
                true,
                Arrays.asList("leche", "huevo"),
                "PASTELERIA"
        );

        cafe.aprobarSugerenciaPlato("admin", sugerencia.getIdSugerencia());

        boolean encontro = false;
        for (ProductoMenu p : cafe.getMenu().values()) {
            if (p.getNombre().equals("Brazo de reina")) {
                encontro = true;
            }
        }

        assertTrue(encontro);
    }

    @Test
    public void testRFAdmin7ConsultarInformeFinanciero() throws Exception {
        String idJuegoVenta = cafe.getJuegosVenta().keySet().iterator().next();
        cafe.comprarJuegoConDescuento("cliente1", idJuegoVenta, 1, 0, "");

        Cliente cliente = (Cliente) cafe.getUsuarios().get("cliente1");
        mesas.Mesa mesa = cafe.asignarMesaACliente(cliente, 2, false, false);

        Pedido pedido = cafe.registrarPedidoMesero("meseroBase", mesa.getIdMesa());

        String idProducto = cafe.getMenu().keySet().iterator().next();
        cafe.agregarProductoAPedido(pedido, idProducto, 1);
        cafe.confirmarPedido(pedido);

        Informe informe = cafe.consultarInforme("admin", "diaria");

        assertNotNull(informe);
        assertTrue(informe.totalJuegos > 0);
        assertTrue(informe.impuestosJuegos > 0);
        assertTrue(informe.totalComida > 0);
        assertTrue(informe.impuestosComida > 0);
    }
    
    @Test
    public void testCrearTorneoConCuposInsuficientes() throws Exception {
        String idJuego = cafe.getJuegosPrestamo().keySet().iterator().next();
        JuegoMesaPrestamo j = cafe.getJuegosPrestamo().get(idJuego);
        int max = j.getMaxJugadores();
        
        Exception e = assertThrows(Exception.class, () -> {
            cafe.crearTorneoAmistoso("admin", idJuego, max + 100, 
                java.time.DayOfWeek.MONDAY, 0.1);
        });
        assertTrue(e.getMessage().contains("copias"));
    }

    @Test
    public void testEmpleadoConTurnoNoPuedeInscribirseATorneo() throws Exception {
        Empleado emp = (Empleado) cafe.getUsuarios().get("meseroBase");
        Turno t = new Turno("TX",
            LocalDateTime.of(2026, 5, 4, 8, 0),
            LocalDateTime.of(2026, 5, 4, 16, 0),
            "LUNES", emp);
        cafe.agregarTurno("admin", "meseroBase", t);
        
        String idJuego = cafe.getJuegosPrestamo().keySet().iterator().next();
        cafe.crearTorneoAmistoso("admin", idJuego, 4, java.time.DayOfWeek.MONDAY, 0.1);
        String idTorneo = cafe.getTorneos().get(0).getId();
        
        Exception e = assertThrows(Exception.class, () -> {
            cafe.inscribirUsuarioTorneo("meseroBase", idTorneo, 1);
        });
        assertTrue(e.getMessage().toLowerCase().contains("turno"));
    }
}