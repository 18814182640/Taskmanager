package com.TMC.chart;



import java.awt.BorderLayout;
import java.awt.Frame;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.wb.swt.SWTResourceManager;

import com.TMS.model.TaskModel;

import de.netronic.jgantt.JGantt;

public class GanttChartJ extends Composite{

	private JGantt jGantt;
	public GanttChartJ(Composite parent){
		super(parent, SWT.EMBEDDED);
		this.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		this.setLayout(new FillLayout());

		BufferedReader bStream;
		StringBuffer sBuffer = new StringBuffer();
		try {
			bStream = new BufferedReader(new FileReader(new File("src/jgantt.cfg")));
			String temp = null;
			while((temp = bStream.readLine())!=null){
				sBuffer.append(temp);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.err.println(sBuffer.toString());
	    jGantt = new JGantt(sBuffer.toString());
	   
	    Frame frame = SWT_AWT.new_Frame(this);
		frame.add(jGantt, BorderLayout.CENTER);

		this.layout();
		
		upData(new ArrayList<TaskModel>());

		this.layout();
	}
	
	public void upData(List<TaskModel> taskModels){
		
	}


	
}
