package documents;

import application_serveur.Abonne;
import application_serveur.Documents;
import exception.ReservationException;
import exception.EmpruntException;

public class DVD implements Documents {
	private static final int AGE_ADULTE = 16;
	private int numero;
	private String titre;
	private boolean adulte;
	
	private Abonne abonne;
	
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
		if (adulte)
			if(ab.getAge() < AGE_ADULTE) throw new ReservationException();
		if(this.abonne != null)
			throw new ReservationException();
		
		this.abonne = ab;
		ab.addDocuments(this);
	}

	@Override
	public void empruntPar(Abonne ab) throws EmpruntException {
		if (adulte)
			if(ab.getAge() < AGE_ADULTE) throw new EmpruntException();
		if(this.abonne != null)
			throw new EmpruntException();
		
		this.abonne = ab;
		ab.addDocuments(this);
	}

	@Override
	public void retour() {
		if(this.abonne != null)
			this.abonne.retirerDocuments(this);
	}
	
	@Override
	public String toString() {
		return "DVD : " + this.titre;
	}

}
