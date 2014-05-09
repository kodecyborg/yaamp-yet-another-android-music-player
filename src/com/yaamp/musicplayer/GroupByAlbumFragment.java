package com.yaamp.musicplayer;

import java.util.ArrayList;

import com.yaamp.musicplayer.SongData.Music;
import com.yaamp.musicplayer.SongData.MusicDB;
import com.yaamp.musicplayer.adapters.AlbumsGridViewAdapter;
import com.yaamp.musicplayer.adapters.SimpleMusicListAdapter;

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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class GroupByAlbumFragment extends Fragment {

	private Context context;
	private ListView albumMusicList;
	private final int ALBUM_RESULT = 6;
	private TextView albumNameTxt;
	private GridView albumGridView;
	private Bitmap albumCover;
	private MusicDB mdb;
	private ArrayList<Music> groups = new ArrayList<Music>();
	private ArrayList<Music> albumSongs = new ArrayList<Music>();
	public static final String SAVED_STATE_ACTION_BAR_HIDDEN = "hidden_state";
	private int albumPosition = -1;
	SimpleMusicListAdapter albumMusicListAdapter;
	private ImageView albumImage;
	String albumName;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		if (savedInstanceState != null) {
			albumPosition=savedInstanceState.getInt("position");
			getMusicsFromAlbum(albumPosition);
			
			
		}
	}


	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		setRetainInstance(true);
		super.onCreate(savedInstanceState);
	}



	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		context = container.getContext();

		View rootView = inflater.inflate(R.layout.fragment_albums, container,
				false);

		mdb = new MusicDB(context);
		groups = mdb.getAlbums();
		albumMusicList = (ListView) rootView.findViewById(R.id.albumMusicList);
		albumGridView = (GridView) rootView.findViewById(R.id.albumGridView);
		albumNameTxt = (TextView) rootView.findViewById(R.id.albumName);
		albumImage = (ImageView) rootView.findViewById(R.id.albumImage);

	
			if(albumPosition!=-1)
			{
				getMusicsFromAlbum(albumPosition);

			}
		
		AlbumsGridViewAdapter adapter = new AlbumsGridViewAdapter(context,
				R.layout.grid_single_album, groups);

		albumGridView.setAdapter(adapter);

		albumMusicList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Intent intent = new Intent(context, YaampActivity.class);
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
				albumPosition = position;
				getMusicsFromAlbum(position);

			}
		});

		return rootView;

	}

	private void getMusicsFromAlbum(int position) {
		albumName = groups.get(position).getAlbumName();
		albumCover = SongsManager.getAlbumCover(groups.get(position));
		albumSongs = mdb.getMusicsByColumnNameValue(mdb.KEY_ALBUM_NAME,
				albumName);

		albumImage.setImageBitmap(albumCover);

		albumNameTxt.setText(albumName);

		albumMusicListAdapter = new SimpleMusicListAdapter(context,
				R.layout.simple_music_item, albumSongs);
		albumMusicList.setAdapter(albumMusicListAdapter);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		outState.putInt("position", albumPosition);

	}

}
