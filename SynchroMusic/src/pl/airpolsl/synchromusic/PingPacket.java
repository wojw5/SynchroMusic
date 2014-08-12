package pl.airpolsl.synchromusic;

import java.util.Random;

public class PingPacket extends PacketUDP {

	private int id;


	public PingPacket(){
		Random generator = new Random();
		id=generator.nextInt();
	}
	
	public PingPacket(int nId){
		id=nId;
	}
}
