package hundirlaflota;

import java.io.Serializable;

/**
 * @author Jon Oreilly del Cerro joreilly1@alumno.uned.es
 */

public interface ICelda extends Serializable {

	public EFila getFila();

	public EColumna getColumna();

}
