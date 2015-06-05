package com.aifengqiang.entity;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class AreaData {
	public int id;
	public String name;
	public City city;
	public City district;
	public AreaData(){
		
	}
	
	public AreaData(int id, String name){
		this.id = id;
		this.name = name;
	}
	public AreaData(int id, String name, City city){
		this.id = id;
		this.name = name;
		this.city = city;
	}
	
	public void initFromString(String entity){
		JSONTokener tokener = new JSONTokener(entity);
		try {
			JSONObject object = (JSONObject)tokener.nextValue();
			this.id = object.getInt("id");
			this.name = object.getString("name");
			JSONObject cityObject = object.getJSONObject("city");
			JSONObject districtObject = object.getJSONObject("district");
			this.city = new City(cityObject.getInt("id"), cityObject.getString("name"));
			this.district = new City(districtObject.getInt("id"), districtObject.getString("name"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void initFromJSON(JSONObject object){
		try {
			this.id = object.getInt("id");
			this.name = object.getString("name");
			JSONObject cityObject = object.getJSONObject("city");
			JSONObject districtObject = object.getJSONObject("district");
			this.city = new City(cityObject.getInt("id"), cityObject.getString("name"));
			this.district = new City(districtObject.getInt("id"), districtObject.getString("name"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
