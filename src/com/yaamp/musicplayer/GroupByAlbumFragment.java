package com.yaamp.musicplayer;

import java.util.ArrayList;

import com.yaamp.musicplayer.SongData.Music;
import com.yaamp.musicplayer.SongData.MusicDB;
import com.yaamp.musicplayer.adapters.AlbumsGridViewAdapter;
import com.yaamp.musicplayer.adapters.MusicListAdapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SlidingDrawer;

public class GroupByAlbumFragment extends Fragment {

	private Context context;
	private ListView albumMusicList;
	private final int ALBUM_RESULT = 6;
	private Button albumNameButton;
	private GridView albumGridView;
	private Bitmap albumCover;
	private MusicDB mdb;
	private ArrayList<Music> groups = new ArrayList<Music>();
	private ArrayList<Music> albumSongs = new ArrayList<Music>();
	
	MusicListAdapter albumMusicListAdapter;
	private ImageView albumImage;
	String albumName;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		context = container.getContext();
		View rootView = inflater.inflate(R.layout.fragment_albums, container,
				false);
		
		mdb = new MusicDB(context);
		getAlbums(context);
		albumMusicList =(ListView)rootView.findViewById(R.id.albumMusicList);	
		albumGridView = (GridView) rootView.findViewById(R.id.albumGridView);
		albumNameButton = (Button) rootView.findViewById(R.id.albumName);
		albumImage=(ImageView)rootView.findViewById(R.id.albumImage);
		
		AlbumsGridViewAdapter adapter = new AlbumsGridViewAdapter(context,
				R.layout.grid_single_album, groups);

		

		albumGridView.setAdapter(adapter);

		albumMusicList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
	           
				Intent intent = new Intent(context,YaampActivity.class);				
				intent.putExtra("albumName", albumName);
				intent.putExtra("songIndex", position);
				getActivity().setResult(ALBUM_RESULT, intent);
				getActivity().finish();
		
				
			}
		});
		
		albumGridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position,
					long arg3) {
			 albumName=groups.get(position).getAlbumName();
				albumCover=SongsManager.getAlbumCover(groups.get(position));
				albumSongs=mdb.getMusicsByColumnNameValue(mdb.KEY_ALBUM_NAME,albumName );
				
				albumImage.setImageBitmap(albumCover);
				
				albumNameButton.setText(albumName);
			
				albumMusicListAdapter = new MusicListAdapter(context,
						R.layout.album_music_item, albumSongs);
				albumMusicList.setAdapter(albumMusicListAdapter);
				
			}
		});

		return rootView;

	}

	private void getAlbums(Context context) {

		
		groups.clear();
		ArrayList<Music> musicArrayList = null;

		for (int i = 1; i < mdb.getMusicsByAlbum().size(); i++) {
			musicArrayList = mdb.getMusicsByAlbum().get(i).iterator().next();
			if (null != mdb.getMusicsByAlbum().get(i)) {
				Music music = musicArrayList.iterator().next();
				groups.add(i - 1, music);

			}
		}

	}

}
