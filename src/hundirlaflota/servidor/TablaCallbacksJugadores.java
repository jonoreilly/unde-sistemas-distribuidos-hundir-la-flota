package hundirlaflota.servidor;

import java.util.HashMap;
import java.util.Map;

import hundirlaflota.jugador_servidor.CallbackJugadorInterface;

/**
 * @author Jon Oreilly del Cerro joreilly1@alumno.uned.es
 */

public class TablaCallbacksJugadores {

	/** key: <String> jugador; value: <CallbackJugadorInterface> callbackJugador */
	private Map<String, CallbackJugadorInterface> mapaCallbacksJugadores = new HashMap<>();
	
	public CallbackJugadorInterface getCallbackJugador(String jugador) {
		return this.mapaCallbacksJugadores.get(jugador);
	}
	
	public void setCallbackJugador(String jugador, CallbackJugadorInterface callbackJugador) {
		this.mapaCallbacksJugadores.put(jugador, callbackJugador);
	}
	
	public void removeCallbackJugador(String jugador) {
		this.mapaCallbacksJugadores.remove(jugador);
	}
	
}
