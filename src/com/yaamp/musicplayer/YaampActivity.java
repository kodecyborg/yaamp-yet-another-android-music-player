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
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
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
import com.yaamp.musicplayer.sensormanager.ShakeDetector;
import com.yaamp.musicplayer.sensormanager.ShakeDetector.OnShakeListener;
import com.yaamp.musicplayer.sensormanager.SimpleGestureFilter;
import com.yaamp.musicplayer.sensormanager.SimpleGestureFilter.SimpleGestureListener;

/**
 * @author unbounded
 * 
 */
public class YaampActivity extends FragmentActivity implements
		OnCompletionListener, SeekBar.OnSeekBarChangeListener,
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
	public  MediaPlayer mp;
	// Handler to update UI timer, progress bar etc,.
	private Handler mHandler = new Handler();;
	private Utilities utils;
	private int seekForwardTime = 5000; // 5000 milliseconds
	private int seekBackwardTime = 5000; // 5000 milliseconds
	private int currentSongIndex = 0;
	private boolean isShuffle = false;
	private boolean isRepeat = false;
	
	private ArrayList<Music> musicList = new ArrayList<Music>();
	private MusicDB musicDB=new MusicDB(this);
	
	final String MEDIA_PATH = Environment.getExternalStorageDirectory()
			.getPath();
	// The following are used for the shake detection
	private SensorManager mSensorManager;
	private Sensor mAccelerometer;
	private ShakeDetector mShakeDetector;
	private SimpleGestureFilter detector;
	private boolean isGestureEnabled = true;
	
	private final int RESULT_SETTINGS = 1;	
	private final int EQUO_SETTINGS = 2;
	private final int RESULT_LIST = 3;
	private final int RESULT_SEARCH=4;
	private final int BY_ARTIST_RESULT=5;
	private final int ALBUM_RESULT=6;
	
	private String albumName = "";
	private String artistName = "";
	private String title = "";
	private int songIndex = 0;

	private PlayerControl playerControl=PlayerControl.getInstance();
	
	
	// Detect touched area
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.player);
		
		try {
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
	public void onPause() {
		// Unregister the Sensor Manager onPause
		mSensorManager.unregisterListener(mShakeDetector);
		super.onPause();
	}

	/**
	 * Receiving song index from playlist view and play the song
	 * Get your extras from data
	 * */
	
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
				playSong(currentSongIndex);
			}
			break;
			
		case RESULT_SEARCH:
			if (data != null) 
			{
				Music music=(Music)data.getParcelableExtra("musicFromSearch");
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
				
				Music music=(Music)data.getParcelableExtra("musicFromAlbum");
				ArrayList<Music> albumSongs=data.getParcelableArrayListExtra("albumSongs");
				musicList=albumSongs;
				playMusic(music);
			}
			break;
		
		}

	}

	public void playMusic(Music music) {
	
		try {
	
				playerControl.playMusic(music);		
				
				Bitmap bm=SongsManager.getAlbumCover(music);
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

				// Changing Button Image to pause image
				btnPlay.setImageResource(R.drawable.btn_pause);

				// set Progress bar values
				songProgressBar.setProgress(0);
				songProgressBar.setMax(100);

				// Updating progress bar
				updateProgressBar();
			
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} 
	}

	/**
	 * Function to play a song
	 * 
	 * @param songIndex
	 *            - index of song
	 * */
	public void playSong(int songIndex) {
		// Play song
		this.songIndex = songIndex;
		try {
			if (musicList.size() != 0) {
			
				Music music=musicList.get(songIndex);
				playerControl.playMusic(music);		
				
				Bitmap bm=SongsManager.getAlbumCover(music);
				if ( bm!= null){
					andPic.setImageBitmap(bm);
				}
				else
					andPic.setImageDrawable(getResources().getDrawable(R.drawable.no_music));

				
				

				albumName = music.getAlbumName();
				artistName = music.getArtistName();
				title = music.getSongTitle();

				if (albumName == null) {
					albumName = "Unknown album";
				}
				if (artistName == null) {
					artistName = "Unknown artist";
				}
				if (title == null) {
					title = "Unknown title";
				}
				songTitleLabel.setText("Title: " + title + " - Artist: "
						+ artistName + " - Album: " + albumName);

				// Changing Button Image to pause image
				btnPlay.setImageResource(R.drawable.btn_pause);

				// set Progress bar values
				songProgressBar.setProgress(0);
				songProgressBar.setMax(100);

				// Updating progress bar
				updateProgressBar();
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} 
	}

	/**
	 * Update timer on seekbar
	 * */
	public void updateProgressBar() {
		mHandler.postDelayed(mUpdateTimeTask, 100);
	}

	/**
	 * Background Runnable thread
	 * */
	private Runnable mUpdateTimeTask = new Runnable() {
		public void run() {

			try {
				long totalDuration = mp.getDuration();
				long currentDuration = mp.getCurrentPosition();

				// Displaying Total Duration time
				songTotalDurationLabel.setText(""
						+ utils.milliSecondsToTimer(totalDuration));
				// Displaying time completed playing
				songCurrentDurationLabel.setText(""
						+ utils.milliSecondsToTimer(currentDuration));

				// Updating progress bar
				int progress = (int) (utils.getProgressPercentage(
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
		int totalDuration = mp.getDuration();
		int currentPosition = utils.progressToTimer(seekBar.getProgress(),
				totalDuration);

		// forward or backward to certain seconds
		mp.seekTo(currentPosition);

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
			playSong(currentSongIndex);
		} else if (isShuffle) {
			// shuffle is on - play a random song
			Random rand = new Random();
			currentSongIndex = rand.nextInt((musicList.size() - 1) - 0 + 1) + 0;
			playSong(currentSongIndex);
		} else {
			// no repeat or shuffle ON - play next song
			if (currentSongIndex < (musicList.size() - 1)) {
				playSong(currentSongIndex + 1);
				currentSongIndex = currentSongIndex + 1;
			} else {
				// play first song
				playSong(0);
				currentSongIndex = 0;
			}
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mp.release();
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent me) {
		// Call onTouchEvent of SimpleGestureFilter class
		this.detector.onTouchEvent(me);
		return super.dispatchTouchEvent(me);
	}

	@Override
	public void onSwipe(int direction) {
		
		if(isGestureEnabled) {
			switch (direction) {

			case SimpleGestureFilter.SWIPE_RIGHT:

				if (currentSongIndex > 0) {
					playSong(currentSongIndex - 1);
					currentSongIndex = currentSongIndex - 1;
				} else {
					// play last song
					playSong(musicList.size() - 1);
					currentSongIndex = musicList.size() - 1;
				}

				break;
			case SimpleGestureFilter.SWIPE_LEFT:
				// check if next song is there or not
				if (currentSongIndex < (musicList.size() - 1)) {
					playSong(currentSongIndex + 1);
					currentSongIndex = currentSongIndex + 1;
				} else {
					// play first song
					playSong(0);
					currentSongIndex = 0;
				}

				break;
			case SimpleGestureFilter.SWIPE_DOWN:
				// str = "Swipe Down";
				break;
			case SimpleGestureFilter.SWIPE_UP:
				// str = "Swipe Up";
				break;

			}
		}

	}

	@Override
	public void onDoubleTap() {
		if (isGestureEnabled) {
			// check for already playing			
			if (mp.isPlaying()) {
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

			showDialog();

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

		
		
		// SharedPreferences sharedPrefs =
		// PreferenceManager.getDefaultSharedPreferences(this);

		showUserSettings();
		detector = new SimpleGestureFilter(this, this);
		// All player buttons

				playerControl=PlayerControl.getInstance();
				mp = playerControl.getMediaPlayer();

		utils = new Utilities();

		// Listeners
		songProgressBar.setOnSeekBarChangeListener(this); // Important
		mp.setOnCompletionListener(this); // Important
		
		//musicDB.dropTable();
		//musicDB.dropMusicDB();
		SongsManager.createDatabase(getApplicationContext(),MEDIA_PATH);
		//musicList=musicDB.getAllMusics();

		/**
		 * Uncomment for database test
		 * /	
	
	musicList=musicDB.getAllMusics();
			musicDB.dropTable();
			musicDB.dropMusicDB();
			
		
		
		
	
		/**
		 * Play button click event plays a song and changes button to pause
		 * image pauses a song and changes button to play image
		 * */
		
		btnPlay.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// check for already playing
				
				if (mp.isPlaying()) {
					if (mp != null) {
						mp.pause();
						// Changing button image to play button
						btnPlay.setImageResource(R.drawable.btn_play);
					}
				} else {
					// Resume song
					if (mp != null) {
						mp.start();
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
				Intent intent = new Intent(Intent.ACTION_SEARCH);
				intent.setPackage("com.google.android.youtube");
				intent.putExtra("query", artistName + " " + title);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);

			}
		});
		mShakeDetector.setOnShakeListener(new OnShakeListener() {

			@Override
			public void onShake(int count) {

				// check if next song is there or not
				if (isGestureEnabled) {
					Random rand = new Random();
					currentSongIndex = rand.nextInt((musicList.size() - 1) - 0 + 1) + 0;
					playSong(currentSongIndex);
					Toast.makeText(getApplicationContext(), "Random",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		btnForward.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// get current song position
				int currentPosition = mp.getCurrentPosition();
				// check if seekForward time is lesser than song duration
				if (currentPosition + seekForwardTime <= mp.getDuration()) {
					// forward song
					mp.seekTo(currentPosition + seekForwardTime);
				} else {
					// forward to end position
					mp.seekTo(mp.getDuration());
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
				int currentPosition = mp.getCurrentPosition();
				// check if seekBackward time is greater than 0 sec
				if (currentPosition - seekBackwardTime >= 0) {
					// forward song
					mp.seekTo(currentPosition - seekBackwardTime);
				} else {
					// backward to starting position
					mp.seekTo(0);
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
					playSong(currentSongIndex + 1);
					currentSongIndex = currentSongIndex + 1;
				} else {
					// play first song
					playSong(0);
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
					playSong(currentSongIndex - 1);
					currentSongIndex = currentSongIndex - 1;
				} else {
					// play last song
					playSong(musicList.size() - 1);
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
					// make shuffle to false
					isShuffle = false;
					btnRepeat.setImageResource(R.drawable.btn_repeat_focused);
					btnShuffle.setImageResource(R.drawable.btn_shuffle);
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
					Toast.makeText(getApplicationContext(), "Shuffle is OFF",
							Toast.LENGTH_SHORT).show();
					btnShuffle.setImageResource(R.drawable.btn_shuffle);
				} else {
					// make repeat to true
					isShuffle = true;
					Toast.makeText(getApplicationContext(), "Shuffle is ON",
							Toast.LENGTH_SHORT).show();
					// make shuffle to false
					isRepeat = false;
					btnShuffle.setImageResource(R.drawable.btn_shuffle_focused);
					btnRepeat.setImageResource(R.drawable.btn_repeat);
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
		String bitrateLong = "Unknown";
		LayoutInflater inflater = getLayoutInflater();
		String albumName = musicList.get(songIndex).getAlbumName();
		String artistName = musicList.get(songIndex).getArtistName();
		String year = musicList.get(songIndex).getYear();
		String title = musicList.get(songIndex).getSongTitle();
		String bitrate = musicList.get(songIndex).getBitRate();
		String duration = musicList.get(songIndex).getDuration();
		String trackNumber = musicList.get(songIndex).getTrackNumber();
		String author = musicList.get(songIndex).getAuthor();
		String mimeType = musicList.get(songIndex).getMimeType();

		if (bitrate != null)
			bitrateLong = Long.parseLong(bitrate) / 1000 + "";
		else
			bitrateLong = "Unknown";

		String[] values = {
				"Title: " + title,
				"Artist: " + artistName,
				"Album: " + albumName,
				"Year: " + year,
				"Track number: " + trackNumber,
				"Duration: "
						+ ID3TagHelper.DurationSplitter
								.getStringSeparatedDuration(duration),
				"Bitrate: " + bitrateLong + " kb/s", "Type: " + mimeType,
				"Author: " + author };

		ListAdapter itemsAdapter = new ArrayAdapter<String>(
				this.getApplicationContext(),
				android.R.layout.simple_list_item_1, values);
		// detailsListView.setAdapter(itemsAdapter);

		AlertDialog.Builder detailsDialogBuilder = new AlertDialog.Builder(this);
		// Add the buttons

		detailsDialogBuilder.setAdapter(itemsAdapter, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub

			}
		});

		detailsDialogBuilder.setView(inflater.inflate(R.layout.details, null))
				.setNegativeButton("Dismiss",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								//Just dismiss the dialog
							}
						});

		AlertDialog alertDialog = detailsDialogBuilder.create();
		alertDialog.show();

	}


}
