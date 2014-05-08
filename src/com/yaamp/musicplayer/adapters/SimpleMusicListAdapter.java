package com.yaamp.musicplayer.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.yaamp.musicplayer.R;
import com.yaamp.musicplayer.SongData.Music;

public class SimpleMusicListAdapter extends ArrayAdapter<Music>{

	Context context;
	 int viewResourceId;    
	 ArrayList<Music> musics = null;
	 MediaMetadataRetriever mmr;

	public SimpleMusicListAdapter(Context context, int viewResourceId,
			ArrayList<Music> musics) {
		super(context, viewResourceId, musics);
		mmr=new MediaMetadataRetriever();
		this.context=context;
		this.viewResourceId=viewResourceId;
		this.musics=musics;
	
	}

	
	@Override
   public View getView(int position, View convertView, ViewGroup parent) {
       View row = convertView;
       MusicHolder holder = null;
       
       if(row == null)
       {
       	LayoutInflater inflater = (LayoutInflater) context
			        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);            
       	row = inflater.inflate(viewResourceId, parent, false);
           
           holder = new MusicHolder();
           holder.txtTitle = (TextView)row.findViewById(R.id.musicTitle);
           holder.txtArtist=(TextView)row.findViewById(R.id.artistName);
           row.setTag(holder);
       }
       else
       {
           holder = (MusicHolder)row.getTag();
       }
       
       Music music = musics.get(position);
   	mmr.setDataSource(music.getSongPath());
   	
		

       holder.txtTitle.setText(music.getSongTitle());
       holder.txtArtist.setText(music.getArtistName());
       
       
       return row;
   }
	 static class MusicHolder
	    {
	       
	        TextView txtTitle;
	        TextView txtArtist;
	    }
   
	

}
