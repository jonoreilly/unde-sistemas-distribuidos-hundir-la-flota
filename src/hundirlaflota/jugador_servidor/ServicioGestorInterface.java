package hundirlaflota.jugador_servidor;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import hundirlaflota.IBarco;
import hundirlaflota.IDisparo;
import hundirlaflota.InformacionJugadorInterface;

/**
 * @author Jon Oreilly del Cerro joreilly1@alumno.uned.es
 */

public interface ServicioGestorInterface extends Remote {

	// Info jugador

	public InformacionJugadorInterface getInformacionJugador(SesionInterface sesion) throws RemoteException;

	public InformacionJugadorInterface getInformacionJugador(SesionInterface sesion, String jugador) throws RemoteException;

	// Creación de partidas
	
	public Integer crearPartida(SesionInterface sesion) throws RemoteException;
	
	public List<IInformacionPartidaEnEspera> getJugadoresEnEspera(SesionInterface sesion) throws RemoteException;

	public boolean unirseAPartida(SesionInterface sesion, int idPartida) throws RemoteException;

	// Juego

	public ITablero getTablero(SesionInterface sesion, int idPartida) throws RemoteException;
	
	public boolean setBarcos(SesionInterface sesion, int idPartida, IBarco barco1, IBarco barco2) throws RemoteException;
	
	public boolean realizarDisparo(SesionInterface sesion, int idPartida, IDisparo disparo) throws RemoteException;
	
	public boolean abandonarPartida(SesionInterface sesion, int idPartida) throws RemoteException;

}
