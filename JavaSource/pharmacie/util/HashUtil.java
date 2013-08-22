package pharmacie.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.sun.org.apache.xerces.internal.impl.dv.util.HexBin;

public class HashUtil {

	private static String algorithm="SHA-1";
	private static String charset="UTF-8";
	public HashUtil() {
		// TODO Auto-generated constructor stub
	}
	
	public static String hash(final String plainText) {
		try {
			MessageDigest digest=MessageDigest.getInstance(algorithm);
			digest.update(plainText.getBytes(charset));
			byte[] rawHash=digest.digest();
			
			return new String(HexBin.encode(rawHash));
			
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String args[]) {
		System.out.println(hash("diokey"));
	}

}
