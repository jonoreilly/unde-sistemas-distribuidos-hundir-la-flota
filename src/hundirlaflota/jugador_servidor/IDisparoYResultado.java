package hundirlaflota.jugador_servidor;

import java.io.Serializable;

import hundirlaflota.IDisparo;

/**
 * @author Jon Oreilly del Cerro joreilly1@alumno.uned.es
 */

public interface IDisparoYResultado extends IDisparo, Serializable {

	public EResultadoDisparo getResultadoDisparo();

}
