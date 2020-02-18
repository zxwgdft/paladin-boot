package com.paladin.framework.utils.qrcode;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageConfig;

/**
 * 二维码图片设置参数
 * 
 * @author TontoZhou
 * 
 */
public class QRCodeConfig {
	/** 默认图像配置 */
	private static MatrixToImageConfig default_config = new MatrixToImageConfig();

	private static Map<EncodeHintType, Object> default_encodeHints = new HashMap<EncodeHintType, Object>();

	static {
		//default_encodeHints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		default_encodeHints.put(EncodeHintType.MARGIN, 0);
	}

	private int width = 250;
	private int height = 250;
	private String format = "jpeg";

	private boolean showLogoBorder = false;
	private int logoBorderSize = 2;
	private Color logoBorderColor = Color.WHITE;

	private boolean showLogoBackground = true;
	private int backgroundPadding = 5;
	private Color logeBackground = Color.WHITE;

	private MatrixToImageConfig matrixToImageConfig = default_config;
	private Map<EncodeHintType, Object> encodeHints = default_encodeHints;
	private boolean is_default_encodeHints = true;

	/**
	 * 二维码图片宽度
	 * 
	 * @return
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * 设置二维码图片宽度
	 * 
	 * @param width
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * 二维码图片高度
	 * 
	 * @return
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * 设置二维码图片高度
	 * 
	 * @param height
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * 获取二维码图片格式
	 * 
	 * @return
	 */
	public String getFormat() {
		return format;
	}

	/**
	 * 设置二维码图片格式，例如png,jpeg等
	 * 
	 * @param format
	 */
	public void setFormat(String format) {
		this.format = format;
	}

	/**
	 * 获取二维码图片凸部分颜色，即一般情况下的黑色
	 * 
	 * @return
	 */
	public int getOnColor() {
		return matrixToImageConfig.getPixelOnColor();
	}

	/**
	 * 获取二维码图片凹部分颜色，即一般情况下的白色
	 * 
	 * @return
	 */
	public int getOffColor() {
		return matrixToImageConfig.getPixelOffColor();
	}

	/**
	 * 设置二维码图片凸凹部分颜色，对应即通常情况下的黑白
	 * 
	 * @param onColor
	 * @param offColor
	 */
	public void setPixelColor(int onColor, int offColor) {
		matrixToImageConfig = new MatrixToImageConfig(onColor, offColor);
	}

	public MatrixToImageConfig getMatrixToImageConfig() {
		return matrixToImageConfig;
	}

	/**
	 * 设置编码参数
	 * 
	 * @param hintType
	 * @param value
	 */
	public void putEncodeHint(EncodeHintType hintType, Object value) {
		if (is_default_encodeHints) {
			encodeHints = new HashMap<EncodeHintType, Object>();
			encodeHints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
			is_default_encodeHints = false;
		}

		if (encodeHints == null) {
			encodeHints = new HashMap<EncodeHintType, Object>();
		}

		encodeHints.put(hintType, value);
	}

	/**
	 * 设置编码参数
	 * 
	 * @param map
	 */
	public void setEncodeHints(Map<EncodeHintType, Object> map) {
		if (is_default_encodeHints)
			is_default_encodeHints = false;
		encodeHints = map;
	}

	/**
	 * 获取编码参数
	 * 
	 * @return
	 */
	public Map<EncodeHintType, Object> getEncodeHints() {
		return encodeHints;
	}

	/**
	 * 是否显示logo边框
	 * 
	 * @return
	 */
	public boolean isShowLogoBorder() {
		return showLogoBorder;
	}

	/**
	 * 是否显示logo边框
	 * 
	 * @param showLogoBorder
	 */
	public void setShowLogoBorder(boolean showLogoBorder) {
		this.showLogoBorder = showLogoBorder;
	}

	/**
	 * logo边框宽度
	 * 
	 * @return
	 */
	public int getLogoBorderSize() {
		return logoBorderSize;
	}

	/**
	 * logo边框宽度
	 * 
	 * @param logoBorderSize
	 */
	public void setLogoBorderSize(int logoBorderSize) {
		this.logoBorderSize = logoBorderSize;
	}

	/**
	 * logo边框颜色
	 * 
	 * @return
	 */
	public Color getLogoBorderColor() {
		return logoBorderColor;
	}

	/**
	 * logo边框颜色
	 * 
	 * @param logoBorderColor
	 */
	public void setLogoBorderColor(Color logoBorderColor) {
		this.logoBorderColor = logoBorderColor;
	}

	/**
	 * 是否显示logo背景色
	 * 
	 * @return
	 */
	public boolean isShowLogoBackground() {
		return showLogoBackground;
	}

	/**
	 * 设置是否显示logo背景色
	 * 
	 * @param showLogoBackground
	 */
	public void setShowLogoBackground(boolean showLogoBackground) {
		this.showLogoBackground = showLogoBackground;
	}

	/**
	 * 获取logo背景颜色
	 * 
	 * @return
	 */
	public Color getLogeBackground() {
		return logeBackground;
	}

	/**
	 * 设置logo背景色
	 * 
	 * @param logeBackground
	 */
	public void setLogeBackground(Color logeBackground) {
		this.logeBackground = logeBackground;
	}

	/**
	 * 背景填充大小（指与logo的边距）
	 * 
	 * @return
	 */
	public int getBackgroundPadding() {
		return backgroundPadding;
	}

	/**
	 * 设置背景填充大小（指与logo的边距）
	 * 
	 * @param backgroundPadding
	 */
	public void setBackgroundPadding(int backgroundPadding) {
		this.backgroundPadding = backgroundPadding;
	}

}
