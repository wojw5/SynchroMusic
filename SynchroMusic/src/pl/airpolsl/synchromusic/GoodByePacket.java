package pl.airpolsl.synchromusic;

public class GoodByePacket extends PacketTCP {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3521281134881054936L;
	private User user;
	private String reason;
	
	public GoodByePacket(User nUser,String nReason){
		user=nUser;
		reason=nReason;
	}

	@Override
	public String toString() {
		return "GoodBye: " + user.getName() + reason;
	}

}
