package at.ac.tuwien.sepm.assignment.groupphase.application.dto;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Search Parameter for the Recipe Search
 */
public class RecipeSearchParam {

	// recipe's in result must consist of at least this subset of ingredients
	private Set<String> ingredients = new HashSet<>(); // never null
	private String recipeName = null; // trimmed string value or null
	private EnumSet<RecipeTag> tags = null; // null or consists of at least one tag, recipe must match every tag
	private Double lowerDurationInkl = null; // null or double
	private Double upperDurationInkl = null; // null or double

	public String getRecipeName() {
		return recipeName;
	}

	public void setRecipeName(String recipeName) {
		this.recipeName = recipeName != null ? recipeName.trim() : null;
	}

	public Double getLowerDurationInkl() {
		return lowerDurationInkl;
	}

	public void setLowerDurationInkl(Double lowerDurationInkl) {
		this.lowerDurationInkl = lowerDurationInkl;
	}

	public Double getUpperDurationInkl() {
		return upperDurationInkl;
	}

	public void setUpperDurationInkl(Double upperDurationInkl) {
		this.upperDurationInkl = upperDurationInkl;
	}

	public Set<String> getIngredients() {
		return ingredients;
	}

	public void addIngredient(String ingredientName) {
		if (ingredientName == null || ingredientName.trim().isEmpty()) {
			return;
		}
		ingredients.add(ingredientName.trim());
	}

	public boolean removeIngredient(String ingredientName) {
		if (ingredientName == null || ingredientName.trim().isEmpty()) {
			return false;
		}
		return ingredients.remove(ingredientName.trim());
	}

	public EnumSet<RecipeTag> getTags() {
		return tags;
	}

	public void setTags(EnumSet<RecipeTag> tags) {
		this.tags = (tags == null || tags.isEmpty()) ? null : tags;
	}

	public String getTagsAsString() {
		return getTags() == null ? null : getTags().stream().map(RecipeTag::toString).collect(Collectors.joining());
	}

	public void setTagsAsString(String tags) {
		this.setTags(Arrays.stream(tags.split("")).map(RecipeTag::valueOf)
				.collect(Collectors.toCollection(() -> EnumSet.noneOf(RecipeTag.class))));
	}
}
