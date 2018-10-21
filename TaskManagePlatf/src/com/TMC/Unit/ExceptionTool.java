package com.TMC.Unit;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionTool {

	public static String getExceptionMessage(Exception e) {
		StringWriter sWriter = new StringWriter();
		PrintWriter pWriter = new PrintWriter(sWriter, true);
		e.printStackTrace(pWriter);
		pWriter.flush();
		sWriter.flush();
		return sWriter.toString();
	}
}
