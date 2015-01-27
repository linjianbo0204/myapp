package com.aifengqiang.ui;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class AFQRadioGroup extends LinearLayout implements android.view.View.OnClickListener{
	private ArrayList<AFQRadioButton> buttons;
	private Context mContext;
	private int selectIndex;
	
	public AFQRadioGroup(Context context) {
		super(context);
		init(context);
		// TODO Auto-generated constructor stub
	}
	public AFQRadioGroup(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
		// TODO Auto-generated constructor stub
	}

	public void init(Context context){
		buttons = new ArrayList<AFQRadioButton>();
		mContext = context;
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		this.setLayoutParams(lp);
		this.setOrientation(HORIZONTAL);
		selectIndex = 0;
	}
	
	public void addButton(AFQRadioButton btn){
		if(buttons.size()==0){
			btn.setOnClickListener(this);
			buttons.add(btn);
			this.addView(btn);
			
		}
		else{
			LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)btn.getLayoutParams();
			lp.leftMargin = 10;
			btn.setLayoutParams(lp);
			btn.setOnClickListener(this);
			buttons.add(btn);
			this.addView(btn);
		}
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
	
}
