package pl.airpolsl.synchromusic;

public class SendMulticast {
	static void start(){
		Thread packet = new Thread (new PacketUDP("start"));
		packet.start();
	}
	static void stop(){
		Thread packet = new Thread (new PacketUDP("stop"));
		packet.start();
	}
}
