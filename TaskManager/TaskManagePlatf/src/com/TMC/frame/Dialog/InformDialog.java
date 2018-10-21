package com.TMC.frame.Dialog;

import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.internal.win32.OS;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import com.TMC.frame.MainFrame;
import com.TMS.entity.TaskInfoPojo;
import com.TMS.entity.UserInfoPojo;
import com.TMS.model.Message;
import com.TMS.model.TaskModel;
import com.config.Config;
import com.configpath.ConfigPath;

public class InformDialog{
	private Shell shell;
    private Message message;
    private Text text;
    private Button btnNewButton;
    private Button btnNewButton_1;
    private MainFrame mainFrame;
    private Table table;
    private TableItem tableItem3;
    private TableItem tableItem4;
    private TableItem tableItem5;
    private TableItem tableItem1;
    private TableItem tableItem2;
    private TableColumn tblclmnNewColumn;
    private TableColumn tblclmnNewColumn_1;
    

	public InformDialog(Display parent,Message message,MainFrame mainFrame) {
		this.message = message;
		this.mainFrame = mainFrame;
		shell = new Shell(parent,SWT.NO_TRIM);
		shell.setImage(new Image(parent, ConfigPath.RemindImagePath));
		shell.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		shell.setText("提示");
		int screenH = Toolkit.getDefaultToolkit().getScreenSize().height;
		int screenW = Toolkit.getDefaultToolkit().getScreenSize().width;
//		shell.setBackgroundImage(new Image(shell.getDisplay(), ConfigPath.ICOImagePath));
		shell.setSize(300,174);
		OS.SetWindowPos(shell.handle,OS.HWND_TOPMOST,screenW-shell.getSize().x, screenH-shell.getSize().y-50,
				shell.getSize().x, shell.getSize().y, SWT.NULL);
		text = new Text(shell, SWT.WRAP | SWT.MULTI);
		text.setText("你有一个新动态:");
		text.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		text.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_RED));
		text.setFont(SWTResourceManager.getFont("楷体", 9, SWT.BOLD));
		text.setBounds(10, 23, 200, 17);
		
		btnNewButton = new Button(shell, SWT.NONE);
		btnNewButton.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_GREEN));
		btnNewButton.setFont(SWTResourceManager.getFont("楷体", 9, SWT.NORMAL));
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				List<Object> taskModels = InformDialog.this.message.getContext();
				if(taskModels!=null && taskModels.size()>0){
					TaskModel taskModel = (TaskModel) taskModels.get(0);
					ShowTaskDialog showTaskDialog ;
					switch (InformDialog.this.message.getMethod()){
						case Message.ASKFINISH:
						case Message.EDITTASK:	 
							showTaskDialog = new ShowTaskDialog(shell,taskModel, 0);
							showTaskDialog.open();
							break;
							/*case Message.CREATE_USER_TASK:
							InformDialog.this.mainFrame.showNewTask();
							break;
						case Message.ASKFINISH:
							InformDialog.this.mainFrame.showAuthTask();
							break;
						case Message.EDITTASK:
							InformDialog.this.mainFrame.showAuthTask();
							break;*/
						default:
							showTaskDialog = new ShowTaskDialog(shell,taskModel,1);
							showTaskDialog.open();
							break;
					}
				}
				shell.dispose();
			}
		});
		btnNewButton.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_INACTIVE_FOREGROUND));
		btnNewButton.setBounds(220, 23, 80, 27);
		btnNewButton.setText("查看");
		
		btnNewButton_1 = new Button(shell, SWT.NONE);
		btnNewButton_1.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_INACTIVE_FOREGROUND));
		btnNewButton_1.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		btnNewButton_1.setFont(SWTResourceManager.getFont("楷体", 9, SWT.NORMAL));
		btnNewButton_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.dispose();
			}
		});
		btnNewButton_1.setBounds(220, 56, 80, 27);
		btnNewButton_1.setText("忽略");
		
		Label lblNewLabel = new Label(shell, SWT.NONE);
		lblNewLabel.setFont(SWTResourceManager.getFont("微软雅黑", 10, SWT.BOLD));
		lblNewLabel.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		lblNewLabel.setBounds(0, 0, 36, 17);
		lblNewLabel.setText("提示:");
		
		table = new Table(shell, SWT.BORDER);
		table.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		table.setBounds(10, 46, 200, 128);
		table.setLinesVisible(true);
		
		tableItem1 = new TableItem(table, SWT.NONE);
		tableItem1.setText("分配人");
		
		tableItem2 = new TableItem(table, SWT.NONE);
		tableItem2.setText("执行人");
		
		tableItem3 = new TableItem(table, SWT.NONE);
		tableItem3.setText("任务详细");
		
		tableItem4 = new TableItem(table, SWT.NONE);
		tableItem4.setText("SVN路径");
		
		tableItem5 = new TableItem(table, SWT.NONE);
		tableItem5.setText("任务状态");
		
		tblclmnNewColumn = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn.setWidth(60);
		tblclmnNewColumn.setText("New Column");
		
		tblclmnNewColumn_1 = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn_1.setWidth(135);
		tblclmnNewColumn_1.setText("New Column");
		
		show();
		shell.open();
		
	}
	
	
	private void show(){
		List<Object> taskModels =  message.getContext();
		if(taskModels!=null && taskModels.size()>0){
			TaskModel taskModel = (TaskModel) taskModels.get(0);
			UserInfoPojo assiger = taskModel.getAssigner();
			UserInfoPojo author = taskModel.getAuthorizer();
			TaskInfoPojo taskInfoPojo = taskModel.getTask();
			if (assiger!=null && author!=null) {
				tableItem1.setText(1,author.getReal_name()+"");
				tableItem2.setText(1,assiger.getReal_name()+"");
				tableItem3.setText(1,taskInfoPojo.getTask_info()+"");
				tableItem4.setText(1,taskInfoPojo.getSvn_path()+"");
				tableItem5.setText(1,Config.TASK_STATUS.getEelenment(taskInfoPojo.getStatus_id()).getInfo()+"");
			}
		}
		table.layout();
		/*switch (message.getMethod()) {
		case Message.CREATE_USER_TASK: 
			showNowTask();
			break;
		case Message.ASKFINISH: 
			showAskFinish();
			break;
		case Message.EDITTASK: 
			showEditTask();
			break;	
		default:
			break;
		}*/
	
	}
	
	
	private void showNowTask(){
		List<Object> taskModels =  message.getContext();
		if(taskModels!=null && taskModels.size()>0){
			TaskModel taskModel = (TaskModel) taskModels.get(0);
			UserInfoPojo assiger = taskModel.getAssigner();
			UserInfoPojo author = taskModel.getAuthorizer();
			if (assiger!=null && author!=null) {
				text.setText(author.getReal_name()+"给你分配了一个新任务！\r\n任务详情:\r\n"+taskModel.getTask().getTask_info());
			}
		}
	}
	
	private void showAskFinish(){
		ArrayList<Object> taskModels =  message.getContext();
		if(taskModels!=null && taskModels.size()>0){
			TaskModel taskModel = (TaskModel) taskModels.get(0);
			UserInfoPojo assiger = taskModel.getAssigner();
			UserInfoPojo author = taskModel.getAuthorizer();
			if (assiger!=null && author!=null) {
				text.setText("你给"+assiger.getReal_name()+"分配的任务申请完成！\r\n任务详情:\r\n"+taskModel.getTask().getTask_info());
			}
		}
	}
	
	
	private void showEditTask(){
		List<Object> taskModels =   message.getContext();
		if(taskModels!=null && taskModels.size()>0){
			TaskModel taskModel = (TaskModel) taskModels.get(0);
			UserInfoPojo assiger = taskModel.getAssigner();
			UserInfoPojo author = taskModel.getAuthorizer();
			if (assiger!=null && author!=null) {
				text.setText(author.getReal_name()+"给你分配的任务有新动态!\r\n任务详情:\r\n"+taskModel.getTask().getTask_info());
			}
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
