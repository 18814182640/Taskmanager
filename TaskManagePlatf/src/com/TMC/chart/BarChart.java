package com.TMC.chart;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.wb.swt.SWTResourceManager;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.experimental.chart.swt.ChartComposite;
import org.jfree.ui.TextAnchor;

import com.TMS.entity.TaskInfoPojo;
import com.TMS.model.TaskModel;

public class BarChart extends Composite{

	private JFreeChart jFreeChart;
	private DefaultPieDataset categoryDataset;
	private DefaultCategoryDataset defaultCategoryDataset; 
	
	public BarChart(Composite parent,String title){
		super(parent,SWT.NONE);
		this.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		this.setLayout(new FillLayout());
		categoryDataset = new DefaultPieDataset();
		jFreeChart = ChartFactory.createPieChart3D(title, categoryDataset, true, true, false);
		ChartComposite frame = new ChartComposite(this, SWT.NONE,jFreeChart,0,0,250,200,
				450,450,true,true,true,true,true,true);
		frame.setRangeZoomable(true);
		frame.setZoomOutFactor(1.0);
		frame.setZoomInFactor(1.0);
		frame.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		this.layout();
	}
	
	
	/**
	 * @wbp.parser.constructor
	 */
	public BarChart(Composite parent,String title,String v,String h){
		super(parent,SWT.NONE);
		this.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		this.setLayout(new FillLayout());
		defaultCategoryDataset = new DefaultCategoryDataset();
		jFreeChart = ChartFactory.createBarChart3D(title, h, v, defaultCategoryDataset);
		CategoryPlot localCategoryPlot = (CategoryPlot)jFreeChart.getPlot();
	    CategoryAxis localCategoryAxis = localCategoryPlot.getDomainAxis();
	    localCategoryAxis.setCategoryLabelPositions(CategoryLabelPositions.createUpRotationLabelPositions(0.39269908169872414D));
	    CategoryItemRenderer localCategoryItemRenderer = localCategoryPlot.getRenderer();
	    localCategoryItemRenderer.setBaseItemLabelsVisible(true);
	    BarRenderer localBarRenderer = (BarRenderer)localCategoryItemRenderer;
	    localBarRenderer.setItemMargin(0.2D);
	    localBarRenderer.setMaximumBarWidth(50.0D);
	    localBarRenderer.setMinimumBarLength(0.1D);
	    localBarRenderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
	    localBarRenderer.setBaseItemLabelsVisible(true);
	    localBarRenderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_LEFT));
	    NumberAxis localNumberAxis = (NumberAxis)localCategoryPlot.getRangeAxis();
	    localNumberAxis.setNumberFormatOverride(NumberFormat.getIntegerInstance());
	    localNumberAxis.setUpperMargin(1D);
	    ChartComposite frame = new ChartComposite(this, SWT.NONE,jFreeChart,0,0,290,250,
				450,450,true,true,true,true,true,true);
		frame.setRangeZoomable(true);
		frame.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		this.layout();
	}
	
	
	
	
	public void showBar(ArrayList<TaskModel> taskModels){
		 getCategoryDataset(getMap(taskModels));
	}
	
	public void showBar2(ArrayList<TaskModel> taskModels){
		getDefaultCategoryDataset(getMap(taskModels));
	}
	
	
	private void getCategoryDataset(LinkedHashMap<String, Number> hashMap){
		categoryDataset.clear();
		Set<String> set = hashMap.keySet();
		Iterator< String> iterator = set.iterator();
		while (iterator.hasNext()) {
			String string = (String) iterator.next();
			Number number = hashMap.get(string);
			categoryDataset.setValue(string,number);
		}
	}
	
	
	private void getDefaultCategoryDataset(LinkedHashMap<String, Number> hashMap){
		defaultCategoryDataset.clear();
		Set<String> set = hashMap.keySet();
		Iterator< String> iterator = set.iterator();
		while (iterator.hasNext()) {
			String string = (String) iterator.next();
			Number number = hashMap.get(string);
			defaultCategoryDataset.setValue(number, string, "");
		}
	}
	
	private LinkedHashMap<String, Number> getMap(ArrayList<TaskModel> list){
		LinkedHashMap< String, Number> hashMap = new LinkedHashMap<>();
		int doing = 0;
		int asking = 0;
		int finalied = 0;
		int close =0;
		for (int i = 0; i < list.size(); i++) {
			TaskModel taskModel = list.get(i);
			TaskInfoPojo taskInfoPojo = taskModel.getTask();
			switch (taskInfoPojo.getStatus_id()) {
			case 0: doing++;break;
			case 1: asking++;break;
			case 2: finalied++;break;
			case 3: close++;break;
			default:break;
			}
		}
		hashMap.put("未完成", doing);
		hashMap.put("申请完成", asking);
		hashMap.put("已经完成", finalied);
		hashMap.put("关闭", close);
		return hashMap;
	}
	
	
	
	
	
	
	
}
