package com.TMC.Task;

import com.TMC.Thread.CellBack;
import com.TMC.Thread.QueueRunnable;
import com.TMC.Unit.ExceptionTool;
import com.TMC.service.ClientBoot;
import com.TMS.model.Message;

public class HeartBeatTask extends QueueRunnable{

	public HeartBeatTask(CellBack cellBack) {
		this.setCellBack(cellBack);
		this.setName("心跳..");
		this.setStep(new String[]{""});
	}

	@Override
	public void doTask() {
		try {
			Message message = new Message();
			message.setMethod(Message.HEARTBEAT);
			message.setContext(null);
			ClientBoot.getInstance().send(message);
			this.getResult().setSuccessful(true);
		} catch (Exception e) {
			this.getResult().setSuccessful(false);
			this.getResult().setReason(e.getMessage());
		}
		
	}
}
