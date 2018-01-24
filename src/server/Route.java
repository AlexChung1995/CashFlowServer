package server;

import java.util.HashMap;
import java.util.function.Function;

public class Route {
	
	private HashMap<String,Route> router;
	private HashMap<String,Function<String [],String>> functions; 
	
	public Route(HashMap<String,Route> routes, Function<String[], String> get, Function <String[], String> put, Function <String[], String> post) throws Exception{
		if (get == null || put == null || post == null) {
			throw new IllegalArgumentException("Must provide a lambda expression for get, post and put"); 
		}
		this.router = routes;
		this.router.put("", this);//loopback
		this.functions = new HashMap<String,Function<String[],String>>();
		this.functions.put("GET", get);
		this.functions.put("PUT", put);
		this.functions.put("POST", post);
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
