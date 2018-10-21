package com.TMS.entity;

public class DepartmentInfoPojo {
	
	private Integer id;
	private String department;
	private String explain;
	private Integer leader_id;
	private Integer member_id;
	private Integer status;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getExplain() {
		return explain;
	}
	public void setExplain(String explain) {
		this.explain = explain;
	}
	public Integer getLeader_id() {
		return leader_id;
	}
	public void setLeader_id(Integer leader_id) {
		this.leader_id = leader_id;
	}
	public Integer getMember_id() {
		return member_id;
	}
	public void setMember_id(Integer member_id) {
		this.member_id = member_id;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
    

}
