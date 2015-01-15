package com.aifengqiang.ui;


import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class NavigationView extends RelativeLayout implements OnClickListener{
	
	public static final int NAVIGATION_BUTTON_LEFT = 0;
	public static final int NAVIGATION_BUTTON_RIGHT = 1;
	
	private Context mContext;
	private NavigationViewListener mListener;

	public NavigationView(Context context) {
		super(context);  
        init(context);  
	}

	public NavigationView(Context context, AttributeSet attrs) {
		super(context, attrs);  
        init(context);  
	}
	
	public NavigationView(Context context, AttributeSet attrs, int defStyle) {  
        super(context, attrs, defStyle);  
        init(context);  
    }

	private void init(Context context) {
		mContext = context;
		
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(-1, -2);
		this.setLayoutParams(lp);
		this.setBackgroundColor(0xfff88727);
	}
	
	public void setLeftButton(String title, int imagePosition, int imageSourceID){
		setButton(title, NAVIGATION_BUTTON_LEFT, imagePosition, imageSourceID);
	}
	
	public void setRightButton(String title, int imagePosition, int imageSourceID){
		setButton(title, NAVIGATION_BUTTON_RIGHT, imagePosition, imageSourceID);
	}
	
	private void setButton(String title, int buttonId, int imagePosition, int imageSourceID){
		NavigationButton oldButton = (NavigationButton) this.findViewWithTag(new Integer(buttonId));
		if(oldButton != null){
			this.removeView(oldButton);
		}
		
		NavigationButton newButton = new NavigationButton(mContext);
		newButton.setTag(new Integer(buttonId));
		newButton.setOnClickListener(this);
		
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(-2, -2);
		if(buttonId == NAVIGATION_BUTTON_LEFT)
			lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		else if(buttonId == NAVIGATION_BUTTON_RIGHT)
			lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		lp.addRule(RelativeLayout.CENTER_VERTICAL);
		lp.setMargins(10, 0, 10, 0);
		newButton.setLayoutParams(lp);
		
		newButton.addButton(title, imagePosition, imageSourceID);
		this.addView(newButton);
	}
	
	public void setTitle(String title){
		TextView oldTitle = (TextView) this.findViewWithTag("title");
		if(oldTitle != null){
			this.removeView(oldTitle);
		}
		
		TextView newTitle = new TextView(mContext);
		newTitle.setTag("title");
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(-2,-2);
		lp.addRule(RelativeLayout.CENTER_IN_PARENT);
		lp.setMargins(0, 30, 0, 30);
		newTitle.setLayoutParams(lp);
		
		newTitle.setText(title);
		newTitle.setTextSize(22);
		newTitle.setTextColor(Color.WHITE);
		
		this.addView(newTitle);
	}
	
	public void setNavigationViewListener(NavigationViewListener listener){
		mListener = listener;
	}
	
	public void onClick(View v){
		int id = ((Integer)v.getTag()).intValue();
		if(mListener != null){
			mListener.OnNavigationButtonClick(id);
		}
	}
	
	public interface NavigationViewListener{
		public void OnNavigationButtonClick(int id);
	}
}
