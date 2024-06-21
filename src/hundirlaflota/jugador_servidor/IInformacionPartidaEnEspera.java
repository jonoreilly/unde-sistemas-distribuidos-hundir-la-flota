package hundirlaflota.jugador_servidor;

import java.io.Serializable;

import hundirlaflota.InformacionJugadorInterface;

/**
 * @author Jon Oreilly del Cerro joreilly1@alumno.uned.es
 */

public interface IInformacionPartidaEnEspera extends Serializable {
	
	public int getIdPartida();
	
	public InformacionJugadorInterface getInformacionContrincante();
	
}
