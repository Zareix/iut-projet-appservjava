package emprunt;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import application_serveur.Abonne;
import application_serveur.Documents;

public class ServiceEmprunt implements Runnable {
	static List<Abonne> abonnes = new ArrayList<>();
	static List<Documents> documents = new ArrayList<>();
	static 
	
	private Socket client;
	
	public ServiceEmprunt(Socket s) {
		this.client = s;
	}

	@Override
	public void run() {

	}

	public static void setAbonnes(List<Abonne> a) {
		ServiceEmprunt.abonnes = a;
		
	}

	public static void setDocuments(List<Documents> d) {
		ServiceEmprunt.documents = d;
	}

}
