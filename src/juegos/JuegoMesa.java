package juegos;

public class JuegoMesa {
    protected String nombre;
    protected int anioPublicacion;
    protected String editorJuego;
    protected String categoria;
    protected int minJugadores;
    protected int maxJugadores;
    protected boolean esDificil;
    protected boolean jueganMenores5;
    protected boolean jueganMenores18;
    protected String idJuego;

    public JuegoMesa(String nombre, int anioPublicacion, String editorJuego, String categoria, int minJugadores, int maxJugadores, boolean esDificil, boolean jueganMenores5, boolean jueganMenores18, String idJuego) {
        this.nombre = nombre;
        this.anioPublicacion = anioPublicacion;
        this.editorJuego = editorJuego;
        this.categoria = categoria;
        this.minJugadores = minJugadores;
        this.maxJugadores = maxJugadores;
        this.esDificil = esDificil;
        this.jueganMenores5 = jueganMenores5;
        this.jueganMenores18 = jueganMenores18;
        this.idJuego = idJuego;
    }



    public String getNombre() {
		return nombre;
	}


	public String getCategoria() {
        return categoria;
    }

    public boolean isEsDificil() {
        return esDificil;
    }

    public int getMinJugadores() {
        return minJugadores;
    }

    public int getMaxJugadores() {
        return maxJugadores;
    }

    public String getIdJuego() {
        return idJuego;
    }

    
    

    public boolean isJueganMenores5() {
		return jueganMenores5;
	}



	public boolean isJueganMenores18() {
		return jueganMenores18;
	}



	public String getEditorJuego() {
		return editorJuego;
	}



	public int getAnioPublicacion() {
		return anioPublicacion;
	}



	@Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        JuegoMesa juego = (JuegoMesa) obj;
        return this.idJuego.equals(juego.idJuego);
    }

    @Override
    public int hashCode() {
        return this.idJuego.hashCode();
    }
}
