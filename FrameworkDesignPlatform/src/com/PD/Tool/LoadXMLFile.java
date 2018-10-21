package com.PD.Tool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class LoadXMLFile {

	public static StringBuffer getXMLContext(String path) throws Exception {
		StringBuffer context = null;
		BufferedReader bufferedReader = null;
		try {
			File file = new File(path);
			if (!file.exists()) {
				throw new RuntimeException("文件" + path + "不存在!");
			}
			context = new StringBuffer();
			bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
			String temp = null;
			while ((temp = bufferedReader.readLine()) != null) {
				context.append(temp).append("\r\n");
			}
			bufferedReader.close();
		} catch (Exception e) {
			try {
				if (bufferedReader != null) {
					bufferedReader.close();
				}
			} catch (IOException e1) {
			}
			throw e;
		}
		return context;
	}

}
