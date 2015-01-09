package com.aifengqiang.ui;

import com.aifengqiang.main.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FoodKindListView extends LinearLayout{
	
	private Context mContext;
	private int count=0;

	public FoodKindListView(Context context) {
		super(context);
		init(context);
		// TODO Auto-generated constructor stub
	}
	
	public FoodKindListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
		// TODO Auto-generated constructor stub
	}
	
	public void init(Context context){
		this.mContext = context;
		this.setPadding(5, 5, 5, 5);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		this.setOrientation(VERTICAL);
		this.setLayoutParams(lp);
	}
	
	public void addItem(String pictureUrl, String title, String detail){
		LinearLayout item = new LinearLayout(mContext);
		item.setPadding(2, 2, 2, 2);
		item.setTag(count);
		count++;
		//Log.e("Aifengqiang","count: "+count);
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.food_kind_show_list, item);
		
		ImageView image = (ImageView)item.findViewById(R.id.food_list_image);
		image.setImageResource(R.drawable.ic_launcher);
		
		TextView titleText = (TextView) item.findViewById(R.id.food_list_title);
		titleText.setText(title);
		
		TextView detailText = (TextView) item.findViewById(R.id.food_list_detail);
		detailText.setText(detail);
		
		this.addView(item);
	}
	
}
