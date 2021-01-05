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

	private final static int PORT_EMPRUNT = 4000;
	private final static int PORT_RETOUR = 5000;

	/**
	 * Note : Après tests, il semblerait qu'avoir plusieurs scanner utilisant le System.in
	 * peut poser problème lorsqu'on .close() l'un d'eux, d'où l'attribut static.
	 */
	private final static Scanner sc = new Scanner(System.in);

	public static void main(String[] args) throws UnknownHostException, IOException {
		System.out.println("Bonjour, que souhaitez vous faire ?");
		System.out.println("Emprunter ou retourner un document ?");

		while (true) {
			String choix = sc.nextLine();
			if (choix.equalsIgnoreCase("emprunter") || choix.equalsIgnoreCase("emprunt")) {
				System.out.println("Connexion au service d'emprunt.");
				lancerService(PORT_EMPRUNT);
				break;
			} else if (choix.equalsIgnoreCase("retourner") || choix.equalsIgnoreCase("retour")) {
				System.out.println("Connexion au service de retour.");
				lancerService(PORT_RETOUR);
				break;
			} else {
				System.out.println("\"" + choix + "\"" + " n'est pas un choix valide");
			}
		}
		sc.close();
		System.out.println("A bientot");
	}

	/**
	 * Se connecte à un service (emprunt ou retour) pour le port choisi et
	 * communique avec celui-ci pour faire l'action demandée
	 * 
	 * @param port : le port auquel se connecte le socket
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	private static void lancerService(int port) throws UnknownHostException, IOException {
		System.out.println("_________________\n");

		Socket socket = new Socket(HOST, port);

		BufferedReader socketIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		PrintWriter socketOut = new PrintWriter(socket.getOutputStream(), true);

		System.out.println(socketIn.readLine());

		// Connexion
		while (true) {
			String s = socketIn.readLine();
			System.out.println(s);

			if (s.matches("([Bienvenue].*)"))
				break;

			socketOut.println(sc.nextLine());
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

		// Retour/Emprunt d'un document
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
