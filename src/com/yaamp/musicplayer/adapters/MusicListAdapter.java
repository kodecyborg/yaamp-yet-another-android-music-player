package com.yaamp.musicplayer.adapters;

import java.util.ArrayList;

import com.yaamp.musicplayer.R;
import com.yaamp.musicplayer.SongData.Music;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

//ListView
public class MusicListAdapter extends ArrayAdapter<Music>{

	Context context;
	 int textViewResourceId;    
	 ArrayList<Music> musics = null;
	 MediaMetadataRetriever mmr;

	public MusicListAdapter(Context context, int textViewResourceId,
			ArrayList<Music> musics) {
		super(context, textViewResourceId, musics);
		mmr=new MediaMetadataRetriever();
		this.context=context;
		this.textViewResourceId=textViewResourceId;
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
        	row = inflater.inflate(textViewResourceId, parent, false);
            
            holder = new MusicHolder();
            holder.albumImage = (ImageView)row.findViewById(R.id.albumImage);
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
    	Bitmap bm=null;
		byte[] cover=mmr.getEmbeddedPicture();
		
		if(cover!=null){
	    bm = BitmapFactory.decodeByteArray(cover, 0, cover.length);
		holder.albumImage.setImageBitmap(bm);
		}
		else{
			holder.albumImage.setImageDrawable(row.getResources().getDrawable(R.drawable.no_music));
		}
        holder.txtTitle.setText(music.getTrackNumber()+"-"+music.getSongTitle());
        holder.txtArtist.setText(music.getArtistName());
        
        
        return row;
    }
	 static class MusicHolder
	    {
	        ImageView albumImage;
	        TextView txtTitle;
	        TextView txtArtist;
	    }
    
	


}
