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
 * Idea, help: Dmitry Skorbovenko
 * User: sergey.burish
 * Date: 10.07.13
 * Time: 13:11
 * HFGridViewAdapter - "wrapper" adapter for HFGridView
 */
public class HFGridViewAdapter extends BaseAdapter {
	final Context context;
	final ListAdapter internalAdapter;
	final int rowWidth;

	private int columnWidth = HFGridView.NOT_SET;
	private int numColumns = 1;
	private int horizontalSpacing = HFGridView.NOT_SET;
	private int verticalSpacing = HFGridView.NOT_SET;
	private int stretchMode = HFGridView.STRETCH_SPACING_UNIFORM_VERT_HOR;

	// assume 1 is the least height/width/spacing of grid element
	private int minHorDimen = 1;
	private int minVerDimen = 1;

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
			itemLPSet.setMargins(horizontalSpacing, verticalSpacing, 0, 0);

			rowLayout.addView(itemView, itemLPSet);
		}

		return rowLayout;
	}

	public void setColumnWidth(int columnWidth) {
		if (columnWidth > rowWidth) {columnWidth = rowWidth;}
		this.columnWidth = columnWidth;
	}

	public int getColumnWidth() {
		return columnWidth;
	}

	public void setNumColumns(int numColumns) {
		this.numColumns = numColumns;
	}

	public int getNumColumns() {
		return numColumns;
	}

	public void setHorizontalSpacing(int horizontalSpacing) {
		this.horizontalSpacing = horizontalSpacing;
	}

	public int getHorizontalSpacing() {
		return horizontalSpacing;
	}

	public void setVerticalSpacing(int verticalSpacing) {
		this.verticalSpacing = verticalSpacing;
	}

	public int getVerticalSpacing() {
		return verticalSpacing;
	}

	public void setStretchMode(int stretchMode) {
		this.stretchMode = stretchMode;
	}

	public int getStretchMode() {
		return stretchMode;
	}

	public void recalculate() {
		int clmnWidth = (columnWidth != HFGridView.NOT_SET) ? columnWidth : minHorDimen;
		horizontalSpacing = (horizontalSpacing != HFGridView.NOT_SET) ? horizontalSpacing : minVerDimen;
		verticalSpacing = (verticalSpacing != HFGridView.NOT_SET) ? verticalSpacing : minVerDimen;

		if (numColumns == HFGridView.AUTO_FIT) { // ---------------------------- AUTO_FIT: numColumns should be count
			switch (stretchMode) {
//				case HFGridView.NO_STRETCH:
//					break;
//				case HFGridView.STRETCH_SPACING:
//					break;
//				case HFGridView.STRETCH_COLUMN_WIDTH:
//					break;
				case HFGridView.STRETCH_SPACING_UNIFORM: {
					calculateSpacingUniform();
					break;
				}
				case HFGridView.STRETCH_SPACING_UNIFORM_VERT_HOR: {
					calculateSpacingUniform();
					verticalSpacing = horizontalSpacing;
					break;
				}
			}
		} else { // --------------------------------------------------------- no AUTO_FIT: numColumns is set

			// estimate, correct horizontalSpacing
			int horSpacing = calculateHorizontalSpacing(clmnWidth, numColumns);

			if (horSpacing <= 0) {
				horSpacing = calculateHorizontalSpacing(minHorDimen, numColumns);
				if (horSpacing <= 0) {horSpacing = minHorDimen;}
			}
			if (horizontalSpacing > horSpacing) {horizontalSpacing = horSpacing;}

			// estimate, correct columnWidth
			clmnWidth = calculateColumnWidth(numColumns, horizontalSpacing);
			if (clmnWidth <= 0) {clmnWidth = minHorDimen;}

			if (columnWidth == HFGridView.NOT_SET || columnWidth > clmnWidth) {
				columnWidth = clmnWidth;
			}

			// estimate, correct numColumns
			int numClmns = calculateNumColumns(columnWidth, horizontalSpacing);
			if (numClmns < 1) {numClmns = 1;}

			if (numColumns > numClmns) {numColumns = numClmns;}
		}
	}

	private void calculateSpacingUniform() {
		if (columnWidth == HFGridView.NOT_SET) {
			columnWidth = rowWidth;
		}

		numColumns = calculateNumColumns(columnWidth, minHorDimen);
		horizontalSpacing = calculateHorizontalSpacing(columnWidth, numColumns);
	}

	// numColumns from given columnWidth, horizontalSpacing
	private int calculateNumColumns(int columnWidth, int horizontalSpacing) {
		return (rowWidth - horizontalSpacing) / (columnWidth + horizontalSpacing);
	}

	// columnWidth from given numColumns, horizontalSpacing
	private int calculateColumnWidth(int numColumns, int horizontalSpacing) {
		return (rowWidth - (numColumns+1)*horizontalSpacing) / numColumns;
	}

	// horizontalSpacing from given columnWidth numColumns
	private int calculateHorizontalSpacing(int columnWidth, int numColumns) {
		return (rowWidth - columnWidth * numColumns) / (numColumns + 1);
	}
}
