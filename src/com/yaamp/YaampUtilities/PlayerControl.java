package com.yaamp.YaampUtilities;

import java.io.IOException;

import android.media.MediaPlayer;

import com.yaamp.musicplayer.SongData.Music;

public class PlayerControl {

	private  MediaPlayer mediaPlayer;

	private static PlayerControl instance = null;
    
	public MediaPlayer getMediaPlayer() {
		return mediaPlayer;
	}

	public static synchronized PlayerControl getInstance() {
        if (instance == null) {
            instance = new PlayerControl();
        }
        return instance;
    }

	public void playMusic(Music music) {
		// Play song
		
		try {
			if (music != null) {
				mediaPlayer.reset();
				mediaPlayer.setDataSource(music.getSongPath());

				mediaPlayer.prepare();
				mediaPlayer.start();
				// Changing Button Image to pause image
				

				
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void pause()
	{
		try{
		
			if (mediaPlayer != null) {
				mediaPlayer.pause();
				
			}
			
		}
		catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	public void play()
	{
		try{
		
			if (mediaPlayer != null) {
				mediaPlayer.start();
				
			}
			
		}
		catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	

	
	public void stop(Music music){
		
	}
	
	public void pause(Music music){
		
	}
	/*
	public void playNext(int currentSongIndex)
	{
		
		if (currentSongIndex > 0) {
			playSong(currentSongIndex - 1);
			currentSongIndex = currentSongIndex - 1;
		} else {
			// play last song
			playSong(songsList.size() - 1);
			currentSongIndex = songsList.size() - 1;
		}
	}
	*/
	public void playPrevious()
	{}
	
	private PlayerControl() { 
        mediaPlayer=new MediaPlayer();
	}
}
