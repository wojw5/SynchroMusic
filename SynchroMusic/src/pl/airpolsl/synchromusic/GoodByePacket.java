package pl.airpolsl.synchromusic;

public class GoodByePacket extends PacketTCP {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4988183167205366721L;
	User user;
	String reason;
	@Override
	void setRawData() {
		// TODO Auto-generated method stub

	}
	
	public GoodByePacket(User nUser,String nReason){
		user=nUser;
		reason=nReason;
	}

}
