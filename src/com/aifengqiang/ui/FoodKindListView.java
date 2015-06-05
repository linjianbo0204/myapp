package com.aifengqiang.ui;

import com.aifengqiang.main.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class FoodKindListView extends LinearLayout{
	
	private Context mContext;
	private int count=0;
	private LinearLayout list;

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
		mContext = context;
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		lp.gravity = Gravity.CENTER_VERTICAL;
		lp.setMargins(0, 20, 0, 20);
		this.setBackgroundColor(0xffffffff);
		this.setLayoutParams(lp);
		this.setOrientation(VERTICAL);
		
		View lineTop = new View(mContext);
		lineTop.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 1));
		lineTop.setBackgroundColor(0xffdedede);
		this.addView(lineTop);
		
		list = new LinearLayout(mContext);
		LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		llp.gravity = Gravity.CENTER_VERTICAL;
		list.setOrientation(VERTICAL);
		list.setLayoutParams(llp);
		this.addView(list);
		
		View lineBottom = new View(mContext);
		lineBottom.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 1));
		lineBottom.setBackgroundColor(0xffdedede);
		this.addView(lineBottom);
	}
	
	public void addItem(Bitmap sourceBmp, String title, String detail){
		if(count!=0){
			View line = new View(mContext);
			LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, 1);
			lp.leftMargin = 10;
			lp.rightMargin = 10;
			line.setLayoutParams(lp);
			line.setBackgroundColor(0xffdedede);
			list.addView(line);
		}
		LinearLayout item = new LinearLayout(mContext);
		item.setTag(count);
		count++;
		//Log.e("Aifengqiang","count: "+count);
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.food_kind_show_list, item);
		
		ImageView image = (ImageView)item.findViewById(R.id.food_list_image);
		if(sourceBmp!=null)
			image.setImageBitmap(sourceBmp);
		
		TextView titleText = (TextView) item.findViewById(R.id.food_list_title);
		titleText.setText(title);
		
		TextView detailText = (TextView) item.findViewById(R.id.food_list_detail);
		detailText.setText(detail);
		
		list.addView(item);
		
	}
	
	public void refreshImage(Bitmap Image, int index){
		LinearLayout item = (LinearLayout)this.findViewWithTag(index);

		ImageView image = (ImageView)item.findViewById(R.id.food_list_image);
		if(Image!=null)
			image.setImageBitmap(Image);
	}
	
}
