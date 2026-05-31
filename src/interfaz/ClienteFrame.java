package interfaz;

import javax.swing.*;
import java.awt.*;

import modelo.Cafe;
import modelo.GestorPersistencia;

public class ClienteFrame extends JFrame {

    private Cafe cafe;
    private String login;
    private GestorPersistencia gp;

    public ClienteFrame(Cafe cafe, String login, GestorPersistencia gp) {
        this.cafe = cafe;
        this.login = login;
        this.gp = gp;

        setTitle("Cliente - " + login);
        setSize(500,400);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(7,1));

        JButton btnMenu = new JButton("Ver Menu");
        JButton btnMesa = new JButton("Asignar Mesa");
        JButton btnPrestamo = new JButton("Pedir Juego");
        JButton btnCompra = new JButton("Comprar Juego");
        JButton btnReserva = new JButton("Reservar Mesa");

        panel.add(btnMenu);
        panel.add(btnMesa);
        panel.add(btnPrestamo);
        panel.add(btnCompra);
        panel.add(btnReserva);
        
        add(panel);
        setVisible(true);

        btnMenu.addActionListener(e -> {
            StringBuilder sb = new StringBuilder();
            cafe.consultarMenu().forEach(p -> sb.append(p).append("\n"));
            JOptionPane.showMessageDialog(this, sb.toString());
        });

        btnMesa.addActionListener(e -> {
            try {
                int personas = Integer.parseInt(JOptionPane.showInputDialog("Personas:"));
                boolean jovenes = JOptionPane.showConfirmDialog(this, "¿Hay jóvenes (5-18 años)?",
                        "Mesa", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
                boolean ninos = JOptionPane.showConfirmDialog(this, "¿Hay niños (<5 años)?",
                        "Mesa", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
                cafe.asignarMesaACliente(
                        (usuarios.Cliente) cafe.getUsuarios().get(login),
                        personas, jovenes, ninos);
                gp.guardarTodo(cafe);
                JOptionPane.showMessageDialog(this, "Mesa asignada");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        });

        btnPrestamo.addActionListener(e -> {
            try {
                String idJuego = JOptionPane.showInputDialog("ID Juego:");
                cafe.solicitarPrestamoJuegoFlexible(login, idJuego);

                gp.guardarTodo(cafe);
                JOptionPane.showMessageDialog(this, "Prestamo realizado");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        });

        btnCompra.addActionListener(e -> {
            try {
                String idJuego = JOptionPane.showInputDialog("ID Juego:");
                int cant = Integer.parseInt(JOptionPane.showInputDialog("Cantidad:"));
                int puntos = Integer.parseInt(JOptionPane.showInputDialog("Puntos a usar (0 si ninguno):"));
                String codigo = JOptionPane.showInputDialog("Código descuento (dejar vacío si ninguno):");
                cafe.comprarJuegoConDescuento(login, idJuego, cant, puntos, codigo);
                gp.guardarTodo(cafe);
                JOptionPane.showMessageDialog(this, "Compra realizada");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        });
        
        StringBuilder sb = new StringBuilder();

        for (mesas.Mesa m : cafe.getMesas()) {

            sb.append(m.getIdMesa())
              .append(" - Capacidad: ")
              .append(m.getCapacidad())
              .append("\n");
        }

        JOptionPane.showMessageDialog(
                this,
                sb.toString(),
                "Mesas disponibles",
                JOptionPane.INFORMATION_MESSAGE
        );
        
        btnReserva.addActionListener(e -> {

            try {

                String idMesa = JOptionPane.showInputDialog(
                        this,
                        "ID de la mesa:"
                );

                int personas = Integer.parseInt(
                        JOptionPane.showInputDialog(
                                this,
                                "Cantidad de personas:"
                        )
                );

                String fechaTexto = JOptionPane.showInputDialog(
                        this,
                        "Fecha y hora (AAAA-MM-DD HH:MM)\nEjemplo: 2026-06-10 18:30"
                );

                java.time.LocalDateTime fecha =
                        java.time.LocalDateTime.parse(
                                fechaTexto.replace(" ", "T")
                        );

                cafe.reservarMesa(
                        login,
                        idMesa,
                        personas,
                        fecha
                );

                gp.guardarTodo(cafe);

                JOptionPane.showMessageDialog(
                        this,
                        "Reserva creada exitosamente"
                );

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                        this,
                        ex.getMessage()
                );
            }
        });
    }
    
}