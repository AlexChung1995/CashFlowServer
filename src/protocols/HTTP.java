package protocols;

import java.io.IOException;
import java.io.InputStream;

import Communications.HTTPRequest;
import Communications.Request;
import utils.ByteUtils;
import utils.StringUtils;


//For HTTP connections
public class HTTP extends Protocol{
	
	public HTTP(String name) {
		super(name);
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
	
	public HTTPRequest parseHeaders(byte[] header) {
		HTTPRequest request = new HTTPRequest();
		request.setHeaders(header);
		
		return request;
	}

	public void getBody(InputStream stream) {
		// TODO Auto-generated method stub
		
	}

}
