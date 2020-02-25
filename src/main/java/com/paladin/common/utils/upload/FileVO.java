package com.paladin.common.utils.upload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileVO {

    private String name;
    private String pelativePath;
    private long lastUpdateTime;
    private long size;

}
