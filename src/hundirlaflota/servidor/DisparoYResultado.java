package hundirlaflota.servidor;

import hundirlaflota.Disparo;
import hundirlaflota.EColumna;
import hundirlaflota.EFila;
import hundirlaflota.ICelda;
import hundirlaflota.IDisparo;
import hundirlaflota.jugador_servidor.EResultadoDisparo;
import hundirlaflota.jugador_servidor.IDisparoYResultado;

/**
 * @author Jon Oreilly del Cerro joreilly1@alumno.uned.es
 */

public class DisparoYResultado extends Disparo implements IDisparoYResultado {

	private static final long serialVersionUID = 3410278544422537002L;
	
	private EResultadoDisparo resultado;
	
	public DisparoYResultado(EFila fila, EColumna columna, EResultadoDisparo resultado) {
		super(fila, columna);
		this.resultado = resultado;
	}
	
	public DisparoYResultado(ICelda celda, EResultadoDisparo resultado) {
		super(celda);
		this.resultado = resultado;
	}

	public DisparoYResultado(IDisparo disparo, EResultadoDisparo resultado) {
		super(disparo);
		this.resultado = resultado;
	}
	
	public DisparoYResultado(IDisparoYResultado disparoYResultado) {
		this(disparoYResultado.getFila(), disparoYResultado.getColumna(), disparoYResultado.getResultadoDisparo());
	}

	public EResultadoDisparo getResultadoDisparo() {
		return this.resultado;
	}
	
	public String toString() {
		String celda = super.toString();
		String resultado = this.resultado.toString();
		return celda + "-" + resultado;
	}
	
}
