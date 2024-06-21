package hundirlaflota.servidor;

import java.util.HashMap;
import java.util.Map;

import hundirlaflota.jugador_servidor.SesionInterface;

/**
 * @author Jon Oreilly del Cerro joreilly1@alumno.uned.es
 */

public class TablaSesiones {

	/** key: <String> jugador; value: <SesionInterface> sesión */
	private Map<String, SesionInterface> mapaSesiones = new HashMap<>();
	
	public SesionInterface getSesion(String jugador) {
		SesionInterface sesion = this.mapaSesiones.get(jugador);
		
		if (sesion == null) {
			return null;
		}
		
		return new SesionImpl(sesion);
	}
	
	public void setSesion(String jugador, SesionInterface sesion) {
		this.mapaSesiones.put(jugador, new SesionImpl(sesion));
	}
	
	public void removeSesion(String jugador) {
		this.removeSesion(jugador);
	}
	
}
