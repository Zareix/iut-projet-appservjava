package documents;

import java.util.Timer;

import application_serveur.Abonne;
import application_serveur.Documents;
import exception.ReservationException;
import réservation.TimerReservation;
import exception.EmpruntException;

public class DVD implements Documents {
	private static final int AGE_ADULTE = 16;
	private int numero;
	private String titre;
	private boolean adulte;

	private Abonne abonne;
	private boolean isReserve = false;
	private boolean isEmprunte = false;

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
	public void reservationPour(final Abonne ab) throws ReservationException {
		synchronized (this) {
			if (adulte)
				if (ab.getAge() < AGE_ADULTE)
					throw new ReservationException("Vous n'avez pas l'age requis pour réserver ce DVD");
			if (this.isReserve)
				throw new ReservationException("Ce document est déjà réservé");
			if (this.isEmprunte)
				throw new ReservationException("Ce document est déjà emprunté");
			this.abonne = ab;
			this.isReserve = true;
			ab.addDocuments(this);
			Timer t = new Timer();
			t.schedule(new TimerReservation(this), 10000);
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
			ab.addDocuments(this);
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
			}
		}
	}

	@Override
	public String toString() {
		return "DVD : " + this.numero + " " + this.titre;
	}

}
