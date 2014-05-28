package com.yaamp.musicplayer.YaampUtilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
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

import android.media.MediaMetadataRetriever;
import android.os.Environment;
import android.util.Log;

public class XmlParser {
	static String XML_OUTPUT_FILE=Environment.getExternalStorageDirectory().getPath()+"/yaamp_sdb.xml";
	private static ArrayList<HashMap<String, String>> xmlSongsList = new ArrayList<HashMap<String, String>>();

	
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
				
				}
				else

					Log.d("XmlParser","File does not exists");
					
			    } catch (Exception e) {
				e.printStackTrace();
				Log.d("XmlParser",songsList.size()+" Problem parsing file");

			    }
		 		return songsList;
			  }
	
	//Dom implementation 
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
	
	
	 private static String getValue(String sTag, Element eElement) {
	
		   NodeList nlList = eElement.getElementsByTagName(sTag).item(0)
	
		     .getChildNodes();

		   Node nValue = nlList.item(0);

		   return nValue.getNodeValue();
		  }
		
	 
	 //Android PullParser
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
		                   Log.i("PullParser message","START_DOCUMENT");
		                    break;
		                    
		                case XmlPullParser.END_DOCUMENT:
			                   Log.i("PullParser message","END_DOCUMENT");
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
		
		
		public static boolean xmlExists(String filePath){
			
			File propFile = new File(filePath);  
			
			if(propFile.exists())
				return true;
			return false;
			
			
		}
		

}
