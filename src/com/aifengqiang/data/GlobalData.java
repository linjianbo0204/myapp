package com.aifengqiang.data;

import java.util.ArrayList;

import android.graphics.Bitmap;

public class GlobalData {
	private GlobalData globalData;
	private ArrayList<Bitmap> mainGalleryImage;
	
	private GlobalData(){
		mainGalleryImage = new ArrayList<Bitmap>();
		
	}
	
	public GlobalData getIntance(){
		if(globalData == null)
			globalData = new GlobalData();
		return globalData;
	}
	
	public Bitmap getMainGalleryImageByIndex(int index){
		if(mainGalleryImage.size()==0)
			loadMainGalleryImage();
		if(index < mainGalleryImage.size())
			return mainGalleryImage.get(index);
		else
			return null;
	}
	
	public ArrayList<Bitmap> getMainGalleryImage(){
		if(mainGalleryImage.size()==0)
			loadMainGalleryImage();
		return mainGalleryImage;
	}
	
	public void loadMainGalleryImage(){
		
	}
}
