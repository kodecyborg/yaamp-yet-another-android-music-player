package com.yaamp.musicplayer.adapters;

import com.yaamp.musicplayer.GroupByAlbumFragment;


import com.yaamp.musicplayer.AllSongs;
import com.yaamp.musicplayer.GroupByArtistFragment;
import com.yaamp.musicplayer.SearchFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsPagerAdapter extends FragmentPagerAdapter {

	public TabsPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {

		switch (index) {
		case 0:
			// Top Rated fragment activity
			return new AllSongs();
		case 1:
			// Games fragment activity
			return new GroupByAlbumFragment();
		case 2:
			// Movies fragment activity
			return new GroupByArtistFragment();
		case 3:
			// Other fragment activity
			return new SearchFragment();
		}
		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 4;
	}

}
