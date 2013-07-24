package com.example.HFGridView.customcontrols;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;

/**
 * Created with IntelliJ IDEA.
 * User: sergey.burish
 * Date: 10.07.13
 * Time: 13:11
 * HFGridViewAdapter - "wrapper" adapter for HFGridView
 */
public class HFGridViewAdapter extends BaseAdapter {
	final Context context;
	final ListAdapter internalAdapter;
	final int rowWidth;
	int columnWidth = 0;
	int numColumns =0;
	private int spacing = 1;

	public HFGridViewAdapter(Context context, ListAdapter adapter, int rowWidth) {
		this.context = context;
		internalAdapter = adapter;
		this.rowWidth = rowWidth;

		internalAdapter.registerDataSetObserver(new DataSetObserver() {
			@Override
			public void onChanged() {
				HFGridViewAdapter.this.notifyDataSetChanged();
			}

			@Override
			public void onInvalidated() {
				HFGridViewAdapter.this.notifyDataSetInvalidated();
			}
		});

		recalculate();
	}

	@Override // BaseAdapter
	public int getCount() {
		int numItems = internalAdapter.getCount();
		int rows = numItems / numColumns;
		if (numItems % numColumns > 0) rows++;
		return rows;
	}

	@Override // BaseAdapter
	public Object getItem(int position) {
		return null;
	}

	@Override // BaseAdapter
	public long getItemId(int position) {
		return 0;
	}

	@Override // BaseAdapter
	public View getView(int position, View convertView, ViewGroup parent) {
		LinearLayout rowLayout;

		if (convertView == null) {
			rowLayout = new LinearLayout(context);
			rowLayout.setOrientation(LinearLayout.HORIZONTAL);
			rowLayout.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.FILL_PARENT, AbsListView.LayoutParams.FILL_PARENT));
		} else {
			rowLayout = (LinearLayout)convertView;
		}

		rowLayout.removeAllViews();

		ViewGroup.LayoutParams itemLpGet = null;
		LinearLayout.LayoutParams itemLPSet = null;
		int width = 0;
		for (int inRawPosition = 0; inRawPosition < numColumns; inRawPosition++) {
			int internalPosition = position * numColumns + inRawPosition;

			if (internalPosition >= internalAdapter.getCount()) break;

			View itemView = internalAdapter.getView(internalPosition, null, parent);

			if (itemLpGet == null) {
				itemLpGet = itemView.getLayoutParams();
				width = (columnWidth != 0) ? columnWidth : itemLpGet.width;
			}

			if (itemLPSet == null) {
				itemLPSet = new LinearLayout.LayoutParams(width, itemLpGet.height);
			}
			itemLPSet.setMargins(spacing, spacing, 0, 0);

			rowLayout.addView(itemView, itemLPSet);
		}

		return rowLayout;
	}

	public void setColumnWidth(int columnWidth) {
		this.columnWidth = columnWidth;
		recalculate();
	}

	public int getNumColumns() {
		return numColumns;
	}

	private void recalculate() {
		// spacing for STRETCH_SPACING_UNIFORM
		spacing = 1; // assume 1 is the least spacing
		numColumns = (rowWidth - spacing) / (columnWidth + spacing);
		spacing = (rowWidth - columnWidth * numColumns) / (numColumns + 1);
	}
}
