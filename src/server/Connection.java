package server;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.function.Function;

import Communications.Request;
import protocols.ProtocolParser;
import utils.ByteUtils;
import utils.StringUtils;

//class for handling connections, threaded
public class Connection implements Runnable {

	private Socket clientSocket;
	private DataOutputStream out;
	private DataInputStream in;
	private Route routes;
	private ProtocolParser parser;
	private boolean keepAlive;
	
	public Connection(Socket clientSocket, Route routes, ProtocolParser parser) throws Exception {
		this.clientSocket = clientSocket;
		this.out = new DataOutputStream(clientSocket.getOutputStream());
		this.in = new DataInputStream(clientSocket.getInputStream());
		this.routes = routes;
		this.parser = parser;
		this.keepAlive = false;
	}
	
	//parse input to find route
	//parse body for route input
	@Override
	public void run() {
		int read = 0;
		while (read >= 0) {
			try {
				Request request = this.parser.parse(in);
				Function<Request,byte[]> operation = this.routes.route(request.getPath(),0,request.getMethodString());
				byte [] response = operation.apply(request);
				out.write(response);
			}
			catch (IOException e) {
				e.printStackTrace();
				break;
			}
		}
		System.out.println("closing connection");
		try {
			this.clientSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void sendResponse(Request request, byte[] response){
		
	}
}
