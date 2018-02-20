package protocols;

import java.io.IOException;
import java.io.InputStream;

import Communications.Request;

public interface ProtocolParser {
	
	public Request parse(InputStream stream) throws IOException;
	
}
