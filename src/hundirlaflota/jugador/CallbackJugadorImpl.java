package hundirlaflota.jugador;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import hundirlaflota.jugador_servidor.CallbackJugadorInterface;
import hundirlaflota.jugador_servidor.CallbackJugadorMensajeEnum;

/**
 * @author Jon Oreilly del Cerro joreilly1@alumno.uned.es
 */

public class CallbackJugadorImpl extends UnicastRemoteObject implements CallbackJugadorInterface {

	private static final long serialVersionUID = -7196819445853245239L;
	
	private CallbackJugadorListaSincronizada listaSincronizada;
	
	public CallbackJugadorImpl(CallbackJugadorListaSincronizada listaSincronizada) throws RemoteException {
		super();
		this.listaSincronizada = listaSincronizada;
	}
	
	public void enviarMensaje(CallbackJugadorMensajeEnum mensaje) {
		this.listaSincronizada.enviarMensaje(mensaje);	
	}

}
