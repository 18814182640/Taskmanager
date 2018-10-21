package com.PD.Task;

import java.util.Set;

import com.FD.model.DataModel.DataType;
import com.FP.frame.Config;
import com.PD.Thread.CellBack;
import com.PD.Thread.QueueRunnable;
import com.PD.Thread.Result;
import com.PD.Tool.FileTool;
import com.PD.Tool.ExceptionTool;

public class SaveDataTypeOnFileTask extends QueueRunnable {
	private Set<DataType> set;
	private String path;

	public SaveDataTypeOnFileTask(Set<DataType> set, String path, CellBack cellBack) {
		this.set = set;
		this.path = path;
		this.setName("±£¥ÊDataType");
		this.setCellBack(cellBack);
		this.setResult(new Result());
	}

	@Override
	public void doTask() {
		try {
			FileTool.writeDataTypeOnFile(set, path);
			this.getResult().setSuccessful(true);
		} catch (Exception e) {
			this.getResult().setSuccessful(false);
			this.getResult().setMessage("Datatype±£¥Ê ß∞‹:\r\n" + ExceptionTool.getExceptionMessage(e));
		}
	}

}
