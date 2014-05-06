package com.yaamp.musicplayer.SongData;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

public class MusicDB extends SQLiteOpenHelper{
		
	// Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "yaampMusicDB";

    //Table name
    private static final String TABLE_NAME="MySong";
   
    public  final String KEY_ID = "id";
    public  final String KEY_SONG_PATH="songPath";
    public  final String KEY_FILE_NAME = "songFileName";
    public  final String KEY_SONG_TITLE = "songTitle";
    public  final String KEY_FILE_DIRECTORY = "fileDirectory";
    public  final String KEY_ALBUM_NAME = "albumName";
    public  final String KEY_ARTIST_NAME = "artistName";
    public  final String KEY_YEAR = "year";
    public  final String KEY_TITLE = "title";
    public  final String KEY_BITRATE = "bitRate";
    public  final String KEY_DURATION = "duration";
    public  final String KEY_TRACK_NUMBER = "trackNumber";
    public  final String KEY_AUTHOR = "author";
    public  final String KEY_MIME_TYPE="mimeType";
	
    Context context;
    
	public MusicDB(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
		this.context=context;
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		// SQL statement to create music table
        String CREATE_MUSIC_TABLE = "CREATE TABLE IF NOT EXISTS "+TABLE_NAME+" ( " 
        		
                + KEY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"  
                + KEY_SONG_PATH+ " TEXT,"
                + KEY_FILE_NAME+" TEXT,"
                + KEY_SONG_TITLE+" TEXT,"
                + KEY_FILE_DIRECTORY+" TEXT,"
                + KEY_ALBUM_NAME+" TEXT,"
                + KEY_ARTIST_NAME+" TEXT,"
                + KEY_YEAR+" TEXT,"
                + KEY_TITLE+" TEXT,"
                + KEY_BITRATE+" TEXT,"
                + KEY_DURATION+" TEXT,"
                + KEY_TRACK_NUMBER+" TEXT,"
                + KEY_AUTHOR+" TEXT,"
                + KEY_MIME_TYPE+" TEXT"
                + ")";
 
        // create music table
        db.execSQL(CREATE_MUSIC_TABLE);
        Log.i("Database created",db.getVersion()+"");
       // new ExportDatabaseFileTask(context, DATABASE_NAME).execute();
        
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		 // Drop older musics table if existed
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        
        // create fresh musics table
        this.onCreate(db);
       // new ExportDatabaseFileTask(context, DATABASE_NAME).execute();

	
	}
	
	public void dropTable()
	{
		
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
		
		this.onCreate(db);
	}
	public void dropMusicDB()
	{
		
		context.deleteDatabase(DATABASE_NAME);
       // new ExportDatabaseFileTask(context, DATABASE_NAME).execute();
		File file=new File(Environment.getDataDirectory() 
				+ "/data/"
				+context.getPackageName()
				+"/databases/"
				+DATABASE_NAME
				+".db");
        if(file.exists())
		file.delete();
        Log.i("Database deleted",DATABASE_VERSION+"");

	}
	
	public void addMusic(HashMap<String, String> musicMap){
		
		SQLiteDatabase db = this.getWritableDatabase();
		
		try {
			ContentValues values = new ContentValues();
			values.put(KEY_SONG_PATH, musicMap.get(KEY_SONG_PATH));
			values.put(KEY_FILE_NAME, musicMap.get(KEY_FILE_NAME)); // get title 
			values.put(KEY_SONG_TITLE, musicMap.get(KEY_SONG_TITLE)); // get title 
			values.put(KEY_FILE_DIRECTORY, musicMap.get(KEY_FILE_DIRECTORY)); // get author
			values.put(KEY_ALBUM_NAME, musicMap.get(KEY_ALBUM_NAME)); // get author
			values.put(KEY_ARTIST_NAME, musicMap.get(KEY_ARTIST_NAME)); // get author
			values.put(KEY_YEAR, musicMap.get(KEY_YEAR)); // get author
			values.put(KEY_TITLE, musicMap.get(KEY_TITLE)); // get author
			values.put(KEY_BITRATE, musicMap.get(KEY_BITRATE)); // get author
			values.put(KEY_DURATION, musicMap.get(KEY_DURATION)); // get author
			values.put(KEY_TRACK_NUMBER, musicMap.get(KEY_TRACK_NUMBER)); // get author
			values.put(KEY_AUTHOR, musicMap.get(KEY_AUTHOR)); // get author
			values.put(KEY_MIME_TYPE, musicMap.get(KEY_MIME_TYPE));

			db.insert(TABLE_NAME, null, values);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
	
		db.close();
       // new ExportDatabaseFileTask(context, DATABASE_NAME).execute();

		}
		}
	
	
	public void addMusic(Music music)
	{
		SQLiteDatabase db = this.getWritableDatabase();

		try {
			ContentValues values = new ContentValues();
			values.put(KEY_SONG_PATH, music.getSongPath());
			values.put(KEY_FILE_NAME, music.getSongFileName());
			values.put(KEY_SONG_TITLE, music.getSongTitle()); // get title 
			values.put(KEY_FILE_DIRECTORY, music.getFileDirectory()); // get author
			values.put(KEY_ALBUM_NAME, music.getAlbumName()); // get author
			values.put(KEY_ARTIST_NAME, music.getArtistName()); // get author
			values.put(KEY_YEAR, music.getYear()); // get author
			
			values.put(KEY_BITRATE, music.getBitRate()); // get author
			values.put(KEY_DURATION, music.getDuration()); // get author
			values.put(KEY_TRACK_NUMBER, music.getTrackNumber()); // get author
			values.put(KEY_AUTHOR, music.getAuthor()); // get author
			values.put(KEY_MIME_TYPE, music.getMimeType());
			
			db.insert(TABLE_NAME, null, values);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
		db.close();
		
       // new ExportDatabaseFileTask(context, DATABASE_NAME).execute();

		}
	}
	
	
	public ArrayList<Music> getAllMusics() {
		
		ArrayList<Music> musics =new ArrayList<Music>();

	String query = "SELECT * FROM " + TABLE_NAME;
	Cursor cursor = null;
	SQLiteDatabase db = this.getWritableDatabase();
	try {
		cursor = db.rawQuery(query, null);
		
		Music music = null;
		if (cursor.moveToFirst()){
			do{
				music =new Music();
				music.setId(Integer.parseInt(cursor.getString(0)));
				music.setSongPath(cursor.getString(1));
				music.setSongFileName(cursor.getString(2));
				music.setSongTitle(cursor.getString(3));
				music.setFileDirectory(cursor.getString(4));
				music.setAlbumName(cursor.getString(5));
				music.setArtistName(cursor.getString(6));
				music.setYear(cursor.getString(7));				
				music.setBitRate(cursor.getString(8));
				music.setTrackNumber(cursor.getString(9));
				music.setAuthor(cursor.getString(10));
				music.setMimeType(cursor.getString(11));
				
				musics.add(music);
			
		} while (cursor.moveToNext());
}
	} catch (NumberFormatException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	finally{
	db.close();
	if(cursor!=null)
	cursor.close();
	

	}
	return musics;
		}
	
	
	public ArrayList<Music> getMusicsByColumnNameValue(String columnName,String columnValue) {
		
		ArrayList<Music> musics =new ArrayList<Music>();
		Cursor cursor=null;
	String query = "SELECT * FROM " 
					+ TABLE_NAME 
					+ " WHERE "
					+columnName
					+" = '"
					+columnValue
					+"'";
	
	SQLiteDatabase db = this.getWritableDatabase();
	
	try {
		cursor = db.rawQuery(query, null);
		Music music = null;
		
		if (cursor.moveToFirst()){
			do{
				music =new Music();
				music.setId(Integer.parseInt(cursor.getString(0)));
				music.setSongPath(cursor.getString(1));
				music.setSongFileName(cursor.getString(2));
				music.setSongTitle(cursor.getString(3));
				music.setFileDirectory(cursor.getString(4));
				music.setAlbumName(cursor.getString(5));
				music.setArtistName(cursor.getString(6));
				music.setYear(cursor.getString(7));				
				music.setBitRate(cursor.getString(8));
				music.setTrackNumber(cursor.getString(9));
				music.setAuthor(cursor.getString(10));
				music.setMimeType(cursor.getString(11));
				
				musics.add(music);
			
		} while (cursor.moveToNext());
}
	} catch (NumberFormatException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	catch(NoSuchElementException e)
	{
		Log.e("MusicDB.getMusicsByColumnNameValue: No such Element","Trying to retreive a non existent value from the database");
	}
	finally
	{
		if(cursor!=null)
		cursor.close();
		db.close();

	}
	return musics;
		}
	
public 	ArrayList<Music> searchMusics(String value)
{
	Set<Music> resultNoDuplicates=new HashSet<Music>();
	
	Set<Music> artistName =new HashSet<Music>(getMusicsByColumnNameValueLWC(KEY_ARTIST_NAME, value));
	Set<Music> albumName=new HashSet<Music>(getMusicsByColumnNameValueLWC(KEY_ALBUM_NAME, value));
	Set<Music> songTitle=new HashSet<Music>(getMusicsByColumnNameValueLWC(KEY_SONG_TITLE, value));
	
	resultNoDuplicates.addAll(artistName);
	resultNoDuplicates.addAll(albumName);
	resultNoDuplicates.addAll(songTitle);
	
	return new ArrayList<Music>(resultNoDuplicates);
	
}
	
//Search with sql like wildcard	
public ArrayList<Music> getMusicsByColumnNameValueLWC(String columnName,String columnValue) {
		
		ArrayList<Music> musics =new ArrayList<Music>();
		Cursor cursor=null;
	String query = "SELECT * FROM " 
					+ TABLE_NAME 
					+ " WHERE "
					+columnName
					+" like '%"
					+columnValue
					+"%'";
	
	SQLiteDatabase db = this.getWritableDatabase();
	
	try {
		cursor = db.rawQuery(query, null);
		Music music = null;
		
		if (cursor.moveToFirst()){
			do{
				music =new Music();
				music.setId(Integer.parseInt(cursor.getString(0)));
				music.setSongPath(cursor.getString(1));
				music.setSongFileName(cursor.getString(2));
				music.setSongTitle(cursor.getString(3));
				music.setFileDirectory(cursor.getString(4));
				music.setAlbumName(cursor.getString(5));
				music.setArtistName(cursor.getString(6));
				music.setYear(cursor.getString(7));
				music.setBitRate(cursor.getString(8));
				music.setTrackNumber(cursor.getString(9));
				music.setAuthor(cursor.getString(10));
				music.setMimeType(cursor.getString(11));
				
				musics.add(music);
			
		} while (cursor.moveToNext());
}
	} catch (NumberFormatException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	catch(NoSuchElementException e)
	{
		Log.e("MusicDB.getMusicsByColumnNameValue: No such Element","Trying to retreive a non existent value from the database");
	}
	finally
	{
		if(cursor!=null)
		cursor.close();
		db.close();

	}
	return musics;
		}
	
	
	
	public HashMap<String, ArrayList<Music>>  getMusicsByArtist() {
		String query = "SELECT DISTINCT " 
						+ KEY_ARTIST_NAME 
						+ " FROM "
						+ TABLE_NAME;
		String artistName="";
		Cursor cursor=null;
		HashMap<String, ArrayList<Music>>  artists=new HashMap<String, ArrayList<Music>>();
		SQLiteDatabase db = this.getWritableDatabase();
		try {
			
			cursor = db.rawQuery(query, null);
			
			
			if (cursor.moveToFirst()) {
				do {
					
					artistName = cursor.getString(0);
					artists.put(artistName,getMusicsByColumnNameValue(KEY_ARTIST_NAME, artistName));
				
				} while (cursor.moveToNext());
			}
		} catch (NoSuchElementException e) {
			// TODO Auto-generated catch block
			Log.e("MusicDB.getMusicsByArtist: No such Element","Trying to retreive a non existent value from the database");
		}
		finally
		{
			db.close();
			if(cursor!=null)
			cursor.close();
	      //  new ExportDatabaseFileTask(context, DATABASE_NAME).execute();

		}
		return artists;

	}
	
	public Multimap<Integer, ArrayList<Music>>  getMusicsByAlbum() {
		String query = "SELECT DISTINCT " 
						+ KEY_ALBUM_NAME 
						+ " FROM "						
						+ TABLE_NAME						
						;
		
		String albumName="";
		Cursor cursor=null;
		Multimap<Integer, ArrayList<Music>> albums=ArrayListMultimap.create();
		SQLiteDatabase db = this.getWritableDatabase();

		try {
			cursor = db.rawQuery(query, null);

			int i=0; 
			if (cursor.moveToFirst()) {
				do {
					
				 albumName = cursor.getString(0);
				 if(albumName!=null)
				 albums.put(i,getMusicsByColumnNameValue(KEY_ALBUM_NAME, albumName));
				 i++;
				} while (cursor.moveToNext());
			}
		}
			catch (NoSuchElementException e) {
				// TODO Auto-generated catch block
				Log.e("MusicDB.getMusicsByAlbum No such Element","Trying to retreive a non existent value from the database");
			}
		finally{
			if(cursor!=null)
			cursor.close();
			db.close();

		}
		return albums;

	}
	public void deleteMusic(Music music) {
		
		
		SQLiteDatabase db = this.getWritableDatabase();
		

		try {
			db.delete(TABLE_NAME, //table name
			KEY_ID+" = ?",
			new String[] { String.valueOf(music.getId()) }); 
			
			Log.d("Delete music",music.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			db.close();
	      //  new ExportDatabaseFileTask(context, DATABASE_NAME).execute();

		}
		
		}
	
	
}
