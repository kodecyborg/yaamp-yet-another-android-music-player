package com.yaamp.musicplayer;

import java.util.ArrayList;

import com.yaamp.musicplayer.SongData.Music;
import com.yaamp.musicplayer.SongData.MusicDB;
import com.yaamp.musicplayer.adapters.MusicListAdapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class MusicsFromAlbumFragment extends FragmentActivity {

	private final int ALBUM_RESULT = 6;
	ListView albumMusicList;
	ArrayList<Music> albumSongs = new ArrayList<Music>();
	String albumName = "";
	MusicDB mdb;
	TextView textView;

	@Override
	public View onCreateView(String name, Context context, AttributeSet attrs) {
			
		mdb = new MusicDB(context);

		return super.onCreateView(name, context, attrs);

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		
		
		textView=(TextView)findViewById(R.id.albumNameText);
		setContentView(R.layout.music_from_album_fragment);
		albumMusicList=(ListView)findViewById(R.id.albumMusicList);	
		albumName = getIntent().getExtras().getString("albumName");
		albumSongs=mdb.getMusicsByColumnNameValue(mdb.KEY_ALBUM_NAME, albumName);

		MusicListAdapter adapter=new MusicListAdapter(
				getApplicationContext(), 
				R.layout.album_music_item, 
				albumSongs);

		albumMusicList.setAdapter(adapter);
		
		albumMusicList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent,
					View view,
					int position, 
					long id) {
				
				Intent intent = new Intent(getApplicationContext(),YaampActivity.class);
				
				intent.putExtra("albumName", albumName);
				intent.putExtra("songIndex", position);
				setResult(ALBUM_RESULT, intent);
				startActivity(intent);
		
			}
		});

	}

}
