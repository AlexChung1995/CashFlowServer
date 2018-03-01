package protocols;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import java.util.Map.Entry;
import java.util.function.Function;

import Communications.HTTPRequest;
import Communications.HTTPResponse;
import Communications.Request;
import Communications.Response;
import utils.ByteUtils;
import utils.StringUtils;

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
		byte[] request = new byte[1024];
		try {
			int bytesRead = stream.read(request,0,1024);
			while (stream.available() > 0) {
				System.out.println("stream.read() bytesRead: " + bytesRead + " " + StringUtils.stringify(request,1));
				bytesRead += stream.read(request,bytesRead,1024 - bytesRead);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
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
		responseString += response.getBody();
		stream.write(ByteUtils.toByteArray(responseString, request.getByteNum()));
		System.out.println("sendResponse: " + response.getBody() + " " + response.getStatus());
		return null;
	}

	@Override
	public Response sendResponse(Request request, Response response, OutputStream stream) throws IOException {
		return (Response) sendResponse((HTTPRequest) request, (HTTPResponse) response, stream);
	}

}
