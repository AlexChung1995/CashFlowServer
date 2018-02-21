package protocols;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import Communications.HTTPRequest;
import Communications.HTTPResponse;
import Communications.Request;
import Communications.Response;

public interface ProtocolParser {
	
	public Request parse(InputStream stream) throws IOException;

	public Response sendResponse(Request request, Response response, OutputStream stream) throws IOException;
	
}
