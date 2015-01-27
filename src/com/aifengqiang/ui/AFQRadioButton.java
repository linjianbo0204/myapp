package com.aifengqiang.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;

public class AFQRadioButton extends LinearLayout implements OnClickListener{
	private Context mContext;
	private boolean selected;
	private int imageSourceOn;
	private int imageSourceOff;
	private ImageView iv;
	private TextView tv;
	public AFQRadioButton(Context context){
		super(context);
		init(context);
	}

	public AFQRadioButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	
	public void init(Context context){
		selected = true;
		mContext = context;
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		this.setLayoutParams(lp);
		this.setOrientation(HORIZONTAL);
	}
	
	public void setButton(int onImageSource, int offImageSource, String text){
		imageSourceOn = onImageSource;
		imageSourceOff = offImageSource;
		iv = new ImageView(mContext);
		LayoutParams ivlp = new LayoutParams(25, 25);
		iv.setLayoutParams(ivlp);
		iv.setBackgroundResource(imageSourceOn);
		tv = new TextView(mContext);
		LayoutParams tvlp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		tv.setLayoutParams(tvlp);
		tv.setTextColor(0xfff88727);
		tv.setTextSize(15);
		tv.setText(text);
		this.addView(iv);
		this.addView(tv);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(selected){
			selected = false;
			iv.setBackgroundResource(imageSourceOff);
			tv.setTextColor(0xfff88727);
		}
		else
		{
			selected = true;
			iv.setBackgroundResource(imageSourceOn);
			tv.setTextColor(0xff666666);
		}
	}

}
