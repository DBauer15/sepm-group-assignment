package at.ac.tuwien.sepm.assignment.groupphase.application.dto;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

public class Recipe {
	private Integer id;
	private String name;
	private Double duration;
	private String description;
	private EnumSet<RecipeTag> tags;
	private Boolean deleted;
	private List<RecipeIngredient> recipeIngredients = null;

	public Recipe() {
		// NOOP
	}

	/**
	 * @param id
	 * @param name
	 * @param duration
	 * @param description
	 * @param tags
	 * @param deleted
	 */
	public Recipe(Integer id, String name, Double duration, String description, EnumSet<RecipeTag> tags,
			Boolean deleted) {
		this.id = id;
		this.name = name.trim();
		this.duration = duration;
		this.description = description.trim();
		this.tags = tags;
		this.deleted = deleted;
	}

	/**
	 * Constructor
	 * 
	 * @param name
	 * @param duration
	 * @param description
	 * @param tags
	 */
	public Recipe(String name, Double duration, String description, EnumSet<RecipeTag> tags) {
		this.name = name.trim();
		this.duration = duration;
		this.description = description.trim();
		this.tags = tags;
	}

	/**
	 * @param id
	 * @param name
	 * @param duration
	 * @param description
	 * @param tagsAsString
	 * @param deleted
	 */
	public Recipe(Integer id, String name, Double duration, String description, String tagsAsString, Boolean deleted) {
		this.id = id;
		this.name = name.trim();
		this.duration = duration;
		this.description = description.trim();
		setTagsAsString(tagsAsString);
		this.deleted = deleted;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getDuration() {
		return duration;
	}

	public void setDuration(Double duration) {
		this.duration = duration;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public EnumSet<RecipeTag> getTags() {
		return tags;
	}

	public void setTags(EnumSet<RecipeTag> tags) {
		this.tags = tags;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	public String getTagsAsString() {
		return tags.stream().map(RecipeTag::toString).collect(Collectors.joining());
	}

	public void setTagsAsString(String tags) {
		this.tags = Arrays.stream(tags.split("")).map(RecipeTag::valueOf)
				.collect(Collectors.toCollection(() -> EnumSet.noneOf(RecipeTag.class)));
	}

	public List<RecipeIngredient> getRecipeIngredients() {
		return recipeIngredients;
	}

	public void setRecipeIngredients(List<RecipeIngredient> recipeIngredients) {
		this.recipeIngredients = recipeIngredients;
	}

	@Override
	public String toString() {
		return "Recipe{" + "id=" + id + ", name='" + name + '\'' + ", duration=" + duration + ", description='"
				+ description + '\'' + ", tags=" + tags + ", deleted=" + deleted + '}';
	}
}