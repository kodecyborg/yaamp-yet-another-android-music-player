/**
 * 
 */
package com.yaamp.musicplayer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.yaamp.musicplayer.SongData.Music;
import com.yaamp.musicplayer.SongData.MusicDB;
import com.yaamp.musicplayer.SongData.MusicDataCacher;
import com.yaamp.musicplayer.SongData.SharedPreferenceManager;
import com.yaamp.musicplayer.YaampUtilities.PlayerControl;
import com.yaamp.musicplayer.YaampUtilities.TimerControl;
import com.yaamp.musicplayer.YaampUtilities.YaampHelper;
import com.yaamp.musicplayer.sensormanager.ShakeDetector;
import com.yaamp.musicplayer.sensormanager.ShakeDetector.OnShakeListener;
import com.yaamp.musicplayer.sensormanager.SimpleGestureFilter;
import com.yaamp.musicplayer.sensormanager.SimpleGestureFilter.SimpleGestureListener;

/**
 * @author Zaki
 * 
 */
public class YaampActivity extends FragmentActivity 
implements
		OnCompletionListener, 
		SeekBar.OnSeekBarChangeListener,
		SimpleGestureListener {

	private ImageButton youtubeBtn;
	private ImageButton btnPlay;
	private ImageButton btnForward;
	private ImageButton btnBackward;
	private ImageButton btnNext;
	private ImageButton btnPrevious;
	private ImageButton btnPlaylist;
	private ImageButton btnRepeat;
	private ImageButton btnShuffle;
	private ImageView andPic;
	private SeekBar songProgressBar;
	private TextView songTitleLabel;
	private TextView songCurrentDurationLabel;
	private TextView songTotalDurationLabel;
	// Media Player
	public  MediaPlayer mediaPlayer;
	// Handler to update UI timer, progress bar etc,.
	private Handler mHandler = new Handler();;
	private TimerControl utils;
	private int seekForwardTime = 5000; // 5000 milliseconds
	private int seekBackwardTime = 5000; // 5000 milliseconds
	private int currentSongIndex = 0;
	private boolean isShuffle = false;
	private boolean isRepeat = false;

	private ArrayList<Music> musicList = new ArrayList<Music>();
	private MusicDB musicDB=new MusicDB(this);
	private ArrayList<Music> allMusics=new ArrayList<Music>();
			
	// The following are used for the shake detection
	private SensorManager mSensorManager;
	private Sensor mAccelerometer;
	private ShakeDetector mShakeDetector;
	private SimpleGestureFilter mSimpleGestureFilter;
	private boolean isGestureEnabled = true;
	
	private final int RESULT_SETTINGS = 1;	
	private final int EQUO_SETTINGS = 2;
	private final int RESULT_LIST = 3;
	private final int RESULT_SEARCH=4;
	private final int BY_ARTIST_RESULT=5;
	private final int ALBUM_RESULT=6;
	private SharedPreferenceManager sharedPrefManager;
	private String albumName = "";
	private String artistName = "";
	private String title = "";
	private int songIndex = 0;
	private int progress=0;
	private PlayerControl playerControl=PlayerControl.getInstance();
	private Music currentMusic;
	
	// Detect touched area
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.player);
	
		
		try {
			/*	To be implemented
			if(getIntent().getAction().equalsIgnoreCase(Intent.ACTION_VIEW))
			{
				Toast.makeText(this, getIntent().getData().getPath()+"", Toast.LENGTH_LONG).show();

			}
		*/	
			init();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		// Register the Session Manager Listener
		mSensorManager.registerListener(mShakeDetector, mAccelerometer,
				SensorManager.SENSOR_DELAY_UI);
	}


	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (resultCode) {
		
		
		
		case RESULT_SETTINGS:
		
			showUserSettings();		
			break;
		

		case RESULT_LIST:
			
			if (data != null) 
			{
				currentSongIndex = data.getExtras().getInt("songIndex");	
				
				musicList=allMusics;
		      	
				playMusic(musicList.get(currentSongIndex));
			}
			break;
			
		case RESULT_SEARCH:
			if (data != null) 
			{
				Music music=(Music)data.getParcelableExtra("musicFromSearch");
				musicList.clear();
				musicList.add(music);
				playMusic(music);
			}
			break;
			
			
		case BY_ARTIST_RESULT:
			if(data!=null)
			{
				
				Music music=(Music)data.getParcelableExtra("musicByArtist");
				ArrayList<Music> artistSongs=data.getParcelableArrayListExtra("artistSongs");
				musicList=artistSongs;
				playMusic(music);
			}
			break;
			
		case ALBUM_RESULT:
			if(data!=null)
			{
				String albumName=data.getStringExtra("albumName");
				int songIndex=data.getExtras().getInt("songIndex");				
				musicList=musicDB.getMusicsByColumnNameValue(musicDB.KEY_ALBUM_NAME, albumName);
				playMusic(musicList.get(songIndex));
			}
			break;
		
		}
		
		YaampHelper.cacheCurrentPlayList(this, musicList);
		
		

	}

	public void setPlayerView(Music music)
	{
		Bitmap bm=YaampHelper.getAlbumCover(music);
		if ( bm!= null){
			andPic.setImageBitmap(bm);
		}
		else
		andPic.setImageDrawable(getResources().getDrawable(R.drawable.no_music));
		albumName = music.getAlbumName();
		artistName = music.getArtistName();
		title = music.getSongTitle();

		songTitleLabel.setText("Title: " + title + " - Artist: "
				+ artistName + " - Album: " + albumName);

		
		

		// set Progress bar values
		songProgressBar.setProgress(0);
		songProgressBar.setMax(100);

		// Updating progress bar
		
		
	}
	
	


	/**
	 * Update timer on seekbar
	 * */
	public void updateProgressBar() {
		mHandler.postDelayed(mUpdateTimeTask, 100);
		
	}

	
	
	/**
	 * 
	 * */
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromTouch) {

	}

	/**
	 * When user starts moving the progress handler
	 * */
	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// remove message Handler from updating progress bar
		mHandler.removeCallbacks(mUpdateTimeTask);
	}

	/**
	 * When user stops moving the progress hanlder
	 * */
	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		mHandler.removeCallbacks(mUpdateTimeTask);
		int totalDuration = mediaPlayer.getDuration();
		int currentPosition = utils.progressToTimer(seekBar.getProgress(),
				totalDuration);

		// forward or backward to certain seconds
		mediaPlayer.seekTo(currentPosition);

		// update timer progress again
		updateProgressBar();
	}

	/**
	 * On Song Playing completed if repeat is ON play same song again if shuffle
	 * is ON play random song
	 * */
	@Override
	public void onCompletion(MediaPlayer arg0) {

		// check for repeat is ON or OFF
		if (isRepeat) {
			// repeat is on play same song again
			playMusic(musicList.get(currentSongIndex));
		} else if (isShuffle) {
			// shuffle is on - play a random song
			Random rand = new Random();
			currentSongIndex = rand.nextInt((musicList.size() - 1)+ 1);
			playMusic(musicList.get(currentSongIndex));

		} else {
			// no repeat or shuffle ON - play next song
			if (currentSongIndex < (musicList.size() - 1)) {
			
				playMusic(musicList.get(currentSongIndex+1));
				currentSongIndex = currentSongIndex + 1;
			} else {
				// play first song
				playMusic(musicList.get(0));
				currentSongIndex = 0;
			}
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mediaPlayer.release();
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent me) {
		// Call onTouchEvent of SimpleGestureFilter class
		this.mSimpleGestureFilter.onTouchEvent(me);
		return super.dispatchTouchEvent(me);
	}

	@Override
	public void onSwipe(int direction) {
		int currentPosition = mediaPlayer.getCurrentPosition();

		if(isGestureEnabled) {
			switch (direction) {

			case SimpleGestureFilter.SWIPE_RIGHT:

				if (currentSongIndex > 0) {
					playMusic(musicList.get(currentSongIndex - 1));
					currentSongIndex = currentSongIndex - 1;
				} else {
					// play last song
					playMusic(musicList.get(musicList.size() - 1));

					currentSongIndex = musicList.size() - 1;
				}

				break;
			case SimpleGestureFilter.SWIPE_LEFT:
				// check if next song is there or not
				if (currentSongIndex < (musicList.size() - 1)) {
					playMusic(musicList.get(currentSongIndex + 1));

					currentSongIndex = currentSongIndex + 1;
				} else {
					// play first song
					playMusic(musicList.get(0));
					currentSongIndex = 0;
				}

				break;
			case SimpleGestureFilter.SWIPE_DOWN:
				// get current song position
				// check if seekBackward time is greater than 0 sec
				if (currentPosition - seekBackwardTime >= 0) {
					// forward song
					mediaPlayer.seekTo(currentPosition - seekBackwardTime);
				} else {
					// backward to starting position
					mediaPlayer.seekTo(0);
				}

				break;
			case SimpleGestureFilter.SWIPE_UP:
				
				
				
				// get current song position
				// check if seekForward time is lesser than song duration
				if (currentPosition + seekForwardTime <= mediaPlayer.getDuration()) {
					// forward song
					mediaPlayer.seekTo(currentPosition + seekForwardTime);
				} else {
					// forward to end position
					mediaPlayer.seekTo(mediaPlayer.getDuration());
				}
			
				
				
				
				break;

			}
		}

	}

	@Override
	public void onDoubleTap() {
		if (isGestureEnabled) {
			// check for already playing			
			if (mediaPlayer.isPlaying()) {
				// Pause song			
					playerControl.pause();
					btnPlay.setImageResource(R.drawable.btn_play);
					Toast.makeText(this, "Paused", Toast.LENGTH_SHORT).show();

				
			} else {
				// Resume song
					playerControl.play();
					// Changing button image to pause button
					btnPlay.setImageResource(R.drawable.btn_pause);
					Toast.makeText(this, "Playing", Toast.LENGTH_SHORT).show();

				
			}
		}
	}

	private void showUserSettings() {
		SharedPreferences sharedPrefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		isGestureEnabled = sharedPrefs.getBoolean("gestureControl", false);
	}

	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case R.id.menu_settings:
			Intent i = new Intent(this, Preference.class);
			startActivityForResult(i, RESULT_SETTINGS);
			break;
		case R.id.equo_settings:
			Intent i2 = new Intent(this, EquoActivity.class);
			startActivityForResult(i2, EQUO_SETTINGS);
			break;

		case R.id.music_details:

			try {
				showDialog();
			} catch (IndexOutOfBoundsException e) {
				Toast.makeText(this, "Select a music fist", Toast.LENGTH_SHORT).show();;
				e.printStackTrace();
			}

			break;
			
		case R.id.scan_musics:
			
			YaampHelper.scanLibrary(this, YaampHelper.MEDIA_PATH);
		
			break;
		}

		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}

	private void init() throws IOException, ClassNotFoundException {

		youtubeBtn = (ImageButton) findViewById(R.id.youtubeBtn);
		btnPlay = (ImageButton) findViewById(R.id.btnPlay);
		btnForward = (ImageButton) findViewById(R.id.btnForward);
		btnBackward = (ImageButton) findViewById(R.id.btnBackward);
		btnNext = (ImageButton) findViewById(R.id.btnNext);
		btnPrevious = (ImageButton) findViewById(R.id.btnPrevious);
		btnPlaylist = (ImageButton) findViewById(R.id.btnPlaylist);
		btnRepeat = (ImageButton) findViewById(R.id.btnRepeat);
		btnShuffle = (ImageButton) findViewById(R.id.btnShuffle);
		songProgressBar = (SeekBar) findViewById(R.id.songProgressBar);
		songTitleLabel = (TextView) findViewById(R.id.songTitle);
		songCurrentDurationLabel = (TextView) findViewById(R.id.songCurrentDurationLabel);
		songTotalDurationLabel = (TextView) findViewById(R.id.songTotalDurationLabel);
		andPic = (ImageView) findViewById(R.id.andPic);
		// ShakeDetector initialization
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mAccelerometer = mSensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mShakeDetector = new ShakeDetector();
		
		
		mSimpleGestureFilter = new SimpleGestureFilter(this, this);
		playerControl=PlayerControl.getInstance();
	    mediaPlayer = playerControl.getMediaPlayer();

		utils = new TimerControl();
		showUserSettings();

		// Listeners
		songProgressBar.setOnSeekBarChangeListener(this); // Important
		mediaPlayer.setOnCompletionListener(this); // Important
		
		loadLastState();
	   
		btnPlay.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// check for already playing
				
				if (mediaPlayer.isPlaying()) {
					if (mediaPlayer != null) {
						mediaPlayer.pause();
						// Changing button image to play button
						btnPlay.setImageResource(R.drawable.btn_play);
					}
				} else {
					// Resume song
					if (mediaPlayer != null) {
						mediaPlayer.start();
						// Changing button image to pause button
						btnPlay.setImageResource(R.drawable.btn_pause);
					}
				}

			}
		});

		/**
		 * Listeners registration Forwards song specified seconds
		 * */
		youtubeBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					Intent intent = new Intent(Intent.ACTION_SEARCH);
					intent.setPackage("com.google.android.youtube");
					intent.putExtra("query", artistName + " " + title);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), "Problem launching GOOGLE youtube app", Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				}

			}
		});
		mShakeDetector.setOnShakeListener(new OnShakeListener() {

			@Override
			public void onShake(int count) {

				// check if next song is there or not
				if (isGestureEnabled) {
					Random rand = new Random();
					currentSongIndex = rand.nextInt((musicList.size() - 1) - 0 + 1) + 0;
					playMusic(musicList.get(currentSongIndex));
					Toast.makeText(getApplicationContext(), "Random",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		btnForward.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// get current song position
				int currentPosition = mediaPlayer.getCurrentPosition();
				// check if seekForward time is lesser than song duration
				if (currentPosition + seekForwardTime <= mediaPlayer.getDuration()) {
					// forward song
					mediaPlayer.seekTo(currentPosition + seekForwardTime);
				} else {
					// forward to end position
					mediaPlayer.seekTo(mediaPlayer.getDuration());
				}
			}
		});

		/**
		 * Backward button click event Backward song to specified seconds
		 * */
		btnBackward.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// get current song position
				int currentPosition = mediaPlayer.getCurrentPosition();
				// check if seekBackward time is greater than 0 sec
				if (currentPosition - seekBackwardTime >= 0) {
					// forward song
					mediaPlayer.seekTo(currentPosition - seekBackwardTime);
				} else {
					// backward to starting position
					mediaPlayer.seekTo(0);
				}

			}
		});

		/**
		 * Next button click event Plays next song by taking currentSongIndex +
		 * 1
		 * */
		btnNext.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// check if next song is there or not
				if (currentSongIndex < (musicList.size() - 1)) {
					playMusic(musicList.get(currentSongIndex + 1));

					currentSongIndex = currentSongIndex + 1;
				} else {
					// play first song
				
				playMusic(musicList.get(0));
					currentSongIndex = 0;
				}

			}
		});

		/**
		 * Back button click event Plays previous song by currentSongIndex - 1
		 * */
		btnPrevious.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (currentSongIndex > 0) {
					
					playMusic(musicList.get(currentSongIndex - 1));
					currentSongIndex = currentSongIndex - 1;
				} else {
					// play last song
					playMusic(musicList.get(musicList.size() - 1));
					currentSongIndex = musicList.size() - 1;
				}

			}
		});

		/**
		 * Button Click event for Repeat button Enables repeat flag to true
		 * */
		btnRepeat.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (isRepeat) {
					isRepeat = false;
					Toast.makeText(getApplicationContext(), "Repeat is OFF",
							Toast.LENGTH_SHORT).show();
					btnRepeat.setImageResource(R.drawable.btn_repeat);
				} else {
					// make repeat to true
					isRepeat = true;
					Toast.makeText(getApplicationContext(), "Repeat is ON",
							Toast.LENGTH_SHORT).show();
					
					btnRepeat.setImageResource(R.drawable.btn_repeat_focused);
				}
			}
		});

		/**
		 * Button Click event for Shuffle button Enables shuffle flag to true
		 * */
		btnShuffle.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (isShuffle) {
					isShuffle = false;
				
					btnShuffle.setImageResource(R.drawable.btn_shuffle);
					Toast.makeText(getApplicationContext(), "Shuffle is OFF",
							Toast.LENGTH_SHORT).show();
				} else {
					// make repeat to true
					isShuffle = true;				
					// make shuffle to false
					isRepeat = true;
					
					btnShuffle.setImageResource(R.drawable.btn_shuffle_focused);
					btnRepeat.setImageResource(R.drawable.btn_repeat_focused);
					
					Toast.makeText(getApplicationContext(), "Shuffle is ON",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		/**
		 * Button Click event for Play list click event Launches list activity
		 * which displays list of songs
		 * */
		btnPlaylist.setOnClickListener(new View.OnClickListener() {

			
			@Override
			public void onClick(View arg0) {
				
				Intent i = new Intent(getApplicationContext(),TabActivity.class);
				
				startActivityForResult(i, 100);
			}
		});

	}

	private void showDialog() {
		
		
		
		String[] val=YaampHelper.Metadata.getMetadataStringArray(currentMusic);

		ListAdapter itemsAdapter = new ArrayAdapter<String>(
				this.getApplicationContext(),
				android.R.layout.simple_list_item_1, val);

		AlertDialog.Builder detailsDialogBuilder = new AlertDialog.Builder(this);
		// Add the button

		detailsDialogBuilder.setAdapter(itemsAdapter, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				//Just dismiss

			}
		});

		detailsDialogBuilder.setView(getLayoutInflater().inflate(R.layout.details, null))
				.setNegativeButton("Dismiss",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								//Just dismiss the dialog
							}
						});

		AlertDialog alertDialog = detailsDialogBuilder.create();
		alertDialog.show();

	}
	
	@SuppressWarnings("unchecked")
	private void loadLastState(){
		
		YaampHelper.createDatabase(this,YaampHelper.MEDIA_PATH);
		 sharedPrefManager=new SharedPreferenceManager(this);

	    try {

		    allMusics=(ArrayList<Music>) MusicDataCacher.readObject(this,MusicDataCacher.KEY_ALL_MUSICS);

		    musicList=(ArrayList<Music>) MusicDataCacher.readObject(this, MusicDataCacher.KEY_CURRENT_PLAYLIST);
			
			
		    currentSongIndex=sharedPrefManager.getLastPlaylistIndex();
			songIndex=currentSongIndex;

			setPlayerView(musicList.get(songIndex));
			
			playMusic(musicList.get(songIndex));;
			mediaPlayer.pause();
	    } catch (Exception e) {

			e.printStackTrace();
		}
	}

	@Override
	public void onPause() {
		// Unregister the Sensor Manager onPause
		mSensorManager.unregisterListener(mShakeDetector);
		 sharedPrefManager=new SharedPreferenceManager(this);

			sharedPrefManager.setLastPlaylistIndex(currentSongIndex);
		
		super.onPause();
	}

	
	/**---------------------------------------------------------
	 * Background Runnable thread
	 * */
	private Runnable mUpdateTimeTask = new Runnable() {
		@Override
		public void run() {

			try {
				long totalDuration = mediaPlayer.getDuration();
				long currentDuration = mediaPlayer.getCurrentPosition();

				// Displaying Total Duration time
				songTotalDurationLabel.setText(""
						+ utils.milliSecondsToTimer(totalDuration));
				// Displaying time completed playing
				songCurrentDurationLabel.setText(""
						+ utils.milliSecondsToTimer(currentDuration));

				// Updating progress bar
				 progress = (utils.getProgressPercentage(
						currentDuration, totalDuration));
				// Log.d("Progress", ""+progress);
				songProgressBar.setProgress(progress);

				// Running this thread after 100 milliseconds
				mHandler.postDelayed(this, 100);
				
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	};
	
	//Play music and update UI
		public void playMusic(Music music) {
		
			try {
					this.currentMusic=music;
					playerControl.playMusic(music);		
					setPlayerView(music);
					btnPlay.setImageResource(R.drawable.btn_pause);

					updateProgressBar();
				
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} 
		}







	

}

   
		
	
	

