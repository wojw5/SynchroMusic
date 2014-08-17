package pl.airpolsl.synchromusic;

import java.util.ArrayList;

import android.content.Context;



public class Tracks extends ArrayList<Track> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8546012108223809889L;
	private static int playersNumber = 0;
	
	public void initPlayers(Context context){
		for (int i=playersNumber;i<=3;i++) {
			this.get(i).initPlayer(context);
		}
	}

}
