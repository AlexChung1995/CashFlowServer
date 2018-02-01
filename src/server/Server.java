package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import Supreme.Authorize;
import Supreme.Generate;
import server.Route;
import utils.ByteUtils;
//server class for listening and handling requests
public class Server implements Runnable {

	private ExecutorService fixedThreadPool;
	private int port;
	private ServerSocket serverSocket;
	private Route base;
	
	public Server(int portNumber, int threadNumber, Route base) throws Exception{
		this.port = portNumber;
		this.fixedThreadPool = Executors.newFixedThreadPool(threadNumber);
		this.serverSocket = new ServerSocket(port);
		this.base = base;
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
	
	public static void main(String[] args) {
		try {
			Route generate = new Route(new HashMap<String,Route>(), 
					(params) -> {
						try {
							return Generate.generateRandomKey(new byte[10]);
						}
						catch(IOException e) {
							return ByteUtils.toByteArray("400 " + e.getLocalizedMessage());
						}
					}
					, 
					null
					,
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
			Route base = new Route(routes,
					null,
					null,
					null
			);
			Server server = new Server(Integer.parseInt(System.getenv("PORT")),Integer.parseInt(System.getenv("THREAD_NUMBER")),base);
			System.out.println(System.getenv("PORT") + " " + System.getenv("THREAD_NUMBER"));
			server.run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
