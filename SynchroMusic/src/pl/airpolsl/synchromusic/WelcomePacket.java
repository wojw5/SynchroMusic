package pl.airpolsl.synchromusic;

import android.content.Context;

public class WelcomePacket extends PacketTCP {

	private static final long serialVersionUID = -4094906936139557885L;
	private User user;
	
	public WelcomePacket(Context context){
		user= new User(context);
	}
	
	public User getUser() {
		return user;
	}

	@Override
	public String toString() {
		return "Welcome: " + user.getName() + " (Android " + user.getOsVersion() +")";
	}

}
