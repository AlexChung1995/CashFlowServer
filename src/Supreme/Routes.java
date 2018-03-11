package Supreme;

import server.Server;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.function.Function;

import Communications.HTTPRequest;
import Communications.HTTPResponse;
import Communications.Request;
import Communications.Response;
import server.Route;

public class Routes {

	public static void main(String[] args) {
		Server server;
		try {
			server = new Server(Integer.parseInt(System.getenv("PORT")),Integer.parseInt(System.getenv("THREAD_NUMBER")));
			server.setRoutes(createRoutes());
			server.run();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static Route createRoutes() {
		System.out.println("Running initServer()");
		try {
			SupremeDriving supreme = new SupremeDriving(System.getenv("JDBC_DATABASE_URL"), System.getenv("JDBC_DATABASE_USERNAME"), System.getenv("JDBC_DATABASE_PASSWORD"));
			Function<Request,Response> defaultFunc =
					(request) -> {
						HTTPResponse response = new HTTPResponse(404);
						return response;
					};
			Route validate = new Route(new HashMap<String,Route>(),
					(request) -> {
						HTTPRequest http = (HTTPRequest) request;
						Authentication authenticate = supreme.getAuthentication();
						HTTPResponse response = new HTTPResponse(200);
						ResultSet result;
						String body;
						try {
							result = authenticate.validate(http.getBodyVal("key"), Integer.parseInt(http.getBodyVal("number_of_processors")), 
									http.getBodyVal("user_profile"), http.getBodyVal("processor_identifier"), http.getBodyVal("os"), 
									http.getBodyVal("computer_name"), http.getBodyVal("processor_architecture"), http.getBodyVal("java_home"), 
									http.getBodyVal("username"));
							body = result.getString("status");
						} catch (NumberFormatException e) {
							e.printStackTrace();
							response.setStatus(500);
							response.setBody(e.toString());
							return response;
						} catch (SQLException e) {
							e.printStackTrace();
							response.setStatus(500);
							response.setBody(e.toString());
							return response;
						}
						response.setBody(body);
						return response;
					},
					null,
					null,
					defaultFunc
			);
			Route generate = new Route(new HashMap<String,Route>(), 
					(request) -> {
						Authentication authenticate = supreme.getAuthentication();
						String key = authenticate.generateRandomString(20);
						HTTPResponse response = new HTTPResponse(200);
						try {
							authenticate.add(key);
						} catch (SQLException e) {
							e.printStackTrace();
							response.setStatus(500);
							response.setBody(e.toString());
							return response;
						}
						response.setBody(key);
						return response;
					}, 
					null,
					null,
					defaultFunc
			);
			Route authorize = new Route(new HashMap<String,Route>(),
					(request) -> {
						HTTPRequest http = (HTTPRequest) request;
						HTTPResponse response = new HTTPResponse(200);
						Authentication authenticate = supreme.getAuthentication();
						try {
							int numUpdated = authenticate.authorize(http.getBodyVal("key"), Authentication.Status.authorized, Integer.parseInt(http.getBodyVal("number_of_processors")), 
									http.getBodyVal("user_profile"), http.getBodyVal("processor_identifier"), http.getBodyVal("os"), 
									http.getBodyVal("computer_name"), http.getBodyVal("processor_architecture"), http.getBodyVal("java_home"), 
									http.getBodyVal("username"));
							response.setStatus(200);
							response.setBody("numUpdated: " + numUpdated);
						} catch (NumberFormatException | SQLException e) {
							e.printStackTrace();
							response.setStatus(500);
							response.setBody(e.toString());
						}
						return response;
					},
					null,
					null,
					defaultFunc
			);
			HashMap<String,Route> routes = new HashMap<String,Route>();
			routes.put("generate", generate);
			routes.put("authorize", authorize);
			routes.put("validate", validate);
			Route base = new Route(routes,
					null,
					null,
					null,
					defaultFunc
			);
			return base;
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
