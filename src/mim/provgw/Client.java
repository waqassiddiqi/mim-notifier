package mim.provgw;

import mim.notifier.model.RequestResult;
import mim.ucip.util.network.TcpClient;

import org.apache.log4j.Logger;

public class Client {
	public static String provGwIP = "127.0.0.1";
	public static int provGwPort = 9091;
	private TcpClient client;
	
	private Logger log = Logger.getLogger(getClass().getName());	
	
	public Client() {
		client = new TcpClient();
	}
	
	public void sendRequest(String request, String referenceId) {
		
		RequestResult r = client.sendRequest(provGwIP, provGwPort, 30, 30, request, referenceId);
		
		log.debug("ProvGw response: " + r.responseString);
		
	}
}
