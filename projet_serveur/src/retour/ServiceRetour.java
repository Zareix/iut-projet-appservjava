package retour;

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

/**
 * Gère le retour d'un document
 */
public class ServiceRetour implements Runnable {
	static List<Abonne> abonnes = new ArrayList<>();
	static List<Document> documents = new ArrayList<>();

	private Socket client;

	public ServiceRetour(Socket s) {
		this.client = s;
	}

	@Override
	public void run() {
		try {
			BufferedReader socketIn = new BufferedReader(new InputStreamReader(client.getInputStream()));
			PrintWriter socketOut = new PrintWriter(client.getOutputStream(), true);

			socketOut.println("Connecté au service de retour.");

			ServiceTools.affichageDocs(socketOut, documents);

			socketOut.println(
					"Veuillez saisir le numéro du document que vous souhaitez retourner. Tapez \"terminer\" pour mettre fin au service de retour");

			// Retour d'un document
			while (true) {
				String s = socketIn.readLine();
				if (s.equalsIgnoreCase("terminer")) {
					socketOut.println("Merci d'avoir utiliser le service de retour");
					client.close();
				}
				boolean docFound = false;
				if (s.matches("-?\\d+")) {
					int numDoc = Integer.valueOf(s);
					for (Document doc : documents) {
						if (doc.numero() == numDoc) {
							docFound = true;
							doc.retour();
							socketOut.println("Document retourné avec succès !");
						}
					}
					if (!docFound)
						socketOut.println("Ce document n'existe pas");
				} else {
					socketOut.println("Merci de rentrer un numéro valide");
				}
			}
		} catch (IOException e) {
			// Fin service retour
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
		ServiceRetour.abonnes = a;
	}

	/**
	 * Initialise la liste des documents
	 * 
	 * @param d : liste de documents
	 */
	public static void setDocuments(List<Document> d) {
		ServiceRetour.documents = d;
	}

}
