package com.PD.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.crypto.Data;

import org.eclipse.swt.internal.win32.TCHITTESTINFO;

public abstract class DataModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1321131632129346543L;
	private int falg = -1;

	public abstract String getType();

	public abstract int getArributeNum();

	public abstract String[] getAttributeNames();

	public abstract Object getAttributeValue(String name);

	public abstract boolean setAttributeValue(String name, Object value);

	public abstract boolean equals(DataModel dataModel);

	public int getFalg() {
		return falg;
	}

	public void setFalg(int falg) {
		this.falg = falg;
	}

}
