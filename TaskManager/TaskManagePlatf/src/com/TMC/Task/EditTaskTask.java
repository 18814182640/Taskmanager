package com.TMC.Task;

import java.util.ArrayList;
import java.util.List;

import com.TMC.Thread.CellBack;
import com.TMC.Thread.QueueRunnable;
import com.TMC.Unit.ExceptionTool;
import com.TMC.service.ClientBoot;
import com.TMC.service.MessageHandle;
import com.TMC.service.ServiceEven;
import com.TMC.service.ServiceListener;
import com.TMS.model.Message;
import com.TMS.model.TaskModel;
import com.config.Config;
import com.config.Config.TASK_STATUS;

public class EditTaskTask extends QueueRunnable{

	private TASK_STATUS status;
	private Config.TASK_STATUS showStatus;
	private TaskModel taskModel;
	public EditTaskTask(TaskModel taskModel,TASK_STATUS status,Config.TASK_STATUS showStatus,CellBack cellBack) {
		this.status = status;
		this.showStatus = showStatus;
		this.taskModel = taskModel;
		this.setCellBack(cellBack);
		this.setName("编辑任务");
		this.setStep(new String[]{"登陆","操作完成","退出登录"});
		
	}

	@Override
	public void doTask() {
		try {
			Message message = new Message();
			message.setMethod(Message.EDITTASK);
			taskModel.getTask().setStatus_id(status.getFlag());
			message.getContext().add(taskModel);
			ClientBoot.getInstance().send(message);
			MessageHandle.addEvenListener(new ServiceListener() {
				@Override
				public void receive(ServiceEven serviceEven) {
					Message message = serviceEven.getMessage();
					if(message!=null){
						if(message.getMethod().equals(Message.RESPONSE)){
							if(message.isResult()){
								getResult().setSuccessful(true);
							}else{
								getResult().setSuccessful(false);
								getResult().setReason(message.getReason());
							}
							getCellBack().cellback(getResult());
							MessageHandle.removeEvenListener(this);
						}
					}else{
						getResult().setSuccessful(false);
						getResult().setReason("消息体为空!");
					}
				}
			}.setMessage(message));
		
		} catch (Exception e) {
			this.getResult().setSuccessful(false);
			this.getResult().setReason(e.getMessage());
			getCellBack().cellback(getResult());
		}
		
	}
}
