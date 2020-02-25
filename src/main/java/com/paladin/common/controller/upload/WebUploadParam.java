package com.paladin.common.controller.upload;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class WebUploadParam {

    private String md5;
    private int chunk;
    private int chunks;
    private MultipartFile file;
    private String name;

}
