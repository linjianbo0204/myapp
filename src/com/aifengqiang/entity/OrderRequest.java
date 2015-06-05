package com.aifengqiang.entity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.format.Time;

public class OrderRequest {
	public int people;
	public String due;
	public LocationData location;
	public int range;
	public AreaData area;
	public City city;
	public ArrayList<Integer> flavors;
	public String diner;
	public String gender;
	public int budget;
	public ArrayList<String> labels;
	public String remark;
	
	public OrderRequest(){
		people = 2;
		due = Time.getCurrentTimezone();
		location = new LocationData(0, 0);
		range = 1000;
		area = new AreaData(1, "");
		flavors = new ArrayList<Integer>();
		diner = "";
		gender = "MALE";
		budget = 6000;
		labels = new ArrayList<String>();
		remark = "";
		city = new City(1, "ÉÏº£");
	}
	
	public JSONObject getEntity(){
		JSONObject object = new JSONObject();
		try {
			object.put("people", people);
			object.put("due", due);
			JSONObject lObject = new JSONObject();
			lObject.put("lat", location.lat);
			lObject.put("lng", location.lng);
			object.put("location", lObject);
			object.put("range", range);
			JSONObject aObject = new JSONObject();
			if(area!=null){
				aObject.put("id", area.id);
				object.put("area", aObject);
			}
			JSONArray fArray = new JSONArray();
			for(int i:flavors){
				fArray.put(new JSONObject().put("id", i));
			}
			object.put("flavors", fArray);
			object.put("diner", diner);
			object.put("gender", gender);
			object.put("budget", budget);
			JSONArray lArray = new JSONArray();
			if(labels!=null){
				for(String s:labels){
					lArray.put(s);
				}
			}
			if(lArray.length()>0)
				object.put("labels", lArray);
			if(remark.length()>0)
				object.put("remark", remark);
			JSONObject cityOject = new JSONObject();
			cityOject.put("id", city.id);
			cityOject.put("name", city.name);
			object.put("city", cityOject);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return object;
	}
}
