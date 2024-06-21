package hundirlaflota.jugador;

import java.util.ArrayList;
import java.util.List;

import hundirlaflota.Barco;
import hundirlaflota.Celda;
import hundirlaflota.Consola;
import hundirlaflota.Disparo;
import hundirlaflota.EColumna;
import hundirlaflota.EFila;
import hundirlaflota.EOrientacion;
import hundirlaflota.IBarco;
import hundirlaflota.ICelda;
import hundirlaflota.IDisparo;
import hundirlaflota.Listas;
import hundirlaflota.NormasDeJuego;
import hundirlaflota.jugador_servidor.EResultadoDisparo;
import hundirlaflota.jugador_servidor.IDisparoYResultado;
import hundirlaflota.jugador_servidor.ITablero;

/**
 * @author Jon Oreilly del Cerro joreilly1@alumno.uned.es
 */

public final class Utils {

	public static IBarco posicionarBarco(String titulo, IBarco otroBarco) {
		while (true) {

			IBarco barco = Utils.leerBarco(titulo);

			// El barco no se sale del tablero (longitud 3 casillas)

			if (!NormasDeJuego.cabeEnElTablero(barco)) {
				if (barco.getOrientacion() == EOrientacion.HORIZONTAL) {
					System.out.println();
					System.out.println(
							"ERROR: El barco no puede salirse del tablero. Para poner en horizontal la proa debe estar entre las columnas 1 y 8");
					System.out.println();
				} else {
					System.out.println();
					System.out.println(
							"ERROR: El barco no puede salirse del tablero. Para poner en vertical la proa debe estar entre las filas A y H");
					System.out.println();
				}

				continue;
			}

			// El barco no coincide con otros barcos

			if (otroBarco != null) {

				if (NormasDeJuego.seSolapan(barco, otroBarco)) {
					System.out.println();
					System.out.println("ERROR: Este barco se solapa con el anterior");
					System.out.println();
					continue;
				}

			}

			// Mostrar nuevo barco

			List<IBarco> barcos = new ArrayList<>();

			barcos.add(barco);

			if (otroBarco != null) {
				barcos.add(otroBarco);
			}

			System.out.println();
			System.out.println("Tus barcos:");

			Utils.mostrarTableroBarcosYDisparos(barcos, new ArrayList<>());

			return barco;

		}
	}

	public static IBarco leerBarco(String titulo) {
		while (true) {
			
			String entrada = Consola.entrada(titulo + " [YXZ]: ");

			for (EFila fila : NormasDeJuego.filas) {
				for (EColumna columna : NormasDeJuego.columnas) {
					for (EOrientacion orientacion : NormasDeJuego.orientaciones) {

						// A5H
						String coordenada = fila.name() + columna.name().substring(1)
								+ orientacion.name().substring(0, 1);

						if (coordenada.contentEquals(entrada)) {
							return new Barco(fila, columna, orientacion);
						}
					}
				}
			}

			System.out.println();
			System.out.println("Error: posición inválida. Debe atenerse a la estructura YXZ");
			System.out.println("  Y = fila de la proa (A – J)");
			System.out.println("  X = columna de la proa (1 – 10)");
			System.out.println("  Z = orientación (V=vertical, H=horizontal)");
			System.out.println();
		}
	}

	public static <T extends IDisparo> IDisparo leerDisparo(String titulo, List<T> disparos) {
		while (true) {

			IDisparo disparo = Utils.leerCoordenadasDisparo(titulo);

			// Comprobar si se rinde
			
			if (disparo == null) {
				return null;
			}

			// Comprobar que no coincide con otro disparo
			
			if (Listas.contiene(disparos, (d) -> NormasDeJuego.coinciden(d, disparo))) {
				System.out.println();
				System.out.println("ERROR: Ya has disparado en esta posición");
				System.out.println();
				continue;
			}

			return disparo;

		}
	}

	public static IDisparo leerCoordenadasDisparo(String titulo) {
		while (true) {
			
			String entrada = Consola.entrada(titulo + " [YX] (o CAPITULO): ");

			// Comprobar si se rinde
			
			if (entrada.contentEquals("CAPITULO")) {
				return null;
			}
			
			// Comprobar si es formao válido
			
			for (EFila fila : NormasDeJuego.filas) {
				for (EColumna columna : NormasDeJuego.columnas) {

					// A5
					String coordenada = fila.name() + columna.name().substring(1);

					if (coordenada.contentEquals(entrada)) {
						return new Disparo(fila, columna);
					}
				}
			}

			System.out.println();
			System.out.println("Error: posición inválida. Debe atenerse a la estructura YXZ");
			System.out.println("  Y = fila de la proa (A – J)");
			System.out.println("  X = columna de la proa (1 – 10)");
			System.out.println();
			System.out.println("  O escribe CAPITULO para rendirte");
			System.out.println();
		}
	}

	private static EFila leerEntradaFila() {
		while (true) {

			String fila = Consola.entrada(" Fila [A-J]: ");

			switch (fila) {
			case "A": {
				return EFila.A;
			}
			case "B": {
				return EFila.B;
			}
			case "C": {
				return EFila.C;
			}
			case "D": {
				return EFila.D;
			}
			case "E": {
				return EFila.E;
			}
			case "F": {
				return EFila.F;
			}
			case "G": {
				return EFila.G;
			}
			case "H": {
				return EFila.H;
			}
			case "I": {
				return EFila.I;
			}
			case "J": {
				return EFila.J;
			}
			}

			System.out.println("Valor inálido. Debe ser una letra entre A y J");

		}
	}

	private static EColumna leerEntradaColumna() {
		while (true) {

			String columna = Consola.entrada(" Columna [1-10]: ");

			switch (columna) {
			case "1": {
				return EColumna._1;
			}
			case "2": {
				return EColumna._2;
			}
			case "3": {
				return EColumna._3;
			}
			case "4": {
				return EColumna._4;
			}
			case "5": {
				return EColumna._5;
			}
			case "6": {
				return EColumna._6;
			}
			case "7": {
				return EColumna._7;
			}
			case "8": {
				return EColumna._8;
			}
			case "9": {
				return EColumna._9;
			}
			case "10": {
				return EColumna._10;
			}
			}

			System.out.println("Valor inálido. Debe ser un número entre 1 y 10");

		}
	}

	private static EOrientacion leerEntradaOrientacion() {
		while (true) {

			String orientacion = Consola.entrada(" Orientación [V=vertical H=horizontal]: ");

			switch (orientacion) {
			case "V": {
				return EOrientacion.VERTICAL;
			}
			case "H": {
				return EOrientacion.HORIZONTAL;
			}
			}

			System.out.println("Valor inálido. Debe ser las letras V o H");

		}
	}

	public static void mostrarInformacionDeInicioDePartida(ITablero tablero) {

		Utils.mostrarTitulo();

		Utils.mostrarTableroEnCurso(tablero);

	}

	public static void mostrarInformacionDespuesDeEspera(ITablero tablero) {

		Utils.mostrarTitulo();

		if (tablero.getDisparosContrincante().size() > 0) {
			Utils.mostrarResultadoDisparoContrincante(tablero);
		}

		Utils.mostrarTableroEnCurso(tablero);

	}

	public static void mostrarInformacionDespuesDeDisparo(ITablero tablero) {

		Utils.mostrarTitulo();

		Utils.mostrarResultadoDisparoUsuario(tablero);

		Utils.mostrarTableroEnCurso(tablero);

	}

	public static void mostrarCapitulacionContrincante(ITablero tablero) {
		Utils.mostrarTitulo();

		System.out.println();
		System.out.println("El contrincante ha capitulado");
		System.out.println("  VICTORIA   +4 puntos");

		Utils.mostrarTableroTerminado(tablero);

		Utils.mostrarBanderaVictoria();
	}

	public static void mostrarCapitulacionJugador(ITablero tablero) {
		Utils.mostrarTitulo();

		System.out.println();
		System.out.println("Has capitulado");

		Utils.mostrarTableroTerminado(tablero);

		Utils.mostrarBanderaDerrota();
	}

	public static void mostrarVictoria(ITablero tablero) {

		Utils.mostrarTitulo();

		Utils.mostrarResultadoDisparoUsuario(tablero);

		System.out.println("  VICTORIA   +4 puntos");

		Utils.mostrarTableroTerminado(tablero);

		Utils.mostrarBanderaVictoria();

	}

	public static void mostrarDerrota(ITablero tablero) {

		Utils.mostrarTitulo();

		Utils.mostrarResultadoDisparoContrincante(tablero);

		Utils.mostrarTableroTerminado(tablero);

		Utils.mostrarBanderaDerrota();

	}

	public static void mostrarTitulo() {
		System.out.println();
		System.out.println();
		System.out.println("############    HUNDIR LA FLOTA    ############");
		System.out.println();
	}

	public static void mostrarBanderaVictoria() {
		System.out.println();
		System.out.println();
		System.out.println("##########################");
		System.out.println("####                  ####");
		System.out.println("####     VICTORIA     ####");
		System.out.println("####                  ####");
		System.out.println("##########################");
		System.out.println();
	}

	public static void mostrarBanderaDerrota() {
		System.out.println();
		System.out.println();
		System.out.println("#########################");
		System.out.println("####                 ####");
		System.out.println("####     DERROTA     ####");
		System.out.println("####                 ####");
		System.out.println("#########################");
		System.out.println();
	}

	public static void mostrarResultadoDisparoUsuario(ITablero tablero) {

		IDisparoYResultado disparo = Listas.ultimo(tablero.getDisparosUsuario());

		System.out.println("");
		System.out.println("Tu disparo: " + Utils.disparoToString(disparo));

		if (disparo.getResultadoDisparo() == EResultadoDisparo.AGUA) {
			System.out.println("  AGUA");
		} else {
			System.out.println("  TOCADO    +1 punto");
		}

	}

	public static void mostrarResultadoDisparoContrincante(ITablero tablero) {

		IDisparo disparo = Listas.ultimo(tablero.getDisparosContrincante());

		System.out.println("");
		System.out.println("Disparo del contrincante: " + Utils.disparoToString(disparo));

		boolean tocaBarco = NormasDeJuego.tocaBarco(disparo, tablero.getBarcosUsuario());

		if (tocaBarco) {
			System.out.println("  TOCADO");
		} else {
			System.out.println("  AGUA");
		}

	}

	public static void mostrarTableroEnCurso(ITablero tablero) {

		System.out.println();
		System.out.println("Tus barcos:");

		Utils.mostrarTableroBarcosYDisparos(tablero.getBarcosUsuario(), tablero.getDisparosContrincante());

		System.out.println();
		System.out.println("Tus disparos:");

		Utils.mostrarTableroDisparos(tablero.getDisparosUsuario());

	}

	public static void mostrarTableroTerminado(ITablero tablero) {

		System.out.println();
		System.out.println("Tus barcos:");

		Utils.mostrarTableroBarcosYDisparos(tablero.getBarcosUsuario(), tablero.getDisparosContrincante());

		System.out.println();
		System.out.println("Barcos del contrincante:");

		Utils.mostrarTableroBarcosYDisparos(tablero.getBarcosContrincante(), tablero.getDisparosUsuario());

	}

	public static <T extends IDisparo> void mostrarTableroBarcosYDisparos(List<IBarco> barcos, List<T> disparos) {

		System.out.println("");
		System.out.println("    1 2 3 4 5 6 7 8 9 10");

		for (EFila fila : NormasDeJuego.filas) {
			System.out.print("  " + fila.toString());

			for (EColumna columna : NormasDeJuego.columnas) {
				ICelda celda = new Celda(fila, columna);

				boolean tieneBarco = NormasDeJuego.tocaBarco(celda, barcos);
				boolean tieneDisparo = NormasDeJuego.coincideAlMenosUno(celda, disparos);

				System.out.print(" ");

				if (tieneBarco) {
					if (tieneDisparo) {
						System.out.print("*");
					} else {
						System.out.print("B");
					}
				} else {
					if (tieneDisparo) {
						System.out.print("•");
					} else {
						System.out.print(" ");
					}
				}
			}

			System.out.println("");
		}

		System.out.println("");
	}

	private static void mostrarTableroDisparos(List<IDisparoYResultado> disparos) {

		System.out.println("");
		System.out.println("    1 2 3 4 5 6 7 8 9 10");

		for (EFila fila : NormasDeJuego.filas) {
			System.out.print("  " + fila.toString());

			for (EColumna columna : NormasDeJuego.columnas) {
				ICelda celda = new Celda(fila, columna);

				IDisparoYResultado disparo = Listas.buscar(disparos, (d) -> NormasDeJuego.coinciden(d, celda));

				System.out.print(" ");

				if (disparo == null) {
					System.out.print(" ");
				} else {
					if (disparo.getResultadoDisparo() == EResultadoDisparo.AGUA) {
						System.out.print("•");
					} else {
						System.out.print("*");
					}
				}
			}

			System.out.println("");
		}

		System.out.println("");
	}

	public static String disparoToString(IDisparo disparo) {
		return disparo.getFila().toString() + "-" + disparo.getColumna().toString().substring(1);
	}

}
