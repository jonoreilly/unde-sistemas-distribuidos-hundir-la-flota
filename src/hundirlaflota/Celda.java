package hundirlaflota;

/**
 * @author Jon Oreilly del Cerro joreilly1@alumno.uned.es
 */

public class Celda implements ICelda {
	
	private static final long serialVersionUID = 7655434541442082255L;

	private EFila fila;
	
	private EColumna columna;
	
	public Celda(EFila fila, EColumna columna) {
		this.fila = fila;
		this.columna = columna;
	}
	
	public Celda(ICelda celda) {
		this(celda.getFila(), celda.getColumna());
	}
	
	public EFila getFila() {
		return this.fila;
	}
	
	public EColumna getColumna() {
		return this.columna;
	}
	
	public String toString() {
		String fila = this.getFila().toString();
		String columna = this.getColumna().toString().substring(1);
		return fila + "-" + columna;
	}

}