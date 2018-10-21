package com.TMC.frame;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
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
import org.swiftgantt.model.Task;

import com.TMC.Task.CreateTaskTask;
import com.TMC.Task.DeleteTaskTask;
import com.TMC.Task.EditTaskTask;
import com.TMC.Task.QueryAssignTask;
import com.TMC.Task.QueryAuthTaskTask;
import com.TMC.Thread.CellBack;
import com.TMC.Thread.QueueThread;
import com.TMC.Thread.Result;
import com.TMC.Unit.DateUnit;
import com.TMC.frame.Dialog.CommonDialog;
import com.TMC.frame.Dialog.CreateTaskDialog;
import com.TMC.frame.Dialog.ShowDateDialog;
import com.TMC.frame.Dialog.ShowTaskDialog;
import com.TMS.entity.TaskInfoPojo;
import com.TMS.entity.UserInfoPojo;
import com.TMS.model.TaskModel;
import com.config.Config;
import com.config.Config.TASK_STATUS;
import com.configpath.ConfigPath;

public class AuthorizerFrame extends Composite {
	private Table table,table_1;
	private Menu menu,menu_1;
	private UserInfoPojo userInfoModel;
	private Label timelabel;
	private MenuItem finishMenu,rollBackMenu,mntmNewItem_2;
	private Config.TASK_STATUS stauts = null;  //默认显示全部
	private Shell shell;
	private DateTime dateTime;
	private MenuItem updateMenu;
	private MenuItem delateMenu;

	public AuthorizerFrame(Composite parent,UserInfoPojo userInfoModel) {
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
						String status = tableItem.getText(3);
						if(Config.TASK_STATUS.ASKING.getInfo().equals(status)){
							finishMenu.setEnabled(true);
						}else{
							finishMenu.setEnabled(false);
						}
						if(Config.TASK_STATUS.FINISH.getInfo().equals(status) || Config.TASK_STATUS.CLOSE.getInfo().equals(status) ){
							rollBackMenu.setEnabled(false);
						}else{
							rollBackMenu.setEnabled(true);
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
		menu.setVisible(false);
		
	    finishMenu = new MenuItem(menu, SWT.NONE);
	    finishMenu.setImage(new Image(getDisplay(), ConfigPath.OverImagePath));
	    finishMenu.addSelectionListener(new SelectionAdapter() {
	    	@Override
	    	public void widgetSelected(SelectionEvent e) {
	    		editTask(Config.TASK_STATUS.FINISH,stauts);
	    	}
	    });
		finishMenu.setText("确认完成");
		
		rollBackMenu = new MenuItem(menu, SWT.NONE);
		rollBackMenu.setImage(new Image(getDisplay(), ConfigPath.RollbackImagePath));
		rollBackMenu.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				editTask(Config.TASK_STATUS.CLOSE,stauts);
			}
		});
		rollBackMenu.setText("撤回任务");
		
		MenuItem mntmNewItem = new MenuItem(menu, SWT.NONE);
		mntmNewItem.setImage(new Image(getDisplay(), ConfigPath.EditImagePath));
		mntmNewItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				showTask(table);
			}
		});
		mntmNewItem.setText("查看任务");
		
		updateMenu = new MenuItem(menu, SWT.NONE);
		updateMenu.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
			}
		});
		updateMenu.setText("修改任务");
		updateMenu.setImage(SWTResourceManager.getImage(ConfigPath.UPDATE));
		
		delateMenu = new MenuItem(menu, SWT.NONE);
		delateMenu.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				deleteTask();
			}
		});
		delateMenu.setText("彻底删除");
		delateMenu.setImage(SWTResourceManager.getImage(ConfigPath.DELETEDIT));
		
		TableColumn tableColumn = new TableColumn(table, SWT.CENTER);
		tableColumn.setWidth(40);
		tableColumn.setText("序号");
		
		TableColumn tableColumn_1 = new TableColumn(table, SWT.CENTER);
		tableColumn_1.setWidth(80);
		tableColumn_1.setText("任务执行人");
		
		TableColumn tableColumn_2 = new TableColumn(table, SWT.CENTER);
		tableColumn_2.setWidth(300);
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
				getAskingTask();
				/*getThisMonthTask();
				getAllTask();*/
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
		fd_lblNewLabel.right = new FormAttachment(0,100);
		fd_lblNewLabel.bottom = new FormAttachment(0, 25);
		fd_lblNewLabel.top = new FormAttachment(0, 0);
		fd_lblNewLabel.left = new FormAttachment(0, 0);
		lblNewLabel.setLayoutData(fd_lblNewLabel);
		lblNewLabel.setText("我分配的任务");
		
		timelabel = new Label(this, SWT.NONE);
		timelabel.setText("任务状态:");
		timelabel.setAlignment(SWT.RIGHT);
		timelabel.setForeground(SWTResourceManager.getColor(255, 99, 71));
		timelabel.setFont(SWTResourceManager.getFont("微软雅黑", 9, SWT.BOLD));
		timelabel.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		FormData fd_lblNewLabel_1 = new FormData();
		fd_lblNewLabel_1.top = new FormAttachment(0, 16);
		timelabel.setLayoutData(fd_lblNewLabel_1);
		
		dateTime = new DateTime(this, SWT.BORDER);
		fd_lblNewLabel_1.left = new FormAttachment(dateTime, 14);
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
		
		
		table_1 = new Table(this, SWT.BORDER | SWT.FULL_SELECTION);
		fd_table.right = new FormAttachment(table_1, -6);
		table_1.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		table_1.setHeaderVisible(true);
		FormData fd_table_1 = new FormData();
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
		fd_table_1.top = new FormAttachment(lblNewLabel_1, 9);
		lblNewLabel_1.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		lblNewLabel_1.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_CYAN));
		lblNewLabel_1.setFont(SWTResourceManager.getFont("楷体", 9, SWT.BOLD));
		FormData fd_lblNewLabel_11 = new FormData();
		fd_lblNewLabel_11.left = new FormAttachment(table_1, 0, SWT.LEFT);
		fd_lblNewLabel_11.top = new FormAttachment(0, 19);
		
		TableColumn tblclmnNewColumn = new TableColumn(table_1, SWT.NONE);
		tblclmnNewColumn.setWidth(35);
		tblclmnNewColumn.setText("NO");
		
		TableColumn tblclmnNewColumn_1 = new TableColumn(table_1, SWT.CENTER);
		tblclmnNewColumn_1.setWidth(80);
		tblclmnNewColumn_1.setText("任务");
		lblNewLabel_1.setLayoutData(fd_lblNewLabel_11);
		lblNewLabel_1.setText("申请完成的任务列表:");
		
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
		
		getAskingTask();
		this.layout();
		/*getThisMonthTask();
		getAllTask();*/
	}
	
	
	private void showTask(Table table){
		TableItem item = table.getItem(table.getSelectionIndex());
		if(item!=null && item.getData()!=null){
			TaskModel taskModel = (TaskModel) item.getData();
			ShowTaskDialog dialog = new ShowTaskDialog(shell,taskModel,1);
			dialog.open();
		}
	}
	
	public void getTask(){
		String time = dateTime.getYear()+"-"+(dateTime.getMonth()+1)+"-"+dateTime.getDay();
		Date date = Date.valueOf(time);
		QueryAuthTaskTask queryAuthTaskTask = new QueryAuthTaskTask(userInfoModel,stauts,date.getTime(), new CellBack() {
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
		QueueThread.getInstance().AddThreadInQueue(queryAuthTaskTask);
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
							UserInfoPojo assigner = taskModel.getAssigner();
							if(assigner!=null){
								item.setText(1, getDefult(assigner.getReal_name()));
							}
							TaskInfoPojo taskInfoPojo = taskModel.getTask();
							if(taskInfoPojo!=null){
								SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								item.setText(2,getDefult(taskInfoPojo.getTask_info()));
								item.setText(3,getDefult( Config.TASK_STATUS.values()[taskInfoPojo.getStatus_id()].getInfo()));
								item.setText(4,getDefult(format.format(taskInfoPojo.getSave_time())));
								item.setText(5,getDefult( format.format(taskInfoPojo.getStart_time())));
								item.setText(6,getDefult(format.format(taskInfoPojo.getEnd_time())));
								item.setData(taskModel);
							}
							
						}
					}
				}
				table.layout();
			}
		});
	}
	
	
	
	private void editTask(Config.TASK_STATUS status,Config.TASK_STATUS showStatus){
		if(CommonDialog.showMessage(getShell(), "询问", "确定执行操作?", SWT.ICON_QUESTION | SWT.YES | SWT.NO)!=SWT.YES){
			return;
		}
		TableItem item = table.getItem(table.getSelectionIndex());
		if(item!=null && item.getData()!=null){
			EditTaskTask editTaskTask = new EditTaskTask((TaskModel)item.getData(), status,showStatus, new CellBack(){
				@Override
				public void cellback(Result result){
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
			QueueThread.getInstance().AddThreadInQueue(editTaskTask);
		}
	}
	
	
	private void deleteTask(){
		if(CommonDialog.showMessage(getShell(), "询问", "确定执行操作?", SWT.ICON_QUESTION | SWT.YES | SWT.NO)!=SWT.YES){
			return;
		}
		TableItem item = table.getItem(table.getSelectionIndex());
		if(item!=null && item.getData()!=null){
			DeleteTaskTask deleteTaskTask = new DeleteTaskTask((TaskModel)item.getData(), new CellBack(){
				@Override
				public void cellback(Result result){
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
			QueueThread.getInstance().AddThreadInQueue(deleteTaskTask);
		}
	}
	
	
	public void getAskingTask(){
		Date date = Date.valueOf("1900-1-1");
		QueryAuthTaskTask queryAuthTaskTask = new QueryAuthTaskTask(userInfoModel,TASK_STATUS.ASKING,date.getTime(), new CellBack() {
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
		QueueThread.getInstance().AddThreadInQueue(queryAuthTaskTask);
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
								long temp = taskInfoPojo.getEnd_time() - taskInfoPojo.getSave_time();  //ms
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
