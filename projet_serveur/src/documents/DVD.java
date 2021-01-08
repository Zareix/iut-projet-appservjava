package documents;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Timer;

import application_serveur.Abonne;
import application_serveur.Document;
import emprunt.TimerTaskEmprunt;
import exception.ReservationException;
import réservation.TimerTaskReservation;
import exception.EmpruntException;

/**
 * @see Document
 */
public class DVD implements Document {
	private static final long DUREE_RESERV = 2; // en heures
	private static final long DUREE_EMPRUNT = 2; // en semaines
	private static final int AGE_ADULTE = 16;
	private static final double RISQUE_DEGRADATION = 10; // % de risque de dégradation d'un doc au rendu

	private int numero;
	private String titre;
	private boolean adulte;

	private Abonne abonne;

	private LocalDateTime dateFinReserv;
	private Timer tReserv;

	private Timer tEmprunt;

	public DVD(int num, String t, boolean a) {
		this.numero = num;
		this.titre = t;
		this.adulte = a;
	}

	/**
	 * Retourne le numéro du DVD
	 * 
	 * @return le numéro du DVD
	 */
	@Override
	public int numero() {
		return this.numero;
	}

	/**
	 * Permet la réservation du DVD <br>
	 * Si la réservation est impossible : throw une EmpruntException, avec le
	 * message correspondant <br>
	 * Si la réservation est effectuée : lance un timer de {@value #DUREE_RESERV}
	 * heure(s) qui retourne le DVD s'il n'a pas été emprunté dans ce lapse de temps
	 * 
	 * @param ab : l'abonné qui réserve
	 */
	@Override
	public void reservationPour(Abonne ab) throws ReservationException {
		if (ab.isBanni())
			throw new ReservationException("Vous êtes interdit de réservation jusqu'au "
					+ ab.getFinBan().getDayOfMonth() + " " + Month.of(ab.getFinBan().getMonthValue()));
		if (adulte)
			if (ab.getAge() < AGE_ADULTE)
				throw new ReservationException("Vous n'avez pas l'âge requis pour réserver ce DVD");

		synchronized (this) {
			if (this.dateFinReserv != null) {
				if (this.abonne == ab)
					throw new ReservationException("Vous réservé déjà ce DVD jusqu'à : " + this.dateFinReserv.getHour()
							+ "h" + this.dateFinReserv.getMinute());
				throw new ReservationException("Ce DVD est réservé par quelqu'un d'autre, jusqu'à : "
						+ this.dateFinReserv.getHour() + "h" + this.dateFinReserv.getMinute());
			}
			if (this.abonne != null)
				throw new ReservationException("Ce DVD est déjà emprunté");

			// Aucun des précédents donc le DVD est disponible à la réservation
			this.abonne = ab;
			this.tReserv = new Timer();
			this.tReserv.schedule(new TimerTaskReservation(this), DUREE_RESERV * 60 * 60 * 1000); // conversion heures
																									// en ms
			this.dateFinReserv = LocalDateTime.now().plusHours(2);
		}
	}

	/**
	 * Permet l'emprunt du DVD <br>
	 * Si l'emprunt est impossible : throw une EmpruntException, avec le message
	 * correspondant <br>
	 * Si l'emprunt est effectué : lance un timer de {@value #DUREE_EMPRUNT}
	 * semaine(s) au bout duquel l'abonné sera banni ({@link Abonne#bannir()}) s'il
	 * n'a pas encore rendu le DVD
	 * 
	 * @param ab : l'abonné qui emprunte
	 */
	@Override
	public void empruntPar(Abonne ab) throws EmpruntException {
		if (ab.isBanni())
			throw new EmpruntException("Vous êtes interdit d'emprunt jusqu'au " + ab.getFinBan().getDayOfMonth() + " "
					+ Month.of(ab.getFinBan().getMonthValue()));
		if (adulte)
			if (ab.getAge() < AGE_ADULTE)
				throw new EmpruntException("Vous n'avez pas l'age requis pour emprunter ce DVD");

		synchronized (this) {
			if (this.dateFinReserv != null) {
				if (this.abonne != ab)
					throw new EmpruntException("Ce DVD est réservé par quelqu'un d'autre, jusqu'à : "
							+ this.dateFinReserv.getHour() + "h" + this.dateFinReserv.getMinute());
			}
			if (this.abonne != null)
				if (this.abonne != ab)
					throw new EmpruntException("Ce DVD est déjà emprunté");

			// Aucun des précédents donc le DVD est disponible à l'emprunt
			if (this.tReserv != null)
				this.tReserv.cancel();
			this.abonne = ab;
			// ab.addDocuments(this);
			this.tEmprunt = new Timer();
			this.tEmprunt.schedule(new TimerTaskEmprunt(this.abonne), DUREE_EMPRUNT * 1000 * 60 * 60 * 24 * 7);
			this.dateFinReserv = null;
		}
	}

	/**
	 * Permet le retour ou l'annulation de la réservation du DVD <br>
	 * {@value #RISQUE_DEGRADATION}% de risque que le DVD soit rendu dégradé
	 */
	@Override
	public void retour() {
		synchronized (this) {
			if (this.abonne != null) {
				if (this.tReserv != null)
					this.tReserv.cancel();
				if (this.tEmprunt != null)
					this.tEmprunt.cancel();
				// this.abonne.retirerDocuments(this);
				if (this.dateFinReserv == null && this.abonne != null && Math.random() * 100 < RISQUE_DEGRADATION)
					this.abonne.bannir();
				this.abonne = null;
				this.dateFinReserv = null;
			}
		}
	}

	@Override
	public String toString() {
		return "DVD : " + this.numero + " " + this.titre + (this.adulte ? " (Pour adulte)" : "");
	}

}
