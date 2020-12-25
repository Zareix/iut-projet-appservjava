package retour;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import application_serveur.Abonne;
import application_serveur.Documents;

public class ServiceRetour implements Runnable {
	static List<Abonne> abonnes = new ArrayList<>();
	static List<Documents> documents = new ArrayList<>();

	private Socket client;

	public ServiceRetour(Socket s) {
		this.client = s;
	}

	@Override
	public void run() {
		try {
			BufferedReader socketIn = new BufferedReader(new InputStreamReader(client.getInputStream()));
			PrintWriter socketOut = new PrintWriter(client.getOutputStream(), true);

			socketOut.println("Bienvenue sur le service de retour.\nMerci de renseigner votre numéro de client");

			Abonne ab = null;

			while (true) {
				String s = socketIn.readLine();
				int numAbo = -1;
				if (s.matches("-?\\d+")) {
					numAbo = (int) Integer.valueOf(s);
					for (Abonne abonne : abonnes) {
						if (abonne.getId() == numAbo) {
							ab = abonne;
							break;
						}
					}
				}
				if (ab == null)
					socketOut.println("Ce numéro d'abonné n'est pas reconnu");
				else
					break;
			}

			socketOut.println("Bienvenue " + ab.getNom() + "\nVoici la liste de vos documents :");

			List<Documents> docAbo = ab.getDocuments();
			for (Documents doc : docAbo) {
				socketOut.println("  - " + doc);
			}
			socketOut.println(
					"Veuillez saisir le numéro du document que vous souhaitez retourner\nTapez \"terminer\" pour mettre fin au service d'emprunt");
			socketOut.println("finListe");

			while (true) {
				String s = socketIn.readLine();
				if (s.equalsIgnoreCase("TERMINER")) {
					try {
						client.close();
					} catch (IOException e2) {
					}
					;
				}
				int numDoc = -1;
				boolean docFound = false;
				if (s.matches("-?\\d+")) {
					numDoc = (int) Integer.valueOf(s);
					for (Documents doc : docAbo) {
						if (doc.numero() == numDoc) {
							docFound = true;
							doc.retour();
							socketOut.println("Document retourné avec succès !");
						}
					}
					if (!docFound)
						socketOut.println("Vous ne possédez pas ce documents");
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

	public static void setAbonnes(List<Abonne> a) {
		ServiceRetour.abonnes = a;

	}

	public static void setDocuments(List<Documents> d) {
		ServiceRetour.documents = d;
	}

}
