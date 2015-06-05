package com.aifengqiang.ui;

import java.util.ArrayList;

import com.aifengqiang.data.GlobalData;
import com.aifengqiang.main.R;

import android.R.integer;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class LabelIconView extends LinearLayout{
	private Context mContext ;
	public LabelIconView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		mContext = context;
		init();
	}

	public LabelIconView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mContext = context;
		init();
	}
	
	public void init(){
		LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		lParams.gravity = Gravity.CENTER_VERTICAL;
		this.setLayoutParams(lParams);
		this.setGravity(Gravity.CENTER_VERTICAL);
	}
	
	public void setLabels(ArrayList<String> labels){
		this.removeAllViews();
		boolean[] status = new boolean[5];
		for(int i = 0;i<5;i++){
			status[i] = false; 
		}
		float scale = GlobalData.getIntance().getScale();
		for(int i = 0;i<labels.size();i++){
			switch (labels.get(i)) {
			case "ROOM":
				status[0] = true;
				break;
			case "NON_SMOKING_SEAT":
				status[1] = true;
				break;
			case "PARK":
				status[2] = true;
				break;
			case "WIFI":
				status[3] = true;
				break;
			case "CHESS_ROOM":
				status[4] = true;
				break;
			default:
				break;
			}
		}
		ImageView room= new ImageView(mContext);
		LayoutParams lp1 = new LayoutParams((int)(25*scale),(int)(25*scale));
		lp1.rightMargin = 5;
		lp1.leftMargin = 5;
		room.setLayoutParams(lp1);
		if(status[0]){
			Log.e("Restaurant","ROOM");
			room.setBackgroundResource(R.drawable.resroom);
		}
		else{
			room.setBackgroundResource(R.drawable.resnoroom);
		}
		this.addView(room);
		ImageView smoke= new ImageView(mContext);
		LayoutParams lp2 = new LayoutParams((int)(25*scale),(int)(25*scale));
		lp2.rightMargin = 5;
		smoke.setLayoutParams(lp2);
		if(status[1]){
			smoke.setBackgroundResource(R.drawable.ressmoke);
		}
		else{
			smoke.setBackgroundResource(R.drawable.resnosmoke);
		}
		this.addView(smoke);
		ImageView park= new ImageView(mContext);
		LayoutParams lp3 = new LayoutParams((int)(25*scale),(int)(25*scale));
		lp3.rightMargin = 5;
		park.setLayoutParams(lp3);
		if(status[2]){
			park.setBackgroundResource(R.drawable.respark);
		}
		else{
			park.setBackgroundResource(R.drawable.resnopark);
		}
		this.addView(park);
		ImageView wifi= new ImageView(mContext);
		LayoutParams lp4 = new LayoutParams((int)(25*scale),(int)(25*scale));
		lp4.rightMargin = 5;
		wifi.setLayoutParams(lp4);
		if(status[3]){
			wifi.setBackgroundResource(R.drawable.reswifi);
		}
		else{
			wifi.setBackgroundResource(R.drawable.resnowifi);
		}
		this.addView(wifi);
		ImageView chess= new ImageView(mContext);
		LayoutParams lp5 = new LayoutParams((int)(25*scale),(int)(25*scale));
		lp5.rightMargin = 5;
		chess.setLayoutParams(lp5);
		if(status[4]){
			chess.setBackgroundResource(R.drawable.reschess);
		}
		else{
			chess.setBackgroundResource(R.drawable.resnochess);
		}
		this.addView(chess);
	}

}
