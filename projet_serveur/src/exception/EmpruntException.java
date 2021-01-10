package exception;

/**
 * Exception dans le cas d'une erreur lors de l'emprunt
 * 
 * @see Exception
 */
@SuppressWarnings("serial")
public class EmpruntException extends Exception {
	public EmpruntException(String message) {
		super(message);
	}
}
