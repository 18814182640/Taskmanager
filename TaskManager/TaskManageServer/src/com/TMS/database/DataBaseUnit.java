package com.TMS.database;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.TMS.Tool.DateUnit;
import com.TMS.entity.DepartmentInfoPojo;
import com.TMS.entity.ItemInfoPojo;
import com.TMS.entity.TaskInfoPojo;
import com.TMS.entity.UserInfoPojo;
import com.TMS.model.TaskModel;
import com.config.Config.TASK_STATUS;
import com.mchange.v2.c3p0.ComboPooledDataSource;




public class DataBaseUnit {                         //数据库连接工具类
	
	private final static String selectUser = "select * from user_info a where a.user_name=? and a.user_password=?";
	private final static String hasUser = "select * from user_info a where  a.user_name=?";
	private final static String selsectMaxID = "select max(id) from user_info";
	private final static String insertUserSQL = "insert into user_info(id,user_name,user_password,user_email,phone_number,company,real_name,user_role,department_id,user_status,save_time ) values(?,?,?,?,?,?,?,?,?,?,now())";
	private final static String GET_MY_TASK_SQL = "select  * from (select * from task_info a where a.assign_id=(select id from user_info a where a.user_name=?  and a.real_name=?) and a.status_id=0  and a.start_time>=?)b left join user_info c on b.auth_id=c.id ORDER BY b.save_time ";
	private final static String GET_DEPARTMENT = "select * from department_info a where a.status=1";
	private final static String GET_ITEM = "select * from item_info a where a.status = 1";
	private static ComboPooledDataSource pooledDataSource;
	static{
		pooledDataSource = new ComboPooledDataSource();
	}
	
	public static void init(){
	}
	
	public static void closeDataBase(){
		/*if (con!=null) {
			try {
				con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}*/
	}
	
	
	public static Connection getConnection(){
	    try{
	    	/*Class.forName("com.mysql.jdbc.Driver");
	    	con=DriverManager.getConnection(url,user,password);*/
//	    	ComboPooledDataSource pooledDataSource = new ComboPooledDataSource();
	    	return pooledDataSource.getConnection();
	    }catch(Exception e){
	    	e.printStackTrace();
	    	return null;
	    }
	}
	
	

  public static UserInfoPojo getUser(UserInfoPojo userInfoModel) throws SQLException{
	  Connection con = null ;
		try {
			con =getConnection();
			if(userInfoModel!=null && con!=null && !con.isClosed()){
				PreparedStatement pStatement = con.prepareStatement(selectUser);
				pStatement.setString(1, userInfoModel.getUser_name());
				pStatement.setString(2, userInfoModel.getUser_password());
				ResultSet rSet = pStatement.executeQuery();
				UserInfoPojo uInfoPojo = null;
				while (rSet.next()) {
					 uInfoPojo = new UserInfoPojo();
					 uInfoPojo.setId(rSet.getInt("id"));
					 uInfoPojo.setUser_name(rSet.getString("user_name"));
					 uInfoPojo.setUser_password(rSet.getString("user_password"));
					 uInfoPojo.setReal_name(rSet.getString("real_name"));
					 uInfoPojo.setDepartment(rSet.getInt("department_id"));
					 uInfoPojo.setUser_role(rSet.getInt("user_role"));
					 uInfoPojo.setCompany(rSet.getString("company"));
					 uInfoPojo.setUser_status(rSet.getInt("user_status"));
					 
				}
				rSet.close();
				pStatement.close();
				con.close();
				return uInfoPojo;
			}else{
				throw new RuntimeException("查询条件不满足!");
			}
		} catch (SQLException e) {
			if(con!=null){
				con.close();
			}
			e.printStackTrace();
			throw e;
		}
  }
	
  public static boolean hasUser(UserInfoPojo userInfoModel) throws SQLException{
	  Connection con = null ;
		try {
			con =getConnection();
			if(userInfoModel!=null && con!=null && !con.isClosed()){
				PreparedStatement pStatement = con.prepareStatement(hasUser);
				pStatement.setString(1, userInfoModel.getUser_name());
				ResultSet rSet = pStatement.executeQuery();
				if(rSet.first()){
					rSet.close();
					pStatement.close();
					con.close();
					return true;
				}else{
					rSet.close();
					pStatement.close();
					con.close();
					return false;
				}
			}else{
				throw new RuntimeException("查询条件不满足!");
			}
		} catch (SQLException e) {
			if(con!=null){
				con.close();
			}
			e.printStackTrace();
			throw e;
		}
  }
  
  public static ArrayList<TaskModel> getDepartment() throws SQLException{
	  Connection con = null ;
		try {
			con =getConnection();
			if(con!=null && !con.isClosed()){
				PreparedStatement pStatement = con.prepareStatement(GET_DEPARTMENT);
				ResultSet rSet = pStatement.executeQuery();
				ArrayList<TaskModel> dList = new ArrayList<>();
				while (rSet.next()) {
					TaskModel taskModel = new TaskModel();
					 DepartmentInfoPojo temp  = new DepartmentInfoPojo();
					 temp.setId(rSet.getInt("id"));
					 temp.setDepartment(rSet.getString("department"));
					 temp.setExplain(rSet.getString("explain"));
					 temp.setLeader_id(rSet.getInt("leader_id"));
					 temp.setMember_id(rSet.getInt("member_id"));
					 temp.setStatus(rSet.getInt("status"));
					 taskModel.setDepartment(temp);
					 dList.add(taskModel);
				}
				rSet.close();
				pStatement.close();
				con.close();
				return dList;
			}else{
				throw new RuntimeException("查询条件不满足!");
			}
		} catch (SQLException e) {
			if(con!=null){
				con.close();
			}
			e.printStackTrace();
			throw e;
		}
  }
  
  public static ArrayList<TaskModel> getItemInfo() throws SQLException{
	  Connection con = null ;
		try {
			con =getConnection();
			if(con!=null && !con.isClosed()){
				PreparedStatement pStatement = con.prepareStatement(GET_ITEM);
				ResultSet rSet = pStatement.executeQuery();
				ArrayList<TaskModel> dList = new ArrayList<>();
				while (rSet.next()) {
					TaskModel taskModel = new TaskModel();
					ItemInfoPojo temp  = new ItemInfoPojo();
					 temp.setId(rSet.getInt("id"));
					 temp.setItemName(rSet.getString("item_name"));
					 temp.setExplain(rSet.getString("explain"));
					 temp.setStatus(rSet.getInt("status"));
					 temp.setDepartment_id(rSet.getInt("department_id"));
					 taskModel.setItemInfoPojo(temp);
					 dList.add(taskModel);
				}
				rSet.close();
				pStatement.close();
				con.close();
				return dList;
			}else{
				throw new RuntimeException("查询条件不满足!");
			}
		} catch (SQLException e) {
			if(con!=null){
				con.close();
			}
			e.printStackTrace();
			throw e;
		}
  }
  
  
  public static int insertUser(UserInfoPojo userInfoModel) throws SQLException{
	  Connection con = null;
	  int index = 0;
		try {
			con =getConnection();
			if(userInfoModel!=null && con!=null && !con.isClosed()){
				int max = 0;
				PreparedStatement pStatement = con.prepareStatement(selsectMaxID);
				ResultSet rSet = pStatement.executeQuery();
				if(rSet.first()){
					max = rSet.getInt(1);
				}
				rSet.close();
				pStatement.close();
				pStatement = con.prepareStatement(insertUserSQL);
				index = max+1;
				pStatement.setInt(1,index);
				pStatement.setString(2, userInfoModel.getUser_name());
				pStatement.setString(3, userInfoModel.getUser_password());
				pStatement.setString(4, userInfoModel.getUser_mail());
				pStatement.setString(5, userInfoModel.getPhone_number());
				pStatement.setString(6, userInfoModel.getCompany());
				pStatement.setString(7, userInfoModel.getReal_name());
				pStatement.setInt(8, userInfoModel.getUser_role());
				pStatement.setInt(9, userInfoModel.getDepartment());
				pStatement.setInt(10, 1);
				pStatement.execute();
			}else{
				if(con!=null){
					con.close();
				}
				throw new RuntimeException("查询条件不满足!");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			if(con!=null){
				con.close();
			}
			throw e;
		}
		return index;
  }
  
  
  
  public static ArrayList<TaskModel> getMyTask(UserInfoPojo userInfoModel, TASK_STATUS status, long timestamp) throws Exception{
	  Connection con = null;
	  try {
		  StringBuffer sql =  new StringBuffer();
		  sql.append("select   b.indexflag,b.status_id,b.task_info,b.start_time,b.end_time,b.save_time,b.svn_path,b.item_id,c.user_name,c.real_name,c.department_id  from (select * from task_info a where 1=1 ");
		  if(userInfoModel!=null){
			  sql.append(" and a.assign_id=(select id from user_info a where a.user_name='")
			     .append(userInfoModel.getUser_name()).append("'  and a.real_name= '").append(userInfoModel.getReal_name()).append("')");
		  }
		  if(status!=null){
			  sql.append(" and a.status_id=").append(status.getFlag());
		  }
		  sql.append(" and a.start_time>='").append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Timestamp(timestamp))).append("'");
		  sql.append(")b left join user_info c on b.auth_id=c.id ORDER BY b.start_time");
		  con =getConnection();
		  if(con!=null){
			  ArrayList<TaskModel> tasks = new ArrayList<>();
			PreparedStatement pStatement = con.prepareStatement(sql.toString());
			ResultSet rSet = pStatement.executeQuery();
			while (rSet.next()) {
				TaskModel taskModel = new TaskModel();
				TaskInfoPojo taskInfoPojo = new TaskInfoPojo();
				taskInfoPojo.setIndexflag(rSet.getInt("indexflag"));
				taskInfoPojo.setStatus_id(rSet.getInt("status_id"));
				Blob taskinfo = rSet.getBlob("task_info");
				if(taskinfo!=null){
					taskInfoPojo.setTask_info(new String(taskinfo.getBytes((long)1,(int) taskinfo.length()),"UTF-8"));
				}
				
//				System.err.println("taskinfo:"+new String(taskinfo.getBytes((long)1,(int) taskinfo.length()),"UTF-8"));
				taskInfoPojo.setStart_time(rSet.getTimestamp("start_time").getTime());
				taskInfoPojo.setEnd_time(rSet.getTimestamp("end_time").getTime());
				taskInfoPojo.setSave_time(rSet.getTimestamp("save_time").getTime());
				Blob svn = rSet.getBlob("svn_path");
				if(svn!=null){
					taskInfoPojo.setSvn_path(new String(svn.getBytes((long)1,(int) svn.length()),"UTF-8"));
				}
				taskInfoPojo.setItem_id(rSet.getInt("item_id"));
				taskModel.setTask(taskInfoPojo);
				taskModel.setAssigner(userInfoModel);   //发起人
				UserInfoPojo authorize = new UserInfoPojo();
				authorize.setUser_name(rSet.getString("user_name"));
				authorize.setReal_name(rSet.getString("real_name"));
				authorize.setDepartment(rSet.getInt("department_id"));
				taskModel.setAuthorizer(authorize);
				tasks.add(taskModel);
			}
		    con.close();
			return tasks;
		  }else{
				throw new RuntimeException("查询条件不满足!");
		  }
	 } catch (Exception e) {
		 if(con!=null){
		  con.close();
		 }
		 e.printStackTrace();
		   throw e;
	 }
  }
  
  public static  ArrayList<TaskModel> getAuthTask(UserInfoPojo userInfoModel, TASK_STATUS status, long date) throws Exception{
	  Connection con = null;
	  try {
		  StringBuffer sql =  new StringBuffer();
		  sql.append("select * from task_info a  left join user_info b on a.assign_id = b.id  where 1=1 ");
		  if(userInfoModel!=null){
			  sql.append(" and a.auth_id=(select id from user_info  where user_name ='")
			     .append(userInfoModel.getUser_name()).append("'  and real_name= '").append(userInfoModel.getReal_name()).append("')");
		  }
		  if(status!=null){
			  sql.append(" and a.status_id= ").append(status.getFlag());
		  }
		  sql.append(" and a.start_time>='").append(new SimpleDateFormat("yyyy-MM-dd").format(new Timestamp(date))).append("'");
//		  System.err.println(sql.toString());
		  con =getConnection();
		  if(con!=null){
			  ArrayList<TaskModel> tasks = new ArrayList<>();
			PreparedStatement pStatement = con.prepareStatement(sql.toString());
			ResultSet rSet = pStatement.executeQuery();
			while (rSet.next()) {
				TaskModel taskModel = new TaskModel();
				TaskInfoPojo taskInfoPojo = new TaskInfoPojo();
				taskInfoPojo.setIndexflag(rSet.getInt("indexflag"));
				taskInfoPojo.setStatus_id(rSet.getInt("status_id"));
				Blob blob = rSet.getBlob("task_info");
				if(blob!=null){
					taskInfoPojo.setTask_info(new String(blob.getBytes((long)1,(int) blob.length()),"UTF-8"));
				}
				taskInfoPojo.setStart_time(rSet.getTimestamp("start_time").getTime());
				taskInfoPojo.setEnd_time(rSet.getTimestamp("end_time").getTime());
				taskInfoPojo.setSave_time(rSet.getTimestamp("save_time").getTime());
				Blob svn = rSet.getBlob("svn_path");
				if(svn!=null){
					taskInfoPojo.setSvn_path(new String(svn.getBytes((long)1,(int) svn.length()),"UTF-8"));
				}
				taskInfoPojo.setItem_id(rSet.getInt("item_id"));
				taskModel.setTask(taskInfoPojo);
				taskModel.setAuthorizer(userInfoModel);   //发起人
				UserInfoPojo assigner = new UserInfoPojo();
				assigner.setUser_name(rSet.getString("user_name"));
				assigner.setReal_name(rSet.getString("real_name"));
				assigner.setDepartment(rSet.getInt("department_id"));
				taskModel.setAssigner(assigner);
				tasks.add(taskModel);
//				System.err.println(taskModel.getTask().getTask_info()+"---"+taskModel.getTask().getSvn_path());
			}
		    con.close();
			return tasks;
		  }else{
				throw new RuntimeException("查询条件不满足!");
		  }
	 } catch (Exception e) {
		 if(con!=null){
			  con.close();
		 }
		 e.printStackTrace();
		   throw e;
	 }
  }
	
  
  public static void askFinish(TaskModel taskModel) throws Exception{
	  Connection con = null;
	  try {
		  TaskInfoPojo taskInfoPojo = taskModel.getTask();
		  if(taskModel!=null && taskInfoPojo!=null){
			  StringBuffer sql = new StringBuffer();
			  sql.append("update task_info  set status_id=1,save_time=now(),svn_path='").append(taskInfoPojo.getSvn_path()).append("' where indexflag=").append(taskInfoPojo.getIndexflag());
			  con =getConnection();
			  PreparedStatement pStatement = con.prepareStatement(sql.toString());
			  pStatement.execute();
			  if(con!=null){
				  con.close();
			 }
		  }else{
			  throw new RuntimeException("查询条件不满足!");
		  }
	} catch (Exception e) {
		if(con!=null){
			  con.close();
		 }
		throw e;
	}
  }


public static void editTask(TaskModel taskModel, TASK_STATUS status) throws Exception {
	Connection con = null;
	 try {
		  if(taskModel!=null && taskModel.getTask()!=null){
			  StringBuffer sql = new StringBuffer();
			  sql.append("update task_info  set status_id=").append(status.getFlag()).append(",save_time=now() where indexflag=").append(taskModel.getTask().getIndexflag());
			  con =getConnection();
			  PreparedStatement pStatement = con.prepareStatement(sql.toString());
			  pStatement.execute();
			  if(con!=null){
				  con.close();
			 }
		  }else{
			  throw new RuntimeException("查询条件不满足!");
		  }
	} catch (Exception e) {
		if(con!=null){
			  con.close();
		 }
		throw e;
	}
	
}

 
public static ArrayList<TaskModel> queryUser(String query) throws Exception {
	Connection con = null;
	 try {
		 StringBuffer sql = new StringBuffer();
//		 sql.append("select * ,COUNT(b.indexflag) as cou from user_info a  left join task_info b on a.id = b.assign_id    where b.status_id=0 and a.user_name like '%").append(query).append("%' or a.real_name like '%").append(query).append("%'  or a.department_id like  '%").append(query).append("%'    group by a.id   ");
		 sql.append("select * ,count(b.indexflag) as cou from (select u.* ,d.department from user_info u left join department_info d on u.department_id = d.id  ").append(" where  u.user_role<>3 and (u.user_name like '%").append(query).append("%' or u.real_name like '%").append(query).append("%'  or d.department like  '%").append(query).append("%')) a  left join (select *   from task_info c where c.status_id=0 ) b on a.id = b.assign_id  group by a.id");
		 con =getConnection();
//		 System.err.println(sql.toString());
		 PreparedStatement pStatement = con.prepareStatement(sql.toString());
		 ResultSet rSet = pStatement.executeQuery();
		 ArrayList<TaskModel> taskModels = new ArrayList<>();
		 while (rSet.next()) {
			 UserInfoPojo uInfoPojo = new UserInfoPojo();
			 uInfoPojo.setId(rSet.getInt("id"));
			 uInfoPojo.setUser_name(rSet.getString("user_name"));
			 uInfoPojo.setUser_password(rSet.getString("user_password"));
			 uInfoPojo.setUser_mail(rSet.getString("user_email"));
			 uInfoPojo.setPhone_number(rSet.getString("phone_number"));
			 uInfoPojo.setReal_name(rSet.getString("real_name"));
			 uInfoPojo.setDepartment(rSet.getInt("department_id"));
			 uInfoPojo.setCompany(rSet.getString("company"));
			 uInfoPojo.setUser_role(rSet.getInt("user_role"));
			 uInfoPojo.setUser_status(rSet.getInt("user_status"));
			 uInfoPojo.setTaskNum(rSet.getInt("cou"));
			 TaskModel taskModel = new TaskModel();
			 taskModel.setAssigner(uInfoPojo);
			 taskModels.add(taskModel);
		 }
		 con.close();
		 return taskModels;
	} catch (Exception e) {
		e.printStackTrace();
		if(con!=null){
			  con.close();
		 }
		throw e;
	}
	
}


public static void createTask(TaskModel taskModel) throws Exception {
	Connection con = null;
	 try {
		  if(taskModel!=null){
			  int auth_id = 0;
			  int assign_id =taskModel.getAssigner().getId();
			  int indexflag =0;
			  StringBuffer sql = new StringBuffer();
			  con =getConnection();
			  sql.append("select id from user_info  where user_name ='")
			     .append(taskModel.getAuthorizer().getUser_name()).append("'  and real_name= '").append(taskModel.getAuthorizer().getReal_name()).append("'");
			  PreparedStatement pStatement = con.prepareStatement(sql.toString());
			  ResultSet rSet = pStatement.executeQuery();
			  if(rSet.first()){
				  auth_id = rSet.getInt(1);
			  }else{
				  throw new RuntimeException("获取"+taskModel.getAuthorizer().getReal_name()+"信息失败!");
			  }
			  pStatement = con.prepareStatement("select max(indexflag) from task_info");
			  rSet = pStatement.executeQuery();
			  if(rSet.first()){
				  indexflag = rSet.getInt(1);
			  }else{
				  throw new RuntimeException("获取任务信息失败!");
			  }
			  
			  indexflag=indexflag+1;
			  sql.setLength(0);
			  sql.append("insert into task_info(indexflag,auth_id,assign_id,status_id,task_info,start_time,end_time,save_time,item_id)  values(?,?,?,?,?,?,?,now(),?)");
			  pStatement = con.prepareStatement(sql.toString());
			  pStatement.setInt(1, indexflag);
			  pStatement.setInt(2, auth_id);
			  pStatement.setInt(3, assign_id);
			  pStatement.setInt(4, 0);
			  pStatement.setString(5, taskModel.getTask().getTask_info());
			  pStatement.setTimestamp(6, new Timestamp(taskModel.getTask().getStart_time()));
			  pStatement.setTimestamp(7, new Timestamp(taskModel.getTask().getEnd_time()));
			  pStatement.setInt(8, taskModel.getTask().getItem_id());
//			  System.out.println(new Timestamp(taskModel.getTask().getStart_time()).getDay()+"---" + new Timestamp(taskModel.getTask().getEnd_time()).getDay());
//			  System.err.println(new Date(taskModel.getTask().getStart_time()).getDay()+"--"+new Date(taskModel.getTask().getEnd_time()).getDay());
			  pStatement.execute();
			  con.close();
			  
		  }else{
			  throw new RuntimeException("查询条件不满足!");
		  }
	} catch (Exception e) {
		if(con!=null){
			  con.close();
		 }
		e.printStackTrace();
		throw e;
	}
	
}


public static void createItem(TaskModel taskModel) throws Exception {
	Connection con = null;
	 try {
		  if(taskModel!=null){
			  ItemInfoPojo infoPojo = taskModel.getItemInfoPojo();
			  if(infoPojo!=null){
				  con =getConnection();
				  StringBuffer sql = new StringBuffer();
				  sql.append("select * from item_info a where a.status=1 and a.item_name='").append(infoPojo.getItemName().trim()).append("'");
				  PreparedStatement  pStatement = con.prepareStatement(sql.toString());
				  ResultSet rSet = pStatement.executeQuery();
				  if(rSet.first()){
					  throw new RuntimeException("已存在该项目!");
				  }
				  sql.setLength(0);
				  sql.append("insert into  item_info values(ifnull((select max(a.id)+1 from item_info a),1),?,?,1,?)");
				  pStatement = con.prepareStatement(sql.toString());
				  pStatement.setString(1, infoPojo.getItemName());
				  pStatement.setString(2, infoPojo.getExplain());
				  pStatement.setInt(3, infoPojo.getDepartment_id());
				  pStatement.execute();
				  con.close();
			  }else{
				  throw new RuntimeException("添加失败!");
			  }
			  
		  }else{
			  throw new RuntimeException("添加失败!");
		  }
	} catch (Exception e) {
		if(con!=null){
			  con.close();
		 }
		e.printStackTrace();
		throw e;
	}
	
}


public static void editItem(TaskModel taskModel) throws Exception {
	Connection con = null;
	 try {
		  if(taskModel!=null){
			  ItemInfoPojo infoPojo = taskModel.getItemInfoPojo();
			  if(infoPojo!=null){
				  con =getConnection();
				  StringBuffer sql = new StringBuffer();
				  sql.append("select * from item_info a where a.id=").append(infoPojo.getId());
				  PreparedStatement  pStatement = con.prepareStatement(sql.toString());
				  ResultSet rSet = pStatement.executeQuery();
				  if(!rSet.first()){
					  throw new RuntimeException("不存在该项目!");
				  }
				  sql.setLength(0);
				  sql.append("update item_info  a set a.item_name=?,a.explain=?,a.status=?,a.department_id=? where a.id=").append(infoPojo.getId());
				  pStatement = con.prepareStatement(sql.toString());
				  pStatement.setString(1, infoPojo.getItemName());
				  pStatement.setString(2, infoPojo.getExplain());
				  pStatement.setInt(3, infoPojo.getStatus());
				  pStatement.setInt(4, infoPojo.getDepartment_id());
				  pStatement.execute();
				  con.close();
			  }else{
				  throw new RuntimeException("操作失败!");
			  }
			  
		  }else{
			  throw new RuntimeException("操作失败!");
		  }
	} catch (Exception e) {
		if(con!=null){
			  con.close();
		 }
		e.printStackTrace();
		throw e;
	}
	
}


public static void createDepartment(TaskModel taskModel) throws Exception {
	Connection con = null;
	 try {
		  if(taskModel!=null){
			  DepartmentInfoPojo departmentInfoPojo = taskModel.getDepartment();
			  if(departmentInfoPojo!=null){
				  con =getConnection();
				  StringBuffer sql = new StringBuffer();
				  sql.append("select * from department_info a where a.department='").append(departmentInfoPojo.getDepartment().trim()).append("'");
				  PreparedStatement  pStatement = con.prepareStatement(sql.toString());
				  ResultSet rSet = pStatement.executeQuery();
				  if(rSet.first()){
					  throw new RuntimeException("已存在该项目!");
				  }
				  sql.setLength(0);
				  sql.append("insert into department_info(id,department,`explain`,status)  values(ifnull((select max(b.id)+1 from department_info b ),1),?,?,1)");
				  pStatement = con.prepareStatement(sql.toString());
				  pStatement.setString(1,departmentInfoPojo.getDepartment());
				  pStatement.setString(2,departmentInfoPojo.getExplain());
				  pStatement.execute();
				  con.close();
			  }else{
				  throw new RuntimeException("添加失败!");
			  }
			  
		  }else{
			  throw new RuntimeException("添加失败!");
		  }
	} catch (Exception e) {
		if(con!=null){
			  con.close();
		 }
		e.printStackTrace();
		throw e;
	}
	
}


public static void editDepartment(TaskModel taskModel) throws Exception {
	Connection con = null;
	 try {
		  if(taskModel!=null){
			  DepartmentInfoPojo departmentInfoPojo = taskModel.getDepartment();
			  if(departmentInfoPojo!=null){
				  con =getConnection();
				  StringBuffer sql = new StringBuffer();
				  sql.append("select * from department_info a where a.status=1 and a.id=").append(departmentInfoPojo.getId());
				  PreparedStatement  pStatement = con.prepareStatement(sql.toString());
				  ResultSet rSet = pStatement.executeQuery();
				  if(!rSet.first()){
					  throw new RuntimeException("不存在该部门!");
				  }
				  sql.setLength(0);
				  sql.append("update department_info  a set a.department=?,a.explain=?,a.status=?  where a.id=").append(departmentInfoPojo.getId());
				  pStatement = con.prepareStatement(sql.toString());
				  pStatement.setString(1, departmentInfoPojo.getDepartment());
				  pStatement.setString(2, departmentInfoPojo.getExplain());
				  pStatement.setInt(3, departmentInfoPojo.getStatus());
				  pStatement.execute();
				  con.close();
			  }else{
				  throw new RuntimeException("操作失败!");
			  }
			  
		  }else{
			  throw new RuntimeException("操作失败!");
		  }
	} catch (Exception e) {
		if(con!=null){
			  con.close();
		 }
		e.printStackTrace();
		throw e;
	}
	
}

public static void editUser(TaskModel taskModel) throws Exception {
	Connection con = null;
	 try {
		  if(taskModel!=null){
			  UserInfoPojo userInfoPojo = taskModel.getAssigner();
			  if(userInfoPojo!=null){
				  con =getConnection();
				  StringBuffer sql = new StringBuffer();
				  sql.append("select * from user_info a where a.user_status=1 and a.id=").append(userInfoPojo.getId());
				  PreparedStatement  pStatement = con.prepareStatement(sql.toString());
				  ResultSet rSet = pStatement.executeQuery();
				  if(!rSet.first()){
					  throw new RuntimeException("不存在该用户!");
				  }
				  sql.setLength(0);
				  sql.append("update user_info  a set a.user_password=?,a.user_email=?,a.phone_number=?,a.real_name=?,a.user_role=?,a.department_id=?,a.user_status=?  where a.id=").append(userInfoPojo.getId());
				  pStatement = con.prepareStatement(sql.toString());
				  pStatement.setString(1, userInfoPojo.getUser_password());
				  pStatement.setString(2, userInfoPojo.getUser_mail());
				  pStatement.setString(3, userInfoPojo.getPhone_number());
				  pStatement.setString(4, userInfoPojo.getReal_name());
				  pStatement.setInt(5, userInfoPojo.getUser_role());
				  pStatement.setInt(6, userInfoPojo.getDepartment());
				  pStatement.setInt(7, userInfoPojo.getUser_status());
				  pStatement.execute();
				  con.close();
			  }else{
				  throw new RuntimeException("操作失败!");
			  }
			  
		  }else{
			  throw new RuntimeException("操作失败!");
		  }
	} catch (Exception e) {
		if(con!=null){
			  con.close();
		 }
		e.printStackTrace();
		throw e;
	}
	
}


public static ArrayList<TaskModel> fixedTimeGetTask(UserInfoPojo userInfoModel,int time) throws Exception{
	  try {
		  StringBuffer sql =  new StringBuffer();
		  sql.append("select   b.indexflag,b.status_id,b.task_info,b.start_time,b.end_time,b.save_time,c.user_name,c.real_name,c.department_id  from (select * from task_info a where 1=1 ");
		  if(userInfoModel!=null){
			  sql.append(" and a.assign_id=(select id from user_info a where a.user_name='")
			     .append(userInfoModel.getUser_name()).append("'  and a.real_name= '").append(userInfoModel.getReal_name()).append("')");
		  }
		  sql.append(" and a.save_time >= date_add(now(), interval -").append(time).append(" second)");
		  sql.append(")b left join user_info c on b.auth_id=c.id ORDER BY b.save_time");
		  System.err.println("time:==="+time+"-------"+sql.toString());
		  Connection con =getConnection();
		  if(con!=null){
			ArrayList<TaskModel> tasks = new ArrayList<>();
			PreparedStatement pStatement = con.prepareStatement(sql.toString());
			ResultSet rSet = pStatement.executeQuery();
			while (rSet.next()) {
				TaskModel taskModel = new TaskModel();
				TaskInfoPojo taskInfoPojo = new TaskInfoPojo();
				taskInfoPojo.setIndexflag(rSet.getInt("indexflag"));
				taskInfoPojo.setStatus_id(rSet.getInt("status_id"));
				Blob blob = rSet.getBlob("task_info");
				if(blob!=null){
					taskInfoPojo.setTask_info(new String(blob.getBytes((long)1, (int)blob.length()),"UTF-8"));
				}
				taskInfoPojo.setStart_time(rSet.getTimestamp("start_time").getTime());
				taskInfoPojo.setEnd_time(rSet.getTimestamp("end_time").getTime());
				taskInfoPojo.setSave_time(rSet.getTimestamp("save_time").getTime());
				Blob svn = rSet.getBlob("svn_path");
				if(svn!=null){
					taskInfoPojo.setSvn_path(new String(svn.getBytes((long)1,(int) svn.length()),"UTF-8"));
				}
				taskModel.setTask(taskInfoPojo);
				taskModel.setAssigner(userInfoModel);   //发起人
				UserInfoPojo authorize = new UserInfoPojo();
				authorize.setUser_name(rSet.getString("user_name"));
				authorize.setReal_name(rSet.getString("real_name"));
				authorize.setDepartment(rSet.getInt("department_id"));
				taskModel.setAuthorizer(authorize);
				tasks.add(taskModel);
//				System.err.println(taskModel.getTask().getTask_info()+"---"+rSet.getTimestamp("save_time").toLocaleString());
			}
			return tasks;
		  }else{
				throw new RuntimeException("查询条件不满足!");
		  }
	 } catch (Exception e) {
		 e.printStackTrace();
		   throw e;
	 }
}
  

public static ArrayList<TaskModel> getAllTaskInfo(UserInfoPojo userInfoModel, TASK_STATUS status, long timestamp) throws Exception{
	  Connection con = null;
	  try {
		  Date date = new Date(timestamp);
		  String startTime = (date.getYear()+1900) + "-" + (date.getMonth()+1) + "-1";
		  String endTime   = (date.getYear()+1900) + "-" + (date.getMonth()+1) + "-" +DateUnit.getDayOnMonth(date);
		  StringBuffer sql =  new StringBuffer();
		  sql.append("select * from user_info  a  left join (select *   from task_info c where  c.status_id<>3 and c.start_time>='").append(startTime).append("' or c.end_time<='").append(endTime).append("') b on a.id = b.assign_id  where a.user_status = 1 and a.user_role<>3 ");
//		  System.err.println(sql.toString());
		  con =getConnection();
		  if(con!=null){
			  ArrayList<TaskModel> tasks = new ArrayList<>();
			PreparedStatement pStatement = con.prepareStatement(sql.toString());
			ResultSet rSet = pStatement.executeQuery();
			while (rSet.next()) {
				TaskModel taskModel = new TaskModel();
				UserInfoPojo assigner = new UserInfoPojo();
				assigner.setId(rSet.getInt("id"));
				assigner.setUser_name(rSet.getString("user_name"));
				assigner.setReal_name(rSet.getString("real_name"));
				assigner.setDepartment(rSet.getInt("department_id"));
				taskModel.setAssigner(assigner);
				int indexflay = rSet.getInt("indexflag");
				if(indexflay>0){
					TaskInfoPojo taskInfoPojo = new TaskInfoPojo();
					taskInfoPojo.setIndexflag(rSet.getInt("indexflag"));
					taskInfoPojo.setStatus_id(rSet.getInt("status_id"));
					Blob taskinfo = rSet.getBlob("task_info");
					if(taskinfo!=null){
						taskInfoPojo.setTask_info(new String(taskinfo.getBytes((long)1,(int) taskinfo.length()),"UTF-8"));
					}
					
//				System.err.println("taskinfo:"+new String(taskinfo.getBytes((long)1,(int) taskinfo.length()),"UTF-8"));
					taskInfoPojo.setStart_time(rSet.getTimestamp("start_time").getTime());
					taskInfoPojo.setEnd_time(rSet.getTimestamp("end_time").getTime());
					taskInfoPojo.setSave_time(rSet.getTimestamp("save_time").getTime());
					Blob svn = rSet.getBlob("svn_path");
					if(svn!=null){
						taskInfoPojo.setSvn_path(new String(svn.getBytes((long)1,(int) svn.length()),"UTF-8"));
					}
					taskInfoPojo.setItem_id(rSet.getInt("item_id"));
					taskModel.setTask(taskInfoPojo);
				}
				tasks.add(taskModel);
			}
		    con.close();
			return tasks;
		  }else{
				throw new RuntimeException("查询条件不满足!");
		  }
	 } catch (Exception e) {
		 if(con!=null){
		  con.close();
		 }
		 e.printStackTrace();
		   throw e;
	 }
}
		
public static void deleteTask(TaskModel taskModel) throws Exception {
	Connection con = null;
	 try {
		  if(taskModel!=null && taskModel.getTask()!=null){
			  StringBuffer sql = new StringBuffer();
			  sql.append("delete from task_info where indexflag = ").append(taskModel.getTask().getIndexflag());
			  con =getConnection();
			  PreparedStatement pStatement = con.prepareStatement(sql.toString());
			  pStatement.execute();
			  if(con!=null){
				  con.close();
			 }
		  }else{
			  throw new RuntimeException("查询条件不满足!");
		  }
	} catch (Exception e) {
		if(con!=null){
			  con.close();
		 }
		throw e;
	}
	
} 


public static void updateTask(TaskModel taskModel) throws Exception {
	Connection con = null;
	 try {
		  if(taskModel!=null){
			  int auth_id = 0;
			  int assign_id =taskModel.getAssigner().getId();
			  int indexflag =0;
			  StringBuffer sql = new StringBuffer();
			  con =getConnection();
			  sql.append("select id from user_info  where user_name ='")
			     .append(taskModel.getAuthorizer().getUser_name()).append("'  and real_name= '").append(taskModel.getAuthorizer().getReal_name()).append("'");
			  PreparedStatement pStatement = con.prepareStatement(sql.toString());
			  ResultSet rSet = pStatement.executeQuery();
			  if(rSet.first()){
				  auth_id = rSet.getInt(1);
			  }else{
				  throw new RuntimeException("获取"+taskModel.getAuthorizer().getReal_name()+"信息失败!");
			  }
			  pStatement = con.prepareStatement("select max(indexflag) from task_info");
			  rSet = pStatement.executeQuery();
			  if(rSet.first()){
				  indexflag = rSet.getInt(1);
			  }else{
				  throw new RuntimeException("获取任务信息失败!");
			  }
			  
			  indexflag=indexflag+1;
			  sql.setLength(0);
			  sql.append("insert into task_info(indexflag,auth_id,assign_id,status_id,task_info,start_time,end_time,save_time,item_id)  values(?,?,?,?,?,?,?,now(),?)");
			  pStatement = con.prepareStatement(sql.toString());
			  pStatement.setInt(1, indexflag);
			  pStatement.setInt(2, auth_id);
			  pStatement.setInt(3, assign_id);
			  pStatement.setInt(4, 0);
			  pStatement.setString(5, taskModel.getTask().getTask_info());
			  pStatement.setTimestamp(6, new Timestamp(taskModel.getTask().getStart_time()));
			  pStatement.setTimestamp(7, new Timestamp(taskModel.getTask().getEnd_time()));
			  pStatement.setInt(8, taskModel.getTask().getItem_id());
//			  System.out.println(new Timestamp(taskModel.getTask().getStart_time()).getDay()+"---" + new Timestamp(taskModel.getTask().getEnd_time()).getDay());
//			  System.err.println(new Date(taskModel.getTask().getStart_time()).getDay()+"--"+new Date(taskModel.getTask().getEnd_time()).getDay());
			  pStatement.execute();
			  con.close();
			  
		  }else{
			  throw new RuntimeException("查询条件不满足!");
		  }
	} catch (Exception e) {
		if(con!=null){
			  con.close();
		 }
		e.printStackTrace();
		throw e;
	}
	
} 


   
   
   
}
