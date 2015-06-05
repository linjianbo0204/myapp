package com.aifengqiang.entity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class EventInfo {
	private String id;
	private String subject;
	private Banner banner;
	private String details;
	private String published;
	private Boolean involved;
	
	public void initFromString(String entity){
		JSONTokener tokener = new JSONTokener(entity);
		try {
			JSONObject object = (JSONObject)tokener.nextValue();
			this.id = object.getString("id");
			this.subject = object.getString("subject");
			if(object.has("details"))
				this.details = object.getString("details");
			if(object.has("published"))
				this.published = object.getString("published");
			if(object.has("involved"))
				this.involved = object.getBoolean("involved");
			this.banner = new Banner();
			if(object.has("banner")){
				JSONObject bannerObject = object.getJSONObject("banner");
				this.banner.initFromJSON(bannerObject);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void initFromJSON(JSONObject object){
		try {
			this.id = object.getString("id");
			this.subject = object.getString("subject");
			if(object.has("details"))
				this.details = object.getString("details");
			if(object.has("published"))
				this.published = object.getString("published");
			if(object.has("involved"))
				this.involved = object.getBoolean("involved");
			this.banner = new Banner();
			if(object.has("banner")){
				JSONObject bannerObject = object.getJSONObject("banner");
				this.banner.initFromJSON(bannerObject);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getId(){
		return id;
	}
	
	public String getSubjectString() {
		return subject;
	}
	
	public Banner getBanner(){
		return banner;
	}
	public String getDetail(){
		return details;
	}
	
	public String getPublished(){
		return published;
	}
	
	public boolean getInvolved(){
		return involved;
	}
}
