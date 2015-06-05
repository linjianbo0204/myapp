package com.aifengqiang.entity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.aifengqiang.network.ConnectionClient;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Banner {
	private String id;
	private String filename;
	private String uploaded;
	private String deleted;
	private String url;
	private Bitmap bitmap;
	
	public void initFromString(String entity){
		JSONTokener tokener = new JSONTokener(entity);
		try {
			JSONObject object = (JSONObject)tokener.nextValue();
			this.id = object.getString("id");
			this.filename = object.getString("filename");
			if(object.has("uploaded"))
				this.uploaded = object.getString("uploaded");
			if(object.has("deleted"))
				this.deleted = object.getString("deleted");
			this.url = object.getString("url");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void initFromJSON(JSONObject object){
		try {
			this.id = object.getString("id");
			this.filename = object.getString("filename");
			if(object.has("uploaded"))
				this.uploaded = object.getString("uploaded");
			if(object.has("deleted"))
				this.deleted = object.getString("deleted");
			this.url = object.getString("url");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getId(){
		return this.id;
	}
	
	public String getFileName(){
		return this.filename;
	}
	
	public String getUrl(){
		return this.url;
	}
	
	public Bitmap getBitmap(){
		return this.bitmap;
	}
	
	public String getUploaded(){
		return this.uploaded;
	}
	
	public String getDeleted(){
		return this.deleted;
	}
	
	public void setImage(Bitmap bm){
		this.bitmap = bm;
	}
}
