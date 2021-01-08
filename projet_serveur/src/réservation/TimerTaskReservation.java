package réservation;

import java.util.TimerTask;

import application_serveur.Documents;

/**
 * Effectue le retour du document {@link #doc} si le lapse de temps est dépassé
 * 
 * @see TimerTask
 */
public class TimerTaskReservation extends TimerTask {
	Documents doc;

	public TimerTaskReservation(Documents d) {
		this.doc = d;
	}

	@Override
	public void run() {
		doc.retour();
	}
}
