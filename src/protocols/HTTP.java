package protocols;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map.Entry;
import java.util.function.Function;

import Communications.HTTPRequest;
import Communications.HTTPResponse;
import Communications.Request;
import Communications.Response;
import utils.ByteUtils;

//For HTTP connections
public class HTTP extends Protocol{
	
	public HTTP() {
		super("HTTP");
		this.defaultFunc =
			(request) -> {
				HTTPResponse response = new HTTPResponse(404);
				return response;
			};
	}

	@Override
	public HTTPRequest parse(InputStream stream) { 
		int read = 0;
		byte[] request = new byte[1024];
		try {
			read = stream.read(request);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HTTPRequest requestObj = new HTTPRequest(request);
		return requestObj;
	}

	public HTTPResponse sendResponse(HTTPRequest request, HTTPResponse response, OutputStream stream) throws IOException{
		String responseString = "";
		responseString += request.getProtocolVersion() + " " + response.getStatus() + " " + response.getStatusMsg() + "\r\n";
		for (Entry<String,String> entry: response.getHeaders().entrySet()) {
			responseString += entry.getKey() +": " + entry.getValue() + "\r\n";
		}
		responseString += "\r\n\r\n";
		for (Entry<String,String> entry: response.getBody().entrySet()) {
			responseString += entry.getKey() +": " + entry.getValue() + "\r\n";
		}
		stream.write(ByteUtils.toByteArray(responseString, request.getByteNum()));
		return null;
	}

	@Override
	public Response sendResponse(Request request, Response response, OutputStream stream) throws IOException {
		return (Response) sendResponse((HTTPRequest) request, (HTTPResponse) response, stream);
	}

}
