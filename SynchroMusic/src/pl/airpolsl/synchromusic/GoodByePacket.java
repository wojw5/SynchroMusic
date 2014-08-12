package pl.airpolsl.synchromusic;

public class GoodByePacket extends PacketTCP {
	
	User user;
	String reason;
	
	public GoodByePacket(User nUser,String nReason){
		user=nUser;
		reason=nReason;
	}

}
