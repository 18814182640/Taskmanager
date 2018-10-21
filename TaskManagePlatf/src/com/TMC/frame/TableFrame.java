package com.TMC.frame;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

import javax.xml.crypto.Data;

import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

import com.TMC.Cache.DataCache;
import com.TMC.Task.QueryAllTask;
import com.TMC.Task.QueryAssignTask;
import com.TMC.Thread.CellBack;
import com.TMC.Thread.QueueThread;
import com.TMC.Thread.Result;
import com.TMC.Unit.DateUnit;
import com.TMC.frame.Dialog.CommonDialog;
import com.TMS.Tool.DataUnit;
import com.TMS.entity.TaskInfoPojo;
import com.TMS.entity.UserInfoPojo;
import com.TMS.model.TaskModel;
import com.config.Config;
import com.config.Config.TASK_STATUS;
import com.configpath.ConfigPath;
import org.eclipse.swt.widgets.Combo;

public class TableFrame extends Composite {
//	private Table table;
	private Composite composite;
	private Menu menu;
	private UserInfoPojo userInfoModel;
	private Config.TASK_STATUS stauts = TASK_STATUS.DOING;  //默认显示未完成的
	private Shell shell;
	private MenuItem mntmNewItem,mntmNewItem_1;
	private Browser browser;
	private Label label;
	private Combo combo;
	private Date date;
	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


	public TableFrame(Composite parent,UserInfoPojo userInfoModel) {
		super(parent, SWT.NONE);
		shell = parent.getShell();
		this.userInfoModel = userInfoModel;
		setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		setLayout(new FormLayout());
		
		composite = new Composite(this,SWT.NONE);
		composite.setLayout(new FillLayout(SWT.HORIZONTAL));
//		diyTable = new DIYTable(composite,null,DataCache.itemList);
	
		FormData fd_table = new FormData();
		fd_table.bottom = new FormAttachment(100, 0);
		fd_table.top = new FormAttachment(0, 40);
		fd_table.left = new FormAttachment(0);
		composite.setLayoutData(fd_table);
	
	    fd_table.right = new FormAttachment(100,0);
		
		Button button_4 = new Button(this, SWT.NONE);
		button_4.setImage(new Image(getDisplay(), ConfigPath.RefreshImagePath));
		button_4.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				getTask();
			}
		});
		FormData fd_button_4 = new FormData();
		fd_button_4.bottom = new FormAttachment(composite, -6);
		fd_button_4.right = new FormAttachment(100);
		fd_button_4.top = new FormAttachment(0, 11);
		
		browser = new Browser(composite, SWT.NONE);
		fd_button_4.left = new FormAttachment(100, -70);
		button_4.setLayoutData(fd_button_4);
		button_4.setText("查询");
		button_4.setForeground(SWTResourceManager.getColor(SWT.COLOR_LINK_FOREGROUND));
		button_4.setFont(SWTResourceManager.getFont("微软雅黑", 9, SWT.BOLD));
		
		Label lblNewLabel = new Label(this, SWT.NONE);
		lblNewLabel.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		lblNewLabel.setFont(SWTResourceManager.getFont("楷体", 12, SWT.BOLD));
		FormData fd_lblNewLabel = new FormData();
		fd_lblNewLabel.bottom = new FormAttachment(0, 20);
		fd_lblNewLabel.top = new FormAttachment(0);
		fd_lblNewLabel.right = new FormAttachment(0, 100);
		fd_lblNewLabel.left = new FormAttachment(0, 0);
		lblNewLabel.setLayoutData(fd_lblNewLabel);
		lblNewLabel.setText("任务总览");
		java.util.Date date = DateUnit.getThisWeekStartDate();
		java.util.Date startOnMonth = DateUnit.getThisMonthStartDate();
		
		label = new Label(this, SWT.NONE);
		label.setText("查询月份:");
		label.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		label.setFont(SWTResourceManager.getFont("微软雅黑", 9, SWT.BOLD));
		label.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		FormData fd_label = new FormData();
		fd_label.top = new FormAttachment(0, 16);
		label.setLayoutData(fd_label);
		
		combo = new Combo(this, SWT.READ_ONLY);
		fd_label.right = new FormAttachment(combo, -6);
		combo.setFont(SWTResourceManager.getFont("楷体", 11, SWT.BOLD));
		combo.setItems(new String[] {"一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"});
		FormData fd_combo = new FormData();
		fd_combo.bottom = new FormAttachment(composite, -6);
		fd_combo.right = new FormAttachment(button_4, -6);
		combo.setLayoutData(fd_combo);
		combo.select(DateUnit.getNowMonth()-1);
		getTask();
		this.layout();
	}
	
	
	public void getTask(){
		
		String time = DateUnit.getNowYear()+"-"+(combo.getSelectionIndex()+1)+"-1";
		final Date date = Date.valueOf(time);
		QueryAllTask queryAllTask = new QueryAllTask(userInfoModel,stauts,date.getTime(), new CellBack() {
			@Override
			public void cellback(Result result) {
				if(result.isSuccessful()){
					ArrayList<TaskModel> taskModels = (ArrayList<TaskModel>) result.getData();
					if(taskModels!=null){
						showHtml(taskModels,date);
					}
				}else{
					CommonDialog.showMessageSyn(shell, "提示", result.getReason(), SWT.ERROR);
				}
			}
		});
		QueueThread.getInstance().AddThreadInQueue(queryAllTask);
	}
	
	
  private void showHtml(final ArrayList<TaskModel> taskModels, final Date date){
	  Display.getDefault().syncExec(new Runnable() {
		@Override
		public void run() {
			String html =  createHtml(taskModels,date);
			browser.setText(html);
		}
	});
  }

  private String createHtml(ArrayList<TaskModel> taskModels, Date date){
	  this.date = date;
	  StringBuffer html = new StringBuffer();
	  html.append("<html><head> <meta http-equiv=\"Content-Type\" content=\"text/html; charset=gbk\" /></head><body><table cellspacing=\"0\" border=\"1\" style=\"width:3000px;border-color:#525445;text-align: center;\">");
	  html.append(createHtmlHead(date));  //表头
	  if (taskModels!=null) {
		    html.append(createBody(taskModels));                              //内容
	  }   
	  html.append("</table></body></html>");
//	  System.err.println(html.toString());
	  return html.toString();
  }
	
 private String createHtmlHead(Date date){
	 StringBuffer header = new StringBuffer();
	 header.append("<thead style=\"background:#f5f5f5;text-align:center;\">").append("\r\n");
	 header.append(createFristRow(date)).append("\r\n");
	 header.append(createSecondRow(date)).append("\r\n");
	 header.append(createThirdRow(date)).append("\r\n");
	 header.append("</thead>");
	 return header.toString();
 }

 private String createFristRow(Date date){
	 int week = DateUnit.getWeekOnYear(date);
	 int weekCount = DateUnit.getWeekCountOnMonth(date);
	 StringBuffer row = new StringBuffer();
	 row.append("<tr>");
	 row.append("<th rowspan=\"3\" style=\"font-size:16px;font-weight: bold;min-width:200px;\">Department</th>");
	 row.append("<th rowspan=\"3\" style=\"font-size:16px;font-weight: bold;min-width:200px;\">Team Member</th>");
	 row.append("<th rowspan=\"3\" style=\"font-size:16px;font-weight: bold;min-width:200px;\">Project Name</th>");
	 for (int i = 0; i < weekCount; i++) {
		int day = DateUnit.getDayCountOnThisWeek(date,i);
		row.append("<th colspan=\"").append(day).append("\" style=\"font-size:16px;min-width:100px;font-weight: bold;\">WK").append(week+i).append("</th>");
	 }
	 row.append("</tr>");
	 return row.toString();
 }

 private String createSecondRow(Date date){
	 int weekCount = DateUnit.getWeekCountOnMonth(date);
	 StringBuffer row = new StringBuffer();
	 row.append("<tr>");
	 int startDay = 1;
	 int endDay = 0;
	 for (int i = 0; i < weekCount; i++) {
		 int day = DateUnit.getDayCountOnThisWeek(date,i);
		 String temp = (date.getYear()+1900) + "-" + (date.getMonth()+1) + "-" + startDay +"~";
		 endDay = startDay+day-1;
		 int b = DateUnit.getDayOnMonth(date);
		 if(endDay>b){
			 temp+=(date.getYear()+1900) + "-" + (date.getMonth()+1) + "-" + b;
		 }else{
			 temp+=(date.getYear()+1900) + "-" + (date.getMonth()+1) + "-" + endDay;
		 }
		row.append("<th colspan=\"").append(day).append("\" style=\"font-size:16px;font-weight: bold;\">").append(temp).append("</th>");
		startDay = endDay + 1;
	 }
	 row.append("</tr>");
	 return row.toString();
 }
 private String createThirdRow(Date date){
	 int dayCount = DateUnit.getDayOnMonth(date);
	 StringBuffer row = new StringBuffer();
	 row.append("<tr>");
	 for (int i = 0; i < dayCount; i++) {
		 String temp = DateUnit.getDayOnWeek(date,i);
		row.append("<th colspan=\"1\" style=\"font-size:14px;min-width:100px;font-weight: bold;\">").append(temp).append("</th>");
	 }
	 row.append("</tr>");
	 return row.toString();
 }
 
 
 private String createBody(ArrayList<TaskModel> taskModels){
	 StringBuffer body = null;
	 if(taskModels!=null){
		 body = new StringBuffer();
		 body.append("<tbody >");
		 Map<Integer, Map<String,Map<Integer, List<TaskInfoPojo>>>> tMap = hanldInfo(taskModels);
		 Set<Entry<Integer, Map<String, Map<Integer, List<TaskInfoPojo>>>>> set = tMap.entrySet();
		 for (Entry<Integer, Map<String, Map<Integer, List<TaskInfoPojo>>>> entry : set) {
			 body.append(getBodyRow(entry));
		 }
		 body.append("</tbody >");
	 }
	 return body==null?"":body.toString();
 }
	
 
 private Map<Integer, Map<String,Map<Integer, List<TaskInfoPojo>>>> hanldInfo(ArrayList<TaskModel> taskModels){
	 Map<Integer, Map<String,Map<Integer, List<TaskInfoPojo>>>> dmap = new HashMap<>();
	 int size = taskModels.size();
	 for (int i = 0; i < size; i++) {
		TaskModel taskModel = taskModels.get(i);
		if (taskModel!=null) {
			UserInfoPojo userInfoPojo = taskModel.getAssigner();
			TaskInfoPojo taskInfoPojo = taskModel.getTask();
			if(userInfoPojo!=null && taskInfoPojo!=null){
				int department = userInfoPojo.getDepartment();
				String userName = userInfoPojo.getUser_name()+userInfoPojo.getReal_name();
				int item = taskInfoPojo.getItem_id();
			    Map<String,Map<Integer, List<TaskInfoPojo>>> tMap = null;
				if(dmap.containsKey(department)){ //存在
					tMap = dmap.get(department);
				}else{                           //不存在
					tMap = new HashMap<>();  
					dmap.put(department, tMap);
				}
				Map<Integer, List<TaskInfoPojo>> pMap = null;
				if(tMap.containsKey(userName)){
					pMap = tMap.get(userName);
				}else{
					pMap = new HashMap<>(); 
					tMap.put(userName, pMap);
				}
				List<TaskInfoPojo> tList = null;
				if(pMap.containsKey(item)){
					tList = pMap.get(item);
				}else{
					tList = new ArrayList<>(); 
					pMap.put(item, tList);
				}
				tList.add(taskInfoPojo);
			}
		}
	}
	 return dmap;
 }
 
 
 private String getBodyRow(Entry<Integer, Map<String, Map<Integer, List<TaskInfoPojo>>>> entry){
	 StringBuffer row = new StringBuffer();
	 if(entry!=null){
		 String department = DataCache.getDepartmentName(entry.getKey());
		 Map<String, Map<Integer, List<TaskInfoPojo>>> tMap = entry.getValue();
		 int allRow = getAllRow(tMap);
		 row.append("<tr>");
		 row.append("<td rowspan=\"").append(allRow).append("\">").append(department).append("</td>");
		 row.append(getFirstBodyRow(tMap));
		 row.append("</tr>");
		 
		 int dayCount = DateUnit.getDayOnMonth(date);
		 int t = 0;
		 Set<Entry<String, Map<Integer, List<TaskInfoPojo>>>> taskSet = tMap.entrySet();
		 for (Entry<String, Map<Integer, List<TaskInfoPojo>>> entry2 : taskSet) {
			 if(t!=0){
				 String userName = entry2.getKey();
				 Map<Integer, List<TaskInfoPojo>> pMap = entry2.getValue();
				 int psize = pMap.size();
				 int pp = 0;
				 for (Entry<Integer, List<TaskInfoPojo>> entry3 : pMap.entrySet()) {
					 List<TaskInfoPojo> list = entry3.getValue();
					 String itemName = DataCache.getItemName(entry3.getKey());
					if(pp!=0){
						row.append("<tr>");
						row.append("<td>").append(itemName).append("</td>");
						for (int i = 1; i <= dayCount; i++) {
							row.append("<td>").append(getTaskOnThis(list,i)).append("</td>");
						}
						row.append("</tr>");
					}else {
						row.append("<tr>");
						row.append("<td rowspan=").append(psize).append(">").append(userName).append("</td>");
						row.append("<td>").append(itemName).append("</td>");
						for (int i = 1; i <= dayCount; i++) {
							row.append("<td>").append(getTaskOnThis(list,i)).append("</td>");
						}
						row.append("</tr>");
					}
					pp++;
				 }
			 }else{
				 int p = 0;
				 Map<Integer, List<TaskInfoPojo>> pMap = entry2.getValue();
				 Set<Entry<Integer, List<TaskInfoPojo>>> set = pMap.entrySet();
					for (Entry<Integer, List<TaskInfoPojo>> entry3 : set) {
						if(p!=0){
							String itemName = DataCache.getItemName(entry3.getKey());
							List<TaskInfoPojo> list = entry3.getValue();
							row.append("<tr>");
							row.append("<td>").append(itemName).append("</td>");
							for (int i = 1; i <= dayCount; i++) {
								row.append("<td>").append(getTaskOnThis(list,i)).append("</td>");
							}
							row.append("</tr>");
						}
						p++;
					}
			 }
			 t++;
		 }
	 }
	 return row.toString();
 }
 
 
 

  private String getFirstBodyRow(Map<String, Map<Integer, List<TaskInfoPojo>>> tMap) {
	  StringBuffer temp = new StringBuffer();
	  Set<Entry<String, Map<Integer, List<TaskInfoPojo>>>> taskSet = tMap.entrySet();
		 for (Entry<String, Map<Integer, List<TaskInfoPojo>>> entry2 : taskSet) {
			String userName = entry2.getKey();
			Map<Integer, List<TaskInfoPojo>> pMap = entry2.getValue();
			int psize = pMap.size();
			temp.append("<td rowspan=").append(psize).append(">").append(userName).append("</td>");
			Set<Entry<Integer, List<TaskInfoPojo>>> set = pMap.entrySet();
			for (Entry<Integer, List<TaskInfoPojo>> entry3 : set) {
				String itemName = DataCache.getItemName(entry3.getKey());
				temp.append("<td>").append(itemName).append("</td>");
				int dayCount = DateUnit.getDayOnMonth(date);
				List<TaskInfoPojo> list = entry3.getValue();
				for (int i = 1; i <= dayCount; i++) {
					temp.append("<td>").append(getTaskOnThis(list,i)).append("</td>");
				}
				break; //只执行一次
			}
			break;  //只执行一次
		 }
	return temp.toString();
}

  
  
  @SuppressWarnings("deprecation")
private String getTaskOnThis(List<TaskInfoPojo> list, int day){
	  StringBuffer temp = new StringBuffer();
	  if(list!=null){
		  try {
			  int len = list.size();
			  for (int j = 0; j < len; j++) {
				  TaskInfoPojo taskInfoPojo = list.get(j);
				  if(taskInfoPojo!=null){
					  long start = taskInfoPojo.getStart_time();
					  long end = taskInfoPojo.getEnd_time();
					  long nowstart = format.parse(DateUnit.getYear(this.date)+"-"+DateUnit.getMonth(this.date)+"-"+day+" 00:00:00").getTime();
					  long nowend = format.parse(DateUnit.getYear(this.date)+"-"+DateUnit.getMonth(this.date)+"-"+day+" 23:59:59").getTime();
					  if(start<=nowend && nowstart<=end){
						  temp.append(taskInfoPojo.getTask_info()).append("</br>");
					  }
					  /* Date sDate = new Date(start);
				  Date eDate = new Date(end);
				  if(DateUnit.getYear(sDate) == DateUnit.getYear(date)){
					  if( DateUnit.getMonth(sDate)== DateUnit.getMonth(this.date)){
						  if(DateUnit.getDay(sDate)<=day && (day<=DateUnit.getDay(eDate)||DateUnit.getMonth(eDate)>DateUnit.getMonth(this.date))){
							  temp.append(taskInfoPojo.getTask_info()).append("</br>");
						  }
					  }else if(DateUnit.getMonth(sDate)<DateUnit.getMonth(this.date)){
						  if(day<=DateUnit.getDay(eDate)||DateUnit.getMonth(eDate)>DateUnit.getMonth(this.date)){
							  temp.append(taskInfoPojo.getTask_info()).append("</br>");
						  }
					  }
				  }*/
				  }
			  }
		  } catch (Exception e) {
			e.printStackTrace();
			return "-";
		  }
	  }
	  if(temp.length()<=0){
		  temp.append("-");
	  }
	  return temp.toString();
  }

private int getAllRow(Map<String, Map<Integer, List<TaskInfoPojo>>> tMap) {
	  int allRow = 0;
	  Set<Entry<String, Map<Integer, List<TaskInfoPojo>>>> taskSet = tMap.entrySet();
		 for (Entry<String, Map<Integer, List<TaskInfoPojo>>> entry2 : taskSet) {
			Map<Integer, List<TaskInfoPojo>> pMap = entry2.getValue();
			allRow += pMap.size();
		 }
	return allRow;
  }


	private String getDefult(String str){
		if(StringUtils.isNotEmpty(str)){
			return str;
		}else{
			return "-";
		}
	}
}
