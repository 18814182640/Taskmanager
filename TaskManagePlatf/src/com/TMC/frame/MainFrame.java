package com.TMC.frame;

import java.awt.Toolkit;
import java.util.List;

import org.apache.commons.collections.functors.UniquePredicate;
import org.eclipse.jface.layout.TreeColumnLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.TreeEvent;
import org.eclipse.swt.events.TreeListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.internal.win32.OS;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tray;
import org.eclipse.swt.widgets.TrayItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.wb.swt.SWTResourceManager;

import com.TMC.Cache.DataCache;
import com.TMC.Task.CreateTaskTask;
import com.TMC.Task.InitInstallTask;
import com.TMC.Task.QueryDepartmentTask;
import com.TMC.Task.QueryItemTask;
import com.TMC.Task.SigninTask;
import com.TMC.Thread.CellBack;
import com.TMC.Thread.QueueThread;
import com.TMC.Thread.Result;
import com.TMC.frame.Dialog.CommonDialog;
import com.TMC.frame.Dialog.CreateTaskDialog;
import com.TMC.frame.Dialog.InformDialog;
import com.TMC.service.ClientBoot;
import com.TMC.service.MessageHandle;
import com.TMC.service.ServiceEven;
import com.TMC.service.ServiceListener;
import com.TMS.entity.UserInfoPojo;
import com.TMS.model.Message;
import com.TMS.model.TaskModel;
import com.configpath.ConfigPath;
import com.sun.org.glassfish.gmbal.Description;
import com.sun.swing.internal.plaf.basic.resources.basic;

import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

public class MainFrame {

	private Display displayMain;
	private Shell shellMain;
	private Tray tray;
	private Menu menu;
	private int screenH,screenW;
	private MenuItem openItem,exitItem;
	private Composite composite_base;
	private StackLayout stackLayout;
	private AssignerFrame assignerFrame;  //任务详情
	private AuthorizerFrame authorizerFrame;  //我分配的
	private MemberFrame memberFrame;              //成员
	private DataFrame dataFrame;              //数据
	private TableFrame tableFrame;
	private ManageFrame manageFrame;
    private String title = "TaskManage V1.0.1 - 未登陆";
    public static MainFrame mainFrame;
    private MenuItem mntmNewItem_2;
    private MenuItem mntmNewItem_3;
    private Composite composite_left;
    private Tree tree;
    private TreeItem trtmNewTreeitem0;
    private TreeItem trtmNewTreeitem1;
    private TreeItem trtmNewTreeitem2;
    private TreeItem trtmNewTreeitem3;
    private TreeItem trtmNewTreeitem4;
    private TreeItem trtmNewTreeitem5;
    private String INDEX = "index";
    private TreeItem trtmNewTreeitem6;
    private MenuItem mntmNewItem_4;
    private MenuItem mntmNewItem_5;
    private ToolBar toolBar;
    private int shellCtrl = 0;  //1
    private static ToolItem updateDepartmentTool,updateItemTool;
    
    
    
	public static void main(String[] args) {
		try {
		    mainFrame = new MainFrame();
		    CommonDialog.delay(100);
			mainFrame.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void open() {
		displayMain = new Display();
		shellMain = new Shell(displayMain,SWT.SHELL_TRIM );
		shellMain.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		shellMain.setText(title);
		screenH = Toolkit.getDefaultToolkit().getScreenSize().height;
		screenW = Toolkit.getDefaultToolkit().getScreenSize().width;
		shellMain.setImage(new Image(displayMain, ConfigPath.ICOImagePath));
		shellMain.setSize(1280, 595);
		shellMain.setLocation((screenW - shellMain.getSize().x) / 2, (screenH - shellMain.getSize().y) / 2);
		shellMain.setMinimized(false); //显示
		shellMain.setActive();
		OS.SetWindowPos(shellMain.handle,shellCtrl,(screenW - shellMain.getSize().x) / 2, (screenH - shellMain.getSize().y) / 2,
				(int) (screenW * 0.8), (int) (screenH * 0.8), SWT.NULL);
		shellMain.addShellListener(new ShellAdapter() {
			@Override
			public void shellIconified(ShellEvent e){
				e.doit = false;
				shellMain.setMinimized(false);
				shellMain.setVisible(false);
			}
			@Override
			public void shellClosed(ShellEvent e){
				e.doit = false;
				shellMain.setVisible(false);
			}
		});
		
		
		tray = displayMain.getSystemTray();
		TrayItem trayItem = new TrayItem(tray, SWT.NONE);
		trayItem.setImage(new Image(displayMain, ConfigPath.ICOImagePath));
		trayItem.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				showShellOrNot();
			}
		});
		trayItem.addMenuDetectListener(new MenuDetectListener() {
			@Override
			public void menuDetected(MenuDetectEvent e) {
				menu.setVisible(true);
			}
		});

		menu = new Menu(shellMain,SWT.POP_UP);
		openItem= new MenuItem(menu, SWT.PUSH);
		openItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				showShellOrNot();
			}
		});
		openItem.setText("隐藏控制台(O)");
		
		MenuItem mntmNewItem = new MenuItem(menu, SWT.NONE);
		mntmNewItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				assignerFrame.getTask();
				stackLayout.topControl = assignerFrame; 
				composite_base.layout();
				shellMain.setVisible(true);
				openItem.setText("隐藏控制台(H)");
				shellMain.setMinimized(false);
				OS.SetWindowPos(shellMain.handle, shellCtrl,(screenW - shellMain.getSize().x) / 2, (screenH - shellMain.getSize().y) / 2,
						(int) (screenW * 0.8), (int) (screenH * 0.8), SWT.NULL);
			}
		});
		mntmNewItem.setText("查看我的任务(T)");
		exitItem = new MenuItem(menu, SWT.PUSH);
		exitItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shellMain.dispose();
				System.exit(0);
			}
		});
		exitItem.setText("退出(E)");
		menu.setDefaultItem(openItem);
		shellMain.setLayout(new FormLayout());
		
		Composite composite = new Composite(shellMain, SWT.BORDER);
		composite.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		composite.setLayout(new FormLayout());
		FormData fd_composite = new FormData();
		fd_composite.bottom = new FormAttachment(100, 0);
		fd_composite.right = new FormAttachment(100, 0);
		fd_composite.top = new FormAttachment(0, 25);
		fd_composite.left = new FormAttachment(0, 0);
		composite.setLayoutData(fd_composite);
		
		stackLayout = new StackLayout();
		composite_base = new Composite(composite, SWT.NONE);
		composite_base.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		composite_base.setLayout(stackLayout);
		FormData fd_composite_1 = new FormData();
		fd_composite_1.right = new FormAttachment(100, 0);
		fd_composite_1.top = new FormAttachment(0,0);
		fd_composite_1.bottom = new FormAttachment(100, 0);
		composite_base.setLayoutData(fd_composite_1);
		
		composite_left = new Composite(composite, SWT.NONE);
		fd_composite_1.left = new FormAttachment(0, 139);
		composite_left.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT));
		composite_left.setLayout(new TreeColumnLayout());
		FormData fd_composite_left = new FormData();
		fd_composite_left.right = new FormAttachment(composite_base, -6);
		fd_composite_left.bottom = new FormAttachment(100, 0);
		fd_composite_left.top = new FormAttachment(0,0);
		fd_composite_left.left = new FormAttachment(0, 0);
		composite_left.setLayoutData(fd_composite_left);
		
		
		tree = new Tree(composite_left, SWT.NONE);
		tree.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TreeItem item = (TreeItem)e.item;
				if(item!=null){
					Integer integer = (Integer) item.getData(INDEX);
					if(integer == null){
						return;
					}
					switch (integer.intValue()) {
					case 0:                                                      
						    break;
					case 1: stackLayout.topControl = authorizerFrame; 
					        authorizerFrame.getTask();          
					        break;
					case 2: 
						    stackLayout.topControl = memberFrame; 
						    memberFrame.queryTask();
						    break;
					case 3: 
						    stackLayout.topControl = dataFrame;
						    dataFrame.getTask();
						    break;
					case 4: 
						    stackLayout.topControl = assignerFrame;
						    assignerFrame.getTask();
						    break;
					case 5: stackLayout.topControl = tableFrame;    
					        break;
					case 6: stackLayout.topControl = manageFrame;
					        manageFrame.getTask();
					        break;
					default:break;
					}
					composite_base.layout();
				}
				
			}
		});
		tree.addTreeListener(new TreeListener() {
			@Override
			public void treeExpanded(TreeEvent e) {
				TreeItem item =(TreeItem) e.item;
				item.setExpanded(!item.getExpanded());
			}
			@Override
			public void treeCollapsed(TreeEvent e) {
				TreeItem item =(TreeItem) e.item;
				item.setExpanded(!item.getExpanded());
			}
		});
		
		tree.setEnabled(false);
		tree.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND));
		trtmNewTreeitem0= new TreeItem(tree, SWT.NONE);
		trtmNewTreeitem0.setText(0, "我的任务");
		trtmNewTreeitem0.setData(INDEX, 0);
		trtmNewTreeitem0.setImage(SWTResourceManager.getImage(ConfigPath.ASSIGNIMAGEPATH));
		
		trtmNewTreeitem4 = new TreeItem(trtmNewTreeitem0, SWT.NONE);
		trtmNewTreeitem4.setText("任务列表");
		trtmNewTreeitem4.setData(INDEX, 4);
		trtmNewTreeitem4.setImage(SWTResourceManager.getImage(ConfigPath.LISTIMAGEPATH));
		
		trtmNewTreeitem5 = new TreeItem(trtmNewTreeitem0, SWT.NONE);
		trtmNewTreeitem5.setText("任务总览");
		trtmNewTreeitem5.setData(INDEX, 5);
		trtmNewTreeitem5.setImage(SWTResourceManager.getImage(ConfigPath.CHARTIMAGEPATH));
		trtmNewTreeitem0.setExpanded(true);
		
		trtmNewTreeitem1 = new TreeItem(tree, SWT.NONE);
		trtmNewTreeitem1.setText("我分配的任务");
		trtmNewTreeitem1.setData(INDEX, 1);
		trtmNewTreeitem1.setImage(SWTResourceManager.getImage(ConfigPath.AUTHORIMAGEPATH));
		
		trtmNewTreeitem2 = new TreeItem(tree, SWT.NONE);
		trtmNewTreeitem2.setText("成员信息");
		trtmNewTreeitem2.setData(INDEX, 2);
		trtmNewTreeitem2.setImage(SWTResourceManager.getImage(ConfigPath.MEMBERIMAGEPATH));
		
		trtmNewTreeitem3 = new TreeItem(tree, SWT.NONE);
		trtmNewTreeitem3.setText("数据统计");
		trtmNewTreeitem3.setData(INDEX, 3);
		trtmNewTreeitem3.setImage(SWTResourceManager.getImage(ConfigPath.DATAIMAGEPATH));
		
		trtmNewTreeitem6 = new TreeItem(tree, 0);
		trtmNewTreeitem6.setText("管理");
		trtmNewTreeitem6.setData(INDEX, 6);
		trtmNewTreeitem6.setImage(SWTResourceManager.getImage(ConfigPath.MANAGEIMAGEPATH));
		
		Label label = new Label(composite, SWT.SEPARATOR | SWT.VERTICAL);
		FormData fd_label = new FormData();
		fd_label.bottom = new FormAttachment(100, 0);
		fd_label.top = new FormAttachment(0);
		fd_label.left = new FormAttachment(composite_left, 2,SWT.RIGHT);
		label.setLayoutData(fd_label);
		
		
		toolBar = new ToolBar(shellMain, SWT.RIGHT | SWT.SHADOW_OUT);
		toolBar.setEnabled(false);
		FormData fd_toolBar = new FormData();
		fd_toolBar.left = new FormAttachment(composite, 0, SWT.LEFT);
		fd_toolBar.right = new FormAttachment(composite, 0, SWT.RIGHT);
		toolBar.setLayoutData(fd_toolBar);
		toolBar.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_BACKGROUND));
		
		
		new ToolItem(toolBar, SWT.SEPARATOR);
		
		updateItemTool = new ToolItem(toolBar, SWT.NONE);
		updateItemTool.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				getItemInfo();
			}
		});
		updateItemTool.setToolTipText("更新项目缓存信息");
		updateItemTool.setWidth(30);
		updateItemTool.setImage(SWTResourceManager.getImage(ConfigPath.UPDATEITEM));
		
		new ToolItem(toolBar, SWT.SEPARATOR);
		
		
	    updateDepartmentTool = new ToolItem(toolBar, SWT.NONE);
		updateDepartmentTool.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				getDepartment();
			}
		});
		updateDepartmentTool.setToolTipText("更新部门缓存信息");
		updateDepartmentTool.setWidth(30);
		updateDepartmentTool.setImage(SWTResourceManager.getImage(ConfigPath.UPDATEDEPARTMENT));
		
		new ToolItem(toolBar, SWT.SEPARATOR);
		ToolItem createTool = new ToolItem(toolBar, SWT.NONE);
		createTool.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				createTask();
			}
		});
		createTool.setToolTipText("创建新任务");
		createTool.setWidth(30);
		createTool.setImage(SWTResourceManager.getImage(ConfigPath.AddImagePath));
		
		
		Menu menu_1 = new Menu(shellMain, SWT.BAR);
		shellMain.setMenuBar(menu_1);
		
		MenuItem startMenu = new MenuItem(menu_1, SWT.CASCADE);
		startMenu.setText("开始(S)");
		
		Menu startMenuDrop = new Menu(shellMain, SWT.DROP_DOWN);
		startMenu.setMenu(startMenuDrop);
		
		mntmNewItem_4 = new MenuItem(startMenuDrop, SWT.NONE);
		mntmNewItem_4.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(CommonDialog.showMessage(shellMain, "提示", "确定退出程序", SWT.YES|SWT.NO|SWT.ICON_QUESTION)==SWT.YES){
					shellMain.dispose();
					System.exit(0);
				}
			}
		});
		mntmNewItem_4.setText("退出（Exit）");
		mntmNewItem_4.setImage(SWTResourceManager.getImage(ConfigPath.EXITIMAGEPATH));
		
		
		MenuItem mntmNewItem_1 = new MenuItem(menu_1, SWT.CASCADE);
		mntmNewItem_1.setText("用户(U)");
		
		Menu mntmNewItem_1_1 = new Menu(shellMain, SWT.DROP_DOWN);
		mntmNewItem_1.setMenu(mntmNewItem_1_1);
		
		mntmNewItem_3 = new MenuItem(mntmNewItem_1_1, SWT.NONE);
		mntmNewItem_3.setImage(SWTResourceManager.getImage(ConfigPath.LandImagePath));
		mntmNewItem_3.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(DataCache.nowUser!=null){
					CommonDialog.showMessage(shellMain, "提示", "当前已是登陆状态!", SWT.ICON_INFORMATION);
					return;
				}else{
					login();
				}
			}
		});
		mntmNewItem_3.setText("登陆(L)");
		
		mntmNewItem_2 = new MenuItem(mntmNewItem_1_1, SWT.CASCADE);
		mntmNewItem_2.setImage(SWTResourceManager.getImage(ConfigPath.LoginImagePath));
		mntmNewItem_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(DataCache.nowUser!=null){
					CommonDialog.showMessage(shellMain, "提示", "当前已是登陆状态!", SWT.ICON_INFORMATION);
					return;
				}else{
					newUser();
				}
			}
		});
		mntmNewItem_2.setText("注册(S)");
		
		MenuItem mntms = new MenuItem(mntmNewItem_1_1, SWT.CASCADE);
		mntms.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				exitLoggin();
			}
		});
		mntms.setText("退出登录(E)");
		mntms.setImage(SWTResourceManager.getImage(ConfigPath.EXITLOGGINIMAGEPATH));
		
		MenuItem help = new MenuItem(menu_1, SWT.CASCADE);
		help.setText("帮助(H)");
		
		Menu helpDROP = new Menu(shellMain, SWT.DROP_DOWN);
		help.setMenu(helpDROP);
		
		mntmNewItem_5 = new MenuItem(helpDROP, SWT.NONE);
		mntmNewItem_5.setText("请联系zhangdatian@xwoda.com");
		mntmNewItem_5.setImage(SWTResourceManager.getImage(ConfigPath.HELPIMAGEPATH));
		
		init();
		
		shellMain.layout();
		shellMain.open();
		while (!shellMain.isDisposed()) {
			if (!displayMain.readAndDispatch()) {
				displayMain.sleep();
			}
		}
		tray.dispose();
		displayMain.dispose();
		
		exit();
	}
	
	private void showShellOrNot(){ 
		shellMain.setVisible(!shellMain.getVisible());
		if(shellMain.getVisible()){
			openItem.setText("隐藏控制台(H)");
			shellMain.setMinimized(false);
			OS.SetWindowPos(shellMain.handle, shellCtrl,(screenW - shellMain.getSize().x) / 2, (screenH - shellMain.getSize().y) / 2,
					(int) (screenW * 0.8), (int) (screenH * 0.8), SWT.NULL);
		}else{
			openItem.setText("打开控制台(O)");
		}
	}
	
	public void showNewTask(){
		shellMain.setVisible(true);
		openItem.setText("隐藏控制台(H)");
		shellMain.setMinimized(false);
		OS.SetWindowPos(shellMain.handle, shellCtrl,(screenW - shellMain.getSize().x) / 2, (screenH - shellMain.getSize().y) / 2,
				(int) (screenW * 0.8), (int) (screenH * 0.8), SWT.NULL);
		stackLayout.topControl = assignerFrame;
		assignerFrame.getTask();
		composite_base.layout();
	}
	
  public void showAuthTask(){
		shellMain.setVisible(true);
		openItem.setText("隐藏控制台(H)");
		shellMain.setMinimized(false);
		OS.SetWindowPos(shellMain.handle, shellCtrl,(screenW - shellMain.getSize().x) / 2, (screenH - shellMain.getSize().y) / 2,
				(int) (screenW * 0.8), (int) (screenH * 0.8), SWT.NULL);
		stackLayout.topControl = authorizerFrame;
		authorizerFrame.getTask();
		composite_base.layout();
	}
	
  
	
	@Description("程序启动时必要的初始化")
	private void init(){
		//启动接收线程
		/*if(!ServiceContronal.getInstance().isClose()){
			HeartBeatTask heartBeatTask = new HeartBeatTask(null);
			QueueThread.getInstance().addscheduleWithFixedDelayThread(heartBeatTask, 5000);
		}else{
			CommonDialog.showMessage(shellMain, "提示", "通讯异常,Socket不存在或关闭", SWT.ERROR);
		}*/
		long start = System.currentTimeMillis();
		long timeOut = 5*1000;
		while(!ClientBoot.getInstance().isOpen()){
			CommonDialog.delay(20);
			if(System.currentTimeMillis()-start>=timeOut){
				CommonDialog.showMessage(shellMain, "提示", "连接服务器超时!", SWT.ERROR);
				return;
			}
		}
		
		if(ClientBoot.getInstance().isOpen()){
			MessageHandle.addEvenListener(new ServiceListener() {
				@Override
				public void receive(ServiceEven serviceEven) {
					Message message = serviceEven.getMessage();
					if(message!=null){
						if(DataCache.nowUser!=null){
							handleMes(message);
						}
					}
				}
			});
		}else{
			CommonDialog.showMessage(shellMain, "提示", "通讯异常,Socket不存在或关闭", SWT.ERROR);
			return;
		}
		login();
		getDepartment();//缓存部门信息
		getItemInfo();  //缓存项目信息
	}
	
	public static void getDepartment(){
		updateDepartmentTool.setEnabled(false);
		//获取部门
		QueryDepartmentTask queryDepartmentTask = new QueryDepartmentTask(new CellBack() {
			@Override
			public void cellback(final Result result) {
				Display.getDefault().syncExec(new Runnable() {
					@Override
					public void run() {
						updateDepartmentTool.setEnabled(true);
						if(!result.isSuccessful()){
							CommonDialog.showMessage(Display.getCurrent().getActiveShell(), "错误信息", result.getReason(), SWT.ERROR);
						}else{
							DataCache.departmentList = (List<Object>) result.getData();
						}
					}
				});
			}
		});
		QueueThread.getInstance().AddThreadInQueue(queryDepartmentTask);
	}
	
	public static void getItemInfo(){
		updateItemTool.setEnabled(false);
		//获取项目
		QueryItemTask queryItemTask = new QueryItemTask(new CellBack() {
			@Override
			public void cellback(final Result result) {
				Display.getDefault().syncExec(new Runnable() {
					@Override
					public void run() {
						updateItemTool.setEnabled(true);
						if(!result.isSuccessful()){
							CommonDialog.showMessage(Display.getCurrent().getActiveShell(), "错误信息", result.getReason(), SWT.ERROR);
						}else{
							DataCache.itemList =  (List<Object>) result.getData();
						}
					}
				});
			}
		});
		QueueThread.getInstance().AddThreadInQueue(queryItemTask);
	}
	
	private void login(){
		//登陆线程
		InitInstallTask initInstallTask = new InitInstallTask(shellMain,new CellBack() {
			@Override
			public void cellback(final Result result) {
				Display.getDefault().syncExec(new Runnable() {
					@Override
					public void run() {
						if(!result.isSuccessful()){
							CommonDialog.showMessage(shellMain, "错误信息", result.getReason(), SWT.ERROR);
						}else{
							Message message = (Message) result.getData();
							DataCache.nowUser =  ((TaskModel) (message.getContext().get(0))).getAssigner();
							UserInfoPojo userInfoModel = DataCache.nowUser;
							if(userInfoModel!=null){
								initFrame(userInfoModel);
							}else{
								CommonDialog.showMessage(shellMain, "登陆失败!", "用户名/密码错误或用户不存在!", SWT.ERROR);
								login();
							}
						}
					}
				});
			}
		});
		QueueThread.getInstance().AddThreadInQueue(initInstallTask);
//				CommonDialog.progress(shellMain, initInstallTask);
	}
	
	
	public void newUser(){
		//登陆线程
		SigninTask newUserTask = new SigninTask(shellMain,new CellBack() {
			@Override
			public void cellback(final Result result) {
				Display.getDefault().syncExec(new Runnable() {
					@Override
					public void run() {
						if(!result.isSuccessful()){
							CommonDialog.showMessage(shellMain, "错误信息", result.getReason(), SWT.ERROR);
						}else{
							Message message = (Message) result.getData();
							if(message!=null){
								DataCache.nowUser = ((TaskModel) (message.getContext().get(0))).getAssigner();
								UserInfoPojo userInfoModel = DataCache.nowUser;
								if(userInfoModel!=null){
									initFrame(userInfoModel);
									CommonDialog.showMessage(shellMain, "注册成功!", "已为您登陆!", SWT.ICON_INFORMATION);
								}else{
									CommonDialog.showMessage(shellMain, "注册失败!", "已存在该用户!", SWT.ERROR);
								}
							}
						}
					}
				});
			}
		});
		QueueThread.getInstance().AddThreadInQueue(newUserTask);
//				CommonDialog.progress(shellMain, initInstallTask);
	}
	
	
	private void createTask(){
		CreateTaskDialog createTaskDialog = new CreateTaskDialog(shellMain);
		TaskModel taskModel = createTaskDialog.open();
		if(taskModel!=null){
			taskModel.setAuthorizer(DataCache.nowUser);
			CreateTaskTask createTaskTask = new CreateTaskTask(taskModel, new CellBack(){
				@Override
				public void cellback( Result result) {
					if(result.isSuccessful()){
					}else{
						CommonDialog.showMessageSyn(shellMain, "提示", result.getReason(), SWT.ERROR);
					}
				}
				
			});
			QueueThread.getInstance().AddThreadInQueue(createTaskTask);
		}
	}
	
	private void initFrame(UserInfoPojo userInfoModel){
		if(userInfoModel!=null){
			shellMain.setText("TaskManage - "+"已登录:"+userInfoModel.getUser_name()+" "+userInfoModel.getReal_name());
			assignerFrame = new AssignerFrame(composite_base, userInfoModel);
			stackLayout.topControl = assignerFrame;
			assignerFrame.getTask();
			authorizerFrame = new AuthorizerFrame(composite_base, userInfoModel);
			memberFrame = new MemberFrame(composite_base, userInfoModel);
			dataFrame = new DataFrame(composite_base, userInfoModel);
			dataFrame = new DataFrame(composite_base, userInfoModel);
			tableFrame = new TableFrame(composite_base, userInfoModel);
			manageFrame = new ManageFrame(composite_base, userInfoModel);
			tree.setEnabled(true);
			toolBar.setEnabled(true);
			composite_base.layout();
		}
	}
	
	private void exitLoggin(){
		DataCache.nowUser = null;
		shellMain.setText("TaskManage - "+"未登录");
		stackLayout.topControl = null;
		assignerFrame = null;
		authorizerFrame = null;
		memberFrame = null;
		dataFrame = null;
		dataFrame = null;
		tableFrame = null;
		manageFrame = null;
		tree.setEnabled(false);
		toolBar.setEnabled(false);
		composite_base.layout();
	}
	
	
	@Description("程序退出时必要的销毁工作")
	private void exit(){
//		ServiceContronal.getInstance().close();
		ClientBoot.getInstance().close();
		QueueThread.getInstance().shutdown();
	}
	
	
	
	
	
	private void handleMes(final Message message){
		if(Message.ERROR.equals(message.getMethod())){
			Display.getDefault().syncExec(new Runnable() {
				public void run() {
					shellMain.setText(title);
					exitLoggin();
					CommonDialog.showMessage(shellMain, "提示", message.getReason(), SWT.ERROR);
					login();
				}
			});
		}else if(Message.CREATE_ITEM.equals(message.getMethod()) || Message.EDIT_ITEM.equals(message.getMethod())){
			getItemInfo();
			Display.getDefault().syncExec(new Runnable() {
				public void run() {
//					new UpdataDialog(displayMain, "项目有新动态，已为你更新!",3*1000).open();
				}
			});
		}else if(Message.CREATE_DEPARTMENT.equals(message.getMethod()) || Message.EDIT_DEPARTMENT.equals(message.getMethod())){
			getDepartment();
			Display.getDefault().syncExec(new Runnable() {
				public void run() {
//					new UpdataDialog(displayMain, "部门有新动态，已为你更新!",3*1000).open();
				}
			});
		}else if(!message.getMethod().equals(Message.RESPONSE) && !message.getMethod().equals(Message.LOGGIN) && !message.getMethod().equals(Message.SIGNIN)){
			Display.getDefault().syncExec(new Runnable() {
				@Override
				public void run() {
					assignerFrame.getDoingTask();
					authorizerFrame.getAskingTask();
			        InformDialog informDialog =  new InformDialog(displayMain,message,mainFrame);
				}
			});
		}
	}
}
