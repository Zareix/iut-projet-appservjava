package réservation;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import application_serveur.Abonne;
import application_serveur.Documents;

public class ServiceReservation implements Runnable {
	static List<Abonne> abonnes = new ArrayList<>();
	static List<Documents> documents = new ArrayList<>();
	
	private Socket client;

	public ServiceReservation(Socket s) {
		this.client = s;
	}
	
	@Override
	public void run() {
		
	}

	public static void setAbonnes(List<Abonne> a) {
		ServiceReservation.abonnes = a;

	}

	public static void setDocuments(List<Documents> d) {
		ServiceReservation.documents = d;
	}

}
