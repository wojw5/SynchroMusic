package pl.airpolsl.synchromusic;

import java.io.Serializable;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;

public class User implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4341181570622075127L;
	private String name;
	private String appVersion;
	private String osVersion;
	private Long cacheSize;
	
	
	public User(Context context){
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
		name = sharedPref.getString("pref_username", "anonymous");
		osVersion = Build.VERSION.RELEASE;
	};
	
	String getName(){
		return name;
	};
	String getOsVersion(){
		return osVersion;
	};
	
	
}
