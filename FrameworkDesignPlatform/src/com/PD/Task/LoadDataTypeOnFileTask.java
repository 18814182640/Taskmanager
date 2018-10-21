package com.PD.Task;

import java.util.Set;

import com.FD.model.DataModel.DataType;
import com.FP.frame.Config;
import com.PD.Thread.CellBack;
import com.PD.Thread.QueueRunnable;
import com.PD.Thread.Result;
import com.PD.Tool.FileTool;
import com.PD.Tool.ExceptionTool;
import com.PD.Tool.LoadXMLFile;
import com.PD.Tool.XMLConfig;
import com.PD.cache.CacheData;

public class LoadDataTypeOnFileTask extends QueueRunnable {
	private Set<DataType> set;
	private String path;

	public LoadDataTypeOnFileTask(Set<DataType> set, String path, CellBack cellBack) {
		this.set = set;
		this.path = path;
		this.setName("º”‘ÿDataType");
		this.setCellBack(cellBack);
		this.setResult(new Result());
	}

	@Override
	public void doTask() {
		try {
			FileTool.readDataTypeOnFile(set, path);
			this.getResult().setSuccessful(true);
		} catch (Exception e) {
			this.getResult().setSuccessful(false);
			this.getResult().setMessage("Datatypeº”‘ÿ ß∞‹:\r\n" + ExceptionTool.getExceptionMessage(e));
		}
	}

}
