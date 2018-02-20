package protocols;

import java.io.IOException;
import java.io.InputStream;

import Communications.Request;

public class Protocol implements ProtocolParser {
	
	protected String name;//protocol name eg "HTTP"
	
	public Protocol(String name) {
		this.name = name;
	}

	@Override
	public Request parse(InputStream stream) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

}
