package com.example.HFGridView.customcontrols;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * Created with IntelliJ IDEA.
 * Idea, help: Dmitry Skorbovenko
 * User: sergey.burish
 * Date: 10.07.13
 * Time: 11:26
 * HFGridView - Header-Footer-GridView - simulates GridView look + header and footer inherited from ListView
 * GridView appearance implemented in HFGridViewAdapter
 */
public class HFGridView extends ListView {
	final static String LOG_TAG = "HFGridView";

	public static final int AUTO_FIT = -1;
	public static final int NOT_SET = -1;

//	public static final int NO_STRETCH = 0;
//	public static final int STRETCH_SPACING = 1;
//	public static final int STRETCH_COLUMN_WIDTH = 2;
	public static final int STRETCH_SPACING_UNIFORM = 3;
	public static final int STRETCH_SPACING_UNIFORM_VERT_HOR = 4;

	private int numColumns = 1;
	private int columnWidth = NOT_SET;
	private int horizontalSpacing = NOT_SET;
	private int verticalSpacing = NOT_SET;
	private int stretchMode = STRETCH_SPACING_UNIFORM_VERT_HOR;

	public interface HFGridViewListener {
		void readyToDisposeItems(); // should be called only once in the first onMeasure() call
	}
	HFGridViewListener listener;

	final Context context;
	HFGridViewAdapter hfGridViewAdapter = null;

	private int viewWidth = 0;
	private boolean readyToDisposeItems = false;

	// ListView
	public HFGridView(Context context) {
		super(context);
		this.context = context;
	}

	// ListView
	public HFGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	// ListView
	public HFGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
	}

	public void setListener(HFGridViewListener listener) {
		this.listener = listener;
	}

	@Override // ListView
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		if (!readyToDisposeItems) {
			viewWidth = MeasureSpec.getSize(widthMeasureSpec);
			if (listener != null) listener.readyToDisposeItems();
		}

		readyToDisposeItems = true;
	}

	@Override // ListView
	public void setAdapter(ListAdapter adapter) {
		hfGridViewAdapter = new HFGridViewAdapter(context, adapter, viewWidth);

		hfGridViewAdapter.setColumnWidth(columnWidth);
		hfGridViewAdapter.setNumColumns(numColumns);
		hfGridViewAdapter.setHorizontalSpacing(horizontalSpacing);
		hfGridViewAdapter.setVerticalSpacing(verticalSpacing);
		hfGridViewAdapter.setStretchMode(stretchMode);

		hfGridViewAdapter.recalculate();

		super.setAdapter(hfGridViewAdapter);
	}

	public  void setColumnWidth(int columnWidth) {
		if (columnWidth > 0) {
			this.columnWidth = columnWidth;
			if (hfGridViewAdapter != null) {
				hfGridViewAdapter.setColumnWidth(columnWidth);
				hfGridViewAdapter.recalculate();
			}
		}
	}

	public int getColumnWidth() {
		if (hfGridViewAdapter != null) {
			return hfGridViewAdapter.getColumnWidth();
		}
		return columnWidth;
	}

	public void setNumColumns(int numColumns) {
		if (numColumns == AUTO_FIT || numColumns > 0) {
			this.numColumns = numColumns;
			if (hfGridViewAdapter != null) {
				hfGridViewAdapter.setNumColumns(numColumns);
				hfGridViewAdapter.recalculate();
			}
		}
	}

	public int getNumColumns() {
		if (hfGridViewAdapter != null) {
			return hfGridViewAdapter.getNumColumns();
		}
		return this.numColumns;
	}

	public void setHorizontalSpacing(int horizontalSpacing) {
		if (horizontalSpacing > 0) {
			this.horizontalSpacing = horizontalSpacing;
			if (hfGridViewAdapter != null) {
				hfGridViewAdapter.setHorizontalSpacing(horizontalSpacing);
				hfGridViewAdapter.recalculate();
			}
		}
	}

	public int getHorizontalSpacing() {
		if (hfGridViewAdapter != null) {
			return hfGridViewAdapter.getHorizontalSpacing();
		}
		return this.horizontalSpacing;
	}

	public void setVerticalSpacing(int verticalSpacing) {
		if (verticalSpacing > 0) {
			this.verticalSpacing = verticalSpacing;
			if (hfGridViewAdapter != null) {
				hfGridViewAdapter.setVerticalSpacing(verticalSpacing);
				hfGridViewAdapter.recalculate();
			}
		}
	}

	public int getVerticalSpacing() {
		if (hfGridViewAdapter != null) {
			return hfGridViewAdapter.getVerticalSpacing();
		}
		return this.verticalSpacing;
	}

	// STRETCH_SPACING_UNIFORM, STRETCH_SPACING_UNIFORM_VERT_HOR (with equal vertical and horizontal spacing) are implemented only
	public void setStretchMode(int stretchMode) {
		if (//stretchMode == NO_STRETCH ||
			//stretchMode == STRETCH_SPACING ||
			//stretchMode == STRETCH_COLUMN_WIDTH ||
			stretchMode == STRETCH_SPACING_UNIFORM ||
			stretchMode == STRETCH_SPACING_UNIFORM_VERT_HOR) {

			this.stretchMode = stretchMode;
			if (hfGridViewAdapter != null) {
				hfGridViewAdapter.setStretchMode(stretchMode);
				hfGridViewAdapter.recalculate();
			}

		}
	}

	public int getStretchMode() {
		if (hfGridViewAdapter != null) {
			return hfGridViewAdapter.getStretchMode();
		}
		return this.stretchMode;
	}
}
