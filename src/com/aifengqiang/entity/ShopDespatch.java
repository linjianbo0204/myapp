package com.aifengqiang.entity;

import java.util.regex.Matcher;

import com.aifengqiang.data.GlobalData;

import android.R.integer;
import android.graphics.Color;

public class ShopDespatch {
	public String name;
	public int color;
	public int start_x;
	public int end_x;
	public int start_y;
	public double delay;
	
	public ShopDespatch(String name){
		this.name = name;
		color = Color.rgb((int)(Math.random()*255),(int) (Math.random()*255), (int)(Math.random()*255));
		start_x = (int)(Math.random()*300*GlobalData.getIntance().getScale());
		end_x = (int)(Math.random()*300*GlobalData.getIntance().getScale());
		start_y = -(int)(Math.random()*100*GlobalData.getIntance().getScale());
		delay = Math.random()*2;
	}
}
