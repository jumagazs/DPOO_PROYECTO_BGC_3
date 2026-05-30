package pruebasJUnit;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import modelo.Cafe;

public class CafeTest {

    private Cafe cafe;

    @BeforeEach
    public void setup() {
        cafe = new Cafe();
    }

    @Test
    public void testRegistrarClienteYLogin() throws Exception {
        cafe.registrarCliente("cliente1", "1234");

        assertNotNull(cafe.getUsuarios().get("cliente1"));

        cafe.iniciarSesion("cliente1", "1234");
    }

    @Test
    public void testNoPermiteDuplicados() throws Exception {
        cafe.registrarCliente("cliente1", "1234");

        Exception e = assertThrows(Exception.class, () -> {
            cafe.registrarCliente("cliente1", "1234");
        });

        assertEquals("Ya existe un usuario registrado con ese login.", e.getMessage());
    }
}