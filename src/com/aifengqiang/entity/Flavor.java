package com.aifengqiang.entity;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class Flavor {
	public int id;
	public String name;
	public Banner banner;
	public String description;
	public Flavor(){
		
	}
	
	public Flavor(int id, String name){
		this.id = id;
		this.name = name;
	}
	
	public void initFromString(String entity){
		JSONTokener tokener = new JSONTokener(entity);
		try {
			JSONObject object = (JSONObject)tokener.nextValue();
			this.id = object.getInt("id");
			this.name = object.getString("name");
			this.description = object.getString("description");
			this.banner = new Banner();
			JSONObject bannerObject = object.getJSONObject("image");
			this.banner.initFromJSON(bannerObject);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void initFromJSON(JSONObject object){
		try {
			this.id = object.getInt("id");
			this.name = object.getString("name");
			if(object.has("description"))
				this.description = object.getString("description");
			this.banner = new Banner();
			if(object.has("image")){
				JSONObject bannerObject = object.getJSONObject("image");
				this.banner.initFromJSON(bannerObject);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
