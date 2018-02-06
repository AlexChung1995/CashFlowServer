package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import Supreme.Authorize;
import Supreme.Generate;
import db.Driving;
import server.Route;
import utils.ByteUtils;
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
				Connection connection = new Connection(clientSocket,this.base);
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
	
	public Driving initDB(String url, String user, String password) throws SQLException {
		Driving driver = new Driving(url,user,password);
		this.db = driver;
		return driver;
	}
	
	public void initServer() {
		try {
			initDB(System.getenv("DATABASE_URL"), "postgres", "346578a@A");
			Route retrieve = new Route(new HashMap<String,Route>(),
					(params) -> {
						try {
							ResultSet rs = this.db.getDB().createStatement().executeQuery("SELECT * FROM SupremeCash;");
							while (rs.next()) {
								System.out.println(rs.getString(1));
							}
							return ByteUtils.toByteArray("");
						} catch (Exception e) {
							return ByteUtils.toByteArray("400"  + e.getLocalizedMessage());
						}
					},
					null,
					null
			);
			Route generate = new Route(new HashMap<String,Route>(), 
					(params) -> {
						try {
							byte[] bytes = Generate.generateRandomKey(new byte[10]);
							return bytes;
						} catch(Exception e) {
							return ByteUtils.toByteArray("400 " + e.getLocalizedMessage());
						}
					}, 
					null,
					null 
			);
			Route authorize = new Route(new HashMap<String,Route>(),
					(params) -> {
						return ByteUtils.toByteArray(Authorize.authorize(ByteUtils.toByteArray(params[0])));
					},
					null,
					null
			);
			HashMap<String,Route> routes = new HashMap<String,Route>();
			routes.put("generate", generate);
			routes.put("authorize", authorize);
			routes.put("retrieve", retrieve);
			Route base = new Route(routes,
					null,
					null,
					null
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
