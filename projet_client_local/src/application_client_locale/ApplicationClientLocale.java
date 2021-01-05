package application_client_locale;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * Connecte l'abonné au service de réservation et permet à ce dernier de
 * l'effectuer
 */
public class ApplicationClientLocale {
	private final static String HOST = "localhost";
	private final static int PORT_RESERVATION = 3000;

	public static void main(String[] args) throws UnknownHostException, IOException {
		System.out.println("Connexion au service de réservation\n_________________\n");

		Socket socket = new Socket(HOST, PORT_RESERVATION);

		BufferedReader socketIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		PrintWriter socketOut = new PrintWriter(socket.getOutputStream(), true);

		Scanner sc = new Scanner(System.in);

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

		// Réservation d'un document
		while (true) {
			String s = sc.nextLine();
			socketOut.println(s);

			System.out.println(socketIn.readLine());

			if (s.equalsIgnoreCase("terminer"))
				break;
		}

		sc.close();
		socket.close();
		try {
			if (socket != null)
				socket.close();
		} catch (IOException e1) {
		}
	}
}
