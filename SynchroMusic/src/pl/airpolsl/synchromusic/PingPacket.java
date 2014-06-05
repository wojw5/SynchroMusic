package pl.airpolsl.synchromusic;

import java.util.Random;

public class PingPacket extends PacketUDP {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3429520175347289332L;
	private int id;
	@Override
	void setRawData() {
		// TODO Auto-generated method stub

	}

	public PingPacket(){
		Random generator = new Random();
		id=generator.nextInt();
	}
	
	public PingPacket(int nId){
		id=nId;
	}
}
