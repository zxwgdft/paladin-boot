package com.paladin.common.service.sys.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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

}
