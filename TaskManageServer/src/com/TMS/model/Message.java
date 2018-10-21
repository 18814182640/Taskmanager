package com.TMS.model;

import java.util.ArrayList;
import java.util.List;

public class Message  {

	public final static String LOGGIN                = "loggin";
	public final static String SIGNIN                = "signin";
	public final static String QUERY_DEPARTMENT      = "query_department";
	public final static String QUERY_ITEM            = "query_item";
	public final static String RESPONSE              = "response";
	public final static String QUERY_MY_TASK         = "query_my_task";
	public final static String QUERY_MY_AUTHOR_TASK  = "query_my_author_task";
	public final static String EDITTASK              = "edittask";
	public final static String ASKFINISH             = "askfinish";
	public final static String QUERYUSER             = "queryuser";
	public final static String CREATE_USER_TASK      = "create_user_task";
	public final static String HEARTBEAT             = "heartbeat";
	public final static String ERROR                 = "error";
	public final static String QUERY_ALL             = "query_all";
	public final static String CREATE_ITEM           = "create_item";
	public final static String EDIT_ITEM             = "edit_item";
	public final static String CREATE_DEPARTMENT     = "create_department";
	public final static String EDIT_DEPARTMENT       = "edit_department";
	public final static String EDIT_USER             = "edit_user";
	public final static String DELTE_TASK             = "delete_task";
	public final static String UPDATE_TASK             = "update_task";
//	public final static String DELETE_DEPARTMENT     = "delete_department";
//	public final static String INFORM = "inform";

	private String method;
	private ArrayList<Object> context=new ArrayList<>();//
	private boolean result = false;
	private String reason;
	private long seq;
	private long time;
	
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public ArrayList<Object> getContext() {
		return context;
	}
	public void setContext(ArrayList<Object> context) {
		this.context = context;
	}
	public boolean isResult() {
		return result;
	}
	public void setResult(boolean result) {
		this.result = result;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public long getSeq() {
		return seq;
	}
	public void setSeq(long seq) {
		this.seq = seq;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	
	
	
	
}
