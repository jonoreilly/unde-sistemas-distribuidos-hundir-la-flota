package hundirlaflota;

/**
 * @author Jon Oreilly del Cerro joreilly1@alumno.uned.es
 */

public class Disparo extends Celda implements IDisparo {

	private static final long serialVersionUID = -4890061265949559862L;

	public Disparo(EFila fila, EColumna columna) {
		super(fila, columna);
	}

	public Disparo(ICelda celda) {
		super(celda);
	}
	
	public Disparo(IDisparo disparo) {
		super(disparo);
	}

}