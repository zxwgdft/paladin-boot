package com.paladin.framework.utils.qrcode;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

public class QRCodeUtil {

	/** 默认生成器 */
	private static MultiFormatWriter writer = new MultiFormatWriter();
	/** 默认生成二维码图片配置 */
	private static QRCodeConfig default_config = new QRCodeConfig();

	static {

	}

	/**
	 * 生成二维码图片
	 * 
	 * @param contents
	 * @param path
	 * @throws QRCodeException
	 *             无法写入到文件或二维码数据无法解析
	 */
	public static void createQRCode(String contents, String path) throws QRCodeException {
		createQRCode(contents, Paths.get(path), default_config);
	}

	/**
	 * 生成二维码图片
	 * 
	 * @param contents
	 * @param path
	 * @throws QRCodeException
	 *             无法写入到文件或二维码数据无法解析
	 */
	public static void createQRCode(String contents, Path path) throws QRCodeException {
		createQRCode(contents, path, default_config);
	}

	/**
	 * 生成二维码图片
	 * 
	 * @param contents
	 * @param path
	 * @param config
	 * @throws QRCodeException
	 *             无法写入到文件或二维码数据无法解析
	 */
	public static void createQRCode(String contents, Path path, QRCodeConfig config) throws QRCodeException {
		config = config == null ? default_config : config;

		try {
			BitMatrix matrix = writer.encode(contents, BarcodeFormat.QR_CODE, config.getWidth(), config.getHeight(), config
					.getEncodeHints());
			MatrixToImageWriter.writeToPath(matrix, config.getFormat(), path, config.getMatrixToImageConfig());
		} catch (IOException e) {
			e.printStackTrace();
			throw new QRCodeException("无法写入图片到文件：" + path);
		} catch (WriterException e) {
			e.printStackTrace();
			throw new QRCodeException("无法解析二维码的数据：" + contents);
		}
	}

	/**
	 * 生成二维码图片数据流
	 * 
	 * @param contents
	 * @param stream
	 * @throws QRCodeException
	 *             无法写入流或二维码数据无法解析
	 */
	public static void createQRCode(String contents, OutputStream stream) throws QRCodeException {
		createQRCode(contents, stream, default_config);
	}

	/**
	 * 生成二维码图片数据流
	 * 
	 * @param contents
	 * @param stream
	 * @param config
	 * @throws QRCodeException
	 *             无法写入流或二维码数据无法解析
	 */
	public static void createQRCode(String contents, OutputStream stream, QRCodeConfig config) throws QRCodeException {
		config = config == null ? default_config : config;

		try {
			BitMatrix matrix = writer.encode(contents, BarcodeFormat.QR_CODE, config.getWidth(), config.getHeight(), config
					.getEncodeHints());
			MatrixToImageWriter.writeToStream(matrix, config.getFormat(), stream, config.getMatrixToImageConfig());
		} catch (IOException e) {
			e.printStackTrace();
			throw new QRCodeException("无法写入图片到流：" + stream);
		} catch (WriterException e) {
			e.printStackTrace();
			throw new QRCodeException("无法解析二维码的数据：" + contents);
		}
	}

	/**
	 * 创建有logo的二维码
	 * 
	 * @param contents
	 * @param output
	 * @param logoStream
	 * @throws QRCodeException
	 */
	public static void createQRCode(String contents, OutputStream output, InputStream logoStream) throws QRCodeException {
		createQRCode(contents, output, logoStream, default_config);
	}

	/**
	 * 创建有logo的二维码
	 * 
	 * @param contents
	 * @param output
	 * @param logoStream
	 * @param config
	 * @throws QRCodeException
	 */
	public static void createQRCode(String contents, OutputStream output, InputStream logoStream, QRCodeConfig config)
			throws QRCodeException {

		config = config == null ? default_config : config;

		try {

			int width = config.getWidth();
			int height = config.getHeight();
			int onColor = config.getOnColor();
			int offColor = config.getOffColor();

			BitMatrix matrix = writer.encode(contents, BarcodeFormat.QR_CODE, width, height, config.getEncodeHints());

			BufferedImage qrImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					qrImage.setRGB(x, y, matrix.get(x, y) ? onColor : offColor);
				}
			}

			Graphics2D gs = qrImage.createGraphics();

			// 载入logo
			Image logoImage = ImageIO.read(logoStream);

			int logoHeight = logoImage.getHeight(null);
			int logoWidth = logoImage.getWidth(null);
			logoHeight = (int) (logoHeight > height * .15 ? height * .15 : logoHeight);
			logoWidth = (int) (logoWidth > width * .15 ? width * .15 : logoWidth);

			// 放中心
			int x = (width - logoWidth) / 2;
			int y = (height - logoHeight) / 2;

			int bx = x;
			int by = y;

			int backgroundWidth = logoWidth;
			int backgroundHeight = logoHeight;

			// 画背景色
			if (config.isShowLogoBackground()) {
				
				int padding = config.getBackgroundPadding();

				bx -= padding;
				by -= padding;

				backgroundWidth += padding * 2;
				backgroundHeight += padding * 2;
				
				gs.setColor(config.getLogeBackground());
				gs.fillRect(bx, by, backgroundWidth, backgroundHeight);
			}

			// 画边框
			if (config.isShowLogoBorder()) {

				int size = config.getLogoBorderSize();

				gs.setColor(config.getLogoBorderColor());
				gs.setStroke(new BasicStroke(size));

				gs.drawRect(bx, by, backgroundWidth, backgroundHeight);
			}

			gs.drawImage(logoImage, x, y, logoWidth, logoHeight, null);

			gs.dispose();

			logoImage.flush();
			qrImage.flush();

			if (!ImageIO.write(qrImage, config.getFormat(), output)) {
				throw new QRCodeException("无法写入" + config.getFormat() + "图片到流" + output);
			}

		} catch (IOException e) {
			e.printStackTrace();
			throw new QRCodeException("无法写入" + config.getFormat() + "图片到流" + output);
		} catch (WriterException e) {
			e.printStackTrace();
			throw new QRCodeException("无法解析二维码的数据：" + contents);
		}
	}

}
