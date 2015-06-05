package com.aifengqiang.entity;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.R.integer;

public class Category {
	private String id;
	private String name;
	private int items;
	public String getId(){
		return this.id;
	}
	public String getName(){
		return this.name;
	}
	
	public void initFromString(String entity){
		JSONTokener tokener = new JSONTokener(entity);
		try {
			JSONObject object = (JSONObject) tokener.nextValue();
			this.id = object.getString("id");
			this.name = object.getString("name");
			this.items = object.getInt("items");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void initFromJSON(JSONObject object){
		try {
			this.id = object.getString("id");
			this.name = object.getString("name");
			this.items = object.getInt("items");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
