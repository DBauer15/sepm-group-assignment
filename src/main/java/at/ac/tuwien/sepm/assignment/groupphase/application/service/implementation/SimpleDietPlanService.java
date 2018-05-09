package at.ac.tuwien.sepm.assignment.groupphase.application.service.implementation;

import java.lang.invoke.MethodHandles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import at.ac.tuwien.sepm.assignment.groupphase.application.dto.DietPlan;
import at.ac.tuwien.sepm.assignment.groupphase.application.persistence.DietPlanPersistence;
import at.ac.tuwien.sepm.assignment.groupphase.application.persistence.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.application.service.DietPlanService;
import at.ac.tuwien.sepm.assignment.groupphase.application.service.ServiceInvokationContext;
import at.ac.tuwien.sepm.assignment.groupphase.application.service.ServiceInvokationException;
import at.ac.tuwien.sepm.assignment.groupphase.application.util.implementation.ValidationUtil;

@Service
public class SimpleDietPlanService implements DietPlanService {

	private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	private DietPlanPersistence dietPlanRepository;

	/**
	 * Constructor
	 * @param dietPlanRepository implementation of {@link DietPlanPersistence}
	 */
	public SimpleDietPlanService(DietPlanPersistence dietPlanRepository) {
		this.dietPlanRepository = dietPlanRepository;
	}

	@Override
	public void create(DietPlan dietPlan) throws ServiceInvokationException {

		ServiceInvokationContext context = new ServiceInvokationContext();
		if (ValidationUtil.validateDietPlanForCreation(dietPlan, context) == false) {
			throw new ServiceInvokationException(context);
		}

		try {
			dietPlanRepository.create(dietPlan);
		} catch (PersistenceException e) {
			LOG.error("An error occured while creating a new Diet plan.");
			throw new ServiceInvokationException("An error occured while creating a new Diet plan in the database.");
		}
	}

	@Override
    public void switchTo(DietPlan dietPlan) throws ServiceInvokationException {

	    ServiceInvokationContext context = new ServiceInvokationContext();
	    if (ValidationUtil.validateDietPlanForUpdate(dietPlan, context) == false) {
	        throw new ServiceInvokationException(context);
        }

        try {
            dietPlanRepository.switchTo(dietPlan);
        } catch (PersistenceException e) {
            throw new ServiceInvokationException(e.getMessage());
        }
    }
}
