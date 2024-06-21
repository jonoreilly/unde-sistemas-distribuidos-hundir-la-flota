package hundirlaflota.jugador_servidor;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author Jon Oreilly del Cerro joreilly1@alumno.uned.es
 */

public interface CallbackJugadorInterface extends Remote {
	public void enviarMensaje(CallbackJugadorMensajeEnum mensaje) throws RemoteException;
}
