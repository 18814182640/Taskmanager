package com.TMS.model;

import com.TMS.entity.DepartmentInfoPojo;
import com.TMS.entity.ItemInfoPojo;
import com.TMS.entity.TaskInfoPojo;
import com.TMS.entity.UserInfoPojo;

public class TaskModel{
	
	private TaskInfoPojo task;
	private UserInfoPojo authorizer;  //发起人
	private UserInfoPojo assigner;  //指定人
	private DepartmentInfoPojo department;
	private ItemInfoPojo itemInfoPojo;
	
	public TaskInfoPojo getTask() {
		return task;
	}
	public void setTask(TaskInfoPojo task) {
		this.task = task;
	}
	public UserInfoPojo getAuthorizer() {
		return authorizer;
	}
	public void setAuthorizer(UserInfoPojo authorizer) {
		this.authorizer = authorizer;
	}
	public UserInfoPojo getAssigner() {
		return assigner;
	}
	public void setAssigner(UserInfoPojo assigner) {
		this.assigner = assigner;
	}
	public DepartmentInfoPojo getDepartment() {
		return department;
	}
	public void setDepartment(DepartmentInfoPojo department) {
		this.department = department;
	}
	public ItemInfoPojo getItemInfoPojo() {
		return itemInfoPojo;
	}
	public void setItemInfoPojo(ItemInfoPojo itemInfoPojo) {
		this.itemInfoPojo = itemInfoPojo;
	}
	
	
	
	

}
