package usuarios;

import java.util.ArrayList;
import java.util.List;
import juegos.*;

public abstract class Usuario {
	protected String login;
	protected String contrasena;
	protected String nombre;
	private List<JuegoMesa> favoritos;
	

	public Usuario(String login, String contrasena) {
		super();
		this.login = login;
		this.contrasena = contrasena;
		this.favoritos = new ArrayList<>();
	}
	
	public String getLogin() {
        return login;
    }

    public String getContrasena() {
        return contrasena;
    }

	// Metodos para RF11 //

	public void agregarJuegoFavorito(JuegoMesa juego) throws Exception{
		if (juego == null) {
			throw new Exception(" No puede ser nulo un juego ");
		}
		if (favoritos.contains(juego)){
			throw new Exception( " Este juego ya esta dentro de sus favoritos");
		}
		favoritos.add(juego);
	}
	
	public void eliminarJuegoFavorito(JuegoMesa juego) throws Exception{
		if(!favoritos.contains(juego)) {
			throw new Exception(" Este juego no hace parte de favoritos ");
		}

		favoritos.remove(juego);
	}
	
	public List<JuegoMesa> getJuegosFavoritos(){
		return new ArrayList<>(favoritos);
	}
	
	public void cambiarPreferencias(List<JuegoMesa> nuevosFavoritos) throws Exception{
		if (nuevosFavoritos == null) {
			throw new Exception (" No puedes pasar una informacion nula ");
		
		}
		this.favoritos.clear();

		for(JuegoMesa juego : nuevosFavoritos){
			if (juego == null){
				throw new Exception ("No se permiten juegos con valor nulo ");

			}
			if (!favoritos.contains(juego)){
				favoritos.add(juego);
			}
		}
	}

	public boolean esFavorito(JuegoMesa juego){
		return favoritos.contains(juego);
	}
	
	@Override
	public String toString() {
	    return "login\t" + this.login + "|contrasena\t" + this.contrasena;
	}
}

