package at.ac.tuwien.sepm.assignment.groupphase.application.dto;

public class RecipeIngredient {

	private Double amount;
	private Integer id;
	private Double energyKcal;
	private Double lipid;
	private Double protein;
	private Double carbohydrate;
	private String unitName;
	private Double unitGramNormalised;
	private Boolean userSpecific;
	private String ingredientName;

	/**
	 * Constructor
	 * @param id
	 * @param amount
	 * @param energyKcal
	 * @param lipid
	 * @param protein
	 * @param carbohydrate
	 * @param unitName
	 * @param unitGramNormalised
	 * @param userSpecific
	 * @param ingredientName
	 */
	public RecipeIngredient(Integer id, Double amount, Double energyKcal, Double lipid, Double protein,
			Double carbohydrate, String unitName, Double unitGramNormalised, Boolean userSpecific, String ingredientName) {
		this.id = id;
		this.amount = amount;
		this.energyKcal = energyKcal;
		this.lipid = lipid;
		this.protein = protein;
		this.carbohydrate = carbohydrate;
		this.unitName = unitName;
		this.unitGramNormalised = unitGramNormalised;
		this.userSpecific = userSpecific;
		this.ingredientName = ingredientName.trim();
	}

	/**
	 * New RecipeIngredient where Ingredient is new
	 * @param amount
	 * @param energyKcal
	 * @param lipid
	 * @param protein
	 * @param carbohydrate
	 * @param unitName
	 * @param unitGramNormalised
	 * @param userSpecific
	 * @param ingredientName
	 */
	public RecipeIngredient(Double amount, Double energyKcal, Double lipid, Double protein, Double carbohydrate,
			String unitName, Double unitGramNormalised, Boolean userSpecific, String ingredientName) {
		this.amount = amount;
		this.energyKcal = energyKcal;
		this.lipid = lipid;
		this.protein = protein;
		this.carbohydrate = carbohydrate;
		this.unitName = unitName;
		this.unitGramNormalised = unitGramNormalised;
		this.userSpecific = userSpecific;
		this.ingredientName = ingredientName.trim();
	}

	/**
	 * New RecipeIngredient where Ingredient ID is not null
	 * @param id
	 * @param amount
	 * @param userSpecific
	 */
	public RecipeIngredient(Integer id, Double amount, Boolean userSpecific) {
		this.id = id;
		this.amount = amount;
		this.userSpecific = userSpecific;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Double getEnergyKcal() {
		return energyKcal;
	}

	public void setEnergyKcal(Double energyKcal) {
		this.energyKcal = energyKcal;
	}

	public Double getLipid() {
		return lipid;
	}

	public void setLipid(Double lipid) {
		this.lipid = lipid;
	}

	public Double getProtein() {
		return protein;
	}

	public void setProtein(Double protein) {
		this.protein = protein;
	}

	public Double getCarbohydrate() {
		return carbohydrate;
	}

	public void setCarbohydrate(Double carbohydrate) {
		this.carbohydrate = carbohydrate;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public Double getUnitGramNormalised() {
		return unitGramNormalised;
	}

	public void setUnitGramNormalised(Double unitGramNormalised) {
		this.unitGramNormalised = unitGramNormalised;
	}

	public Boolean getUserSpecific() {
		return userSpecific;
	}

	public void setUserSpecific(Boolean userSpecific) {
		this.userSpecific = userSpecific;
	}

	public String getIngredientName() {
		return ingredientName;
	}

	public void setIngredientName(String ingredientName) {
		this.ingredientName = ingredientName;
	}

	@Override
    public String toString() {
	    return ingredientName;
    }

}
