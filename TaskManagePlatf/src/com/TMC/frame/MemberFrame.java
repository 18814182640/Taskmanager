package com.TMC.frame;

import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.SWT;
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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import com.TMC.Cache.DataCache;
import com.TMC.Task.CreateTaskTask;
import com.TMC.Task.QueryUserTask;
import com.TMC.Thread.CellBack;
import com.TMC.Thread.QueueThread;
import com.TMC.Thread.Result;
import com.TMC.frame.Dialog.CommonDialog;
import com.TMC.frame.Dialog.CreateTaskDialog;
import com.TMS.entity.UserInfoPojo;
import com.TMS.model.TaskModel;
import com.config.Config;
import com.config.Config.TASK_STATUS;
import com.configpath.ConfigPath;

public class MemberFrame extends Composite {
	private Table table;
	private Menu menu;
	private UserInfoPojo userInfoModel;
	private MenuItem finishMenu;
	private Config.TASK_STATUS stauts = TASK_STATUS.DOING;  //默认显示未完成的
	private Text text;
	private String query;
	private Shell shell;


	public MemberFrame(Composite parent,UserInfoPojo userInfoModel) {
		super(parent, SWT.NONE);
		shell =parent.getShell();
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
						UserInfoPojo uPojo = (UserInfoPojo) tableItem.getData();
						if(Config.USER_STATUS.VALID.getFlag() == uPojo.getUser_status()){
							finishMenu.setEnabled(true);
						}else{
							finishMenu.setEnabled(false);
						}
					}
				}else{
					menu.setVisible(false);
				}
			}
		});
		table.setFont(SWTResourceManager.getFont("微软雅黑", 9, SWT.BOLD));
		table.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		FormData fd_table = new FormData();
		fd_table.right = new FormAttachment(100,0);
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
	    finishMenu.setImage(new Image(getDisplay(), ConfigPath.AddImagePath));
	    finishMenu.addSelectionListener(new SelectionAdapter() {
	    	@Override
	    	public void widgetSelected(SelectionEvent e) {
	    		createTask();
	    	}
	    });
		finishMenu.setText("分配任务");
		
		TableColumn tableColumn = new TableColumn(table, SWT.CENTER);
		tableColumn.setWidth(40);
		tableColumn.setText("序号");
		
		TableColumn tableColumn_1 = new TableColumn(table, SWT.CENTER);
		tableColumn_1.setWidth(150);
		tableColumn_1.setText("工号");
		
		TableColumn tableColumn_2 = new TableColumn(table, SWT.CENTER);
		tableColumn_2.setWidth(150);
		tableColumn_2.setText("姓名");
		
		TableColumn tableColumn_3 = new TableColumn(table, SWT.CENTER);
		tableColumn_3.setWidth(150);
		tableColumn_3.setText("部门");
		
		TableColumn tableColumn_4 = new TableColumn(table, SWT.CENTER);
		tableColumn_4.setWidth(150);
		tableColumn_4.setText("当前进行任务数");
		
		Label lblNewLabel = new Label(this, SWT.NONE);
		lblNewLabel.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		lblNewLabel.setFont(SWTResourceManager.getFont("楷体", 11, SWT.BOLD));
		FormData fd_lblNewLabel = new FormData();
		fd_lblNewLabel.top = new FormAttachment(0, 0);
		fd_lblNewLabel.right = new FormAttachment(0, 100);
		fd_lblNewLabel.left = new FormAttachment(0, 0);
		
		TableColumn tblclmnNewColumn = new TableColumn(table, SWT.CENTER);
		tblclmnNewColumn.setWidth(150);
		tblclmnNewColumn.setText("状态");
		lblNewLabel.setLayoutData(fd_lblNewLabel);
		lblNewLabel.setText("成员");
		
		Label lblNewLabel_1 = new Label(this, SWT.NONE);
		lblNewLabel_1.setFont(SWTResourceManager.getFont("微软雅黑", 9, SWT.BOLD));
		lblNewLabel_1.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		lblNewLabel_1.setForeground(SWTResourceManager.getColor(SWT.COLOR_LINK_FOREGROUND));
		FormData fd_lblNewLabel_11 = new FormData();
		lblNewLabel_1.setLayoutData(fd_lblNewLabel_11);
		lblNewLabel_1.setText("关键字:");
		
		text = new Text(this, SWT.BORDER);
		fd_lblNewLabel_11.top = new FormAttachment(text, 3, SWT.TOP);
		fd_lblNewLabel_11.right = new FormAttachment(100, -242);
		FormData fd_text = new FormData();
		fd_text.left = new FormAttachment(lblNewLabel_1, 6);
		fd_text.top = new FormAttachment(0, 11);
		text.setLayoutData(fd_text);
		
		Button btnNewButton = new Button(this, SWT.NONE);
		fd_text.right = new FormAttachment(btnNewButton, -6);
		btnNewButton.setImage(new Image(getDisplay(), ConfigPath.FindImagePath));
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				queryTask();
			}
		});
		FormData fd_btnNewButton = new FormData();
		fd_btnNewButton.top = new FormAttachment(0, 9);
		fd_btnNewButton.left = new FormAttachment(100, -70);
		fd_btnNewButton.right = new FormAttachment(100);
		btnNewButton.setLayoutData(fd_btnNewButton);
		btnNewButton.setText("搜索");
		this.layout();
	}
	

	private void refreshTable(final ArrayList<TaskModel> taskModels){
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				if(taskModels!=null){
					table.removeAll();
					for (int i = 0; i < taskModels.size(); i++) {
						TaskModel  taskModel = taskModels.get(i);
						UserInfoPojo userInfoPojo = taskModel.getAssigner();
						if(userInfoPojo!=null){
							TableItem item= new TableItem(table, SWT.NONE);
							item.setText(0,(i+1)+"");
							item.setText(1, getDefult(userInfoPojo.getUser_name()));
							item.setText(2, getDefult(userInfoPojo.getReal_name()));
							item.setText(3, getDefult(DataCache.getDepartmentName(userInfoPojo.getDepartment())));
							item.setText(4, userInfoPojo.getTaskNum()+"");
							item.setText(5, userInfoPojo.getUser_status() == Config.USER_STATUS.VALID.getFlag()?Config.USER_STATUS.VALID.getInfo():Config.USER_STATUS.INVALID.getInfo());
							item.setData(userInfoPojo);
						}
					}
				}
				
			}
		});
	}
	
	
	public void queryTask(){
		query = text.getText();
		QueryUserTask queryUserTask = new QueryUserTask(query, new CellBack(){
			@Override
			public void cellback(Result result) {
				if(result.isSuccessful()){
					ArrayList<TaskModel> uPojos = (ArrayList<TaskModel>) result.getData();
					if(uPojos!=null){
						refreshTable(uPojos);
					}else{
						CommonDialog.showMessageSyn(getShell(), "提示", result.getReason(), SWT.ERROR);
					}
				}
			}
		});
		QueueThread.getInstance().AddThreadInQueue(queryUserTask);
	}
	
	private void createTask(){
		int select = table.getSelectionIndex();
		if(select<0){
			return;
		}
		CreateTaskDialog dialog = new CreateTaskDialog(getShell(),table.getItem(select).getText(1)+" "+table.getItem(select).getText(2));
		TaskModel taskModel = dialog.open();
		if(taskModel!=null){
			taskModel.setAuthorizer(userInfoModel);
			UserInfoPojo assigner = (UserInfoPojo)table.getItem(table.getSelectionIndex()).getData();
			taskModel.setAssigner(assigner);
			CreateTaskTask createTaskTask = new CreateTaskTask(taskModel, new CellBack(){
				@Override
				public void cellback( Result result) {
					if(result.isSuccessful()){
						Display.getDefault().syncExec(new Runnable() {
							@Override
							public void run() {
								queryTask();
							}
						});
					}else{
						CommonDialog.showMessageSyn(shell, "提示", result.getReason(), SWT.ERROR);
					}
				}
				
			});
			QueueThread.getInstance().AddThreadInQueue(createTaskTask);
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
