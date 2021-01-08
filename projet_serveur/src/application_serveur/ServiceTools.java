package application_serveur;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Différents outils utilisés par les services d'emprunt, retour et réservation
 *
 */
public class ServiceTools {
	public static Abonne connexion(BufferedReader socketIn, PrintWriter socketOut, List<Abonne> abonnes)
			throws IOException {
		while (true) {
			String s = socketIn.readLine();
			int numAbo = -1;
			if (!s.matches("-?\\d+")) {
				socketOut.println("Merci d'entrer un numéro valide");
			} else {
				numAbo = (int) Integer.valueOf(s);
				for (Abonne abonne : abonnes) {
					if (abonne.getId() == numAbo)
						return abonne;
				}
				socketOut.println("Ce numéro d'abonné n'est pas reconnu");
			}
		}
	}

	public static void affichageDocs(PrintWriter socketOut, List<Document> documents) {
		socketOut.println("Voici la liste des documents :");
		String s = "";
		for (Document doc : documents) {
			s += "  - " + doc;
		}
		s += "finliste";
		socketOut.println(s);
	}
}
