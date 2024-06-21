package hundirlaflota.servidor_basededatos;

import java.io.Serializable;
import java.util.List;

import hundirlaflota.IBarco;
import hundirlaflota.IDisparo;

/**
 * @author Jon Oreilly del Cerro joreilly1@alumno.uned.es
 */

public interface IPartida extends Serializable {

	public int getId();
	
	public String getJugador1();
	
	public String getJugador2();
	
	public List<IBarco> getBarcosJugador1();
	
	public List<IBarco> getBarcosJugador2();
	
	public List<IDisparo> getDisparosJugador1();
	
	public List<IDisparo> getDisparosJugador2();

	public boolean getHaCapituladoJugador1();
	
	public boolean getHaCapituladoJugador2();
	
	public EEstadoPartida getEstado();
	
}
