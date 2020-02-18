package com.paladin.demo.service.org.vo;

import java.util.Date;

import com.paladin.common.core.container.AttachmentContainer;
import com.paladin.common.model.syst.SysAttachment;

public class DemoPersonnelVO {

	// 
	private String id;

	// 所属机构
	private String agencyId;

	// 机构名称
	private String agencyName;

	// 证件类型
	private Integer identificationType;

	// 证件号码
	private String identificationNo;

	// 
	private String name;

	// 
	private Integer sex;

	// 
	private Date birthday;

	// 
	private String cellphone;

	// 
	private String officePhone;

	// 头像
	private String profilePhoto;

	// 民族
	private Integer nation;

	// 开始工作时间
	private Date startWorkTime;

	// 加入党派时间
	private Date joinPartyTime;

	// 政治面貌
	private Integer politicalAffiliation;

	// 籍贯
	private String nativePlace;
	
	public SysAttachment getProfilePhotoFile() {
		if(profilePhoto != null && profilePhoto.length() > 0) {
			return AttachmentContainer.getAttachment(profilePhoto);
		}
		return null;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAgencyId() {
		return agencyId;
	}

	public void setAgencyId(String agencyId) {
		this.agencyId = agencyId;
	}

	public String getAgencyName() {
		return agencyName;
	}

	public void setAgencyName(String agencyName) {
		this.agencyName = agencyName;
	}

	public Integer getIdentificationType() {
		return identificationType;
	}

	public void setIdentificationType(Integer identificationType) {
		this.identificationType = identificationType;
	}

	public String getIdentificationNo() {
		return identificationNo;
	}

	public void setIdentificationNo(String identificationNo) {
		this.identificationNo = identificationNo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getCellphone() {
		return cellphone;
	}

	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}

	public String getOfficePhone() {
		return officePhone;
	}

	public void setOfficePhone(String officePhone) {
		this.officePhone = officePhone;
	}

	public String getProfilePhoto() {
		return profilePhoto;
	}

	public void setProfilePhoto(String profilePhoto) {
		this.profilePhoto = profilePhoto;
	}

	public Integer getNation() {
		return nation;
	}

	public void setNation(Integer nation) {
		this.nation = nation;
	}

	public Date getStartWorkTime() {
		return startWorkTime;
	}

	public void setStartWorkTime(Date startWorkTime) {
		this.startWorkTime = startWorkTime;
	}

	public Date getJoinPartyTime() {
		return joinPartyTime;
	}

	public void setJoinPartyTime(Date joinPartyTime) {
		this.joinPartyTime = joinPartyTime;
	}

	public Integer getPoliticalAffiliation() {
		return politicalAffiliation;
	}

	public void setPoliticalAffiliation(Integer politicalAffiliation) {
		this.politicalAffiliation = politicalAffiliation;
	}

	public String getNativePlace() {
		return nativePlace;
	}

	public void setNativePlace(String nativePlace) {
		this.nativePlace = nativePlace;
	}

}