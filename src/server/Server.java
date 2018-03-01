package server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;

import org.postgresql.*;

import Communications.HTTPRequest;
import Communications.HTTPResponse;
import Communications.Request;
import Communications.Response;
import Supreme.Authentication;
import Supreme.SupremeDriving;
import db.Driving;
import protocols.HTTP;
import server.Route;
import utils.ByteUtils;
import utils.StringUtils;
//server class for listening and handling requests
public class Server implements Runnable {

	private ExecutorService fixedThreadPool;
	private int port;
	private ServerSocket serverSocket;
	private Route base;
	private Driving db;
	private int sizeData;
	
	public Server(int portNumber, int threadNumber) throws Exception{
		this.port = portNumber;
		System.out.println("portNumber: " + portNumber);
		this.fixedThreadPool = Executors.newFixedThreadPool(threadNumber);
		this.serverSocket = new ServerSocket(port);
		this.sizeData = Byte.SIZE;//default encoding 
	}
	
	public void run() {
		while (true) {
			try {
				System.out.println("accepting new connection");
				Socket clientSocket = this.serverSocket.accept();
				System.out.println("this.base: " + this.base.toString());
				Connection connection = new Connection(clientSocket,this.base, new HTTP());
				this.fixedThreadPool.execute(connection);
			}
			catch (Exception e) {
				e.printStackTrace();
				break;
			}
		}
		try {
			this.serverSocket.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setSize(int size) {
		this.sizeData = size;
	}
	
	public void setRoutes(Route base) {
		this.base = base;
	}
	
	public Driving initDB(String url, String user, String password) throws SQLException, ClassNotFoundException {
		Driving driver = new Driving(url,user,password);
		this.db = driver;
		return driver;
	}
	
	public void initServer() {
		System.out.println("Running initServer()");
		try {
			SupremeDriving supreme = new SupremeDriving(System.getenv("DATABASE_URL"), "postgres", "346578a@A");
			this.db = supreme;
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
			this.base = base;
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public Driving getDB() {
		return this.db;
	}
	
	public static void main(String[] args) {
		try {
			Server server = new Server(Integer.parseInt(System.getenv("PORT")),Integer.parseInt(System.getenv("THREAD_NUMBER")));
			server.initServer();
			server.run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
