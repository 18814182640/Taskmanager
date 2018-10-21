package com.TMC.chart;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.wb.swt.SWTResourceManager;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.axis.DateTickUnitType;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.GanttRenderer;
import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;
import org.jfree.data.time.SimpleTimePeriod;
import org.jfree.experimental.chart.swt.ChartComposite;

import com.TMS.model.TaskModel;

public class GanttChartJfree extends Composite{

	private JFreeChart jFreeChart;
	private TaskSeriesCollection taskSeriesCollection;
	public GanttChartJfree(Composite parent){
		super(parent, SWT.NONE);
		this.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		this.setLayout(new FillLayout());
		taskSeriesCollection = new TaskSeriesCollection();
		jFreeChart = ChartFactory.createGanttChart("", "项目", "", taskSeriesCollection, true, true, false);
		CategoryPlot localCategoryPlot = (CategoryPlot)jFreeChart.getPlot();
	    localCategoryPlot.setRangePannable(true); 
	    localCategoryPlot.setDomainGridlinesVisible(true);  //显示网格线
	    localCategoryPlot.setRangeCrosshairVisible(true);
	    localCategoryPlot.getDomainAxis().setMaximumCategoryLabelWidthRatio(10.0F);
	    GanttRenderer localGanttRenderer = (GanttRenderer)localCategoryPlot.getRenderer();
	    localGanttRenderer.setDrawBarOutline(false);  //任务边款
	    DateAxis dateAxis = (DateAxis)localCategoryPlot.getRangeAxis();
	    dateAxis.setAutoTickUnitSelection(false);
	    dateAxis.setRange(new Date(2018, 9, 23), new Date(2018, 9, 29));
	    dateAxis.setStandardTickUnits(DateAxis.createStandardDateTickUnits());
	    dateAxis.setTickUnit(new DateTickUnit(DateTickUnitType.DAY, 1));
	    dateAxis.setDateFormatOverride(new SimpleDateFormat("dd"));
	    
	    ChartComposite frame = new ChartComposite(this, SWT.NONE,jFreeChart,0,0,720,500,
				1000,650,true,true,true,true,true,true);
		frame.setRangeZoomable(true);
		frame.setZoomOutFactor(1.0);
		frame.setZoomInFactor(1.0);
		frame.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		
		upData(new ArrayList<TaskModel>());

		this.layout();
	}
	
	public void upData(List<TaskModel> taskModels){
		 TaskSeries localTaskSeries = new TaskSeries("Scheduled");
		    Task localTask1 = new Task("Write Proposal", date(1, 3, 2001), date(5, 3, 2001));
		    localTask1.setPercentComplete(1.5D);
		    localTaskSeries.add(localTask1);
		    Task localTask2 = new Task("Obtain Approval", date(9, 3, 2001), date(9, 3, 2001));
		    localTask2.setPercentComplete(1.0D);
		    localTaskSeries.add(localTask2);
		    Task localTask3 = new Task("Requirements Analysis", date(10, 3, 2001), date(5, 4, 2001));
		    Task localTask4 = new Task("Requirements 1", date(10, 3, 2001), date(25, 3, 2001));
		    localTask4.setPercentComplete(1.0D);
		    Task localTask5 = new Task("Requirements 2", date(1, 4, 2001), date(5, 4, 2001));
		    localTask5.setPercentComplete(1.0D);
		    localTask3.addSubtask(localTask4);
		    localTask3.addSubtask(localTask5);
		    localTaskSeries.add(localTask3);
		    Task localTask6 = new Task("Design Phase", date(6, 4, 2001), date(30, 4, 2001));
		    Task localTask7 = new Task("Design 1", date(6, 4, 2001), date(10, 4, 2001));
		    localTask7.setPercentComplete(1.0D);
		    Task localTask8 = new Task("Design 2", date(15, 4, 2001), date(20, 4, 2001));
		    localTask8.setPercentComplete(1.0D);
		    Task localTask9 = new Task("Design 3", date(23, 4, 2001), date(30, 4, 2001));
		    localTask9.setPercentComplete(0.5D);
		    localTask6.addSubtask(localTask7);
		    localTask6.addSubtask(localTask8);
		    localTask6.addSubtask(localTask9);
		    localTaskSeries.add(localTask6);
		    Task localTask10 = new Task("Design Signoff", date(2, 5, 2001), date(2, 5, 2001));
		    localTaskSeries.add(localTask10);
		    Task localTask11 = new Task("Alpha Implementation", date(3, 5, 2001), date(31, 6, 2001));
		    localTask11.setPercentComplete(0.6D);
		    localTaskSeries.add(localTask11);
		    Task localTask12 = new Task("Design Review", date(1, 7, 2001), date(8, 7, 2001));
		    localTask12.setPercentComplete(0.0D);
		    localTaskSeries.add(localTask12);
		    Task localTask13 = new Task("Revised Design Signoff", date(10, 7, 2001), date(10, 7, 2001));
		    localTask13.setPercentComplete(0.0D);
		    localTaskSeries.add(localTask13);
		    Task localTask14 = new Task("Beta Implementation", date(12, 7, 2001), date(12, 8, 2001));
		    localTask14.setPercentComplete(0.0D);
		    localTaskSeries.add(localTask14);
		    Task localTask15 = new Task("Testing", date(13, 8, 2001), date(31, 9, 2001));
		    localTask15.setPercentComplete(0.0D);
		    localTaskSeries.add(localTask15);
		    Task localTask16 = new Task("Final Implementation", date(1, 10, 2001), date(15, 10, 2001));
		    localTask16.setPercentComplete(0.0D);
		    localTaskSeries.add(localTask16);
		    Task localTask17 = new Task("Signoff", date(28, 10, 2001), date(30, 10, 2001));
		    localTask17.setPercentComplete(0.0D);
		    localTaskSeries.add(localTask17);
		    taskSeriesCollection.add(localTaskSeries);
	}
	
	  private static Date date(int paramInt1, int paramInt2, int paramInt3)
	  {
	    Calendar localCalendar = Calendar.getInstance();
	    localCalendar.set(paramInt3, paramInt2, paramInt1);
	    Date localDate = localCalendar.getTime();
	    return localDate;
	  }
	
	
}
