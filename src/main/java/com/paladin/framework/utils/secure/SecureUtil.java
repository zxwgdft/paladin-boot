package com.paladin.framework.utils.secure;

import java.security.SecureRandom;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

public class SecureUtil {

	private static SecureRandom secureRandom = new SecureRandom();
	private static int DEFAULT_NUMBYTES = 16;

	public static String createSalte() {
		return createSalte(DEFAULT_NUMBYTES);
	}

	public static String createSalte(int numBytes) {
		byte[] bytes = new byte[numBytes];
		secureRandom.nextBytes(bytes);
		return ByteSource.Util.bytes(bytes).toHex();
	}

	public static String createPassword(String password, String salt) {
		return new SimpleHash("md5", password, ByteSource.Util.bytes(salt), 1).toHex();
	}

	public static void main(String[] args) {
		System.out.println(createPassword("admin", "3ad6c1dd71e0ec26c62ae1b7c81b3e82"));
	}
}
