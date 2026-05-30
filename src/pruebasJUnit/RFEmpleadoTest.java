package pruebasJUnit;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import juegos.JuegoMesaPrestamo;
import mesas.Mesa;
import modelo.Cafe;
import pedidos.Pedido;
import prestamos.Prestamo;
import sugerencias.SugerenciaPlato;
import usuarios.Administrador;
import usuarios.Cliente;
import usuarios.Empleado;
import usuarios.Turno;
import ventas.VentaJuego;

public class RFEmpleadoTest {

    private Cafe cafe;

    @BeforeEach
    public void setup() throws Exception {
        cafe = new Cafe();

        cafe.getUsuarios().put("admin", new Administrador("admin", "admin"));

        cafe.registrarCliente("cliente1", "1234");
        cafe.registrarMesero("admin", "mesero1", "1234");
        cafe.registrarMesero("admin", "mesero2", "1234");
        cafe.registrarCocinero("admin", "cocinero1", "1234");

        cafe.agregarMesa(new Mesa("M1", 2));
        cafe.agregarMesa(new Mesa("M2", 4));

        cafe.agregarJuegoPrestamo("admin", "FIFA", 2020, "Sony", "Accion",
                2, 10, false, true, true, true, 0, "Bueno");

        cafe.agregarJuegoPrestamo("admin", "Halo", 2021, "Microsoft", "Aventura",
                3, 6, true, false, false, true, 0, "Nuevo");

        cafe.agregarJuegoVenta("admin", "Roblox", 2022, "Nintendo", "Aventura",
                1, 5, false, false, true, 180000, 5, 120000);

        cafe.agregarBebida("admin", "Milo", 8500, true, false, true);
    }

    @Test
    public void testRF12ConsultarTurnoPropio() throws Exception {
        Empleado mesero = (Empleado) cafe.getUsuarios().get("mesero1");

        Turno turno = new Turno("T1",
                LocalDateTime.of(2026, 4, 20, 8, 0),
                LocalDateTime.of(2026, 4, 20, 16, 0),
                "SABADO",
                mesero);

        cafe.agregarTurno("admin", "mesero1", turno);

        assertEquals(1, cafe.consultarTurnoEmpleado("mesero1").size());
        assertEquals("T1", cafe.consultarTurnoEmpleado("mesero1").get(0).getIdTurno());
    }

    @Test
    public void testRF13SolicitarCambioTurnoGeneral() throws Exception {
        Empleado mesero = (Empleado) cafe.getUsuarios().get("mesero1");

        Turno turno = new Turno("T1",
                LocalDateTime.of(2026, 4, 20, 8, 0),
                LocalDateTime.of(2026, 4, 20, 16, 0),
                "SABADO",
                mesero);

        cafe.agregarTurno("admin", "mesero1", turno);

        assertNotNull(cafe.solicitarCambioTurnoGeneral("mesero1", "T1"));
        assertEquals(1, cafe.getSolicitudesCambioTurno().size());
    }

    @Test
    public void testRF14ComprarConDescuentoEmpleado() throws Exception {
        String idJuegoVenta = cafe.getJuegosVenta().keySet().iterator().next();

        VentaJuego venta = cafe.comprarJuegoConDescuento("mesero1", idJuegoVenta, 1, 0, "");

        assertNotNull(venta);
        assertTrue(venta.getTotal() > 0);
        assertTrue(venta.getTotal() < 214200);
    }

    @Test
    public void testRF15EmpleadoPideJuegoFueraDeTurno() throws Exception {
        String idJuego = cafe.getJuegosPrestamo().keySet().iterator().next();

        Prestamo prestamo = cafe.solicitarPrestamoJuegoFlexible("mesero1", idJuego);

        assertNotNull(prestamo);
        assertEquals("mesero1", prestamo.getUsuario().getLogin());
        assertNull(prestamo.getMesa());
    }

    @Test
    public void testRF16EmpleadoGestionaFavoritos() throws Exception {
        String idJuego = cafe.getJuegosPrestamo().keySet().iterator().next();

        cafe.agregarJuegoFavoritoAUsuario("mesero1", idJuego);

        assertEquals(1, cafe.consultarFavoritos("mesero1").size());

        cafe.eliminarJuegoFavoritoDeUsuario("mesero1", idJuego);

        assertEquals(0, cafe.consultarFavoritos("mesero1").size());
    }

    @Test
    public void testRF17EmpleadoSugiereNuevoPlatillo() throws Exception {
        SugerenciaPlato sugerencia = cafe.sugerirPlato(
                "mesero1",
                "Lechona",
                7000,
                false,
                true,
                Arrays.asList("Queso", "Huevo"),
                "COMIDA"
        );

        assertNotNull(sugerencia);
        assertEquals("Lechona", sugerencia.getNombrePropuesto());
        assertEquals(1, cafe.getSugerencias().size());
    }

    @Test
    public void testRF18MeseroRegistraPedidoMesa() throws Exception {
        Cliente cliente = (Cliente) cafe.getUsuarios().get("cliente1");
        Mesa mesa = cafe.asignarMesaACliente(cliente, 2, false, false);

        Pedido pedido = cafe.registrarPedidoMesero("mesero1", mesa.getIdMesa());

        assertNotNull(pedido);
        assertEquals(mesa.getIdMesa(), pedido.getMesa().getIdMesa());
    }

    @Test
    public void testRF19MeseroGestionaPrestamoCliente() throws Exception {
        Cliente cliente = (Cliente) cafe.getUsuarios().get("cliente1");
        Mesa mesa = cafe.asignarMesaACliente(cliente, 2, false, false);

        String idJuego = null;
        for (JuegoMesaPrestamo j : cafe.getJuegosPrestamo().values()) {
            if (j.getNombre().equals("FIFA")) {
                idJuego = j.getIdJuegoPrestamo();
            }
        }

        Prestamo prestamo = cafe.prestamoDeJuegos(idJuego, "mesero1", mesa.getIdMesa(), "cliente1");

        assertNotNull(prestamo);
        assertEquals("cliente1", prestamo.getUsuario().getLogin());
        assertFalse(cafe.getJuegosPrestamo().get(idJuego).isDisponible());
    }

    @Test
    public void testRF20CocineroPreparaPedido() throws Exception {
        Cliente cliente = (Cliente) cafe.getUsuarios().get("cliente1");
        Mesa mesa = cafe.asignarMesaACliente(cliente, 2, false, false);

        Pedido pedido = cafe.registrarPedidoMesero("mesero1", mesa.getIdMesa());

        String idProducto = cafe.getMenu().keySet().iterator().next();
        cafe.agregarProductoAPedido(pedido, idProducto, 1);
        cafe.confirmarPedido(pedido);

        cafe.prepararPedido("cocinero1", pedido.getIdPedido());

        assertEquals("PREPARADO", pedido.getEstado());
    }
}