package com.TMC.frame.Dialog;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.wb.swt.SWTResourceManager;

import com.TMC.Cache.DataCache;
import com.TMC.Task.QueryUserTask;
import com.TMC.Thread.CellBack;
import com.TMC.Thread.QueueThread;
import com.TMC.Thread.Result;
import com.TMC.frame.MainFrame;
import com.TMS.entity.ItemInfoPojo;
import com.TMS.entity.TaskInfoPojo;
import com.TMS.entity.UserInfoPojo;
import com.TMS.model.TaskModel;
import com.configpath.ConfigPath;

import sun.applet.Main;

public class CreateTaskDialog extends Dialog {

	private Shell shlservice;
	private TaskModel taskModel;
	private Text text;
	private DateTime startDate,startTime,endDate;
	private int model;
	private String name;
	private DateTime deadLine;
	private Control control;
	private Combo combo;
	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private DateTime endTime;

	/**
	 * @wbp.parser.constructor
	 */
	public CreateTaskDialog(Shell parent,String name) {
		super(parent);
		this.name = name;
		model = 1;
	}
	
	public CreateTaskDialog(Shell parent,DateTime deadLine) {
		super(parent);
		this.deadLine = deadLine;
		model = 2;
	}
	
	public CreateTaskDialog(Shell parent) {
		super(parent);
		model = 3;
	}
  
	public TaskModel open() {
		shlservice = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		shlservice.addShellListener(new ShellAdapter() {
			@Override
			public void shellClosed(ShellEvent e) {
				taskModel = null;
			}
		});
		shlservice.setImage(new Image(shlservice.getDisplay(), ConfigPath.AddImagePath));
		shlservice.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT));
		shlservice.setSize(486, 588);
		shlservice.setText("分配任务");
		shlservice.setLocation(getParent().getLocation().x + getParent().getSize().x / 2 - shlservice.getSize().x / 2,
				getParent().getLocation().y + getParent().getSize().y / 2 - shlservice.getSize().y / 2);
		shlservice.setLayout(new FillLayout());

		Composite composite = new Composite(shlservice, SWT.BORDER);
		composite.setFont(SWTResourceManager.getFont("楷体", 11, SWT.NORMAL));
		composite.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		composite.setLayout(new FormLayout());

		Button okButton = new Button(composite, SWT.CENTER);
		okButton.setImage(new Image(shlservice.getDisplay(), ConfigPath.AddImagePath));
		okButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				newTask();
			}
		});
		FormData fd_okButton = new FormData();
		fd_okButton.bottom = new FormAttachment(100, -10);
		fd_okButton.left = new FormAttachment(50, -85);
		fd_okButton.right = new FormAttachment(50, -5);
		okButton.setLayoutData(fd_okButton);
		okButton.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		okButton.setText("Add");

		Button cancelButton = new Button(composite, SWT.CENTER);
		cancelButton.setImage(new Image(shlservice.getDisplay(), ConfigPath.RemoveImagePath));
		cancelButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				taskModel = null;
				shlservice.dispose();
			}
		});
		cancelButton.setText("Cancel");
		FormData fd_button = new FormData();
		fd_button.top = new FormAttachment(okButton, 0, SWT.TOP);
		fd_button.right = new FormAttachment(50, 80);
		fd_button.left = new FormAttachment(50, 5);
		cancelButton.setLayoutData(fd_button);
		cancelButton.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		
		Label lblNewLabel = new Label(composite, SWT.NONE);
		lblNewLabel.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		lblNewLabel.setFont(SWTResourceManager.getFont("楷体", 11, SWT.BOLD));
		FormData fd_lblNewLabel = new FormData();
		fd_lblNewLabel.left = new FormAttachment(0, 10);
		lblNewLabel.setLayoutData(fd_lblNewLabel);
		lblNewLabel.setText("任务详情");
		
		text = new Text(composite, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		FormData fd_text = new FormData();
		fd_text.top = new FormAttachment(0, 99);
		fd_text.right = new FormAttachment(100, -10);
		fd_text.left = new FormAttachment(0, 10);
		text.setLayoutData(fd_text);
		
		startDate = new DateTime(composite, SWT.BORDER);
		FormData fd_startDate = new FormData();
		fd_startDate.bottom = new FormAttachment(okButton, -10);
		fd_startDate.left = new FormAttachment(0, 10);
		startDate.setLayoutData(fd_startDate);
		
		Label lblNewLabel_1 = new Label(composite, SWT.NONE);
		fd_text.bottom = new FormAttachment(lblNewLabel_1, -6);
		fd_startDate.top = new FormAttachment(lblNewLabel_1, 8);
		lblNewLabel_1.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		lblNewLabel_1.setFont(SWTResourceManager.getFont("楷体", 11, SWT.BOLD));
		FormData fd_lblNewLabel_1 = new FormData();
		fd_lblNewLabel_1.bottom = new FormAttachment(okButton, -42);
		fd_lblNewLabel_1.left = new FormAttachment(0, 10);
		lblNewLabel_1.setLayoutData(fd_lblNewLabel_1);
		lblNewLabel_1.setText("计划开始时间");
		
		startTime = new DateTime(composite, SWT.BORDER | SWT.TIME);
		FormData fd_startTime = new FormData();
		fd_startTime.top = new FormAttachment(startDate, 0, SWT.TOP);
		fd_startTime.left = new FormAttachment(startDate, 6);
		startTime.setLayoutData(fd_startTime);
		
		Label label = new Label(composite, SWT.NONE);
		label.setText("计划结束时间");
		label.setFont(SWTResourceManager.getFont("楷体", 11, SWT.BOLD));
		label.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		FormData fd_label = new FormData();
		fd_label.left = new FormAttachment(lblNewLabel_1, 190);
		fd_label.right = new FormAttachment(100, -54);
		fd_label.top = new FormAttachment(text, 6);
		label.setLayoutData(fd_label);
		
		endDate = new DateTime(composite, SWT.BORDER);
		FormData fd_endDate = new FormData();
		fd_endDate.top = new FormAttachment(startDate, 0, SWT.TOP);
		endDate.setLayoutData(fd_endDate);
		
		endTime = new DateTime(composite, SWT.BORDER | SWT.TIME);
		fd_endDate.right = new FormAttachment(endTime, -6);
		FormData fd_endTime = new FormData();
		fd_endTime.top = new FormAttachment(label, 8);
		fd_endTime.right = new FormAttachment(100, -10);
		endTime.setLayoutData(fd_endTime);
		
		Button btnNewButton = new Button(composite, SWT.CENTER);
		fd_lblNewLabel.top = new FormAttachment(btnNewButton, 6, SWT.TOP);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				text.setText("");
			}
		});
		btnNewButton.setImage(new Image(shlservice.getDisplay(), ConfigPath.ClearImagePath));
		FormData fd_btnNewButton = new FormData();
		fd_btnNewButton.left = new FormAttachment(lblNewLabel, 313);
		fd_btnNewButton.right = new FormAttachment(100, -10);
		fd_btnNewButton.bottom = new FormAttachment(text, -4);
		btnNewButton.setLayoutData(fd_btnNewButton);
		btnNewButton.setText("Clean");
		
		Label label_1 = new Label(composite, SWT.NONE);
		label_1.setText("执行人:");
		label_1.setFont(SWTResourceManager.getFont("楷体", 11, SWT.BOLD));
		label_1.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		FormData fd_label_1 = new FormData();
		fd_label_1.left = new FormAttachment(0, 10);
		label_1.setLayoutData(fd_label_1);
		fd_label_1.top = new FormAttachment(0, 9);
		
		Label label_2 = new Label(composite, SWT.NONE);
		label_2.setText("所属项目:");
		label_2.setFont(SWTResourceManager.getFont("楷体", 11, SWT.BOLD));
		label_2.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		FormData fd_label_2 = new FormData();
		fd_label_2.left = new FormAttachment(label_1, 0, SWT.LEFT);
		fd_label_2.bottom = new FormAttachment(lblNewLabel, -16);
		label_2.setLayoutData(fd_label_2);
		
		if (model==1) {
			control = new Label(composite, SWT.NONE);
			((Label)control).setText(name);
		}else if(model==2){
			control = new Combo(composite, SWT.BORDER|SWT.READ_ONLY);
			endDate.setYear(deadLine.getYear());
			endDate.setMonth(deadLine.getMonth());
			endDate.setDay(deadLine.getDay());
//			endDate.setDate(deadLine.getYear(), deadLine.getMonth(), deadLine.getDay());
			endDate.update();
			queryUser();
		}else{
			control = new Combo(composite, SWT.BORDER|SWT.READ_ONLY);
			queryUser();
		}
		FormData fd_lblNewLabel_2 = new FormData();
		fd_lblNewLabel_2.right = new FormAttachment(startTime, 51, SWT.RIGHT);
		fd_lblNewLabel_2.top = new FormAttachment(label_1, -1, SWT.TOP);
		control.setLayoutData(fd_lblNewLabel_2);
		control.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		
		combo = new Combo(composite, SWT.READ_ONLY);
		fd_lblNewLabel_2.left = new FormAttachment(combo, 0, SWT.LEFT);
		FormData fd_combo = new FormData();
		fd_combo.right = new FormAttachment(100, -251);
		fd_combo.left = new FormAttachment(label_2);
		fd_combo.top = new FormAttachment(label_2, -5, SWT.TOP);
		combo.setLayoutData(fd_combo);
		refreshItemCombo();

		shlservice.layout();
		shlservice.open();
		Display display = shlservice.getDisplay();
		while (!shlservice.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return taskModel;
	}

	private void newTask() {
		taskModel = new TaskModel();
		TaskInfoPojo taskInfoPojo = new TaskInfoPojo();
		String taskInfo = text.getText();
		int ind = combo.getSelectionIndex();
		if(ind<0){
			CommonDialog.showMessage(shlservice, "提示", "请选择所属项目!", SWT.ICON_INFORMATION);
			return ;
		}
		int itemId = DataCache.getItemId(ind);
		if(itemId>-1){
			taskInfoPojo.setItem_id(itemId);
		}else{
			CommonDialog.showMessage(shlservice, "提示", "获取项目ID失败!", SWT.ERROR);
		}
		if(StringUtils.isNotEmpty(taskInfo)){
			taskInfoPojo.setTask_info(text.getText());
		}else{
			CommonDialog.showMessage(shlservice, "提示", "请输入任务", SWT.ICON_INFORMATION);
			taskInfoPojo =null;
			return ;
		}
		long start=0,stop=0;
		try {
			start = format.parse(startDate.getYear()+"-"+(startDate.getMonth()+1)+"-"+startDate.getDay()+" "+startTime.getHours()+":"+startTime.getMinutes()+":"+startTime.getSeconds()).getTime();
			stop = format.parse(endDate.getYear()+"-"+(endDate.getMonth()+1)+"-"+endDate.getDay()+" "+endTime.getHours()+":"+endTime.getMinutes()+":"+endTime.getSeconds()).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
			shlservice.dispose();
		}
//		System.err.println("startDate:---"+startDate.getYear()+"-"+(startDate.getMonth()+1)+"-"+startDate.getDay()+" "+startTime.getHours()+":"+startTime.getMinutes()+":"+startTime.getSeconds());
//		System.err.println("enddata:-----"+endDate.getYear()+"-"+(endDate.getMonth()+1)+"-"+endDate.getDay()+" "+endTime.getHours()+":"+endTime.getMinutes()+":"+endTime.getSeconds());
		/*if(start<=System.currentTimeMillis()){
			CommonDialog.showMessage(shlservice, "提示", "开始时间不能早于当前时间", SWT.ERROR);
			taskInfoPojo = null;
			return;
		}*/
		if(start>=stop){
			CommonDialog.showMessage(shlservice, "提示", "开始时间不能早于结束时间", SWT.ERROR);
			taskInfoPojo = null;
			return;
		}
		taskInfoPojo.setStart_time(start);
		taskInfoPojo.setEnd_time(stop);
		taskModel.setTask(taskInfoPojo);
		if(model==2 || model==3){
			int select = ((Combo)control).getSelectionIndex();
			if(select<0){
				CommonDialog.showMessage(shlservice, "提示", "请选择执行人!", SWT.ERROR);
				taskInfoPojo = null;
				return;
			}
			ArrayList<TaskModel> taskModels = (ArrayList<TaskModel>) control.getData();
			taskModel.setAssigner(taskModels.get(select).getAssigner());
		}
		shlservice.dispose();
	}
	
	
	public void queryUser(){
		QueryUserTask queryUserTask = new QueryUserTask("", new CellBack(){
			@Override
			public void cellback(Result result) {
				if(result.isSuccessful()){
					ArrayList<TaskModel> uPojos = (ArrayList<TaskModel>) result.getData();
					if(uPojos!=null){
						refreshCombo(uPojos);
					}else{
						CommonDialog.showMessageSyn(shlservice, "提示", result.getReason(), SWT.ERROR);
					}
				}
			}
		});
		QueueThread.getInstance().AddThreadInQueue(queryUserTask);
	}
	private void refreshCombo(final ArrayList<TaskModel> taskModels){
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run(){
				if(taskModels!=null){
					ArrayList< String> lists = new ArrayList<>();
					for (int i = 0; i < taskModels.size(); i++) {
						TaskModel  taskModel = taskModels.get(i);
						UserInfoPojo userInfoPojo = taskModel.getAssigner();
						if(userInfoPojo!=null){
							lists.add(userInfoPojo.getUser_name()+" "+userInfoPojo.getReal_name());
						}
					}
					String [] temps = new String[lists.size()];
					lists.toArray(temps);
					((Combo)control).setItems(temps);
					control.setData(taskModels);
				}
			}
		});
	}
	
	
	

	private void refreshItemCombo() {
		String [] names = DataCache.getItemNames();
		if(names!=null){
			combo.setItems(names);
		}
	}
	
	
	
	
	
}
