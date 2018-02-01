package Supreme;

import java.io.IOException;
import java.security.SecureRandom;

import utils.ByteUtils;

public class Generate {
	
	public static byte[] generateRandomKey(byte []seed) throws IOException {
		byte[] key = new byte[seed.length];//(Integer.SIZE/Byte.SIZE)];
		SecureRandom random = new SecureRandom(seed);
		random.nextBytes(key); 
		return key;
	}
	
	public static void saveRandomKey(byte[] key) {
		
	}
	
	public static void main(String [] args) throws Exception{
		int[] seed = new int[10];
		for (int i = 0; i<seed.length; i++) {
			seed[i] = i;
		}
		byte[] key = generateRandomKey(ByteUtils.toByteArray(seed));
		
		System.out.println(Authorize.authorize(key));
	}
	
}
