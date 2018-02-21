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

import Communications.HTTPResponse;
import Communications.Request;
import Communications.Response;
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
		this.fixedThreadPool = Executors.newFixedThreadPool(threadNumber);
		this.serverSocket = new ServerSocket(port);
		this.sizeData = Byte.SIZE;//default encoding 
	}
	
	public void run() {
		while (true) {
			try {
				System.out.println("accepting new connection");
				Socket clientSocket = this.serverSocket.accept();
				Connection connection = new Connection(clientSocket,this.base, new HTTP("HTTP"));
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
		try {
			SupremeDriving supreme = new SupremeDriving(System.getenv("DATABASE_URL"), "postgres", "346578a@A");
			this.db = supreme;
			Function<Request,Response> defaultFunc =
					(request) -> {
						HTTPResponse response = new HTTPResponse(404);
						return response;
					};
			Route retrieve = new Route(new HashMap<String,Route>(),
					(request) -> {
						byte [] bytes = new byte[1024];
						try {
							ResultSet rs = this.db.getDB().createStatement().executeQuery("SELECT * FROM authentication;");
							while (rs.next()) {
								System.out.println(rs.getBytes(1));
							}
							return rs.getBytes(1);
						} catch (Exception e) {
							return ByteUtils.toByteArray("400"  + e.getLocalizedMessage(),request.getByteNum());
						}
					},
					null,
					null,
					defaultFunc
			);
			Route generate = new Route(new HashMap<String,Route>(), 
					(request) -> {
						try {
							byte[] bytes = supreme.getAuthentication().generateRandomKey(new byte[20]);
							supreme.getAuthentication().add(StringUtils.stringify(bytes, 2));
							return bytes;
						} catch(Exception e) {
							return ByteUtils.toByteArray("400 " + e.getLocalizedMessage(), request.getByteNum());
						}
					}, 
					null,
					null,
					defaultFunc
			);
			Route authorize = new Route(new HashMap<String,Route>(),
					(request) -> {
						return ByteUtils.toByteArray(false, request.getByteNum());//ByteUtils.toByteArray(supreme.getAuthentication().validate(key, status, number_of_processors, user_profile, processor_identifier, os, computer_name, processor_architecture, java_home, username));
					},
					null,
					null,
					defaultFunc
			);
			HashMap<String,Route> routes = new HashMap<String,Route>();
			routes.put("generate", generate);
			routes.put("authorize", authorize);
			routes.put("retrieve", retrieve);
			Route base = new Route(routes,
					null,
					null,
					null,
					defaultFunc
			);
			this.setRoutes(base);
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
