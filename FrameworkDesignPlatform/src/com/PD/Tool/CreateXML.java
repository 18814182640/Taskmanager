package com.PD.Tool;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.FD.model.DataModel.DataType;
import com.FD.model.DataModel.EnumType;
import com.FD.model.DataModel.FixdtType;
import com.FD.model.DataModel.StructType;
import com.FD.model.DataModel.StructType.Elenment;
import com.FP.frame.Config;
import com.PD.cache.CacheData;
import com.PD.model.DataModel;
import com.PD.model.FunctionModel;
import com.PD.model.ModuleModel;
import com.PD.model.SWCModel;
import com.PD.model.SignalModel;

public class CreateXML {

	public static String getBoby(List<SWCModel> swcModels, Set<DataType> dataTypes) {
		String body = "";
		if (swcModels != null || dataTypes != null) {
			body = CacheData.AUTOSAR.toString();
			body = body.replace(":autosar", getSwcALL(swcModels) + getDataTypeALL(dataTypes));
		} else {
			throw new RuntimeException("无法构建XML文件，因模块数据为空!");
		}
		return body;
	}

	private static String getSwcALL(List<SWCModel> swcModels) {
		String swcContextAll = "";
		if (swcModels != null && swcModels.size() > 0) { // 大于0
			swcContextAll = CacheData.AR_PACKAGES.toString();
			StringBuffer sBuffer = new StringBuffer();
			for (int i = 0; i < swcModels.size(); i++) {
				SWCModel sModel = swcModels.get(i);
				if (sModel != null) {
					if (sModel.isLineToRTE()) {
						sBuffer.append(getSwc(sModel.getSwcData()));
					}
				}
			}
			swcContextAll = swcContextAll.replace(":ar-packages", sBuffer.toString());
		}
		return swcContextAll;
	}

	private static String getSwc(ModuleModel moduleModel) {
		String swcContext = "";
		if (moduleModel != null) {
			swcContext = CacheData.AR_PACKAGE.toString();
			swcContext = swcContext.replace(":des", moduleModel.getName() + " PACKAGE DESCRIPTION");
			swcContext = swcContext.replace(":short-name", moduleModel.getName());
			swcContext = swcContext.replace(":file-name", moduleModel.getName() + "_FUNCTION_FILE");
			swcContext = swcContext.replace(":call-cycle", moduleModel.getTime() + "");
			swcContext = swcContext.replace(":CODE-DESCRIPTORS", Config.ModelC_Code[moduleModel.getFrameModel()]);
			swcContext = swcContext.replace(":ports-description", getPort(moduleModel.getDataModels()));
		}
		return swcContext;
	}

	private static String getPort(List<DataModel> dataModels) {
		StringBuffer R_Port = new StringBuffer();
		StringBuffer P_Port = new StringBuffer();
		if (dataModels != null) {
			for (int i = 0; i < dataModels.size(); i++) {
				DataModel dataModel = dataModels.get(i);
				if (dataModel != null) {
					String portgroup = (String) dataModel.getAttributeValue("PortGroup");
					if (portgroup.equals(Config.PortGroup.R_Port.getName())) {
						R_Port.append(getR_Port(dataModel));
					} else if (portgroup.equals(Config.PortGroup.P_Port.getName())) {
						P_Port.append(getP_Port(dataModel));
					}
				}
			}
		}
		String rportStr = CacheData.R_PORTS.toString();
		String pportStr = CacheData.P_PORTS.toString();
		rportStr = rportStr.replace(":r-ports", R_Port.toString());
		pportStr = pportStr.replace(":p-ports", P_Port.toString());

		return rportStr + pportStr;
	}

	private static String getR_Port(DataModel dataModel) {
		String rString = "";
		if (dataModel != null) {
			String portAttribute = (String) dataModel.getAttributeValue("PortAttribute");
			if (Config.PortAttribute.Receive.getName().equals(portAttribute)) {
				if (Config.Signal.equals(dataModel.getType())) {
					rString = CacheData.RECEIVE.toString();
					SignalModel signalModel = (SignalModel) dataModel;
					rString = rString.replace(":short-name", signalModel.getPortName());
					rString = rString.replace(":data-type", signalModel.getDataType());
					rString = rString.replace(":init-value", signalModel.getInitValue());
					rString = rString.replace(":max-value", signalModel.getMax() + "");
					rString = rString.replace(":min-value", signalModel.getMin() + "");
					rString = rString.replace(":factor", signalModel.getFactor() + "");
					rString = rString.replace(":off-set", signalModel.getOffset() + "");
					rString = rString.replace(":dimension", signalModel.getDim());
					rString = rString.replace(":unit", signalModel.getUnits());
					rString = rString.replace(":description", signalModel.getDescription() + "");
				} else {
					throw new RuntimeException("解析到模块" + dataModel.getAttributeValue("PortName") + "时发现不匹配数据:["
							+ dataModel.getType() + "-" + portAttribute + "]");
				}
			} else if (Config.PortAttribute.Client.getName().equals(portAttribute)) {
				rString = CacheData.CLIENT.toString();
				if (Config.Function.equals(dataModel.getType())) {
					rString = CacheData.CLIENT.toString();
					FunctionModel fModel = (FunctionModel) dataModel;
					rString = rString.replace(":short-name", fModel.getPortName());
					rString = rString.replace(":data-type", fModel.getReturnType());
					rString = rString.replace(":parameters", getParamR(fModel.getParameters()));

				} else {
					throw new RuntimeException("解析到模块" + dataModel.getAttributeValue("PortName") + "时发现不匹配数据:["
							+ dataModel.getType() + "-" + portAttribute + "]");
				}
			}
		}
		return rString;
	}

	private static String getParamR(String param) {
		StringBuffer rString = new StringBuffer();
		if (param != null && !param.isEmpty()) {
			if (param.contains(";")) {
				String[] rows = param.trim().split(";");
				for (int i = 0; i < rows.length; i++) {
					String p = CacheData.PARAMETER_R.toString();
					String temp = rows[i].trim();
					if (temp != null && !temp.isEmpty() && temp.contains(" ")) {
						String[] col = temp.split(" ");
						if (col.length == 2) {
							p = p.replace(":short-name", col[1]);
							p = p.replace(":data-type", col[0]);
							p = p.replace(":description", "NULL");
						} else if (col.length == 3) {
							p = p.replace(":short-name", col[1]);
							p = p.replace(":data-type", col[0]);
							p = p.replace(":description", col[2]);
						}
					}
					rString.append(p);
				}
			}
		}
		return rString.toString();
	}

	private static String getP_Port(DataModel dataModel) {
		String pString = "";
		if (dataModel != null) {
			String portAttribute = (String) dataModel.getAttributeValue("PortAttribute");
			if (Config.PortAttribute.Send.getName().equals(portAttribute)) {
				if (Config.Signal.equals(dataModel.getType())) {
					pString = CacheData.SEND_PORT.toString();
					SignalModel signalModel = (SignalModel) dataModel;
					pString = pString.replace(":short-name", signalModel.getPortName());
					pString = pString.replace(":data-type", signalModel.getDataType());
					pString = pString.replace(":init-value", signalModel.getInitValue());
					pString = pString.replace(":max-value", signalModel.getMax() + "");
					pString = pString.replace(":min-value", signalModel.getMin() + "");
					pString = pString.replace(":factor", signalModel.getFactor() + "");
					pString = pString.replace(":off-set", signalModel.getOffset() + "");
					pString = pString.replace(":dimension", signalModel.getDim());
					pString = pString.replace(":unit", signalModel.getUnits());
					pString = pString.replace(":description", signalModel.getDescription() + "");
				} else {
					throw new RuntimeException("解析到模块" + dataModel.getAttributeValue("PortName") + "时发现不匹配数据:["
							+ dataModel.getType() + "-" + portAttribute + "]");
				}
			} else if (Config.PortAttribute.Service.getName().equals(portAttribute)) {
				pString = CacheData.SERVICE_PORT.toString();
				if (Config.Function.equals(dataModel.getType())) {
					pString = CacheData.CLIENT.toString();
					FunctionModel fModel = (FunctionModel) dataModel;
					pString = pString.replace(":short-name", fModel.getPortName());
					pString = pString.replace(":data-type", fModel.getReturnType());
					pString = pString.replace(":parameters", getParamP(fModel.getParameters()));

				} else {
					throw new RuntimeException("解析到模块" + dataModel.getAttributeValue("PortName") + "时发现不匹配数据:["
							+ dataModel.getType() + "-" + portAttribute + "]");
				}
			}
		}
		return pString;
	}

	private static String getParamP(String param) {
		StringBuffer rString = new StringBuffer();
		if (param != null && !param.isEmpty()) {
			if (param.contains(";")) {
				String[] rows = param.trim().split(";");
				for (int i = 0; i < rows.length; i++) {
					String p = CacheData.PARAMETER_P.toString();
					String temp = rows[i].trim();
					if (temp != null && !temp.isEmpty() && temp.contains(" ")) {
						String[] col = temp.split(" ");
						if (col.length == 2) {
							p = p.replace(":short-name", col[1]);
							p = p.replace(":data-type", col[0]);
							p = p.replace(":description", "NULL");
						} else if (col.length == 3) {
							p = p.replace(":short-name", col[1]);
							p = p.replace(":data-type", col[0]);
							p = p.replace(":description", col[2]);
						}
					}
					rString.append(p);
				}
			}
		}
		return rString.toString();
	}

	private static String getDataTypeALL(Set<DataType> dataTypes) {
		String dtContextALL = "";
		if (dataTypes != null && dataTypes.size() > 0) { // 大于0
			dtContextALL = CacheData.AR_DATA_DES.toString();
			StringBuffer sBuffer = new StringBuffer();
			Iterator<DataType> iterator = dataTypes.iterator();
			while (iterator.hasNext()) {
				DataType dataType = (DataType) iterator.next();
				if (dataType != null) {
					sBuffer.append(getDataType(dataType));
				}
			}
			dtContextALL = dtContextALL.replace(":ar-packages", sBuffer.toString());
		}
		return dtContextALL;
	}

	private static String getDataType(DataType dataType) {
		String dtContext = "";
		if (dataType != null) {
			switch (dataType.getType()) {
			case Config.FixdtType:
				dtContext = CacheData.SW_FIXDT_TYPE.toString();
				FixdtType fixdtType = (FixdtType) dataType;
				dtContext = dtContext.replace(":short-name", fixdtType.getShortName());
				dtContext = dtContext.replace(":symbol", fixdtType.getSymbol());
				dtContext = dtContext.replace(":slope", fixdtType.getSlope() + "");
				dtContext = dtContext.replace(":offset", fixdtType.getOffset() + "");
				dtContext = dtContext.replace(":length", fixdtType.getLength() + "");
				dtContext = dtContext.replace(":description", fixdtType.getDescription() + "");
				break;
			case Config.StructType:
				dtContext = CacheData.SW_STRUCT_TYPE.toString();
				StructType structType = (StructType) dataType;
				dtContext = dtContext.replace(":short-name", structType.getShortName());
				dtContext = dtContext.replace(":description", structType.getDescription() + "");
				dtContext = dtContext.replace(":elements", getElementS(structType.getElenments()));
				break;
			case Config.EnumTyep:
				dtContext = CacheData.SW_ENUM_TYPE.toString();
				EnumType enumType = (EnumType) dataType;
				dtContext = dtContext.replace(":short-name", enumType.getShortName());
				dtContext = dtContext.replace(":description", enumType.getDescription() + "");
				dtContext = dtContext.replace(":elements", getElementE(enumType.getElenments()));
				break;
			default:
				break;
			}
		}
		return dtContext;
	}

	private static String getElementS(List<Elenment> elenments) {
		StringBuffer sBuffer = new StringBuffer();
		if (elenments != null) {
			for (int i = 0; i < elenments.size(); i++) {
				Elenment elenment = elenments.get(i);
				if (elenment != null) {
					String eString = CacheData.STRUCT_CUSTOM_ELENMENT_TYPE.toString();
					eString = eString.replace(":short-name", elenment.getShortName());
					eString = eString.replace(":data-type", elenment.getDataType());
					eString = eString.replace(":dimension", elenment.getDimension());
					eString = eString.replace(":description", elenment.getDescription());
					sBuffer.append(eString);
				}
			}
		}
		return sBuffer.toString();
	}

	private static String getElementE(List<com.FD.model.DataModel.EnumType.Elenment> elenments) {
		StringBuffer sBuffer = new StringBuffer();
		if (elenments != null) {
			for (int i = 0; i < elenments.size(); i++) {
				com.FD.model.DataModel.EnumType.Elenment elenment = elenments.get(i);
				if (elenment != null) {
					String eString = CacheData.ENUM_CUSTOM_ELENMENT_DEF.toString();
					eString = eString.replace(":short-name", elenment.getShortName());
//					eString = eString.replace(":data-type", elenment.getDataType());
					eString = eString.replace(":init-value", elenment.getInitValue());
					eString = eString.replace(":description", elenment.getDescription());
					sBuffer.append(eString);
				}
			}
		}
		return sBuffer.toString();
	}

}
