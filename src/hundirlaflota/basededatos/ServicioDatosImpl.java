package hundirlaflota.basededatos;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import hundirlaflota.InformacionJugadorInterface;
import hundirlaflota.servidor_basededatos.IPartida;
import hundirlaflota.servidor_basededatos.ServicioDatosInterface;

/**
 * @author Jon Oreilly del Cerro joreilly1@alumno.uned.es
 */

public class ServicioDatosImpl extends UnicastRemoteObject implements ServicioDatosInterface {

	private static final long serialVersionUID = -2959119202393393491L;

	TablaContrasenas tablaContrasenas;

	TablaInformacionJugadores tablaInformacionJugadores;

	TablaPartidas tablaPartidas;

	protected ServicioDatosImpl(TablaContrasenas tablaContrasenas, TablaInformacionJugadores tablaInformacionJugadores,
			TablaPartidas tablaPartidas) throws RemoteException {

		super();

		this.tablaContrasenas = tablaContrasenas;

		this.tablaInformacionJugadores = tablaInformacionJugadores;

		this.tablaPartidas = tablaPartidas;

	}

	public boolean registrarJugador(String jugador, String contrasena) {
		return this.tablaContrasenas.registrarJugador(jugador, contrasena);
	}

	public boolean iniciarSesion(String jugador, String contrasena) {
		return this.tablaContrasenas.iniciarSesion(jugador, contrasena);
	}

	public InformacionJugadorInterface getInformacionJugador(String jugador) {
		return this.tablaInformacionJugadores.getInformacionJugador(jugador);
	}

	public void setInformacionJugador(InformacionJugadorInterface informacionJugador) {
		this.tablaInformacionJugadores.setInformacionJugador(informacionJugador);
	}
	
	public List<InformacionJugadorInterface> getInformacionJugadores() {
		return this.tablaInformacionJugadores.getInformacionJugadores();
	}

	public IPartida getPartida(int id) {
		return this.tablaPartidas.getPartida(id);
	}
	
	public void setPartida(IPartida partida) {
		 this.tablaPartidas.setPartida(partida);
	}
	
	public List<IPartida> getPartidas() {
		return this.tablaPartidas.getPartidas();
	}

}
