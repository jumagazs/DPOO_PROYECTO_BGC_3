package interfaz;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.DayOfWeek;
import java.util.ArrayList;

import modelo.Cafe;
import modelo.GestorPersistencia;

import usuarios.Empleado;
import usuarios.Turno;

public class AdminFrame extends JFrame {

    private Cafe cafe;
    private String login;
    private GestorPersistencia gp;

    public AdminFrame(Cafe cafe, String login, GestorPersistencia gp) {
        this.cafe = cafe;
        this.login = login;
        this.gp = gp;

        setTitle("Admin - " + login);
        setSize(500,500);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(10,1));

        JButton btnBebida = new JButton("Agregar Bebida");
        JButton btnPasteleria = new JButton("Agregar Pasteleria");
        JButton btnJuegoVenta = new JButton("Agregar Juego Venta");
        JButton btnJuegoPrestamo = new JButton("Agregar Juego Prestamo");
        JButton btnMesero = new JButton("Registrar Mesero");
        JButton btnCocinero = new JButton("Registrar Cocinero");
        JButton btnTurno = new JButton("Asignar Turno");
        JButton btnTorneo = new JButton("Crear Torneo");

        panel.add(btnBebida);
        panel.add(btnPasteleria);
        panel.add(btnJuegoVenta);
        panel.add(btnJuegoPrestamo);
        panel.add(btnMesero);
        panel.add(btnCocinero);
        panel.add(btnTurno);
        panel.add(btnTorneo);

        add(panel);
        setVisible(true);

        btnBebida.addActionListener(e -> {
            try {
                String nombre = JOptionPane.showInputDialog("Nombre:");
                double precio = Double.parseDouble(JOptionPane.showInputDialog("Precio:"));

                cafe.agregarBebida(login, nombre, precio, true, false, true);
                gp.guardarTodo(cafe);

                JOptionPane.showMessageDialog(this, "Bebida creada");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        });

        btnPasteleria.addActionListener(e -> {
            try {
                String nombre = JOptionPane.showInputDialog("Nombre:");
                double precio = Double.parseDouble(JOptionPane.showInputDialog("Precio:"));

                cafe.agregarPasteleria(login, nombre, precio, true, new ArrayList<>());
                gp.guardarTodo(cafe);

                JOptionPane.showMessageDialog(this, "Pasteleria creada");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        });

        btnJuegoVenta.addActionListener(e -> {
            try {
                String nombre = JOptionPane.showInputDialog("Nombre:");
                double precio = Double.parseDouble(JOptionPane.showInputDialog("Precio:"));

                cafe.agregarJuegoVenta(login, nombre, 2020, "Editorial",
                        "Categoria", 2, 4, false, true, true,
                        precio, 10, precio * 0.7);

                gp.guardarTodo(cafe);
                JOptionPane.showMessageDialog(this, "Juego venta agregado");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        });
        
        btnJuegoPrestamo.addActionListener(e -> {
            try {
                String nombre = JOptionPane.showInputDialog("Nombre:");
                int anio = Integer.parseInt(JOptionPane.showInputDialog("Año:"));
                String editor = JOptionPane.showInputDialog("Editorial:");
                String categoria = JOptionPane.showInputDialog("Categoría:");
                int min = Integer.parseInt(JOptionPane.showInputDialog("Min jugadores:"));
                int max = Integer.parseInt(JOptionPane.showInputDialog("Max jugadores:"));
                boolean dificil = JOptionPane.showInputDialog("Es dificil? (true/false)").equalsIgnoreCase("true");
                boolean menores5 = JOptionPane.showInputDialog("Permite menores 5? (true/false)").equalsIgnoreCase("true");
                boolean menores18 = JOptionPane.showInputDialog("Permite menores 18? (true/false)").equalsIgnoreCase("true");

                boolean disponible = true;
                int vecesPrestado = 0;
                String estado = "Nuevo";

                cafe.agregarJuegoPrestamo(
                        login,
                        nombre,
                        anio,
                        editor,
                        categoria,
                        min,
                        max,
                        dificil,
                        menores5,
                        menores18,
                        disponible,
                        vecesPrestado,
                        estado
                );

                gp.guardarTodo(cafe);

                JOptionPane.showMessageDialog(this, "Juego de préstamo agregado");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        });

        btnMesero.addActionListener(e -> {
            try {
                String user = JOptionPane.showInputDialog("Login:");
                String pass = JOptionPane.showInputDialog("Pass:");

                cafe.registrarMesero(login, user, pass);
                gp.guardarTodo(cafe);

                JOptionPane.showMessageDialog(this, "Mesero creado");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        });

        btnCocinero.addActionListener(e -> {
            try {
                String user = JOptionPane.showInputDialog("Login:");
                String pass = JOptionPane.showInputDialog("Pass:");

                cafe.registrarCocinero(login, user, pass);
                gp.guardarTodo(cafe);

                JOptionPane.showMessageDialog(this, "Cocinero creado");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        });

        btnTurno.addActionListener(e -> {
            try {
                String empLogin = JOptionPane.showInputDialog("Login empleado:");

                if (!cafe.getUsuarios().containsKey(empLogin)) {
                    throw new Exception("Empleado no existe");
                }

                Empleado emp = (Empleado) cafe.getUsuarios().get(empLogin);

                Turno turno = new Turno(
                        "T" + System.currentTimeMillis(),
                        LocalDateTime.now(),
                        LocalDateTime.now().plusHours(8),
                        "LUNES",
                        emp
                );

                cafe.agregarTurno(login, empLogin, turno);

                gp.guardarTodo(cafe);
                JOptionPane.showMessageDialog(this, "Turno asignado");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        });

        btnTorneo.addActionListener(e -> {
            try {
                String tipo = JOptionPane.showInputDialog("amistoso / competitivo");
                String idJuego = JOptionPane.showInputDialog("ID Juego:");
                int cupos = Integer.parseInt(JOptionPane.showInputDialog("Cupos:"));

                if (tipo.equalsIgnoreCase("amistoso")) {
                    cafe.crearTorneoAmistoso(login, idJuego, cupos, DayOfWeek.MONDAY, 0.1);
                } else {
                    cafe.crearTorneoCompetitivo(login, idJuego, cupos, DayOfWeek.MONDAY, 5000);
                }

                gp.guardarTodo(cafe);
                JOptionPane.showMessageDialog(this, "Torneo creado");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        });
    }
}