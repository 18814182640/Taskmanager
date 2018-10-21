package com.PD.model;

import com.FP.frame.Config;

public class SignalModel extends DataModel {
	private static final long serialVersionUID = -6287055026209699950L;
	private String PortName;
	private String PortGroup;
	private String PortAttribute;
	private String DataType;
	private String InitValue;
	private Number Factor = 1; // 系数，默认为1
	private Number Offset = 0; // 偏移，默认为0
	private String Dim;
	private String Units;
	private Number Min;
	private Number Max;
	private String Description = ""; // 默认值

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

	public String getDataType() {
		return DataType;
	}

	public void setDataType(String dataType) {
		DataType = dataType;
	}

	public String getInitValue() {
		return InitValue;
	}

	public void setInitValue(String initValue) {
		InitValue = initValue;
	}

	public Number getFactor() {
		return Factor;
	}

	public void setFactor(Number factor) {
		Factor = factor;
	}

	public Number getOffset() {
		return Offset;
	}

	public void setOffset(Number offset) {
		Offset = offset;
	}

	public String getDim() {
		return Dim;
	}

	public void setDim(String dim) {
		Dim = dim;
	}

	public String getUnits() {
		return Units;
	}

	public void setUnits(String units) {
		Units = units;
	}

	public Number getMin() {
		return Min;
	}

	public void setMin(Number min) {
		Min = min;
	}

	public Number getMax() {
		return Max;
	}

	public void setMax(Number max) {
		Max = max;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public boolean equal(Object object) { // 重写equal方法
		SignalModel other = (SignalModel) object;
		if (this.PortName.equals(other.getPortName()) && this.PortGroup.equals(other.getPortGroup())
				&& this.PortAttribute.equals(other.getPortAttribute()) && this.DataType.equals(other.getDataType())
				&& this.InitValue.equals(other.getInitValue()) && this.Factor.equals(other.getFactor())
				&& this.Offset.equals(other.getOffset()) && this.Dim.equals(other.getDim())
				&& this.Units.equals(other.getUnits()) && this.Max.equals(other.getMax())
				&& this.Min.equals(other.getMin()) && this.Description.equals(other.getDescription())) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public String getType() {
		return Config.Signal;
	}

	@Override
	public int getArributeNum() {
		return 12;
	}

	@Override
	public String[] getAttributeNames() {
		return new String[] { "PortName", "PortGroup", "PortAttribute", "DataType", "InitValue", "Factor", "Offset",
				"Dim", "Units", "Min", "Max", "Description", };
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
		case "DataType":
			return DataType;
		case "InitValue":
			return InitValue;
		case "Factor":
			return Factor;
		case "Offset":
			return Offset;
		case "Dim":
			return Dim;
		case "Units":
			return Units;
		case "Min":
			return Min;
		case "Max":
			return Max;
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
		case "DataType":
			this.DataType = (String) value;
			break;
		case "InitValue":
			this.InitValue = (String) value;
			break;
		case "Factor":
			this.Factor = (Number) value;
			break;
		case "Offset":
			this.Offset = (Number) value;
			break;
		case "Dim":
			this.Dim = (String) value;
			break;
		case "Units":
			this.Units = (String) value;
			break;
		case "Min":
			this.Min = (Number) value;
			break;
		case "Max":
			this.Max = (Number) value;
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
