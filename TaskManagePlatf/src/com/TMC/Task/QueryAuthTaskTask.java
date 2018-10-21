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
import com.TMS.entity.TaskInfoPojo;
import com.TMS.entity.UserInfoPojo;
import com.TMS.model.Message;
import com.TMS.model.TaskModel;
import com.config.Config.TASK_STATUS;

public class QueryAuthTaskTask extends QueueRunnable{

	private long timestamp;
	private TASK_STATUS status;
	private UserInfoPojo userInfoModel;
	public QueryAuthTaskTask(UserInfoPojo userInfoModel,TASK_STATUS status,long timestamp,CellBack cellBack) {
		this.timestamp = timestamp;
		this.status = status;
		this.userInfoModel = userInfoModel;
		this.setCellBack(cellBack);
		this.setName("获取我分配的任务");
		this.setStep(new String[]{"登陆","获取我分配的任务","退出登录"});
		
	}

	@Override
	public void doTask() {
		try {
//			System.err.println(Thread.currentThread().getName()+"--startime==="+System.currentTimeMillis());
			Message message = new Message();
			message.setMethod(Message.QUERY_MY_AUTHOR_TASK);
			TaskModel taskModel = new TaskModel();
			message.getContext().add(taskModel);
			taskModel.setAuthorizer(userInfoModel);
			TaskInfoPojo taskInfoPojo = new TaskInfoPojo();
			taskInfoPojo.setStatus_id(status==null?-1:status.getFlag());
			taskInfoPojo.setStart_time(timestamp);
			taskModel.setTask(taskInfoPojo);
			ClientBoot.getInstance().send(message);
			MessageHandle.addEvenListener(new ServiceListener() {
				@Override
				public void receive(ServiceEven serviceEven) {
					Message message = serviceEven.getMessage();
					if(message!=null){
						if(message.getMethod().equals(Message.RESPONSE)){
							if(message.isResult()){
								List<Object> taskModels =   message.getContext();
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
						getResult().setReason("消息体为空!!");
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
