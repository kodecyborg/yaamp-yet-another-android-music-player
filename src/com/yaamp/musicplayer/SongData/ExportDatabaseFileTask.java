package com.yaamp.musicplayer.SongData;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

public class ExportDatabaseFileTask extends AsyncTask<String, Void, Boolean> {
	 Context context;
	 String dbName="";

	 
	 
        public ExportDatabaseFileTask(Context context,String dbName) {
		super();
    	this.context=context;
		this.dbName=dbName;
	} 

		// can use UI thread here
        @Override
		protected void onPreExecute() {
          Toast.makeText(context, "Exporting database", Toast.LENGTH_LONG).show();
          Log.i("Database export","Exporting database "+dbName+".db");
        }

        // automatically done on worker thread (separate from UI thread)
        @Override
		protected Boolean doInBackground(final String... args) {

        	 try {
           File dbFile =
                    new File("//"+Environment.getDataDirectory() 
                    		+ "/data/"+context.getPackageName()
                    		+"/databases/"+dbName+".db");

           File exportDir = new File(Environment.getExternalStorageDirectory(), "");
           if (!exportDir.exists()) {
              exportDir.mkdirs();
           }
           File file = new File(exportDir, dbFile.getName());

          
              file.createNewFile();
              this.copyFile(dbFile, file);
              return true;
           } catch (Exception e) {
              Log.e("my package", e.getMessage(), e);
              return false;
           }
        }

        // can use UI thread here
        @Override
		protected void onPostExecute(final Boolean success) {
          
           if (success) {
              Toast.makeText(context, "Export successful!", Toast.LENGTH_SHORT).show();
           } else {
              Toast.makeText(context, "Export failed", Toast.LENGTH_SHORT).show();
           }
        }

        void copyFile(File src, File dst) throws IOException {
           FileChannel inChannel = new FileInputStream(src).getChannel();
           FileChannel outChannel = new FileOutputStream(dst).getChannel();
           try {
              inChannel.transferTo(0, inChannel.size(), outChannel);
           } finally {
              if (inChannel != null)
                 inChannel.close();
              if (outChannel != null)
                 outChannel.close();
           }
        }

     }