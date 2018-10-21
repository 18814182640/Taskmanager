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

public class CreateTaskTask extends QueueRunnable{

	private TaskModel taskModel;
	public CreateTaskTask(TaskModel taskModel,CellBack cellBack) {
		this.taskModel = taskModel;
		this.setCellBack(cellBack);
		this.setName("申请完成");
		this.setStep(new String[]{"登陆","申请完成","退出登录"});
		
	}

	@Override
	public void doTask() {
			/*DataBaseUnit.openDataBase(Config.DB_URL, Config.DB_USERNAME, Config.DB_PASSWWORD);
			DataBaseUnit.createTask(taskModel);
			List<UserInfoPojo> uPojos = DataBaseUnit.queryUser(query);
			getResult().setData(uPojos);
			DataBaseUnit.closeDataBase();*/
			
			try {
				Message message = new Message();
				message.setMethod(Message.CREATE_USER_TASK);
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
