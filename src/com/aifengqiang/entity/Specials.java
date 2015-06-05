package com.aifengqiang.entity;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.R.integer;
import android.renderscript.Type;

public class Specials {
	private String id;
	private String subject;
	private Types types;
	private Banner banner;
	private String details;
	private int price;
	private String published;
	private boolean enabled;
	private boolean limited;
	private String begin;
	private String end;
	private int people;
	
	public void initFromJson(JSONObject object){
		try {
			this.id = object.getString("id");
			this.subject = object.getString("subject");
			this.types = new Types();
			types.initFromJSON(object.getJSONObject("type"));
			this.banner = new Banner();
			if(object.has("banner"))
				banner.initFromJSON(object.getJSONObject("banner"));
			if(object.has("details"))
				this.details = object.getString("details");
			if(object.has("price"))
				this.price = object.getInt("price");
			if(object.has("published"))
				this.published = object.getString("published");
			if(object.has("enabled"))
				this.enabled = object.getBoolean("enabled");
			if(object.has("limited"))
				this.limited = object.getBoolean("limited");
			if(object.has("people"))
				this.people = object.getInt("people");
			if(object.has("begin"))
				this.begin = object.getString("begin");
			if(object.has("end"))
				this.end = object.getString("end");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void initFromString(String input){
		JSONTokener tokener = new JSONTokener(input);
		try {
			JSONObject object = (JSONObject)tokener.nextValue();
			this.id = object.getString("id");
			this.subject = object.getString("subject");
			this.types = new Types();
			types.initFromJSON(object.getJSONObject("type"));
			this.banner = new Banner();
			if(object.has("banner"))
				banner.initFromJSON(object.getJSONObject("banner"));
			if(object.has("details"))
				this.details = object.getString("details");
			if(object.has("price"))
				this.price = object.getInt("price");
			if(object.has("published"))
				this.published = object.getString("published");
			if(object.has("enabled"))
				this.enabled = object.getBoolean("enabled");
			if(object.has("limited"))
				this.limited = object.getBoolean("limited");
			if(object.has("people"))
				this.people = object.getInt("people");
			if(object.has("begin"))
				this.begin = object.getString("begin");
			if(object.has("end"))
				this.end = object.getString("end");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public String getId(){
		return id;
	}
	public String getSubject()
	{
		return this.subject;
	}
	public Types getTypes(){
		return types;
	}
	public Banner getBanner(){
		return this.banner;
	}
	public String getDetail(){
		return this.details;
	}
	public int getPrice(){
		return price;
	}
	public String getPublished(){
		return this.published;
	}
	public boolean getEnabled(){
		return this.enabled;
	}
	public boolean getLimited(){
		return limited;
	}
	public int getPeople(){
		return this.people;
	}
	public String getBegin(){
		return this.begin;
	}
	public String getEnd(){
		return this.end;
	}
}
