package réservation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import abonne.Abonne;
import application_serveur.Document;
import application_serveur.ServiceTools;
import exception.ReservationException;

/**
 * Gère la réservation d'un document par un abonné
 */
public class ServiceReservation implements Runnable {
	static List<Abonne> abonnes = new ArrayList<>();
	static List<Document> documents = new ArrayList<>();

	private Socket client;

	public ServiceReservation(Socket s) {
		this.client = s;
	}

	@Override
	public void run() {
		try {
			BufferedReader socketIn = new BufferedReader(new InputStreamReader(client.getInputStream()));
			PrintWriter socketOut = new PrintWriter(client.getOutputStream(), true);

			socketOut.println("Connecté !\nMerci de renseigner votre numéro de client.");

			Abonne ab = ServiceTools.connexion(socketIn, socketOut, abonnes);

			socketOut.println("Bienvenue " + ab.getNom());

			ServiceTools.affichageDocs(socketOut, documents);

			socketOut.println(
					"Veuillez saisir le numéro du document que vous souhaitez réserver. Tapez \"terminer\" pour mettre fin au service de réservation.");

			// Réservation d'un documents
			while (true) {
				String s = socketIn.readLine();
				if (s.equalsIgnoreCase("terminer")) {
					socketOut.println("Merci d'avoir utiliser le service de réservation.");
					client.close();
				}
				boolean docFound = false;
				try {
					int numDoc = Integer.parseInt(s);
					for (Document doc : documents) {
						if (doc.numero() == numDoc) {
							docFound = true;
							try {
								doc.reservationPour(ab);
								socketOut.println("Document réservé avec succès !");
							} catch (ReservationException e) {
								socketOut.println(e.getMessage());
							}
						}
					}
					if (!docFound)
						socketOut.println("Ce numéro de document n'existe pas.");
				} catch (NumberFormatException e) {
					socketOut.println("Merci de rentrer un numéro valide.");
				}
			}
		} catch (IOException e) {
			// Fin service réservation
		}
	}

	protected void finalize() throws Throwable {
		client.close();
	}

	/**
	 * Initialise la liste des abonnés
	 * 
	 * @param a : liste d'abonnés
	 */
	public static void setAbonnes(List<Abonne> a) {
		ServiceReservation.abonnes = a;
	}

	/**
	 * Initialise la liste des documents
	 * 
	 * @param d : liste de documents
	 */
	public static void setDocuments(List<Document> d) {
		ServiceReservation.documents = d;
	}

}
