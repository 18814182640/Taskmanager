package com.TMS.entity;

import java.sql.Date;
import java.sql.Timestamp;

public class UserInfoPojo {
	
	private Integer id;
	private String user_name;
	private String user_password;
	private String user_mail;
	private String phone_number;
	private String company;
	private String real_name;
	private Integer user_role;
	private Integer department;
	private Integer user_status;
	private long save_time;
	
	private int taskNum;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	
	public String getUser_password() {
		return user_password;
	}
	public void setUser_password(String user_password) {
		this.user_password = user_password;
	}
	public String getUser_mail() {
		return user_mail;
	}
	public void setUser_mail(String user_mail) {
		this.user_mail = user_mail;
	}
	public String getPhone_number() {
		return phone_number;
	}
	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getReal_name() {
		return real_name;
	}
	public void setReal_name(String real_name) {
		this.real_name = real_name;
	}
	public Integer getUser_role() {
		return user_role;
	}
	public void setUser_role(Integer user_role) {
		this.user_role = user_role;
	}
	public Integer getDepartment() {
		return department;
	}
	public void setDepartment(Integer department) {
		this.department = department;
	}
	public Integer getUser_status() {
		return user_status;
	}
	public void setUser_status(Integer user_status) {
		this.user_status = user_status;
	}
	public long getSave_time() {
		return save_time;
	}
	public void setSave_time(long save_time) {
		this.save_time = save_time;
	}
	public int getTaskNum() {
		return taskNum;
	}
	public void setTaskNum(int taskNum) {
		this.taskNum = taskNum;
	}
	
	

}
