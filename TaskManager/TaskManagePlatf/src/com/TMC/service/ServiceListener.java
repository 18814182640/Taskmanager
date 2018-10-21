package com.TMC.service;

import com.TMS.model.Message;

abstract public class ServiceListener {

	private Message message;  //发送的消息体
	abstract public void receive(ServiceEven serviceEven);
	public Message getMessage() {
		return message;
	}
	public ServiceListener setMessage(Message message) {
		this.message = message;
		return this;
	}
	
}
