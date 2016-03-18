package com.beijing.slidingparallax;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {

	private ParallaxListView listview;
	private View headerView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		listview = (ParallaxListView) findViewById(R.id.listview);
		// 去掉listview到头时的蓝色阴影
		listview.setOverScrollMode(AbsListView.OVER_SCROLL_NEVER);

		// 加载head布局
		headerView = View.inflate(this, R.layout.layout_header, null);
		ImageView parallaxImage = (ImageView) headerView
				.findViewById(R.id.parallaxImage);
		listview.addHeaderView(headerView);

		listview.setAdapter(new MyAdapter());
		// 设置视差ImageIVew
		listview.setParallaxImage(parallaxImage);
	}

	private class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return 20;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = View.inflate(getApplicationContext(), R.layout.simple_list, null);
				holder = new ViewHolder();
				holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
				convertView.setTag(holder);
			}else{
				holder=(ViewHolder) convertView.getTag();
			}
			holder.tv_name.setText(""+position);
			return convertView;
		}

	}
	static class ViewHolder{
		TextView tv_name;
	}
}
