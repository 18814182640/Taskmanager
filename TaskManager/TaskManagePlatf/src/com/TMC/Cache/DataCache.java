package com.TMC.Cache;

import java.util.ArrayList;
import java.util.List;

import com.TMS.entity.DepartmentInfoPojo;
import com.TMS.entity.ItemInfoPojo;
import com.TMS.entity.UserInfoPojo;
import com.TMS.model.TaskModel;

public class DataCache {
  
	private  DataCache(){}
	
	public static UserInfoPojo nowUser;       //缓存当前登陆人
    public static List<Object> departmentList;  //所有部门
    public static List<Object> itemList;
	
	
    
    /**
     * 通过ID获取部门名称
     * */
	public static  String getDepartmentName(int id){
		if ( departmentList!=null && id>0 && id-1<departmentList.size()) {
			TaskModel temp = (TaskModel) departmentList.get(id-1);
			if(temp!=null){
				return temp.getDepartment().getDepartment();
			}else{
				return "--";
			}
		}else{
			return "--";
		}
	}
	
	/**
     * 通过索引获取部门ID
     * */
	public static  int getDepartmentID(int index){
		if ( departmentList!=null && index>=0 && index<departmentList.size()) {
			TaskModel temp = (TaskModel) departmentList.get(index);
			if(temp!=null){
				return temp.getDepartment().getId();
			}else{
				return -1;
			}
		}else{
			return -1;
		}
	}
	
	/**
     * 通过全部部门名称
     * */
	public static  String[] getDepartmentNames(){
		List<Object> list = DataCache.departmentList;
		ArrayList<String> aStrings = new ArrayList<>();
		String [] names = null;
		int size = list.size();
		if(list!=null){
			for (int i = 0; i < size; i++) {
				Object object = list.get(i);
				if(object!=null){
					TaskModel tModel = (TaskModel) object;
					DepartmentInfoPojo departmentInfoPojo = tModel.getDepartment();
					if(departmentInfoPojo!=null){
						aStrings.add(departmentInfoPojo.getDepartment());
					}
				}
			}
			names = new String[aStrings.size()];
			aStrings.toArray(names);
	    }
		return names;
	}
	
	
	
	/**
     * 通过索引获取项目ID
     * */
	public static  int getItemId(int index){
		if (itemList!=null) {
			TaskModel temp = (TaskModel) itemList.get(index);
			if(temp!=null){
				return temp.getItemInfoPojo().getId();
			}else{
				return -1;
			}
		}else{
			return -1;
		}
	}
	
	
	/**
     * 通过ID获取项目名
     * */
	public static  String getItemName(int id){
		if (itemList!=null) {
			for (int i = 0; i < itemList.size(); i++) {
				TaskModel temp = (TaskModel) itemList.get(i);
				if(temp!=null){
					ItemInfoPojo itemInfoPojo = temp.getItemInfoPojo();
					if(itemInfoPojo!=null){
						if(itemInfoPojo.getId().intValue() == id){
							return itemInfoPojo.getItemName();
						}
					}
				}
			}
			return "--";
		}else{
			return "--";
		}
	}
	
	/**
     * 通过名称获取项目ID
     * */
	public static int getItemId(String name){
		if (itemList!=null) {
			for (int i = 0; i < itemList.size(); i++) {
				TaskModel temp = (TaskModel) itemList.get(i);
				if(temp!=null){
					ItemInfoPojo itemInfoPojo = temp.getItemInfoPojo();
					if(itemInfoPojo!=null){
						if(itemInfoPojo.getItemName().equals(name)){
							return itemInfoPojo.getId();
						}
					}
				}
			}
			return -1;
		}else{
			return -1;
		}
	}
	
	/**
     * 获取全部项目名称
     * */
	public static String[] getItemNames(){
		List<Object> list = DataCache.itemList;
		ArrayList<String> aStrings = new ArrayList<>();
		String [] names = null;
		int size = list.size();
		if(list!=null){
			for (int i = 0; i < size; i++) {
				Object object = list.get(i);
				if(object!=null){
					TaskModel tModel = (TaskModel) object;
					ItemInfoPojo itemInfoPojo = tModel.getItemInfoPojo();
					if(itemInfoPojo!=null){
						aStrings.add(itemInfoPojo.getItemName());
					}
				}
			}
			names = new String[aStrings.size()];
			aStrings.toArray(names);
	    }
		return names;
	}
    
	
}
