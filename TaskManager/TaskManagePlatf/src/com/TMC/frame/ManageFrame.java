package com.TMC.frame;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.wb.swt.SWTResourceManager;

import com.TMC.Cache.DataCache;
import com.TMC.Task.CreateDepartmentTask;
import com.TMC.Task.CreateItemTask;
import com.TMC.Task.DeleteDepartmentTask;
import com.TMC.Task.EditItemTask;
import com.TMC.Task.EditUserTask;
import com.TMC.Task.QueryDepartmentTask;
import com.TMC.Task.QueryItemTask;
import com.TMC.Task.QueryUserTask;
import com.TMC.Thread.CellBack;
import com.TMC.Thread.QueueThread;
import com.TMC.Thread.Result;
import com.TMC.frame.Dialog.CommonDialog;
import com.TMC.frame.Dialog.CreateDepartmentDialog;
import com.TMC.frame.Dialog.CreateItemDialog;
import com.TMC.frame.Dialog.EditDepartmentDialog;
import com.TMC.frame.Dialog.EditItemDialog;
import com.TMC.frame.Dialog.EditUserDialog;
import com.TMS.entity.DepartmentInfoPojo;
import com.TMS.entity.ItemInfoPojo;
import com.TMS.entity.UserInfoPojo;
import com.TMS.model.TaskModel;
import com.config.Config;
import com.configpath.ConfigPath;

public class ManageFrame extends Composite {
	private UserInfoPojo userInfoModel;
	private Shell shell;
	private Table itemTable;
	private Menu itemMenu,departmentMenu,userMenu;
	private Table departmentTable;
	private Table userTable;
	private Button addItemButton,addDepartmentButton;
	private Label lblNewLabel;
	private Group group,group_1,group_2;

	public ManageFrame(Composite parent,UserInfoPojo userInfoModel) {
		super(parent, SWT.EMBEDDED);
		shell = parent.getShell();
		this.userInfoModel = userInfoModel;
		setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		setLayout(new FormLayout());
		
		
		Label label = new Label(this, SWT.NONE);
		label.setText("管理");
		label.setFont(SWTResourceManager.getFont("楷体", 12, SWT.BOLD));
		label.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		FormData fd_label = new FormData();
		fd_label.right = new FormAttachment(100, -904);
		fd_label.left = new FormAttachment(0);
		fd_label.bottom = new FormAttachment(0, 16);
		fd_label.top = new FormAttachment(0);
		label.setLayoutData(fd_label);
		
		group = new Group(this, SWT.BORDER | SWT.SHADOW_NONE);
		group.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		group.setFont(SWTResourceManager.getFont("微软雅黑", 10, SWT.BOLD));
		group.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_BACKGROUND));
		group.setText("项目管理");
		group.setLayout(new FormLayout());
		FormData fd_group = new FormData();
		fd_group.bottom = new FormAttachment(40, 10);
		fd_group.right = new FormAttachment(50, -10);
		fd_group.top = new FormAttachment(label, 20);
		fd_group.left = new FormAttachment(0, 10);
		group.setLayoutData(fd_group);
		
		addItemButton = new Button(group, SWT.NONE);
		addItemButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				addItem();
			}
		});
		FormData fd_addItemButton = new FormData();
		fd_addItemButton.top = new FormAttachment(0);
		fd_addItemButton.left = new FormAttachment(100, -90);
		fd_addItemButton.right = new FormAttachment(100, -10);
		addItemButton.setLayoutData(fd_addItemButton);
		addItemButton.setImage(SWTResourceManager.getImage(ConfigPath.AddImagePath));
		addItemButton.setText("添加");
		
		itemTable = new Table(group, SWT.BORDER | SWT.FULL_SELECTION);
		FormData fd_itemTable = new FormData();
		fd_itemTable.bottom = new FormAttachment(100, 0);
		fd_itemTable.right = new FormAttachment(100, 0);
		fd_itemTable.top = new FormAttachment(0, 31);
		fd_itemTable.left = new FormAttachment(0);
		itemTable.setLayoutData(fd_itemTable);
		itemTable.setHeaderVisible(true);
		itemTable.setLinesVisible(true);
		itemMenu = new Menu(itemTable);
		itemTable.setMenu(itemMenu);
		itemTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				if(e.button == 3){
					Point point = new Point(e.x, e.y);
					TableItem tableItem= itemTable.getItem(point);
					if(tableItem==null){
						itemMenu.setVisible(false);
					}else{
						itemMenu.setVisible(true);
					}
				}else{
					itemMenu.setVisible(false);
				}
			}
		});
		
		
		MenuItem mntmNewItem = new MenuItem(itemMenu, SWT.NONE);
		mntmNewItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(CommonDialog.showMessage(shell, "询问", "确定删除?", SWT.YES|SWT.NO|SWT.ICON_QUESTION)==SWT.YES){
					deleteItem();
				}
			}
		});
		mntmNewItem.setText("删除");
		mntmNewItem.setImage(new Image(getDisplay(), ConfigPath.RemoveImagePath));
		
		MenuItem mntmNewItem1 = new MenuItem(itemMenu, SWT.NONE);
		mntmNewItem1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				editItem();
			}
		});
		mntmNewItem1.setText("修改");
		mntmNewItem1.setImage(new Image(getDisplay(), ConfigPath.EditImagePath));
		
		TableColumn tblclmnNewColumn = new TableColumn(itemTable, SWT.NONE);
		tblclmnNewColumn.setWidth(51);
		tblclmnNewColumn.setText("序号");
		
		TableColumn tblclmnNewColumn_1 = new TableColumn(itemTable, SWT.CENTER);
		tblclmnNewColumn_1.setWidth(130);
		tblclmnNewColumn_1.setText("项目名");
		
		TableColumn tableColumn = new TableColumn(itemTable, SWT.CENTER);
		tableColumn.setWidth(100);
		tableColumn.setText("描述");
		
		TableColumn tblclmnNewColumn_2 = new TableColumn(itemTable, SWT.CENTER);
		tblclmnNewColumn_2.setWidth(79);
		tblclmnNewColumn_2.setText("状态");
		
		TableColumn tableColumn_1 = new TableColumn(itemTable, SWT.CENTER);
		tableColumn_1.setWidth(140);
		tableColumn_1.setText("所属部门");
		
		Button refreshItemButton = new Button(group, SWT.NONE);
		refreshItemButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				getItemInfo();
			}
		});
		refreshItemButton.setText("刷新");
		refreshItemButton.setImage(SWTResourceManager.getImage(ConfigPath.RefreshImagePath));
		FormData fd_refreshItemButton = new FormData();
		fd_refreshItemButton.left = new FormAttachment(addItemButton, -86, SWT.LEFT);
		fd_refreshItemButton.top = new FormAttachment(addItemButton, 0, SWT.TOP);
		fd_refreshItemButton.right = new FormAttachment(addItemButton, -6);
		refreshItemButton.setLayoutData(fd_refreshItemButton);
		
		group_1 = new Group(this, SWT.BORDER | SWT.SHADOW_NONE);
		group_1.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		group_1.setFont(SWTResourceManager.getFont("微软雅黑", 10, SWT.BOLD));
		group_1.setText("部门管理");
		group_1.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_BACKGROUND));
		group_1.setLayout(new FormLayout());
		FormData fd_group_1 = new FormData();
		fd_group_1.bottom = new FormAttachment(group, 0, SWT.BOTTOM);
		fd_group_1.left = new FormAttachment(group, 10);
		fd_group_1.right = new FormAttachment(100, -10);
		fd_group_1.top = new FormAttachment(0, 36);
		group_1.setLayoutData(fd_group_1);
		
		addDepartmentButton = new Button(group_1, SWT.NONE);
		addDepartmentButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				addDepartment();
			}
		});
		addDepartmentButton.setText("添加");
		addDepartmentButton.setImage(SWTResourceManager.getImage("icons/add.png"));
		FormData fd_addDepartmentButton = new FormData();
		fd_addDepartmentButton.top = new FormAttachment(0);
		fd_addDepartmentButton.right = new FormAttachment(100, -10);
		addDepartmentButton.setLayoutData(fd_addDepartmentButton);
		
		departmentTable = new Table(group_1, SWT.BORDER | SWT.FULL_SELECTION);
		departmentTable.setLinesVisible(true);
		departmentTable.setHeaderVisible(true);
		FormData fd_departmentTable = new FormData();
		fd_departmentTable.bottom = new FormAttachment(100);
		fd_departmentTable.top = new FormAttachment(0, 31);
		fd_departmentTable.right = new FormAttachment(100, 0);
		fd_departmentTable.left = new FormAttachment(0);
		departmentTable.setLayoutData(fd_departmentTable);
		departmentTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				if(e.button == 3){
					Point point = new Point(e.x, e.y);
					TableItem tableItem= departmentTable.getItem(point);
					if(tableItem==null){
						departmentMenu.setVisible(false);
					}else{
						departmentMenu.setVisible(true);
					}
				}else{
					departmentMenu.setVisible(false);
				}
			}
		});
		
		TableColumn tableColumn_2 = new TableColumn(departmentTable, SWT.NONE);
		tableColumn_2.setWidth(51);
		tableColumn_2.setText("序号");
		
		TableColumn tableColumn_3 = new TableColumn(departmentTable, SWT.CENTER);
		tableColumn_3.setWidth(230);
		tableColumn_3.setText("部门名称");
		
		TableColumn tableColumn_4 = new TableColumn(departmentTable, SWT.CENTER);
		tableColumn_4.setWidth(200);
		tableColumn_4.setText("描述");
		
		departmentMenu = new Menu(departmentTable);
		departmentTable.setMenu(departmentMenu);
		
		MenuItem menuItem = new MenuItem(departmentMenu, SWT.NONE);
		menuItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(CommonDialog.showMessage(shell, "询问", "确定删除?", SWT.YES|SWT.NO|SWT.ICON_QUESTION)==SWT.YES){
					deleteDepartmen();
				}
			}
		});
		menuItem.setText("删除");
		menuItem.setImage(SWTResourceManager.getImage("icons/remove.png"));
		
		MenuItem menuItem_1 = new MenuItem(departmentMenu, SWT.NONE);
		menuItem_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				editeDepartment();
			}
		});
		menuItem_1.setText("修改");
		menuItem_1.setImage(SWTResourceManager.getImage("icons/edit.png"));
		
		Button button_1 = new Button(group_1, SWT.NONE);
		fd_addDepartmentButton.left = new FormAttachment(0, 439);
		button_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				getDepartment();
			}
		});
		button_1.setText("刷新");
		button_1.setImage(SWTResourceManager.getImage("icons/refresh.png"));
		FormData fd_button_1 = new FormData();
		fd_button_1.top = new FormAttachment(addDepartmentButton, 0, SWT.TOP);
		fd_button_1.right = new FormAttachment(addDepartmentButton, -6);
		fd_button_1.left = new FormAttachment(addDepartmentButton, -86,SWT.LEFT);
		button_1.setLayoutData(fd_button_1);
		
		group_2 = new Group(this, SWT.BORDER | SWT.SHADOW_NONE);
		group_2.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		group_2.setFont(SWTResourceManager.getFont("微软雅黑", 10, SWT.BOLD));
		group_2.setText("人员管理");
		group_2.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_BACKGROUND));
		group_2.setLayout(new FormLayout());
		FormData fd_group_2 = new FormData();
		fd_group_2.top = new FormAttachment(40, 20);
		fd_group_2.bottom = new FormAttachment(100, -10);
		fd_group_2.left = new FormAttachment(0, 10);
		fd_group_2.right = new FormAttachment(100, -10);
		group_2.setLayoutData(fd_group_2);
		
		userTable = new Table(group_2, SWT.BORDER | SWT.FULL_SELECTION);
		userTable.setLinesVisible(true);
		userTable.setHeaderVisible(true);
		FormData fd_userTable = new FormData();
		fd_userTable.bottom = new FormAttachment(100);
		fd_userTable.top = new FormAttachment(0, 31);
		fd_userTable.right = new FormAttachment(100);
		fd_userTable.left = new FormAttachment(0);
		userTable.setLayoutData(fd_userTable);
		userTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				if(e.button == 3){
					Point point = new Point(e.x, e.y);
					TableItem tableItem= userTable.getItem(point);
					if(tableItem==null){
						userMenu.setVisible(false);
					}else{
						userMenu.setVisible(true);
					}
				}else{
					userMenu.setVisible(false);
				}
			}
		});
		
		TableColumn tableColumn_7 = new TableColumn(userTable, SWT.NONE);
		tableColumn_7.setWidth(51);
		tableColumn_7.setText("序号");
		
		TableColumn tableColumn_8 = new TableColumn(userTable, SWT.CENTER);
		tableColumn_8.setWidth(150);
		tableColumn_8.setText("工号");
		
		TableColumn tableColumn_9 = new TableColumn(userTable, SWT.CENTER);
		tableColumn_9.setWidth(150);
		tableColumn_9.setText("姓名");
		
		TableColumn tableColumn_5 = new TableColumn(userTable, SWT.CENTER);
		tableColumn_5.setWidth(150);
		tableColumn_5.setText("邮箱");
		
		TableColumn tableColumn_6 = new TableColumn(userTable, SWT.CENTER);
		tableColumn_6.setWidth(150);
		tableColumn_6.setText("电话");
		
		TableColumn tableColumn_10 = new TableColumn(userTable, SWT.CENTER);
		tableColumn_10.setWidth(79);
		tableColumn_10.setText("权限");
		
		TableColumn tableColumn_11 = new TableColumn(userTable, SWT.CENTER);
		tableColumn_11.setWidth(200);
		tableColumn_11.setText("所属部门");
		
		userMenu = new Menu(userTable);
		userTable.setMenu(userMenu);
		
		MenuItem menuItem_3 = new MenuItem(userMenu, SWT.NONE);
		menuItem_3.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				editUser();
			}
		});
		menuItem_3.setText("修改");
		menuItem_3.setImage(SWTResourceManager.getImage("icons/edit.png"));
		
		TableColumn tableColumn_12 = new TableColumn(userTable, SWT.CENTER);
		tableColumn_12.setWidth(79);
		tableColumn_12.setText("状态");
		
		Button button_3 = new Button(group_2, SWT.NONE);
		button_3.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				queryUser();
			}
		});
		button_3.setText("刷新");
		button_3.setImage(SWTResourceManager.getImage("icons/refresh.png"));
		FormData fd_button_3 = new FormData();
		fd_button_3.right = new FormAttachment(100, -10);
		fd_button_3.top = new FormAttachment(0);
		fd_button_3.left = new FormAttachment(100, -90);
		button_3.setLayoutData(fd_button_3);
		
		lblNewLabel = new Label(this, SWT.NONE);
		lblNewLabel.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		lblNewLabel.setAlignment(SWT.LEFT);
		lblNewLabel.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		FormData fd_lblNewLabel = new FormData();
		fd_lblNewLabel.bottom = new FormAttachment(group_2, -241);
		fd_lblNewLabel.right = new FormAttachment(group_1, -348);
		fd_lblNewLabel.left = new FormAttachment(0, 10);
		lblNewLabel.setLayoutData(fd_lblNewLabel);
		this.layout();
	}
	

	private void authorityControl() {
		Display.getDefault().syncExec( new Runnable() {
			public void run() {
				if(userInfoModel!=null){
					int role = userInfoModel.getUser_role();
					switch (role) {
					case 1:
						addItemButton.setEnabled(false);
						itemMenu.setEnabled(false);
						group_1.setVisible(false);
						group_2.setVisible(false);
						lblNewLabel.setText("提示:权限不足，部分功能已禁用!");
						break;
					case 2:
						group_1.setVisible(false);
						group_2.setVisible(false);
						lblNewLabel.setText("提示:权限不足，部分功能已禁用!");
						break;
					default:
						break;
					}
					shell.layout();
				}
			}
		});
	}


	public void getTask(){
		authorityControl();
		getItemInfo();
		if(userInfoModel!=null){
			int role = userInfoModel.getUser_role();
			if(role==3){
				getDepartment();
				queryUser();
			}
		}
	}

	private void queryUser(){
		QueryUserTask queryUserTask = new QueryUserTask("", new CellBack(){
			@Override
			public void cellback(final Result result) {
				Display.getDefault().syncExec(new Runnable() {
					@Override
					public void run() {
						if(result.isSuccessful()){
							ArrayList<TaskModel> uPojos = (ArrayList<TaskModel>) result.getData();
							refreshUser(uPojos);
						}else{
							CommonDialog.showMessage(Display.getCurrent().getActiveShell(), "错误信息", result.getReason(), SWT.ERROR);
						}
					}
				});
			}
		});
		QueueThread.getInstance().AddThreadInQueue(queryUserTask);
	}
	
	private void refreshUser(ArrayList<TaskModel> taskModels){
		userTable.removeAll();
		if(taskModels!=null){
			int size = taskModels.size();
			for (int i = 0; i < size; i++) {
				TaskModel  taskModel = taskModels.get(i);
				UserInfoPojo userInfoPojo = taskModel.getAssigner();
				if(userInfoPojo!=null){
					TableItem tableItem= new TableItem(userTable, SWT.NONE);
					tableItem.setText(0, (i+1)+"");
					tableItem.setText(1, getDefult(userInfoPojo.getUser_name()));
					tableItem.setText(2, getDefult(userInfoPojo.getReal_name()));
					tableItem.setText(3, getDefult(userInfoPojo.getUser_mail()));
					tableItem.setText(4, getDefult(userInfoPojo.getPhone_number()));
//					System.err.println("role=="+userInfoPojo.getUser_role());
					tableItem.setText(5, getDefult(Config.ROLE.getEelenment(userInfoPojo.getUser_role()).getInfo()));
					tableItem.setText(6, getDefult(DataCache.getDepartmentName(userInfoPojo.getDepartment())));
					tableItem.setText(7, getDefult(Config.USER_STATUS.getEelenment((userInfoPojo.getUser_status())).getInfo()));
					tableItem.setData(userInfoPojo);
				}
			}
		}
	}
	
	
	
	private void editUser(){
		int select = userTable.getSelectionIndex();
		TableItem tableItem = userTable.getItem(select);
		if(tableItem!=null){
			UserInfoPojo userInfoPojo = (UserInfoPojo) tableItem.getData();
			if(userInfoPojo!=null){
				TaskModel taskModel = new EditUserDialog(shell, userInfoPojo).open();
				if(taskModel!=null){
					if(taskModel!=null){
						EditUserTask editUserTask = new EditUserTask(taskModel, new CellBack(){
							@Override
							public void cellback( Result result) {
								if(result.isSuccessful()){
									queryUser();
								}else{
									CommonDialog.showMessageSyn(shell, "提示", result.getReason(), SWT.ERROR);
								}
							}
						});
						QueueThread.getInstance().AddThreadInQueue(editUserTask);
					} 
				}
			}else{
				CommonDialog.showMessageSyn(shell, "提示", "没有选择项目!", SWT.ERROR);
			}
		}else{
			CommonDialog.showMessageSyn(shell, "提示", "没有选择项目!", SWT.ERROR);
		}
	}
	
	private  void getDepartment(){
		//获取部门
		QueryDepartmentTask queryDepartmentTask = new QueryDepartmentTask(new CellBack() {
			@Override
			public void cellback(final Result result) {
				Display.getDefault().syncExec(new Runnable() {
					@Override
					public void run() {
						if(!result.isSuccessful()){
							CommonDialog.showMessage(Display.getCurrent().getActiveShell(), "错误信息", result.getReason(), SWT.ERROR);
						}else{
							DataCache.departmentList = (List<Object>) result.getData();
							refreshDepartment(DataCache.departmentList);
						}
					}
				});
			}
		});
		QueueThread.getInstance().AddThreadInQueue(queryDepartmentTask);
	}
	
	private void refreshDepartment(List<Object> departmentList) {
		departmentTable.removeAll();
		if(departmentList!=null){
			for (int i = 0; i < departmentList.size(); i++) {
			    TaskModel taskModel = (TaskModel) departmentList.get(i);
			    if(taskModel!=null){
			    	DepartmentInfoPojo deInfoPojo = taskModel.getDepartment();
			    	if(deInfoPojo!=null){
			    		TableItem tableItem= new TableItem(departmentTable, SWT.NONE);
						tableItem.setText(0,(i+1)+"");
						tableItem.setText(1, getDefult(deInfoPojo.getDepartment()));
						tableItem.setText(2, getDefult(deInfoPojo.getExplain()));
						tableItem.setData(deInfoPojo);
			    	}
			    }
			}
		}
    }
	
	private void addDepartment(){
		TaskModel taskModel = new CreateDepartmentDialog(shell).open();
		if(taskModel!=null){
			taskModel.setAuthorizer(userInfoModel);
			CreateDepartmentTask createDepartmentTask = new CreateDepartmentTask(taskModel, new CellBack(){
				@Override
				public void cellback( Result result) {
					if(result.isSuccessful()){
						getDepartment();
					}else{
						CommonDialog.showMessageSyn(shell, "提示", result.getReason(), SWT.ERROR);
					}
				}
			});
			QueueThread.getInstance().AddThreadInQueue(createDepartmentTask);
		} 
	}
	
	private void deleteDepartmen(){
		int select = departmentTable.getSelectionIndex();
		TableItem tableItem = departmentTable.getItem(select);
		if(tableItem!=null){
			DepartmentInfoPojo departmentInfoPojo = (DepartmentInfoPojo) tableItem.getData();
			if(departmentInfoPojo!=null){
				TaskModel taskModel = new TaskModel();
				departmentInfoPojo.setStatus(0);
				taskModel.setDepartment(departmentInfoPojo);
				if(taskModel!=null){
					taskModel.setAuthorizer(userInfoModel);
					DeleteDepartmentTask deleteDepartmentTask = new DeleteDepartmentTask(taskModel, new CellBack(){
						@Override
						public void cellback( Result result) {
							if(result.isSuccessful()){
								getDepartment();
							}else{
								CommonDialog.showMessageSyn(shell, "提示", result.getReason(), SWT.ERROR);
							}
						}
					});
					QueueThread.getInstance().AddThreadInQueue(deleteDepartmentTask);
				} 
			}else{
				CommonDialog.showMessageSyn(shell, "提示", "没有选择项目!", SWT.ERROR);
			}
		}else{
			CommonDialog.showMessageSyn(shell, "提示", "没有选择项目!", SWT.ERROR);
		}
	}
	
	private void editeDepartment(){
		int select = departmentTable.getSelectionIndex();
		TableItem tableItem = departmentTable.getItem(select);
		if(tableItem!=null){
			DepartmentInfoPojo departmentInfoPojo = (DepartmentInfoPojo) tableItem.getData();
			if(departmentInfoPojo!=null){
				TaskModel taskModel = new EditDepartmentDialog(shell, departmentInfoPojo).open();
				if(taskModel!=null){
					if(taskModel!=null){
						taskModel.setAuthorizer(userInfoModel);
						DeleteDepartmentTask deleteDepartmentTask = new DeleteDepartmentTask(taskModel, new CellBack(){
							@Override
							public void cellback( Result result) {
								if(result.isSuccessful()){
									getDepartment();
								}else{
									CommonDialog.showMessageSyn(shell, "提示", result.getReason(), SWT.ERROR);
								}
							}
						});
						QueueThread.getInstance().AddThreadInQueue(deleteDepartmentTask);
					} 
				}
			}else{
				CommonDialog.showMessageSyn(shell, "提示", "没有选择项目!", SWT.ERROR);
			}
		}else{
			CommonDialog.showMessageSyn(shell, "提示", "没有选择项目!", SWT.ERROR);
		}
	}
	
	
	private void getItemInfo(){
		//获取项目
		QueryItemTask queryItemTask = new QueryItemTask(new CellBack() {
			@Override
			public void cellback(final Result result) {
				Display.getDefault().syncExec(new Runnable() {
					@Override
					public void run() {
						if(!result.isSuccessful()){
							CommonDialog.showMessage(Display.getCurrent().getActiveShell(), "错误信息", result.getReason(), SWT.ERROR);
						}else{
							DataCache.itemList =  (List<Object>) result.getData();
							refreshItem(DataCache.itemList);
						}
					}
				});
			}
		});
		QueueThread.getInstance().AddThreadInQueue(queryItemTask);
	}
	
	
	
	private void refreshItem(List<Object> itemList) {
		itemTable.removeAll();
		if(itemList!=null){
			for (int i = 0; i < itemList.size(); i++) {
			    TaskModel taskModel = (TaskModel) itemList.get(i);
			    if(taskModel!=null){
			    	ItemInfoPojo itemInfoPojo = taskModel.getItemInfoPojo();
			    	if(itemInfoPojo!=null){
			    		TableItem tableItem= new TableItem(itemTable, SWT.NONE);
						tableItem.setText(0,(i+1)+"");
						tableItem.setText(1, getDefult(itemInfoPojo.getItemName()));
						tableItem.setText(2, getDefult(itemInfoPojo.getExplain()));
						tableItem.setText(3,Config.USER_STATUS.getEelenment(itemInfoPojo.getStatus()).getInfo());
						tableItem.setText(4,DataCache.getDepartmentName(itemInfoPojo.getDepartment_id()));
						tableItem.setData(itemInfoPojo);
			    	}
			    }
			}
		}
	}

	private void addItem(){
		TaskModel taskModel = new CreateItemDialog(shell).open();
		if(taskModel!=null){
			taskModel.setAuthorizer(userInfoModel);
			CreateItemTask createItemTask = new CreateItemTask(taskModel, new CellBack(){
				@Override
				public void cellback( Result result) {
					if(result.isSuccessful()){
						getItemInfo();
					}else{
						CommonDialog.showMessageSyn(shell, "提示", result.getReason(), SWT.ERROR);
					}
				}
				
			});
			QueueThread.getInstance().AddThreadInQueue(createItemTask);
		} 
	}
	
	private void deleteItem(){
		int select = itemTable.getSelectionIndex();
		TableItem tableItem = itemTable.getItem(select);
		if(tableItem!=null){
			ItemInfoPojo itemInfoPojo = (ItemInfoPojo) tableItem.getData();
			if(itemInfoPojo!=null){
				TaskModel taskModel = new TaskModel();
				itemInfoPojo.setStatus(0);
				taskModel.setItemInfoPojo(itemInfoPojo);
				if(taskModel!=null){
					taskModel.setAuthorizer(userInfoModel);
					EditItemTask editItemTask = new EditItemTask(taskModel, new CellBack(){
						@Override
						public void cellback( Result result) {
							if(result.isSuccessful()){
								getItemInfo();
							}else{
								CommonDialog.showMessageSyn(shell, "提示", result.getReason(), SWT.ERROR);
							}
						}
					});
					QueueThread.getInstance().AddThreadInQueue(editItemTask);
				} 
			}else{
				CommonDialog.showMessageSyn(shell, "提示", "没有选择项目!", SWT.ERROR);
			}
		}else{
			CommonDialog.showMessageSyn(shell, "提示", "没有选择项目!", SWT.ERROR);
		}
	}
	
	
	private void editItem(){
		int select = itemTable.getSelectionIndex();
		TableItem tableItem = itemTable.getItem(select);
		if(tableItem!=null){
			ItemInfoPojo itemInfoPojo = (ItemInfoPojo) tableItem.getData();
			if(itemInfoPojo!=null){
				TaskModel taskModel = new EditItemDialog(shell, itemInfoPojo).open();
				if(taskModel!=null){
					if(taskModel!=null){
						taskModel.setAuthorizer(userInfoModel);
						EditItemTask editItemTask = new EditItemTask(taskModel, new CellBack(){
							@Override
							public void cellback( Result result) {
								if(result.isSuccessful()){
									getItemInfo();
								}else{
									CommonDialog.showMessageSyn(shell, "提示", result.getReason(), SWT.ERROR);
								}
							}
						});
						QueueThread.getInstance().AddThreadInQueue(editItemTask);
					} 
				}
			}else{
				CommonDialog.showMessageSyn(shell, "提示", "没有选择项目!", SWT.ERROR);
			}
		}else{
			CommonDialog.showMessageSyn(shell, "提示", "没有选择项目!", SWT.ERROR);
		}
	}
	

	private String getDefult(String str){
		if(StringUtils.isNotEmpty(str)){
			return str;
		}else{
			return "--";
		}
		
	}
}
