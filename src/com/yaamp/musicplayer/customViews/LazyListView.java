package com.yaamp.musicplayer.customViews;

import java.util.ArrayList;

import com.yaamp.musicplayer.SongData.Music;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

public class LazyListView extends ListView implements OnScrollListener {
	
	private View footer;
	private boolean isLoading=false;

	private LazyListener listener;
	private LazyAdapter adapter;

	public LazyListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.setOnScrollListener(this);
	}

	public LazyListView(Context context) {
		super(context);
		this.setOnScrollListener(this);
	}

	public void setListener(LazyListener listener) {
		this.listener = listener;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub

	}

	public void setLoadingView(int resId) {
		LayoutInflater inflater = (LayoutInflater) super.getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		footer = inflater.inflate(resId, null);
		this.addFooterView(footer);

	}

	public void setAdapter(LazyAdapter adapter) {
		super.setAdapter(adapter);
		this.adapter = adapter;
		this.removeFooterView(footer);
	}

	public void addNewData(ArrayList<Music> data) {

		this.removeFooterView(footer);

		adapter.addAll(data);
		adapter.notifyDataSetChanged();
		isLoading = false;
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		if (getAdapter() == null)
			return;

		if (getAdapter().getCount() == 0)
			return;

		int l = visibleItemCount + firstVisibleItem;

		if (l >= totalItemCount && !isLoading) {
			
			// It is time to add new data. We call the listener
			
			this.addFooterView(footer);
			isLoading = true;
			listener.loadData();
		}

	}

	public LazyListener getListener() {
		return listener;
	}

	public static interface LazyListener {
		public void loadData();
	}

}
