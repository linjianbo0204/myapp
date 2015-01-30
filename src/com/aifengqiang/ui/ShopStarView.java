package com.aifengqiang.ui;

import com.aifengqiang.main.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class ShopStarView extends LinearLayout{
	private int star;
	private Context mContext;
	public ShopStarView(Context context) {
		super(context);
		init();
	}

	public ShopStarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	public void init(){
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.gravity = Gravity.LEFT;
		this.setLayoutParams(lp);
		this.setOrientation(HORIZONTAL);
	}
	
	public void setStar(int star){
		this.star = star;
		for(int i = 0;i<star;i++){
			ImageView iv = new ImageView(mContext);
			LayoutParams lp = new LayoutParams(15, 15);
			lp.rightMargin = 3;
			iv.setLayoutParams(lp);
			iv.setBackgroundResource(R.drawable.icon_switch);
		}
		for(int j = 0 ;j<5-star; j++){
			ImageView iv = new ImageView(mContext);
			LayoutParams lp = new LayoutParams(15, 15);
			lp.rightMargin = 3;
			iv.setLayoutParams(lp);
			iv.setBackgroundResource(R.drawable.icon_info);
		}
	}
}
