package com.aifengqiang.ui;

import com.aifengqiang.main.R;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MenuView extends LinearLayout implements OnClickListener{

	private Context mContext;
	private MenuViewListener mListener;
	
	public MenuView(Context context) {
		super(context);
		init(context);
		// TODO Auto-generated constructor stub
	}
	
	public MenuView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
		// TODO Auto-generated constructor stub
	}

//	public MenuView(Context context, AttributeSet attrs, int defStyle) {  
//        super(context, attrs, defStyle);
//    }
	
	public void init(Context context){
		this.mContext = context;
		setBackgroundColor(Color.TRANSPARENT);
		setOrientation(LinearLayout.HORIZONTAL);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		lp.gravity = Gravity.BOTTOM;
		setLayoutParams(lp);
		setBackgroundResource(R.drawable.tab_top_bg);
	}
	
	public void setItem(int title, int bgId, int weight, String titleStr, int buttonBg){
		LinearLayout itemView = new LinearLayout(mContext);
		LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT);
		linearLayoutParams.gravity = Gravity.BOTTOM;
		linearLayoutParams.weight = weight;
		itemView.setLayoutParams(linearLayoutParams);
		itemView.setGravity(Gravity.CENTER_HORIZONTAL);
		itemView.setOrientation(HORIZONTAL);
		if(buttonBg != 0){
			LinearLayout center = new LinearLayout(mContext);
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			lp.gravity = Gravity.BOTTOM;
			lp.topMargin = 5;
			center.setLayoutParams(lp);
			center.setOrientation(VERTICAL);
			
			FrameLayout frame = new FrameLayout(mContext);
			FrameLayout.LayoutParams fl = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			fl.gravity = Gravity.CENTER;
			frame.setLayoutParams(fl);
			ImageView background = new ImageView(mContext);
			background.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			background.setImageResource(buttonBg);
			frame.addView(background);
			
			LinearLayout innerView = new LinearLayout(mContext);
			LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);

			
			innerView.setOrientation(LinearLayout.VERTICAL);
			innerView.setGravity(Gravity.BOTTOM);
			llp.gravity = Gravity.BOTTOM;
			innerView.setLayoutParams(llp);
			
			itemView.setTag(title);
			itemView.setClickable(true);
			itemView.setOnClickListener(this);
			
			ImageView item = new ImageView(mContext);
			item.setImageResource(bgId);
			LayoutParams params1 = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			item.setLayoutParams(params1);
			innerView.addView(item);
			//innerView.setGravity(Gravity.CENTER);
			
			TextView titleView = new TextView(mContext);
			LayoutParams params2 = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			titleView.setText(titleStr);
			titleView.setLayoutParams(params2);
			titleView.setGravity(Gravity.CENTER);
			titleView.setTextSize(10);
			titleView.setTextColor(0xffffffff);
			innerView.addView(titleView);
			
			frame.addView(innerView);
			center.addView(frame);
			itemView.addView(center);
		}
		else{
			itemView.setOrientation(LinearLayout.VERTICAL);
			LinearLayout innerView = new LinearLayout(mContext);
			LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			llp.gravity = Gravity.CENTER_HORIZONTAL;
			llp.weight = weight;
		
			innerView.setOrientation(LinearLayout.VERTICAL);
			innerView.setLayoutParams(llp);
			itemView.setTag(title);
			itemView.setClickable(true);
			itemView.setOnClickListener(this);
		
			ImageView item = new ImageView(mContext);
			item.setImageResource(bgId);
			LayoutParams params1 = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params1.gravity=Gravity.CENTER_HORIZONTAL;
			item.setLayoutParams(params1);
			innerView.addView(item);
		
			TextView titleView = new TextView(mContext);
			LayoutParams params2 = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			titleView.setText(titleStr);
			titleView.setLayoutParams(params2);
			titleView.setGravity(Gravity.CENTER);
			titleView.setTextSize(10);
			titleView.setTextColor(0xff999999);
			innerView.addView(titleView);
		
			itemView.addView(innerView);
		}
		
		this.addView(itemView);
	}
	
	public void setMenuViewListener(MenuViewListener listener){
		this.mListener = listener;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = ((Integer)v.getTag()).intValue();
		if(mListener != null){
			mListener.OnMenuViewClick(id);
		}
	}
	
	public interface MenuViewListener{
		public void OnMenuViewClick(int id);
	}
}
