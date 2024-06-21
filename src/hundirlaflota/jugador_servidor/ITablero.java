package hundirlaflota.jugador_servidor;

import java.io.Serializable;
import java.util.List;

import hundirlaflota.IBarco;
import hundirlaflota.IDisparo;

/**
 * @author Jon Oreilly del Cerro joreilly1@alumno.uned.es
 */

public interface ITablero extends Serializable {

	public String getContrincante();

	public List<IDisparoYResultado> getDisparosUsuario();

	public List<IDisparo> getDisparosContrincante();

	public List<IBarco> getBarcosUsuario();

	// Solo cuando la partida ha terminado, null si está en curso
	public List<IBarco> getBarcosContrincante();

	public EEstadoTablero getEstadoTablero();

	public boolean getEsTurnoUsuario();

}
