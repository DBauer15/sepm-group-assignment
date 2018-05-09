package at.ac.tuwien.sepm.assignment.groupphase.application.util;

import at.ac.tuwien.sepm.assignment.groupphase.application.service.ServiceInvokationContext;

public interface Validator <T> {

    public boolean validateForCreation(T dto, ServiceInvokationContext context);
    public boolean validateForUpdate(T dto, ServiceInvokationContext context);
}
