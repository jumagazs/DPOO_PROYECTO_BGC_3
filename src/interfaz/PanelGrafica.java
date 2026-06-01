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
import mesas.Reserva;

public class PanelGrafica extends JPanel {

    private Cafe cafe;
    private String tipoGrafica;
    private String nombreJuego;
    private LocalDate fechaInicioBarras;
    private LocalDate fechaInicioLineas;
    

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
 
            if (!cafe.existeJuegoPorNombre(nombreJuego)) {
                JOptionPane.showMessageDialog(this, "No existe ese juego.");
                this.tipoGrafica = "";
                this.nombreJuego = "";
                this.repaint();
                return;
            }
        } else if (tipoGrafica.equals("BARRAS")) {
            this.fechaInicioBarras = pedirFecha("Fecha de inicio del rango (yyyy-MM-dd):\n"
                    + "Se mostraran 5 dias a partir de esa fecha.");
            if (this.fechaInicioBarras == null) {
                this.tipoGrafica = "";
                return;
            }
 
        } else if (tipoGrafica.equals("LINEAS")) {
            this.fechaInicioLineas = pedirFecha("Fecha de inicio de la semana (yyyy-MM-dd):\n"
                    + "Se mostraran 7 dias a partir de esa fecha.");
            if (this.fechaInicioLineas == null) {
                this.tipoGrafica = "";
                return;
            }
        }
 
        this.repaint();
    }
    
    private LocalDate pedirFecha(String mensaje) {
        String texto = JOptionPane.showInputDialog(this, mensaje);
        if (texto == null || texto.trim().isEmpty()) {
            return null;
        }
        try {
            return LocalDate.parse(texto.trim());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Fecha invalida. Use el formato yyyy-MM-dd.");
            return null;
        }
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
    	 
        int[] dist = cafe.distribucionCopiasJuego(nombreJuego);
        int prestamo = dist[0];
        int venta = dist[1];
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
    	
        if (fechaInicioBarras == null) {
            return;
        }
        

        LocalDate inicio = fechaInicioBarras;
        LocalDate fin = inicio.plusDays(4);

        Map<LocalDate, Double> juegos = cafe.ventasNetasJuegosPorDia(inicio, fin);
        Map<LocalDate, Double> cafeteria = cafe.ventasNetasCafeteriaPorDia(inicio, fin);

        g2d.setColor(Color.BLACK);
        g2d.setFont(g2d.getFont().deriveFont(Font.BOLD, 20));

        g2d.drawString("Ventas últimos 5 días", 50, 40);

        int x = 100;

        double maximo = 1;

        for (int i = 0; i < 5; i++) {
            LocalDate dia = inicio.plusDays(i);
            maximo = Math.max(maximo, juegos.getOrDefault(dia, 0.0));
            maximo = Math.max(maximo, cafeteria.getOrDefault(dia, 0.0));
        }

        int alturaMaxima = 300;

        for (int i = 0; i < 5; i++) {
            LocalDate dia = inicio.plusDays(i);
 
            double valorJuegos = juegos.getOrDefault(dia, 0.0);
            double valorCafe   = cafeteria.getOrDefault(dia, 0.0);
 
            int altoJuegos = (int) ((valorJuegos / maximo) * alturaMaxima);
            int altoCafe   = (int) ((valorCafe / maximo) * alturaMaxima);
 
            g2d.setColor(Color.BLUE);
            g2d.fillRect(x, 450 - altoJuegos, 30, altoJuegos);
            g2d.setColor(Color.ORANGE);
            g2d.fillRect(x + 35, 450 - altoCafe, 30, altoCafe);
 
            g2d.setColor(Color.BLACK);
            g2d.drawString(dia.toString().substring(5), x - 5, 480);
 
            x += 120;
        }
        g2d.drawLine(80, 450, 720, 450);
        g2d.setColor(Color.BLUE);
        g2d.fillRect(50, 520, 20, 20);

        g2d.setColor(Color.BLACK);
        g2d.drawString("Juegos", 80, 535);

        g2d.setColor(Color.ORANGE);
        g2d.fillRect(180, 520, 20, 20);

        g2d.setColor(Color.BLACK);
        g2d.drawString("Cafetería", 210, 535);
    }

    private void dibujarLineas(Graphics2D g2d) {
    	
        if (fechaInicioLineas == null) {
            return;
        }

    		LocalDate inicio = fechaInicioLineas;
    		LocalDate fin = inicio.plusDays(6);
        Map<LocalDate, Integer> reservasPorDia = cafe.reservasPorDia(inicio, fin);


        g2d.setColor(Color.BLACK);
        g2d.setFont(g2d.getFont().deriveFont(Font.BOLD, 20));
        g2d.drawString("Reservas durante la última semana", 40, 40);

        int origenX = 80;
        int origenY = 450;

        g2d.drawLine(origenX, origenY, 760, origenY);

        g2d.drawLine(origenX, 100, origenX, origenY);

        int maxReservas = 1;

        for (int i = 0; i < 7; i++) {
            int r = reservasPorDia.getOrDefault(inicio.plusDays(i), 0);
            if (r > maxReservas) maxReservas = r;
        }
        
        int anchoDisponible = 600;
        int pasoX = anchoDisponible / 6;

        int alturaMaxima = 300;

        int xAnterior = -1;
        int yAnterior = -1;

        for (int i = 0; i < 7; i++) {
            LocalDate dia = inicio.plusDays(i);
            int reservas = reservasPorDia.getOrDefault(dia, 0);
 
            int x = origenX + (i * pasoX);
            int y = origenY - (int) (((double) reservas / maxReservas) * alturaMaxima);
 
            g2d.setColor(Color.BLUE);
            g2d.fillOval(x - 4, y - 4, 8, 8);
            if (xAnterior != -1) {
                g2d.drawLine(xAnterior, yAnterior, x, y);
            }
            g2d.setColor(Color.BLACK);
            g2d.drawString(String.valueOf(reservas), x - 5, y - 10);
            g2d.drawString(dia.toString().substring(5), x - 15, origenY + 20);
 
            xAnterior = x;
            yAnterior = y;
        }
        
        g2d.setColor(Color.BLUE);
        g2d.drawLine(100, 520, 140, 520);
        g2d.fillOval(118, 516, 8, 8);
        g2d.setColor(Color.BLACK);
        g2d.drawString(
                "Cantidad de reservas",
                160,
                525
        );
    }
}