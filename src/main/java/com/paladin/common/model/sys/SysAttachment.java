package com.paladin.common.model.sys;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class SysAttachment {

    @TableId
    private String id;

    private String storeType;

    private String name;

    private String suffix;

    private Long size;

    private String relativePath;

    private String thumbnailRelativePath;

    private Date createTime;

    private String operateBy;

    private Date operateTime;

    private Boolean deleted;
}