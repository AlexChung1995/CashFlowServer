package Supreme;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.security.SecureRandom;

import utils.ByteUtils;

public class Generate {
	
	public static byte[] generateRandomKey(byte []seed) {
		int[] key = new int[seed.length/(Integer.SIZE/Byte.SIZE)];
		System.out.println("key length: " + key.length);
		SecureRandom random = new SecureRandom(seed);
		for (int i = 0; i<key.length; i++) {
			key[i] = random.nextInt(); 
		}
		return ByteUtils.toByteArray(key);
	}
	
	public static void main(String [] args) throws Exception{
		int[] seed = new int[10];
		for (int i = 0; i<seed.length; i++) {
			seed[i] = i;
		}
		byte[] key = generateRandomKey(ByteUtils.toByteArray(seed));
		DataOutputStream write = new DataOutputStream(new FileOutputStream("key"));
		for (int i = 0; i<key.length; i++) {
				write.writeByte(key[i]); 
		}
		System.out.println(Authorize.authorize(key, "key"));
		
	}
	
}
