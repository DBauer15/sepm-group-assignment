package at.ac.tuwien.sepm.assignment.groupphase.application.util;

import at.ac.tuwien.sepm.assignment.groupphase.application.service.ServiceInvokationContext;

public interface Validator <T> {

    /**
     * Validates a given DTO for creation purposes
     * @param dto Data Transfer Object to be validated
     * @param context {@link ServiceInvokationContext}
     * @return Boolean indicating if the given DTO is valid
     */
    public boolean validateForCreation(T dto, ServiceInvokationContext context);

    /**
     * Validates a given DTO for update purposes
     * @param dto Data Transfer Object to be validated
     * @param context {@link ServiceInvokationContext}
     * @return Boolean indicating if the given DTO is valid
     */
    public boolean validateForUpdate(T dto, ServiceInvokationContext context);
}
