package com.TMC.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.lang.StringUtils;

import com.TMS.model.Message;
import com.TMS.model.TaskModel;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateHandler;
import net.sf.json.JSONObject;

public class MessageHandle extends ChannelInboundHandlerAdapter {

	private static  CopyOnWriteArrayList<ServiceListener> sListeners = new CopyOnWriteArrayList<ServiceListener>(); 
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		String data = (String) msg;
		if(StringUtils.isNotEmpty(data)){
//		   System.out.println("receive"+data);
		   ServiceEven serviceEven =  handle(data);
		   inform(serviceEven);
		}
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		
	}


	
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		super.exceptionCaught(ctx, cause);
	}
	
	

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		super.userEventTriggered(ctx, evt);
	}

	private ServiceEven handle(String msg){
	    JSONObject jsonObject = JSONObject.fromObject(msg);
	    Map<String, Class> map = new HashMap<>();
	    map.put("method", String.class);
	    map.put("context", TaskModel.class);
	    map.put("result", boolean.class);
	    Message message = (Message) JSONObject.toBean(jsonObject, Message.class,map);
	    ServiceEven serviceEven = new ServiceEven();
	    serviceEven.setType(ServiceEven.RECEVICE);
	    serviceEven.setMessage(message);
	    serviceEven.setTime(System.currentTimeMillis());
	    return serviceEven;
	}
	
	synchronized private void inform(ServiceEven serviceEven){
		Iterator<ServiceListener> iterator = sListeners.iterator();
		Message r = serviceEven.getMessage();
		while (iterator.hasNext()) {
			ServiceListener serviceListener = (ServiceListener) iterator.next();
			 if(serviceListener!=null){
				 Message s = serviceListener.getMessage();
				 if( r!=null && s!=null && r.getSeq()==s.getSeq()){
//					 System.err.println(s.getMethod()+"--"+r.getSeq()+"---"+s.getSeq());
					 serviceListener.receive(serviceEven);
				 }else if(r!=null && s==null){  //广播
//					 System.err.println(r.getMethod()+"--广播");
					 serviceListener.receive(serviceEven);
				 }
			   }
		}
	}
	
	
	public static void addEvenListener(ServiceListener sListener){
		if(sListener!=null){
			sListeners.add(sListener);
		}
	}
	
	public static void removeEvenListener(ServiceListener sListener){
		if(!sListeners.isEmpty()){
			sListeners.remove(sListener);
		}
	}
	
	
	

}
