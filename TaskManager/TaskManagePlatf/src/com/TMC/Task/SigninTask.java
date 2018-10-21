package com.TMC.Task;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.TMC.Thread.CellBack;
import com.TMC.Thread.QueueRunnable;
import com.TMC.Unit.ExceptionTool;
import com.TMC.frame.Dialog.CommonDialog;
import com.TMC.frame.Dialog.LoginDialog;
import com.TMC.frame.Dialog.SigninDialog;
import com.TMC.service.ClientBoot;
import com.TMC.service.MessageHandle;
import com.TMC.service.ServiceEven;
import com.TMC.service.ServiceListener;
import com.TMS.entity.UserInfoPojo;
import com.TMS.model.Message;
import com.TMS.model.TaskModel;

import sun.util.logging.resources.logging;

public class SigninTask extends QueueRunnable{

	private Shell shell;
	private UserInfoPojo uInfoModel;
	public SigninTask(Shell shell,CellBack cellBack) {
		this.shell = shell;
		this.setCellBack(cellBack);
		this.setName("初始化");
		this.setStep(new String[]{"获取当前登陆信息","解析信息","检查用户信息","注册用户信息","初始化完成"});
	}

	@Override
	public void doTask() {
		try {
			Runtime runtime = Runtime.getRuntime();
			Process process = runtime.exec("whoami -fqdn");
			CommonDialog.delay(500);
			InputStream inputStream = process.getInputStream();
			byte [] temp = new byte[inputStream.available()];
			inputStream.read(temp);
			String string = new String(temp, "GBK");
			String [] info = string.split(",");
			this.setNowStep(this.getNowStep()+1);
			if(info!=null){
				uInfoModel = new UserInfoPojo();
				if(info.length>0 && info[0].contains("=")){
					String  cn = info[0].split("=")[1];
					String [] user = cn.split(" +");
					if (user.length>=2) {
						uInfoModel.setReal_name(user[1].trim());
					}
				}
				if(info.length>=4 && info[3].contains("=")){
					String ou = info[3].split("=")[1];
					uInfoModel.setCompany(ou.trim());
				}
			uInfoModel.setUser_name(System.getProperty("user.name"));
			}
				
			Display.getDefault().syncExec(new Runnable() {
				@Override
				public void run() {
					uInfoModel = new SigninDialog(shell,uInfoModel).open();
				}
			});
			
			if(uInfoModel!=null){
				TaskModel taskModel = new TaskModel();
				uInfoModel.setUser_role(1);
				taskModel.setAssigner(uInfoModel);
				Message message = new Message();
				message.setMethod(Message.SIGNIN);
				message.getContext().add(taskModel);
				ClientBoot.getInstance().send(message);
				this.setNowStep(this.getNowStep()+1);
				MessageHandle.addEvenListener(new ServiceListener() {
					@Override
					public void receive(ServiceEven serviceEven) {
						Message message = serviceEven.getMessage();
						if(message!=null){
							if(message.isResult()){
								getResult().setSuccessful(true);
								getResult().setData(message);
							}else{
								getResult().setSuccessful(false);
								getResult().setReason("获取用户信息失败");
							}
							getCellBack().cellback(getResult());
							MessageHandle.removeEvenListener(this);
						}	
					}
				}.setMessage(message));
			}else{
				this.getResult().setSuccessful(false);
			}
			
			
		
				
		} catch (Exception e) {
			this.getResult().setSuccessful(false);
			this.getResult().setReason(e.getMessage());
			getCellBack().cellback(getResult());
		}
	}
}
