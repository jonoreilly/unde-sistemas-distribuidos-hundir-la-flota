package hundirlaflota.jugador;

import java.util.ArrayList;
import java.util.List;

import hundirlaflota.Consola;
import hundirlaflota.IBarco;
import hundirlaflota.IDisparo;
import hundirlaflota.InformacionJugadorInterface;
import hundirlaflota.Listas;
import hundirlaflota.jugador_servidor.CallbackJugadorInterface;
import hundirlaflota.jugador_servidor.CallbackJugadorMensajeEnum;
import hundirlaflota.jugador_servidor.IInformacionPartidaEnEspera;
import hundirlaflota.jugador_servidor.ITablero;
import hundirlaflota.jugador_servidor.ServicioAutenticacionInterface;
import hundirlaflota.jugador_servidor.ServicioGestorInterface;
import hundirlaflota.jugador_servidor.SesionInterface;

/**
 * @author Jon Oreilly del Cerro joreilly1@alumno.uned.es
 */

public class Juego {

	public enum Pantalla {
		MENU_INICIAL, REGISTRAR_NUEVO_USUARIO, INICIAR_SESION, LOBBY, INFORMACION_DEL_JUGADOR, CREAR_PARTIDA,
		LISTAR_PARTIDAS_EN_ESPERA, UNIRSE_A_UNA_PARTIDA, POSICIONAR_BARCOS, DISPARAR, ESPERAR_DISPARO_CONTRINCANTE,
		SALIR
	}

	private ServicioAutenticacionInterface servicioAutenticacion;

	private ServicioGestorInterface servicioGestor;

	private CallbackJugadorListaSincronizada listaMensajesCallbackSincronizada;

	private SesionInterface sesion;

	private Integer idPartida;

	public Juego(ServicioAutenticacionInterface servicioAutenticacion, ServicioGestorInterface servicioGestor,
			CallbackJugadorListaSincronizada listaMensajesCallbackSincronizada) {
		this.servicioAutenticacion = servicioAutenticacion;
		this.servicioGestor = servicioGestor;
		this.listaMensajesCallbackSincronizada = listaMensajesCallbackSincronizada;
	}

	public void iniciar() {
		// Mensaje de bienvenida
		System.out.println("");
		System.out.println("");
		System.out.println("#####    Juego de barcos    ####");
		System.out.println("");

		Pantalla pantalla = Pantalla.MENU_INICIAL;

		while (pantalla != Pantalla.SALIR) {

			switch (pantalla) {

			case MENU_INICIAL: {
				pantalla = this.menuInicial();
				break;
			}

			case REGISTRAR_NUEVO_USUARIO: {
				pantalla = this.registrarNuevoUsuario();
				break;
			}

			case INICIAR_SESION: {
				pantalla = this.iniciarSesion();
				break;
			}

			case LOBBY: {
				pantalla = this.lobby();
				break;
			}

			case INFORMACION_DEL_JUGADOR: {
				pantalla = this.informacionDelJugador();
				break;
			}

			case CREAR_PARTIDA: {
				pantalla = this.crearPartida();
				break;
			}

			case LISTAR_PARTIDAS_EN_ESPERA: {
				pantalla = this.listarPartidasEnEspera();
				break;
			}

			case UNIRSE_A_UNA_PARTIDA: {
				pantalla = this.unirseAUnaPartida();
				break;
			}

			case POSICIONAR_BARCOS: {
				pantalla = this.posicionarBarcos();
				break;
			}

			case DISPARAR: {
				pantalla = this.disparar();
				break;
			}

			case ESPERAR_DISPARO_CONTRINCANTE: {
				pantalla = this.esperarDisparoContrincante();
				break;
			}

			case SALIR: {
				return;
			}

			}

		}

	}

	private Pantalla menuInicial() {
		List<String> opciones = new ArrayList<>();
		opciones.add("Registrar un nuevo jugador");
		opciones.add("Iniciar sesion");
		opciones.add("Salir");

		int opcion = Consola.menu("", opciones);

		switch (opcion) {
		case 1:
			return Pantalla.REGISTRAR_NUEVO_USUARIO;

		case 2:
			return Pantalla.INICIAR_SESION;

		default:
			return Pantalla.SALIR;
		}
	}

	private Pantalla registrarNuevoUsuario() {
		System.out.println("");
		System.out.println("Registrar un nuevo jugador");

		// Introducir usuario

		boolean esUsuarioValido = false;

		String usuario = "";

		while (!esUsuarioValido) {
			usuario = Consola.entrada(" Usuario: ");

			if (usuario.length() < 3) {
				System.out.println("Demasiado corto, al menos 3 caracteres");
				continue;
			}

			esUsuarioValido = true;
		}

		// Introducir contraseña

		boolean esContraseniaValida = false;

		String contrasenia = "";

		while (!esContraseniaValida) {
			contrasenia = Consola.entrada(" Contraseña: ");

			if (contrasenia.length() < 3) {
				System.out.println("Demasiado corto, al menos 3 caracteres");
				continue;
			}

			esContraseniaValida = true;
		}

		// Peticion al servidor

		try {
			boolean exito = this.servicioAutenticacion.registrarJugador(usuario, contrasenia);

			if (exito) {
				System.out.println();
				System.out.println("Jugador " + usuario + " registrado exitosamente");
				System.out.println();

				return Pantalla.MENU_INICIAL;
			} else {
				System.out.println();
				System.out.println("ERROR: Parece que el jugador " + usuario + " ya existe.");
				System.out.println("Intente iniciar sesión");
				System.out.println();

				return Pantalla.MENU_INICIAL;
			}
		} catch (Exception e) {
			System.out.println();
			System.out.println("ERROR: Algo inesperado ha sucedido");
			System.out.println(e);
			System.out.println();

			return Pantalla.SALIR;
		}

	}

	private Pantalla iniciarSesion() {
		System.out.println("");
		System.out.println("Iniciar sesión");
		System.out.println("");

		// Introducir credenciales

		String usuario = Consola.entrada(" Usuario: ");

		String contrasenia = Consola.entrada(" Contraseña: ");

		// Peticion al servidor

		try {

			CallbackJugadorInterface callbackJugador = new CallbackJugadorImpl(this.listaMensajesCallbackSincronizada);

			SesionInterface sesion = this.servicioAutenticacion.iniciarSesion(usuario, contrasenia, callbackJugador);

			if (sesion != null) {
				this.sesion = sesion;

				System.out.println();
				System.out.println("Sesión iniciada con el jugador " + usuario);
				System.out.println();

				return Pantalla.LOBBY;
			} else {
				System.out.println();
				System.out.println("ERROR: Credenciales incorrectas. Pruebe otra vez");
				System.out.println();

				return Pantalla.MENU_INICIAL;
			}
		} catch (Exception e) {
			System.out.println();
			System.out.println("ERROR: Algo inesperado ha sucedido");
			System.out.println(e);
			System.out.println();

			return Pantalla.SALIR;
		}
	}

	private Pantalla lobby() {
		List<String> opciones = new ArrayList<>();
		opciones.add("Información del jugador (consultar puntuación histórica)");
		opciones.add("Iniciar una partida");
		opciones.add("Listar partidas iniciadas a la espera de contrincante");
		opciones.add("Unirse a una partida ya iniciada");
		opciones.add("Salir \"Logout\"");

		int opcion = Consola.menu("", opciones);

		switch (opcion) {
		case 1:
			return Pantalla.INFORMACION_DEL_JUGADOR;

		case 2:
			return Pantalla.CREAR_PARTIDA;

		case 3:
			return Pantalla.LISTAR_PARTIDAS_EN_ESPERA;

		case 4:
			return Pantalla.UNIRSE_A_UNA_PARTIDA;

		default:
			try {
				// Cerrar sesión

				boolean exito = this.servicioAutenticacion.cerrarSesion(this.sesion);

				if (!exito) {
					System.out.println();
					System.out.println("ERROR: no se ha podido cerrar la sesión:");
					System.out.println();
				}
				
				this.sesion = null;
				
				this.idPartida = null;

				return Pantalla.MENU_INICIAL;
				
			} catch (Exception e) {
				System.out.println();
				System.out.println("ERROR: Algo inesperado ha sucedido");
				System.out.println(e);
				System.out.println();

				return Pantalla.SALIR;
			}

		}
	}

	private Pantalla informacionDelJugador() {
		System.out.println();
		System.out.println("Informacion del jugador:");
		System.out.println();

		try {
			InformacionJugadorInterface informacion = this.servicioGestor.getInformacionJugador(this.sesion);

			System.out.println(" Jugador: " + informacion.getJugador());
			System.out.println(" Partidas jugadas: " + informacion.getPartidasJugadas());
			System.out.println(" Puntuación total: " + informacion.getPuntuacion());
			if (informacion.getPartidasJugadas() > 0) {
				System.out.println(" Puntuación media por partida: "
						+ informacion.getPuntuacion() / informacion.getPartidasJugadas());
			}

			return Pantalla.LOBBY;
		} catch (Exception e) {
			System.out.println();
			System.out.println("ERROR: Algo inesperado ha sucedido");
			System.out.println(e);
			System.out.println();

			return Pantalla.SALIR;
		}
	}

	private Pantalla crearPartida() {
		System.out.println();
		System.out.println("Crear partida:");
		System.out.println();

		try {
			// Crear partida

			System.out.println();
			System.out.println("Creando partida...");
			System.out.println();

			Integer idPartida = this.servicioGestor.crearPartida(this.sesion);

			if (idPartida == null) {
				System.out.println();
				System.out.println("ERROR: No se ha podido crear la partida");
				System.out.println();

				return Pantalla.LOBBY;
			}

			System.out.println();
			System.out.println("Partida creada");
			System.out.println();

			this.idPartida = idPartida;

			// Esperar a que se una un contrincante

			System.out.println();
			System.out.println("Esperando a que se conecte un contrincante...");
			System.out.println();

			while (true) {
				CallbackJugadorMensajeEnum mensaje = this.listaMensajesCallbackSincronizada.recibirMensaje();

				if (mensaje == CallbackJugadorMensajeEnum.CONTRINCANTE_UNIDO_COLOCAR_BARCOS) {
					System.out.println();
					System.out.println("Contrincante encontrado");
					System.out.println();

					return Pantalla.POSICIONAR_BARCOS;
				}

				System.out.println();
				System.out.println("Mensaje inesperado: " + mensaje);
				System.out.println();
			}

		} catch (Exception e) {
			System.out.println();
			System.out.println("ERROR: Algo inesperado ha sucedido");
			System.out.println(e);
			System.out.println();

			return Pantalla.SALIR;
		}
	}

	private Pantalla listarPartidasEnEspera() {
		System.out.println();
		System.out.println("Listado de jugadores en espera");
		System.out.println();

		try {

			List<IInformacionPartidaEnEspera> partidasEnEspera = this.servicioGestor.getJugadoresEnEspera(this.sesion);

			// Comprobar si hay partidas en espera

			if (partidasEnEspera == null || partidasEnEspera.size() == 0) {
				System.out.println();
				System.out.println("En este momento no hay partidas en espera");
				System.out.println();

				return Pantalla.LOBBY;
			}

			// Listar las partidas en espera

			System.out.println();
			System.out.println("Partidas en espera: " + partidasEnEspera.size());
			System.out.println();

			for (IInformacionPartidaEnEspera informacion : partidasEnEspera) {
				InformacionJugadorInterface contrincante = informacion.getInformacionContrincante();

				System.out.println();
				System.out.println(" ID: " + informacion.getIdPartida());

				if (contrincante == null) {
					System.out.println();
					System.out.println("ERROR: No se ha podido obtener la información del jugador");
					System.out.println();

					continue;
				}

				System.out.println(" Usuario: " + contrincante.getJugador());
				System.out.println(" Puntuación total: " + contrincante.getPuntuacion());
				System.out.println(" Partidas jugadas: " + contrincante.getPartidasJugadas());
				if (contrincante.getPartidasJugadas() > 0) {
					System.out.println(" Puntuación media por partida: "
							+ contrincante.getPuntuacion() / contrincante.getPartidasJugadas());
				}
			}

			System.out.println();

			return Pantalla.LOBBY;

		} catch (Exception e) {
			System.out.println();
			System.out.println("ERROR: Algo inesperado ha sucedido");
			System.out.println(e);
			System.out.println();

			return Pantalla.SALIR;
		}
	}

	private Pantalla unirseAUnaPartida() {
		System.out.println();
		System.out.println("Unirse a una partida");
		System.out.println();

		try {

			List<IInformacionPartidaEnEspera> partidasEnEspera = this.servicioGestor.getJugadoresEnEspera(this.sesion);

			// Comprobar si hay partidas en espera

			if (partidasEnEspera == null || partidasEnEspera.size() == 0) {
				System.out.println();
				System.out.println("En este momento no hay partidas en espera");
				System.out.println();

				return Pantalla.LOBBY;
			}

			// Listar las partidas en espera como un menú

			List<String> opciones = Listas.transformarLista(partidasEnEspera, (p) -> {
				InformacionJugadorInterface contrincante = p.getInformacionContrincante();

				if (contrincante == null) {
					return "Error: Jugador no encontrado";
				}

				return contrincante.getJugador() + ": " + contrincante.getPuntuacion() + "/"
						+ contrincante.getPartidasJugadas();
			});

			opciones.add("Cancelar (volver al menú)");

			int opcion = Consola.menu("Partidas en espera:", opciones);

			if (opcion == opciones.size()) {
				return Pantalla.LOBBY;
			}

			IInformacionPartidaEnEspera partida = partidasEnEspera.get(opcion - 1);

			if (partida == null) {
				System.out.println();
				System.out.println("La selección de partida no ha funcionado correctamente");
				System.out.println();

				return Pantalla.LOBBY;
			}

			// Unirse a la partida

			boolean exito = this.servicioGestor.unirseAPartida(this.sesion, partida.getIdPartida());

			if (exito) {
				System.out.println();
				System.out.println("Te has unido a la partida");
				System.out.println();

				this.idPartida = partida.getIdPartida();

				return Pantalla.POSICIONAR_BARCOS;
			}

			System.out.println();
			System.out.println("No te has podido unir a la partida");
			System.out.println();

			return Pantalla.LOBBY;

		} catch (Exception e) {
			System.out.println();
			System.out.println("ERROR: Algo inesperado ha sucedido");
			System.out.println(e);
			System.out.println();

			return Pantalla.SALIR;
		}
	}

	private Pantalla posicionarBarcos() {
		try {

			System.out.println();
			System.out.println("Posiciona tus barcos");
			System.out.println();

			// Introducir las coordenadas de tus barcos

			IBarco barco1 = Utils.posicionarBarco("Introduzca coordenadas barco 1", null);
			IBarco barco2 = Utils.posicionarBarco("Introduzca coordenadas barco 2", barco1);

			// Subir las coordenadas al servidor

			boolean exito = this.servicioGestor.setBarcos(this.sesion, this.idPartida, barco1, barco2);

			if (!exito) {

				System.out.println();
				System.out.println("ERROR: No se han podido posicionar los barcos");
				System.out.println();

				return Pantalla.LOBBY;
			}

			System.out.println();
			System.out.println("Barcos colocados. Esperando al contrincante...");
			System.out.println();

			// Esperar a que el contrincante coloque sus barcos

			while (true) {

				CallbackJugadorMensajeEnum mensaje = this.listaMensajesCallbackSincronizada.recibirMensaje();

				if (mensaje == CallbackJugadorMensajeEnum.COMIENZA_EL_JUEGO_TU_TURNO) {

					ITablero tablero = this.servicioGestor.getTablero(this.sesion, this.idPartida);

					if (tablero == null) {
						System.out.println();
						System.out.println("ERROR: No se han podido encontrar el tablero");
						System.out.println();

						return Pantalla.LOBBY;
					}

					Utils.mostrarInformacionDeInicioDePartida(tablero);

					return Pantalla.DISPARAR;
				}

				if (mensaje == CallbackJugadorMensajeEnum.COMIENZA_EL_JUEGO_TURNO_CONTRINCANTE) {

					ITablero tablero = this.servicioGestor.getTablero(this.sesion, this.idPartida);

					if (tablero == null) {
						System.out.println();
						System.out.println("ERROR: No se han podido encontrar el tablero");
						System.out.println();

						return Pantalla.LOBBY;
					}

					Utils.mostrarInformacionDeInicioDePartida(tablero);

					return Pantalla.ESPERAR_DISPARO_CONTRINCANTE;
				}

				System.out.println();
				System.out.println("Mensaje inesperado: " + mensaje);
				System.out.println();
			}

		} catch (Exception e) {
			System.out.println();
			System.out.println("ERROR: Algo inesperado ha sucedido");
			System.out.println(e);
			System.out.println();

			return Pantalla.SALIR;
		}
	}

	private Pantalla disparar() {
		try {

			ITablero tablero = this.servicioGestor.getTablero(this.sesion, this.idPartida);

			if (tablero == null) {
				System.out.println();
				System.out.println("ERROR: No se han podido encontrar el tablero");
				System.out.println();

				return Pantalla.LOBBY;
			}

			// Realizar disparo

			System.out.println();

			IDisparo disparo = Utils.leerDisparo("Introduce las coordenadas de tu disparo",
					tablero.getDisparosUsuario());

			// Comprobar si capitula

			if (disparo == null) {

				boolean exito = this.servicioGestor.abandonarPartida(this.sesion, this.idPartida);

				if (!exito) {
					System.out.println();
					System.out.println("ERROR: No se ha podido capitular");
					System.out.println();

					return Pantalla.LOBBY;
				}

				tablero = this.servicioGestor.getTablero(this.sesion, this.idPartida);

				if (tablero == null) {
					System.out.println();
					System.out.println("ERROR: No se han podido encontrar el tablero");
					System.out.println();

					return Pantalla.LOBBY;
				}

				Utils.mostrarCapitulacionJugador(tablero);

				return Pantalla.LOBBY;
			}

			boolean exito = this.servicioGestor.realizarDisparo(this.sesion, this.idPartida, disparo);

			if (!exito) {
				System.out.println();
				System.out.println("ERROR: No se han podido realizar el disparo");
				System.out.println();

				return Pantalla.LOBBY;
			}

			// Resultado del disparo

			while (true) {
				CallbackJugadorMensajeEnum mensaje = this.listaMensajesCallbackSincronizada.recibirMensaje();

				if (mensaje == CallbackJugadorMensajeEnum.VICTORIA) {

					tablero = this.servicioGestor.getTablero(this.sesion, this.idPartida);

					if (tablero == null) {
						System.out.println();
						System.out.println("ERROR: No se han podido encontrar el tablero");
						System.out.println();

						return Pantalla.LOBBY;
					}

					Utils.mostrarVictoria(tablero);
					
					this.idPartida = null;

					return Pantalla.LOBBY;

				}

				if (mensaje == CallbackJugadorMensajeEnum.DISPARO_TUYO_AGUA
						|| mensaje == CallbackJugadorMensajeEnum.DISPARO_TUYO_TOCADO) {

					tablero = this.servicioGestor.getTablero(this.sesion, this.idPartida);

					if (tablero == null) {
						System.out.println();
						System.out.println("ERROR: No se han podido encontrar el tablero");
						System.out.println();

						return Pantalla.LOBBY;
					}

					Utils.mostrarInformacionDespuesDeDisparo(tablero);

					return Pantalla.ESPERAR_DISPARO_CONTRINCANTE;

				}

				System.out.println();
				System.out.println("Mensaje inesperado: " + mensaje);
				System.out.println();
			}

		} catch (Exception e) {
			System.out.println();
			System.out.println("ERROR: Algo inesperado ha sucedido");
			System.out.println(e);
			System.out.println();

			return Pantalla.SALIR;
		}
	}

	private Pantalla esperarDisparoContrincante() {
		try {

			System.out.println();
			System.out.println("Esperando al disparo del contrincante...");

			// Esperar a disparo del contrincante

			while (true) {
				CallbackJugadorMensajeEnum mensaje = this.listaMensajesCallbackSincronizada.recibirMensaje();

				if (mensaje == CallbackJugadorMensajeEnum.DERROTA) {

					ITablero tablero = this.servicioGestor.getTablero(this.sesion, this.idPartida);

					if (tablero == null) {
						System.out.println();
						System.out.println("ERROR: No se han podido encontrar el tablero");
						System.out.println();

						return Pantalla.LOBBY;
					}

					Utils.mostrarDerrota(tablero);

					this.idPartida = null;

					return Pantalla.LOBBY;

				}

				if (mensaje == CallbackJugadorMensajeEnum.DISPARO_CONTRINCANTE_AGUA
						|| mensaje == CallbackJugadorMensajeEnum.DISPARO_CONTRINCANTE_TOCADO) {

					ITablero tablero = this.servicioGestor.getTablero(this.sesion, this.idPartida);

					if (tablero == null) {
						System.out.println();
						System.out.println("ERROR: No se han podido encontrar el tablero");
						System.out.println();

						return Pantalla.LOBBY;
					}

					Utils.mostrarInformacionDespuesDeEspera(tablero);

					return Pantalla.DISPARAR;

				}

				if (mensaje == CallbackJugadorMensajeEnum.CONTRINCANTE_CAPITULA) {

					ITablero tablero = this.servicioGestor.getTablero(this.sesion, this.idPartida);

					if (tablero == null) {
						System.out.println();
						System.out.println("ERROR: No se han podido encontrar el tablero");
						System.out.println();

						return Pantalla.LOBBY;
					}

					Utils.mostrarCapitulacionContrincante(tablero);

					return Pantalla.LOBBY;

				}

				System.out.println();
				System.out.println("Mensaje inesperado: " + mensaje);
				System.out.println();
			}

		} catch (Exception e) {
			System.out.println();
			System.out.println("ERROR: Algo inesperado ha sucedido");
			System.out.println(e);
			System.out.println();

			return Pantalla.SALIR;
		}
	}
}
