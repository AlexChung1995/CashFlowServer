package utils;

import java.util.ArrayList;

public class StringUtils {
	
	public static String[] split(String string, String delimit, int numMatches) {
		int size = 0;
		int numFound = 0;
		ArrayList<String> strings = new ArrayList<String>();
		String current = "";
		for (int i = 0; i<string.length(); i++) {	
			if (string.charAt(i) == delimit.charAt(0) && numFound < numMatches) {
				String possible = "" + string.charAt(i);
				boolean isDelimiter = true;
				for (int j = 1; j<delimit.length();j++) {
					possible += string.charAt(i+j);
					if (string.charAt(i+j) != delimit.charAt(j)) {
						current += possible;
						i += j;
						isDelimiter = false;
						break;
					}
				}
				if (isDelimiter) {
					numFound ++;
					strings.add(current);
					current = "";
				}
			}
			else {
				current += string.charAt(i);
			}
		}
		strings.add(current);
		return strings.toArray(new String[size]); 
	}
	
	public static String[] split(String string, String delimit) {
		return split(string,delimit,(int)Float.POSITIVE_INFINITY);
	}
	public static String strip(String string, String delimit) {
		int current = 0;
		int i = 0;
		int j = string.length() - 1;
		int start = i;
		int end = j;
		while (i < string.length()) {
			if (string.charAt(i) == delimit.charAt(current)) {
				i++;
				current ++;
				if (current == delimit.length()) {
					current = 0;
					start = i;
				}
			}
			else {
				break;
			}
		}
		current = delimit.length() - 1;
		while (j > start) {
			if (string.charAt(j) == delimit.charAt(current)) {
				j--;
				current --;
				if (current == -1) {
					current = delimit.length() - 1;
					end = j;
				}
			}
			else {
				break;
			}
		}
		return string.substring(i, j + 1); 
	}
	
	public static String stringify(byte[] bytes, int byteNum) {
		String toReturn = "";
		int i = 0;
		while (i<bytes.length) {
			char a = (char)bytes[i];
			int j = 1;
			while (j < byteNum) {
				i++;
				j++;
				a = (char) (a << 8);
				a = (char) (a | bytes[i]);
			}
			i++;
			toReturn += a;
		}
		return toReturn;
	}
	
	public static void main(String [] args) {
		String test = "/";
		String testStripped = strip(test, "/");
		System.out.println(testStripped.equals(null));
		System.out.println(split(testStripped,"/").toString());
	}
}
