package com.sendtomoon.eroica.common.security;



import java.math.BigInteger;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class PasswordCodeUtils  {

	private static byte[] ENC_KEY_BYTES = "Q0L4GOr4a01u6GQ4".getBytes();
	
	private static SecretKeySpec DEF_KEY = new SecretKeySpec(ENC_KEY_BYTES, "AES");
	
	
	public static String encode(SecretKey key, String datas) throws Exception {
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(1, key);
		byte[] encoding = cipher.doFinal(datas.getBytes());
		BigInteger n = new BigInteger(encoding);
		return n.toString(16);
	}

	public static String encode(String datas){
		try {
			return encode(DEF_KEY, datas);
		} catch (Exception e) {
			throw new PasswordProviderException("PasswordEncode error,cause:"+e.getMessage(),e);
		}
	}

	public static String decode(SecretKey key, String datas) throws Exception {
		BigInteger n = new BigInteger(datas, 16);
		byte[] encoding = n.toByteArray();
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(2, key);
		byte[] decode = cipher.doFinal(encoding);
		return new String(decode);
	}

	public static String decode(String datas)  {
		try {
			return decode(DEF_KEY, datas);
		} catch (Exception e) {
			throw new PasswordProviderException("PasswordDecode error,cause:"+e.getMessage(),e);
		}
	}

	public static void main(String[] args) throws Exception {
		String password = "Paic1234$";
		String encPasswd = PasswordCodeUtils.encode(password);
		System.out.println("原密码：" + password);
		System.out.println("加密后的密码：" + encPasswd);
		System.out.println("解密后的密码：" + PasswordCodeUtils.decode(encPasswd));
	}
}