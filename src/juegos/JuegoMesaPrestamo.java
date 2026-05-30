package juegos;

public class JuegoMesaPrestamo extends JuegoMesa {
    private String idJuegoPrestamo;
    private boolean disponible;
    private int vecesPrestado;
    private String estado;

    public JuegoMesaPrestamo(String nombre, int anioPublicacion, String editorJuego, String categoria, int minJugadores, int maxJugadores, boolean esDificil, boolean jueganMenores5, boolean jueganMenores18,  String idJuegoPrestamo, boolean disponible, int vecesPrestado, String estado) {
        super(nombre, anioPublicacion, editorJuego, categoria, minJugadores, maxJugadores,
              esDificil, jueganMenores5, jueganMenores18, idJuegoPrestamo);
        this.idJuegoPrestamo = idJuegoPrestamo;
        this.disponible = disponible;
        this.vecesPrestado = vecesPrestado;
        this.estado = estado;
    }

    public String getIdJuegoPrestamo() {
        return idJuegoPrestamo;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public int getVecesPrestado() {
        return vecesPrestado;
    }

    public String getEstado() {
        return estado;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public void aumentarVecesPrestado() {
        this.vecesPrestado++;
    }

	public void setEstado(String estado) {
		this.estado = estado;
	}
    
	@Override
	public String toString() {
	    return "id\t" + this.idJuegoPrestamo + "|nombre\t" + this.nombre + "|anio\t" + this.anioPublicacion + "|editor\t" + this.editorJuego + "|categoria\t" + this.categoria + "|minJugadores\t" + this.minJugadores + "|maxJugadores\t" + this.maxJugadores + "|dificil\t" + this.esDificil + "|menores5\t" + this.jueganMenores5 + "|menores18\t" + this.jueganMenores18 + "|disponible\t" + this.disponible + "|vecesPrestado\t" + this.vecesPrestado + "|estado\t" + this.estado;
	}
}	
