package at.ac.tuwien.sepm.assignment.groupphase.application.persistence;

/**
 * Persistence Exception
 * @author e01529136
 *
 */
public class PersistenceException extends Exception {
	private static final long serialVersionUID = -5201167670507374078L;

    public PersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public PersistenceException(String message) {
        super(message);
    }
}