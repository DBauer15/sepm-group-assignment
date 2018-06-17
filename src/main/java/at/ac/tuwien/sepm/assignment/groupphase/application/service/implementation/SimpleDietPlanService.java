package at.ac.tuwien.sepm.assignment.groupphase.application.service.implementation;

import java.lang.invoke.MethodHandles;
import java.util.List;

import at.ac.tuwien.sepm.assignment.groupphase.application.persistence.NoEntryFoundException;
import at.ac.tuwien.sepm.assignment.groupphase.application.util.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import at.ac.tuwien.sepm.assignment.groupphase.application.dto.DietPlan;
import at.ac.tuwien.sepm.assignment.groupphase.application.persistence.DietPlanPersistence;
import at.ac.tuwien.sepm.assignment.groupphase.application.persistence.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.application.service.DietPlanService;
import at.ac.tuwien.sepm.assignment.groupphase.application.service.ServiceInvokationContext;
import at.ac.tuwien.sepm.assignment.groupphase.application.service.ServiceInvokationException;

@Service
public class SimpleDietPlanService implements DietPlanService {
	private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	private DietPlanPersistence dietPlanRepository;
	private Validator<DietPlan> dietPlanValidator;

	/**
	 * Constructor
	 * @param dietPlanRepository implementation of {@link DietPlanPersistence}
	 */
	public SimpleDietPlanService(DietPlanPersistence dietPlanRepository, Validator<DietPlan> dietPlanValidator) {
		this.dietPlanRepository = dietPlanRepository;
		this.dietPlanValidator = dietPlanValidator;
	}

	@Override
	public void create(DietPlan dietPlan) throws ServiceInvokationException {

		ServiceInvokationContext context = new ServiceInvokationContext();
		if (dietPlanValidator.validateForCreation(dietPlan, context) == false) {
			throw new ServiceInvokationException(context);
		}

		try {
			dietPlanRepository.create(dietPlan);
		} catch (PersistenceException e) {
			throw new ServiceInvokationException(e.getMessage(), e);
		}
	}

	@Override
    public List<DietPlan> readAll() throws ServiceInvokationException {
	    try {
	        List<DietPlan> dietPlans = dietPlanRepository.readAll();
	        for (DietPlan dietPlan : dietPlans) {
	            ServiceInvokationContext context = new ServiceInvokationContext();
	            if (dietPlanValidator.validateForReading(dietPlan, context) == false) {
	                throw new ServiceInvokationException(context);
                }
            }
            return dietPlans;
        } catch (PersistenceException e) {
            throw new ServiceInvokationException(e.getMessage(), e);
        }
    }

    @Override
    public DietPlan readActive() throws ServiceInvokationException, NoEntryFoundException{ //TODO NoEntryFoundException indicates that there is no active plan and that the plan chooser has to be shown
        try {
            ServiceInvokationContext context = new ServiceInvokationContext();
            DietPlan dietPlan = dietPlanRepository.readActive();
            if (dietPlanValidator.validateForReading(dietPlan, context) == false) {
                throw new ServiceInvokationException(context);
            }
            return dietPlan;
        } catch (PersistenceException e) {
            throw new ServiceInvokationException(e.getMessage(), e);
        }
    }

	@Override
    public void switchTo(DietPlan dietPlan) throws ServiceInvokationException {

	    ServiceInvokationContext context = new ServiceInvokationContext();
	    if (dietPlanValidator.validateForUpdate(dietPlan, context) == false) {
	        throw new ServiceInvokationException(context);
        }

        try {
            dietPlanRepository.switchTo(dietPlan);
        } catch (PersistenceException e) {
            throw new ServiceInvokationException(e.getMessage(), e);
        }
    }

	@Override
	public void update(DietPlan dietPlan) throws ServiceInvokationException {
        ServiceInvokationContext context = new ServiceInvokationContext();

        if (!dietPlanValidator.validateForUpdate(dietPlan, context)) {
            throw new ServiceInvokationException(context);
        }

        try {
        	dietPlanRepository.update(dietPlan);
        } catch (PersistenceException e) {
            throw new ServiceInvokationException(e);
        }
	}
}