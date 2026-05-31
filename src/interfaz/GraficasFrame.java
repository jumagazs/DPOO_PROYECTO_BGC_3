package interfaz;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

import modelo.Cafe;

public class GraficasFrame extends JFrame {

    private Cafe cafe;
    private PanelOpcionesGraficas panelOpciones;
    private PanelGrafica panelGrafica;

    public GraficasFrame(Cafe cafe) {
        this.cafe = cafe;

        this.panelGrafica = new PanelGrafica(cafe);
        this.panelOpciones = new PanelOpcionesGraficas(this, panelGrafica);

        this.setTitle("Gráficas del sistema");
        this.setSize(1000, 700);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int ancho = (int) screenSize.getWidth();
        int alto = (int) screenSize.getHeight();
        this.setLocation(ancho / 2 - this.getWidth() / 2, alto / 2 - this.getHeight() / 2);

        this.setLayout(new BorderLayout());

        this.add(panelOpciones, BorderLayout.WEST);
        this.add(panelGrafica, BorderLayout.CENTER);

        this.setVisible(true);
    }
}