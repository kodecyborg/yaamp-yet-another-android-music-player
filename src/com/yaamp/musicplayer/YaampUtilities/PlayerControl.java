package com.yaamp.musicplayer.YaampUtilities;

import java.io.IOException;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.audiofx.BassBoost;
import android.media.audiofx.Equalizer;
import android.os.IBinder;

import com.yaamp.musicplayer.SongData.Music;

public class PlayerControl extends Service{
	 Equalizer equalizer =null;
	 BassBoost bassBoost=null;
	private  MediaPlayer mediaPlayer;

	private static PlayerControl instance = null;
    
	public MediaPlayer getMediaPlayer() {
		return mediaPlayer;
	}
	
	public Equalizer getEqualizer()
	{
		return equalizer;
	}

	public BassBoost getBassBoost()
	{
		return bassBoost;
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
        equalizer=new Equalizer(0, 0);
        bassBoost=new BassBoost(0, 0);
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
