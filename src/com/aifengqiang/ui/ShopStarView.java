package com.aifengqiang.ui;

import com.aifengqiang.data.GlobalData;
import com.aifengqiang.main.R;

import android.R.integer;
import android.R.xml;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.view.View.OnClickListener;;

public class ShopStarView extends LinearLayout{
	private int star;
	private Context mContext;
	private OnStarChangeListener listener;
	public ShopStarView(Context context) {
		super(context);
		this.mContext = context;
		init();
	}

	public ShopStarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		init();
	}
	
	public void init(){
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		lp.gravity = Gravity.LEFT;
		this.setLayoutParams(lp);
		this.setOrientation(HORIZONTAL);
		setStar(5, 5);
	}
	
	private void setStar(int star, int margin){
		this.star = star;
		for(int i = 0;i<star;i++){
			LinearLayout layout = new LinearLayout(mContext);
			LayoutParams layoutParams = new LayoutParams(0,LinearLayout.LayoutParams.MATCH_PARENT);
			layoutParams.gravity = Gravity.CENTER;
			layoutParams.rightMargin = margin;
			layoutParams.weight = 1;
			layout.setLayoutParams(layoutParams);
			
			ImageView iv = new ImageView(mContext);
			LayoutParams lp = new LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			lp.gravity = Gravity.CENTER;
			iv.setLayoutParams(lp);
			iv.setTag(i);
			iv.setBackgroundResource(R.drawable.icon_xing_pre);
			layout.addView(iv);
			this.addView(layout);
		}
		for(int j = 0 ;j<5-star; j++){
			LinearLayout layout = new LinearLayout(mContext);
			LayoutParams layoutParams = new LayoutParams(0,LinearLayout.LayoutParams.MATCH_PARENT);
			layoutParams.gravity = Gravity.CENTER;
			layoutParams.rightMargin = margin;
			layoutParams.weight = 1;
			layout.setLayoutParams(layoutParams);
			
			ImageView iv = new ImageView(mContext);
			LayoutParams lp = new LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			lp.gravity = Gravity.CENTER;
			iv.setLayoutParams(lp);
			iv.setTag(star+j);
			iv.setBackgroundResource(R.drawable.icon_xing_nor);
			layout.addView(iv);
			this.addView(layout);
		}
	}
	
	public void setStar(int star){
		this.star = star;
		for(int i = 0; i< 5; i++){
			ImageView iv = (ImageView)findViewWithTag(i);
			if(i<star)
				iv.setBackgroundResource(R.drawable.icon_xing_pre);
			else {
				iv.setBackgroundResource(R.drawable.icon_xing_nor);
			}
		}
		invalidate();
	}
	
	public int getStar(){
		return this.star;
	}
	
	public void setOnStarChangeListener(
			OnStarChangeListener listener){
		this.listener = listener;
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent event){
		final float x = event.getX();
		int index = (int) (x/(getWidth()/5));
		if(index<5){
			if(listener!=null)
				listener.onStarChange(event, index+1);
		}
		return true;
	}
	
	public interface OnStarChangeListener{
		void onStarChange(MotionEvent event, int index);
	}
}
