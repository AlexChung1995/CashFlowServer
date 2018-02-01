package server;

import java.util.HashMap;
import java.util.function.Function;

public class Route {
	
	private HashMap<String,Route> router;
	private HashMap<String,Function<String [],String>> functions; 
	
	public Route(HashMap<String,Route> routes, Function<String[], String> get, Function <String[], String> put, Function <String[], String> post){
		this.router = routes;
		this.router.put("", this);//loopback
		Function<String[], String> defaultFunc = (params) -> { return "404 Not Found"; };
		this.functions = new HashMap<String,Function<String[],String>>();
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
	
	public Function<String[],String> route(String [] path, int pathPlace, String request) {
		if (pathPlace == path.length) {
			return this.functions.get(request);
		}
		else {
			return this.router.get(path[pathPlace]).route(path, pathPlace + 1, request);
		}
	}
	
}
