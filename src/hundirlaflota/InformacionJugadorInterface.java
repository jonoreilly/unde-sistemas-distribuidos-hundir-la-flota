package hundirlaflota;

import java.io.Serializable;

/**
 * @author Jon Oreilly del Cerro joreilly1@alumno.uned.es
 */

public interface InformacionJugadorInterface extends Serializable {

	public String getJugador();

	public Integer getPuntuacion();

	public Integer getPartidasJugadas();
}
