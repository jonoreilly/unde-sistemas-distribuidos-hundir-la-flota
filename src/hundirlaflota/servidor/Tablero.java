package hundirlaflota.servidor;

import java.util.List;

import hundirlaflota.Barco;
import hundirlaflota.Disparo;
import hundirlaflota.IBarco;
import hundirlaflota.IDisparo;
import hundirlaflota.Listas;
import hundirlaflota.jugador_servidor.EEstadoTablero;
import hundirlaflota.jugador_servidor.IDisparoYResultado;
import hundirlaflota.jugador_servidor.ITablero;

/**
 * @author Jon Oreilly del Cerro joreilly1@alumno.uned.es
 */

public class Tablero implements ITablero {

	private static final long serialVersionUID = 3582475029814035091L;

	private String contrincante;

	private List<IDisparoYResultado> disparosUsuario;

	private List<IDisparo> disparosContrincante;

	private List<IBarco> barcosUsuario;

	private List<IBarco> barcosContrincante;

	private EEstadoTablero estadoTablero;

	private boolean esTurnoUsuario;

	public Tablero(String contrincante, List<IDisparoYResultado> disparosUsuario,
			List<IDisparo> disparosContrincante, List<IBarco> barcosUsuario, List<IBarco> barcosContrincante,
			EEstadoTablero estadoTablero, boolean esTurnoUsuario) {

		this.contrincante = contrincante;

		this.disparosUsuario = Listas.clonarLista(disparosUsuario, (d) -> new DisparoYResultado(d));

		this.disparosContrincante = Listas.clonarLista(disparosContrincante, (d) -> new Disparo(d));

		this.barcosUsuario = Listas.clonarLista(barcosUsuario, (b) -> new Barco(b));

		if (estadoTablero != EEstadoTablero.EN_CURSO) {
			this.barcosContrincante = Listas.clonarLista(barcosContrincante, (b) -> new Barco(b));
		}

		this.estadoTablero = estadoTablero;

		this.esTurnoUsuario = esTurnoUsuario;
	}
	
	public String getContrincante() {
		return this.contrincante;
	}

	public List<IDisparoYResultado> getDisparosUsuario() {
		return Listas.clonarLista(this.disparosUsuario, (d) -> new DisparoYResultado(d));
	}

	public List<IDisparo> getDisparosContrincante() {
		return Listas.clonarLista(this.disparosContrincante, (d) -> new Disparo(d));
	}

	public List<IBarco> getBarcosUsuario() {
		return Listas.clonarLista(this.barcosUsuario, (b) -> new Barco(b));
	}

	public List<IBarco> getBarcosContrincante() {
		if (estadoTablero != EEstadoTablero.EN_CURSO) {
			return Listas.clonarLista(this.barcosContrincante, (b) -> new Barco(b));
		}

		return null;
	}

	public EEstadoTablero getEstadoTablero() {
		return this.estadoTablero;
	}

	public boolean getEsTurnoUsuario() {
		return this.esTurnoUsuario;
	}

}
