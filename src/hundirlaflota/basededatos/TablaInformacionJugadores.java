package hundirlaflota.basededatos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hundirlaflota.InformacionJugadorInterface;

/**
 * @author Jon Oreilly del Cerro joreilly1@alumno.uned.es
 */

public class TablaInformacionJugadores {

	/** key: <String> jugador; value: <InformacionJugadorInterface> puntuación */
	private Map<String, InformacionJugadorInterface> mapaInformacionJugadores = new HashMap<>();
	
	public InformacionJugadorInterface getInformacionJugador(String jugador) {
		return this.mapaInformacionJugadores.get(jugador);
	}
	
	public void setInformacionJugador(InformacionJugadorInterface informacionJugador) {
		this.mapaInformacionJugadores.put(informacionJugador.getJugador(), informacionJugador);
	}
	
	public List<InformacionJugadorInterface> getInformacionJugadores() {
		return new ArrayList<InformacionJugadorInterface>(this.mapaInformacionJugadores.values());
	}
	
}
