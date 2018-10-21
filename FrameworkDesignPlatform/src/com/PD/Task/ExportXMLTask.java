package com.PD.Task;

import java.io.File;
import java.io.PrintWriter;
import java.util.List;
import java.util.Set;

import com.FD.model.DataModel.DataType;
import com.FP.frame.Config;
import com.FP.frame.Dialog.CommonDialog;
import com.PD.Thread.CellBack;
import com.PD.Thread.QueueRunnable;
import com.PD.Thread.Result;
import com.PD.Tool.FileTool;
import com.PD.Tool.CreateXML;
import com.PD.Tool.ExceptionTool;
import com.PD.Tool.LoadXMLFile;
import com.PD.Tool.XMLConfig;
import com.PD.cache.CacheData;
import com.PD.model.SWCModel;

public class ExportXMLTask extends QueueRunnable {
	private List<SWCModel> swcModels;
	private Set<DataType> dataTypes;
	private String path;

	public ExportXMLTask(List<SWCModel> swcModels, Set<DataType> dataTypes, String path, CellBack cellBack) {
		this.swcModels = swcModels;
		this.dataTypes = dataTypes;
		this.path = path;
		this.setName("构建XML");
		this.setStep(new String[] { "获取链接模块", "开始构建XML", "构建XML完成", "开始输出文件", "输出完成", });
		this.setCellBack(cellBack);
		this.setResult(new Result());
	}

	@Override
	public void doTask() {
		this.setNowStep(1);
		String temp = CreateXML.getBoby(swcModels, dataTypes);
		this.setNowStep(2);
		if (path != null && !path.isEmpty()) {
			PrintWriter printWriter = null;
			try {
				this.setNowStep(3);
				printWriter = new PrintWriter(new File(path));
			} catch (Exception e) {
				this.getResult().setSuccessful(false);
				this.getResult().setReason("Datatype加载失败:\r\n" + ExceptionTool.getExceptionMessage(e));
			}
			printWriter.write(temp);
			printWriter.flush();
		}
		this.setNowStep(4);
		this.getResult().setSuccessful(true);
		this.getResult().setMessage(temp);
	}

}
