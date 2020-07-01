package com.paladin.common.service.core.impl;

import com.paladin.common.service.core.FileStoreService;
import com.paladin.framework.io.FtpHelper;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import java.io.InputStream;

/**
 * @author TontoZhou
 * @since 2020/6/28
 */
public class FtpFileStoreService implements FileStoreService {

    @Value("${paladin.file.ftp-host}")
    private String host;

    @Value("${paladin.file.ftp-port}")
    private int port;

    @Value("${paladin.file.ftp-username}")
    private String username;

    @Value("${paladin.file.ftp-password}")
    private String password;

    @Value("${paladin.file.ftp-visit-host}")
    private int visitHost;

    @Value("${paladin.file.ftp-visit-port}")
    private int visitPort;

    private String baseVisitUrl;

    @PostConstruct
    protected void initialize() {
        baseVisitUrl = "http://" + visitHost + ":" + visitPort + "/";
    }


    @Override
    public void storeFile(InputStream input, String filePath, String fileName) {
        FTPClient ftp = FtpHelper.getFTPClient(host, port, username, password);
        FtpHelper.uploadFile(ftp, input, filePath, fileName);
        FtpHelper.closeFTP(ftp);
    }

    @Override
    public void deleteFile(String filePath, String fileName) {
        FTPClient ftp = FtpHelper.getFTPClient(host, port, username, password);
        FtpHelper.deleteFile(ftp, filePath, fileName);
        FtpHelper.closeFTP(ftp);
    }

    @Override
    public void checkAndMakeDirectory(String filePath) {
        // do nothing 在存储文件时会检查是否存在文件
    }

    @Override
    public String getStoreType() {
        return "ftp";
    }

    @Override
    public String getFileUrl(String relativePath) {
        return baseVisitUrl + relativePath;
    }
}
