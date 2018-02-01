package utils;

public class ByteUtils {
	
	public static byte[] toByteArray(int[] ints) {
		byte[] bytes = new byte[ints.length*(Integer.SIZE/Byte.SIZE)];
		int counter = 0;
		for (int i = 0; i<ints.length; i++) {
			for (int j = Integer.SIZE/Byte.SIZE - 1; j>=0; j--) {
				bytes[counter] = (byte)(ints[i]>>Byte.SIZE*j);
				counter ++;
			}
		}
		return bytes;
	}
	
	public static byte[] toByteArray(String string) {
		byte [] bytes = new byte[string.length()*(Character.SIZE/Byte.SIZE)];
		int counter = 0;
		for (int i = 0; i<string.length(); i++) {
			for (int j = Character.SIZE/Byte.SIZE - 1; j>=0; j--) {
				bytes[counter] = (byte)(string.charAt(i)>>Byte.SIZE*j);
				counter ++;
			}
		}
		return bytes;
	}
	
}
