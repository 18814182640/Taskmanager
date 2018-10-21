package com.TMC.Task;

import java.util.List;

import com.TMC.Thread.CellBack;
import com.TMC.Thread.QueueRunnable;
import com.TMC.Unit.ExceptionTool;
import com.TMC.service.ClientBoot;
import com.TMC.service.MessageHandle;
import com.TMC.service.ServiceEven;
import com.TMC.service.ServiceListener;
import com.TMS.model.Message;

public class QueryItemTask extends QueueRunnable{

	public QueryItemTask(CellBack cellBack) {
		this.setCellBack(cellBack);
		this.setName("获取项目");
		this.setStep(new String[]{""});
		
	}

	@Override
	public void doTask() {
		try {
			Message message = new Message();
			message.setMethod(Message.QUERY_ITEM);
			ClientBoot.getInstance().send(message);
			MessageHandle.addEvenListener(new ServiceListener() {
				@Override
				public void receive(ServiceEven serviceEven) {
					Message message = serviceEven.getMessage();
					if(message!=null){
						if(message.getMethod().equals(Message.RESPONSE)){
							if(message.isResult()){
								List<Object> list =  message.getContext();
								getResult().setData(list);
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
