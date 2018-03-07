package server;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;
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
	private Function<Request, Response> defaultFunc;
	
	public Route(HashMap<String,Route> routes, Function<Request, Response> get, Function <Request, Response> put, 
			Function <Request, Response> post, Function<Request,Response> defaultFunc){
		this.router = routes;
		this.defaultFunc = defaultFunc;
		this.router.put("", this);//loopback
		this.functions = new HashMap<String,Function<Request,Response>>();
		this.functions.put("GET", get);
		this.functions.put("PUT", put);
		this.functions.put("POST", post);
	}
	
	public Function<Request,Response> route(String [] path, int pathPlace, String request) {
		System.out.println("path: " + Arrays.toString(path) + " pathPlace: " + pathPlace + " path.length: " + path.length + " " + request + ";");	
		if (pathPlace == path.length) {
			Function<Request,Response> func = this.functions.get(request);
			if (func == null) {
				System.out.println("no route, sending defaultFunc");
				func = this.defaultFunc;
			}
			return func;
		}else {
			try {
				return this.router.get(path[pathPlace]).route(path, pathPlace + 1, request);
			} catch(NullPointerException e) {
				System.out.println("No such route: " + Arrays.toString(path));
				return this.defaultFunc;
			}
		}
	}
	
	public String toString() {
		String routeAsString = "route: {\r\n";
		for (Entry<String, Function<Request,Response>> route: functions.entrySet()) {
			routeAsString += "\r" + route.getKey() + ": " + route.getValue() + "\r\n";
		}
		routeAsString += "}";
		return routeAsString;
	}
	
}
