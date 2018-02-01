package Supreme;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Authorize {
	public static boolean authorize(byte[] bytes, String filename) throws IOException {
		DataInputStream reader = new DataInputStream(new FileInputStream(filename));
		for (int i = 0; i< bytes.length; i++) {
			byte bite = reader.readByte();
			if(bytes[i] != bite) return false;
		}
		return true;
	}
}
