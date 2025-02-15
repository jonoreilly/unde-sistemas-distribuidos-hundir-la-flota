package hundirlaflota.jugador_servidor;

/**
 * @author Jon Oreilly del Cerro joreilly1@alumno.uned.es
 */

public enum CallbackJugadorMensajeEnum {
	PARTIDA_CREADA,
	CONTRINCANTE_UNIDO_COLOCAR_BARCOS,
	COMIENZA_EL_JUEGO_TU_TURNO,
	COMIENZA_EL_JUEGO_TURNO_CONTRINCANTE,
	DISPARO_CONTRINCANTE_AGUA,
	DISPARO_CONTRINCANTE_TOCADO,
	DISPARO_TUYO_AGUA,
	DISPARO_TUYO_TOCADO,
	HAS_HUNDIDO_UN_BARCO,
	TE_HAN_HUNDIDO_UN_BARCO,
	VICTORIA,
	DERROTA,
	CONTRINCANTE_CAPITULA,
	NUEVO_INICIO_DE_SESION,
	CONTRINCANTE_DESCONECTADO
}
