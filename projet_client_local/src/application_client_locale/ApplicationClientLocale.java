package application_client_locale;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ApplicationClientLocale {
	private final static String HOST = "localhost";
	private final static int PORT_RESERVATION = 3000;

	public static void main(String[] args) throws UnknownHostException, IOException {
		Socket socket = new Socket(HOST, PORT_RESERVATION);

		BufferedReader socketIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		PrintWriter socketOut = new PrintWriter(socket.getOutputStream(), true);

		Scanner sc = new Scanner(System.in);

		System.out.println(socketIn.readLine());

		// Entrer le numéro d'abonné
		while (true) {
			String s = socketIn.readLine();
			System.out.println(s);

			if (s.matches("([Bienvenue].*)"))
				break;

			socketOut.println(sc.nextLine());
		}

		// Affichage liste de docs
		while (true) {
			String s = socketIn.readLine();
			if(s.matches("([finliste].*)"))
				break;
			else System.out.println(s);
		}

		// Réservation d'un livre
		while (true) {
			String s = sc.nextLine();
			socketOut.println(s);

			System.out.println(socketIn.readLine());

			if (s.equalsIgnoreCase("terminer"))
				break;
		}

		sc.close();
		socket.close();
	}
}
