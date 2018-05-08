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
	private LocalDate from_date;
	private LocalDate to_date;

	/**
	 * Constructor
	 * @param id {@link Integer}
	 * @param name {@link String}
	 * @param energy_kcal {@link Double}
	 * @param lipid {@link Double}
	 * @param protein {@link Double}
	 * @param carbohydrate {@link Double}
	 * @param from_date {@link LocalDate}
	 * @param to_date {@link LocalDate}
	 */
	public DietPlan(Integer id, String name, Double energy_kcal, Double lipid, Double protein, Double carbohydrate,
			LocalDate from_date, LocalDate to_date) {
		this.id = id;
		this.name = name;
		this.energy_kcal = energy_kcal;
		this.lipid = lipid;
		this.protein = protein;
		this.carbohydrate = carbohydrate;
		this.from_date = from_date;
		this.to_date = to_date;
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

	public LocalDate getFrom_date() {
		return from_date;
	}

	public void setFrom_date(LocalDate from_date) {
		this.from_date = from_date;
	}

	public LocalDate getTo_date() {
		return to_date;
	}

	public void setTo_date(LocalDate to_date) {
		this.to_date = to_date;
	}
}
