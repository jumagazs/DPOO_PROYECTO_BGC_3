package interfaz;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class PanelOpcionesGraficas extends JPanel implements ActionListener {

    private JButton bPastel;
    private JButton bBarras;
    private JButton bLineas;
    private PanelGrafica panelGrafica;
    private GraficasFrame ventana;

    public PanelOpcionesGraficas(GraficasFrame ventana, PanelGrafica panelGrafica) {
        this.ventana = ventana;
        this.panelGrafica = panelGrafica;

        this.setLayout(new GridLayout(6, 1, 10, 10));

        this.bPastel = new JButton("Disponibilidad");
        this.bBarras = new JButton("Ventas");
        this.bLineas = new JButton("Reservas");

        this.add(bPastel);
        this.add(bBarras);
        this.add(bLineas);

        this.bPastel.addActionListener(this);
        this.bBarras.addActionListener(this);
        this.bLineas.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == bPastel) {
            panelGrafica.setTipoGrafica("PASTEL");
        } else if (e.getSource() == bBarras) {
            panelGrafica.setTipoGrafica("BARRAS");
        } else if (e.getSource() == bLineas) {
            panelGrafica.setTipoGrafica("LINEAS");
        }
    }
}