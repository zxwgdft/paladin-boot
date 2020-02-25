package com.paladin.common.core.upload;

import com.paladin.framework.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

@Slf4j
public class BigFileUploaderContainer {

    private String targetFolder;
    private long chunkSize;
    private boolean initialized;

    public BigFileUploaderContainer(String targetFolder) {
        this(targetFolder, 5 * 1024 * 1024);
    }

    public BigFileUploaderContainer(String targetFolder, long chunkSize) {
        this.targetFolder = targetFolder;
        this.chunkSize = chunkSize;
        initialize();
    }

    private void initialize() {
        if (targetFolder == null || targetFolder.trim().length() == 0) {
            initialized = false;
            return;
        }
        // 创建目录
        Path root = Paths.get(targetFolder);
        try {
            Files.createDirectories(root);
            log.info("大文件存放目录：" + targetFolder);
            initialized = true;
        } catch (Exception e) {
            log.error("创建大文件存放目录异常[" + targetFolder + "]", e);
            initialized = false;
        }
    }

    private Map<String, BigFileUploader> bigFileUploaderMap = new HashMap<>();

    public BigFileUploader getOrCreateUploader(String id, int chunkCount, String fileName) {
        checkInitialized();

        BigFileUploader uploader = bigFileUploaderMap.get(id);
        if (uploader == null) {
            synchronized (bigFileUploaderMap) {
                uploader = bigFileUploaderMap.get(id);
                if (uploader == null) {
                    uploader = new BigFileUploader(id, chunkCount, chunkSize, targetFolder, fileName);
                    bigFileUploaderMap.put(id, uploader);
                }
            }
        }

        return uploader;
    }

    public boolean checkFileChunk(String id, int chunkIndex) {
        checkInitialized();

        BigFileUploader uploader = bigFileUploaderMap.get(id);
        if (uploader != null) {
            return uploader.checkFileChunk(chunkIndex);
        }
        return false;
    }

    public int uploadFileChunk(String id, int chunkIndex, byte[] data) {
        checkInitialized();

        BigFileUploader uploader = bigFileUploaderMap.get(id);
        if (uploader != null) {
            return uploader.uploadFileChunk(chunkIndex, data);
        }
        return BigFileUploader.UPLOAD_REUPLOAD;
    }

    public void cleanUploader(int minutes) {
        long time = System.currentTimeMillis() - 60L * 1000 * minutes;
        Iterator<Map.Entry<String, BigFileUploader>> it = bigFileUploaderMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, BigFileUploader> entry = it.next();
            BigFileUploader uploader = entry.getValue();
            if (uploader.isCompleted() || uploader.getLastUpdateTime() < time) {
                it.remove();
            }
        }
    }

    public List<FileVO> findAllFiles() {
        checkInitialized();

        Path root = Paths.get(targetFolder);
        List<FileVO> videos = new ArrayList<>();

        try {
            Files.walkFileTree(root, new FileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {

                    if (!file.toString().endsWith("temp")) {

                        String pelativePath = file.toString();

                        pelativePath = pelativePath.substring(targetFolder.length() - 1);

                        FileVO video = new FileVO();
                        video.setName(pelativePath);
                        video.setPelativePath(pelativePath);
                        video.setLastUpdateTime(Files.getLastModifiedTime(file).toMillis());
                        video.setSize(Files.size(file));

                        videos.add(video);
                    }

                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            throw new BusinessException("查找文件失败");
        }

        return videos;
    }

    public boolean deleteFile(String pelativePath) {
        checkInitialized();

        Path path = Paths.get(targetFolder + pelativePath);
        try {
            if (Files.deleteIfExists(path)) {
                cleanUploader(60);
                return true;
            }
            return false;
        } catch (IOException e) {
            throw new BusinessException("删除文件失败");
        }
    }

    private void checkInitialized() {
        if (!initialized) {
            throw new BusinessException("大文件上传服务未启动");
        }
    }

}
