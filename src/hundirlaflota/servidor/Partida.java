package hundirlaflota.servidor;

import java.util.List;

import hundirlaflota.Barco;
import hundirlaflota.Disparo;
import hundirlaflota.IBarco;
import hundirlaflota.IDisparo;
import hundirlaflota.Listas;
import hundirlaflota.servidor_basededatos.EEstadoPartida;
import hundirlaflota.servidor_basededatos.IPartida;

/**
 * @author Jon Oreilly del Cerro joreilly1@alumno.uned.es
 */

public class Partida implements IPartida {

	private static final long serialVersionUID = 4294616012630808218L;

	private int id;

	private String jugador1;

	private String jugador2;

	private List<IBarco> barcosJugador1;

	private List<IBarco> barcosJugador2;

	private List<IDisparo> disparosJugador1;

	private List<IDisparo> disparosJugador2;

	private boolean haCapituladoJugador1 = false;

	private boolean haCapituladoJugador2 = false;

	private EEstadoPartida estado;

	public Partida(int id, String jugador1) {
		this.id = id;
		this.jugador1 = jugador1;

		this.estado = Utils.calcularEstado(this);
	}

	public Partida(int id, String jugador1, String jugador2) {
		this.id = id;
		this.jugador1 = jugador1;
		this.jugador2 = jugador2;

		this.estado = Utils.calcularEstado(this);
	}

	public Partida(int id, String jugador1, String jugador2, List<IBarco> barcosJugador1, List<IBarco> barcosJugador2) {
		this.id = id;
		this.jugador1 = jugador1;
		this.jugador2 = jugador2;
		this.barcosJugador1 = barcosJugador1;
		this.barcosJugador2 = barcosJugador2;

		this.estado = Utils.calcularEstado(this);
	}

	public Partida(int id, String jugador1, String jugador2, List<IBarco> barcosJugador1, List<IBarco> barcosJugador2,
			List<IDisparo> disparosJugador1, List<IDisparo> disparosJugador2) {
		this.id = id;
		this.jugador1 = jugador1;
		this.jugador2 = jugador2;
		this.barcosJugador1 = barcosJugador1;
		this.barcosJugador2 = barcosJugador2;
		this.disparosJugador1 = disparosJugador1;
		this.disparosJugador2 = disparosJugador2;

		this.estado = Utils.calcularEstado(this);
	}
	
	public Partida(int id, String jugador1, String jugador2, List<IBarco> barcosJugador1, List<IBarco> barcosJugador2,
			List<IDisparo> disparosJugador1, List<IDisparo> disparosJugador2, boolean haCapituladoJugador1,
			boolean haCapituladoJugador2) {
		this.id = id;
		this.jugador1 = jugador1;
		this.jugador2 = jugador2;
		this.barcosJugador1 = barcosJugador1;
		this.barcosJugador2 = barcosJugador2;
		this.disparosJugador1 = disparosJugador1;
		this.disparosJugador2 = disparosJugador2;
		this.haCapituladoJugador1 = haCapituladoJugador1;
		this.haCapituladoJugador2 = haCapituladoJugador2;
		
		this.estado = Utils.calcularEstado(this);
	}

	public Partida(IPartida partida) {
		this.id = partida.getId();

		this.jugador1 = partida.getJugador1();
		this.jugador2 = partida.getJugador2();

		this.barcosJugador1 = Listas.clonarLista(partida.getBarcosJugador1(), (p) -> new Barco(p));
		this.barcosJugador2 = Listas.clonarLista(partida.getBarcosJugador2(), (p) -> new Barco(p));

		this.disparosJugador1 = Listas.clonarLista(partida.getDisparosJugador1(), (p) -> new Disparo(p));
		this.disparosJugador2 = Listas.clonarLista(partida.getDisparosJugador2(), (p) -> new Disparo(p));

		this.estado = Utils.calcularEstado(this);
	}

	public int getId() {
		return this.id;
	}

	public String getJugador1() {
		return this.jugador1;
	}

	public String getJugador2() {
		return this.jugador2;
	}

	public List<IBarco> getBarcosJugador1() {
		return Listas.clonarLista(this.barcosJugador1, (p) -> new Barco(p));
	}

	public List<IBarco> getBarcosJugador2() {
		return Listas.clonarLista(this.barcosJugador2, (p) -> new Barco(p));
	}

	public List<IDisparo> getDisparosJugador1() {
		return Listas.clonarLista(this.disparosJugador1, (p) -> new Disparo(p));
	}

	public List<IDisparo> getDisparosJugador2() {
		return Listas.clonarLista(this.disparosJugador2, (p) -> new Disparo(p));
	}
	
	public boolean getHaCapituladoJugador1() {
		return this.haCapituladoJugador1;
	}
	
	public boolean getHaCapituladoJugador2() {
		return this.haCapituladoJugador2;
	}

	public EEstadoPartida getEstado() {
		return this.estado;
	}

	public String toString() {
		return this.getJugador1() + "-" + this.getJugador2();
	}

}
