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

//GridView
public class AlbumsGridViewAdapter extends ArrayAdapter<Music>{

	Context context; 
	int layoutRessourceID; 
	ArrayList<Music> albumList;
	MediaMetadataRetriever mmr;

	public AlbumsGridViewAdapter(Context context, 
			int layoutRessourceID,
			ArrayList<Music> albumList) {
		
		super(context, layoutRessourceID, albumList);
		mmr=new MediaMetadataRetriever();
		this.context=context;
		this.layoutRessourceID=layoutRessourceID;
		this.albumList=albumList;
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View row=convertView;
		AlbumViewHolder holder=null;
		if(row==null){
			LayoutInflater inflater = (LayoutInflater) context
			        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row=inflater.inflate(layoutRessourceID, parent,false);
			holder=new AlbumViewHolder();
			
			holder.albumTitle=(TextView)row.findViewById(R.id.grid_text);
			holder.albumImage=(ImageView)row.findViewById(R.id.grid_image);
			row.setTag(holder);
		}
		else{
		holder = (AlbumViewHolder) row.getTag();
		}
		
		Music music = albumList.get(position);
		mmr.setDataSource(music.getSongPath());
		 Bitmap bm =null;
		byte[] cover=mmr.getEmbeddedPicture();
		
		if(cover!=null){
			bm = BitmapFactory.decodeByteArray(cover, 0, cover.length);
			 holder.albumImage.setImageBitmap(bm);

		}else
		{
			holder.albumImage.setImageDrawable(row.getResources().getDrawable(R.drawable.no_music));
		}
		  holder.albumTitle.setText(music.getAlbumName());
		return row;
	}

	
	

	static class AlbumViewHolder
	{ 
		TextView albumTitle; 
		ImageView albumImage; }
	
	
}
