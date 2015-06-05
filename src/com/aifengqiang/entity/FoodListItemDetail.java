package com.aifengqiang.entity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.text.GetChars;
import android.util.Log;

public class FoodListItemDetail {
	private String name;
	private String cost;
	private Bitmap pic;
	
	public FoodListItemDetail(String nameString, String cost){
		name = nameString;
		this.cost = cost;
	}
	
	public FoodListItemDetail(String name, String cost, int id, Resources res){
		this.name = name;
		this.cost = cost;
		pic =BitmapFactory.decodeResource(res, id);
	}
	
	public FoodListItemDetail(String name, String cost, String src){
		this.name = name;
		this.cost = cost;
		pic = getbitmap(src);
	}
	
	public  String getName()
	{
		return this.name;
	}
	
	public String getCost(){
		return this.cost+"元";
	}
	
	public Bitmap getImage(){
		return this.pic;
	}
	
	/** 
     * 根据一个网络连接(String)获取bitmap图像 
     *  
     * @param imageUri 
     * @return 
     * @throws MalformedURLException 
     */  
    public static Bitmap getbitmap(String imageUri) {  
        // 显示网络上的图片  
        Bitmap bitmap = null;  
        try {  
            URL myFileUrl = new URL(imageUri);  
            HttpURLConnection conn = (HttpURLConnection) myFileUrl  
                    .openConnection();  
            conn.setDoInput(true);  
            conn.connect();  
            InputStream is = conn.getInputStream();  
            bitmap = BitmapFactory.decodeStream(is);  
            is.close();  
  
            Log.i("", "image download finished." + imageUri);  
        } catch (IOException e) {  
            e.printStackTrace();  
            return null;  
        }  
        return bitmap;  
    }
    
//    public ArrayList<FoodListItemDetail> getFoodListDetailWithPic(String input){
//    	ArrayList<FoodListItemDetail> foodList = 
//    }
}
