package pl.airpolsl.synchromusic;

public class WelcomePacket extends PacketTCP {

	User user;
	
	public WelcomePacket(User nUser){
		user=nUser;
	}

}
