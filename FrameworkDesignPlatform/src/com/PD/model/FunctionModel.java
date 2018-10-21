package com.PD.model;

import com.FP.frame.Config;

public class FunctionModel extends DataModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6399625232557416815L;
	private String PortName;
	private String PortGroup;
	private String PortAttribute;
	private String ReturnType;
	private String Parameters = "";
	private String Description = ""; // Ä¬ÈÏÖµ
	private int flag = -1;

	public String getPortName() {
		return PortName;
	}

	public void setPortName(String portName) {
		PortName = portName;
	}

	public String getPortGroup() {
		return PortGroup;
	}

	public void setPortGroup(String portGroup) {
		PortGroup = portGroup;
	}

	public String getPortAttribute() {
		return PortAttribute;
	}

	public void setPortAttribute(String portAttribute) {
		PortAttribute = portAttribute;
	}

	public String getReturnType() {
		return ReturnType;
	}

	public void setReturnType(String returnType) {
		ReturnType = returnType;
	}

	public String getParameters() {
		return Parameters;
	}

	public void setParameters(String parameters) {
		Parameters = parameters;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	@Override
	public String getType() {
		return Config.Function;
	}

	@Override
	public int getArributeNum() {
		return 6;
	}

	@Override
	public String[] getAttributeNames() {
		return new String[] { "PortName", "PortGroup", "PortAttribute", "ReturnType", "Parameters", "Description" };
	}

	@Override
	public Object getAttributeValue(String name) {
		switch (name) {
		case "PortName":
			return PortName;
		case "PortGroup":
			return PortGroup;
		case "PortAttribute":
			return PortAttribute;
		case "ReturnType":
			return ReturnType;
		case "Parameters":
			return Parameters;
		case "Description":
			return Description;
		default:
			return null;
		}
	}

	@Override
	public boolean setAttributeValue(String name, Object value) {
		switch (name) {
		case "PortName":
			this.PortName = (String) value;
			break;
		case "PortGroup":
			this.PortGroup = (String) value;
			break;
		case "PortAttribute":
			this.PortAttribute = (String) value;
			break;
		case "ReturnType":
			this.ReturnType = (String) value;
			break;
		case "Parameters":
			this.Parameters = (String) value;
			break;
		case "Description":
			this.Description = (String) value;
			break;
		default:
			return false;
		}
		return true;
	}

	@Override
	public boolean equals(DataModel dataModel) {
		if (dataModel != null) {
			if (this.getType().equals(dataModel.getType())) {
				for (int i = 0; i < dataModel.getArributeNum(); i++) {
					String aString = (String) this.getAttributeValue(dataModel.getAttributeNames()[i]);
					String bString = (String) dataModel.getAttributeValue(dataModel.getAttributeNames()[i]);
					if (!aString.equals(bString)) {
						return false;
					}
				}
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}

	}

}
