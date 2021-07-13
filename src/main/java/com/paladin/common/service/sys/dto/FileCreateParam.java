package com.paladin.common.service.sys.dto;

import com.paladin.framework.exception.BusinessException;
import com.paladin.framework.utils.convert.Base64Util;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Getter
@Setter
public class FileCreateParam {

    // 文件名称
    private String filename;
    // 文件大小
    private long size;
    // 文件输入流
    private InputStream input;

    // 图片压缩参数

    // 是否压缩
    private boolean needCompress;

    // 图片压缩后宽度
    private Integer width;
    // 图片压缩后高度
    private Integer height;
    // 图片压缩规模
    private Double scale;
    // 图片压缩质量
    private Double quality;

    // 缩略图参数

    // 是否创建图片缩略图
    private boolean needThumbnail;

    // 缩略图宽度
    private Integer thumbnailWidth;
    // 缩略图高度
    private Integer thumbnailHeight;

    public FileCreateParam(MultipartFile file) {
        this(file, null);
    }

    public FileCreateParam(MultipartFile file, String filename) {
        if (filename == null || filename.length() == 0) {
            filename = file.getOriginalFilename();
            // IE文件名会包含路径，这里需要处理掉
            int i = filename.lastIndexOf("\\");
            if (i > -1) {
                filename = filename.substring(i + 1);
            }

            i = filename.lastIndexOf("/");
            if (i > -1) {
                filename = filename.substring(i + 1);
            }
        }

        this.filename = filename;
        this.size = file.getSize();
        try {
            this.input = file.getInputStream();
        } catch (IOException e) {
            throw new BusinessException("读取文件异常", e);
        }
    }

    public FileCreateParam(String base64Str, String filename) {
        byte[] data = Base64Util.decode(base64Str);
        this.size = data.length;
        this.filename = (filename == null || filename.length() == 0) ? "未命名" : filename;
        this.input = new ByteArrayInputStream(data);
    }


}
