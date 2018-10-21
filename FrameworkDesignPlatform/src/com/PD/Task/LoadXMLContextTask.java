package com.PD.Task;

import com.PD.Thread.CellBack;
import com.PD.Thread.QueueRunnable;
import com.PD.Thread.Result;
import com.PD.Tool.ExceptionTool;
import com.PD.Tool.LoadXMLFile;
import com.PD.Tool.XMLConfig;
import com.PD.cache.CacheData;

public class LoadXMLContextTask extends QueueRunnable {
	public LoadXMLContextTask(CellBack cellBack) {
		this.setName("加载XML文件");
		this.setCellBack(cellBack);
		this.setResult(new Result());
	}

	@Override
	public void doTask() {
		try {
			CacheData.AUTOSAR = LoadXMLFile.getXMLContext(XMLConfig.AUTOSAR);
			CacheData.AR_PACKAGES = LoadXMLFile.getXMLContext(XMLConfig.AR_PACKAGES);
			CacheData.AR_PACKAGE = LoadXMLFile.getXMLContext(XMLConfig.AR_PACKAGE);
			CacheData.P_PORTS = LoadXMLFile.getXMLContext(XMLConfig.P_PORTS);
			CacheData.PARAMETER_P = LoadXMLFile.getXMLContext(XMLConfig.PARAMETER_P);
			CacheData.SEND_PORT = LoadXMLFile.getXMLContext(XMLConfig.SEND_PORT);
			CacheData.SERVICE_PORT = LoadXMLFile.getXMLContext(XMLConfig.SERVICE_PORT);
			CacheData.CLIENT = LoadXMLFile.getXMLContext(XMLConfig.CLIENT);
			CacheData.PARAMETER_R = LoadXMLFile.getXMLContext(XMLConfig.PARAMETER_R);
			CacheData.R_PORTS = LoadXMLFile.getXMLContext(XMLConfig.R_PORTS);
			CacheData.RECEIVE = LoadXMLFile.getXMLContext(XMLConfig.RECEIVE);
			CacheData.AR_DATA_DES = LoadXMLFile.getXMLContext(XMLConfig.AR_DATA_DES);
			CacheData.ENUM_CUSTOM_ELENMENT_DEF = LoadXMLFile.getXMLContext(XMLConfig.ENUM_CUSTOM_ELENMENT_DEF);
			CacheData.STRUCT_CUSTOM_ELENMENT_TYPE = LoadXMLFile.getXMLContext(XMLConfig.STRUCT_CUSTOM_ELENMENT_TYPE);
			CacheData.SW_ENUM_TYPE = LoadXMLFile.getXMLContext(XMLConfig.SW_ENUM_TYPE);
			CacheData.SW_FIXDT_TYPE = LoadXMLFile.getXMLContext(XMLConfig.SW_FIXDT_TYPE);
			CacheData.SW_STRUCT_TYPE = LoadXMLFile.getXMLContext(XMLConfig.SW_STRUCT_TYPE);
			this.getResult().setSuccessful(true);
		} catch (Exception e) {
			this.getResult().setSuccessful(false);
			this.getResult().setMessage("XML文件加载失败:\r\n" + ExceptionTool.getExceptionMessage(e));
		}
	}

}
