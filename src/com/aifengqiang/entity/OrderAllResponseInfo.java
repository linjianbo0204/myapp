package com.aifengqiang.entity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.aifengqiang.main.R.bool;

import android.R.integer;
import android.text.format.Time;

public class OrderAllResponseInfo {
	private String id;
	private String number;
	private int people;
	private LocationData location;
	private int range;
	private ArrayList<Flavor> flavors;
	private Time dueTime;
	private String diner;
	private int budget;
	private String gender;
	private String status;
	private Customer customer;
	private RestaurantInfo restaurantInfo;
	private String timestamp;
	private boolean complete;
	private String updated;
	private ArrayList<String> labels;
	private String remark = "";
	private boolean review;
	
	public void init(boolean done){
		this.id = "1909909ss";
		this.people = 1;
		this.location = new LocationData(128.1729f, 12.1231f);
		this.range = 1000;
		this.flavors = new ArrayList<Flavor>();
		dueTime = new Time();
		dueTime.parse3339("2015-01-19T17:30:00.000Z");
		flavors.add(new Flavor(1, "…’øæ"));
		diner = "µÀœ»…˙";
		budget = 200;
		if(done){
			status = "FINISH";
		}
		else{
			status = "PENDING";
		}
	}
	
	public static ArrayList<OrderAllResponseInfo> parseJSON(String input){
		JSONTokener tokener = new JSONTokener(input);
		try {
			ArrayList<OrderAllResponseInfo> result = new ArrayList<OrderAllResponseInfo>();
			JSONObject jsonObject = (JSONObject)tokener.nextValue();
			JSONArray array = jsonObject.getJSONArray("items");
			for(int i = 0; i<array.length();i++){
				OrderAllResponseInfo info = new OrderAllResponseInfo();
				JSONObject object = (JSONObject)array.get(i);
				info.id = object.getString("id");
				info.people = object.getInt("people");
				JSONObject locationJsonObject = object.getJSONObject("location");
				info.location = new LocationData(locationJsonObject.getLong("lng"), locationJsonObject.getLong("lat"));
				info.range = object.getInt("range");
				JSONArray flavorJsonArray = object.getJSONArray("flavors");
				info.flavors = new ArrayList<Flavor>();
				for(int j = 0; j<flavorJsonArray.length();j++){
					JSONObject fObject = flavorJsonArray.getJSONObject(j);
					Flavor flavor = new Flavor(fObject.getInt("id"), fObject.getString("name"));
					info.flavors.add(flavor);
				}
				info.dueTime = new Time();
				info.dueTime.parse3339(object.getString("due")+"Z");
				info.diner = object.getString("diner");
				info.budget = object.getInt("budget");
				info.status = object.getString("status");
				info.customer = new Customer();
				JSONObject cJsonObject = object.getJSONObject("customer");
				info.customer.id = cJsonObject.getString("id");
				info.customer.mobile = cJsonObject.getString("mobile");
				if(object.has("restaurant")){
					info.restaurantInfo = new RestaurantInfo();
					JSONObject resJsonObject = object.getJSONObject("restaurant");
					info.restaurantInfo.initFromJSON(resJsonObject);
				}
				info.gender = object.getString("gender");
				info.number = object.getString("number");
				info.timestamp = object.getString("timestamp");
				info.updated = object.getString("updated");
				if(object.has("completed"))
					info.complete = object.getBoolean("completed");
				JSONArray labelArray = object.getJSONArray("labels");
				info.labels = new ArrayList<>();
				for(int j = 0 ;j<labelArray.length();j++){
					info.labels.add(labelArray.getString(j));
				}
				if(object.has("remark"))
				{
					info.remark = object.getString("remark");
				}
				if(object.has("review"))
					info.review = true;
				else {
					info.review = false;
				}
				result.add(info);
			}
			return result;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
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
	
	public ArrayList<Flavor> getFlavors(){
		return this.flavors;
	}
	
	public String getDateTime(){
		return String.format("%04d-%02d-%02d", dueTime.year, dueTime.month+1, dueTime.monthDay);
	}
	
	public String getDate(){
		return String.format("%02d-%02d", dueTime.month+1, dueTime.monthDay);
	}
	
	public String getTime(){
		return String.format("%02d:%02d", dueTime.hour, dueTime.minute);
	}
	
	public String getDiner(){
		return diner;
	}
	
	public int getBuget(){
		return this.budget;
	}
	
	public String getRemark(){
		return this.remark;
	}
	
	public String getStatus(){
		return this.status;
	}
	
	public ArrayList<String> getLabels(){
		return this.labels;
	}

	public String getNumber() {
		return this.number;
	}

	public RestaurantInfo getRestaurant() {
		return this.restaurantInfo;
	}

	public void setStatus(String string) {
		// TODO Auto-generated method stub
		this.status = string;
	}

	public boolean getReview() {
		// TODO Auto-generated method stub
		return review;
	}

}
