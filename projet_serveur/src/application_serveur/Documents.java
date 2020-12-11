package application_serveur;

import exception.EmpruntException;
import exception.ReservationException;

public interface Documents {
	int numero();
	void reservationPour(Abonne ab) throws ReservationException ;
	void empruntPar(Abonne ab) throws EmpruntException;
	
	// retour document ou annulation réservation
	void retour();
}
