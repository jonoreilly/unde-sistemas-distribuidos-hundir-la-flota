package hundirlaflota.servidor_basededatos;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import hundirlaflota.InformacionJugadorInterface;

/**
 * @author Jon Oreilly del Cerro joreilly1@alumno.uned.es
 */

public interface ServicioDatosInterface extends Remote {

	public boolean registrarJugador(String jugador, String contrasena) throws RemoteException;

	public boolean iniciarSesion(String jugador, String contrasena) throws RemoteException;
	
	public InformacionJugadorInterface getInformacionJugador(String jugador) throws RemoteException;
	
	public void setInformacionJugador(InformacionJugadorInterface informacionJugador) throws RemoteException;

	public List<InformacionJugadorInterface> getInformacionJugadores() throws RemoteException;
	
	public IPartida getPartida(int id) throws RemoteException;
	
	public void setPartida(IPartida partida) throws RemoteException;
	
	public List<IPartida> getPartidas() throws RemoteException;
	
}
