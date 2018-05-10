package at.ac.tuwien.sepm.assignment.groupphase.application.persistence.implementation;

import java.util.EnumSet;

import org.junit.Assert;
import org.junit.Test;

import at.ac.tuwien.sepm.assignment.groupphase.application.dto.Recipe;
import at.ac.tuwien.sepm.assignment.groupphase.application.dto.RecipeTag;
import at.ac.tuwien.sepm.assignment.groupphase.application.persistence.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.application.persistence.RecipePersistence;
import at.ac.tuwien.sepm.assignment.groupphase.application.util.implementation.JDBCConnectionManager;

public class DBRecipePersistenceTest {

	@Test
	public void testCreate_withRecipe_successWithSetRecipeId() throws PersistenceException {
		RecipePersistence dietPlanPersistence = new DBRecipePersistence();

		EnumSet<RecipeTag> set = EnumSet.noneOf(RecipeTag.class);
		set.add(RecipeTag.B);

		Recipe recipe = new Recipe("My recipe", 120d, "Test", set);
		dietPlanPersistence.create(recipe);

		Assert.assertNotNull(recipe.getId());
		Assert.assertTrue(recipe.getId() > 0);
	}
}