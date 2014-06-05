package pl.airpolsl.synchromusic;

public class WelcomePacket extends PacketTCP {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2255228179268266933L;
	User user;
	@Override
	void setRawData() {
		// TODO Auto-generated method stub

	}
	
	public WelcomePacket(User nUser){
		user=nUser;
	}

}
