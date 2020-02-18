package com.paladin.framework.utils;

import java.util.Base64;

public class Base64Util {

	public static final String ENCODE = "UTF-8";

	public static String encode(byte[] data) {
		if(data == null) {
			return null;
		}
		return Base64.getEncoder().encodeToString(data);
	}

	public static byte[] decode(String data) {
		if(data == null || data.length() == 0) {
			return null;
		}
		return Base64.getDecoder().decode(data);
	}

	/**
	 * 获取文件大小
	 * @param data
	 * @return
	 */
	public static int getFileSize(String data) {
		int equalIndex = data.indexOf("=");
		int length = equalIndex > 0 ? equalIndex : data.length();
		return length * 3 / 4;
	}



	
}
