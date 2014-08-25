package pl.airpolsl.synchromusic;

import java.util.ArrayList;

import android.content.Context;



public class Tracks extends ArrayList<Track> {
	
	private static final long serialVersionUID = -8546012108223809889L;
	
	public void initPlayers(Context context){
		int max=3;
		if(max>this.size()) max=this.size();
		for (int i=0;i<max;i++) {
			this.get(i).initPlayer(context);
		}
	}

}
