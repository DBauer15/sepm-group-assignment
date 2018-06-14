package at.ac.tuwien.sepm.assignment.groupphase.application.dto;

public class IngredientSearchWord {
	private String ingredientTag;
	
	public IngredientSearchWord(String ingredientTag) {
		this.ingredientTag = ingredientTag;
	}

	public String getIngredientTag() {
		return ingredientTag;
	}

	public void setIngredientTag(String ingredientTag) {
		this.ingredientTag = ingredientTag;
	}
}