package com.paladin.framework.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.paladin.framework.GlobalProperties;

@ConfigurationProperties(prefix = "paladin.web")
public class WebProperties {

    /**
     * 文件存放地址
     */
    private String filePath = "file:D:/file/";

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

    /**
     * url跳转配置
     */
    private List<UrlForwardOption> forwards = new ArrayList<>();


    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getFileMaxSize() {
        return fileMaxSize;
    }

    public void setFileMaxSize(int fileMaxSize) {
        this.fileMaxSize = fileMaxSize;
    }

    public String getStaticPath() {
        return staticPath;
    }

    public void setStaticPath(String staticPath) {
        this.staticPath = staticPath;
    }

    public String getFaviconPath() {
        return faviconPath;
    }

    public void setFaviconPath(String faviconPath) {
        this.faviconPath = faviconPath;
    }

    public String getRootView() {
        return rootView;
    }

    public void setRootView(String rootView) {
        this.rootView = rootView;
    }

    public List<UrlForwardOption> getForwards() {
        return forwards;
    }

    public void setForwards(List<UrlForwardOption> forwards) {
        this.forwards = forwards;
    }


}
