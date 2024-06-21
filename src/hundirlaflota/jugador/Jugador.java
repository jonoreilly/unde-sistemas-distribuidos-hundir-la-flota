package hundirlaflota.jugador;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

import hundirlaflota.jugador_servidor.ServicioAutenticacionInterface;
import hundirlaflota.jugador_servidor.ServicioGestorInterface;

/**
 * @author Jon Oreilly del Cerro joreilly1@alumno.uned.es
 */

public class Jugador {

	public static void main(String[] args) {
		try {

			String version = System.getProperty("java.version");
			System.out.println("Version: " + version);

			try {
				// Iniciar Registry
				LocateRegistry.createRegistry(1099);
			} catch(Exception e) {
				// Registry ya ha sido creado
			}

			// Iniciar Registry

			if (LocateRegistry.getRegistry() == null) {
				LocateRegistry.createRegistry(1099);
			}
			
			// Iniciar lista de mensajes callback
			
			CallbackJugadorListaSincronizada listaMensajesCallbackSincronizada = new CallbackJugadorListaSincronizada();

			// Conectarse al servidor

			System.out.println("Conectandose al servidor...");
			
			ServicioAutenticacionInterface servicioAutenticacion = (ServicioAutenticacionInterface) Naming.lookup("servicioAutenticacion");
			
			ServicioGestorInterface servicioGestor = (ServicioGestorInterface) Naming.lookup("servicioGestor");

			System.out.println("Conectado al servidor");
			

			// Aceptar comandos de linea

			Juego juego = new Juego(servicioAutenticacion, servicioGestor, listaMensajesCallbackSincronizada);
			
			juego.iniciar();

		} catch (Exception e) {
			System.out.println("Error en el jugador: " + e.getMessage());
		}
	}
	
}
