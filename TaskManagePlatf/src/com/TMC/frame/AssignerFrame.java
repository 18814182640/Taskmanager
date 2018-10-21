package com.TMC.frame;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
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
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.wb.swt.SWTResourceManager;

import com.TMC.Task.AskFinishTask;
import com.TMC.Task.CreateTaskTask;
import com.TMC.Task.QueryAssignTask;
import com.TMC.Thread.CellBack;
import com.TMC.Thread.QueueThread;
import com.TMC.Thread.Result;
import com.TMC.Unit.DateUnit;
import com.TMC.frame.Dialog.CommonDialog;
import com.TMC.frame.Dialog.CreateTaskDialog;
import com.TMC.frame.Dialog.SVNPathDialog;
import com.TMC.frame.Dialog.ShowDateDialog;
import com.TMC.frame.Dialog.ShowTaskDialog;
import com.TMS.Tool.DataUnit;
import com.TMS.entity.TaskInfoPojo;
import com.TMS.entity.UserInfoPojo;
import com.TMS.model.TaskModel;
import com.config.Config;
import com.config.Config.TASK_STATUS;
import com.configpath.ConfigPath;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.ModifyEvent;

public class AssignerFrame extends Composite {
	private Table table;
	private Menu menu,menu_1;
	private UserInfoPojo userInfoModel;
	private Label timelabel;
	private Config.TASK_STATUS stauts = null;  //默认显示未完成的
	private Shell shell;
	private MenuItem mntmNewItem,mntmNewItem_1,mntmNewItem_2;
	private Table table_1;
	private DateTime dateTime;


	public AssignerFrame(Composite parent,UserInfoPojo userInfoModel) {
		super(parent, SWT.NONE);
		shell = parent.getShell();
		this.userInfoModel = userInfoModel;
		setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		setLayout(new FormLayout());
		
		table = new Table(this, SWT.BORDER | SWT.FULL_SELECTION);
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				if(e.button == 3){
					Point point = new Point(e.x, e.y);
					TableItem tableItem= table.getItem(point);
					if(tableItem==null){
						menu.setVisible(false);
					}else{
						menu.setVisible(true);
						if(Config.TASK_STATUS.DOING.getInfo().equals(tableItem.getText(3))){
							mntmNewItem.setEnabled(true);
						}else{
							mntmNewItem.setEnabled(false);
						}
					}
				}else{
					menu.setVisible(false);
				}
			}
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				showTask(table);
			}
		});
		table.setFont(SWTResourceManager.getFont("微软雅黑", 9, SWT.BOLD));
		table.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		FormData fd_table = new FormData();
		fd_table.left = new FormAttachment(0);
		fd_table.bottom = new FormAttachment(100, 0);
		fd_table.top = new FormAttachment(0, 40);
		table.setLayoutData(fd_table);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		menu = new Menu(table);
		table.setMenu(menu);
		
	    mntmNewItem = new MenuItem(menu, SWT.NONE);
		mntmNewItem.setImage(new Image(getDisplay(), ConfigPath.AskImagePath));
		mntmNewItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				askFinish();
			}
		});
		mntmNewItem.setText("申请完成");
		
		mntmNewItem_1 = new MenuItem(menu, SWT.NONE);
		mntmNewItem_1.setImage(new Image(getDisplay(), ConfigPath.EditImagePath));
		mntmNewItem_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				showTask(table);
			}
		});
		mntmNewItem_1.setText("查看任务详情");
		
		TableColumn tableColumn = new TableColumn(table, SWT.CENTER);
		tableColumn.setWidth(40);
		tableColumn.setText("序号");
		
		TableColumn tableColumn_1 = new TableColumn(table, SWT.CENTER);
		tableColumn_1.setWidth(80);
		tableColumn_1.setText("任务分配人");
		
		TableColumn tableColumn_2 = new TableColumn(table, SWT.CENTER);
		tableColumn_2.setWidth(302);
		tableColumn_2.setText("任务描述");
		
		TableColumn tableColumn_3 = new TableColumn(table, SWT.CENTER);
		tableColumn_3.setWidth(70);
		tableColumn_3.setText("任务状态");
		
		TableColumn tableColumn_4 = new TableColumn(table, SWT.CENTER);
		tableColumn_4.setWidth(130);
		tableColumn_4.setText("更新时间");
		
		TableColumn tableColumn_5 = new TableColumn(table, SWT.CENTER);
		tableColumn_5.setWidth(130);
		tableColumn_5.setText("计划开始时间");
		
		TableColumn tableColumn_6 = new TableColumn(table, SWT.CENTER);
		tableColumn_6.setWidth(130);
		tableColumn_6.setText("计划结束时间");
		
		Button button_4 = new Button(this, SWT.NONE);
		button_4.setImage(new Image(getDisplay(), ConfigPath.RefreshImagePath));
		button_4.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				getTask();
				getDoingTask();
			}
		});
		FormData fd_button_4 = new FormData();
		fd_button_4.top = new FormAttachment(0, 11);
		fd_button_4.bottom = new FormAttachment(table, -3);
		fd_button_4.right = new FormAttachment(table, 0,SWT.RIGHT);
		fd_button_4.left = new FormAttachment(table, -80,SWT.RIGHT);
		button_4.setLayoutData(fd_button_4);
		button_4.setText("刷新");
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
		lblNewLabel.setText("我的任务");
		
		timelabel = new Label(this, SWT.NONE);
		timelabel.setText("任务状态:");
		timelabel.setAlignment(SWT.RIGHT);
		timelabel.setForeground(SWTResourceManager.getColor(255, 99, 71));
		timelabel.setFont(SWTResourceManager.getFont("微软雅黑", 9, SWT.BOLD));
		timelabel.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		FormData fd_lblNewLabel_1 = new FormData();
		timelabel.setLayoutData(fd_lblNewLabel_1);
		
		dateTime = new DateTime(this, SWT.BORDER);
		dateTime.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				java.util.Date date = new ShowDateDialog(shell).open();
	    		if(date!=null){
	    			dateTime.setYear(DateUnit.getYear(date));
	    			dateTime.setMonth(DateUnit.getMonth(date));
	    			dateTime.setDay(DateUnit.getDay(date)-1);
	    		}
			}
		});
	    dateTime.setYear(DateUnit.getNowYear());
	    dateTime.setMonth(DateUnit.getNowMonth()-1);
	    dateTime.setDay(1);
	    FormData fd_dateTime = new FormData();
	    fd_dateTime.top = new FormAttachment(timelabel, -4, SWT.TOP);
	    fd_dateTime.right = new FormAttachment(timelabel, -6);
	    dateTime.setLayoutData(fd_dateTime);
		
		final Combo combo = new Combo(this, SWT.READ_ONLY);
		fd_lblNewLabel_1.left = new FormAttachment(combo, -66,SWT.LEFT);
		fd_lblNewLabel_1.top = new FormAttachment(combo, 4, SWT.TOP);
		fd_lblNewLabel_1.right = new FormAttachment(combo, -6);
		combo.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		combo.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_FOREGROUND));
		combo.setFont(SWTResourceManager.getFont("微软雅黑", 9, SWT.NORMAL));
		combo.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				int select = combo.getSelectionIndex();
				switch (select) {
				  case 0: stauts = null;break;
				  case 1: stauts = Config.TASK_STATUS.DOING;break;
				  case 2: stauts = Config.TASK_STATUS.ASKING;break;
				  case 3: stauts = Config.TASK_STATUS.FINISH;break;
				  case 4: stauts = Config.TASK_STATUS.CLOSE;break;
				  default:break;
				}
				getTask();
			}
		});
		combo.setItems(new String[] {"全部", "未完成", "申请完成", "已完成", "关闭"});
		FormData fd_combo = new FormData();
		fd_combo.bottom = new FormAttachment(table, 11);
		fd_combo.top = new FormAttachment(button_4, 1, SWT.TOP);
		fd_combo.left = new FormAttachment(button_4, -80, SWT.LEFT);
		fd_combo.right = new FormAttachment(button_4, -10);
		combo.setLayoutData(fd_combo);
		combo.select(0);
		
		table_1 =  new Table(this, SWT.BORDER | SWT.FULL_SELECTION);
		fd_table.right = new FormAttachment(table_1, -6);
		table_1.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		table_1.setHeaderVisible(true);
		FormData fd_table_1 = new FormData();
		fd_table_1.top = new FormAttachment(table, 0, SWT.TOP);
		fd_table_1.bottom = new FormAttachment(100);
		fd_table_1.left = new FormAttachment(100, -220);
		fd_table_1.right = new FormAttachment(100);
		table_1.setLayoutData(fd_table_1);
		table_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				if(e.button == 3){
					Point point = new Point(e.x, e.y);
					TableItem tableItem= table_1.getItem(point);
					if(tableItem==null){
						menu_1.setVisible(false);
					}else{
						menu_1.setVisible(true);
					}
				}else{
					menu_1.setVisible(false);
				}
			}
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				showTask(table_1);
			}
		});
		
		Label lblNewLabel_1 = new Label(this, SWT.NONE);
		lblNewLabel_1.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		lblNewLabel_1.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_CYAN));
		lblNewLabel_1.setFont(SWTResourceManager.getFont("楷体", 9, SWT.BOLD));
		FormData fd_lblNewLabel_11 = new FormData();
		fd_lblNewLabel_11.top = new FormAttachment(button_4, 8, SWT.TOP);
		fd_lblNewLabel_11.left = new FormAttachment(button_4, 8);
		
		TableColumn tblclmnNewColumn = new TableColumn(table_1, SWT.NONE);
		tblclmnNewColumn.setWidth(35);
		tblclmnNewColumn.setText("NO");
		
		TableColumn tblclmnNewColumn_1 = new TableColumn(table_1, SWT.CENTER);
		tblclmnNewColumn_1.setWidth(80);
		tblclmnNewColumn_1.setText("任务");
		lblNewLabel_1.setLayoutData(fd_lblNewLabel_11);
		lblNewLabel_1.setText("待完成任务列表:");
		
		TableColumn tblclmnNewColumn_2 = new TableColumn(table_1, SWT.CENTER);
		tblclmnNewColumn_2.setWidth(100);
		tblclmnNewColumn_2.setText("剩余");
		
		
		menu_1 = new Menu(table_1);
		table_1.setMenu(menu_1);
		
	    mntmNewItem_2 = new MenuItem(menu_1, SWT.NONE);
	    mntmNewItem_2.setImage(new Image(getDisplay(), ConfigPath.EditImagePath));
	    mntmNewItem_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				showTask(table_1);
			}
		});
	    mntmNewItem_2.setText("查看任务");
	    
	    
	    Label label = new Label(this, SWT.NONE);
	    label.setText("开始时间:");
	    label.setForeground(SWTResourceManager.getColor(255, 99, 71));
	    label.setFont(SWTResourceManager.getFont("微软雅黑", 9, SWT.BOLD));
	    label.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
	    label.setAlignment(SWT.RIGHT);
	    FormData fd_label = new FormData();
	    fd_label.left = new FormAttachment(dateTime, -66, SWT.LEFT);
	    fd_label.top = new FormAttachment(dateTime, 4, SWT.TOP);
	    fd_label.right = new FormAttachment(dateTime, -6);
	    label.setLayoutData(fd_label);
		
		getDoingTask();
		this.layout();
	}
	
	
	
	
	


	private void showTask(Table table){
		int selete = table.getSelectionIndex();
		if(selete>=0 && selete<table.getItemCount()){
			TableItem item = table.getItem(selete);
			if(item!=null && item.getData()!=null){
				TaskModel taskModel = (TaskModel) item.getData();
				ShowTaskDialog dialog = new ShowTaskDialog(shell,taskModel,0);
				dialog.open();
			}
		}
	}
	
	public void getTask(){
		String time = dateTime.getYear()+"-"+(dateTime.getMonth()+1)+"-"+dateTime.getDay();
		Date date = Date.valueOf(time);
		QueryAssignTask queryAssignTaskTask = new QueryAssignTask(userInfoModel,stauts,date.getTime(), new CellBack() {
			@Override
			public void cellback(Result result) {
				if(result.isSuccessful()){
					ArrayList<TaskModel> taskModels = (ArrayList<TaskModel>) result.getData();
					if(taskModels!=null){
						refreshTable(taskModels);
					}
				}else{
					CommonDialog.showMessageSyn(shell, "提示", result.getReason(), SWT.ERROR);
				}
			}
		});
		QueueThread.getInstance().AddThreadInQueue(queryAssignTaskTask);
	}
	
	
	
	private void refreshTable(final ArrayList<TaskModel> taskModels){
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				if(taskModels!=null){
					table.removeAll();
					for (int i = 0; i < taskModels.size(); i++) {
						TaskModel taskModel = taskModels.get(i);
						if(taskModel!=null){
							TableItem item= new TableItem(table, SWT.NONE);
							item.setText(0,(i+1)+"");
							UserInfoPojo authorize = taskModel.getAuthorizer();
							if(authorize!=null){
								item.setText(1, getDefult(authorize.getReal_name()));
							}
							TaskInfoPojo taskInfoPojo = taskModel.getTask();
							if(taskInfoPojo!=null){
								SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								item.setText(2,getDefult(taskInfoPojo.getTask_info()));
								TASK_STATUS status = Config.TASK_STATUS.getEelenment(taskInfoPojo.getStatus_id());
								item.setText(3,getDefult(status==null?"":status.getInfo()));
								item.setText(4,getDefult(format.format(new Timestamp(taskInfoPojo.getSave_time()))));
								item.setText(5,getDefult( format.format(new Timestamp((taskInfoPojo.getStart_time())))));
								item.setText(6,getDefult(format.format(new Timestamp(taskInfoPojo.getEnd_time()))));
								item.setData(taskModel);
							}
							
						}
					}
				}
				table.layout();
			}
		});
	}
	
	
	
	
	private void askFinish(){
		TableItem item = table.getItem(table.getSelectionIndex());
		if(item!=null && item.getData()!=null){
			SVNPathDialog svnPathDialog =new SVNPathDialog(shell);
			String svn_path = svnPathDialog.open();
			if(svn_path==null || svn_path.isEmpty()){
				return;
			}
			TaskModel taskModel = (TaskModel)item.getData();
			taskModel.getTask().setSvn_path(svn_path);
			AskFinishTask askFinishTask = new AskFinishTask(taskModel, stauts, new CellBack(){
				@Override
				public void cellback(Result result) {
					if(result.isSuccessful()){
						Display.getDefault().syncExec(new Runnable() {
							@Override
							public void run() {
								getTask();
							}
						});
					}else{
						CommonDialog.showMessageSyn(shell, "提示", result.getReason(), SWT.ERROR);
					}
				}
			});
			QueueThread.getInstance().AddThreadInQueue(askFinishTask);
		}
	}
	
	
	public void getDoingTask(){
		Date date = Date.valueOf("1900-1-1");
		QueryAssignTask queryAssignTaskTask = new QueryAssignTask(userInfoModel,TASK_STATUS.DOING,date.getTime(), new CellBack() {
			@Override
			public void cellback(Result result) {
				if(result.isSuccessful()){
					ArrayList<TaskModel> taskModels = (ArrayList<TaskModel>) result.getData();
					if(taskModels!=null){
						showRemindInfo(taskModels);
					}
				}else{
					CommonDialog.showMessageSyn(shell, "提示", result.getReason(), SWT.ERROR);
				}
			}
		});
		QueueThread.getInstance().AddThreadInQueue(queryAssignTaskTask);
	}

	
	private void showRemindInfo(final ArrayList<TaskModel> taskModels){
		if(taskModels!=null){
			Display.getDefault().syncExec(new Runnable() {
				@Override
				public void run() {
					table_1.removeAll();
					for (int i = 0; i < taskModels.size(); i++) {
						TaskModel taskModel = taskModels.get(i);
						if(taskModel!=null){
							TaskInfoPojo taskInfoPojo = taskModel.getTask();
							if(taskInfoPojo!=null){
								TableItem tableItem= new TableItem(table_1, SWT.NONE);
								tableItem.setText(0,(i+1)+"");
								tableItem.setText(1, taskInfoPojo.getTask_info());
								long temp = taskInfoPojo.getEnd_time()- System.currentTimeMillis();  //ms
								String tempStr =null;
								if(temp<=0){
									tempStr = temp/(1000*60*60*24) +"天(逾期)";
									tableItem.setForeground(2,SWTResourceManager.getColor(SWT.COLOR_RED));
								}else{
									tempStr = temp/(1000*60*60*24) +"天";
									tableItem.setForeground(2,SWTResourceManager.getColor(SWT.COLOR_BLACK));
								}
								tableItem.setText(2, tempStr);
								tableItem.setData(taskModel);
							}
						}
					}
					table_1.layout();
				}
			});
		}
	}

	
	
	private String getDefult(String str){
		if(StringUtils.isNotEmpty(str)){
			return str;
		}else{
			return "-";
		}
		
	}
}
