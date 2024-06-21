package hundirlaflota.basededatos;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.util.Arrays;
import java.util.List;

import hundirlaflota.Consola;
import hundirlaflota.InformacionJugadorInterface;
import hundirlaflota.servidor_basededatos.ServicioDatosInterface;

/**
 * @author Jon Oreilly del Cerro joreilly1@alumno.uned.es
 */

public class Basededatos {

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

			// Iniciando tablas de datos

			TablaContrasenas tablaContrasenas = new TablaContrasenas();

			TablaInformacionJugadores tablaInformacionJugadores = new TablaInformacionJugadores();

			TablaPartidas tablaPartidas = new TablaPartidas();

			// Iniciar servicio de datos

			System.out.println("Iniciando el servicio de datos...");

			ServicioDatosInterface servicioDatos = new ServicioDatosImpl(tablaContrasenas, tablaInformacionJugadores,
					tablaPartidas);

			Naming.rebind("servicioDatos", servicioDatos);

			System.out.println("Servicio de datos listo");

			// Aceptar comandos de linea

			while (true) {

				int opcion = Consola.menu("Base de datos:", Arrays.asList("Información de la Base de Datos",
						"Listar jugadores registrados (Sus puntuaciones)", "Salir"));

				switch (opcion) {

				case 1: {
					System.out.println();
					System.out.println("Servidor de base de datos en marcha");
					System.out.println("  rmi://localhost:1099/servicioDatos");
					System.out.println();
					break;
				}

				case 2: {

					List<InformacionJugadorInterface> informaciones = tablaInformacionJugadores.getInformacionJugadores();
					
					if(informaciones == null || informaciones.size() == 0) {
						System.out.println();
						System.out.println("No hay usuarios registrados");
						System.out.println();
					} else {
						for (InformacionJugadorInterface informacion : informaciones) {
							System.out.println();
							System.out.println(" Jugador: " + informacion.getJugador());
							System.out.println(" Partidas jugadas: " + informacion.getPartidasJugadas());
							System.out.println(" Puntuación total: " + informacion.getPuntuacion());
							if (informacion.getPartidasJugadas() > 0) {
								System.out.println(" Puntuación media por partida: "
										+ informacion.getPuntuacion() / informacion.getPartidasJugadas());
							}
							System.out.println();
						}
					}
					
					break;
				}

				case 3: {
					System.out.println("Apagando la base de datos");
					Naming.unbind("servicioDatos");
					return;
				}

				}

			}

		} catch (Exception e) {
			System.out.println("Error en la base de datos: " + e.getMessage());
		}
	}

}
