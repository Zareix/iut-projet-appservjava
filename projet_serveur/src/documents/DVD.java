package documents;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Timer;

import application_serveur.Abonne;
import application_serveur.Documents;
import exception.ReservationException;
import réservation.TimerReservation;
import exception.EmpruntException;

public class DVD implements Documents {
	private static final long DUREE_RESERV = 2; //en heures
	private static final int AGE_ADULTE = 16;
	private int numero;
	private String titre;
	private boolean adulte;

	private Abonne abonne;
	private boolean isEmprunte = false;
	private boolean isReserve = false;
	private Timer t;
	private LocalDateTime dateReserv;

	public DVD(int num, String t, boolean a) {
		this.numero = num;
		this.titre = t;
		this.adulte = a;
	}

	@Override
	public int numero() {
		return this.numero;
	}

	@Override
	public void reservationPour(Abonne ab) throws ReservationException {
		synchronized (this) {
			if (adulte)
				if (ab.getAge() < AGE_ADULTE)
					throw new ReservationException("Vous n'avez pas l'age requis pour réserver ce DVD");
			if (this.isReserve) {
				long minutes = ChronoUnit.MINUTES.between(LocalDateTime.now(), this.dateReserv.plusHours(2));
				throw new ReservationException("Ce document est déjà réservé pendant encore : " + minutes + " minutes");
			}

			if (this.isEmprunte)
				throw new ReservationException("Ce document est déjà emprunté");
			this.abonne = ab;
			this.isReserve = true;
			this.t = new Timer();
			this.t.schedule(new TimerReservation(this), DUREE_RESERV*60*60*1000); // 2h
			this.dateReserv = LocalDateTime.now();
		}
	}

	@Override
	public void empruntPar(Abonne ab) throws EmpruntException {
		synchronized (this) {
			if (adulte)
				if (ab.getAge() < AGE_ADULTE)
					throw new EmpruntException("Vous n'avez pas l'age requis pour emprunter ce DVD");
			if (this.isReserve && ab != this.abonne)
				throw new EmpruntException("Ce document est déjà réservé");
			if (this.isEmprunte)
				throw new EmpruntException("Ce document est déjà emprunté");
			this.abonne = ab;
			this.isEmprunte = true;
			this.isReserve = false;
			ab.addDocuments(this);
			this.t.cancel();
			this.dateReserv = null;
		}
	}

	@Override
	public void retour() {
		synchronized (this) {
			if (this.abonne != null) {
				this.abonne.retirerDocuments(this);
				this.abonne = null;
				this.isReserve = false;
				this.isEmprunte = false;
				this.t.cancel();
				this.dateReserv = null;
			}
		}
	}

	@Override
	public String toString() {
		return "DVD : " + this.numero + " " + this.titre;
	}

}
