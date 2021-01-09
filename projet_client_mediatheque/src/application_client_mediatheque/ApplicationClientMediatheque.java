package application_client_mediatheque;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * Gère la connexion et communication avec le service d'emprunt et de retour
 */
public class ApplicationClientMediatheque {
	private final static String HOST = "localhost";

	private final static int PORT_RESERV = 3000;
	private final static int PORT_EMPRUNT = 4000;
	private final static int PORT_RETOUR = 5000;

	/**
	 * Note : Après tests, il semblerait qu'avoir plusieurs scanner utilisant le
	 * System.in peut poser problème lorsqu'on .close() l'un d'eux, d'où l'attribut
	 * static.
	 */
	private final static Scanner sc = new Scanner(System.in);

	public static void main(String[] args) throws UnknownHostException, IOException {
		System.out.println("Bonjour, bienvenue sur l'application de la médiathèque !");

		while (true) {
			System.out.println(
					"Que souhaitez vous faire ?\nEmprunter, retourner ou reserver un document ? (Entrer [quitter] pour quitter l'app)");
			String choix = sc.nextLine();
			if (choix.equalsIgnoreCase("emprunter") || choix.equalsIgnoreCase("emprunt")) {
				System.out.println("Connexion au service d'emprunt.");
				lancerService(PORT_EMPRUNT, true);
			} else if (choix.equalsIgnoreCase("retourner") || choix.equalsIgnoreCase("retour")) {
				System.out.println("Connexion au service de retour.");
				lancerService(PORT_RETOUR, false);
			} else if (choix.equalsIgnoreCase("reserver") || choix.equalsIgnoreCase("reservation")) {
				System.out.println("Connexion au service de réservation.");
				lancerService(PORT_RESERV, true);
			} else if (choix.equalsIgnoreCase("quitter")) {
				break;
			} else {
				System.out.println("\"" + choix + "\"" + " n'est pas un choix valide");
			}
		}
		sc.close();
		System.out.println("_________________\n");
		System.out.println("A bientot");
	}

	/**
	 * Se connecte à un service (emprunt, retour ou réservation) pour le port choisi
	 * et communique avec celui-ci pour faire l'action demandée
	 * 
	 * @param port           : le port auquel se connecte le socket
	 * @param needConnection : true si l'abo doit se connecter
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	private static void lancerService(int port, boolean needConnection) throws UnknownHostException, IOException {
		System.out.println("_________________\n");

		Socket socket = new Socket(HOST, port);

		BufferedReader socketIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		PrintWriter socketOut = new PrintWriter(socket.getOutputStream(), true);

		System.out.println(socketIn.readLine());

		if (needConnection) {
			// Connexion
			while (true) {
				String s = socketIn.readLine();
				System.out.println(s);

				if (s.matches("([Bienvenue].*)"))
					break;

				socketOut.println(sc.nextLine());
			}
		}

		System.out.println("_________________\n");

		// Affichage liste des documents
		while (true) {
			String s = socketIn.readLine();
			if (s.matches("([finliste].*)"))
				break;
			else
				System.out.println(s);
		}

		System.out.println(socketIn.readLine());

		// Retour/Emprunt/Reservation d'un document
		while (true) {
			String s = sc.nextLine();
			socketOut.println(s);

			System.out.println(socketIn.readLine());

			if (s.equalsIgnoreCase("terminer"))
				break;
		}

		System.out.println("_________________\n");

		socket.close();
		try {
			if (socket != null)
				socket.close();
		} catch (IOException e1) {
		}
	}
}
