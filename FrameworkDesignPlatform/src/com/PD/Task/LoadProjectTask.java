package com.PD.Task;

import java.io.File;
import java.io.PrintWriter;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import com.FD.model.DataModel.DataType;
import com.FP.frame.Config;
import com.FP.frame.EditFrame;
import com.FP.frame.Dialog.CommonDialog;
import com.PD.Thread.CellBack;
import com.PD.Thread.QueueRunnable;
import com.PD.Thread.Result;
import com.PD.Tool.FileTool;
import com.PD.Tool.CreateXML;
import com.PD.Tool.ExceptionTool;
import com.PD.Tool.LoadXMLFile;
import com.PD.Tool.ProjectTool;
import com.PD.Tool.XMLConfig;
import com.PD.cache.CacheData;
import com.PD.model.ModuleModel;
import com.PD.model.RTEModel;
import com.PD.model.SWCModel;

public class LoadProjectTask extends QueueRunnable {
	private EditFrame editFrame;
	private String path;

	public LoadProjectTask(EditFrame editFrame, String path, CellBack cellBack) {
		this.editFrame = editFrame;
		this.path = path;
		this.setName("加载工程");
		this.setStep(new String[] { "开始解析.pro","开始反系列化模块数据", "开始构建模块","加载完成", });
		this.setCellBack(cellBack);
		this.setResult(new Result());
	}

	@Override
	public void doTask() {
		try {
			ProjectTool.clearAll(editFrame);
			this.setNowStep(1);
			Properties properties = ProjectTool.loadPro(path);
			this.setNowStep(2);
			String swcpath = path.substring(0, path.lastIndexOf("\\"))+File.separator+"swc";
			List<ModuleModel> models = ProjectTool.loadProject(properties, swcpath);
			this.setNowStep(3);
			ProjectTool.refresh(editFrame, models, properties);
			this.setNowStep(4);
			this.getResult().setSuccessful(true);
			this.getResult().setMessage(getStep()[getStep().length-1]);
		} catch (Exception e) {
			this.getResult().setReason(ExceptionTool.getExceptionMessage(e));
		}finally {
			this.setNowStep(4);
		}
		
	}

}
