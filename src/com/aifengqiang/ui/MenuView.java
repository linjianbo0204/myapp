package com.aifengqiang.ui;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
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
	}
	
	public void setItem(int title, int bgId, int weight, String titleStr){
		LinearLayout itemView = new LinearLayout(mContext);
		LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT);
		linearLayoutParams.gravity = Gravity.CENTER_HORIZONTAL;
		linearLayoutParams.weight = weight;
		
		itemView.setBackgroundColor(Color.WHITE);
		itemView.setOrientation(LinearLayout.VERTICAL);
		itemView.setTag(title);
		itemView.setLayoutParams(linearLayoutParams);
		itemView.setClickable(true);
		itemView.setOnClickListener(this);
		
		ImageView item = new ImageView(mContext);
		item.setImageResource(bgId);
		LayoutParams params1 = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		item.setLayoutParams(params1);
		itemView.addView(item);
		
		TextView titleView = new TextView(mContext);
		LayoutParams params2 = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		titleView.setText(titleStr);
		titleView.setLayoutParams(params2);
		titleView.setGravity(Gravity.CENTER);
		titleView.setTextSize(10);
		titleView.setTextColor(Color.BLACK);
		itemView.addView(titleView);
		
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
