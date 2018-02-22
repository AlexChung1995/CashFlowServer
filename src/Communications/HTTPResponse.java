package Communications;

import java.util.HashMap;

public class HTTPResponse extends Response {
	
	private int status;
	private static HashMap<Integer,String> statusMsgs;
	private HashMap<String,String> headers;
	private String body;
	
	public HTTPResponse(int status) {
		super();
		if (statusMsgs == null) {
			setStatusMsgs();
		}
		this.status = status;
		this.headers = new HashMap<String,String>();
		this.body = "";
	}
	
	public HTTPResponse(int status, HashMap<String,String> headers, String body) {
		super();
		if (statusMsgs == null) {
			setStatusMsgs();
		}
		this.status = status;
		this.headers = headers;
		this.body = body;
	}
	
	public String getStatusMsg() {
		return statusMsgs.get(this.status);
	}
	
	private static void setStatusMsgs() {
		statusMsgs = new HashMap<Integer,String>(40,1);
		statusMsgs.put(100, "Continue");
		statusMsgs.put(101, "Switching Protocols");
		statusMsgs.put(200, "OK");
		statusMsgs.put(202, "Accepted");
		statusMsgs.put(203, "Non-Authoritative Information");
		statusMsgs.put(204, "No Content");
		statusMsgs.put(205, "Reset Content");
		statusMsgs.put(206, "Partial Content");
		statusMsgs.put(300, "Multiple Choices");
		statusMsgs.put(301, "Moved Permanently");
		statusMsgs.put(303, "See Other");
		statusMsgs.put(304, "Not Modified");
        statusMsgs.put(305, "Use Proxy");
        statusMsgs.put(307, "Temporary Redirect");
        statusMsgs.put(400, "Bad Request");
        statusMsgs.put(401, "Unauthorized");
        statusMsgs.put(402, "Payment Required");
        statusMsgs.put(403, "Forbidden");
        statusMsgs.put(404, "Not Found");
        statusMsgs.put(405, "Method Not Allowed");
        statusMsgs.put(406, "Not Acceptable");
        statusMsgs.put(407, "Proxy Authentication Required");
        statusMsgs.put(408, "Request Time-out");
        statusMsgs.put(409, "Conflict");
        statusMsgs.put(410, "Gone");
        statusMsgs.put(411, "Length Required");
        statusMsgs.put(412, "Precondition Failed");
        statusMsgs.put(413, "Request Entity Too Large");
        statusMsgs.put(414, "Request-URI Too Large");
        statusMsgs.put(415, "Unsupported Media Type");
        statusMsgs.put(416, "Requested range not satisfiable");
        statusMsgs.put(417, "Expectation Failed");
        statusMsgs.put(500, "Internal Server Error");
        statusMsgs.put(501, "Not Implemented");
        statusMsgs.put(502, "Bad Gateway");
        statusMsgs.put(503, "Service Unavailable");
        statusMsgs.put(504, "Gateway Time-out");
        statusMsgs.put(505, "HTTP Version not supported");
	}
	
	public HashMap<String,String> getHeaders() {
		return this.headers;
	}
	
	public void setHeaders(HashMap<String,String> headers) {
		this.headers = headers;
	}
	
	public void addHeader(String key, String value) {
		this.headers.put(key,value);
	}
	
	public void removeHeader(String key) {
		this.headers.remove(key);
	}
	
	public String getBody(){
		return this.body;
	}
	
	public void setBody(String body) {
		this.body = body;
	}
	
	public int getStatus() {
		return this.status;
	}
	
	public void setStatus(int status) {
		this.status = status;
	}
}
