package com.yaamp.musicplayer;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.yaamp.musicplayer.R;
import com.yaamp.musicplayer.SongData.Music;
import com.yaamp.musicplayer.SongData.MusicDB;
import com.yaamp.musicplayer.adapters.ArtistExpandableListViewAdapter;
import com.yaamp.musicplayer.adapters.MusicListAdapter;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class GroupByArtistFragment extends Fragment {
	// Songs list
		Context context;
		HashMap<String, ArrayList<Music>>  songsList=new HashMap<String, ArrayList<Music>>();
		ArrayList<String> header;	
		ExpandableListView expandableListView;
		PlayerControl playerControl=PlayerControl.getInstance();
		private final int BY_ARTIST_RESULT=5;
			private MusicDB mdb;
			
			
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			
			this.context=container.getContext();
			mdb=new MusicDB(context);
			View rootView = inflater.inflate(R.layout.group_by_artist_fragment, container, false);
			expandableListView=(ExpandableListView) rootView.findViewById(R.id.lvExp);

	
		
			return rootView;
		}
		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onActivityCreated(savedInstanceState);
			SongsManager plm = new SongsManager();
			
			plm.execute();

				songsList=mdb.getMusicsByArtist();
				header=new ArrayList<String>(songsList.keySet());
				ArtistExpandableListViewAdapter adapter=
						new ArtistExpandableListViewAdapter(
								context,
								songsList,
								R.layout.artist_item_header,
								R.layout.artist_item_children);
			    //Adding menuItems to ListView
			      

			expandableListView.setAdapter(adapter);
			
			
		expandableListView.setOnChildClickListener(new OnChildClickListener() {
				
				@Override
				public boolean onChildClick(ExpandableListView parent, View v,
						int groupPosition, int childPosition, long id) {
					ArrayList<Music> artistSongs=songsList.get(header.get(groupPosition));
					Music music=artistSongs.get(childPosition);
							
					Intent intent=new Intent(context,YaampActivity.class);				
					intent.putExtra("musicByArtist",music);
					intent.putExtra("artistSongs",artistSongs);
					getActivity().setResult(BY_ARTIST_RESULT,intent);
					getActivity().finish();
					playerControl.playMusic(music);
					
										return false;
				}
			});
			
		

			
		}
		
}
