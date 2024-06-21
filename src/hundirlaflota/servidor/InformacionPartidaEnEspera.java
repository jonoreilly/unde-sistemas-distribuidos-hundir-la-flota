package hundirlaflota.servidor;

import hundirlaflota.InformacionJugadorInterface;
import hundirlaflota.jugador_servidor.IInformacionPartidaEnEspera;

/**
 * @author Jon Oreilly del Cerro joreilly1@alumno.uned.es
 */

public class InformacionPartidaEnEspera implements IInformacionPartidaEnEspera {

	private static final long serialVersionUID = -5405441057605169818L;
	
	private int idPartida;
	
	private InformacionJugadorInterface informacionJugador;

	public InformacionPartidaEnEspera(int idPartida, InformacionJugadorInterface informacionJugador) {
		this.idPartida = idPartida;
		this.informacionJugador = informacionJugador;
	}
	
	public int getIdPartida() {
		return this.idPartida;
	}

	public InformacionJugadorInterface getInformacionContrincante() {
		return this.informacionJugador;
	}
	
}
