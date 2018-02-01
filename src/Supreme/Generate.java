package Supreme;

import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.SecureRandom;

import utils.ByteUtils;

public class Generate {
	
	public static byte[] generateRandomKey(byte []seed) throws IOException {
		byte[] key = new byte[seed.length];//(Integer.SIZE/Byte.SIZE)];
		System.out.println("key length: " + key.length);
		SecureRandom random = new SecureRandom(seed);
		random.nextBytes(key); 
		DataOutputStream write = new DataOutputStream(new FileOutputStream("key"));
		for (int i = 0; i<key.length; i++) {
				write.writeByte(key[i]); 
		}
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
