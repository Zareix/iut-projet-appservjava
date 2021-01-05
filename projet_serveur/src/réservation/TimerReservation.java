package réservation;

import java.util.TimerTask;

import application_serveur.Documents;

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
