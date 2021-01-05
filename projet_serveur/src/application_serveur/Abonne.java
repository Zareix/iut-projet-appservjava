package application_serveur;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

/**
 * Un abonné à la médiathèque
 */
public class Abonne {
	private int id;
	private LocalDate dateNaissance;
	private String nom;

	private List<Documents> docsEmpruntes;

	public Abonne(int id, String n, LocalDate dateN) {
		this.id = id;
		this.nom = n;
		this.dateNaissance = dateN;

		this.docsEmpruntes = new ArrayList<>();
	}

	/**
	 * Retourne l'ID de l'abonné
	 * 
	 * @return l'ID de l'abonné
	 */
	public int getId() {
		return id;
	}

	/**
	 * Calcule et retourne l'age de l'abonne
	 * 
	 * @return l'age de l'abonné
	 */
	public int getAge() {
		return Period.between(this.dateNaissance, LocalDate.now()).getYears();
	}

	/**
	 * Retourne le nom de l'abonne
	 * 
	 * @return le nom de l'abonné
	 */
	public String getNom() {
		return nom;
	}

	/**
	 * Ajoute un document à la liste {@link #docsEmpruntes}
	 * 
	 * @param d : le document
	 */
	public void addDocuments(Documents d) {
		this.docsEmpruntes.add(d);
	}

	/**
	 * Retire un document de la liste {@link #docsEmpruntes}
	 * 
	 * @param d : le document
	 */
	public void retirerDocuments(Documents d) {
		docsEmpruntes.remove(d);
	}

	/**
	 * Retourne une copie de la liste {@link #docsEmpruntes}
	 * 
	 * @return la liste des documents empruntés par l'abonné
	 */
	public List<Documents> getDocuments() {
		return new ArrayList<>(docsEmpruntes);
	}

}
