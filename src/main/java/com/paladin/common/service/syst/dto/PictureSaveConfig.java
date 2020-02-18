package com.paladin.common.service.syst.dto;

public class PictureSaveConfig {

	private Integer width;
	private Integer height;
	private Double scale;
	private Double quality;
	private Integer thumbnailWidth;
	private Integer thumbnailHeight;

	public PictureSaveConfig() {

	}

	public PictureSaveConfig(int thumbnailWidth, int thumbnailHeight) {
		this.thumbnailHeight = thumbnailHeight;
		this.thumbnailWidth = thumbnailWidth;
	}

	public PictureSaveConfig(int width, int height, Double quality) {
		this.width = width;
		this.height = height;
		this.quality = quality;
	}

	public PictureSaveConfig(double scale, Double quality) {
		this.scale = scale;
		this.quality = quality;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public Double getScale() {
		return scale;
	}

	public void setScale(Double scale) {
		this.scale = scale;
	}

	public Double getQuality() {
		return quality;
	}

	public void setQuality(Double quality) {
		this.quality = quality;
	}

	public Integer getThumbnailWidth() {
		return thumbnailWidth;
	}

	public void setThumbnailWidth(Integer thumbnailWidth) {
		this.thumbnailWidth = thumbnailWidth;
	}

	public Integer getThumbnailHeight() {
		return thumbnailHeight;
	}

	public void setThumbnailHeight(Integer thumbnailHeight) {
		this.thumbnailHeight = thumbnailHeight;
	}
}
