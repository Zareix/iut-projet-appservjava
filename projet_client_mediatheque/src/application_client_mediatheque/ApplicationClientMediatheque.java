package application_client_mediatheque;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ApplicationClientMediatheque {
	private final static String HOST = "localhost";

	private final static int PORT_EMPRUNT = 4000;
	private final static int PORT_RETOUR = 5000;

	public static void main(String[] args) throws UnknownHostException, IOException {
		System.out.println("Bonjour, que souhaitez vous faire ?");
		System.out.println("Emprunter ou retourner un document ?");

		while (true) {
			Scanner s = new Scanner(System.in);
			String choix = s.nextLine();
			if (choix.equalsIgnoreCase("emprunter")) {
				emprunter();
				s.close();
				break;
			} else if (choix.equalsIgnoreCase("retourner")) {
				retour();
				s.close();
				break;
			} else {
				System.out.println(choix + " n'est pas un choix valide");
			}
		}
		System.out.println("A bientot");
	}

	public static void emprunter() throws UnknownHostException, IOException {
		System.out.println("Vous avez choisi d'emprunter un documents.");

		Socket socket = new Socket(HOST, PORT_EMPRUNT);

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

		// Affichage liste de docs dispo
		while (true) {
			String s = socketIn.readLine();
			if (s.equals("finListe"))
				break;
			System.out.println(s);
		}

		// Emprunt d'un livre
		while (true) {
			String s = sc.nextLine();
			socketOut.println(s);

			if(s.equalsIgnoreCase("terminer"))
				break;
			
			System.out.println(socketIn.readLine());
		}
		
		sc.close();
		socket.close();
		System.out.println("Merci d'avoir utiliser le service d'emprunt");
	}

	public static void retour() {
		System.out.println("Vous avez choisi d'emprunter un documents.");
	}
}
