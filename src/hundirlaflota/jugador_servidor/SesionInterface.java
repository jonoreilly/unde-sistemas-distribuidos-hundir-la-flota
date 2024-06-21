package hundirlaflota.jugador_servidor;

import java.io.Serializable;

/**
 * @author Jon Oreilly del Cerro joreilly1@alumno.uned.es
 */

public interface SesionInterface extends Serializable {

	public String getJugador();
	
	public int getIdSesion();
	
}
