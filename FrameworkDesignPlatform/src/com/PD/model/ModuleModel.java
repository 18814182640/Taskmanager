package com.PD.model;

import java.io.Serializable;
import java.util.List;
import java.util.Random;

import com.FP.frame.Config;

public class ModuleModel implements Serializable {

	private static final long serialVersionUID = 5043096563644854384L;
	private String name;
	private int time = 0;
	private int frameModel = 0;
	private List<DataModel> dataModels;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public int getFrameModel() {
		return frameModel;
	}

	public void setFrameModel(int frameModel) {
		this.frameModel = frameModel;
	}

	public List<DataModel> getDataModels() {
		return dataModels;
	}

	public void setDataModels(List<DataModel> dataModels) {
		this.dataModels = dataModels;
	}

}
