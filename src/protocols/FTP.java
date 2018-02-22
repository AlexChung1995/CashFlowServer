package protocols;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import Communications.Request;
import Communications.Response;

//for File Transfer Protocol Connections
public class FTP extends Protocol{

	public FTP(String name) {
		super(name);
	}

	@Override
	public Request parse(InputStream stream) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response sendResponse(Request request, Response response, OutputStream stream) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

}
