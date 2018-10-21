package com.TMC.Task;

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

public class QueryAllTask extends QueueRunnable{

	private long timestamp;
	private TASK_STATUS status;
	private UserInfoPojo userInfoModel;
	public QueryAllTask(UserInfoPojo userInfoModel, TASK_STATUS stauts, long timestamp,CellBack cellBack) {
		this.timestamp = timestamp;
		this.status = stauts;
		this.userInfoModel = userInfoModel;
		this.setCellBack(cellBack);
		this.setName("获取所有任务");
		this.setStep(new String[]{"登陆","获取信息","退出登录"});
		
	}

	@Override
	public void doTask() {
		try {
			Message message = new Message();
			message.setMethod(Message.QUERY_ALL);
			TaskModel taskModel = new TaskModel();
			taskModel.setAssigner(userInfoModel);
			message.getContext().add(taskModel);
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
			getCellBack().cellback(getResult());
		}
		
	}
}
