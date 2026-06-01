package interfaz;

import modelo.Cafe;
import modelo.GestorPersistencia;

public class CierreVentana extends java.awt.event.WindowAdapter {
    
    private Cafe cafe;
    private GestorPersistencia gp;

    public CierreVentana(Cafe cafe, GestorPersistencia gp) {
        this.cafe = cafe;
        this.gp = gp;
    }

    @Override
    public void windowClosing(java.awt.event.WindowEvent e) {
        try { 
            gp.guardarTodo(cafe); 
        } catch (Exception ex) { 
            ex.printStackTrace(); 
        }
    }
}