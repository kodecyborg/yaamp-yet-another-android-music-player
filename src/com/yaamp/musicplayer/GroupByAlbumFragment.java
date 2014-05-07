package com.yaamp.musicplayer;

import java.util.ArrayList;

import com.yaamp.musicplayer.SongData.Music;
import com.yaamp.musicplayer.SongData.MusicDB;
import com.yaamp.musicplayer.adapters.AlbumsGridViewAdapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;

public class GroupByAlbumFragment extends Fragment {

	Context context;
	 //ArrayList<byte[]> albumCoverArray=new ArrayList<byte[]>();
	 LinearLayout albumLayout;
	 
	 GridView albumGridView;
	 private ArrayList<Music> groups = new ArrayList<Music>();
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		context=container.getContext();
		View rootView = inflater.inflate(R.layout.fragment_albums, container, false);
		
		getAlbums(context);

		albumGridView = (GridView) rootView.findViewById(R.id.albumGridView);

		AlbumsGridViewAdapter adapter=new AlbumsGridViewAdapter(context, R.layout.grid_single_album, groups);
		
			albumGridView.setAdapter(adapter);

			albumGridView.setOnItemClickListener(new OnItemClickListener() { 
				@Override public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3)
				{ 
					Intent intent=new Intent(getActivity(),MusicsFromAlbumFragment.class);
					intent.putExtra("albumName", groups.get(position).getAlbumName());
					startActivity(intent);
			} });

			
		return rootView;
	}

	private void getAlbums(Context context){
		
		MusicDB mdb=new MusicDB(context);
		groups.clear();
		ArrayList<Music> musicArrayList=null;

		for(int i=1;i<mdb.getMusicsByAlbum().size();i++){
			musicArrayList=mdb.getMusicsByAlbum().get(i).iterator().next();
			if(null!=mdb.getMusicsByAlbum().get(i)){
				Music music=musicArrayList.iterator().next();
			groups.add(i-1, music);
			
		}
	}
	}

	}
	
	
	
	

