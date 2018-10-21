package com.TMC.chart;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.ColorModel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.wb.swt.SWTResourceManager;
import org.swiftgantt.Config;
import org.swiftgantt.GanttChart;
import org.swiftgantt.common.EventLogger;
import org.swiftgantt.common.Time;
import org.swiftgantt.model.GanttModel;
import org.swiftgantt.model.Task;
import org.swiftgantt.ui.TimeUnit;

import com.TMC.Cache.DataCache;
import com.TMS.entity.TaskInfoPojo;
import com.TMS.model.TaskModel;
import javax.swing.ScrollPaneConstants;
import java.awt.BorderLayout;
import java.awt.Color;

public class GanttChartSwift extends Composite{

	private GanttChart ganttChart;
	private Config config;
	private GanttModel ganttModel;
	private Frame frame;
	private long maxTime;
	private long minTime = System.currentTimeMillis();
	private SimpleDateFormat format =new SimpleDateFormat("yyyy年MM月dd日");
	
	public GanttChartSwift(Composite parent){
		super(parent, SWT.EMBEDDED);
		this.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		this.setLayout(new FillLayout());
		
		
	}
	
	public void upData(java.sql.Date date,List<TaskModel> taskModels){
		ganttModel = new GanttModel();
		ganttModel.removeAll();
		ArrayList<Task> tasks = new ArrayList<>();
		if(taskModels!=null){
			for (int i = 0; i < taskModels.size(); i++) {
				TaskModel temp = taskModels.get(i);
				if(temp!=null){
					TaskInfoPojo taskInfoPojo = temp.getTask();
					if(taskInfoPojo!=null){
						String itemName = taskInfoPojo.getTask_info();
						minTime = taskInfoPojo.getStart_time()<minTime? taskInfoPojo.getStart_time() : minTime;
						maxTime = taskInfoPojo.getEnd_time()>maxTime? taskInfoPojo.getEnd_time() : maxTime;
						Time start = getGregorianCalendar(taskInfoPojo.getStart_time());
						Time end = getGregorianCalendar(taskInfoPojo.getEnd_time());
						Task task = new Task(itemName,start, end);
//						System.err.println(itemName+":"+format.format(new Date(taskInfoPojo.getStart_time()))+"---"+format.format(taskInfoPojo.getEnd_time()));
						switch (taskInfoPojo.getStatus_id()) {
						   case 0: 
								   int p = getProgress(taskInfoPojo);
			 					   if(p==-1){
									 	task.setBackcolor(Color.RED);
								   }else{
										task.setProgress(p);
								   } break;
						   case 1: task.setBackcolor(Color.YELLOW); break;
						   case 2: task.setBackcolor(Color.GREEN);break;
						   case 3: task.setBackcolor(Color.DARK_GRAY); break; 
						   default:break;
						}
						tasks.add(task);
					}
				}
			}
		}
		Task [] ts = new Task[tasks.size()];
		tasks.toArray(ts);
//		System.err.println("min:"+format.format(new Date(minTime))+"---max:"+format.format(new Date(maxTime)));
		Task mainTask = new Task(format.format(date)+"起的任务", getGregorianCalendar(minTime), getGregorianCalendar(maxTime), null);
		mainTask.add(ts);
		ganttModel.addTask(mainTask);
		ganttModel.setKickoffTime(getKickoffTime(minTime));
		ganttModel.setDeadline(getDeadline(maxTime));
		if(ganttChart!=null){
			ganttChart.removeAll();
		}
		ganttChart = new GanttChart();
		ganttChart.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		ganttChart.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		ganttChart.setModel(ganttModel);
		config = ganttChart.getConfig();
		config.setDeadlineBackColor(Color.RED);
		config.setKickoffTimeBackColor(Color.BLUE);
		config.setProgressBarBackColor(Color.orange);
		config.setRestoutTimeBackColor(Color.WHITE);
		config.setWorkingTimeBackColor(Color.GRAY);
//		config.setTaskTreeViewBackColor(Color.RED);
		if(frame!=null){
			frame.dispose();
		}
		frame = SWT_AWT.new_Frame(this);
		frame.add(ganttChart, BorderLayout.CENTER);
		ganttChart.updateUI();
		this.layout();
		
		
	}
	
	private Time getGregorianCalendar(long date){
		Date date2 = new Date(date);
		return new Time(date2.getYear()+1900, date2.getMonth()+1, date2.getDay()+15, date2.getHours(), date2.getMinutes(), date2.getSeconds());
	}
	
	private Time getKickoffTime(long date){
		/*Calendar calendar = Calendar.getInstance(Locale.CHINA);
		calendar.setTime(new Date(date));
		int dayofweek = calendar.get(Calendar.DAY_OF_WEEK);
		calendar.add(Calendar.DATE, -7-dayofweek+1);*/
		Date date2 = new Date(date);//calendar.getTime();
		return new Time(date2.getYear()+1900, date2.getMonth()+1, date2.getDay()+15, date2.getHours(), date2.getMinutes(), date2.getSeconds());
	}
	
	private Time getDeadline(long date){
		Date date2 = new Date(date);
		return new Time(date2.getYear()+1900, date2.getMonth()+1, date2.getDay()+15, date2.getHours(), date2.getMinutes(), date2.getSeconds());
	}
	
	private int getProgress(TaskInfoPojo taskInfoPojo){
		if(taskInfoPojo !=null){
			long start = taskInfoPojo.getStart_time();
			long end = taskInfoPojo.getEnd_time();
			long space = end - start;
			if(taskInfoPojo.getStatus_id()!=0){   
				return (int)(space/(60*60*1000*24));  //完成
			}else{        //doing
				long now = System.currentTimeMillis();
				if(now>end){   //逾期
					return -1;
				}else{
					return (int)((now-start)/(60*60*1000*24));  //进度
				}
			}
		}else{
			return 0;
		}
	}
	
}
