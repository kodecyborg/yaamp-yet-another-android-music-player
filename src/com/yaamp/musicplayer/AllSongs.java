package com.yaamp.musicplayer;

import java.util.ArrayList;

import com.yaamp.musicplayer.SongData.Music;
import com.yaamp.musicplayer.SongData.MusicDB;
import com.yaamp.musicplayer.adapters.MusicListAdapter;

import android.support.v4.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class AllSongs extends ListFragment {
	// Songs list
	Context context;
		public ArrayList<Music> songsList = new ArrayList<Music>();
		private final int RESULT_LIST = 3;
		private MusicDB mdb;
		private Music music=new Music();
		// Search EditText
	    //EditText inputSearch;
	@Override
	public View onCreateView(LayoutInflater inflater, 
			ViewGroup container,
			Bundle savedInstanceState) {
		this.context=container.getContext();
		mdb=new MusicDB(context);
		View rootView = inflater.inflate(R.layout.all_musics, container, false);

		return rootView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {

		super.onActivityCreated(savedInstanceState);
		SongsManager plm = new SongsManager();
		
		plm.execute();

			songsList=mdb.getAllMusics();
			MusicListAdapter adapter=new MusicListAdapter(context,R.layout.album_music_item, songsList);
		      

		setListAdapter(adapter);
		ListView lv = getListView();
	
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, 
					View view,
					int position, 
					long id) {
				// getting listitem index
				
				music=songsList.get(position);
				// Starting new intent
				Intent intent = new Intent(context,YaampActivity.class);
				
				// Sending songIndex to PlayerActivity
				intent.putExtra("allMusicSong", music);
				intent.putExtra("songsList", songsList);
				getActivity().setResult(RESULT_LIST, intent);
				// Closing allSongs view
				getActivity().finish();
			}
		});


	}

	
	
}
