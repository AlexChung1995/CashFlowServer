package Communications;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;

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
		System.out.println("Stringified request: " + StringUtils.stringify(request,this.byteNum));
		String[] message = StringUtils.split(StringUtils.stringify(request, this.byteNum), "\r\n\r\n");
		String header = message[0];
		String body = "";
		if (message.length == 2) {
			body = message[1];
		}
		this.headers = parseHeaders(header);
		this.body = parseBody(body);
		System.out.println(this.toString());
	}
	
	public HashMap<String,String> parseHeaders(String headerString){
		String [] metadata = StringUtils.split(headerString, "\r\n", 1);
		String [] command = StringUtils.split(metadata[0], " ");
		System.out.println("command: " + Arrays.toString(command));
		this.method = command[0];
		try {
			this.path = StringUtils.split(StringUtils.strip(command[1], "/"), "/");
		}catch (Exception e) {
			e.printStackTrace();
		}
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
				map.put(entry[0], entry[1]);
			}
		}
		return map;
	}
	
	public String getBodyVal(String key) {
		return this.body.get(key);
	}
	
	public String getProtocolVersion() {
		return this.protocolVersion;
	}
	
	public String toString() {
		String requestAsString = "{\r\nmethod: " + this.method;
		requestAsString += "\r\npath: " + Arrays.toString(this.path) + "\r\nheaders: {";
		for (Entry<String,String> header: this.headers.entrySet()) {
			requestAsString += "\r\n\r" + header.getKey() +": " + header.getValue();
		}
		requestAsString += "}\r\nbody: " + this.body + "\r\n}";
		return requestAsString;
		
	}
	
}
