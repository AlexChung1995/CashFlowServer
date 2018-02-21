package Communications;

import java.util.Arrays;
import java.util.HashMap;

import utils.StringUtils;

public class HTTPRequest extends Request {

	private HashMap<String,String> body;
	private HashMap<String,String> headers;
	private String protocolVersion;
	
	public HTTPRequest() {
		super();
	}
	
	public HTTPRequest(byte[] request, String[] path, String method, int byteNum) {
		super(request, path, method, byteNum);
	}
	
	public HTTPRequest(byte[] request) {
		super(request);
		this.byteNum = 1;
		this.headers = new HashMap<String,String>();
		this.body = new HashMap<String,String>();
		String[] message = StringUtils.split(StringUtils.stringify(request, this.byteNum), "\r\n\r\n");
		String header = message[0];
		System.out.println("header: " + header);
		String body = "";
		if (message.length == 2) {
			body = message[1];
			System.out.println("body: " + body);
		}
		this.headers = parseHeaders(header);
		this.body = parseBody(body);
		System.out.println(this.method);
		System.out.println(Arrays.toString(this.path));
		System.out.println(this.headers.toString());
		System.out.println(this.body.toString());
	}
	
	public HashMap<String,String> parseHeaders(String headerString){
		String [] metadata = StringUtils.split(headerString, "\n", 1);
		String [] command = StringUtils.split(metadata[0], " ");
		this.method = command[0];
		this.path = StringUtils.split(StringUtils.strip(command[1], "/"), "/");
		this.protocolVersion= command[2];
		HashMap<String,String> headers = parseValues(metadata[1],": ","\n");
		return headers;
	}
	
	public HashMap<String,String> parseBody(String bodyString){
		HashMap<String,String> body = parseValues(bodyString,"=","&");
		return body;
	}
	
	public HashMap<String,String> parseValues(String valuesString, String keyValSeperator, String entrySeperator){
		HashMap<String,String> map = new HashMap<String,String>();
		String [] entries = StringUtils.split(valuesString, entrySeperator);
		for (int i = 0; i<entries.length; i++) {
			String [] entry = StringUtils.split(entries[i], keyValSeperator);
			if (entry.length == 2) {
				System.out.println("entry key: " + entry[0] + "; entry value: " + entry[1]);
				map.put(entry[0], entry[1]);
			}
		}
		return map;
	}
	
	public void setHeaders(byte[] headers) {
		String headerString = StringUtils.stringify(headers, 1);
		String[] headerArray = StringUtils.split(headerString, "\n");
		int i = 0;
		for (i = 0; i<headerArray.length; i++) {
			String[] keyAndValue = StringUtils.split(headerArray[i], ": ");
			this.headers.put(keyAndValue[0], keyAndValue[1]);
		}
	}
	
	public String getProtocolVersion() {
		return this.protocolVersion;
	}
	
}
