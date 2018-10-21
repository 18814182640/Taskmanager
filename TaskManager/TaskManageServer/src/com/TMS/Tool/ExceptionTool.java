package com.TMS.Tool;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionTool {

	public static String getExceptionMessage(Throwable cause) {
		StringWriter sWriter = new StringWriter();
		PrintWriter pWriter = new PrintWriter(sWriter, true);
		cause.printStackTrace(pWriter);
		pWriter.flush();
		sWriter.flush();
		return sWriter.toString();
	}
}
