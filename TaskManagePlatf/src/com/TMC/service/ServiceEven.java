package com.TMC.service;

import com.TMS.model.Message;

public class ServiceEven {

	public static String SEND = "send";
	public static String RECEVICE = "recevice";
	private String type;
	private Message message;
	private long time;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Message getMessage() {
		return message;
	}
	public void setMessage(Message message) {
		this.message = message;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	
}
