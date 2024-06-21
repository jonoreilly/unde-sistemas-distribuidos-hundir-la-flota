package hundirlaflota.servidor;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.util.Arrays;
import java.util.List;

import hundirlaflota.Consola;
import hundirlaflota.jugador_servidor.ServicioAutenticacionInterface;
import hundirlaflota.jugador_servidor.ServicioGestorInterface;
import hundirlaflota.servidor_basededatos.IPartida;
import hundirlaflota.servidor_basededatos.ServicioDatosInterface;

/**
 * @author Jon Oreilly del Cerro joreilly1@alumno.uned.es
 */

public class Servidor {

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

			// Iniciar tablas locales de sesion y callbacks

			TablaSesiones tablaSesiones = new TablaSesiones();

			TablaCallbacksJugadores tablaCallbacksJugadores = new TablaCallbacksJugadores();

			// Conectarse a la base de datos

			System.out.println("Conectandose a la base de datos...");

			ServicioDatosInterface servicioDatos = (ServicioDatosInterface) Naming.lookup("servicioDatos");

			System.out.println("Conectado a la base de datos");

			// Iniciar servicios

			System.out.println("Iniciando el servicio de autenticación y el servicio gestor...");

			ServicioAutenticacionInterface servicioAutenticacion = new ServicioAutenticacionImpl(servicioDatos,
					tablaSesiones, tablaCallbacksJugadores);

			ServicioGestorInterface servicioGestor = new ServicioGestorImpl(servicioDatos, tablaSesiones,
					tablaCallbacksJugadores);

			Naming.rebind("servicioAutenticacion", servicioAutenticacion);

			Naming.rebind("servicioGestor", servicioGestor);

			System.out.println("Servicios de autenticación y gestor listos");

			// Aceptar comandos de linea

			while (true) {

				int opcion = Consola.menu("Servidor:", Arrays.asList("Información del Servidor",
						"Estado de las partidas que se están jugando en este momento", "Salir"));

				switch (opcion) {

				case 1: {
					System.out.println();
					System.out.println("Servidor de base de datos en marcha");
					System.out.println("  rmi://localhost:1099/servicioAutenticacion");
					System.out.println("  rmi://localhost:1099/servicioGestor");
					System.out.println();
					break;
				}

				case 2: {
					try {
						List<IPartida> partidas = servicioDatos.getPartidas();
						
						if (partidas == null || partidas.size() == 0) {
							System.out.println();
							System.out.println("No ha habido partidas");
							System.out.println();
						}

						for (IPartida partida : partidas) {
							System.out.println();
							System.out.println("ID de partida: " + partida.getId());
							System.out.println("  Jugador 1: " + partida.getJugador1());
							System.out.println("  Jugador 2: " + partida.getJugador2());
							System.out.println("  Estado: " + partida.getEstado().name());
						}
						
					} catch(Exception e) {
						System.out.println();
						System.out.println("ERROR: Algo inesperado ha sucedido");
						System.out.println(e);
						System.out.println();
					}
					break;
				}

				case 3: {
					System.out.println("Apagando el servidor");
					Naming.unbind("servicioAutenticacion");
					Naming.unbind("servicioGestor");
					return;
				}

				}

			}

		} catch (Exception e) {
			System.out.println("Error en el servidor: " + e.getMessage());
		}
	}

}
