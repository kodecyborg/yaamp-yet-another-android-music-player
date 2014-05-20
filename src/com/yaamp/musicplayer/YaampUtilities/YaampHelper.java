package com.yaamp.musicplayer.YaampUtilities;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.yaamp.musicplayer.SongData.Music;
import com.yaamp.musicplayer.SongData.MusicDB;
import com.yaamp.musicplayer.SongData.MusicDataCacher;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.Environment;
import android.util.Log;

public class YaampHelper {
	// SDCard Path
	public static final String MEDIA_PATH = Environment
			.getExternalStorageDirectory().getPath();

	public static Bitmap getAlbumCover(Music music) {
		MediaMetadataRetriever mmr = new MediaMetadataRetriever();
		mmr.setDataSource(music.getSongPath());

		byte[] albumCover = mmr.getEmbeddedPicture();

		if (albumCover != null) {
			return BitmapFactory.decodeByteArray(albumCover, 0,
					albumCover.length);
		} else
			return null;

	}

	/**
	 * Function to read all mp3 files from sdcard and store the details in
	 * database
	 * */

	private static void createMusicDB(Context context, String filePath,
			MusicDB mdb) {

		File home = new File(filePath);
		File[] files = home.listFiles();
		MediaMetadataRetriever mmr = new MediaMetadataRetriever();

		for (File f : files) {
			if (f.isDirectory())
				createMusicDB(context, f.getPath(), mdb);
			else {

				if (f.getName().endsWith(".mp3")
						|| f.getName().endsWith(".MP3")
						|| f.getName().endsWith(".wma")
						|| f.getName().endsWith(".WMA")
						|| f.getName().endsWith(".m4a")
						
						|| f.getName().endsWith(".M4A")

				) {

					mmr.setDataSource(f.getPath());

					String albumName = mmr
							.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
					String artistName = mmr
							.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
					String year = mmr
							.extractMetadata(MediaMetadataRetriever.METADATA_KEY_YEAR);
					String title = mmr
							.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
					String bitRate = mmr
							.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE);
					String duration = mmr
							.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
					String trackNumber = mmr
							.extractMetadata(MediaMetadataRetriever.METADATA_KEY_CD_TRACK_NUMBER);
					String author = mmr
							.extractMetadata(MediaMetadataRetriever.METADATA_KEY_AUTHOR);
					String mimeType = mmr
							.extractMetadata(MediaMetadataRetriever.METADATA_KEY_MIMETYPE);
					String songFileName = f.getName().substring(0,
							(f.getName().length() - 4));
					String fileDirectory = f.getParent();
					String songPath = f.getPath();

					Music music = new Music(songPath, songFileName, title,
							fileDirectory, albumName, artistName, year,
							bitRate, duration, trackNumber, author, mimeType);

					mdb.addMusic(music);

				}
			}
		}
	}

	public static void scanLibrary(Context context, String filePath) {
		MusicDB mdb = new MusicDB(context);
		delecteCached(context);
		mdb.dropTable();
		createDatabase(context, filePath);
	}

	private static void cacheLibrary(Context context, MusicDB mdb) {
		try {
			MusicDataCacher.writeObject(context,
					MusicDataCacher.KEY_ALL_MUSICS, mdb.getAllMusics());

		} catch (IOException e) {
			Log.e("Yaamp Caching Error: ", "Error caching data with key "
					+ MusicDataCacher.KEY_ALL_MUSICS);
			e.printStackTrace();
		}

		try {
			MusicDataCacher.writeObject(context, MusicDataCacher.KEY_ALBUMS,
					mdb.getAlbums());
		} catch (IOException e) {
			Log.e("Yaamp Caching Error: ", "Error caching data with key "
					+ MusicDataCacher.KEY_ALBUMS);
			e.printStackTrace();
		}
	}

	public static void cacheCurrentPlayList(Context context,
			ArrayList<Music> musics) {
		try {
			MusicDataCacher.writeObject(context,
					MusicDataCacher.KEY_CURRENT_PLAYLIST, musics);
		} catch (IOException e) {
			Log.e("Yaamp Caching Error: ", "Error caching data with key "
					+ MusicDataCacher.KEY_ALBUMS);
			e.printStackTrace();
		}

	}

	@SuppressWarnings("unchecked")
	public static void createDatabase(Context context, String filePath) {
		MusicDB mdb = new MusicDB(context);
		ArrayList<Music> allMusics = (ArrayList<Music>) MusicDataCacher
				.readObject(context, MusicDataCacher.KEY_ALL_MUSICS);
		;
		if (!mdb.isTablePresent() || allMusics == null) {
			try {
				mdb.createTable();
				createMusicDB(context, filePath, mdb);
				cacheLibrary(context, mdb);
			} catch (Exception e) {
				Log.e("Yaamp Database Error: ",
						"Error creating database with name "
								+ mdb.getDatabaseName());
				e.printStackTrace();
			}
		}

	}

	public static void delecteCached(Context context) {
		MusicDataCacher.deleteObject(context, MusicDataCacher.KEY_ALBUMS);
		MusicDataCacher.deleteObject(context, MusicDataCacher.KEY_ALL_MUSICS);
		MusicDataCacher.deleteObject(context,
				MusicDataCacher.KEY_CURRENT_PLAYLIST);
	}

	public static class Metadata {
		private static long durationLong = 0;
		private static long durationTotal = 0;
		private static long seconds = 0;
		private static long minutes = 0;
		private static long hours = 0;

		private static void getDurationTotal(String duration) {
			if (duration != null) {
				durationLong = Long.parseLong(duration);
				durationTotal = durationLong / 1000;
			}

		}

		public static long getHours(String duration) {

			getDurationTotal(duration);
			hours = durationTotal / 3600;
			return hours;

		}

		public static long getMinutes(String duration) {
			getDurationTotal(duration);
			minutes = (durationTotal - hours * 3600) / 60;
			return minutes;

		}

		public static long getSeconds(String duration) {
			getDurationTotal(duration);
			long mns2 = (durationTotal - hours * 3600) / 60;
			seconds = durationTotal - (hours * 3600 + mns2 * 60);
			return seconds;

		}

		public static String[] getMetadataStringArray(Music music) {

			try {
				if (music != null) {
					String bitrateLong = null;

					String albumName = music.getAlbumName();
					String artistName = music.getArtistName();
					String year = music.getYear();
					String title = music.getSongTitle();
					String bitrate = music.getBitRate();
					String duration = music.getDuration();
					String trackNumber = music.getTrackNumber();
					String author = music.getAuthor();
					String mimeType = music.getMimeType();

					if (bitrate != "Unknown bitrate")
						bitrateLong = Long.parseLong(bitrate) / 1000 + "";

					String[] values = {
							"Title: " + title,
							"Artist: " + artistName,
							"Album: " + albumName,
							"Year: " + year,
							"Track number: " + trackNumber,
							"Duration: " + getStringSeparatedDuration(duration),
							"Bitrate: " + bitrateLong + " kb/s",
							"Type: " + mimeType, "Author: " + author };
					return values;
				}
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NullPointerException e) {
				e.printStackTrace();
			}
			return null;

		}

		public static String getColumnSeparatedDuration(String duration) {
			if (duration == "-1")
				return "Not available";
			else
				return getHours(duration) + ":" + getMinutes(duration) + ":"
						+ getMinutes(duration);

		}

		public static String getStringSeparatedDuration(String duration) {
			if (duration == "-1")
				return "Not available";
			else
				return getHours(duration) + " hr " + getMinutes(duration)
						+ " mn " + getSeconds(duration) + " sec ";

		}
	}

}
