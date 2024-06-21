package hundirlaflota.servidor;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import hundirlaflota.IBarco;
import hundirlaflota.IDisparo;
import hundirlaflota.InformacionJugadorImpl;
import hundirlaflota.InformacionJugadorInterface;
import hundirlaflota.Listas;
import hundirlaflota.NormasDeJuego;
import hundirlaflota.jugador_servidor.CallbackJugadorInterface;
import hundirlaflota.jugador_servidor.CallbackJugadorMensajeEnum;
import hundirlaflota.jugador_servidor.IInformacionPartidaEnEspera;
import hundirlaflota.jugador_servidor.ITablero;
import hundirlaflota.jugador_servidor.ServicioGestorInterface;
import hundirlaflota.jugador_servidor.SesionInterface;
import hundirlaflota.servidor_basededatos.EEstadoPartida;
import hundirlaflota.servidor_basededatos.IPartida;
import hundirlaflota.servidor_basededatos.ServicioDatosInterface;

/**
 * @author Jon Oreilly del Cerro joreilly1@alumno.uned.es
 */

public class ServicioGestorImpl extends UnicastRemoteObject implements ServicioGestorInterface {

	private static final long serialVersionUID = 1451688413830704759L;

	private ServicioDatosInterface servicioDatos;

	private TablaSesiones tablaSesiones;

	private TablaCallbacksJugadores tablaCallbacksJugadores;

	protected ServicioGestorImpl(ServicioDatosInterface servicioDatos, TablaSesiones tablaSesiones,
			TablaCallbacksJugadores tablaCallbacksJugadores) throws RemoteException {

		super();

		this.servicioDatos = servicioDatos;

		this.tablaSesiones = tablaSesiones;

		this.tablaCallbacksJugadores = tablaCallbacksJugadores;

	}

	public InformacionJugadorInterface getInformacionJugador(SesionInterface sesion) throws RemoteException {

		if (!this.esSesionValida(sesion)) {
			return null;
		}

		return this.getInformacionJugador(sesion.getJugador());

	}

	public InformacionJugadorInterface getInformacionJugador(SesionInterface sesion, String jugador)
			throws RemoteException {

		if (!this.esSesionValida(sesion)) {
			return null;
		}

		return this.getInformacionJugador(jugador);

	}

	public Integer crearPartida(SesionInterface sesion) throws RemoteException {

		if (!this.esSesionValida(sesion)) {
			return null;
		}

		int idPartida = new Random().nextInt();

		IPartida partida = new Partida(idPartida, sesion.getJugador());

		this.servicioDatos.setPartida(partida);

		return idPartida;

	}

	public List<IInformacionPartidaEnEspera> getJugadoresEnEspera(SesionInterface sesion) throws RemoteException {

		if (!this.esSesionValida(sesion)) {
			return null;
		}

		// Obtener jugadores en espera

		List<IPartida> partidas = this.servicioDatos.getPartidas();

		List<InformacionJugadorInterface> informacionJugadores = this.servicioDatos.getInformacionJugadores();

		List<IPartida> partidasEnEspera = Listas.filtrarLista(partidas,
				(p) -> p.getEstado() == EEstadoPartida.ESPERANDO_CONTRINCANTE);

		List<IInformacionPartidaEnEspera> informacionPartidasEnEspera = Listas.transformarLista(partidasEnEspera,
				(p) -> {
					int idPartida = p.getId();

					InformacionJugadorInterface informacionJugador = Listas.buscar(informacionJugadores,
							(j) -> j.getJugador().contentEquals(p.getJugador1()));

					if (informacionJugador == null) {
						informacionJugador = new InformacionJugadorImpl(p.getJugador1(), 0, 0);
					}

					return new InformacionPartidaEnEspera(idPartida, informacionJugador);
				});

		return informacionPartidasEnEspera;

	}

	public boolean unirseAPartida(SesionInterface sesion, int idPartida) throws RemoteException {

		if (!this.esSesionValida(sesion)) {
			return false;
		}

		IPartida partida = this.servicioDatos.getPartida(idPartida);

		// Comprobar que existe la partida

		if (partida == null) {
			return false;
		}

		// Comprobar que la partida está en espera de contrincante

		if (partida.getEstado() != EEstadoPartida.ESPERANDO_CONTRINCANTE) {
			return false;
		}

		// Comprobar que no es el mismo jugador contra si mismo

		String contrincante = partida.getJugador1();

		if (contrincante.contentEquals(sesion.getJugador())) {
			return false;
		}

		// Actualizar la partida

		IPartida nuevaPartida = new Partida(idPartida, contrincante, sesion.getJugador());

		this.servicioDatos.setPartida(nuevaPartida);

		// Notificar al contrincante

		CallbackJugadorInterface callbackContrincante = this.tablaCallbacksJugadores.getCallbackJugador(contrincante);

		if (callbackContrincante == null) {
			return false;
		}

		callbackContrincante.enviarMensaje(CallbackJugadorMensajeEnum.CONTRINCANTE_UNIDO_COLOCAR_BARCOS);

		return true;
	}

	public ITablero getTablero(SesionInterface sesion, int idPartida) throws RemoteException {

		if (!this.esSesionValida(sesion)) {
			return null;
		}

		IPartida partida = this.servicioDatos.getPartida(idPartida);

		// Comprobar que existe la partida

		if (partida == null) {
			return null;
		}

		// Comprobar que es su partida

		if (!Utils.getEsSuPartida(partida, sesion.getJugador())) {
			return null;
		}

		ITablero tablero = Utils.crearTablero(partida, sesion.getJugador());

		return tablero;

	}

	public boolean setBarcos(SesionInterface sesion, int idPartida, IBarco barco1, IBarco barco2)
			throws RemoteException {

		if (!this.esSesionValida(sesion)) {
			return false;
		}

		IPartida partida = this.servicioDatos.getPartida(idPartida);

		// Comprobar que existe la partida

		if (partida == null) {
			return false;
		}

		// Comprobar que está en fase de puesta de barcos

		if (partida.getEstado() != EEstadoPartida.COLOCANDO_BARCOS) {
			return false;
		}

		// Comprobar que es su partida

		if (!Utils.getEsSuPartida(partida, sesion.getJugador())) {
			return false;
		}

		// Comprobar que aun no ha añadido barcos

		boolean esJugador1 = Utils.getEsJugador1(partida, sesion.getJugador());

		List<IBarco> barcos = esJugador1 ? partida.getBarcosJugador1() : partida.getBarcosJugador2();

		if (barcos != null && barcos.size() != 0) {
			return false;
		}

		// Añadir barcos

		List<IBarco> nuevosBarcos = Arrays.asList(barco1, barco2);

		partida = Utils.setBarcos(partida, sesion.getJugador(), nuevosBarcos);

		this.servicioDatos.setPartida(partida);

		// Sincronizar con el contrincante

		if (partida.getEstado() == EEstadoPartida.EN_CURSO) {

			// Notificar ambos jugadores

			CallbackJugadorInterface callbackJugador1 = this.tablaCallbacksJugadores
					.getCallbackJugador(partida.getJugador1());

			CallbackJugadorInterface callbackJugador2 = this.tablaCallbacksJugadores
					.getCallbackJugador(partida.getJugador2());

			if (callbackJugador1 == null || callbackJugador2 == null) {
				return false;
			}

			callbackJugador1.enviarMensaje(CallbackJugadorMensajeEnum.COMIENZA_EL_JUEGO_TU_TURNO);

			callbackJugador2.enviarMensaje(CallbackJugadorMensajeEnum.COMIENZA_EL_JUEGO_TURNO_CONTRINCANTE);

		}

		return true;

	}

	public boolean realizarDisparo(SesionInterface sesion, int idPartida, IDisparo disparo) throws RemoteException {

		if (!this.esSesionValida(sesion)) {
			return false;
		}

		IPartida partida = this.servicioDatos.getPartida(idPartida);

		// Comprobar que está en partida

		if (partida == null) {
			return false;
		}

		// Comprobar que la partida está en curso

		if (partida.getEstado() != EEstadoPartida.EN_CURSO) {
			return false;
		}

		// Comprobar que es su partida

		if (!Utils.getEsSuPartida(partida, sesion.getJugador())) {
			return false;
		}

		// Comprobar que es su turno

		if (!Utils.getEsTurnoJugador(partida, sesion.getJugador())) {
			return false;
		}

		// Comprobar que no repite el disparo en el mismo sitio

		boolean esJugador1 = Utils.getEsJugador1(partida, sesion.getJugador());

		List<IDisparo> disparos = esJugador1 ? partida.getDisparosJugador1() : partida.getDisparosJugador2();

		if (NormasDeJuego.coincideAlMenosUno(disparo, disparos)) {
			return false;
		}

		// Añadir disparo

		IPartida nuevaPartida = Utils.anadirDisparo(partida, sesion.getJugador(), disparo);

		this.servicioDatos.setPartida(nuevaPartida);

		// Calcular nuevas puntuaciones

		if (nuevaPartida.getEstado() != EEstadoPartida.EN_CURSO) {

			InformacionJugadorInterface informacionJugador1 = this.getInformacionJugador(nuevaPartida.getJugador1());

			InformacionJugadorInterface informacionJugador2 = this.getInformacionJugador(nuevaPartida.getJugador2());

			InformacionJugadorInterface nuevaInformacionJugador1 = Utils.calcularNuevaInformacionJugador(nuevaPartida,
					informacionJugador1);

			InformacionJugadorInterface nuevaInformacionJugador2 = Utils.calcularNuevaInformacionJugador(nuevaPartida,
					informacionJugador2);

			this.servicioDatos.setInformacionJugador(nuevaInformacionJugador1);

			this.servicioDatos.setInformacionJugador(nuevaInformacionJugador2);

		}

		// Notificar el resultado a ambos jugadores

		CallbackJugadorInterface callbackJugador1 = this.tablaCallbacksJugadores
				.getCallbackJugador(nuevaPartida.getJugador1());

		CallbackJugadorInterface callbackJugador2 = this.tablaCallbacksJugadores
				.getCallbackJugador(nuevaPartida.getJugador2());

		if (callbackJugador1 == null || callbackJugador2 == null) {
			return false;
		}

		// Notificar victoria del jugador 1

		if (nuevaPartida.getEstado() == EEstadoPartida.VICTORIA_JUGADOR_1) {

			callbackJugador1.enviarMensaje(CallbackJugadorMensajeEnum.VICTORIA);

			callbackJugador2.enviarMensaje(CallbackJugadorMensajeEnum.DERROTA);

			return true;

		}

		// Notificar victoria del jugador 2

		if (nuevaPartida.getEstado() == EEstadoPartida.VICTORIA_JUGADOR_2) {

			callbackJugador1.enviarMensaje(CallbackJugadorMensajeEnum.DERROTA);

			callbackJugador2.enviarMensaje(CallbackJugadorMensajeEnum.VICTORIA);

			return true;

		}

		// Notificar tocado

		List<IBarco> barcosContrincante = esJugador1 ? nuevaPartida.getBarcosJugador2() : nuevaPartida.getBarcosJugador1();

		if (NormasDeJuego.tocaBarco(disparo, barcosContrincante)) {

			if (esJugador1) {

				callbackJugador1.enviarMensaje(CallbackJugadorMensajeEnum.DISPARO_TUYO_TOCADO);

				callbackJugador2.enviarMensaje(CallbackJugadorMensajeEnum.DISPARO_CONTRINCANTE_TOCADO);

			} else {

				callbackJugador1.enviarMensaje(CallbackJugadorMensajeEnum.DISPARO_CONTRINCANTE_TOCADO);

				callbackJugador2.enviarMensaje(CallbackJugadorMensajeEnum.DISPARO_TUYO_TOCADO);

			}

			return true;

		}

		// Notificar agua

		if (esJugador1) {

			callbackJugador1.enviarMensaje(CallbackJugadorMensajeEnum.DISPARO_TUYO_AGUA);

			callbackJugador2.enviarMensaje(CallbackJugadorMensajeEnum.DISPARO_CONTRINCANTE_AGUA);

		} else {

			callbackJugador1.enviarMensaje(CallbackJugadorMensajeEnum.DISPARO_CONTRINCANTE_AGUA);

			callbackJugador2.enviarMensaje(CallbackJugadorMensajeEnum.DISPARO_TUYO_AGUA);

		}

		return true;

	}

	public boolean abandonarPartida(SesionInterface sesion, int idPartida) throws RemoteException {

		if (!this.esSesionValida(sesion)) {
			return false;
		}

		IPartida partida = this.servicioDatos.getPartida(idPartida);

		// Comprobar que está en partida

		if (partida == null) {
			return false;
		}

		// Comprobar que la partida está en curso

		if (partida.getEstado() != EEstadoPartida.EN_CURSO) {
			return false;
		}

		// Comprobar que es su partida

		if (!Utils.getEsSuPartida(partida, sesion.getJugador())) {
			return false;
		}

		// Comprobar que es su turno

		if (!Utils.getEsTurnoJugador(partida, sesion.getJugador())) {
			return false;
		}

		// Abandonar la partida

		IPartida nuevaPartida = Utils.abandonarPartida(partida, sesion.getJugador());

		this.servicioDatos.setPartida(nuevaPartida);

		// Calcular nuevas puntuaciones

		InformacionJugadorInterface informacionJugador1 = this.getInformacionJugador(nuevaPartida.getJugador1());

		InformacionJugadorInterface informacionJugador2 = this.getInformacionJugador(nuevaPartida.getJugador2());

		InformacionJugadorInterface nuevaInformacionJugador1 = Utils.calcularNuevaInformacionJugador(nuevaPartida,
				informacionJugador1);

		InformacionJugadorInterface nuevaInformacionJugador2 = Utils.calcularNuevaInformacionJugador(nuevaPartida,
				informacionJugador2);

		this.servicioDatos.setInformacionJugador(nuevaInformacionJugador1);

		this.servicioDatos.setInformacionJugador(nuevaInformacionJugador2);

		// Notificar el resultado al contrincante

		boolean esJugador1 = Utils.getEsJugador1(nuevaPartida, sesion.getJugador());

		String contrincante = esJugador1 ? nuevaPartida.getJugador2() : nuevaPartida.getJugador1();

		CallbackJugadorInterface callbackContrincante = this.tablaCallbacksJugadores.getCallbackJugador(contrincante);

		if (callbackContrincante == null) {
			return false;
		}

		callbackContrincante.enviarMensaje(CallbackJugadorMensajeEnum.CONTRINCANTE_CAPITULA);

		return true;

	}

	private boolean esSesionValida(SesionInterface sesion) {

		SesionInterface sesionServidor = this.tablaSesiones.getSesion(sesion.getJugador());

		return Utils.esMismaSesion(sesion, sesionServidor);

	}

	private InformacionJugadorInterface getInformacionJugador(String jugador) throws RemoteException {

		InformacionJugadorInterface informacionJugador = this.servicioDatos.getInformacionJugador(jugador);

		if (informacionJugador == null) {
			return new InformacionJugadorImpl(jugador, 0, 0);
		}

		return informacionJugador;

	}

}
