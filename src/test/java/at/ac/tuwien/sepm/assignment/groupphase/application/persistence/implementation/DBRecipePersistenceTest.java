package at.ac.tuwien.sepm.assignment.groupphase.application.persistence.implementation;

import java.util.EnumSet;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import at.ac.tuwien.sepm.assignment.groupphase.application.dto.IngredientSearchParam;
import at.ac.tuwien.sepm.assignment.groupphase.application.dto.Recipe;
import at.ac.tuwien.sepm.assignment.groupphase.application.dto.RecipeIngredient;
import at.ac.tuwien.sepm.assignment.groupphase.application.dto.RecipeTag;
import at.ac.tuwien.sepm.assignment.groupphase.application.persistence.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.application.persistence.RecipePersistence;
import at.ac.tuwien.sepm.assignment.groupphase.application.util.implementation.JDBCConnectionManager;

public class DBRecipePersistenceTest {

	@Test
	public void testCreate_withRecipe_successWithSetRecipeId() throws PersistenceException {
		RecipePersistence recipePersistence = new DBRecipePersistence();

		EnumSet<RecipeTag> set = EnumSet.noneOf(RecipeTag.class);
		set.add(RecipeTag.B);

		Recipe recipe = new Recipe("My recipe", 120d, "Test", set);
		recipePersistence.create(recipe);

		Assert.assertNotNull(recipe.getId());
		Assert.assertTrue(recipe.getId() > 0);
	}

	@Test
	public void testSearchIngredient_queryIsEgg_resultSetNotEmptyAndContainsSpecificIngredient()
			throws PersistenceException {
		RecipePersistence recipePersistence = new DBRecipePersistence();

		List<RecipeIngredient> result = recipePersistence.searchIngredient(new IngredientSearchParam("egg"));
		Assert.assertTrue(result.size() >= 37);

		boolean found = false;
		for (RecipeIngredient ri : result) {
			if ("Eggnog".equals(ri.getIngredientName()) == false) {
				continue;
			}
			Assert.assertTrue(56 == ri.getId());
			Assert.assertTrue(88.0 == ri.getEnergyKcal());
			Assert.assertTrue(4.19 == ri.getLipid());
			Assert.assertTrue(4.55 == ri.getProtein());
			Assert.assertTrue(8.05 == ri.getCarbohydrate());
			Assert.assertTrue(254.0 == ri.getUnitGramNormalised());
			Assert.assertFalse(ri.getUserSpecific());
			Assert.assertEquals("cup", ri.getUnitName());
			found = true;
		}
		if (found == false) {
			Assert.fail("At least one Recipe ingredient was not transformed "
					+ "correct from resultset or ingredient data is incomplete.");
		}
	}
}