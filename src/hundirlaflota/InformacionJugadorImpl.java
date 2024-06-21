package hundirlaflota;

/**
 * @author Jon Oreilly del Cerro joreilly1@alumno.uned.es
 */

public class InformacionJugadorImpl implements InformacionJugadorInterface {

	private static final long serialVersionUID = 3801300910628579095L;

	private String jugador;

	private Integer puntuacion;

	private Integer partidasJugadas;
	
	public InformacionJugadorImpl(String jugador, Integer puntuacion, Integer partidasJugadas) {
		this.jugador = jugador;
		this.puntuacion = puntuacion;
		this.partidasJugadas = partidasJugadas;
	}
	
	public InformacionJugadorImpl(InformacionJugadorInterface informacionJugador) {
		this.jugador = informacionJugador.getJugador();
		this.puntuacion = informacionJugador.getPuntuacion();
		this.partidasJugadas = informacionJugador.getPartidasJugadas();
	}
	
	public String getJugador() {
		return this.jugador;
	}

	public Integer getPuntuacion() {
		return this.puntuacion;
	}

	public Integer getPartidasJugadas() {
		return this.partidasJugadas;
	}
	
	public String toString() {
		return this.jugador + ":" + this.puntuacion + "/" + this.partidasJugadas;
	}
}
