package application_serveur;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import abonne.Abonne;

/**
 * Différents outils utilisés par les services d'emprunt, retour et réservation.
 * <br>
 * Permet de factoriser le code des services et d'éviter les redondances.
 */
public class ServiceTools {

	/**
	 * Permet à l'utilisateur de se connecter et renvoie l'abonné qui vient de se
	 * connecter
	 * 
	 * @param socketIn  : le socket de lecture
	 * @param socketOut : le socket d'ecriture
	 * @param abonnes   : la liste des abonnés
	 * @return : l'abonné connecté
	 * @throws IOException
	 */
	public static Abonne connexion(BufferedReader socketIn, PrintWriter socketOut, List<Abonne> abonnes)
			throws IOException {
		while (true) {
			String s = socketIn.readLine();
			try {
				int numAbo = Integer.parseInt(s);
				for (Abonne abonne : abonnes) {
					if (abonne.getId() == numAbo)
						return abonne;
				}
				socketOut.println("Ce numéro d'abonné n'est pas reconnu");
			} catch (NumberFormatException e) {
				socketOut.println("Merci d'entrer un numéro valide");
			}
		}
	}

	/**
	 * Envoie dans le socket la liste des documents
	 * 
	 * @param socketOut : le socket d'écriture
	 * @param documents : la liste des documents
	 */
	public static void affichageDocs(PrintWriter socketOut, List<Document> documents) {
		socketOut.println("Voici la liste des documents :");
		String s = "";
		for (Document doc : documents)
			s += "  - " + doc + "\n";
		s += "finliste";
		socketOut.println(s);
	}
}
