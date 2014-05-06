package com.yaamp.musicplayer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import com.yaamp.musicplayer.SongData.Music;
import com.yaamp.musicplayer.SongData.MusicDB;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

public class SongsManager extends AsyncTask<String, Integer, String>{
	// SDCard Path
	final String MEDIA_PATH = new String(Environment.getExternalStorageDirectory().getPath());
	private static ArrayList<HashMap<String, String>> xmlSongsList = new ArrayList<HashMap<String, String>>();

	static String XML_OUTPUT_FILE=Environment.getExternalStorageDirectory().getPath()+"/yaamp_sdb.xml";


	
	
	/**
	 * Function to read all mp3 files from sdcard
	 * and store the details in ArrayList
	 * */

	public static Bitmap getAlbumCover(Music music)
	{
		MediaMetadataRetriever mmr=new MediaMetadataRetriever();
		mmr.setDataSource(music.getSongPath());
		
		byte[] albumCover=mmr.getEmbeddedPicture();
		
		if(albumCover!=null)
		{
		return BitmapFactory.decodeByteArray(albumCover, 0, albumCover.length);
		}
		return null;
	
		
	}

	private static void createMusicDB(Context context,String filePath,MusicDB mdb) {

		File home = new File(filePath);
		File[] files=home.listFiles();
		MediaMetadataRetriever mmr=new MediaMetadataRetriever();
		
		//mdb.dropMusicDB();
		Music music=null;
		for(File f:files)
		{
			if(f.isDirectory())
				createDatabase(context,f.getPath());
			else
			{
				
				
				
				if(     f.getName().endsWith(".mp3")|| 
						f.getName().endsWith(".MP3")||
						f.getName().endsWith(".wma")|| 						
						f.getName().endsWith(".WMA")||
						f.getName().endsWith(".m4a")|| 
						f.getName().endsWith(".M4A")
						
						){
				
				
				mmr.setDataSource(f.getPath());
				
				
				String albumName = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
				String artistName = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
				String year = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_YEAR);
				String title = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
				String bitRate = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE);
				String duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
				String trackNumber = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_CD_TRACK_NUMBER);
				String author = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_AUTHOR);
				String mimeType = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_MIMETYPE);
				String songFileName=f.getName().substring(0, (f.getName().length() - 4));
				String fileDirectory=f.getParent();
				String songPath=f.getPath();
				
				music=new Music(songPath,
								songFileName,
								title,
								fileDirectory, 
								albumName, 
								artistName, 
								year, 
								bitRate, 
								duration, 
								trackNumber, 
								author,
								mimeType);
				
				
				mdb.addMusic(music);

			
			}
				}
		}
		
	}
	
	public static void createDatabase(Context context,String filePath){
		
		MusicDB mdb=new MusicDB(context);
		
		createMusicDB(context,filePath,mdb) ;
		
	}

	public static ArrayList<HashMap<String, String>> pullParse(String xmlPath)
	{
		HashMap<String, String> song=new HashMap<String, String>();
		try 
		{
			MediaMetadataRetriever mmr=new MediaMetadataRetriever();
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			XmlPullParser parser = factory.newPullParser();
			
			File file = new File(xmlPath);
			FileInputStream fis = new FileInputStream(file);
			parser.setInput(new InputStreamReader(fis));
			
			int eventType = parser.getEventType();
			eventType=parser.next();
			String currentTag="";

			while (eventType != XmlPullParser.END_DOCUMENT) {
				//String text = parser.getText();

				switch (eventType) {
				
	                case XmlPullParser.START_DOCUMENT:
	                   Log.i("SongsManager.pullParse message","START_DOCUMENT");
	                    break;
	                    
	                case XmlPullParser.END_DOCUMENT:
		                   Log.i("SongsManager.pullParse message","END_DOCUMENT");
		                    break;  
		                    
	                case XmlPullParser.END_TAG:    
	                	if(parser.getName().equals("song"))
	                	{
    					xmlSongsList.add(song);
	                	song=new HashMap<String, String>();
	                	}
	                	break;
		                    
	                case XmlPullParser.START_TAG:
	                    currentTag = parser.getName();

	            		
	                        if (currentTag.contentEquals("songFileName")) {
	      						song.put("songFileName", parser.nextText());

	                        } 
	                        if (currentTag.contentEquals("songPath")) {
	                        	String songPath=parser.nextText();
	    						song.put("songPath", songPath);
	    						mmr.setDataSource(songPath);
	    						//albumCoverArray.add(mmr.getEmbeddedPicture());
	                        } 
	                        if (currentTag.contentEquals("fileDirectory")) {
	    						song.put("fileDirectory", parser.nextText());

	                        }

	                        if (currentTag.contentEquals("albumName")) {
	    						song.put("albumName", parser.nextText());

	                        }

	                        if (currentTag.contentEquals("artistName")) {
	    						song.put("artistName", parser.nextText());

	                        }

	                        if (currentTag.contentEquals("year")) {
	    						song.put("year", parser.nextText());

	                        }

	                        if (currentTag.contentEquals("title")) {
	    						song.put("title", parser.nextText());

	                        }

	                        if (currentTag.contentEquals("bitRate")) {
	    						song.put("bitRate", parser.nextText());

	                        }

	                        if (currentTag.contentEquals("duration")) {
	    						song.put("duration", parser.nextText());

	                        }
	                    
	                        if (currentTag.contentEquals("trackNumber")) {
	    						song.put("trackNumber", parser.nextText());

	                        }

	                        if (currentTag.contentEquals("author")) {
	    						song.put("author", parser.nextText());

	                        }

	                    break;
				
			}
				eventType=parser.next();
		}
			

 }

		catch (Exception ex) {
			ex.printStackTrace();
			}
		return xmlSongsList;
		

	}
	

	public static  ArrayList<HashMap<String, String>> getPlayListFromXML(String xmlFilePath,String rootNode)
	{
		
		ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		HashMap<String, String> songMap = new HashMap<String, String>();		

		 try {
			 
				File fXmlFile = new File(xmlFilePath);
				if(fXmlFile.exists()){
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

				Document doc = dBuilder.parse(fXmlFile);
				
				doc.getDocumentElement().normalize();
			 
			 
				NodeList nList = doc.getElementsByTagName(rootNode);
			 
			 
				for (int temp = 0; temp < nList.getLength(); temp++) {
			 
					Node nNode = nList.item(temp);
			 
			 
					if (nNode.getNodeType() == Node.ELEMENT_NODE) {
			 
						Element eElement = (Element) nNode;
			 
						//System.out.println("Staff id : " + eElement.getAttribute("id"));
						
						songMap.put("songFileName",  getValue("songFileName", eElement));
						songMap.put("songPath",  getValue("songPath", eElement));
						songMap.put("fileDirectory", getValue("fileDirectory", eElement));
						
						songMap.put("albumName", getValue("albumName", eElement));
						songMap.put("artistName", getValue("artistName", eElement));
						songMap.put("year", getValue("year", eElement));
						songMap.put("title", getValue("title", eElement));
						songMap.put("bitRate", getValue("bitRate", eElement));
						songMap.put("duration", getValue("duration", eElement));
						songMap.put("trackNumber", getValue("trackNumber", eElement));
						songMap.put("author", getValue("author", eElement));
						
						songsList.add(songMap);
					}
				}
				Log.d("--------------------------xmlSize",songsList.size()+"");
				
				}
				else

					Log.d("--------------------------xmlExists","File does not exists");
					
			    } catch (Exception e) {
				e.printStackTrace();
				Log.d("--------------------------xmlSize",songsList.size()+" Problem parsing file");

			    }
		 		return songsList;
			  }
	 private static String getValue(String sTag, Element eElement) {
	
		   NodeList nlList = eElement.getElementsByTagName(sTag).item(0)
	
		     .getChildNodes();

		   Node nValue = (Node) nlList.item(0);

		   return nValue.getNodeValue();
		  }
		
	
	
	public static void writeXmlFile(ArrayList<HashMap<String, String>>  list) {

	    try {

	        DocumentBuilderFactory dFact = DocumentBuilderFactory.newInstance();
	        DocumentBuilder build = dFact.newDocumentBuilder();
	        Document doc = build.newDocument();

	        Element root = doc.createElement("songLibrary");
	        doc.appendChild(root);


	        for(int i=0; i<list.size(); i++ ) 
	        {
	        	Element Details = doc.createElement("song");
		        root.appendChild(Details);

	            Element name = doc.createElement("songFileName");
	            name.appendChild(doc.createTextNode(String.valueOf(list.get(i).get("songFileName"))));
	            Details.appendChild(name);
	            
	            
	            Element id = doc.createElement("songPath");
	            id.appendChild(doc.createTextNode(String.valueOf(list.get(i).get("songPath"))));
	            Details.appendChild(id);
	            

	            
	            Element mmi = doc.createElement("fileDirectory");
	            mmi.appendChild(doc.createTextNode(String.valueOf(list.get(i).get("fileDirectory"))));
	            Details.appendChild(mmi);
	           
	            
	            Element albumName = doc.createElement("albumName");
	            albumName.appendChild(doc.createTextNode(String.valueOf(list.get(i).get("albumName"))));
	            Details.appendChild(albumName);
	           
	            
	            Element artistName = doc.createElement("artistName");
	            artistName.appendChild(doc.createTextNode(String.valueOf(list.get(i).get("artistName"))));
	            Details.appendChild(artistName);
	           
	            
	            Element year = doc.createElement("year");
	            year.appendChild(doc.createTextNode(String.valueOf(list.get(i).get("year"))));
	            Details.appendChild(year);
	           
	            Element title = doc.createElement("title");
	            title.appendChild(doc.createTextNode(String.valueOf(list.get(i).get("title"))));
	            Details.appendChild(title);
	           
	            
	            Element bitRate = doc.createElement("bitRate");
	            bitRate.appendChild(doc.createTextNode(String.valueOf(list.get(i).get("bitRate"))));
	            Details.appendChild(bitRate);
	            
	            
	            Element duration = doc.createElement("duration");
	            duration.appendChild(doc.createTextNode(String.valueOf(list.get(i).get("duration"))));
	            Details.appendChild(duration);
	            
	            Element trackNumber = doc.createElement("trackNumber");
	            trackNumber.appendChild(doc.createTextNode(String.valueOf(list.get(i).get("trackNumber"))));
	            Details.appendChild(trackNumber);
	            
	            Element author = doc.createElement("author");
	            author.appendChild(doc.createTextNode(String.valueOf(list.get(i).get("author"))));
	            Details.appendChild(author);

	            Element mimeType = doc.createElement("mimeType");
	            mimeType.appendChild(doc.createTextNode(String.valueOf(list.get(i).get("mimeType"))));
	            Details.appendChild(mimeType);


	        }


	         // Save the document to the disk file
	        TransformerFactory tranFactory = TransformerFactory.newInstance();
	        Transformer aTransformer = tranFactory.newTransformer();
	        
	        // format the XML nicely
	        //aTransformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

	        aTransformer.setOutputProperty(
	        		"{http://xml.apache.org/xslt}indent-amount", "4");
	        aTransformer.setOutputProperty(OutputKeys.INDENT, "yes");



	        DOMSource source = new DOMSource(doc);

	        try {
	            FileWriter fos = new FileWriter(XML_OUTPUT_FILE);
	            StreamResult result = new StreamResult(fos);
	            aTransformer.transform(source, result);

	        } catch (IOException e) {
	        	Log.e("File writing error", "Cannot write xml to "+XML_OUTPUT_FILE);
	            e.printStackTrace();
	        }



	    } catch (TransformerException ex) {
	        System.out.println("Error outputting document");

	    } catch (ParserConfigurationException ex) {
	        System.out.println("Error building document");
	    }
	}
	
	
	
	public static boolean xmlExists(String filePath){
		
		File propFile = new File(filePath);  
		
		if(propFile.exists())
			return true;
		return false;
		
		
	}
	
	/**
	 * Class to filter files which are having .mp3 extension
	 * */
	class FileExtensionFilter implements FilenameFilter {
		

		
		public FileExtensionFilter() {
			super();
			
			// TODO Auto-generated constructor stub
		}

		public boolean accept(File dir, String name) {
			
			return (name.endsWith(".mp3") || 
					name.endsWith(".MP3")||
					name.endsWith(".OGG")|| 
					name.endsWith(".ogg")|| 
					name.endsWith(".MP3")|| 
					name.endsWith(".flac")|| 
					name.endsWith(".FLAC")||
					name.endsWith(".aac")|| 
					name.endsWith(".AAC")|| 
					name.endsWith(".m4a")|| 
					name.endsWith(".M4A")|| 
					name.endsWith(".wma")|| 
					name.endsWith(".WMA")|| 
					name.endsWith(".wav"));
		}
	}
	
	@Override
	protected String doInBackground(String... params) {
	
		
		return null;
	}
}
