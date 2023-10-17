package telran.net;

import java.io.*;
import java.net.*;
public class TcpHandler implements Closeable{
    private Socket socket;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private String host;
    private int port;
    
    public TcpHandler(String host, int port) throws Exception {
    	this.host = host;
    	this.port = port;
    	connect();
    }
	@Override
	public void close() throws IOException {
		socket.close();
		
	}
	public <T> T send(String requestType, Serializable requestData)  {
		Request request = new Request(requestType, requestData);
		T res = null;
		try {
			output.writeObject(request);
			res = getResponse(input);
			if(res == null) {
				connect();
				output.writeObject(request);
				res = getResponse(input);
			}
		} catch (IOException e) {
			try {
				connect();
				output.writeObject(request);
				res = getResponse(input);
			} catch (Exception e1) {
				
				e1.printStackTrace();
			}
			
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		return res;
		
	
	}
	
	private <T> T getResponse(ObjectInputStream input) {
		try {
			Response response = (Response) input.readObject();
			if (response.code() != ResponseCode.OK) {
				throw new RuntimeException(response.responseData().toString());
			}
			@SuppressWarnings("unchecked")
			T res = (T) response.responseData();
			return res;
		} catch (Exception e) {
			
			throw new RuntimeException(e.getMessage());
		}
	}
	
	private void connect() throws Exception {
		System.out.println("connection");
		socket = new Socket(host, port);
    	output = new ObjectOutputStream(socket.getOutputStream());
    	input = new ObjectInputStream(socket.getInputStream());
	}
	

}
