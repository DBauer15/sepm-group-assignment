package at.ac.tuwien.sepm.assignment.groupphase.application.dto;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.stream.Collectors;

public class Recipe {
    private Integer id;
    private String name;
    private Double duration;
    // private description;
    private EnumSet<RecipeTag> tags;
    private Boolean deleted;

    /**
     * @param id
     * @param name
     * @param duration
     * @param tags
     * @param deleted
     */
    public Recipe(Integer id, String name, Double duration, EnumSet<RecipeTag> tags, Boolean deleted) {
        this.id = id;
        this.name = name;
        this.duration = duration;
        this.tags = tags;
        this.deleted = deleted;
    }

    /**
     * @param id
     * @param name
     * @param duration
     * @param tagsAsString
     * @param deleted
     */
    public Recipe(Integer id, String name, Double duration, String tagsAsString, Boolean deleted) {
        this.id = id;
        this.name = name;
        this.duration = duration;
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
        return tags
            .stream()
            .map(RecipeTag::toString)
            .collect(Collectors.joining());
    }

    public void setTagsAsString(String tags) {
        this.tags = Arrays
            .stream(tags.split(""))
            .map(RecipeTag::valueOf)
            .collect(Collectors.toCollection(() -> EnumSet.noneOf(RecipeTag.class)));
    }
}
