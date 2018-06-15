package at.ac.tuwien.sepm.assignment.groupphase.application.dto;

import java.util.ArrayList;
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
	private List<RecipeImage> recipeImages = null;
    private Double calories;
    private Double carbohydrates;
    private Double proteins;
    private Double fats;

	public Recipe() {
		recipeIngredients = new ArrayList<>();
		setRecipeImages(new ArrayList<>());
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

    public List<RecipeImage> getRecipeImages() {
		return recipeImages;
	}

	public void setRecipeImages(List<RecipeImage> recipeImages) {
		this.recipeImages = recipeImages;
	}

	public Double getCalories() {
        return calories;
    }

    public void setCalories(Double calories) {
        this.calories = calories;
    }

    public Double getCarbohydrates() {
        return carbohydrates;
    }

    public void setCarbohydrates(Double carbohydrates) {
        this.carbohydrates = carbohydrates;
    }

    public Double getProteins() {
        return proteins;
    }

    public void setProteins(Double proteins) {
        this.proteins = proteins;
    }

    public Double getFats() {
        return fats;
    }

    public void setFats(Double fats) {
        this.fats = fats;
    }

    public Double getCarbohydratePercent() {
        return (getCarbohydrates() / getNutritionTotal()) * 100;
    }

    public Double getProteinPercent() {
        return (getProteins() / getNutritionTotal()) * 100;
    }

    public Double getFatPercent() {
        return (getFats() / getNutritionTotal()) * 100;
    }

    private Double getNutritionTotal() {
        return getCarbohydrates() + getProteins() + getFats();
    }

	@Override
	public String toString() {
		return "Recipe{" + "id=" + id + ", name='" + name + '\'' + ", duration=" + duration + ", description='"
				+ description + '\'' + ", tags=" + tags + ", deleted=" + deleted + '}';
	}

	@Override
    public boolean equals(Object o) {
        if (o instanceof Recipe) {
            Recipe r = (Recipe)o;
            return r.getId() == this.getId();
        }

        return false;
    }

    @Override
    public int hashCode(){
	    return this.getId();
    }
}