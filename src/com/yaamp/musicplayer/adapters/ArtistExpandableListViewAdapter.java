package com.yaamp.musicplayer.adapters;

import java.util.ArrayList;
import java.util.HashMap;

import com.yaamp.musicplayer.R;
import com.yaamp.musicplayer.SongData.Music;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class ArtistExpandableListViewAdapter extends BaseExpandableListAdapter {

	private Context context;
	private int headerLayoutRessourceID;
	private int childLayoutRessourceID;
	private HashMap<String, ArrayList<Music>> dataList =new HashMap<String, ArrayList<Music>>();
	
	private ArrayList<String> headerText; // header titles

	public ArtistExpandableListViewAdapter(Context context,
			HashMap<String, ArrayList<Music>> childrenList,
			int headerRessourceID, 
			int childLayoutRessourceID) {
		
		super();
		this.context = context;
		this.dataList = childrenList;
		this.headerText = new ArrayList<String>(childrenList.keySet());
		this.headerLayoutRessourceID = headerRessourceID;
		this.childLayoutRessourceID = childLayoutRessourceID;
	}

	@Override
	public int getGroupCount() {
		return this.headerText.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return this.dataList.get(this.headerText.get(groupPosition)).size();
		
	}

	@Override
	public Object getGroup(int groupPosition) {
		return this.headerText.get(groupPosition);

	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {

		String child=this.
				dataList.
				get(this.headerText.get(groupPosition)).
				get(childPosition).getSongTitle();
		return child;

	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {

		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {

		String headerTitle = (String) getGroup(groupPosition);
		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this.context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(headerLayoutRessourceID, null);
		}

		TextView lblListHeader = (TextView) convertView
				.findViewById(R.id.listHeader);

		lblListHeader.setTypeface(null, Typeface.BOLD);
		lblListHeader.setText(headerTitle);

		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		final String childText = (String) getChild(groupPosition, childPosition);
		
		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this.context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			convertView = infalInflater.inflate(this.childLayoutRessourceID,
					null);
		}

		TextView txtListChild = (TextView) convertView
				.findViewById(R.id.listItem);
		txtListChild.setText(childText);
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {

		return true;
	}

}
