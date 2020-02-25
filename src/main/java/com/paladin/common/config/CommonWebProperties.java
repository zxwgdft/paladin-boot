package com.paladin.common.config;

import com.paladin.framework.GlobalProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "paladin.web")
@Getter
@Setter
public class CommonWebProperties {

    /**
     * 文件存放地址
     */
    private String filePath;

    /**
     * 上传文件目录
     */
    private String uploadDir;

    /**
     * 文件最大M数
     */
    private int fileMaxSize = 10;

    /**
     * 静态资源路径
     */
    private String staticPath = "classpath:/static/";

    /**
     * 图标路径
     */
    private String faviconPath = "classpath:favicon.ico";

    /**
     * root view
     */
    private String rootView = "redirect:/" + GlobalProperties.project + "/login";


}
