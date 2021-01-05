package réservation;

import java.util.TimerTask;

import application_serveur.Documents;

/**
 * Effectue le retour du document {@link #doc} si le lapse de temps est dépassé
 * 
 * @see TimerTask
 */
public class TimerReservation extends TimerTask {
	Documents doc;

	public TimerReservation(Documents d) {
		this.doc = d;
	}

	@Override
	public void run() {
		doc.retour();
	}
}
