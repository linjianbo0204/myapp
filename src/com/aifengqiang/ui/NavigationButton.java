package com.aifengqiang.ui;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NavigationButton extends LinearLayout{
	
	public static final int NAVIGATIONIMAGELEFT = 0;
	public static final int NAVIGATIONIMAGENONE = 1;
	public static final int NAVIGATIONIMAGERIGHT = 2;
	private Context mContext;
	
	public NavigationButton(Context context) {
		super(context);
		mContext = context;
		this.setOrientation(HORIZONTAL);
		this.setGravity(Gravity.CENTER_VERTICAL);
		// TODO Auto-generated constructor stub
	}
	
	public NavigationButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		this.setOrientation(HORIZONTAL);
		this.setGravity(Gravity.CENTER_VERTICAL);
		// TODO Auto-generated constructor stub
	}

	public void addButton(String title, int imagePosition, int imageSourceID){
		TextView tv = new TextView(mContext);
		tv.setText(title);
		tv.setTextSize(15);
		tv.setTextColor(Color.WHITE);
		tv.setBackgroundColor(Color.TRANSPARENT);
		if(imagePosition != NAVIGATIONIMAGENONE){
			ImageView iv = new ImageView(mContext);
			iv.setImageResource(imageSourceID);
			LayoutParams imageLP = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			imageLP.gravity = Gravity.CENTER_VERTICAL;
			imageLP.topMargin = 3;
			imageLP.leftMargin = 3;
			iv.setLayoutParams(imageLP);
			
			if(imagePosition == NAVIGATIONIMAGELEFT){
				this.addView(iv);
				this.addView(tv);
			}
			else
			{
				this.addView(tv);
				this.addView(iv);
			}
		}
		else
			this.addView(tv);
	}
}
