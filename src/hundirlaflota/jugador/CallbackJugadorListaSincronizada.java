package hundirlaflota.jugador;

import java.util.ArrayList;
import java.util.List;

import hundirlaflota.jugador_servidor.CallbackJugadorMensajeEnum;

/**
 * @author Jon Oreilly del Cerro joreilly1@alumno.uned.es
 */

public class CallbackJugadorListaSincronizada {

	private List<CallbackJugadorMensajeEnum> mensajes = new ArrayList<>();
	
	public synchronized void enviarMensaje(CallbackJugadorMensajeEnum mensaje) {
		
		this.mensajes.add(mensaje);
		
		this.notifyAll();
		
	}
	
	public synchronized CallbackJugadorMensajeEnum recibirMensaje() throws InterruptedException {
		
		while (mensajes.size() == 0) {
			this.wait();
		}
		
		CallbackJugadorMensajeEnum mensaje = mensajes.get(0);
		
		mensajes.remove(0);
		
		return mensaje;
		
	}
}
