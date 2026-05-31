package interfaz;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

public class PanelOpcionesGraficas extends JPanel {

    private PanelGrafica panelGrafica;

    public PanelOpcionesGraficas(
            GraficasFrame ventana,
            PanelGrafica panelGrafica) {

        this.panelGrafica = panelGrafica;

        setLayout(new GridLayout(3, 1));

        JButton btnPastel = new JButton("Pastel");
        JButton btnBarras = new JButton("Barras");
        JButton btnLineas = new JButton("Líneas");

        add(btnPastel);
        add(btnBarras);
        add(btnLineas);

        btnPastel.addActionListener(e -> {
            panelGrafica.setTipoGrafica("PASTEL");
        });

        btnBarras.addActionListener(e -> {
            panelGrafica.setTipoGrafica("BARRAS");
        });

        btnLineas.addActionListener(e -> {
            panelGrafica.setTipoGrafica("LINEAS");
        });
    }
}