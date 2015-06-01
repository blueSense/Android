package com.bluesensenetworks.proximitysense;

import java.security.MessageDigest;
import android.util.Log;

public class Utils {
	final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

	public static String bytesToHex(byte[] bytes) {
		char[] hexChars = new char[bytes.length * 2];
		int v;
		for (int j = 0; j < bytes.length; j++) {
			v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}

	public static byte[] hexToBytes(final String hex) {
		int len = hex.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4) + Character.digit(hex.charAt(i + 1), 16));
		}
		return data;
	}

	public static byte[] parseMacAddress(String macAddress) {
		String[] macAddressParts = macAddress.split(":");
		byte[] macAddressBytes = new byte[macAddressParts.length];

		for (int i = 0; i < macAddressParts.length; i++) {
			Integer hex = Integer.parseInt(macAddressParts[i], 16);
			macAddressBytes[i] = hex.byteValue();
		}
		return macAddressBytes;
	}

	public static String generateHash(String value) {

		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(value.getBytes("UTF-8"));

			return bytesToHex(hash);
		} catch (Exception ex) {
			Log.e("Utils", ex.getMessage(), ex);
		}
		
		return null;
	}
}
