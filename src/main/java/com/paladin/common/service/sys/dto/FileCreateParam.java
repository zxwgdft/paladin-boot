package com.paladin.common.service.sys.dto;

import com.paladin.framework.exception.BusinessException;
import com.paladin.framework.utils.convert.Base64Util;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Getter
@Setter
public class FileCreateParam {

    @ApiModelProperty("文件名称")
    private String filename;
    @ApiModelProperty("文件大小")
    private long size;
    @ApiModelProperty("文件类型")
    private Integer type;
    @ApiModelProperty("文件输入流")
    private InputStream input;


    @ApiModelProperty("是否压缩")
    private boolean needCompress;

    // 修改原图参数
    @ApiModelProperty("图片压缩后宽度")
    private Integer width;
    @ApiModelProperty("图片压缩后高度")
    private Integer height;
    @ApiModelProperty("图片压缩规模")
    private Double scale;
    @ApiModelProperty("图片压缩质量")
    private Double quality;

    // 修改缩略图参数
    @ApiModelProperty("缩略图宽度")
    private Integer thumbnailWidth;
    @ApiModelProperty("缩略图高度")
    private Integer thumbnailHeight;


    public void setFileContent(MultipartFile file) {
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
        this.size = file.getSize();
        try {
            this.input = file.getInputStream();
        } catch (IOException e) {
            throw new BusinessException("读取文件异常", e);
        }
    }

    public void setFileContent(String base64Str) {
        byte[] data = Base64Util.decode(base64Str);
        this.size = data.length;
        this.input = new ByteArrayInputStream(data);
    }


}
