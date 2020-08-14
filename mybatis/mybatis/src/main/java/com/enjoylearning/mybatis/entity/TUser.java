package com.enjoylearning.mybatis.entity;

import java.io.Serializable;

public class TUser implements Serializable{

	private String id;

	private String user_name;

	private String real_name;

	private int sex;

	private String email;

	private String note;

	private String mobile;

	private int position_id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getReal_name() {
		return real_name;
	}

	public void setReal_name(String real_name) {
		this.real_name = real_name;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public int getPosition_id() {
		return position_id;
	}

	public void setPosition_id(int position_id) {
		this.position_id = position_id;
	}

	@Override
	public String toString() {
		return "TUser{" +
				"id='" + id + '\'' +
				", user_name='" + user_name + '\'' +
				", real_name='" + real_name + '\'' +
				", sex=" + sex +
				", email='" + email + '\'' +
				", note='" + note + '\'' +
				", mobile='" + mobile + '\'' +
				", position_id=" + position_id +
				'}';
	}
}