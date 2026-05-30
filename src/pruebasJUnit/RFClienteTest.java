package pruebasJUnit;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import juegos.JuegoMesaPrestamo;
import mesas.Mesa;
import modelo.Cafe;
import prestamos.Prestamo;
import usuarios.Administrador;
import usuarios.Cliente;
import usuarios.Usuario;
import productos.ProductoMenu;
import pedidos.Pedido;
import ventas.VentaJuego;
import juegos.JuegoMesa;

public class RFClienteTest {

    private Cafe cafe;
    private Cliente cliente;

    @BeforeEach
    public void setup() throws Exception {
        cafe = new Cafe();

        cafe.getUsuarios().put("admin", new Administrador("admin", "admin"));
        cliente = cafe.registrarCliente("cliente1", "1234");

        cafe.agregarMesa(new Mesa("M1", 2));
        cafe.agregarMesa(new Mesa("M2", 4));
        cafe.agregarMesa(new Mesa("M3", 6));

        cafe.agregarJuegoPrestamo(
            "admin", "FIFA", 2020, "Sony", "Deporte",
            2, 10, false, true, true, true, 0, "Bueno"
        );
    }

    @Test
    public void testRF1RegistrarCliente() throws Exception {
        Cliente nuevo = cafe.registrarCliente("cliente2", "abcd");
        assertNotNull(nuevo);
        assertEquals("cliente2", nuevo.getLogin());
        assertEquals(0, nuevo.getPuntosFidelidad());
    }

    @Test
    public void testRF1NoPermiteLoginDuplicado() {
        Exception e = assertThrows(Exception.class, () -> {
            cafe.registrarCliente("cliente1", "otra");
        });

        assertEquals("Ya existe un usuario registrado con ese login.", e.getMessage());
    }

    @Test
    public void testRF2IniciarSesionCorrectamente() throws Exception {
        Usuario usuario = cafe.iniciarSesion("cliente1", "1234");

        assertNotNull(usuario);
        assertEquals("cliente1", usuario.getLogin());
    }

    @Test
    public void testRF2BloquearContrasenaIncorrecta() {
        Exception e = assertThrows(Exception.class, () -> {
            cafe.iniciarSesion("cliente1", "mala");
        });

        assertEquals("La contraseña es incorrecta.", e.getMessage());
    }

    @Test
    public void testRF3ConsultarCatalogoJuegos() {
        assertEquals(1, cafe.consultarCatalogoJuegosPrestamo().size());

        JuegoMesaPrestamo juego = cafe.consultarCatalogoJuegosPrestamo().iterator().next();
        assertEquals("FIFA", juego.getNombre());
        assertTrue(juego.isDisponible());
    }

    @Test
    public void testRF4AsignarMesaMasPequenaDisponible() throws Exception {
        Mesa mesa = cafe.asignarMesaACliente(cliente, 3, false, false);

        assertEquals("M2", mesa.getIdMesa());
        assertTrue(mesa.isOcupada());
        assertEquals(3, mesa.getPersonasActuales());
    }

    @Test
    public void testRF5SolicitarPrestamoJuego() throws Exception {
        Mesa mesa = cafe.asignarMesaACliente(cliente, 3, false, false);

        String idJuego = cafe.getJuegosPrestamo().keySet().iterator().next();

        Prestamo prestamo = cafe.solicitarPrestamoJuegoFlexible("cliente1", idJuego);

        assertNotNull(prestamo);
        assertEquals("cliente1", prestamo.getUsuario().getLogin());
        assertEquals(mesa.getIdMesa(), prestamo.getMesa().getIdMesa());
        assertFalse(cafe.getJuegosPrestamo().get(idJuego).isDisponible());
    }

    @Test
    public void testRF6DevolverJuegoPrestado() throws Exception {
        cafe.asignarMesaACliente(cliente, 3, false, false);

        String idJuego = cafe.getJuegosPrestamo().keySet().iterator().next();
        Prestamo prestamo = cafe.solicitarPrestamoJuegoFlexible("cliente1", idJuego);

        cafe.devolverJuego(prestamo.getIdPrestamo());

        assertTrue(prestamo.fueDevuelto());
        assertTrue(cafe.getJuegosPrestamo().get(idJuego).isDisponible());
    }

    @Test
    public void testRF6NoPermiteDobleDevolucion() throws Exception {
        cafe.asignarMesaACliente(cliente, 3, false, false);

        String idJuego = cafe.getJuegosPrestamo().keySet().iterator().next();
        Prestamo prestamo = cafe.solicitarPrestamoJuegoFlexible("cliente1", idJuego);

        cafe.devolverJuego(prestamo.getIdPrestamo());

        Exception e = assertThrows(Exception.class, () -> {
            cafe.devolverJuego(prestamo.getIdPrestamo());
        });

        assertEquals("Ese préstamo ya fue devuelto.", e.getMessage());
    }

    @Test
    public void testRF7ConsultarMenu() throws Exception {
        cafe.agregarBebida("admin", "Milo", 8500, true, false, true);
        cafe.agregarPasteleria("admin", "Pastel de pollo", 9000, true, java.util.Arrays.asList("Gluten"));

        assertEquals(2, cafe.consultarMenu().size());

        boolean encontro = false;
        for (ProductoMenu p : cafe.consultarMenu()) {
            if (p.getNombre().equals("Milo")) {
                encontro = true;
            }
        }

        assertTrue(encontro);
    }

    @Test
    public void testRF8RealizarPedidoCafeteria() throws Exception {
        cafe.registrarMesero("admin", "mesero1", "1234");
        cafe.agregarBebida("admin", "Milo", 8500, true, false, true);

        Mesa mesa = cafe.asignarMesaACliente(cliente, 2, false, false);
        Pedido pedido = cafe.registrarPedidoMesero("mesero1", mesa.getIdMesa());

        String idProducto = cafe.getMenu().keySet().iterator().next();

        cafe.agregarProductoAPedido(pedido, idProducto, 2);
        cafe.confirmarPedido(pedido);

        assertEquals("EN PREPARACIÓN", pedido.getEstado());
        assertTrue(pedido.getTotal() > 0);
        assertEquals(1, pedido.getDetalles().size());
    }

    @Test
    public void testRF9ComprarJuegoMesa() throws Exception {
        cafe.agregarJuegoVenta("admin", "Halo", 2021, "Microsoft", "Accion",
                1, 5, false, false, true, 180000, 5, 120000);

        String idJuegoVenta = cafe.getJuegosVenta().keySet().iterator().next();

        VentaJuego venta = cafe.comprarJuegoConDescuento("cliente1", idJuegoVenta, 1, 0, "");

        assertNotNull(venta);
        assertTrue(venta.getTotal() > 0);
        assertTrue(cliente.getPuntosFidelidad() > 0);
    }

    @Test
    public void testRF10UsarPuntosFidelidad() throws Exception {
        cafe.agregarJuegoVenta("admin", "Halo", 2021, "Microsoft", "Accion",
                1, 5, false, false, true, 180000, 5, 120000);

        String idJuegoVenta = cafe.getJuegosVenta().keySet().iterator().next();

        VentaJuego primeraVenta = cafe.comprarJuegoConDescuento("cliente1", idJuegoVenta, 1, 0, "");
        int puntos = cliente.getPuntosFidelidad();

        VentaJuego segundaVenta = cafe.comprarJuegoConDescuento("cliente1", idJuegoVenta, 1, puntos, "");

        assertTrue(puntos > 0);
        assertTrue(segundaVenta.getTotal() < primeraVenta.getTotal());
    }

    @Test
    public void testRF11GestionarJuegosFavoritos() throws Exception {
        String idJuego = cafe.getJuegosPrestamo().keySet().iterator().next();

        cafe.agregarJuegoFavoritoAUsuario("cliente1", idJuego);

        assertEquals(1, cafe.consultarFavoritos("cliente1").size());

        JuegoMesa favorito = cafe.consultarFavoritos("cliente1").get(0);
        assertEquals("FIFA", favorito.getNombre());

        cafe.eliminarJuegoFavoritoDeUsuario("cliente1", idJuego);

        assertEquals(0, cafe.consultarFavoritos("cliente1").size());
    }
}