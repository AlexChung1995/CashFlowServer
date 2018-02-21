package Communications;

import server.Route;
import utils.StringUtils;

public class Request {
	
	protected int byteNum;
	protected String[] path;
	protected Route route;
	protected byte[] request;//the whole request
	protected String method;
	public Request() {
		
	}
	public Request(byte[] request) {
		this.request = request;
	}
	public Request(byte[] request, String[] path, String method, int byteNum) {
		this.path = path;
		this.request = request;
		this.method = method;
		this.byteNum = byteNum;
	}
	//TODO: lazy evaluation of parameters
	public void setPath(String[] path) {
		this.path = path;
	}
	public String[] getPath() {
		return this.path;
	}
	public String getRequestString() {
		return StringUtils.stringify(this.request,this.byteNum);
	}
	public byte[] getRequestBytes() {
		return this.request;
	}
	public String getMethodString() {
		return this.method;
	}
	public void setMethodString(String method) {
		this.method = method;
	}
	public byte[] getInput() {
		return new byte[1024];
	}
	public int getByteNum() {
		return this.byteNum;
	}
	public void setRoute(Route route) {
		this.route = route;
	}
}
