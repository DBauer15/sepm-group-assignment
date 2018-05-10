package at.ac.tuwien.sepm.assignment.groupphase.application.persistence;

/**
 * Persistence Exception
 * @author e01529136
 *
 */
public class NoEntryFoundException extends Exception {

    private static final long serialVersionUID = -5201167577803974078L;

    public NoEntryFoundException(String message) {
		super(message);
	}

}
