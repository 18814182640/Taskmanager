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

public class QueryUserTask extends QueueRunnable{

	private String query;
	public QueryUserTask(String query,CellBack cellBack) {
		this.query = query;
		this.setCellBack(cellBack);
		this.setName("查询任务");
		this.setStep(new String[]{"登陆","操作完成","退出登录"});
		
	}

	@Override
	public void doTask() {
		try {
			Message message = new Message();
			message.setMethod(Message.QUERYUSER);
			TaskModel taskModel = new TaskModel();
		    message.getContext().add(taskModel);
			message.setReason(query);
			ClientBoot.getInstance().send(message);
			MessageHandle.addEvenListener(new ServiceListener() {
				@Override
				public void receive(ServiceEven serviceEven) {
					Message message = serviceEven.getMessage();
					if(message!=null){
						if(message.getMethod().equals(Message.RESPONSE)){
							if(message.isResult()){
								List<Object> taskModels =  message.getContext();
								getResult().setData(taskModels);
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
		}
		
	}
}
