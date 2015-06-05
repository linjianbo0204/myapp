package com.aifengqiang.entity;

import org.json.JSONObject;
import org.json.JSONTokener;

public class UserDetailInfo {
	private String nameString;
	private String genderString;
	private String mobileString;
	private String birthDayString;
	private int points;
	private int events;
	
	public void getUserDetailInfoFromJson(String json){
		JSONTokener tokener = new JSONTokener(json);
		try{
			JSONObject jsonObject = (JSONObject)tokener.nextValue();
			if(jsonObject.has("name"))
				this.nameString = jsonObject.getString("name");
			if(jsonObject.has("gender"))
				this.genderString = jsonObject.getString("gender");
			if(jsonObject.has("mobile"))
				this.mobileString = jsonObject.getString("mobile");
			if(jsonObject.has("birthday"))
				this.birthDayString = jsonObject.getString("birthday");
			if(jsonObject.has("points"))
				this.points = jsonObject.getInt("points");
			if(jsonObject.has("events"))
				this.events = jsonObject.getInt("events");
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public String getName(){
		return this.nameString;
	}
	
	public String getGender(){
		return this.genderString;
	}
	
	public String getMobile(){
		return this.mobileString;
	}
	public String getBirthday(){
		return this.birthDayString;
	}
	
	public int getPoints(){
		return this.points;
	}
	
	public int getEvents(){
		return this.events;
	}

	public void setName(String editName) {
		// TODO Auto-generated method stub
		this.nameString = editName;
	}
}
