package com.yaamp.musicplayer.SongData;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import android.content.Context;

public class MusicDataCacher {
	
	public static void writeObject(Context context, String key, Object object) throws IOException {
	      FileOutputStream fos = context.openFileOutput(key, Context.MODE_PRIVATE);
	      ObjectOutputStream oos = new ObjectOutputStream(fos);
	      oos.writeObject(object);
	      oos.close();
	      fos.close();
	   }
	 
	   public static Object readObject(Context context, String key)  
	          {
		   try{
	      FileInputStream fis = context.openFileInput(key);
	      ObjectInputStream ois = new ObjectInputStream(fis);
	      Object object = ois.readObject();
	      return object;
		   }
		   catch (IOException e){
			   
		   }
		   catch (ClassNotFoundException e) {
			// TODO: handle exception
		}
		return null;
	   }
	   
	   public static boolean deleteObject(Context context, String key) {
		   
		  if(context.deleteFile(key))
			  return true;
		  else 
			  return false;
					  
	   }

}
