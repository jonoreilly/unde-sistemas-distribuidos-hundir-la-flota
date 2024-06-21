package hundirlaflota.basededatos;

import java.util.Map;
import java.util.HashMap;

/**
 * @author Jon Oreilly del Cerro joreilly1@alumno.uned.es
 */

public class TablaContrasenas {
	
	/** key: <String> jugador; value: <String> contraseña */
	private Map<String, String> mapaContrasenas = new HashMap<>();
	
	public boolean registrarJugador(String jugador, String contrasena) {
		String contrasenaDB = this.mapaContrasenas.get(jugador);
		
		// El usuario ya se ha registrado
		if (contrasenaDB != null) {
			return false;
		}
		
		this.mapaContrasenas.put(jugador, contrasena);
		
		return true;
	}
	
	public boolean iniciarSesion(String jugador, String contrasena) {
		String contrasenaDB = this.mapaContrasenas.get(jugador);

		// El usuario no está registrado
		if (contrasenaDB == null) {
			return false;
		}
		
		return contrasenaDB.contentEquals(contrasena);
	}
	
}
