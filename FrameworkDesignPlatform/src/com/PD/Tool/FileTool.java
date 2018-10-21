package com.PD.Tool;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;

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
import com.PD.model.SignalModel;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

public class FileTool {

	
	public static File isExistPath(String path){
		File file;
		try {
			file = new File(path);
			if(!file.exists()){
				file.mkdir();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return file;
	}
	
	public static File isExistFile(String pathName,boolean isNew){
		File file = new File(pathName);
		if(!file.exists()){
			if(isNew){   //新建
				try {
					file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
					return null;
				}
			}else{     //不新建
				return null;
			}
		}
		return file;
	}
	
	public static byte[] getFiileByte(File file){
		FileInputStream fStream;
		byte [] temp = null;
		try {
			fStream = new FileInputStream(file);
			temp = new byte[fStream.available()];
			fStream.read(temp);
			fStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return temp;
	}
	
	
	
	
	public static List<ModuleModel> parseExcel(String fileName) {
		List<ModuleModel> moduleModels = null;
		try {
			Workbook workbook = Workbook.getWorkbook(new File(fileName));
			moduleModels = new ArrayList<>();
			int smum = workbook.getNumberOfSheets();
			for (int i = 0; i < smum; i++) {
				ModuleModel m = parseSheet(workbook.getSheet(i));
				if (m != null) {
					moduleModels.add(m);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return moduleModels;

	}

	private static ModuleModel parseSheet(Sheet sheet) {
		ModuleModel moduleModel = null;
		List<DataModel> dataModels = null;
		if (sheet != null) {
			moduleModel = new ModuleModel();
			dataModels = new ArrayList<DataModel>();
			moduleModel.setName(sheet.getName());
		}
		int rowNum = sheet.getRows();
		for (int i = 1; i < rowNum; i++) {
			DataModel d = parseRow(sheet.getRow(i));
			if (d != null) {
				dataModels.add(d);
			}
		}
		moduleModel.setDataModels(dataModels);
		return moduleModel;

	}

	private static FunctionModel parseRow(Cell[] cells) {
		FunctionModel functionModel = null;
		if (cells != null && cells.length >= 6 && cells[0].getContents() != null && !cells[0].getContents().isEmpty()) {
			functionModel = new FunctionModel();
			functionModel.setPortGroup(cells[0].getContents());
			functionModel.setPortName(cells[1].getContents());
			functionModel.setPortAttribute(cells[2].getContents());
			functionModel.setReturnType(cells[3].getContents());
			functionModel.setParameters(cells[4].getContents());
			functionModel.setDescription(cells[5].getContents());
			functionModel.setFalg(Config.BASE);
		}
		return functionModel;
	}

	public static boolean readDataTypeOnFile(Set<DataType> set, String path) throws Exception {
		if (set != null && path != null) {
			File file = new File(path);
			BufferedReader bufferedReader = null;
			if (file.exists() && file.canRead()) {
				try {
					bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
					String temp = "";
					while ((temp = bufferedReader.readLine()) != null) {
						String[] typeStr = temp.split(Config.Separate);
						switch (typeStr[0]) {
						case Config.FixdtType: {
							FixdtType fixdtType = new FixdtType();
							fixdtType.setShortName(typeStr[1]);
							fixdtType.setSymbol(typeStr[2]);
							fixdtType.setSlope(DataUnit.getNumber(typeStr[3]));
							fixdtType.setOffset(DataUnit.getNumber(typeStr[4]));
							fixdtType.setLength(Integer.parseInt(typeStr[5]));
							if (typeStr.length >= 7) {
								fixdtType.setDescription(typeStr[6]);
							}
							set.add(fixdtType);
						}
							break;
						case Config.StructType: {
							StructType structType = new StructType();
							structType.setShortName(typeStr[1]);
							List<Elenment> structTypeElenments = new ArrayList<>();
							if (typeStr[2] != null && !typeStr[2].isEmpty()) {
								String[] eStrings = typeStr[2].split(Config.Elenment);
								for (int i = 0; i < eStrings.length; i++) {
									String[] children = eStrings[i].split(Config.Elenment_children);
									Elenment elenment = structType.new Elenment();
									elenment.setShortName(children[0]);
									elenment.setDataType(children[1]);
									elenment.setDimension(children[2]);
									elenment.setDescription(children[3]);
									structTypeElenments.add(elenment);

								}
							}
							structType.setElenments(structTypeElenments);
							if (typeStr.length >= 4) {
								structType.setDescription(typeStr[3]);
							}
							set.add(structType);
						}
							break;
						case Config.EnumTyep: {
							EnumType enumType = new EnumType();
							enumType.setShortName(typeStr[1]);
							List<com.FD.model.DataModel.EnumType.Elenment> enumTypeElenments = new ArrayList<>();
							if (typeStr[2] != null && !typeStr[2].isEmpty()) {
								String[] elStrings = typeStr[2].split(Config.Elenment);
								for (int i = 0; i < elStrings.length; i++) {
									String[] children = elStrings[i].split(Config.Elenment_children);
									com.FD.model.DataModel.EnumType.Elenment elenment = enumType.new Elenment();
									elenment.setShortName(children[0]);
//									elenment.setDataType(children[1]);
									elenment.setInitValue(children[1]);
									if (children.length >= 4) {
										elenment.setDescription(children[3]);
									}
									enumTypeElenments.add(elenment);

								}
							}
							enumType.setElenments(enumTypeElenments);
							if (typeStr.length >= 4) {
								enumType.setDescription(typeStr[3]);
							}
							set.add(enumType);
						}
							break;
						default:
							break;
						}
					}
					bufferedReader.close();
					return true;
				} catch (Exception e) {
					try {
						bufferedReader.close();
					} catch (IOException e1) {
					}
					throw e;
				}
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public static boolean writeDataTypeOnFile(Set<DataType> set, String path) throws Exception {
		if (set != null && path != null) {
			File file = new File(path);
			BufferedWriter bufferedWriter = null;
			try {
				if (!file.exists()) {
					try {
						file.createNewFile();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (file.canWrite()) {
					bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
					bufferedWriter.write("");
					Iterator<DataType> it = set.iterator();
					while (it.hasNext()) {
						DataType dataType = (DataType) it.next();
						StringBuffer temp = new StringBuffer();
						switch (dataType.getType()) {
						case Config.FixdtType:
							FixdtType fixdtType = (FixdtType) dataType;
							temp.setLength(0);
							temp.append(fixdtType.getType()).append(Config.Separate);
							temp.append(fixdtType.getShortName()).append(Config.Separate);
							temp.append(fixdtType.getSymbol()).append(Config.Separate);
							temp.append(fixdtType.getSlope()).append(Config.Separate);
							temp.append(fixdtType.getOffset()).append(Config.Separate);
							temp.append(fixdtType.getLength()).append(Config.Separate);
							temp.append(fixdtType.getDescription());
							break;
						case Config.StructType:
							StructType structType = (StructType) dataType;
							temp.setLength(0);
							temp.append(structType.getType()).append(Config.Separate);
							temp.append(structType.getShortName()).append(Config.Separate);
							for (int i = 0; i < structType.getElenments().size(); i++) {
								Elenment elenment = structType.getElenments().get(i);
								temp.append(elenment.getShortName()).append(Config.Elenment_children);
								temp.append(elenment.getDataType()).append(Config.Elenment_children);
								temp.append(elenment.getDimension()).append(Config.Elenment_children);
								temp.append(elenment.getDescription());
								if (i < structType.getElenments().size() - 1) {
									temp.append(Config.Elenment);
								}
							}
							temp.append(Config.Separate);
							temp.append(structType.getDescription());

							break;
						case Config.EnumTyep:
							EnumType enumType = (EnumType) dataType;
							temp.setLength(0);
							temp.append(enumType.getType()).append(Config.Separate);
							temp.append(enumType.getShortName()).append(Config.Separate);
							for (int i = 0; i < enumType.getElenments().size(); i++) {
								com.FD.model.DataModel.EnumType.Elenment elenment = enumType.getElenments().get(i);
								temp.append(elenment.getShortName()).append(Config.Elenment_children);
//								temp.append(elenment.getDataType()).append(Config.Elenment_children);
								temp.append(elenment.getInitValue()).append(Config.Elenment_children);
								temp.append(elenment.getDescription());
								if (i < enumType.getElenments().size() - 1) {
									temp.append(Config.Elenment);
								}
							}
							temp.append(Config.Separate);
							temp.append(enumType.getDescription());
							break;
						default:
							break;
						}
						String inputStr = new String(temp.toString().getBytes("GBK"));
						// System.err.println(inputStr);
						bufferedWriter.append(inputStr);
						if (it.hasNext()) {
							bufferedWriter.newLine();
						}
					}
					bufferedWriter.flush();
					bufferedWriter.close();
					return true;
				} else {
					return false;
				}
			} catch (Exception e) {
				try {
					bufferedWriter.close();
				} catch (IOException e1) {
				}
				throw e;
			}
		} else {
			return false;
		}
	}

}
