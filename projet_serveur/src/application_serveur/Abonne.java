package application_serveur;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;


public class Abonne {
	private int id;
	private LocalDate dateNaissance;
	private String nom;
	
	private List<Documents> documents;
	
	public Abonne(int id, String n, LocalDate dateN) {
		this.id = id;
		this.dateNaissance = dateN;
		this.nom = n;
		
		this.documents = new ArrayList<>();
	}


	public int getId() {
		return id;
	}


	public int getAge() {
		return Period.between(this.dateNaissance, LocalDate.now()).getYears();
	}


	public String getNom() {
		return nom;
	}
	
	public void addDocuments(Documents d) {
		this.documents.add(d);
	}


	public void retirerDocuments(Documents d) {
		documents.remove(d);
	}
	
	public List<Documents> getDocuments() {
		return new ArrayList<>(documents);
	}
	
}
