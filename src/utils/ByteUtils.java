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
	
	//specify byteNum as 1 if client expects 8 bit (1 byte) char encoding, 2 if client expects 16 bit (2 byte) etc.
	public static byte[] toByteArray(String string, int byteNum) {
		byte [] bytes = new byte[string.length()*(byteNum)];
		int counter = 0;
		for (int i = 0; i<string.length(); i++) {
			for (int j = byteNum - 1; j>=0; j--) {
				bytes[counter] = (byte)(string.charAt(i)>>Byte.SIZE*j);
				counter ++;
			}
		}
		return bytes;
	}
	
	public static byte[] toByteArray(boolean bool, int byteNum) {
		if (bool) {
			return toByteArray("true",byteNum);
		}
		else {
			return toByteArray("false",byteNum);
		}
	}
	
	public static byte[] join(byte[] a, int startA, int endA, byte[] b, int startB, int endB) {
		byte[] joined = new byte[endA-startA + startB-endB];
		int pos = 0;
		for (int i = startA; i < endA; i++){
			joined[pos] = a[i];
			pos++;
		}
		for (int j = startB; j < endB ; j++) {
			joined[pos] = b[j];
			pos++;
		}
		return joined;
	}
	
	public static byte[] copy(byte[] a, int start, int end) {
		byte[] copied = new byte[end - start];
		int pos = 0;
		for (int i = start; i<end; i++) {
			copied[pos] = a[i];
			pos++;
		}
		return copied;
	}
	
}
