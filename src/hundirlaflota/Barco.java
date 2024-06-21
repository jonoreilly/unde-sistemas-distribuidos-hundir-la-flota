package hundirlaflota;

/**
 * @author Jon Oreilly del Cerro joreilly1@alumno.uned.es
 */

public class Barco extends Celda implements IBarco {
	
	private static final long serialVersionUID = 5435012742499457962L;
	
	private EOrientacion orientacion;
	
	public Barco(EFila fila, EColumna columna, EOrientacion orientacion) {
		super(fila, columna);
		this.orientacion = orientacion;
	}

	public Barco(ICelda celda, EOrientacion orientacion) {
		super(celda);
		this.orientacion = orientacion;
	}
	
	public Barco(IBarco barco) {
		this(barco.getFila(), barco.getColumna(), barco.getOrientacion());
	}
	
	public EOrientacion getOrientacion() {
		return this.orientacion;
	}

	public String toString() {
		String celda = super.toString();
		String orientacion = this.orientacion.toString();
		return celda + "-" + orientacion;
	}

}
