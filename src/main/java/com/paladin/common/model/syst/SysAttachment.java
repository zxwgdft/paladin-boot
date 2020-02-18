package com.paladin.common.model.syst;

import java.util.Date;

import javax.persistence.Id;

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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public String getPelativePath() {
		return pelativePath;
	}

	public void setPelativePath(String pelativePath) {
		this.pelativePath = pelativePath;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}



	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getUseType() {
		return useType;
	}

	public void setUseType(Integer useType) {
		this.useType = useType;
	}

	public String getThumbnailPelativePath() {
		return thumbnailPelativePath;
	}

	public void setThumbnailPelativePath(String thumbnailPelativePath) {
		this.thumbnailPelativePath = thumbnailPelativePath;
	}

}