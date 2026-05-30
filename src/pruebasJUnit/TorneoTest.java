package pruebasJUnit;

import static org.junit.jupiter.api.Assertions.*;
import java.time.DayOfWeek;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import juegos.JuegoMesaPrestamo;
import torneos.*;
import usuarios.*;

public class TorneoTest {

    private JuegoMesaPrestamo juego;
    private Torneo torneo;
    private Cliente cliente;

    @BeforeEach
    public void setup() {
        juego = new JuegoMesaPrestamo(
            "Parques", 2012, "Mattel", "Juego de mesa",
            2, 10, false, true, true,
            "JP33", true, 0, "Bueno"
        );

        torneo = new TorneoAmistoso("TOR1", 4, juego, DayOfWeek.FRIDAY, 0.1);
        cliente = new Cliente("cliente1", "1234");
    }

    @Test
    public void testInscribirUsuario() throws Exception {
        torneo.inscribirUsuario(cliente, 1);

        assertTrue(torneo.getInscritosRegulares().containsKey("cliente1"));
        assertEquals(1, torneo.getInscritosRegulares().get("cliente1"));
    }

    @Test
    public void testBloqueoPorCupos() {
        Exception e = assertThrows(Exception.class, () -> {
            torneo.inscribirUsuario(cliente, 4);
        });

        assertEquals("Se permiten entre 1 y 3 cupos por usuario.", e.getMessage());
    }
    
    @Test
    public void testInscripcionFanaticoTomaCupoFanatico() throws Exception {
        cliente.agregarJuegoFavorito(juego);
        torneo.inscribirUsuario(cliente, 1);
        assertTrue(torneo.getInscritosFanaticos().containsKey("cliente1"));
        assertFalse(torneo.getInscritosRegulares().containsKey("cliente1"));
    }

    @Test
    public void testFanaticoTomaCupoRegularSiSeAgotanFanaticos() throws Exception {
        Cliente fan1 = new Cliente("fan1", "x");
        Cliente fan2 = new Cliente("fan2", "x");
        fan1.agregarJuegoFavorito(juego);
        fan2.agregarJuegoFavorito(juego);
        
        torneo.inscribirUsuario(fan1, 1); // toma único cupo fanático
        torneo.inscribirUsuario(fan2, 1); // sin cupo fan, toma regular
        
        assertTrue(torneo.getInscritosRegulares().containsKey("fan2"));
    }

    @Test
    public void testDesinscribirLiberaCupos() throws Exception {
        int cuposAntes = torneo.getCuposRegulares();
        torneo.inscribirUsuario(cliente, 2);
        torneo.eliminarUsuario(cliente);
        assertEquals(cuposAntes, torneo.getCuposRegulares());
        assertFalse(torneo.getInscritosRegulares().containsKey("cliente1"));
    }

    @Test
    public void testNoPermiteInscribirseDosVeces() throws Exception {
        torneo.inscribirUsuario(cliente, 1);
        Exception e = assertThrows(Exception.class, () -> {
            torneo.inscribirUsuario(cliente, 1);
        });
        assertTrue(e.getMessage().toLowerCase().contains("ya está inscrito"));
    }

    @Test
    public void testPremioAmistosoOtorgaDescuentoAlCliente() throws Exception {
        TorneoAmistoso ta = (TorneoAmistoso) torneo;
        ta.inscribirUsuario(cliente, 1);
        ta.otorgarPremio(cliente);
        assertEquals(0.1, cliente.getPorcentajeDescuentoTorneo(), 0.001);
    }

    @Test
    public void testNoOtorgaPremioSiYaTieneDescuento() throws Exception {
        TorneoAmistoso ta = (TorneoAmistoso) torneo;
        cliente.otorgarDescuentoTorneo(0.05);
        assertThrows(Exception.class, () -> ta.otorgarPremio(cliente));
    }

    @Test
    public void testPremioCompetitivoExcluyeEmpleados() throws Exception {
        torneos.TorneoCompetitivo tc = new torneos.TorneoCompetitivo(
            "TC1", 10, juego, DayOfWeek.FRIDAY, 10000.0);
        
        Cliente c1 = new Cliente("c1", "x");
        Cliente c2 = new Cliente("c2", "x");
        usuarios.Mesero emp = new usuarios.Mesero("emp", "x");
        
        tc.inscribirUsuario(c1, 2);
        tc.inscribirUsuario(c2, 1);
        tc.inscribirUsuario(emp, 1);
        
        assertEquals(30000.0, tc.getPremio(), 0.01);
    }

    @Test
    public void testConsumirDescuentoLoBorraDelCliente() throws Exception {
        cliente.otorgarDescuentoTorneo(0.15);
        double pct = cliente.consumirDescuentoTorneo();
        assertEquals(0.15, pct, 0.001);
        assertEquals(0.0, cliente.getPorcentajeDescuentoTorneo(), 0.001);
    }

    @Test
    public void testNoSePuedeConsumirSiNoTieneDescuento() {
        assertThrows(Exception.class, () -> cliente.consumirDescuentoTorneo());
    }
}