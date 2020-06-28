package com.paladin.common.service.sys.impl;

import com.paladin.common.service.sys.FileStoreService;
import com.paladin.framework.exception.BusinessException;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author TontoZhou
 * @since 2020/6/28
 */
public class DefaultFileStoreService implements FileStoreService {

    @Value("${paladin.file.base-path}")
    private String filePath;


    @PostConstruct
    protected void initialize() {
        if (filePath.startsWith("file:")) {
            filePath = filePath.substring(5);
        }

        filePath = filePath.replaceAll("\\\\", "/");

        if (!filePath.endsWith("/")) {
            filePath += "/";
        }

        // 创建目录
        Path root = Paths.get(filePath);
        try {
            Files.createDirectories(root);
        } catch (IOException e) {
            throw new RuntimeException("创建附件存放目录异常", e);
        }
    }


    @Override
    public void storeFile(InputStream input, String filePath, String fileName) {
        String path = this.filePath + filePath + "/" + fileName;
        try (OutputStream out = Files.newOutputStream(Paths.get(path))) {
            byte[] buffer = new byte[2048];
            int len;
            while ((len = input.read(buffer)) > -1) {
                out.write(buffer, 0, len);
            }
        } catch (IOException e) {
            throw new BusinessException("存储文件[" + path + "]失败", e);
        }
    }

    @Override
    public void checkAndMakeDirectory(String subPath) {
        Path path = Paths.get(filePath, subPath);
        if (!Files.exists(path)) {
            try {
                Files.createDirectory(path);
            } catch (FileAlreadyExistsException e1) {
                // 继续
            } catch (IOException e) {
                throw new BusinessException("创建文件目录[" + filePath + subPath + "]失败", e);
            }
        }
    }
}
