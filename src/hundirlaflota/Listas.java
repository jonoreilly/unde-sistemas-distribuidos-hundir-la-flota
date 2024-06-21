package hundirlaflota;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jon Oreilly del Cerro joreilly1@alumno.uned.es
 */

public final class Listas {

	public static <T1, T2, E extends Throwable> List<T2> transformarLista(List<T1> lista,
			FuncionConExcepciones<T1, T2, E> generador) throws E {
		List<T2> nuevaLista = new ArrayList<>();

		if (lista != null) {
			for (T1 objeto : lista) {
				nuevaLista.add(generador.apply(objeto));
			}
		}

		return nuevaLista;
	}

	public static <T, E extends Throwable> List<T> clonarLista(List<T> lista, FuncionConExcepciones<T, T, E> generador)
			throws E {
		return Listas.transformarLista(lista, generador);
	}

	public static <T> T ultimo(List<T> lista) {
		if (lista == null || lista.size() == 0) {
			return null;
		}

		return lista.get(lista.size() - 1);
	}

	public static <T, E extends Throwable> T buscar(List<T> lista, FuncionConExcepciones<T, Boolean, E> comparador)
			throws E {
		if (lista != null) {
			for (T objeto : lista) {
				if (comparador.apply(objeto)) {
					return objeto;
				}
			}	
		}

		return null;
	}

	public static <T, E extends Throwable> Integer buscarIndice(List<T> lista,
			FuncionConExcepciones<T, Boolean, E> comparador) throws E {
		if (lista != null) {
			for (int i = 0; i < lista.size(); i++) {
				if (comparador.apply(lista.get(i))) {
					return i;
				}
			}
		}

		return null;
	}

	public static <T, E extends Throwable> List<T> filtrarLista(List<T> lista,
			FuncionConExcepciones<T, Boolean, E> comparador) throws E {
		List<T> nuevaLista = new ArrayList<>();

		if (lista != null) {
			for (T objeto : lista) {
				if (comparador.apply(objeto)) {
					nuevaLista.add(objeto);
				}
			}
		}

		return nuevaLista;
	}

	public static <T, E extends Throwable> boolean contiene(List<T> lista,
			FuncionConExcepciones<T, Boolean, E> comparador) throws E {
		if (lista != null) {
			for (T objeto : lista) {
				if (comparador.apply(objeto)) {
					return true;
				}
			}
		}

		return false;
	}

}
