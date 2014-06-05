package pl.airpolsl.synchromusic;

public class ConnectionDetails {
	private String protocol;
	protected enum SendMode {
	    Unicast,Multicast, Broadcast
	} 
	private SendMode sendMode;
	
	public ConnectionDetails(String nProtocol, SendMode nSendMode){
		protocol=nProtocol;
		sendMode=nSendMode;
	}
}
