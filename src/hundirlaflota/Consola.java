package hundirlaflota;

import java.util.List;
import java.util.Scanner;


/**
 * @author Jon Oreilly del Cerro joreilly1@alumno.uned.es
 */

public class Consola {

	private static Scanner scanner = new Scanner(System.in);

	public static int menu(String titulo, List<String> opciones) {
		System.out.println();
		System.out.println(titulo);
		System.out.println();

		for (int i = 0; i < opciones.size(); i++) {
			String opcion = opciones.get(i);

			System.out.println(" " + (i + 1) + " - " + opcion);
		}

		System.out.println();

		while (true) {
			int opcion = Consola.entradaNumerica("> ");

			if (opcion > 0 && opcion <= opciones.size()) {
				return opcion;
			}

			System.out.println("Opción invalida, debe ser un número entre 1 y " + opciones.size());
		}
	}

	public static String entrada(String pregunta) {
		System.out.print(pregunta);

		return scanner.nextLine().strip();
	}

	public static int entradaNumerica(String pregunta) {
		while (true) {
			String input = Consola.entrada(pregunta);

			try {
				int valor = Integer.parseInt(input.strip());
				
				return valor;
			} catch (Exception e) {
			}

			System.out.println("Opción invalida, debe ser un número");
		}
	}

}
