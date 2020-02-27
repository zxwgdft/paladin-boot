package com.paladin.common.service.sys.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * @author TontoZhou
 * @since 2020/2/27
 */
@Getter
@Setter
public class FileResource {

    private String name;
    private long size;
    private String url;
    private String thumbnailUrl;

}
