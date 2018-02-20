package server;

import java.util.HashMap;
import java.util.function.Function;

import Communications.Request;
import utils.ByteUtils;

public class Route {
	
	public static enum REST{
		GET,POST,PUT
	}
	
	private HashMap<String,Route> router;
	private HashMap<String,Function<Request,byte[]>> functions; 
	
	public Route(HashMap<String,Route> routes, Function<Request, byte[]> get, Function <Request, byte[]> put, Function <Request, byte[]> post){
		this.router = routes;
		this.router.put("", this);//loopback
		Function<Request, byte[]> defaultFunc = (request) -> {
			byte[] bytes = ByteUtils.toByteArray("404 Not Found", request.getByteNum()); 
			System.out.println(new String(bytes));
			return bytes;
		};
		this.functions = new HashMap<String,Function<Request,byte[]>>();
		if (get == null) {
			this.functions.put("GET", defaultFunc);
		}else {
			this.functions.put("GET", get);
		}
		if(put == null) {
			this.functions.put("PUT", defaultFunc);
		}else {
			this.functions.put("PUT", put);
		}
		if(post == null) {
			this.functions.put("POST", defaultFunc);
		}
		else {
			this.functions.put("POST", post);
		}
	}
	
	public Function<Request,byte[]> route(String [] path, int pathPlace, String request) {
		if (pathPlace == path.length) {
			return this.functions.get(request);
		}
		else {
			return this.router.get(path[pathPlace]).route(path, pathPlace + 1, request);
		}
	}
	
}
