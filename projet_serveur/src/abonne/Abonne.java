package abonne;

import java.time.LocalDate;
import java.time.Period;
import java.util.Timer;

/**
 * Un abonné à la médiathèque
 */
public class Abonne {
	private static final long DUREE_BAN = 1; // en mois

	private int id;
	private LocalDate dateNaissance;
	private String nom;

	private LocalDate finBan;
	private Timer tDeban;

	public Abonne(int id, String n, LocalDate dateN) {
		this.id = id;
		this.nom = n;
		this.dateNaissance = dateN;
	}

	/**
	 * Retourne l'ID de l'abonné
	 * 
	 * @return l'ID de l'abonné
	 */
	public int getId() {
		return id;
	}

	/**
	 * Calcule et retourne l'age de l'abonne
	 * 
	 * @return l'age de l'abonné
	 */
	public int getAge() {
		return Period.between(this.dateNaissance, LocalDate.now()).getYears();
	}

	/**
	 * Retourne le nom de l'abonne
	 * 
	 * @return le nom de l'abonné
	 */
	public String getNom() {
		return nom;
	}

	/**
	 * Bannit l'abonné (l'empéchant d'effectuer des emprunts et réservation) <br>
	 * Remarque : prolonge la durée du bannissement si l'abonné l'est déjà
	 */
	public synchronized void bannir() {
		this.finBan = LocalDate.now().plusMonths(DUREE_BAN);
		this.tDeban = new Timer();
		tDeban.schedule(new TimerTaskDeban(this), DUREE_BAN * 1000 * 60 * 60 * 24 * 7 * 30);
	}

	/**
	 * Débannit l'abonné
	 * 
	 */
	public synchronized void debannir() {
		this.finBan = null;
		if (this.tDeban != null)
			this.tDeban.cancel();
	}

	/**
	 * Indique si l'abonné est banni
	 * 
	 * @return l'état du bannissement
	 */
	public synchronized boolean isBanni() {
		return this.finBan != null;
	}

	/**
	 * Renvoie la date à laquelle l'abonne ne sera plus banni
	 * 
	 * @return la date de fin du bannissement
	 */
	public LocalDate getFinBan() {
		return this.finBan;
	}

}
