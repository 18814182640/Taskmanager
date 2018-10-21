package com.FD.model.DataModel;

import java.util.List;

import com.FP.frame.Config;

public class EnumType extends DataType {

	private String shortName;
	private String description = "";
	private List<Elenment> elenments;

	public class Elenment {
		private String shortName;
		private String initValue;
		private String description = "";

		public String getShortName() {
			return shortName;
		}

		public void setShortName(String shortName) {
			this.shortName = shortName;
		}

		public String getInitValue() {
			return initValue;
		}

		public void setInitValue(String initValue) {
			this.initValue = initValue;
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
		return Config.EnumTyep;
	}

}
