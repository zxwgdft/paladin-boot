package com.paladin.framework.utils.others;

import java.util.Random;

/**
 * 随机值产生工具
 * 
 * @author TontoZhou
 * 
 */
public class RandomUtil {

	private static ThreadLocal<Random> randomMap = new ThreadLocal<Random>() {
		@Override
		protected Random initialValue() {
			return new Random();
		}
	};

	private static char[] simpleCharArray = { '_', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E',
			'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b',
			'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y',
			'z' };

	private static Random getRandom() {
		return randomMap.get();
	}

	/**
	 * 随机产生一个0到0x7fffffff之间的整数
	 * 
	 * @return
	 */
	public static Integer getRandomInt() {
		return getRandom().nextInt();
	}

	/**
	 * 在0到{@code max}之间随机产生一个数
	 * 
	 * @param max
	 * @return
	 */
	public static Integer getRandomInt(int max) {
		return getRandomInt(0, max);
	}

	/**
	 * 在{@code min}和{@code max}范围内随机产生一个整数
	 * 
	 * @param min
	 * @param max
	 * @return
	 */
	public static Integer getRandomInt(int min, int max) {
		int d = max - min;
		if (d < 0)
			return -1;

		return getRandom().nextInt(d) + min;
	}

	/**
	 * 随机产生一个0的0x7fffffff之间的双精度浮点数
	 * 
	 * @return
	 */
	public static Double getRandomDouble() {
		return getRandom().nextDouble() * 0x7fffffff;
	}

	/**
	 * 随机产生一个0到{@code max}的双精度浮点数
	 * 
	 * @param max
	 * @return
	 */
	public static Double getRandomDouble(double max) {
		return getRandomDouble(0, max);
	}

	/**
	 * 随机产生一个{@code min}到{@code max}的双精度浮点数
	 * 
	 * @param min
	 * @param max
	 * @return
	 */
	public static Double getRandomDouble(double min, double max) {
		double d = max - min;
		if (d < 0)
			return -1.0;
		return getRandom().nextDouble() * d + min;
	}

	/**
	 * 随机产生一个0到0x7fffffff之间的浮点数
	 * 
	 * @return
	 */
	public static Float getRandomFloat() {
		return getRandom().nextFloat() * 0x7fffffff;
	}

	/**
	 * 随机产生一个0到{@code max}的浮点数
	 * 
	 * @param max
	 * @return
	 */
	public static Float getRandomFloat(float max) {
		return getRandomFloat(0, max);
	}

	/**
	 * 随机产生一个{@code min}到{@code max}的浮点数
	 * 
	 * @param min
	 * @param max
	 * @return
	 */
	public static Float getRandomFloat(float min, float max) {
		float d = max - min;
		if (d < 0)
			return new Float(-1);
		return getRandom().nextFloat() * d + min;
	}

	/**
	 * 随机产生一个长度2到10之间的字符串
	 * 
	 * @return
	 */
	public static String getRandomString() {
		return getRandomString(2, 10);
	}

	/**
	 * 随机产生一个{@code minlength}到{@code maxlength}之间长度的字符串
	 * 
	 * @param minlength
	 * @param maxlength
	 * @return
	 */
	public static String getRandomString(int minlength, int maxlength) {
		int d = maxlength - minlength;
		if (d <= 0)
			return "";

		return getRandomString(getRandom().nextInt(d) + minlength);
	}

	/**
	 * 随机产生一个指定长度的字符串
	 * 
	 * @param length
	 * @return
	 */
	public static String getRandomString(int length) {
		if (length <= 0)
			return "";

		Random random = getRandom();
		int edge = simpleCharArray.length - 1;
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < length; i++)
			sb.append(simpleCharArray[random.nextInt(edge)]);
		return sb.toString();
	}

	/**
	 * 随机产生一个长整数
	 * 
	 * @return
	 */
	public static Long getRandomLong() {
		return getRandom().nextLong();
	}

	/**
	 * 随机产生一个范围在0到{@code max}之间的长整数
	 * 
	 * @param max
	 * @return
	 */
	public static Long getRandomLong(long max) {
		return getRandomLong(0, max);
	}

	/**
	 * 随机产生一个范围在{@code min}到{@code max}之间的长整数
	 * 
	 * @param min
	 * @param max
	 * @return
	 */
	public static Long getRandomLong(long min, long max) {
		long d = max - min;
		int p1 = (int) (d >> 32);
		int p2 = (int) (d >> 16 & 0xffff);
		int p3 = (int) (d & 0xffff);

		Random random = getRandom();
		return ((long) random.nextInt(p1) << 32) + ((long) random.nextInt(p2) << 16) + ((long) random.nextInt(p3)) + min;
	}

	/**
	 * 随机生成一个Byte
	 * 
	 * @return
	 */
	public static Byte getRandomByte() {
		return getRandomInt(256).byteValue();
	}

	/**
	 * 随机生成一个Boolean
	 * 
	 * @return
	 */
	public static Boolean getRandomBoolean() {
		return getRandom().nextBoolean();
	}

}
