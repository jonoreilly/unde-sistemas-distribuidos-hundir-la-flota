package hundirlaflota.servidor;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import hundirlaflota.InformacionJugadorImpl;
import hundirlaflota.jugador_servidor.CallbackJugadorInterface;
import hundirlaflota.jugador_servidor.ServicioAutenticacionInterface;
import hundirlaflota.jugador_servidor.SesionInterface;
import hundirlaflota.servidor_basededatos.ServicioDatosInterface;

/**
 * @author Jon Oreilly del Cerro joreilly1@alumno.uned.es
 */

public class ServicioAutenticacionImpl extends UnicastRemoteObject implements ServicioAutenticacionInterface {

	private static final long serialVersionUID = -8511279009104045114L;

	private ServicioDatosInterface servicioDatos;

	private TablaSesiones tablaSesiones;

	private TablaCallbacksJugadores tablaCallbacksJugadores;

	protected ServicioAutenticacionImpl(ServicioDatosInterface servicioDatos, TablaSesiones tablaSesiones,
			TablaCallbacksJugadores tablaCallbacksJugadores) throws RemoteException {
		super();

		this.servicioDatos = servicioDatos;

		this.tablaSesiones = tablaSesiones;

		this.tablaCallbacksJugadores = tablaCallbacksJugadores;
	}

	public boolean registrarJugador(String jugador, String contrasena) throws RemoteException {
		
		// Registrar jugador
		
		boolean exito = this.servicioDatos.registrarJugador(jugador, contrasena);
		
		if (!exito) {
			return false;
		}
		
		// Inicializar la información historica del jugador
		
		this.servicioDatos.setInformacionJugador(new InformacionJugadorImpl(jugador, 0, 0));
		
		return true;
	}

	public SesionInterface iniciarSesion(String jugador, String contrasena, CallbackJugadorInterface callbackJugador)
			throws RemoteException {
		boolean exitoIniciandoSesion = this.servicioDatos.iniciarSesion(jugador, contrasena);

		if (!exitoIniciandoSesion) {
			return null;
		}

		// TODO: si ya esta conectado en otro sitio, cerrar su sesion y notificarle a el
		// y a su contrincante si tiene

		SesionInterface sesion = new SesionImpl(jugador);

		this.tablaSesiones.setSesion(jugador, sesion);

		this.tablaCallbacksJugadores.setCallbackJugador(jugador, callbackJugador);

		return sesion;
	}

	public boolean cerrarSesion(SesionInterface sesion) throws RemoteException {

		SesionInterface sesionServidor = this.tablaSesiones.getSesion(sesion.getJugador());

		if (!Utils.esMismaSesion(sesion, sesionServidor)) {
			return false;
		}

		this.tablaSesiones.removeSesion(sesion.getJugador());

		this.tablaCallbacksJugadores.removeCallbackJugador(sesion.getJugador());

		return true;

	}

}
