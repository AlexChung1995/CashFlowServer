package server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashMap;
import java.util.function.Function;

import utils.StringUtils;

//class for handling connections, threaded
public class Connection implements Runnable {

	private Socket clientSocket;
	private DataOutputStream out;
	private BufferedReader in;
	private Route routes;
	
	public Connection(Socket clientSocket, Route routes) throws Exception {
		this.clientSocket = clientSocket;
		this.out = new DataOutputStream(clientSocket.getOutputStream());
		this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		this.routes = routes;
	}
	
	//find route with initial line
	//use remaining lines as input to route function
	@Override
	public void run() {
		try {
			String input;
			Function<String[],byte[]> operation = null;
			while ((input = this.in.readLine()) != null) {
				System.out.println(input);
				if (input.equals(">")) {
					System.out.println("reading new request");
					String request = this.in.readLine();
					String[] paths = StringUtils.split(StringUtils.strip(this.in.readLine(),"/"),"/");
					operation = this.routes.route(paths, 0, request);
				}
				else if (input.equals("<")) {
					System.out.println("ending request");
					break;
				}
				else {
					System.out.println("applying operator");
					this.out.write(operation.apply(StringUtils.split(input, ",")));
				}
			}
			this.clientSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private void initConnection() {
		
	}

}
