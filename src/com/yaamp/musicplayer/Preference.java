/**
 * 
 */
package com.yaamp.musicplayer;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * @author unbounded
 *
 */
public class Preference extends PreferenceActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preference);
	}


}
