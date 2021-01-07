package application_serveur;

import java.util.TimerTask;

/**
 * Débannit un abonné après le temps choisi
 *
 * @see TimerTask
 * @see Abonne
 */
public class TimerDeban extends TimerTask {
	private Abonne abonne;

	public TimerDeban(Abonne ab) {
		this.abonne = ab;
	}

	@Override
	public void run() {
		this.abonne.debannir();
	}

}
