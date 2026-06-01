package interfaz;

import javax.swing.*;

import juegos.JuegoMesa;
import mesas.Mesa;

import java.awt.*;
import java.time.LocalDateTime;

import modelo.Cafe;
import modelo.GestorPersistencia;
import pedidos.Pedido;
import prestamos.Prestamo;
import productos.ProductoMenu;
import torneos.Torneo;
import usuarios.Cliente;
import usuarios.Mesero;
import usuarios.Usuario;
import ventas.VentaJuego;

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
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        addWindowListener(new CierreVentana(cafe, gp)); 

        JPanel panel = new JPanel(new GridLayout(0, 1, 6, 6));

        JButton bCatalogo = new JButton("Ver catalogo juegos prestamo");
        JButton bMenu = new JButton("Ver menu");
        JButton bComprar = new JButton("Comprar juego");
        JButton bVerFav = new JButton("Ver favoritos");
        JButton bAgregarFav = new JButton("Agregar favorito");
        JButton bEliminarFav = new JButton("Eliminar favorito");
        JButton bTomarMesa = new JButton("Tomar mesa (RF4)");
        JButton bPrestamo = new JButton("Solicitar prestamo (RF5)");
        JButton bDevolver = new JButton("Devolver juego (RF6)");
        JButton bPedido = new JButton("Realizar pedido cafeteria (RF8)");
        JButton bPuntos = new JButton("Ver puntos de fidelidad (RF10)");
        JButton bVerTorneos = new JButton("Ver torneos disponibles");
        JButton bInscribir = new JButton("Inscribirse a torneo");
        JButton bDesinscribir = new JButton("Desinscribirse de torneo");
        JButton bComprarTorneo = new JButton("Comprar con descuento de torneo");
        JButton bLiberar = new JButton("Liberar mesa");
        JButton bReservar = new JButton("Reservar mesa");

        panel.add(bCatalogo); 
        panel.add(bMenu); 
        panel.add(bComprar);
        panel.add(bVerFav); 
        panel.add(bAgregarFav); 
        panel.add(bEliminarFav);
        panel.add(bTomarMesa); 
        panel.add(bPrestamo); 
        panel.add(bDevolver);
        panel.add(bPedido); 
        panel.add(bPuntos); 
        panel.add(bVerTorneos);
        panel.add(bInscribir); 
        panel.add(bDesinscribir); 
        panel.add(bComprarTorneo);
        panel.add(bLiberar); 
        panel.add(bReservar);
        
        add(new JScrollPane(panel));
        setVisible(true);
        
        bCatalogo.addActionListener(e -> {
            try {
                StringBuilder sb = new StringBuilder();
                for (JuegoMesa j : cafe.consultarCatalogoJuegosPrestamo()) {
                    sb.append(j).append("\n");
                }
                JOptionPane.showMessageDialog(this, sb.toString(), "Catálogo préstamo",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) { 
            	JOptionPane.showMessageDialog(this, ex.getMessage()); 
            	}
        });
        
        bMenu.addActionListener(e -> {
            StringBuilder sb = new StringBuilder();
            for (ProductoMenu pr : cafe.consultarMenu()) {
            		sb.append(pr).append("\n");
            }
            JOptionPane.showMessageDialog(this, sb.toString());
        });
        
        bComprar.addActionListener(e -> {
            try {
                String idJuego = JOptionPane.showInputDialog("ID juego venta:");
                int cantidad = Integer.parseInt(JOptionPane.showInputDialog("Cantidad:"));
                int puntos = Integer.parseInt(JOptionPane.showInputDialog("Puntos a usar (0 si ninguno):"));
                String codigo = JOptionPane.showInputDialog("Codigo descuento (vacio si ninguno):");
                VentaJuego v = cafe.comprarJuegoConDescuento(login, idJuego, cantidad, puntos, codigo);
                JOptionPane.showMessageDialog(this,
                        "Compra realizada | Total: " + v.getTotal() + " | Puntos: " + v.getPuntosGenerados());
            } catch (Exception ex) {
            	JOptionPane.showMessageDialog(this, ex.getMessage());
            	}
        });
        

        bVerFav.addActionListener(e -> {
            try {
                StringBuilder sb = new StringBuilder();
                for (JuegoMesa j : cafe.consultarFavoritos(login)) sb.append(j).append("\n");
                JOptionPane.showMessageDialog(this, sb.toString());
            } catch (Exception ex) { 
            	JOptionPane.showMessageDialog(this, ex.getMessage()); 
            	}
        });
        
        bAgregarFav.addActionListener(e -> {
            try {
                String idJuego = JOptionPane.showInputDialog("ID juego:");
                cafe.agregarJuegoFavoritoAUsuario(login, idJuego);
                JOptionPane.showMessageDialog(this, "Favorito agregado.");
            } catch (Exception ex) { 
            	JOptionPane.showMessageDialog(this, ex.getMessage());
            	}
        });
        
        bEliminarFav.addActionListener(e -> {
            try {
                String idJuego = JOptionPane.showInputDialog("ID juego:");
                cafe.eliminarJuegoFavoritoDeUsuario(login, idJuego);
                JOptionPane.showMessageDialog(this, "Favorito eliminado.");
            } catch (Exception ex) { 
            	JOptionPane.showMessageDialog(this, ex.getMessage()); 
            	}
        });
        
        bTomarMesa.addActionListener(e -> {
            try {
                int personas = Integer.parseInt(JOptionPane.showInputDialog("Cantidad de personas:"));
                boolean jovenes = JOptionPane.showConfirmDialog(this, "Hay jovenes (5-18)?",
                        "Mesa", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
                boolean ninos = JOptionPane.showConfirmDialog(this, "Hay ninos (<5)?",
                        "Mesa", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
                Mesa m = cafe.asignarMesaACliente(login, personas, jovenes, ninos);
                JOptionPane.showMessageDialog(this, "Mesa asignada: " + m.getIdMesa());
            } catch (Exception ex) { 
            	JOptionPane.showMessageDialog(this, ex.getMessage()); 
            	}
        });
        
        bPrestamo.addActionListener(e -> {
            try {
                String idJuego = JOptionPane.showInputDialog("ID juego:");
                Prestamo p = cafe.solicitarPrestamoJuegoFlexible(login, idJuego);
                if (!p.isFueExplicado()) {
                    JOptionPane.showMessageDialog(this,
                            "Advertencia: no hay mesero capacitado para explicar este juego.",
                            "Advertencia", JOptionPane.WARNING_MESSAGE);
                }
                JOptionPane.showMessageDialog(this, "Préstamo registrado: " + p.getIdPrestamo());
            } catch (Exception ex) { 
            	JOptionPane.showMessageDialog(this, ex.getMessage());
            	}
        });
        
        bDevolver.addActionListener(e -> {
            try {
                String id = JOptionPane.showInputDialog("ID prestamo:");
                cafe.devolverJuego(id);
                JOptionPane.showMessageDialog(this, "Juego devuelto.");
            } catch (Exception ex) { 
            	JOptionPane.showMessageDialog(this, ex.getMessage()); 
            	}
        });

        bPedido.addActionListener(e -> {
            try {
                String idMesa      = JOptionPane.showInputDialog("ID mesa:");
                String loginMesero = cafe.getLoginMeseroDisponible();
                Pedido pedido      = cafe.registrarPedidoMesero(loginMesero, idMesa);
                String idProd      = JOptionPane.showInputDialog("ID producto (vacío para terminar):");
                while (idProd != null && !idProd.isEmpty()) {
                    int cant = Integer.parseInt(JOptionPane.showInputDialog("Cantidad:"));
                    cafe.agregarProductoAPedido(pedido, idProd, cant);
                    idProd = JOptionPane.showInputDialog("ID producto (vacío para terminar):");
                }
                cafe.confirmarPedido(pedido);
                JOptionPane.showMessageDialog(this, "Pedido confirmado | Total: " + pedido.getTotal());
            } catch (Exception ex) { 
            	JOptionPane.showMessageDialog(this, ex.getMessage()); 
            	}
        });

        bPuntos.addActionListener(e -> {
            try {
                int puntos = cafe.getPuntosCliente(login);
                double descuento = cafe.getDescuentoTorneoCliente(login);
                JOptionPane.showMessageDialog(this,
                        "Puntos de fidelidad: " + puntos
                        + "\nDescuento de torneo activo: " + descuento);
            } catch (Exception ex) { 
            	JOptionPane.showMessageDialog(this, ex.getMessage()); 
            	}
        });

        bVerTorneos.addActionListener(e -> {
            StringBuilder sb = new StringBuilder();
            for (Torneo t : cafe.getTorneos()) {
                sb.append("ID: ").append(t.getId())
                  .append(" | Juego: ").append(t.getJuego().getNombre())
                  .append(" | Dia: ").append(t.getDia())
                  .append(" | CuposFan: ").append(t.getCuposFanaticos())
                  .append(" | CuposReg: ").append(t.getCuposRegulares()).append("\n");
            }
            JOptionPane.showMessageDialog(this, sb.toString());
        });

        bInscribir.addActionListener(e -> {
            try {
                String idTorneo = JOptionPane.showInputDialog("ID torneo:");
                int cupos = Integer.parseInt(JOptionPane.showInputDialog("Cupos a tomar (1-3):"));
                cafe.inscribirUsuarioTorneo(login, idTorneo, cupos);
                JOptionPane.showMessageDialog(this, "Inscrito.");
            } catch (Exception ex) { 
            	JOptionPane.showMessageDialog(this, ex.getMessage());
            	}
        });

        bDesinscribir.addActionListener(e -> {
            try {
                String idTorneo = JOptionPane.showInputDialog("ID torneo:");
                cafe.desinscribirUsuarioTorneo(login, idTorneo);
                JOptionPane.showMessageDialog(this, "Desinscrito.");
            } catch (Exception ex) { 
            	JOptionPane.showMessageDialog(this, ex.getMessage());
            	}
        });

        bComprarTorneo.addActionListener(e -> {
            try {
                String idJuego = JOptionPane.showInputDialog("ID juego venta:");
                int cant = Integer.parseInt(JOptionPane.showInputDialog("Cantidad:"));
                VentaJuego v = cafe.comprarJuegoConDescuentoTorneo(login, idJuego, cant);
                JOptionPane.showMessageDialog(this, "Compra con descuento torneo | Total: " + v.getTotal());
            } catch (Exception ex) { 
            	JOptionPane.showMessageDialog(this, ex.getMessage());
            	}
        });

        bLiberar.addActionListener(e -> {
            try {
                cafe.liberarMesa(login);
                JOptionPane.showMessageDialog(this, "Mesa liberada. Hasta pronto!");
            } catch (Exception ex) {
            	JOptionPane.showMessageDialog(this, ex.getMessage()); 
            	}
        });

        bReservar.addActionListener(e -> {
            try {
                StringBuilder sb = new StringBuilder("Mesas:\n");
                for (Mesa m : cafe.getMesas()) {
                    sb.append(m.getIdMesa()).append(" - Capacidad: ").append(m.getCapacidad()).append("\n");
                }
                JOptionPane.showMessageDialog(this, sb.toString(), "Mesas disponibles",
                        JOptionPane.INFORMATION_MESSAGE);
                String idMesa = JOptionPane.showInputDialog("ID de la mesa:");
                int personas = Integer.parseInt(JOptionPane.showInputDialog("Cantidad de personas:"));
                String fechaTexto = JOptionPane.showInputDialog(
                        "Fecha y hora (AAAA-MM-DD HH:MM)\nEjemplo: 2026-06-10 18:30");
                LocalDateTime fecha = LocalDateTime.parse(fechaTexto.replace(" ", "T"));
                cafe.reservarMesa(login, idMesa, personas, fecha);
                JOptionPane.showMessageDialog(this, "Reserva creada exitosamente");
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
        });
        
    }
    
}