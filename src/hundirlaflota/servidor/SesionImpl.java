package hundirlaflota.servidor;

import java.util.Random;

import hundirlaflota.jugador_servidor.SesionInterface;

/**
 * @author Jon Oreilly del Cerro joreilly1@alumno.uned.es
 */

public class SesionImpl implements SesionInterface {

	private static final long serialVersionUID = -4541018070598502067L;

	private String jugador;
	
	private int idSesion;
	
	public SesionImpl(String jugador) {
		
		this.jugador = jugador;
		
		this.idSesion = new Random().nextInt();
		
	}
	
	public SesionImpl(SesionInterface sesion) {
		
		this.jugador = sesion.getJugador();
		
		this.idSesion = sesion.getIdSesion();
		
	}
	
	public String getJugador() {
		return this.jugador;
	}

	public int getIdSesion() {
		return this.idSesion;
	}

}
