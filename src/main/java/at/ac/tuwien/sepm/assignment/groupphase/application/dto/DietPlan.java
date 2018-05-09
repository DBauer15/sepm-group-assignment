package at.ac.tuwien.sepm.assignment.groupphase.application.dto;

import java.time.LocalDate;

/**
 * DTO for Diet Plan
 * @author e01529136
 *
 */
public class DietPlan {

	private Integer id;
	private String name;
	private Double energy_kcal;
	private Double lipid;
	private Double protein;
	private Double carbohydrate;
	private LocalDate fromDate;
	private LocalDate toDate;

	/**
	 * Constructor
	 * @param id {@link Integer}
	 * @param name {@link String}
	 * @param energy_kcal {@link Double}
	 * @param lipid {@link Double}
	 * @param protein {@link Double}
	 * @param carbohydrate {@link Double}
	 * @param fromDate {@link LocalDate}
	 * @param toDate {@link LocalDate}
	 */
	public DietPlan(Integer id, String name, Double energy_kcal, Double lipid, Double protein, Double carbohydrate,
			LocalDate fromDate, LocalDate toDate) {
		this.id = id;
		this.name = name;
		this.energy_kcal = energy_kcal;
		this.lipid = lipid;
		this.protein = protein;
		this.carbohydrate = carbohydrate;
		this.setFromDate(fromDate);
		this.setToDate(toDate);
	}

	/**
	 * Constructor
	 * @param name {@link String}
	 * @param energy_kcal {@link Double}
	 * @param lipid {@link Double}
	 * @param protein {@link Double}
	 * @param carbohydrate {@link Double}
	 */
	public DietPlan(String name, Double energy_kcal, Double lipid, Double protein, Double carbohydrate) {
		this.name = name;
		this.energy_kcal = energy_kcal;
		this.lipid = lipid;
		this.protein = protein;
		this.carbohydrate = carbohydrate;
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

	public Double getEnergy_kcal() {
		return energy_kcal;
	}

	public void setEnergy_kcal(Double energy_kcal) {
		this.energy_kcal = energy_kcal;
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

	public LocalDate getFromDate() {
		return fromDate;
	}

	public void setFromDate(LocalDate fromDate) {
		this.fromDate = fromDate;
	}

	public LocalDate getToDate() {
		return toDate;
	}

	public void setToDate(LocalDate toDate) {
		this.toDate = toDate;
	}

}
