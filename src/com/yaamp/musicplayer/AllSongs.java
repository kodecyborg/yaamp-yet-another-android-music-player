package com.yaamp.musicplayer;

import java.util.ArrayList;

import com.yaamp.musicplayer.SongData.Music;
import com.yaamp.musicplayer.SongData.MusicDB;
import com.yaamp.musicplayer.adapters.MusicListAdapter;
import com.yaamp.musicplayer.adapters.SimpleMusicListAdapter;
import com.yaamp.musicplayer.customViews.LazyAdapter;
import com.yaamp.musicplayer.customViews.LazyListView;
import com.yaamp.musicplayer.customViews.LazyListView.LazyListener;

import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class AllSongs extends Fragment {
	// Songs list
	Context context;
	public ArrayList<Music> songsList = new ArrayList<Music>();
	private final int RESULT_LIST = 3;
	private MusicDB mdb;
	

	ListView lv;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container,
				 savedInstanceState);
		this.context = container.getContext();
		
		mdb = new MusicDB(context);
		songsList = mdb.getAllMusics();
		
		View rootView = inflater.inflate(R.layout.all_musics, container, false);
		lv = (ListView) rootView.findViewById(R.id.allMusicList);
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {

		super.onActivityCreated(savedInstanceState);
		
		
		
		SimpleMusicListAdapter adapter = new SimpleMusicListAdapter(context,
				R.layout.simple_music_item, songsList);

		lv.setAdapter(adapter);
		
		

		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// getting listitem index
				int songIndex = position;

				// Starting new intent
				Intent intent = new Intent(context, YaampActivity.class);

				// Sending songIndex to PlayerActivity
				intent.putExtra("songIndex", songIndex);

				getActivity().setResult(RESULT_LIST, intent);
				// Closing PlayListView
				getActivity().finish();
			}
		});

		
		
	}
	
	
	
	

}
