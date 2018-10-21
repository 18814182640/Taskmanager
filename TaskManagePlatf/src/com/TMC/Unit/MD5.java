package com.TMC.Unit;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.lang.StringUtils;

public class MD5 {
   private static MessageDigest mDigest;
   static{
	   try {
		mDigest = MessageDigest.getInstance("MD5");
	} catch (NoSuchAlgorithmException e) {
		e.printStackTrace();
	}
   }
   
  public static String encode(String context) {
	   try {
		   if(StringUtils.isNotEmpty(context)){
			   mDigest.update(context.getBytes("utf-8"));
				byte[] data = mDigest.digest();
				return byteToStr(data);
		   }else{
			    return null;
		   } 
	   } catch (Exception e) {
		   e.printStackTrace();
	   }
	   return null;
   }
   
  
  public static String decode(String context){
	  try {
		   if(StringUtils.isNotEmpty(context)){
			    mDigest.update(context.getBytes("utf-8"));
				byte[] data = mDigest.digest();
				return byteToStr(data);
		   }else{
			    return null;
		   } 
	   } catch (Exception e) {
		   e.printStackTrace();
	   }
	   return null;
  }
  
  private static String byteToStr(byte [] data){
	  StringBuilder stringBuilder = new StringBuilder();
	  for (int i = 0; i < data.length; i++) {
		 int temp = data[i] & 0xff;
		 String str = "00"+Integer.toHexString(temp);
		 stringBuilder.append(str.substring(str.length()-2));
	  }
	  return stringBuilder.toString();
  }
   
}
