package utils;

public class ByteUtils {
	
	public static byte[] toByteArray(int[] ints) {
		byte[] bytes = new byte[ints.length*4];
		int counter = 0;
		for (int i = 0; i<ints.length; i++) {
			for (int j = 3; j>=0; j--) {
				bytes[counter] = (byte)(ints[i]>>8*j);
				counter ++;
			}
		}
		return bytes;
	}
	
}
