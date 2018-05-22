package at.ac.tuwien.sepm.assignment.groupphase.application.dto;

public enum RecipeTag {
    B ("breakfast"),
    D ("dinner"),
    L ("lunch");

    private final String name;

    private RecipeTag(String name) {
        this.name = name;
    }

    public String toString() {
        return this.name;
    }
}