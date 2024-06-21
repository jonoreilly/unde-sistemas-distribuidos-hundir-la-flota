package hundirlaflota.jugador_servidor;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author Jon Oreilly del Cerro joreilly1@alumno.uned.es
 */

public interface ServicioAutenticacionInterface extends Remote {

	public boolean registrarJugador(String jugador, String contrasena) throws RemoteException;
	
	public SesionInterface iniciarSesion(String jugador, String contrasena, CallbackJugadorInterface callbackJugador) throws RemoteException;
	
	public boolean cerrarSesion(SesionInterface sesion) throws RemoteException;
	
}
