package at.ac.tuwien.sepm.assignment.groupphase.application.dto;

public class RecipeImage {
	private Integer id;

	/**
	 * New RecipeIngredient where Ingredient ID is not null
	 * @param id
	 * @param amount
	 * @param userSpecific
	 */
	public RecipeImage(Integer id, Double amount, Boolean userSpecific) {
		this.id = id;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
}