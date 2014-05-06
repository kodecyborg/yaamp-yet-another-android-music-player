package com.yaamp.musicplayer;


public class ID3TagHelper {

	
	String albumName ;
	String artistName ;
	String year ;
	String title;
	String bitRate ;
	String trackNumber ;
	String author ;
	
	
	public static class DurationSplitter
	{
		private static long hrs=0;
		private static long mns=0;
		private static long durationLong=0;
		private static long durationTotal=0;
		private static long seconds=0;
		private static long minutes=0;
		private static long hours=0;
		
		private static void getDurationTotal(String duration)
		{
			if(duration!=null)
			{
			durationLong=Long.parseLong(duration);
			durationTotal = durationLong / 1000;
			}
			
		}
		
		public static long getHours(String duration){
			
			getDurationTotal(duration);
			hours=durationTotal / 3600;
			return hours;
				
		}
		
		public static long getMinutes(String duration)
		{
			getDurationTotal(duration);
			minutes=(durationTotal - hrs * 3600) / 60;
			return minutes;
			
		}
		
		public static long getSeconds(String duration)
		{
			getDurationTotal(duration);
			long mns2=(durationTotal - hrs * 3600) / 60;
			seconds= durationTotal - (hrs * 3600 + mns2 * 60);
			return seconds;
			
		}
		
		public static String getColumnSeparatedDuration(String duration){
			if(duration==null)
			return "Not available";
			else
			return getHours(duration)+":"+getMinutes(duration)+":"+getMinutes(duration);
			
			
		}
		public static String getStringSeparatedDuration(String duration){
			if(duration==null)
				return "Not available";
			else
			return getHours(duration)+" hr "+getMinutes(duration)+" mn "+getSeconds(duration)+" sec ";
						
		}
	}

}
