package interfaz;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Font;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.JPanel;


import juegos.JuegoMesaPrestamo;
import juegos.JuegoMesaVenta;
import modelo.Cafe;
import pedidos.Pedido;
import ventas.VentaJuego;

public class PanelGrafica extends JPanel {

    private Cafe cafe;
    private String tipoGrafica;
    private String nombreJuego;

    public PanelGrafica(Cafe cafe) {
        this.cafe = cafe;
        this.tipoGrafica = "";
        this.nombreJuego = "";
    }

    public void setTipoGrafica(String tipoGrafica) {
        this.tipoGrafica = tipoGrafica;

        if (tipoGrafica.equals("PASTEL")) {
            this.nombreJuego = JOptionPane.showInputDialog(this, "Nombre del juego:");

            if (this.nombreJuego == null || this.nombreJuego.trim().equals("")) {
                this.tipoGrafica = "";
                return;
            }

            if (!existeJuego(nombreJuego)) {
                JOptionPane.showMessageDialog(this, "No existe ese juego.");
                this.tipoGrafica = "";
                this.nombreJuego = "";
                this.repaint();
                return;
            }
        }

        this.repaint();
    }
    
    private boolean existeJuego(String nombreJuego) {
        for (JuegoMesaPrestamo j : cafe.getJuegosPrestamo().values()) {
            if (j.getNombre().equalsIgnoreCase(nombreJuego)) {
                return true;
            }
        }

        for (JuegoMesaVenta j : cafe.getJuegosVenta().values()) {
            if (j.getNombre().equalsIgnoreCase(nombreJuego)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void paint(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, this.getWidth(), this.getHeight());

        if (tipoGrafica.equals("PASTEL")) {
            dibujarPastel(g2d);
        }
        else if (tipoGrafica.equals("BARRAS")) {
            dibujarBarras(g2d);
        }
        else if (tipoGrafica.equals("LINEAS")) {
            dibujarLineas(g2d);
        }
    }

    private void dibujarPastel(Graphics2D g2d) {

        int prestamo = 0;
        int venta = 0;

        for (JuegoMesaPrestamo j : cafe.getJuegosPrestamo().values()) {
            if (j.getNombre().equalsIgnoreCase(nombreJuego)) {
                prestamo++;
            }
        }

        for (JuegoMesaVenta j : cafe.getJuegosVenta().values()) {
            if (j.getNombre().equalsIgnoreCase(nombreJuego)) {
                venta += j.getCantidadStock();
            }
        }

        int total = prestamo + venta;

        if (total == 0) {
            return;
        }

        int anguloPrestamo = (int) ((prestamo * 360.0) / total);

        g2d.setColor(Color.BLACK);
        g2d.setFont(g2d.getFont().deriveFont(Font.BOLD, 22));

        String titulo = "Disponibilidad del juego " + nombreJuego;
        int anchoTitulo = g2d.getFontMetrics().stringWidth(titulo);

        g2d.drawString(titulo, (this.getWidth() - anchoTitulo) / 2, 50);

        g2d.setFont(g2d.getFont().deriveFont(Font.PLAIN, 14));

        g2d.setColor(Color.BLACK);
        g2d.drawString("Copias para préstamo: " + prestamo, 50, 140);
        g2d.drawString("Copias para venta: " + venta, 50, 170);

        g2d.setColor(Color.BLUE);
        g2d.fillRect(50, 230, 25, 25);

        g2d.setColor(Color.BLACK);
        g2d.drawString("Préstamo", 85, 248);

        g2d.setColor(Color.ORANGE);
        g2d.fillRect(50, 280, 25, 25);

        g2d.setColor(Color.BLACK);
        g2d.drawString("Venta", 85, 298);

        g2d.setColor(Color.BLUE);
        g2d.fillArc(420, 180, 240, 240, 0, anguloPrestamo);

        g2d.setColor(Color.ORANGE);
        g2d.fillArc(420, 180, 240, 240, anguloPrestamo, 360 - anguloPrestamo);
    }

    private void dibujarBarras(Graphics2D g2d) {

        Map<LocalDate, Double> juegos = new HashMap<>();

        Map<LocalDate, Double> cafeteria = new HashMap<>();

        for (VentaJuego v : cafe.getVentas()) {

            LocalDate fecha =LocalDate.parse(v.getFecha().substring(0,10));

            juegos.put(fecha, juegos.getOrDefault(fecha, 0.0) + v.getSubTotal());
        }

        for (Pedido p : cafe.getPedidos()) {

            LocalDate fecha = LocalDate.parse(p.getFecha().substring(0,10));

            cafeteria.put(fecha, cafeteria.getOrDefault(fecha, 0.0) + p.getSubtotal());
        }

        LocalDate hoy = LocalDate.now();

        g2d.setColor(Color.BLACK);
        g2d.drawString( "Ventas últimos 5 días", 50, 40);

        int x = 100;

        for (int i = 4; i >= 0; i--) {

            LocalDate dia = hoy.minusDays(i);

            int altoJuegos = (int)(juegos.getOrDefault(dia,0.0) / 1000);

            int altoCafe = (int)(cafeteria.getOrDefault(dia,0.0) / 1000);

            g2d.setColor(Color.BLUE);
            g2d.fillRect(x, 500 - altoJuegos, 30, altoJuegos);

            g2d.setColor(Color.ORANGE);
            g2d.fillRect(x + 35, 500 - altoCafe, 30, altoCafe);

            g2d.setColor(Color.BLACK);
            g2d.drawString(dia.toString().substring(5), x, 530);
            x += 120;
        }

        g2d.setColor(Color.BLUE);
        g2d.fillRect(50, 580, 20, 20);

        g2d.setColor(Color.BLACK);
        g2d.drawString("Juegos", 80, 595);

        g2d.setColor(Color.ORANGE);
        g2d.fillRect(180, 580, 20, 20);

        g2d.setColor(Color.BLACK);
        g2d.drawString("Cafetería", 210, 595);
    }

    private void dibujarLineas(Graphics2D g2d) {

        g2d.setColor(Color.BLACK);

        g2d.drawString("Falta hacerla",100,100);

        g2d.drawString("Creo que todavia no estamos guardando el historial de reservas, toca revisar eso", 100,130);
    }
}