package com.FP.frame;

public interface Config {

	public static final int SWC = 0;
	public static final int PlUG = 1;
	public static final int RTE = 2;

	public static final int PUT = 0;
	public static final int MOVE = 1;

	public static final int SWCWidth = 80;
	public static final int SWCHeight = 60;
	public static final int RTCWidth = 255;
	public static final int RTCHeight = 75;

	public static final String UP = "UP";
	public static final String Down = "Down";

	public static final String SWCImagePath = "icons/swc.png";
	public static final String RTEImagePath = "icons/rte.png";
	public static final String OKImagePath = "icons/ok_16.png";
	public static final String CloseImagePath = "icons/error_16.png";
	public static final String refresh = "icons/refresh.png";
	public static final String RemoveImagePath = "icons/remove.png";
	public static final String AddImagePath = "icons/add.png";
	public static final String DataTypeImagePath = "icons/datatype.png";
	public static final String SaveImagePath = "icons/save.png";
	public static final String LinkImagePath = "icons/link.png";
	public static final String CancelLinkImagePath = "icons/cancelLink.png";
	public static final String IconImagePath = "icons/icon.png";
	public static final String ExportImagePath = "icons/export.png";

	public static final String DataTypeFilePath = "datatype/datatype.properties";

	public static final String Signal = "Signal";
	public static final String Function = "Function";

	public static final String[] ModelC_Code = { "Model", "C/C++" };

	public static final int BASE = 0;
	public static final int ADD = 1;
	public static final int NEW = 2;
	public static final int LOST = 3;

	public static final String FixdtType = "定点类型";
	public static final String StructType = "结构体类型";
	public static final String EnumTyep = "枚举类型";
	public static final String[] Stmol = { "signed", "unsigned" };
	public static final String[] BaseType = { "int8", "int16", "int32", "uint8", "uint16", "uint32", "signal", "double",
			"boolean" };

	public static final String Separate = "%SEPARATE%";
	public static final String Elenment = "%ELENMENT%";
	public static final String Elenment_children = "%ELENMENT-CHILDREN%";
	
	public static final String SWCSuffix =".swc";
	public static final String ProSuffix =".pro";

	public static enum PortGroup {
		P_Port("P-Port"), R_Port("R-Port");
		private String name;

		PortGroup(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

	}

	public static enum PortAttribute {
		Service("Service"), Client("Client"), Send("Send"), Receive("Receive");
		private String name;

		PortAttribute(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

	}

}
