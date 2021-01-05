package emprunt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import application_serveur.Abonne;
import application_serveur.Documents;
import exception.EmpruntException;

/**
 * Gère l'emprunt d'un document par un abonné
 */
public class ServiceEmprunt implements Runnable {
	static List<Abonne> abonnes = new ArrayList<>();
	static List<Documents> documents = new ArrayList<>();

	private Socket client;

	public ServiceEmprunt(Socket s) {
		this.client = s;
	}

	@Override
	public void run() {
		try {
			BufferedReader socketIn = new BufferedReader(new InputStreamReader(client.getInputStream()));
			PrintWriter socketOut = new PrintWriter(client.getOutputStream(), true);

			socketOut.println("Connecté !\nMerci de renseigner votre numéro de client");

			Abonne ab = null;

			// Connexion de l'abonné avec son numéro
			while (true) {
				String s = socketIn.readLine();
				int numAbo = -1;
				if (!s.matches("-?\\d+")) {
					socketOut.println("Merci d'entrer un numéro valide");
				} else {
					numAbo = (int) Integer.valueOf(s);
					for (Abonne abonne : abonnes) {
						if (abonne.getId() == numAbo) {
							ab = abonne;
							break;
						}
					}
					if (ab == null)
						socketOut.println("Ce numéro d'abonné n'est pas reconnu");
					else
						break;
				}
			}

			socketOut.println("Bienvenue " + ab.getNom() + "\nVoici la liste des documents disponibles :");

			// Affichage de tout les documents
			for (Documents doc : documents) {
				socketOut.println("  - " + doc);
			}
			socketOut.println(
					"Veuillez saisir le numéro du document que vous souhaitez emprunter\nTapez \"terminer\" pour mettre fin au service d'emprunt");
			socketOut.println("finliste");

			// Emprunt d'un document
			while (true) {
				String s = socketIn.readLine();
				if (s.equalsIgnoreCase("TERMINER")) {
					socketOut.println("Merci d'avoir utiliser le service d'emprunt");
					client.close();
				}
				int numDoc = -1;
				boolean docFound = false;
				if (s.matches("-?\\d+")) {
					numDoc = (int) Integer.valueOf(s);
					for (Documents doc : documents) {
						if (doc.numero() == numDoc) {
							docFound = true;
							try {
								doc.empruntPar(ab);
								socketOut.println("Document emprunté avec succès !");
							} catch (EmpruntException e) {
								socketOut.println(e.getMessage());
							}
						}
					}
					if (!docFound)
						socketOut.println("Ce numéro de document n'existe pas");
				} else {
					socketOut.println("Merci de rentrer un numéro valide");
				}
			}

		} catch (IOException e) {
			// Fin service emprunt
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
		ServiceEmprunt.abonnes = a;
	}

	/**
	 * Initialise la liste des documents
	 * 
	 * @param d : liste de documents
	 */
	public static void setDocuments(List<Documents> d) {
		ServiceEmprunt.documents = d;
	}

}
