package Supreme;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Authorize {
	public static boolean authorize(byte[] bytes) {
		for (int i = 0; i< bytes.length; i++) {
			if(bytes[i] != bytes[i]) return false;
		}
		return true;
	}
	public static boolean verifyKey(byte[] bytes) {
		return true;
	}
	public static boolean verifyString(String string) {
		return true;
	}
	public static boolean verifyInt(int x) {
		return true;
	}
}
