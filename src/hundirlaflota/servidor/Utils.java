package hundirlaflota.servidor;

import java.util.List;

import hundirlaflota.Barco;
import hundirlaflota.Disparo;
import hundirlaflota.IBarco;
import hundirlaflota.ICelda;
import hundirlaflota.IDisparo;
import hundirlaflota.InformacionJugadorImpl;
import hundirlaflota.InformacionJugadorInterface;
import hundirlaflota.Listas;
import hundirlaflota.NormasDeJuego;
import hundirlaflota.jugador_servidor.EEstadoTablero;
import hundirlaflota.jugador_servidor.EResultadoDisparo;
import hundirlaflota.jugador_servidor.IDisparoYResultado;
import hundirlaflota.jugador_servidor.ITablero;
import hundirlaflota.jugador_servidor.SesionInterface;
import hundirlaflota.servidor_basededatos.EEstadoPartida;
import hundirlaflota.servidor_basededatos.IPartida;

/**
 * @author Jon Oreilly del Cerro joreilly1@alumno.uned.es
 */

public final class Utils {

	public static boolean esMismaSesion(SesionInterface sesion1, SesionInterface sesion2) {

		return sesion1 != null && sesion2 != null && sesion1.getJugador().contentEquals(sesion2.getJugador())
				&& sesion1.getIdSesion() == sesion2.getIdSesion();

	}

	public static ITablero crearTablero(IPartida partida, String usuario) {

		boolean esJugador1 = partida.getJugador1().contentEquals(usuario);

		String contrincante = esJugador1 ? partida.getJugador2() : partida.getJugador1();

		List<IBarco> barcosUsuario = esJugador1 ? partida.getBarcosJugador1() : partida.getBarcosJugador2();
		List<IBarco> barcosContrincante = esJugador1 ? partida.getBarcosJugador2() : partida.getBarcosJugador1();

		List<IDisparo> disparosUsuario = esJugador1 ? partida.getDisparosJugador1() : partida.getDisparosJugador2();
		List<IDisparo> disparosContrincante = esJugador1 ? partida.getDisparosJugador2()
				: partida.getDisparosJugador1();

		List<IDisparoYResultado> disparosYResultadosUsuario = Utils.getDisparosYResultados(disparosUsuario,
				barcosContrincante);

		boolean elUsuarioHaUndidoTodosLosBarcosDelContrincante = NormasDeJuego.estanTodosHundidos(barcosContrincante,
				disparosUsuario);
		boolean elContrincanteHaUndidoTodosLosBarcosDelUsuario = NormasDeJuego.estanTodosHundidos(barcosUsuario,
				disparosContrincante);

		boolean haCapituladoUsuario = esJugador1 ? partida.getHaCapituladoJugador1()
				: partida.getHaCapituladoJugador2();
		boolean haCapituladoContrincante = esJugador1 ? partida.getHaCapituladoJugador2()
				: partida.getHaCapituladoJugador1();

		boolean esVictoria = elUsuarioHaUndidoTodosLosBarcosDelContrincante || haCapituladoContrincante;
		boolean esDerrota = elContrincanteHaUndidoTodosLosBarcosDelUsuario || haCapituladoUsuario;

		EEstadoTablero estadoTablero = esVictoria ? EEstadoTablero.VICTORIA
				: esDerrota ? EEstadoTablero.DERROTA : EEstadoTablero.EN_CURSO;

		boolean esTurnoJugador1 = partida.getDisparosJugador1().size() == partida.getDisparosJugador2().size();
		boolean esTurnoJugador2 = partida.getDisparosJugador1().size() == partida.getDisparosJugador2().size() + 1;

		boolean esTurnoUsuario = esJugador1 ? esTurnoJugador1 : esTurnoJugador2;

		List<IDisparoYResultado> clonDisparosYResultadosUsuario = Listas.clonarLista(disparosYResultadosUsuario,
				(d) -> new DisparoYResultado(d));
		List<IDisparo> clonDisparosContrincante = Listas.clonarLista(disparosContrincante, (d) -> new Disparo(d));
		List<IBarco> clonBarcosUsuario = Listas.clonarLista(barcosUsuario, (b) -> new Barco(b));
		List<IBarco> clonBarcosContrincante = Listas.clonarLista(barcosContrincante, (b) -> new Barco(b));

		List<IBarco> finalBarcosContrincante = estadoTablero != EEstadoTablero.EN_CURSO ? clonBarcosContrincante : null;

		return new Tablero(contrincante, clonDisparosYResultadosUsuario, clonDisparosContrincante, clonBarcosUsuario,
				finalBarcosContrincante, estadoTablero, esTurnoUsuario);

	}

	private static List<IDisparoYResultado> getDisparosYResultados(List<IDisparo> disparos, List<IBarco> barcos) {
		return Listas.transformarLista(disparos, (disparo) -> Utils.getDisparoYResultado(disparo, barcos));
	}

	private static IDisparoYResultado getDisparoYResultado(IDisparo disparo, List<IBarco> barcos) {
		EResultadoDisparo resultado = Utils.getResultado(disparo, barcos);

		return new DisparoYResultado(disparo, resultado);
	}

	private static EResultadoDisparo getResultado(IDisparo disparo, List<IBarco> barcos) {
		boolean tocaBarco = NormasDeJuego.tocaBarco(disparo, barcos);

		return tocaBarco ? EResultadoDisparo.TOCADO : EResultadoDisparo.AGUA;
	}

	public static boolean getEsSuPartida(IPartida partida, String jugador) {
		if (jugador.contentEquals(partida.getJugador1())) {
			return true;
		}

		if (jugador.contentEquals(partida.getJugador2())) {
			return true;
		}

		return false;
	}

	public static boolean getEsJugador1(IPartida partida, String jugador) {
		return partida.getJugador1().contentEquals(jugador);
	}

	public static IPartida setBarcos(IPartida partida, String jugador, List<IBarco> barcos) {
		boolean esJugador1 = Utils.getEsJugador1(partida, jugador);

		List<IBarco> barcosJugador1 = esJugador1 ? barcos : partida.getBarcosJugador1();
		List<IBarco> barcosJugador2 = esJugador1 ? partida.getBarcosJugador2() : barcos;

		return new Partida(partida.getId(), partida.getJugador1(), partida.getJugador2(), barcosJugador1,
				barcosJugador2);
	}

	private static boolean getEsTurnoJugador1(IPartida partida) {
		return partida.getDisparosJugador1().size() == partida.getDisparosJugador2().size();
	}

	private static boolean getEsTurnoJugador2(IPartida partida) {
		return partida.getDisparosJugador1().size() == partida.getDisparosJugador2().size() + 1;
	}

	public static boolean getEsTurnoJugador(IPartida partida, String jugador) {
		boolean esJugador1 = Utils.getEsJugador1(partida, jugador);

		return esJugador1 ? Utils.getEsTurnoJugador1(partida) : Utils.getEsTurnoJugador2(partida);
	}

	public static IPartida anadirDisparo(IPartida partida, String jugador, IDisparo disparo) {
		boolean esJugador1 = Utils.getEsJugador1(partida, jugador);

		List<IDisparo> disparosJugador1 = partida.getDisparosJugador1();
		List<IDisparo> disparosJugador2 = partida.getDisparosJugador2();

		if (esJugador1) {
			disparosJugador1.add(disparo);
		} else {
			disparosJugador2.add(disparo);
		}

		return new Partida(partida.getId(), partida.getJugador1(), partida.getJugador2(), partida.getBarcosJugador1(),
				partida.getBarcosJugador2(), disparosJugador1, disparosJugador2);
	}

	public static IPartida abandonarPartida(IPartida partida, String jugador) {
		boolean esJugador1 = Utils.getEsJugador1(partida, jugador);

		boolean haCapituladoJugador1 = esJugador1 ? true : partida.getHaCapituladoJugador1();
		boolean haCapituladoJugador2 = esJugador1 ? true : partida.getHaCapituladoJugador2();

		return new Partida(partida.getId(), partida.getJugador1(), partida.getJugador2(), partida.getBarcosJugador1(),
				partida.getBarcosJugador2(), partida.getDisparosJugador1(), partida.getDisparosJugador2(),
				haCapituladoJugador1, haCapituladoJugador2);
	}

	public static EEstadoPartida calcularEstado(IPartida partida) {

		if (partida.getJugador2() == null) {
			return EEstadoPartida.ESPERANDO_CONTRINCANTE;
		}

		if (partida.getHaCapituladoJugador1()) {
			return EEstadoPartida.CAPITULACION_JUGADOR_1;
		}

		if (partida.getHaCapituladoJugador2()) {
			return EEstadoPartida.CAPITULACION_JUGADOR_2;
		}

		if (partida.getBarcosJugador1() == null || partida.getBarcosJugador1().size() == 0
				|| partida.getBarcosJugador2() == null || partida.getBarcosJugador2().size() == 0) {
			return EEstadoPartida.COLOCANDO_BARCOS;
		}

		if (NormasDeJuego.estanTodosHundidos(partida.getBarcosJugador2(), partida.getDisparosJugador1())) {
			return EEstadoPartida.VICTORIA_JUGADOR_1;
		}

		if (NormasDeJuego.estanTodosHundidos(partida.getBarcosJugador1(), partida.getDisparosJugador2())) {
			return EEstadoPartida.VICTORIA_JUGADOR_2;
		}

		return EEstadoPartida.EN_CURSO;

	}

	public static InformacionJugadorInterface calcularNuevaInformacionJugador(IPartida partida,
			InformacionJugadorInterface informacionJugador) {

		boolean esJugador1 = Utils.getEsJugador1(partida, informacionJugador.getJugador());

		List<IDisparo> disparos = esJugador1 ? partida.getDisparosJugador1() : partida.getDisparosJugador2();

		List<IBarco> barcos = esJugador1 ? partida.getBarcosJugador2() : partida.getBarcosJugador1();

		boolean esVictoriaJugador1 = partida.getEstado() == EEstadoPartida.VICTORIA_JUGADOR_1
				|| partida.getEstado() == EEstadoPartida.CAPITULACION_JUGADOR_2;

		boolean esVictoriaJugador2 = partida.getEstado() == EEstadoPartida.VICTORIA_JUGADOR_2
				|| partida.getEstado() == EEstadoPartida.CAPITULACION_JUGADOR_1;

		boolean esVictoria = (esJugador1 && esVictoriaJugador1) || (!esJugador1 && esVictoriaJugador2);

		int puntuacion;

		if (esVictoria) {
			// Si gana (normal o capitulación) siempre se le dá la puntuación máxima obtenible
			puntuacion = NormasDeJuego.NUMERO_BARCOS * NormasDeJuego.LONGITUD_BARCO * NormasDeJuego.PUNTOS_POR_TOCADO;
			puntuacion += NormasDeJuego.PUNTOS_POR_VICTORIA;
		} else {
			puntuacion = 0;
			
			if (barcos != null) {
				for (IBarco barco : barcos) {
					for (ICelda celda : NormasDeJuego.getCeldasBarco(barco)) {
						if (NormasDeJuego.coincideAlMenosUno(celda, disparos)) {
							puntuacion += NormasDeJuego.PUNTOS_POR_TOCADO;
						}
					}
				}
			}
		}

		return new InformacionJugadorImpl(informacionJugador.getJugador(),
				informacionJugador.getPuntuacion() + puntuacion, informacionJugador.getPartidasJugadas() + 1);

	}

}
