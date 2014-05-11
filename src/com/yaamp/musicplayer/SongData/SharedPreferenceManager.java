package com.yaamp.musicplayer.SongData;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceManager {

	Context context;
	SharedPreferences playerState;
	SharedPreferences.Editor editor;
	
	public SharedPreferenceManager(Context context) {
		super();
		this.context = context;
	
		playerState =context.getSharedPreferences("yaamp_prefs", 0);
		editor=playerState.edit();
	}
	
	public int getLastSelectedAlbumIndex()
	{
		return playerState.getInt("LastSelectedAlbum",-1);
	}
	
	public void setLastSelectedAlbumIndex(int value)
	{
		editor.putInt("LastSelectedAlbum", value);
		editor.commit();
	}
	
	public int getLastPlaylistIndex()
	{
		return playerState.getInt("LastPlayListIndex", 0);
	}
	
	public void setLastPlaylistIndex(int value)
	{
		editor.putInt("LastPlayListIndex", value);
		editor.commit();
		
	}

}
