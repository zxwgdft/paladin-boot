package com.paladin.common.service.file.impl;

import com.paladin.common.service.file.TemporaryFileOutputStream;
import com.paladin.common.service.file.TemporaryFileService;
import com.paladin.framework.utils.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 本地临时文件服务实现
 */
@Slf4j
public class LocalTemporaryFileService implements TemporaryFileService {

    @Value("${paladin.file.base-path}")
    private String filePath;
    // 最大文件保留时长
    private int maxKeepMinutes = 30;

    @PostConstruct
    public void init() {
        if (filePath.startsWith("file:")) {
            filePath = filePath.substring(5);
        }

        filePath = filePath.replaceAll("\\\\", "/");

        if (!filePath.endsWith("/")) {
            filePath += "/temp/";
        } else {
            filePath += "temp/";
        }

        // 创建目录
        Path root = Paths.get(filePath);
        try {
            Files.createDirectories(root);
        } catch (IOException e) {
            throw new RuntimeException("创建临时文件存放目录异常", e);
        }

        log.info("临时文件目录：" + filePath);
    }

    @Override
    public TemporaryFileOutputStream getTemporaryFileOutputStream(String name, String suffix) {
        String id = UUIDUtil.create32UUID();
        String fileName = name == null ? id : (name + id);

        if (suffix != null) {
            fileName += "." + suffix;
        }

        try {
            return new LocalTemporaryFileOutputStream(new FileOutputStream(filePath + fileName), "/file/temp/" + fileName);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void clearTemporaryFile() {
        log.info("开始清理过期临时文件");

        Path root = Paths.get(filePath);
        long time = System.currentTimeMillis() - 60L * 1000 * maxKeepMinutes;

        try {
            Files.list(root).forEach(path -> {
                if (path.toFile().lastModified() < time) {
                    try {
                        Files.delete(path);
                    } catch (IOException e) {
                        log.error("删除临时文件[" + path + "]失败");
                    }
                }
            });
        } catch (IOException e) {
            log.error("遍历删除临时文件失败");
        }
    }


    public static class LocalTemporaryFileOutputStream extends TemporaryFileOutputStream {

        private OutputStream output;
        private String fileUrl;

        private LocalTemporaryFileOutputStream(OutputStream output, String fileUrl) {
            this.output = output;
            this.fileUrl = fileUrl;
        }

        @Override
        public void write(int b) throws IOException {
            output.write(b);
        }

        @Override
        public void write(byte[] b) throws IOException {
            output.write(b);
        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            output.write(b, off, len);
        }

        @Override
        public void flush() throws IOException {
            output.flush();
        }

        @Override
        public void close() throws IOException {
            output.close();
        }

        public String getFileUrl() {
            return fileUrl;
        }
    }

}
