package com.PD.cache;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.List;

import com.FD.model.DataModel.DataType;
import com.FP.frame.Config;
import com.PD.model.ModuleModel;
import com.PD.model.SWCModel;

public class CacheData {

	private CacheData() {
	}

	private static Set<DataType> dataTypes = new HashSet<DataType>(); // 自定义数据类型
	public static StringBuffer AUTOSAR;
	public static StringBuffer AR_PACKAGES;
	public static StringBuffer AR_PACKAGE;
	public static StringBuffer P_PORTS;
	public static StringBuffer PARAMETER_P;
	public static StringBuffer SEND_PORT;
	public static StringBuffer SERVICE_PORT;
	public static StringBuffer CLIENT;
	public static StringBuffer PARAMETER_R;
	public static StringBuffer R_PORTS;
	public static StringBuffer RECEIVE;
	public static StringBuffer AR_DATA_DES;
	public static StringBuffer ENUM_CUSTOM_ELENMENT_DEF;
	public static StringBuffer STRUCT_CUSTOM_ELENMENT_TYPE;
	public static StringBuffer SW_ENUM_TYPE;
	public static StringBuffer SW_FIXDT_TYPE;
	public static StringBuffer SW_STRUCT_TYPE;
	
	
	private static java.util.List<ModuleModel> loadModule;
	private static Properties properties;

	public static Set<DataType> getDataTypes() {
		return dataTypes;
	}

	public static String[] getAllDataType() {
		java.util.List<String> list = new ArrayList<>();
		list.addAll(Arrays.asList(Config.BaseType));
		Iterator<DataType> iterator = dataTypes.iterator();
		while (iterator.hasNext()) {
			DataType dataType = (DataType) iterator.next();
			list.add(dataType.getShortName());
		}
		String[] temp = new String[list.size()];
		list.toArray(temp);
		return temp;
	}


	public static Properties getProperties() {
		return properties;
	}

	public static void setProperties(Properties properties) {
		CacheData.properties = properties;
	}

	public static java.util.List<ModuleModel> getLoadModule() {
		return loadModule;
	}

	public static void setLoadModule(java.util.List<ModuleModel> loadModule) {
		CacheData.loadModule = loadModule;
	}
	
	
	

}
