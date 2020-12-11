package retour;

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
		// TODO Auto-generated method stub

	}

	public static void setAbonnes(List<Abonne> a) {
		ServiceRetour.abonnes = a;

	}

	public static void setDocuments(List<Documents> d) {
		ServiceRetour.documents = d;
	}

}
