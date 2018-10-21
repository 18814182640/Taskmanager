package com.PD.Tool;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.FP.frame.Dialog.CommonDialog;

public class SerializationTool {

	@SuppressWarnings("unchecked")
	public static <T> Object copyObjec(T t) {
		T ob = null;
		ByteArrayOutputStream bStream = new ByteArrayOutputStream();
		ObjectOutputStream oStream;
		try {
			oStream = new ObjectOutputStream(bStream);
			oStream.writeObject(t);
			ObjectInputStream iStream = new ObjectInputStream(new ByteArrayInputStream(bStream.toByteArray()));
			ob = (T) iStream.readObject();
			iStream.close();
			bStream.close();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return ob;
	}
	
	public static <T> byte[] serializationObjec(T t) {
		ByteArrayOutputStream bStream = new ByteArrayOutputStream();
		ObjectOutputStream oStream;
		try {
			oStream = new ObjectOutputStream(bStream);
			oStream.writeObject(t);
			oStream.close();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return bStream.toByteArray();
	}
	
	public static <T> Object unSerializationObjec(byte [] bytes) {
		T ob = null;
		try {
			ObjectInputStream iStream = new ObjectInputStream(new ByteArrayInputStream(bytes));
			ob = (T) iStream.readObject();
			iStream.close();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return ob;
	}
	
	
	

}
