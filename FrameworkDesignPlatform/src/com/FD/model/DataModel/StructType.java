package com.FD.model.DataModel;

import java.util.List;

import com.FP.frame.Config;

public class StructType extends DataType {

	private String shortName;
	private String description = "";
	private List<Elenment> elenments;

	public class Elenment { // ÄÚ²¿Àà
		private String shortName;
		private String dataType;
		private String dimension;
		private String description = "";

		public String getShortName() {
			return shortName;
		}

		public void setShortName(String shortName) {
			this.shortName = shortName;
		}

		public String getDataType() {
			return dataType;
		}

		public void setDataType(String dataType) {
			this.dataType = dataType;
		}

		public String getDimension() {
			return dimension;
		}

		public void setDimension(String dimension) {
			this.dimension = dimension;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Elenment> getElenments() {
		return elenments;
	}

	public void setElenments(List<Elenment> elenments) {
		this.elenments = elenments;
	}

	@Override
	public String getType() {
		return Config.StructType;
	}

}
