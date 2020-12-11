package application_serveur;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import documents.DVD;
import emprunt.ServeurEmprunt;
import emprunt.ServiceEmprunt;
import retour.ServeurRetour;
import retour.ServiceRetour;
import réservation.ServeurReservation;
import réservation.ServiceReservation;

public class ApplicationServeur {
	private final static int PORT_RESERVATION = 3000;
	private final static int PORT_EMPRUNT = 4000;
	private final static int PORT_RETOUR = 5000;
	
	public static void main(String[] args) throws IOException {
		List<Abonne> abonnes = initAbonnes();
		List<Documents> documents = initDocuments();
		
		ServiceEmprunt.setAbonnes(abonnes);
		ServiceEmprunt.setDocuments(documents);
		ServiceReservation.setAbonnes(abonnes);
		ServiceReservation.setDocuments(documents);
		ServiceRetour.setAbonnes(abonnes);
		ServiceRetour.setDocuments(documents);
		
		
		new Thread(new ServeurReservation(PORT_RESERVATION));
		new Thread(new ServeurEmprunt(PORT_EMPRUNT));
		new Thread(new ServeurRetour(PORT_RETOUR));
	}

	
	/*
	 * Initialise la liste des abonnés
	 */
	public static List<Abonne> initAbonnes() {
		List<Abonne> abonnes = new ArrayList<>();
		abonnes.add(new Abonne(1000, "Amina Abadi", LocalDate.of(2001, Month.OCTOBER, 9)));
		abonnes.add(new Abonne(1001, "Clément Nguyen", LocalDate.of(2007, Month.OCTOBER, 17)));
		abonnes.add(new Abonne(1002, "Vincent Nguyen", LocalDate.of(2001, Month.JULY, 18)));
		abonnes.add(new Abonne(1003, "Raphaël Gonçalves Catarino", LocalDate.of(2001, Month.MAY, 29)));
		return abonnes;
	}

	/*
	 * Initialise la liste des documents
	 */
	private static List<Documents> initDocuments() {
		List<Documents> documents = new ArrayList<>();
		documents.add(new DVD(501, "L'incroyable vie d'Amina", false));
		documents.add(new DVD(502, "Les avm de Vincent", true));
		documents.add(new DVD(503, "Les tricks de longboard de Raphaël", false));
		return documents;
	}
}
