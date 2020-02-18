package com.paladin.framework.utils.qrcode;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import com.google.zxing.EncodeHintType;

public class QRCodeParam {
	private String offColor="ffffff";
	private String onColor="000000";
	private int width=200;
	private int height=200;
	private String format="png";
	private int showLogoBorder=0;
	private int logoBorderSize=2;
	private String logoBorderColor="00ff00";
	private String charset;
	private int margin=-1;
	
	public QRCodeConfig toConfig(){
		QRCodeConfig config=new QRCodeConfig();
		config.setWidth(width);
		config.setHeight(height);
		config.setPixelColor(Integer.valueOf(onColor,16)|0XFF000000, Integer.valueOf(offColor,16)|0XFF000000);
		config.setFormat(format);
		config.setShowLogoBorder(showLogoBorder==1?true:false);
		config.setLogoBorderColor(new Color(Integer.valueOf(logoBorderColor,16)));
		config.setLogoBorderSize(logoBorderSize);
		
		if(charset!=null||margin>=0)
		{
			Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
			if(charset!=null)
				hints.put(EncodeHintType.CHARACTER_SET, "UTF-8"); 	
			if(margin>=0)
				hints.put(EncodeHintType.MARGIN, margin);
			config.setEncodeHints(hints);
		}
		
		return config;
		
	}

	public String getOffColor() {
		return offColor;
	}

	public void setOffColor(String offColor) {
		this.offColor = offColor;
	}

	public String getOnColor() {
		return onColor;
	}

	public void setOnColor(String onColor) {
		this.onColor = onColor;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public int getShowLogoBorder() {
		return showLogoBorder;
	}

	public void setShowLogoBorder(int showLogoBorder) {
		this.showLogoBorder = showLogoBorder;
	}

	public int getLogoBorderSize() {
		return logoBorderSize;
	}

	public void setLogoBorderSize(int logoBorderSize) {
		this.logoBorderSize = logoBorderSize;
	}

	public String getLogoBorderColor() {
		return logoBorderColor;
	}

	public void setLogoBorderColor(String logoBorderColor) {
		this.logoBorderColor = logoBorderColor;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public int getMargin() {
		return margin;
	}

	public void setMargin(int margin) {
		this.margin = margin;
	}
}
