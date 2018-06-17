package at.ac.tuwien.sepm.assignment.groupphase.application.dto;

import java.awt.image.BufferedImage;

public class RecipeImage {
	private Integer id;
	private BufferedImage image;
	private String imageType;
	
	public RecipeImage(Integer id, BufferedImage image, String imageType) {
		this.id = id;
		this.setImage(image);
		this.setImageType(imageType);
	}
	
	public RecipeImage(BufferedImage image, String imageType) {
		this.setImage(image);
		this.setImageType(imageType);
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public BufferedImage getImage() {
		return image;
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}

	public String getImageType() {
		return imageType;
	}

	public void setImageType(String imageType) {
		this.imageType = imageType;
	}
}