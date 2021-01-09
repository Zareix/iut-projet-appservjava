package documents;

import java.util.TimerTask;

import application_serveur.Document;

/**
 * Effectue le retour du document {@link #doc} si le lapse de temps est dépassé
 * 
 * @see TimerTask
 */
public class TimerTaskReservation extends TimerTask {
	Document doc;

	public TimerTaskReservation(Document d) {
		this.doc = d;
	}

	@Override
	public void run() {
		doc.retour();
	}
}
