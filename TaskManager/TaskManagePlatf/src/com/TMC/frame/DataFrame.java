package com.TMC.frame;

import java.sql.Date;
import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

import com.TMC.Task.QueryAssignTask;
import com.TMC.Task.QueryAuthTaskTask;
import com.TMC.Thread.CellBack;
import com.TMC.Thread.QueueThread;
import com.TMC.Thread.Result;
import com.TMC.Unit.DateUnit;
import com.TMC.chart.BarChart;
import com.TMC.chart.GanttChartSwift;
import com.TMC.frame.Dialog.CommonDialog;
import com.TMS.entity.UserInfoPojo;
import com.TMS.model.TaskModel;
import com.configpath.ConfigPath;

public class DataFrame extends Composite {
	private UserInfoPojo userInfoModel;
	private Shell shell;
	private BarChart barChartPie,barChart;
	private BarChart barChartPie1,barChart1;

	public DataFrame(Composite parent,UserInfoPojo userInfoModel) {
		super(parent, SWT.EMBEDDED);
		shell = parent.getShell();
		this.userInfoModel = userInfoModel;
		setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		setLayout(new FormLayout());
		
		Button button_4 = new Button(this, SWT.NONE);
		button_4.setImage(new Image(getDisplay(), ConfigPath.RefreshImagePath));
		button_4.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				getTask();
			}
		});
		FormData fd_button_4 = new FormData();
		fd_button_4.right = new FormAttachment(100);
		fd_button_4.left = new FormAttachment(100, -80);
		button_4.setLayoutData(fd_button_4);
		button_4.setText("刷新");
		button_4.setForeground(SWTResourceManager.getColor(SWT.COLOR_LINK_FOREGROUND));
		button_4.setFont(SWTResourceManager.getFont("微软雅黑", 9, SWT.BOLD));
		
		
		Label label = new Label(this, SWT.NONE);
		label.setText("甘特图");
		label.setFont(SWTResourceManager.getFont("楷体", 12, SWT.BOLD));
		label.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		FormData fd_label = new FormData();
		fd_label.right = new FormAttachment(100, -904);
		fd_label.left = new FormAttachment(0);
		fd_label.bottom = new FormAttachment(0, 16);
		fd_label.top = new FormAttachment(0);
		label.setLayoutData(fd_label);
		java.util.Date date = DateUnit.getThisMonthStartDate();
		
		
		barChartPie = new BarChart(this,"我的任务("+(new java.util.Date().getMonth()+1)+"月)");
		FormData fd_barChartPie = new FormData();
		fd_barChartPie.top = new FormAttachment(label, 30);
		fd_barChartPie.left = new FormAttachment(label, 0, SWT.LEFT);
		fd_barChartPie.bottom = new FormAttachment(50, 0);
		fd_barChartPie.right = new FormAttachment(30,0);
		barChartPie.setLayoutData(fd_barChartPie);
		barChartPie.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		
		barChart = new BarChart(this, "我的任务(All)", "", "");
		fd_button_4.bottom = new FormAttachment(barChart, -2);
		FormData fd_barChart = new FormData();
		fd_barChart.bottom = new FormAttachment(barChartPie, 0, SWT.BOTTOM);
		fd_barChart.right = new FormAttachment(100, -10);
		fd_barChart.left = new FormAttachment(barChartPie, 6);
		fd_barChart.top = new FormAttachment(0, 46);
		barChart.setLayoutData(fd_barChart);
		
		barChartPie1 = new BarChart(this,"我分配的任务("+(new java.util.Date().getMonth()+1)+"月)");
		barChartPie1.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		FormData fd_barChart1 = new FormData();
		fd_barChart1.left = new FormAttachment(barChartPie, 0, SWT.LEFT);
		fd_barChart1.right = new FormAttachment(barChartPie, 0, SWT.RIGHT);
		fd_barChart1.top = new FormAttachment(barChartPie, 6);
		fd_barChart1.bottom = new FormAttachment(100, -10);
		barChartPie1.setLayoutData(fd_barChart1);
		
		barChart1 = new BarChart(this, "我分配的任务(All)", "", "");
		FormData fd_barChart11 = new FormData();
		fd_barChart11.top = new FormAttachment(barChartPie1, 0, SWT.TOP);
		fd_barChart11.right = new FormAttachment(100, -10);
		fd_barChart11.left = new FormAttachment(barChartPie1, 6);
		fd_barChart11.bottom = new FormAttachment(100, -10);
		barChart1.setLayoutData(fd_barChart11);
		
		
		getTask();
		this.layout();
	}
	

	public void getTask(){
//		getTaskonGantt();
		getThisMonthTask();
		getAllTask();
		getThisMonthTask1();
		getAllTask1();
	}
	
	
	
	/*public void getTaskonGantt(){
		String time = dateTime.getYear()+"-"+(dateTime.getMonth()+1)+"-"+dateTime.getDay();
		final Date date = Date.valueOf(time);
		QueryAssignTask queryAssignTaskTask = new QueryAssignTask(userInfoModel,null,date.getTime(), new CellBack() {
			@Override
			public void cellback(Result result) {
				if(result.isSuccessful()){
					final ArrayList<TaskModel> taskModels = (ArrayList<TaskModel>) result.getData();
					if(taskModels!=null){
						Display.getDefault().syncExec(new Runnable() {
							@Override
							public void run() {
								ganttChart.upData(date,taskModels);
							}
						});
					}
				}else{
					CommonDialog.showMessageSyn(shell, "提示", result.getReason(), SWT.ERROR);
				}
			}
		});
		QueueThread.getInstance().AddThreadInQueue(queryAssignTaskTask);
	}*/
	
	
	
	
	public void getThisMonthTask(){
		java.util.Date thismonth = new java.util.Date();
		String time = thismonth.getYear()+1900+"-"+(thismonth.getMonth()+1)+"-1";
		Date date = Date.valueOf(time);
		QueryAssignTask queryAssignTaskTask = new QueryAssignTask(userInfoModel,null,date.getTime(), new CellBack() {
			@Override
			public void cellback(Result result) {
				if(result.isSuccessful()){
					final ArrayList<TaskModel> taskModels = (ArrayList<TaskModel>) result.getData();
					if(taskModels!=null){
						Display.getDefault().syncExec(new Runnable() {
							@Override
							public void run() {
								barChartPie.showBar(taskModels);
							}
						});
					}
				}else{
					CommonDialog.showMessageSyn(shell, "提示", result.getReason(), SWT.ERROR);
				}
			}
		});
		QueueThread.getInstance().AddThreadInQueue(queryAssignTaskTask);
	}
	
	
	public void getAllTask(){
		String time = "1900-01-01";
		Date date = Date.valueOf(time);
		QueryAssignTask queryAssignTaskTask = new QueryAssignTask(userInfoModel,null,date.getTime(), new CellBack() {
			@Override
			public void cellback(Result result) {
				if(result.isSuccessful()){
					final ArrayList<TaskModel> taskModels = (ArrayList<TaskModel>) result.getData();
					if(taskModels!=null){
						Display.getDefault().syncExec(new Runnable() {
							@Override
							public void run() {
								barChart.showBar2(taskModels);
							}
						});
					}
				}else{
					CommonDialog.showMessageSyn(shell, "提示", result.getReason(), SWT.ERROR);
				}
			}
		});
		QueueThread.getInstance().AddThreadInQueue(queryAssignTaskTask);
	}
	
	
	public void getThisMonthTask1(){
		java.util.Date thismonth = new java.util.Date();
		String time = thismonth.getYear()+1900+"-"+(thismonth.getMonth()+1)+"-1";
		Date date = Date.valueOf(time);
		QueryAuthTaskTask queryAuthTaskTask = new QueryAuthTaskTask(userInfoModel,null,date.getTime(), new CellBack() {
			@Override
			public void cellback(final Result result) {
				Display.getDefault().syncExec(new Runnable() {
					@Override
					public void run() {
						if(result.isSuccessful()){
							ArrayList<TaskModel> taskModels = (ArrayList<TaskModel>) result.getData();
							if(taskModels!=null){
							   barChartPie1.showBar(taskModels);
							}
						}else{
							CommonDialog.showMessage(shell, "提示", result.getReason(), SWT.ERROR);
						}
					}
				});
			}
		});
		QueueThread.getInstance().AddThreadInQueue(queryAuthTaskTask);
	}
	
	
	public void getAllTask1(){
		String time = "1900-01-01";
		Date date = Date.valueOf(time);
		QueryAuthTaskTask queryAuthTaskTask = new QueryAuthTaskTask(userInfoModel,null,date.getTime(), new CellBack() {
			@Override
			public void cellback(Result result) {
				if(result.isSuccessful()){
					final ArrayList<TaskModel> taskModels = (ArrayList<TaskModel>) result.getData();
					if(taskModels!=null){
						Display.getDefault().syncExec(new Runnable() {
							@Override
							public void run() {
								barChart1.showBar2(taskModels);
							}
						});
					}
				}else{
					CommonDialog.showMessageSyn(shell, "提示", result.getReason(), SWT.ERROR);
				}
			}
		});
		QueueThread.getInstance().AddThreadInQueue(queryAuthTaskTask);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
