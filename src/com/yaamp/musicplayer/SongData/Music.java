package com.yaamp.musicplayer.SongData;

import android.os.Parcel;
import android.os.Parcelable;

public class Music implements Parcelable {

	private long id;
	private String songPath;
	private String songFileName;
	private String songTitle;
	private String fileDirectory;
	private String albumName;
	private String artistName;
	private String year;
	private String bitRate;
	private String duration;
	private String trackNumber;
	private String author;
	private String mimeType;

	public Music() {
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {

		dest.writeLong(id);
		dest.writeString(songPath);
		dest.writeString(songFileName);
		dest.writeString(songTitle);
		dest.writeString(fileDirectory);
		dest.writeString(albumName);
		dest.writeString(artistName);
		dest.writeString(year);
		dest.writeString(bitRate);
		dest.writeString(duration);
		dest.writeString(trackNumber);
		dest.writeString(author);
		dest.writeString(mimeType);

	}

	public static final Parcelable.Creator<Music> 
	CREATOR = new Parcelable.Creator<Music>() {

		public Music createFromParcel(Parcel in) {
			Music music=new Music();
			music.id = in.readLong();
			music.songPath = in.readString();
			music.songFileName = in.readString();
			music.songTitle = in.readString();
			music.fileDirectory = in.readString();
			music.albumName = in.readString();
			music.artistName = in.readString();
			music.year = in.readString();
			music.bitRate = in.readString();
			music.duration = in.readString();
			music.trackNumber = in.readString();
			music.author = in.readString();
			music.mimeType = in.readString();
			return music;
		}

		public Music[] newArray(int size) {
			return new Music[size];
		}

	};

	public Music(String songPath,
			String songFileName, 
			String songTitle,
			String fileDirectory, 
			String albumName, 
			String artistName,
			String year, 
			String bitRate, 
			String duration,
			String trackNumber, 
			String author, 
			String mimeType) {
		super();
		this.songPath = songPath;
		this.songFileName = songFileName;
		this.songTitle = songTitle;
		this.fileDirectory = fileDirectory;
		this.albumName = albumName;
		this.artistName = artistName;
		this.year = year;
		this.bitRate = bitRate;
		this.duration = duration;
		this.trackNumber = trackNumber;
		this.author = author;
		this.mimeType = mimeType;
	}

	public long getId() {
		
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getSongPath() {
		return songPath;
	}

	public void setSongPath(String songPath) {
		this.songPath = songPath;
	}

	public String getSongFileName() {
		if(songFileName==null)
			return "Unknown song filename";
		return songFileName;
	}

	public void setSongFileName(String songFileName) {
		this.songFileName = songFileName;
	}

	public String getSongTitle() {
		if(songTitle==null)
			return "Unknown song title";
		return songTitle;
	}

	public void setSongTitle(String songTitle) {
		this.songTitle = songTitle;
	}

	public String getFileDirectory() {
		return fileDirectory;
	}

	public void setFileDirectory(String fileDirectory) {
		this.fileDirectory = fileDirectory;
	}

	public String getAlbumName() {
		if(albumName==null)
			return "Unknown album";
		return albumName;
	}

	public void setAlbumName(String albumName) {
		this.albumName = albumName;
	}

	public String getArtistName() {
		if(artistName==null)
			return "Unknown artist";
		return artistName;
	}

	public void setArtistName(String artistName) {
		this.artistName = artistName;
	}

	public String getYear() {
		if(year==null)
			return "Unknown year";
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getBitRate() {
		if(bitRate==null)
			return "-1";
		return bitRate;
	}

	public void setBitRate(String bitRate) {
		this.bitRate = bitRate;
	}

	public String getDuration() {
		if(duration==null)
			return "0";
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getTrackNumber() {
		if(trackNumber==null)
			return "-1";
		return trackNumber;
	}

	public void setTrackNumber(String trackNumber) {
		this.trackNumber = trackNumber;
	}

	public String getAuthor() {
		if(author==null)
			return "Unknown author";
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getMimeType() {
		if(mimeType==null)
			return "Unknown type";
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

}
