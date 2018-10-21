package com.config;


public interface Config {

	final static String HOST = "127.0.0.1";//"172.30.234.83";//"172.30.234.58";//
	final static int PORT = 5690;
    
	

	public static final int TimeDaley = 10*60;
	
    public static enum ROLE{
    	USER(1,"用户"),
    	ADMIN(2,"管理员"),
    	DEVELOP(3,"超级管理员");
    	private int flag;
    	private String info;
    	private ROLE(int flag,String info) {
    		this.flag = flag;
    		this.info = info;
		}
		public int getFlag() {
			return flag;
		}
		public void setFlag(int flag) {
			this.flag = flag;
		}
		public String getInfo() {
			return info;
		}
		public void setInfo(String info) {
			this.info = info;
		}
		public static ROLE getEelenment(int flag){
			switch (flag) {
			 case 1:return USER;
			 case 2:return ADMIN;
			 case 3:return DEVELOP;
			 default:return null;
			}
		}
		
    	
    }
    
    public static enum USER_STATUS{
    	VALID(1,"有效"),
    	INVALID(0,"无效");
    	private int flag;
    	private String info;
    	private USER_STATUS(int flag,String info) {
    		this.flag = flag;
    		this.info =info;
		}
		public int getFlag() {
			return flag;
		}
		public String getInfo() {
			return info;
		}
		
		public static USER_STATUS getEelenment(int flag){
			switch (flag) {
			 case 1:return VALID;
			 case 0:return INVALID;
			 default:return null;
			}
		}
    }
    
    public static enum TASK_STATUS{
    	DOING(0,"未完成"),
    	ASKING(1,"申请完成"),
    	FINISH(2,"已完成"),
    	CLOSE(3,"关闭");
    	private int flag;
    	private String info;
    	private TASK_STATUS(int flag,String info){
    		this.flag = flag;
    		this.info = info;
    	}
		public int getFlag() {
			return flag;
		}
		public void setFlag(int flag) {
			this.flag = flag;
		}
		public String getInfo() {
			return info;
		}
		public void setInfo(String info) {
			this.info = info;
		}
		public static TASK_STATUS getEelenment(int flag){
			switch (flag) {
			 case 0:return TASK_STATUS.DOING;
			 case 1:return TASK_STATUS.ASKING;
			 case 2:return TASK_STATUS.FINISH;
			 case 3:return TASK_STATUS.CLOSE;
			 default:return null;
			}
		}
    }

}
