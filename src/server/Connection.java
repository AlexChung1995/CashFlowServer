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
import Communications.Response;
import protocols.Protocol;
import protocols.ProtocolParser;
import utils.ByteUtils;
import utils.StringUtils;

//class for handling connections, threaded
public class Connection implements Runnable {

	private Socket clientSocket;
	private DataOutputStream out;
	private DataInputStream in;
	private Route routes;
	private Protocol protocol;
	private boolean keepAlive;
	
	public Connection(Socket clientSocket, Route routes, Protocol protocol) throws Exception {
		this.clientSocket = clientSocket;
		this.out = new DataOutputStream(clientSocket.getOutputStream());
		this.in = new DataInputStream(clientSocket.getInputStream());
		this.routes = routes;
		this.protocol = protocol;
		this.keepAlive = false;
	}
	
	//parse input to find route
	//parse body for route input
	@Override
	public void run() {
		//TODO: develop keepAlive logic
		try {
			Request request = this.protocol.parse(in);
			System.out.println("this.routes: " + this.routes.toString());
			Function<Request,Response> operation = this.routes.route(request.getPath(),0,request.getMethodString());
			Response response = operation.apply(request);
			this.protocol.sendResponse(request, response, out);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("closing connection");
		try {
			this.clientSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
