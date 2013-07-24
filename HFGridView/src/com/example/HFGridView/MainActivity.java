package com.example.HFGridView;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.HFGridView.customcontrols.HFGridView;

public class MainActivity extends Activity {
	private final int gridCellSize = 100;

	private HFGridView gridView;
	private BaseAdapter adapter = new BaseAdapter() {
		@Override
		public int getCount() {
			return 50;
		}

		@Override
		public Object getItem(int i) {
			return null;
		}

		@Override
		public long getItemId(int i) {
			return 0;
		}

		@Override
		public View getView(int i, View view, ViewGroup viewGroup) {
			ImageView imageView;
			if (view == null) {
				imageView = new ImageView(MainActivity.this);
				imageView.setLayoutParams(new LinearLayout.LayoutParams(gridCellSize, gridCellSize));
				imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			} else {
				imageView = (ImageView) view;
			}

			imageView.setImageResource(R.drawable.icon);
			return imageView;
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		gridView = (HFGridView) findViewById(R.id.gridView);
		gridView.setColumnWidth(gridCellSize);

		View header1 = getHeader("Header 1 text");
		View header2 = getHeader("Header 2 text");

		View footer1 = getFooter("Footer 1 text");
		View footer2 = getFooter("Footer 2 text");

		gridView.addHeaderView(header1);
		gridView.addHeaderView(header2);
		gridView.addFooterView(footer1);
		gridView.addFooterView(footer2);

		gridView.setListener(new HFGridView.HFGridViewListener() {
			@Override
			public void readyToDisposeItems() {
				gridView.setAdapter(adapter);
			}
		});
	}

	private View getHeader(String text) {
		View header = getLayoutInflater().inflate(R.layout.header, null);
		TextView textView = (TextView) header.findViewById(R.id.headerTextView);
		textView.setText(text);
		return header;
	}

	private View getFooter(String text) {
		View footer = getLayoutInflater().inflate(R.layout.footer, null);
		TextView textView = (TextView) footer.findViewById(R.id.footerTextView);
		textView.setText(text);
		return footer;
	}
}
