package server;

import java.util.HashMap;
import java.util.function.Function;

import Communications.Request;
import Communications.Response;
import utils.ByteUtils;

public class Route {
	
	public static enum REST{
		GET,POST,PUT
	}
	
	private HashMap<String,Route> router;
	private HashMap<String,Function<Request,Response>> functions; 
	
	public Route(HashMap<String,Route> routes, Function<Request, Response> get, Function <Request, Response> put, Function <Request, Response> post, Function<Request,Response> defaultFunc){
		this.router = routes;
		this.router.put("", this);//loopback
		this.functions = new HashMap<String,Function<Request,Response>>();
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
	
	public Function<Request,Response> route(String [] path, int pathPlace, String request) {
		if (pathPlace == path.length) {
			return this.functions.get(request);
		}
		else {
			return this.router.get(path[pathPlace]).route(path, pathPlace + 1, request);
		}
	}
	
}
