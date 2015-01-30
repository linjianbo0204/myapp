package com.aifengqiang.ui;

import java.util.ArrayList;

import com.aifengqiang.main.R;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class StyleListView extends LinearLayout{
	private Context mContext;
	private LinearLayout list;

	public StyleListView(Context context) {
		super(context);
		this.mContext = context;
		init();
		// TODO Auto-generated constructor stub
	}

	public StyleListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		init();
	}
	
	public void init(){
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		lp.gravity = Gravity.CENTER_VERTICAL;
		lp.topMargin = 20;
		this.setBackgroundColor(0xffffffff);
		this.setLayoutParams(lp);
		this.setOrientation(VERTICAL);
		
		View lineTop = getLine();
		this.addView(lineTop);
		
		list = new LinearLayout(mContext);
		LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		llp.leftMargin = 20;
		llp.rightMargin = 20;
		llp.gravity = Gravity.CENTER_VERTICAL;
		list.setOrientation(VERTICAL);
		list.setLayoutParams(llp);
		this.addView(list);
		
		View lineBottom = getLine();
		this.addView(lineBottom);
	}
	
	public void setList(ArrayList<String> lists){
		for(int i = 0 ;i<lists.size();i++){
			LinearLayout innerLinearLayout = new LinearLayout(mContext);
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 75);
			lp.gravity = Gravity.CENTER_VERTICAL;
			innerLinearLayout.setOrientation(HORIZONTAL);
			innerLinearLayout.setLayoutParams(lp);
			innerLinearLayout.setGravity(Gravity.CENTER_VERTICAL);
			innerLinearLayout.setTag(i);
			
			TextView tv = new TextView(mContext);
			LayoutParams tvlp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			tvlp.weight = 1;
			tv.setText(lists.get(i));
			tv.setTextColor(0xff333333);
			tv.setTextSize(20);
			
			innerLinearLayout.addView(tv);
			list.addView(innerLinearLayout);
			if(i!=lists.size()-1){
				View line = getLine();
				list.addView(line);
			}
		}
	}
	
	public View getLine(){
		View line = new View(mContext);
		line.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 1));
		line.setBackgroundColor(0xffdedede);
		return line;
	}

}