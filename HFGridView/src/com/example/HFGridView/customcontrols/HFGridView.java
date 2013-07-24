package com.example.HFGridView.customcontrols;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * Created with IntelliJ IDEA.
 * User: sergey.burish
 * Date: 10.07.13
 * Time: 11:26
 * HFGridView - Header-Footer-GridView - simulates GridView look + header and footer inherited from ListView
 * GridView appearance implemented in HFGridViewAdapter
 */
public class HFGridView extends ListView {
	final static String LOG_TAG = "HFGridView";

	public int getNumColumns() {
		if (hfGridViewAdapter != null) {
			return hfGridViewAdapter.getNumColumns();
		}
		return -1;
	}

	public interface HFGridViewListener {
		void readyToDisposeItems(); // should be called only once in the first onMeasure() call
	}
	HFGridViewListener listener;

	final Context context;
	HFGridViewAdapter hfGridViewAdapter = null;

	private int viewWidth = 0;
	private int columnWidth = 0;
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

		super.setAdapter(hfGridViewAdapter);
	}

	// extended STRETCH_SPACING_UNIFORM is implemented only (with equal vertical and horizontal spacing),
	// so only setColumnWidth is needed (no setNumColumns, setVerticalSpacing, setHorizontalSpacing, setStretchMode)
	public  void setColumnWidth(int columnWidth) {
		this.columnWidth = columnWidth;
		if (hfGridViewAdapter != null) hfGridViewAdapter.setColumnWidth(columnWidth);
	}
}
