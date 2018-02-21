package protocols;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.function.Function;

import Communications.Request;
import Communications.Response;

public abstract class Protocol implements ProtocolParser {
	
	protected String name;//protocol name eg "HTTP"
	protected Function<Request,Response> defaultFunc;
	
	public Protocol(String name) {
		this.name = name;
	}

	@Override
	public abstract Request parse(InputStream stream);
	
	public abstract Response sendResponse(Request request, Response response, OutputStream stream) throws IOException;
	
}
