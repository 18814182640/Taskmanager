package com.TMS.service;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.management.Query;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;

import com.TMS.Tool.ExceptionTool;
import com.TMS.database.DataBaseUnit;
import com.TMS.entity.DepartmentInfoPojo;
import com.TMS.entity.TaskInfoPojo;
import com.TMS.entity.UserInfoPojo;
import com.TMS.model.Message;
import com.TMS.model.TaskModel;
import com.config.Config.TASK_STATUS;
import com.sun.xml.internal.ws.util.ByteArrayBuffer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import net.sf.json.JSONObject;

public class RequestHandler extends ChannelInboundHandlerAdapter {

	private Log log;
	private static Base64 base64 = new Base64();
	public static ConcurrentHashMap<String, ChannelHandlerContext> AllLogginClient = new ConcurrentHashMap<>();
	public RequestHandler(Log log){
		this.log = log;
	}
	
	
	
	
	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		log.info("处理器载入"+ctx.channel().remoteAddress());
	}




	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		log.info("处理器移除"+ctx.channel().remoteAddress());
		Set<Entry<String, ChannelHandlerContext>> set = AllLogginClient.entrySet();
		for (Entry<String, ChannelHandlerContext> entry : set) {
			ChannelHandlerContext context = entry.getValue();
			if(context!=null && context.channel().remoteAddress().toString().equals(ctx.channel().remoteAddress().toString())){
				AllLogginClient.remove(entry.getKey());
			}
		}
	}




	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {   //接入
		log.info("接入"+ctx.channel().remoteAddress());
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {  //断开
		ctx.close();
		log.info("断开"+ctx.channel().remoteAddress());
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if(ctx.channel().isActive()){
			try {
			    Message message = getMessage(msg);  //获取信息
				HandlerMessages(ctx,message);       //处理信息
			} catch (Exception e) {
			}
		}
	}


	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//		super.exceptionCaught(ctx, cause);
		cause.printStackTrace();
		if (ctx.channel().isActive()) {
			sendErrorInfo(ctx,"服务器报了一个错误,你已被下线"+cause.getMessage());
		}else{
		}
		log.info("日志信息:"+ctx.channel().remoteAddress()+"---"+cause.getMessage());
		log.error(ExceptionTool.getExceptionMessage(cause));
//		AllLogginClient.re
//		ctx.close();
	}
	
	
	private Message getMessage(Object msg) throws Exception{
//		ByteBuf byteBuf = (ByteBuf)msg;
//		if (byteBuf.isReadable()) {
//			byte [] data = new byte[byteBuf.readableBytes()];
//			byteBuf.getBytes(0, data);
//			byte [] temp = base64.decode(data);
//			String mString = new String(data,"UTF-8");/
		String mString = (String) msg;
		if (StringUtils.isNotEmpty(mString)) {
//			System.out.println("recevice=="+mString);
			JSONObject jsonObject = JSONObject.fromObject(mString);
			Map<String, Class> map = new HashMap<>();
			map.put("method", String.class);
			map.put("context", TaskModel.class);
			map.put("result", boolean.class);
			Message message = (Message) JSONObject.toBean(jsonObject, Message.class,map);
			return message;
		}else{
			return null;
		}
	}
	
	
	private void HandlerMessages(ChannelHandlerContext ctx, Message message) {
		if (message!=null){
			try {
				switch (message.getMethod()) {
				  case Message.LOGGIN: 
					  loggin(ctx,message);
					  break;
				  case Message.SIGNIN: 
					  signin(ctx,message);
					  break;
				  case Message.QUERY_DEPARTMENT: 
					  queryDepartment(ctx,message);
					  break;
				  case Message.QUERY_ITEM: 
					  queryItem(ctx,message);
					  break;
				  case Message.QUERY_MY_TASK: 
					  queryMyTask(ctx,message);
					  break;
				  case Message.ASKFINISH: 
					  askfinish(ctx,message);
					  break;
				  case Message.QUERY_MY_AUTHOR_TASK: 
					  queryMyAuthorTask(ctx,message);
					  break;
				  case Message.EDITTASK: 
					  editTask(ctx,message);
					  break;
				  case Message.QUERYUSER: 
					  queryuser(ctx,message);
					  break;
				  case Message.CREATE_USER_TASK: 
					  createUserTask(ctx,message);
					  break;
				  case Message.HEARTBEAT: 
					  heartbeat(ctx,message);
					  break;
				  case Message.QUERY_ALL: 
					  queryAll(ctx,message);
					  break;
				  case Message.CREATE_ITEM: 
					  createItem(ctx,message);
					  break;
				  case Message.EDIT_ITEM: 
					  editItem(ctx,message);
					  break;
				  case Message.CREATE_DEPARTMENT: 
					  createDepartment(ctx,message);
					  break;
				  case Message.EDIT_DEPARTMENT: 
					  editDepartment(ctx,message);
					  break;  
				  case Message.EDIT_USER: 
					  editUser(ctx,message);
					  break;
				  case Message.DELTE_TASK: 
					  deleteTask(ctx,message);
					  break;
				  case Message.UPDATE_TASK: 
					  updateTask(ctx,message);
					  break;
				  default:
					  log.info("消息类型错误");
					  sendErrorMes(ctx,message);
					  break;
				}
				
			} catch (Exception e) {
				sendErrorInfo(ctx,"服务器报了一个错误,你已被下线"+e.getMessage());
			}
		}
		
	}
	
	private void  sendErrorMes(ChannelHandlerContext ctx,Message message) throws Exception{
		message.setMethod(Message.RESPONSE);
		message.setResult(false);
		message.setReason("服务器不能响应的非法请求!");
		sendMessage(ctx,message);
	} 







	private void  loggin(ChannelHandlerContext ctx,Message message) throws Exception{
		List<Object> taskModels =  message.getContext();
		if(taskModels!=null && taskModels.size()>0){
			TaskModel taskModel = (TaskModel) taskModels.get(0);
			if (taskModel!=null){
				UserInfoPojo assign = taskModel.getAssigner();
				if(assign!=null){
					UserInfoPojo temp = DataBaseUnit.getUser(assign);
//						taskModel.getAssigner().setId(DataBaseUnit.insertUser(assign));
					taskModel.setAssigner(temp);
					message.setResult(true);
					if(temp!=null){
						AllLogginClient.put(assign.getUser_name(), ctx);
					}
					sendMessage(ctx,message);
				}
			}
		}else{
			message.setResult(false);
			message.setReason("拒绝访问!");
			sendMessage(ctx,message);
		}
	}
	
	
	private void  signin(ChannelHandlerContext ctx,Message message) throws Exception{
		List<Object> taskModels =  message.getContext();
		if(taskModels!=null && taskModels.size()>0){
			TaskModel taskModel = (TaskModel) taskModels.get(0);
			if (taskModel!=null){
				UserInfoPojo assign = taskModel.getAssigner();
				if(assign!=null){
					if(!DataBaseUnit.hasUser(assign)){
						assign.setId(DataBaseUnit.insertUser(assign));
						AllLogginClient.put(assign.getUser_name(), ctx);
					}else{
						taskModel.setAssigner(null);
					}
					message.setResult(true);
					sendMessage(ctx,message);
				}
			}
		}else{
			message.setResult(false);
			message.setReason("拒绝访问!");
			sendMessage(ctx,message);
		}
	}
	
	private void  queryItem(ChannelHandlerContext ctx,Message message) throws Exception{
		message.setMethod(Message.RESPONSE);
		try {
			List<TaskModel> dList = DataBaseUnit.getItemInfo();
			message.getContext().clear();
			message.getContext().addAll(dList);
			message.setResult(true);
			sendMessage(ctx,message);
		} catch (Exception e) {
			message.setResult(false);
			message.setReason(e.getMessage());
			sendMessage(ctx,message);
		}
	} 
	
	private void  queryDepartment(ChannelHandlerContext ctx,Message message) throws Exception{
		message.setMethod(Message.RESPONSE);
		try {
			List<TaskModel> dList = DataBaseUnit.getDepartment();
			message.getContext().clear();
			message.getContext().addAll(dList);
			message.setResult(true);
			sendMessage(ctx,message);
		} catch (Exception e) {
			message.setResult(false);
			message.setReason(e.getMessage());
			sendMessage(ctx,message);
		}
	} 
	
	
	private void queryMyTask(ChannelHandlerContext ctx,Message message) throws UnsupportedEncodingException {
		List<Object> taskModels =  message.getContext();
		if(taskModels!=null && taskModels.size()>0){
			TaskModel taskModel = (TaskModel) taskModels.get(0);
			message.setMethod(Message.RESPONSE);
			if (taskModel!=null && isLoggin(taskModel)){
				UserInfoPojo assign = taskModel.getAssigner();
				TaskInfoPojo taskInfoPojo = taskModel.getTask();
				if(assign!=null){
					ArrayList<TaskModel> taskList;
					try {
						taskList = DataBaseUnit.getMyTask(assign,TASK_STATUS.getEelenment(taskInfoPojo.getStatus_id()), taskInfoPojo.getStart_time());
//						message.setContext(taskList);
						message.getContext().clear();
						message.getContext().addAll(taskList);
						message.setResult(true);
						sendMessage(ctx,message);
					} catch (Exception e) {
						message.setResult(false);
						message.setReason(e.getMessage());
						sendMessage(ctx,message);
//						e.printStackTrace();
					}
				}
			}else{
				message.setResult(false);
				message.setReason("未登陆，拒绝访问!");
				sendMessage(ctx,message);
			}
		}else{
			message.setResult(false);
			message.setReason("拒绝访问!");
			sendMessage(ctx,message);
		}
	}
	
	

	private void askfinish(ChannelHandlerContext ctx, Message message) throws UnsupportedEncodingException {
		List<Object> taskModels =  message.getContext();
		if(taskModels!=null && taskModels.size()>0){
			TaskModel taskModel = (TaskModel) taskModels.get(0);
			message.setMethod(Message.RESPONSE);
			if (taskModel!=null && isLoggin(taskModel)){
				try {
					DataBaseUnit.askFinish(taskModel);
					message.setResult(true);
					sendMessage(ctx,message);
					sendInformAuthor(Message.ASKFINISH, taskModel);  //通知
				} catch (Exception e) {
					message.setResult(false);
					message.setReason(e.getMessage());
					sendMessage(ctx,message);
//					e.printStackTrace();
				}
			}else{
				message.setResult(false);
				message.setReason("未登陆，拒绝访问!");
				sendMessage(ctx,message);
			}
		}else{
			message.setResult(false);
			message.setReason("拒绝访问!");
			sendMessage(ctx,message);
		}
		
	}
	
	
	private void queryMyAuthorTask(ChannelHandlerContext ctx,Message message) throws UnsupportedEncodingException {
		List<Object> taskModels =  message.getContext();
		if(taskModels!=null && taskModels.size()>0){
			TaskModel taskModel = (TaskModel) taskModels.get(0);
			message.setMethod(Message.RESPONSE);
			if (taskModel!=null || isLoggin(taskModel)){
				UserInfoPojo author = taskModel.getAuthorizer();
				TaskInfoPojo taskInfoPojo = taskModel.getTask();
				if(author!=null){
					List<TaskModel> taskList;
					try {
						taskList = DataBaseUnit.getAuthTask(author,TASK_STATUS.getEelenment(taskInfoPojo.getStatus_id()), taskInfoPojo.getStart_time());
						message.getContext().clear();
						message.getContext().addAll(taskList);
//						message.setContext(taskList);
						message.setResult(true);
						sendMessage(ctx,message);
					} catch (Exception e) {
						message.setResult(false);
						message.setReason(e.getMessage());
						sendMessage(ctx,message);
//						e.printStackTrace();
					}
				}
			}else{
				message.setResult(false);
				message.setReason("未登陆，拒绝访问!");
				sendMessage(ctx,message);
			}
		}else{
			message.setResult(false);
			message.setReason("拒绝访问!");
			sendMessage(ctx,message);
		}
	}
	
	

	private void editTask(ChannelHandlerContext ctx, Message message) throws UnsupportedEncodingException {
		List<Object> taskModels =  message.getContext();
		if(taskModels!=null && taskModels.size()>0){
			TaskModel taskModel = (TaskModel) taskModels.get(0);
			message.setMethod(Message.RESPONSE);
			if (taskModel!=null || isLoggin(taskModel)){
				UserInfoPojo author = taskModel.getAuthorizer();
				TaskInfoPojo taskInfoPojo = taskModel.getTask();
				if(author!=null){
					try {
						DataBaseUnit.editTask(taskModel, TASK_STATUS.getEelenment(taskInfoPojo.getStatus_id()));
						message.setResult(true);
						sendMessage(ctx,message);
						sendInformAssigner(Message.EDITTASK, taskModel);  //通知
					} catch (Exception e) {
						message.setResult(false);
						message.setReason(e.getMessage());
						sendMessage(ctx,message);
//						e.printStackTrace();
					}
				}
			}else{
				message.setResult(false);
				message.setReason("未登陆，拒绝访问!");
				sendMessage(ctx,message);
			}
		}else{
			message.setResult(false);
			message.setReason("拒绝访问!");
			sendMessage(ctx,message);
		}
	}

	
	private void queryuser(ChannelHandlerContext ctx, Message message) throws UnsupportedEncodingException {
		List<Object> taskModels =  message.getContext();
		if(taskModels!=null && taskModels.size()>0){
			TaskModel taskModel = (TaskModel) taskModels.get(0);
			message.setMethod(Message.RESPONSE);
			if (taskModel!=null || isLoggin(taskModel)){
				List<TaskModel> taskList;
				try {
					taskList = DataBaseUnit.queryUser(message.getReason());
//					message.setContext(taskList);
					message.getContext().clear();
					message.getContext().addAll(taskList);
					message.setResult(true);
					sendMessage(ctx,message);
				} catch (Exception e) {
					message.setResult(false);
					message.setReason(e.getMessage());
					sendMessage(ctx,message);
//					e.printStackTrace();
				}
			}else{
				message.setResult(false);
				message.setReason("未登陆，拒绝访问!");
				sendMessage(ctx,message);
			}
		}else{
			message.setResult(false);
			message.setReason("拒绝访问!");
			sendMessage(ctx,message);
		}
		
	}
	

	private void createUserTask(ChannelHandlerContext ctx, Message message) throws UnsupportedEncodingException {
		List<Object> taskModels =  message.getContext();
		if(taskModels!=null && taskModels.size()>0){
			TaskModel taskModel = (TaskModel) taskModels.get(0);
			message.setMethod(Message.RESPONSE);
			if (taskModel!=null || isLoggin(taskModel)){
				try {
				    DataBaseUnit.createTask(taskModel);
					message.setResult(true);
					sendMessage(ctx,message);
					sendInformAssigner(Message.CREATE_USER_TASK,taskModel);
				} catch (Exception e) {
					message.setResult(false);
					message.setReason(e.getMessage());
					sendMessage(ctx,message);
//					e.printStackTrace();
				}
			}else{
				message.setResult(false);
				message.setReason("未登陆，拒绝访问!");
				sendMessage(ctx,message);
			}
		}else{
			message.setResult(false);
			message.setReason("拒绝访问!");
			sendMessage(ctx,message);
		}
	}
	
	private void createItem(ChannelHandlerContext ctx, Message message) throws UnsupportedEncodingException {
		List<Object> taskModels =  message.getContext();
		if(taskModels!=null && taskModels.size()>0){
			TaskModel taskModel = (TaskModel) taskModels.get(0);
			message.setMethod(Message.RESPONSE);
			if (taskModel!=null || isLoggin(taskModel)){
				try {
				    DataBaseUnit.createItem(taskModel);
					message.setResult(true);
					sendMessage(ctx,message);
					sendNewStatus(Message.CREATE_ITEM,taskModel);
				} catch (Exception e) {
					message.setResult(false);
					message.setReason(e.getMessage());
					sendMessage(ctx,message);
//					e.printStackTrace();
				}
			}else{
				message.setResult(false);
				message.setReason("未登陆，拒绝访问!");
				sendMessage(ctx,message);
			}
		}else{
			message.setResult(false);
			message.setReason("拒绝访问!");
			sendMessage(ctx,message);
		}
	}
	
	
	private void editItem(ChannelHandlerContext ctx, Message message) throws UnsupportedEncodingException {
		List<Object> taskModels =  message.getContext();
		if(taskModels!=null && taskModels.size()>0){
			TaskModel taskModel = (TaskModel) taskModels.get(0);
			message.setMethod(Message.RESPONSE);
			if (taskModel!=null || isLoggin(taskModel)){
				try {
				    DataBaseUnit.editItem(taskModel);
					message.setResult(true);
					sendMessage(ctx,message);
					sendNewStatus(Message.EDIT_ITEM,taskModel);
				} catch (Exception e) {
					message.setResult(false);
					message.setReason(e.getMessage());
					sendMessage(ctx,message);
//					e.printStackTrace();
				}
			}else{
				message.setResult(false);
				message.setReason("未登陆，拒绝访问!");
				sendMessage(ctx,message);
			}
		}else{
			message.setResult(false);
			message.setReason("拒绝访问!");
			sendMessage(ctx,message);
		}
	}
	
	private void createDepartment(ChannelHandlerContext ctx, Message message) throws UnsupportedEncodingException {
		List<Object> taskModels =  message.getContext();
		if(taskModels!=null && taskModels.size()>0){
			TaskModel taskModel = (TaskModel) taskModels.get(0);
			message.setMethod(Message.RESPONSE);
			if (taskModel!=null || isLoggin(taskModel)){
				try {
				    DataBaseUnit.createDepartment(taskModel);
					message.setResult(true);
					sendMessage(ctx,message);
					sendNewStatus(Message.CREATE_DEPARTMENT,taskModel);
				} catch (Exception e) {
					message.setResult(false);
					message.setReason(e.getMessage());
					sendMessage(ctx,message);
//					e.printStackTrace();
				}
			}else{
				message.setResult(false);
				message.setReason("未登陆，拒绝访问!");
				sendMessage(ctx,message);
			}
		}else{
			message.setResult(false);
			message.setReason("拒绝访问!");
			sendMessage(ctx,message);
		}
	}
	
	private void editDepartment(ChannelHandlerContext ctx, Message message) throws UnsupportedEncodingException {
		List<Object> taskModels =  message.getContext();
		if(taskModels!=null && taskModels.size()>0){
			TaskModel taskModel = (TaskModel) taskModels.get(0);
			message.setMethod(Message.RESPONSE);
			if (taskModel!=null || isLoggin(taskModel)){
				try {
				    DataBaseUnit.editDepartment(taskModel);
					message.setResult(true);
					sendMessage(ctx,message);
					sendNewStatus(Message.EDIT_DEPARTMENT,taskModel);
				} catch (Exception e) {
					message.setResult(false);
					message.setReason(e.getMessage());
					sendMessage(ctx,message);
//					e.printStackTrace();
				}
			}else{
				message.setResult(false);
				message.setReason("未登陆，拒绝访问!");
				sendMessage(ctx,message);
			}
		}else{
			message.setResult(false);
			message.setReason("拒绝访问!");
			sendMessage(ctx,message);
		}
	}
	
	
	private void editUser(ChannelHandlerContext ctx, Message message) throws UnsupportedEncodingException {
		List<Object> taskModels =  message.getContext();
		if(taskModels!=null && taskModels.size()>0){
			TaskModel taskModel = (TaskModel) taskModels.get(0);
			message.setMethod(Message.RESPONSE);
			if (taskModel!=null || isLoggin(taskModel)){
				try {
				    DataBaseUnit.editUser(taskModel);
					message.setResult(true);
					sendMessage(ctx,message);
				} catch (Exception e) {
					message.setResult(false);
					message.setReason(e.getMessage());
					sendMessage(ctx,message);
//					e.printStackTrace();
				}
			}else{
				message.setResult(false);
				message.setReason("未登陆，拒绝访问!");
				sendMessage(ctx,message);
			}
		}else{
			message.setResult(false);
			message.setReason("拒绝访问!");
			sendMessage(ctx,message);
		}
	}
	
	
	private void heartbeat(ChannelHandlerContext ctx, Message message) throws Exception {
		message.setResult(true);
		sendMessage(ctx,message);
		
	}
	
	private void queryAll(ChannelHandlerContext ctx,Message message) throws UnsupportedEncodingException {
		List<Object> taskModels =  message.getContext();
		if(taskModels!=null && taskModels.size()>0){
			TaskModel taskModel = (TaskModel) taskModels.get(0);
			message.setMethod(Message.RESPONSE);
			if (taskModel!=null && isLoggin(taskModel)){
				UserInfoPojo assign = taskModel.getAssigner();
				TaskInfoPojo taskInfoPojo = taskModel.getTask();
				if(assign!=null){
					ArrayList<TaskModel> taskList;
					try {
						taskList = DataBaseUnit.getAllTaskInfo(assign,TASK_STATUS.getEelenment(taskInfoPojo.getStatus_id()), taskInfoPojo.getStart_time());
//						message.setContext(taskList);
						message.getContext().clear();
						message.getContext().addAll(taskList);
						message.setResult(true);
						sendMessage(ctx,message);
					} catch (Exception e) {
						message.setResult(false);
						message.setReason(e.getMessage());
						sendMessage(ctx,message);
//						e.printStackTrace();
					}
				}
			}else{
				message.setResult(false);
				message.setReason("未登陆，拒绝访问!");
				sendMessage(ctx,message);
			}
		}else{
			message.setResult(false);
			message.setReason("拒绝访问!");
			sendMessage(ctx,message);
		}
	}
	
	
	private void deleteTask(ChannelHandlerContext ctx, Message message) throws UnsupportedEncodingException {
		List<Object> taskModels =  message.getContext();
		if(taskModels!=null && taskModels.size()>0){
			TaskModel taskModel = (TaskModel) taskModels.get(0);
			message.setMethod(Message.RESPONSE);
			if (taskModel!=null || isLoggin(taskModel)){
				try {
					DataBaseUnit.deleteTask(taskModel);
					message.setResult(true);
					sendMessage(ctx,message);
					sendInformAssigner(Message.DELTE_TASK, taskModel);  //通知
				} catch (Exception e) {
					message.setResult(false);
					message.setReason(e.getMessage());
					sendMessage(ctx,message);
//						e.printStackTrace();
				}
			}else{
				message.setResult(false);
				message.setReason("未登陆，拒绝访问!");
				sendMessage(ctx,message);
			}
		}else{
			message.setResult(false);
			message.setReason("拒绝访问!");
			sendMessage(ctx,message);
		}
	}
	
	
	private void updateTask(ChannelHandlerContext ctx, Message message) throws UnsupportedEncodingException {
		List<Object> taskModels =  message.getContext();
		if(taskModels!=null && taskModels.size()>0){
			TaskModel taskModel = (TaskModel) taskModels.get(0);
			message.setMethod(Message.RESPONSE);
			if (taskModel!=null || isLoggin(taskModel)){
				try {
					DataBaseUnit.updateTask(taskModel);
					message.setResult(true);
					sendMessage(ctx,message);
					sendInformAssigner(Message.DELTE_TASK, taskModel);  //通知
				} catch (Exception e) {
					message.setResult(false);
					message.setReason(e.getMessage());
					sendMessage(ctx,message);
//						e.printStackTrace();
				}
			}else{
				message.setResult(false);
				message.setReason("未登陆，拒绝访问!");
				sendMessage(ctx,message);
			}
		}else{
			message.setResult(false);
			message.setReason("拒绝访问!");
			sendMessage(ctx,message);
		}
	}
	
	
	private boolean isLoggin(TaskModel taskModel){
		UserInfoPojo assign = taskModel.getAssigner();
		if(assign!=null){
			return AllLogginClient.containsKey(assign.getUser_name());
		}else{
			return false;
		}
	}
	
	 private void sendMessage(ChannelHandlerContext ctx,Message message) throws UnsupportedEncodingException{
		if (message!=null) {
			JSONObject jsonObject = JSONObject.fromObject(message);
//			System.err.println("send=="+jsonObject.toString());
			byte [] data = base64.encode(jsonObject.toString().getBytes("UTF-8"));
			byte [] end = "\r\n".getBytes();
			ByteBuf byteBuf = Unpooled.directBuffer(data.length+end.length);
			byteBuf.writeBytes(data);
			byteBuf.writeBytes(end);
			ctx.writeAndFlush(byteBuf);
		}
	}
	
	
	private void sendInformAuthor(String string, TaskModel taskModel){
		String key = taskModel.getAuthorizer().getUser_name();
		ChannelHandlerContext ctx = AllLogginClient.get(key);
		if(ctx!=null && ctx.channel().isActive()){
			Message message = new Message();
			message.setMethod(string);
			message.getContext().add(taskModel);
			try {
				sendMessage(ctx,message);
			} catch (UnsupportedEncodingException e) {
//				e.printStackTrace();
			}
		}
	}
	private void sendInformAssigner(String string, TaskModel taskModel){
		String key = taskModel.getAssigner().getUser_name();
		ChannelHandlerContext ctx = AllLogginClient.get(key);
		if(ctx!=null && ctx.channel().isActive()){
			Message message = new Message();
			message.setMethod(string);
			message.getContext().add(taskModel);
			try {
				sendMessage(ctx,message);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void sendNewStatus(String string,TaskModel taskModel){
		Set<Entry<String, ChannelHandlerContext>> set =  AllLogginClient.entrySet();
		for (Entry<String, ChannelHandlerContext> entry : set) {
			ChannelHandlerContext ctx = entry.getValue();
			if(ctx!=null && ctx.channel().isActive()){
				Message message = new Message();
				message.setMethod(string);
				message.getContext().add(taskModel);
				try {
					sendMessage(ctx,message);
				} catch (UnsupportedEncodingException e) {
//					e.printStackTrace();
				}
			}
		}
	}
	
	private void sendErrorInfo(ChannelHandlerContext ctx,String info){
		Message message = new Message();
		message.setMethod(Message.ERROR);
		message.setReason(info);
		try {
			sendMessage(ctx,message);
		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
		}
	}
	
	
	
	
	
	
	

}
