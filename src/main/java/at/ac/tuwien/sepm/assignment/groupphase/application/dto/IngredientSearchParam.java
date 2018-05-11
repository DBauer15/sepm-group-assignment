package at.ac.tuwien.sepm.assignment.groupphase.application.dto;

/**
 * Search Param for ingredient search operation
 * @author e01529136
 *
 */
public class IngredientSearchParam {
	private String ingredientName;

	/**
	 * Constructor
	 * @param ingredientName {@link String}
	 */
	public IngredientSearchParam(String ingredientName) {
		this.ingredientName = ingredientName;
	}

	public String getIngredientName() {
		return ingredientName;
	}

	public void setIngredientName(String ingredientName) {
		this.ingredientName = ingredientName;
	}
}
