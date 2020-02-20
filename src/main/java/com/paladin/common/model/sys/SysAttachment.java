package com.paladin.common.model.sys;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;
import java.util.Date;

@Getter
@Setter
public class SysAttachment {

    public final static int USE_TYPE_COLUMN_RELATION = 1;
    public final static int USE_TYPE_RESOURCE = 2;

    public static final String COLUMN_FIELD_ID = "id";
    public static final String COLUMN_FIELD_USER_TYPE = "userType";

    @Id
    private String id;

    private Integer useType;

    private String type;

    private String name;

    private String suffix;

    private Long size;

    private String pelativePath;

    private String thumbnailPelativePath;

    private Date createTime;


}