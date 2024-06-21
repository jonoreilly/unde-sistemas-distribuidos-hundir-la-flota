package hundirlaflota.basededatos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hundirlaflota.servidor_basededatos.IPartida;

/**
 * @author Jon Oreilly del Cerro joreilly1@alumno.uned.es
 */

public class TablaPartidas {

	/** key: <Integer> id; value: <IPartida> partida */
	private Map<Integer, IPartida> mapaPartidas = new HashMap<>();

	public IPartida getPartida(int id) {
		return this.mapaPartidas.get(id);
	}
	
	public void setPartida(IPartida partida) {
		this.mapaPartidas.put(partida.getId(), partida);
	}
	
	public List<IPartida> getPartidas() {
		return new ArrayList<IPartida>(this.mapaPartidas.values());
	}
	
}
