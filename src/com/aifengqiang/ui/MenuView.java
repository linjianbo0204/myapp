package com.aifengqiang.ui;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

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
		setOrientation(HORIZONTAL);
	}
	
	public void setItem(int title, int bgId, int weight){
		View item = new View(mContext);
		item.setTag(title);
		item.setBackgroundResource(bgId);
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.weight = weight;
		item.setLayoutParams(params);
		item.setClickable(true);
		item.setOnClickListener(this);
		this.addView(item);
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
