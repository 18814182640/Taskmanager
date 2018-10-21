package com.PD.Tool;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringBufferInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.swt.widgets.Display;

import com.FP.frame.Config;
import com.FP.frame.EditFrame;
import com.PD.model.ModuleModel;
import com.PD.model.RTEModel;
import com.PD.model.SWCModel;
import com.sun.org.glassfish.gmbal.Description;

import sun.security.pkcs11.Secmod;

public class ProjectTool {

	
	@Description("保存.pro")
	public static void sendPro(RTEModel rteModel,String path) throws Exception{
		if(rteModel!=null && StringUtils.isNotEmpty(path)){
			File file = FileTool.isExistFile(path,true);
			if(file!=null){
				List<SWCModel> swcModels = rteModel.getSwcModelList();
				if(swcModels !=null){
					PrintWriter pWriter;
					StringBuffer sBuffer = new StringBuffer();
					for (int i = 0; i < swcModels.size(); i++) {
						SWCModel swcModel =  swcModels.get(i);
						if(swcModel!=null && swcModel.getSwcData()!=null){
							ModuleModel model = swcModel.getSwcData();
							try {
								getInfo(swcModel,sBuffer);
							} catch (Exception e) {
								e.printStackTrace();
								throw e;
							}
						}
					}
					pWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
					pWriter.println(sBuffer.toString());
					pWriter.close();
				}
			}
		}
	}
	
	@Description("组装row")
	private static void getInfo(final SWCModel swcModel,final StringBuffer sBuffer){
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				synchronized (sBuffer) {
					sBuffer.append(swcModel.getSwcData().getName()).append("=")
					.append(swcModel.getLocation().x).append(",").append(swcModel.getLocation().y)
					.append(";").append(swcModel.isLineToRTE()).append("\r\n");
				}
			}
		});
	}
	
	
	@Description("系列化所有模块")
	public static void sendProject(RTEModel rteModel,String path) throws IOException{
		if(rteModel!=null && StringUtils.isNotEmpty(path)){
			try {
				List<SWCModel> swcModels = rteModel.getSwcModelList();
				if(swcModels !=null){
					for (int i = 0; i < swcModels.size(); i++) {
						SWCModel swcModel =  swcModels.get(i);
						if(swcModel!=null){
							String pathName = path+File.separator+swcModel.getSwcData().getName()+Config.SWCSuffix;
							sendSWCModel(swcModel, pathName);
						}
						
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
				throw e;
			}
		}
	}
	
	@Description("系列化模块")
	public static void sendSWCModel(SWCModel swcModel, String path) throws IOException{
		ModuleModel model = swcModel.getSwcData();
		if(model!=null){
			File file = FileTool.isExistFile(path,true);
			if(file!=null){
				try {
					FileOutputStream fStream = new FileOutputStream(file);
					fStream.write(SerializationTool.serializationObjec(model));
					fStream.flush();
					fStream.close();
				} catch (IOException e) {
					e.printStackTrace();
					throw e;
				}
			}
		}
	}
	
	
	@Description("反系列化")
	public static ModuleModel loadModuleModel(String path){
		if(StringUtils.isNotEmpty(path)){
			File file = new File(path);
			if (file.exists() && file.canRead()) {
				if(file.getName().endsWith(Config.SWCSuffix)){
					byte [] temp = FileTool.getFiileByte(file);
					if(temp!=null){
						ModuleModel model = (ModuleModel) SerializationTool.unSerializationObjec(temp);
						return model;
					}
				}
			}
		}
		return null;
	}

	@Description("加载.pro")
	public static Properties loadPro(String path) throws Exception {
		Properties properties =null;
		if(StringUtils.isNotEmpty(path)){
			File file = FileTool.isExistFile(path, false);
			if(file!=null){
				try {
					properties = new Properties();
					properties.load(new InputStreamReader(new FileInputStream(file), "UTF-8"));
				} catch (Exception e) {
					e.printStackTrace();
					throw e;
				}
			}
		}
		return properties;
	}


	public static List<ModuleModel> loadProject(Properties properties, String swcpath) throws IOException {
		List<ModuleModel> models = null;
		if(properties != null){
			try {
				Set<String> set = properties.stringPropertyNames();
				Iterator< String> iterator = set.iterator();
				models = new ArrayList<>();
				while (iterator.hasNext()){
					String string = (String) iterator.next();
					string = swcpath + File.separator + string +Config.SWCSuffix;
					ModuleModel model = loadModuleModel(string);
					models.add(model);
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}
		}
        return models;		
	}
	
	
	
	
	
	public static void refresh(final EditFrame editFrame,final List<ModuleModel> models,final Properties properties){
		if(editFrame!=null && models!=null && properties!=null){
			Display.getDefault().syncExec(new Runnable() {
				@Override
				public void run() {
					for (int i = 0; i < models.size(); i++) {
						ModuleModel model = models.get(i);
						if(model!=null){
							String location = properties.getProperty(model.getName());
							int x=0,y=0;
							boolean islink = false;
							if(location.contains(";")){
								String [] temp = location.split(";");
								if(temp.length>=2){
									if(temp[0].contains(",")){
										String [] point = temp[0].split(",");
										x = Integer.parseInt(point[0]);
										y = Integer.parseInt(point[1]);
									}
									islink = Boolean.parseBoolean(temp[1]);
								}
							}
							editFrame.loadSWCmodel(models.get(i), x, y, islink);
						}
					}
				}
			});
		}
	}
	
	
	
	public static void clearAll(final EditFrame editFrame){
		if(editFrame!=null){
			Display.getDefault().syncExec(new Runnable() {
				@Override
				public void run() {
					synchronized (editFrame) {
						editFrame.clearAll();
					}
				}
			});
		}
		
	}
	
	
	
	
	
	
	
	
	
}
