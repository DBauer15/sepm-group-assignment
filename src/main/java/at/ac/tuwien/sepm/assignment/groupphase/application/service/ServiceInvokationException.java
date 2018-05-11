package at.ac.tuwien.sepm.assignment.groupphase.application.service;

/**
 * ServiceInvokationException
 * @author e01529136
 *
 */
public class ServiceInvokationException extends Exception {

	private static final long serialVersionUID = -983319879804160537L;
	private ServiceInvokationContext context;

	/**
	 * Constructor
	 * @param context {@link ServiceInvokationContext}
	 */
	public ServiceInvokationException(ServiceInvokationContext context) {
		this.context = context;
	}

	/**
	 * Constructor
	 * @param errorMessage {@link String}
	 */
	public ServiceInvokationException(String errorMessage) {
		context = new ServiceInvokationContext();
		context.addError(errorMessage);
	}

    public ServiceInvokationException(Throwable cause) {
        this(cause.getMessage());
    }

	/**
	 * Get the service invocation context from the exception
	 * @return {@link ServiceInvokationContext}
	 */
	public ServiceInvokationContext getContext() {
		return context;
	}

    public ServiceInvokationException(Throwable cause) {
        this(cause.getMessage());
    }
}
