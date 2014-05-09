package com.yaamp.musicplayer;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SearchView;

import com.yaamp.YaampUtilities.PlayerControl;
import com.yaamp.musicplayer.SongData.Music;
import com.yaamp.musicplayer.SongData.MusicDB;
import com.yaamp.musicplayer.adapters.MusicListAdapter;

public class SearchFragment extends ListFragment implements SearchView.OnQueryTextListener,
SearchView.OnCloseListener{
	// Songs list
		Context context;
		SearchView searchView;
		MusicListAdapter adapter;
		PlayerControl playerControl=PlayerControl.getInstance();
		private final int SEARCH_RESULT=4;
			public ArrayList<Music> songsList = new ArrayList<Music>();
			
			private MusicDB mdb;
			
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.context=container.getContext();
		mdb=new MusicDB(context);
		View rootView = inflater.inflate(R.layout.fragment_search, container, false);
		
		searchView=(SearchView) rootView.findViewById(R.id.musicSearch);
		searchView.setIconifiedByDefault(false);
		 
        searchView.setOnQueryTextListener(this);
        searchView.setOnCloseListener(this);
		

		
		
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {

		super.onActivityCreated(savedInstanceState);
		
		
		

			
			
		ListView lv = getListView();
	
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, 
					View view,
					int position, 
					long id) {
				Intent intent=new Intent(context,YaampActivity.class);				
				intent.putExtra("musicFromSearch",songsList.get(position));
				getActivity().setResult(SEARCH_RESULT,intent);
				getActivity().finish();
			}
		});



		
	}

	@Override
	public boolean onClose() {
		
		setListAdapter(adapter);
		return false;
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		
		return false;
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		if(newText.length()!=0)
		{
		songsList=mdb.searchMusics(newText);
		adapter=new MusicListAdapter(context,R.layout.album_music_item, songsList);
		setListAdapter(adapter);
		}
		else{
			songsList.clear();
		adapter=new MusicListAdapter(context,R.layout.album_music_item, songsList);
		setListAdapter(adapter);}
		return false;
	}
}
