package com.paladin.common.service.upload;

public class FileVO {
	
	private String name;
	private String pelativePath;
	private long lastUpdateTime;
	private long size;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPelativePath() {
		return pelativePath;
	}
	public void setPelativePath(String pelativePath) {
		this.pelativePath = pelativePath;
	}
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	public long getLastUpdateTime() {
		return lastUpdateTime;
	}
	public void setLastUpdateTime(long lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	
}
