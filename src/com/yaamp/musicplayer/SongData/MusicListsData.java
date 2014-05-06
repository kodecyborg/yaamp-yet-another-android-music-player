package com.yaamp.musicplayer.SongData;

import java.io.Serializable;
import java.util.ArrayList;

public class MusicListsData implements Serializable{
	final static long serialVersionUID=1L;
	
	private ArrayList<Music> musicList = new ArrayList<Music>();

	public MusicListsData(ArrayList<Music> musicList) {
		super();
		this.musicList = musicList;
		
		
	}

	public ArrayList<Music> getMusicList() {
		return musicList;
	}



	public void setMusicList(ArrayList<Music> musicList) {
		this.musicList = musicList;
	}

	


	
	

}
