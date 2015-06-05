package com.aifengqiang.ui;

import com.aifengqiang.data.GlobalData;
import com.aifengqiang.main.R;

import android.R.integer;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
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
		if(title != 1){
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
				
				innerView.setTag(title);
				innerView.setClickable(true);
				innerView.setOnClickListener(this);
				
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
			else if(title != 1){
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
				item.setTag(title+"bg");
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
		else {
			LinearLayout itemView = new LinearLayout(mContext);
			LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT);
			linearLayoutParams.gravity = Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL;
			linearLayoutParams.weight = weight;
			itemView.setLayoutParams(linearLayoutParams);
			itemView.setGravity(Gravity.CENTER_HORIZONTAL);
			itemView.setOrientation(HORIZONTAL);
			
			FrameLayout frameLayout = new FrameLayout(mContext);
			FrameLayout.LayoutParams fLayoutParams = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			frameLayout.setLayoutParams(fLayoutParams);

			LinearLayout innerView = new LinearLayout(mContext);
			LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			llp.gravity = Gravity.CENTER_HORIZONTAL;
			llp.weight = weight;
			innerView.setPadding(10, 7, 10, 0);
		
			innerView.setOrientation(LinearLayout.VERTICAL);
			innerView.setLayoutParams(llp);
			itemView.setTag(title);
			itemView.setClickable(true);
			itemView.setOnClickListener(this);
		
			ImageView item = new ImageView(mContext);
			item.setImageResource(bgId);
			item.setTag(title+"bg");
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
			
			FrameLayout countView = new FrameLayout(mContext);
			countView.setTag(title+"view");
			countView.setVisibility(View.INVISIBLE);
			FrameLayout.LayoutParams countParams = new FrameLayout.LayoutParams((int)(15*GlobalData.getIntance().getScale()),(int)(15*GlobalData.getIntance().getScale()));
			countParams.gravity=Gravity.END;
			countView.setLayoutParams(countParams);
			
			ImageView imageView = new ImageView(mContext);
			FrameLayout.LayoutParams frameLayoutParams = new FrameLayout.LayoutParams((int)(15*GlobalData.getIntance().getScale()),(int)(15*GlobalData.getIntance().getScale()));
			imageView.setImageResource(R.drawable.bubble_bg);
			imageView.setLayoutParams(frameLayoutParams);
			
			TextView countText = new TextView(mContext);
			FrameLayout.LayoutParams countTextParams = new FrameLayout.LayoutParams((int)(15*GlobalData.getIntance().getScale()),(int)(15*GlobalData.getIntance().getScale()));
			countText.setLayoutParams(countTextParams);
			countText.setGravity(Gravity.CENTER);
			countText.setTag(title+"count");
			countText.setTextSize(8);
			countText.setTextColor(Color.WHITE);
			countView.addView(imageView);
			countView.addView(countText);
			
			frameLayout.addView(innerView);
			frameLayout.addView(countView);
			itemView.addView(frameLayout);
			this.addView(itemView);
		}
	}
	
	public void changeBg(int index, int src){
		ImageView imageView = (ImageView)this.findViewWithTag(index+"bg");
		if(imageView != null)
			imageView.setImageResource(src);
	}
	
	public void changeCount(int count){
		if(count==0){
			FrameLayout countView = (FrameLayout)findViewWithTag(1+"view");
			if(countView!=null)
				countView.setVisibility(View.INVISIBLE);
		}
		else{
			FrameLayout countView = (FrameLayout)findViewWithTag(1+"view");
			TextView countTextView = (TextView)findViewWithTag(1+"count");
			if(countView!=null&&countTextView!=null){
				countView.setVisibility(View.VISIBLE);
				countTextView.setText(count+"");
			}
		}
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
