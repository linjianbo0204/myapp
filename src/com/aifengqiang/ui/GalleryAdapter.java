package com.aifengqiang.ui;

import java.util.ArrayList;
import java.util.List;

import com.aifengqiang.main.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

public class GalleryAdapter extends BaseAdapter{
	private Context mContext;
	private ArrayList<String> imageUrl;
	
	public GalleryAdapter(Context context){
		mContext = context;
		imageUrl = new ArrayList<String>();
	}
	
	public GalleryAdapter(Context context, int count){
		mContext = context;
		imageUrl = new ArrayList<String>();
	}
	
	public GalleryAdapter(Context context, int count, ArrayList<String> imageUrl){
		mContext = context;
		this.imageUrl = imageUrl;
		imageUrl = new ArrayList<String>();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 3;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ImageView imageView = new ImageView(mContext);
		imageView.setImageResource(R.drawable.ic_launcher);
		imageView.setScaleType(ImageView.ScaleType.FIT_XY);
		imageView.setLayoutParams(new Gallery.LayoutParams(Gallery.LayoutParams.MATCH_PARENT, 200));
		return imageView;
	}

}
