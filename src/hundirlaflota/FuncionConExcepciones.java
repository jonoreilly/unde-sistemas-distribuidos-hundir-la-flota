package hundirlaflota;

/**
 * @author Jon Oreilly del Cerro joreilly1@alumno.uned.es
 */

public interface FuncionConExcepciones<T, R, E extends Throwable> {
	public R apply(T t) throws E;
}
