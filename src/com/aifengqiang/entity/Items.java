package com.aifengqiang.entity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Items {
	private String id;
	private String name;
	private String price;
	private Category category;
	private Banner image;
	
	public void initFromString(String entity){
		JSONTokener tokener = new JSONTokener(entity);
		try {
			JSONObject object = (JSONObject)tokener.nextValue();
			this.id = object.getString("id");
			this.name = object.getString("name");
			this.price = object.getString("price");
			this.category = new Category();
			this.category.initFromJSON(object.getJSONObject("category"));
			this.image = new Banner();
			if(object.has("image"))
				this.image.initFromJSON(object.getJSONObject("image"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void initFromJSON(JSONObject object){
		try {
			this.id = object.getString("id");
			this.name = object.getString("name");
			this.price = object.getString("price");
			this.category = new Category();
			this.category.initFromJSON(object.getJSONObject("category"));
			this.image = new Banner();
			if(object.has("image"))
				this.image.initFromJSON(object.getJSONObject("image"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getId(){
		return this.id;
	}
	
	public String getName(){
		return this.name;
	}
	
	public String getPrice(){
		return this.price;
	}
	
	public Category getCategory(){
		return this.category;
	}
	
	public Banner getImage(){
		return this.image;
	}
}	
