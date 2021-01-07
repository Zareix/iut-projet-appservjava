package application_serveur;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

/**
 * Un abonné à la médiathèque
 */
public class Abonne {
	private static final long DUREE_BAN = 1; // en mois

	private int id;
	private LocalDate dateNaissance;
	private String nom;

	@Deprecated
	private List<Documents> docsEmpruntes;

	private LocalDate finBan;

	private Timer tDeban;

	public Abonne(int id, String n, LocalDate dateN) {
		this.id = id;
		this.nom = n;
		this.dateNaissance = dateN;

		this.docsEmpruntes = new ArrayList<>();
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
	 * Ajoute un document à la liste {@link #docsEmpruntes}
	 * 
	 * @param d : le document
	 */
	@Deprecated
	public void addDocuments(Documents d) {
		this.docsEmpruntes.add(d);
	}

	/**
	 * Retire un document de la liste {@link #docsEmpruntes}
	 * 
	 * @param d : le document
	 */
	@Deprecated
	public void retirerDocuments(Documents d) {
		docsEmpruntes.remove(d);
	}

	/**
	 * Retourne une copie de la liste {@link #docsEmpruntes}
	 * 
	 * @return la liste des documents empruntés par l'abonné
	 */
	@Deprecated
	public List<Documents> getDocuments() {
		return new ArrayList<>(docsEmpruntes);
	}

	/**
	 * Bannit l'abonné (l'empéchant d'effectuer des emprunts et réservation)
	 * 
	 */
	public void bannir() {
		this.finBan = LocalDate.now().plusMonths(DUREE_BAN);
		this.tDeban = new Timer();
		tDeban.schedule(new TimerDeban(this), DUREE_BAN * 60 * 60 * 24 * 7 * 30); // TODO : move ?
	}

	/**
	 * Débannit l'abonné
	 * 
	 */
	public void debannir() {
		this.finBan = null;
		if(this.tDeban != null)
			this.tDeban.cancel();
	}

	/**
	 * Indique si l'abonné est banni
	 * 
	 * @return l'état du bannissement
	 */
	public boolean isBanni() {
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
