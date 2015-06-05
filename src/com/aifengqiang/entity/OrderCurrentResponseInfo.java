package com.aifengqiang.entity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.text.format.Time;

public class OrderCurrentResponseInfo {
	private String id;
	private int people;
	private LocationData location;
	private int range;
	private AreaData area;
	private ArrayList<Flavor> flavors;
	private Time dueTime;
	private String diner;
	private int budget;
	private Customer customer;
	private String remark;
	private boolean completed;
	
	public boolean parseJSON(String input){
		JSONTokener tokener = new JSONTokener(input);
		try {
			JSONObject object = (JSONObject)tokener.nextValue();
			this.id = object.getString("id");
			this.people = object.getInt("people");
			JSONObject locationJsonObject = object.getJSONObject("location");
			location = new LocationData(locationJsonObject.getLong("lng"), locationJsonObject.getLong("lat"));
			this.range = object.getInt("range");
			JSONObject areaObject = object.getJSONObject("area");
			area = new AreaData(areaObject.getInt("id"), areaObject.getString("name"),
					new City(areaObject.getJSONObject("city").getInt("id"),
							areaObject.getJSONObject("city").getString("name")));
			JSONArray flavorJsonArray = object.getJSONArray("flavors");
			flavors = new ArrayList<Flavor>();
			for(int i = 0; i<flavorJsonArray.length();i++){
				JSONObject fObject = flavorJsonArray.getJSONObject(i);
				Flavor flavor = new Flavor(fObject.getInt("id"), fObject.getString("name"));
				flavors.add(flavor);
			}
			dueTime = new Time();
			dueTime.parse3339(object.getString("due")+"Z");
			diner = object.getString("diner");
			budget = object.getInt("budget");
			JSONObject customerObject = object.getJSONObject("customer");
			customer = new Customer(customerObject.getString("id"), customerObject.getString("mobile"), customerObject.getString("token"));
			remark = object.getString("remark");
			completed = object.getBoolean("completed");
			return true;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public String getId(){
		return this.id;
	}
	
	public int getPeople(){
		return people;
	}
	
	public LocationData getLocation(){
		return this.location;
	}
	
	public int getRange(){
		return this.range;
	}
	
	public AreaData getAreaData(){
		return this.area;
	}
	
	public ArrayList<Flavor> getFlavors(){
		return this.flavors;
	}
	
	public String getDate(){
		return String.format("MM-DD", dueTime.month, dueTime.monthDay);
	}
	
	public String getTime(){
		return String.format("HH:mm", dueTime.hour, dueTime.minute);
	}
	
	public String getDiner(){
		return diner;
	}
	
	public int getBuget(){
		return this.budget;
	}
	
	public Customer getCustomer(){
		return this.customer;
	}
	
	public String getRemark(){
		return this.remark;
	}
	
	public boolean getCompleteStatus(){
		return this.completed;
	}
}
