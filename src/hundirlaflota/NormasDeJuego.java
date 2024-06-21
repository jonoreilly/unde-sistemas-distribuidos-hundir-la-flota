package hundirlaflota;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Jon Oreilly del Cerro joreilly1@alumno.uned.es
 */

public final class NormasDeJuego {
	
	public static final int LONGITUD_BARCO = 3;
	
	public static final int NUMERO_BARCOS = 2;
	
	public static final int PUNTOS_POR_TOCADO = 1;
	
	public static final int PUNTOS_POR_VICTORIA = 4;

	public static final List<EFila> filas = Arrays.asList(EFila.values());
	
	public static final List<EColumna> columnas = Arrays.asList(EColumna.values());
	
	public static final List<EOrientacion> orientaciones = Arrays.asList(EOrientacion.values());
	
	// Coinciden celdas
	
	public static <T1 extends ICelda, T2 extends ICelda> boolean coinciden(T1 celda1, T2 celda2) {
		if (celda1 == null || celda2 == null) {
			return false;
		}
		
		return celda1.getFila() == celda2.getFila() && 
				celda1.getColumna() == celda2.getColumna();
	}
	
	public static <T1 extends ICelda, T2 extends ICelda> boolean coincideAlMenosUno(T1 celda1, List<T2> celdas2) {
		for (ICelda celda2 : celdas2) {
			boolean coinciden = NormasDeJuego.coinciden(celda1, celda2);
			
			if (coinciden) {
				return true;
			}
		}
		
		return false;
	}

	public static <T1 extends ICelda, T2 extends ICelda> boolean coincideAlMenosUno(List<T1> celdas1, List<T2> celdas2) {
		for (ICelda celda1 : celdas1) {
			boolean coincideAlMenosUno = NormasDeJuego.coincideAlMenosUno(celda1, celdas2);
			
			if (coincideAlMenosUno) {
				return true;
			}
		}
		
		return false;
	}
	
	// Barcos
	
	public static boolean cabeEnElTablero(IBarco barco) {
		if (barco.getOrientacion() == EOrientacion.HORIZONTAL) {
			for (int i = NormasDeJuego.columnas.size() - (NormasDeJuego.LONGITUD_BARCO - 1); i < NormasDeJuego.columnas.size(); i++) {
				if (barco.getColumna() == NormasDeJuego.columnas.get(i)) {
					return false;
				}
			}
		} else {
			for (int i = NormasDeJuego.filas.size() - (NormasDeJuego.LONGITUD_BARCO - 1); i < NormasDeJuego.filas.size(); i++) {
				if (barco.getFila() == NormasDeJuego.filas.get(i)) {
					return false;
				}
			}
		}
			
		return true;
	}

	public static List<ICelda> getCeldasBarco(IBarco barco) {

		List<ICelda> celdasBarco = new ArrayList<>();
		
		for (int i = 0; i < NormasDeJuego.LONGITUD_BARCO; i++) {
			int idFila = NormasDeJuego.filas.indexOf(barco.getFila());
			int idColumna = NormasDeJuego.columnas.indexOf(barco.getColumna());
			
			if (barco.getOrientacion() == EOrientacion.HORIZONTAL) {
				idColumna += i;
			} else {
				idFila += i;
			}
			
			EFila filaCelda = NormasDeJuego.filas.get(idFila);
			EColumna columnaCelda = NormasDeJuego.columnas.get(idColumna);
			
			ICelda celda = new Celda(filaCelda, columnaCelda);
			
			celdasBarco.add(celda);
		}
		
		return celdasBarco;
		
	}

	public static boolean seSolapan(IBarco barco1, IBarco barco2) {
		
		List<ICelda> celdasBarco1 = NormasDeJuego.getCeldasBarco(barco1);

		List<ICelda> celdasBarco2 = NormasDeJuego.getCeldasBarco(barco2);
		
		return NormasDeJuego.coincideAlMenosUno(celdasBarco1, celdasBarco2);
	}
	
	public static <T extends ICelda> boolean tocaBarco(T celda, IBarco barco) {

		List<ICelda> celdasBarco = NormasDeJuego.getCeldasBarco(barco);
		
		return NormasDeJuego.coincideAlMenosUno(celda, celdasBarco);
	}

	public static <T extends ICelda> boolean tocaBarco(T celda, List<IBarco> barcos) {

		for (IBarco barco : barcos) {
			if (NormasDeJuego.tocaBarco(celda, barco)) {
				return true;
			}
		}
		
		return false;
	}

	public static <T extends ICelda> boolean estaHundido(IBarco barco, List<T> disparos) {
		for (ICelda celda : NormasDeJuego.getCeldasBarco(barco)) {
			if (!NormasDeJuego.coincideAlMenosUno(celda, disparos)) {
				return false;
			}
		}
		
		return true;
	}
	
	public static <T extends ICelda> boolean estanTodosHundidos(List<IBarco> barcos, List<T> disparos) {
		for (IBarco barco : barcos) {
			if (!NormasDeJuego.estaHundido(barco, disparos)) {
				return false;
			}
		}
		
		return true;
	}
	
}
