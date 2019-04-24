package com.wow.carlauncher.repertory.db.manage;

public class BaseEntity{
	private Long id;//自增id
	private String createDate;// 创建日期
	private String modifyDate;// 修改日期

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(String modifyDate) {
		this.modifyDate = modifyDate;
	}
}
