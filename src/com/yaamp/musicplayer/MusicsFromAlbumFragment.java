package com.yaamp.musicplayer;


import java.util.ArrayList;

import com.yaamp.musicplayer.SongData.Music;
import com.yaamp.musicplayer.SongData.MusicDB;
import com.yaamp.musicplayer.adapters.MusicListAdapter;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MusicsFromAlbumFragment extends FragmentActivity {
	
	
	
	static Intent intent;
	private final int ALBUM_RESULT=6;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.all_music_item);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
			
		}
		
		intent=getIntent();
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.album_musics, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		TextView textView;
		ImageView albumView;
		ListView albumMusicList;
		ImageView btnPlay;
		ArrayList<Music> albumSongs=new ArrayList<Music>();
		String albumName="";
		PlayerControl playerControl;
		final int ALBUM_RESULT=6;
		private Context context;
		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.music_from_album_fragment,
					container, false);
			this.context=container.getContext();
			MusicDB mdb=new MusicDB(context);
			btnPlay=(ImageView)rootView.findViewById(R.id.btnPlay);
			textView=(TextView) rootView.findViewById(R.id.albumNameText);
			albumView=(ImageView) rootView.findViewById(R.id.andPic);
			playerControl=PlayerControl.getInstance();
			albumName=intent.getExtras().getString("albumName");
			
			albumSongs=mdb.getMusicsByColumnNameValue(mdb.KEY_ALBUM_NAME, albumName);			
			albumMusicList=(ListView)rootView.findViewById(R.id.albumMusicList);			
			textView.setText(albumName.toString());
			
			MusicListAdapter adapter=new MusicListAdapter(getActivity(), R.layout.album_music_item, albumSongs);
			albumMusicList.setAdapter(adapter);
			albumMusicList.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
				Intent intent=new Intent(context,YaampActivity.class);		
				intent.putExtra("musicFromAlbum",albumSongs.get(position));
				intent.putExtra("albumSongs", albumSongs);
				getActivity().setResult(ALBUM_RESULT,intent);
				//getActivity().finish();
				
				}
			});
			return rootView;
		}
		

	}

}
