package com.TMS.entity;

import java.sql.Date;
import java.sql.Timestamp;


public class TaskInfoPojo {

	
	private Integer indexflag;
	private Integer auth_id;
	private Integer assign_id;
	private Integer status_id;
	private String task_info;
	private long start_time;
	private long end_time;
	private long save_time;
	private String svn_path;
	private Integer item_id;
	public Integer getIndexflag() {
		return indexflag;
	}
	public void setIndexflag(Integer indexflag) {
		this.indexflag = indexflag;
	}
	public Integer getAuth_id() {
		return auth_id;
	}
	public void setAuth_id(Integer auth_id) {
		this.auth_id = auth_id;
	}
	public Integer getAssign_id() {
		return assign_id;
	}
	public void setAssign_id(Integer assign_id) {
		this.assign_id = assign_id;
	}
	
	public Integer getStatus_id() {
		return status_id;
	}
	public void setStatus_id(Integer status_id) {
		this.status_id = status_id;
	}
	public String getTask_info() {
		return task_info;
	}
	public void setTask_info(String task_info) {
		this.task_info = task_info;
	}
	public long getStart_time() {
		return start_time;
	}
	public void setStart_time(long start_time) {
		this.start_time = start_time;
	}
	public long getEnd_time() {
		return end_time;
	}
	public void setEnd_time(long end_time) {
		this.end_time = end_time;
	}
	public long getSave_time() {
		return save_time;
	}
	public void setSave_time(long save_time) {
		this.save_time = save_time;
	}
	public String getSvn_path() {
		return svn_path;
	}
	public void setSvn_path(String svn_path) {
		this.svn_path = svn_path;
	}
	public Integer getItem_id() {
		return item_id;
	}
	public void setItem_id(Integer item_id) {
		this.item_id = item_id;
	}
	
    
	
	
	
}
