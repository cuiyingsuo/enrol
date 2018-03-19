package com.itcast.enrol.common.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;

public class MD5Util {

	/*
	 * public static void main(String[] args) { String s=encryption("test");
	 * System.out.println(s); }
	 */

	public static String encryption(String plain) {
		byte[] hash;
		try {
			hash = MessageDigest.getInstance("MD5").digest(
					plain.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Huh, MD5 should be supported?", e);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Huh, UTF-8 should be supported?", e);
		}

		return hexString(hash);
	}

	public static final String hexString(byte[] bytes) {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < bytes.length; i++) {
			buffer.append(hexString(bytes[i]));
		}
		return buffer.toString();
	}

	public static final String hexString(byte byte0) {
		char ac[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A',
				'B', 'C', 'D', 'E', 'F' };
		char ac1[] = new char[2];
		ac1[0] = ac[byte0 >>> 4 & 0xf];
		ac1[1] = ac[byte0 & 0xf];
		String s = new String(ac1);
		return s;
	}
	public static void main(String[] args){
		String mobile="13161212921";
		String password="itheima";
		String endStr="chuanzhienrol123456@.com";
		System.out.println(encryption(mobile+password+endStr));
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
		System.out.println(sdf.format(1520301453019L));
	}
}
