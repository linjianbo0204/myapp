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

import android.R.integer;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class RestaurantInfo {
	private String id;
	private String name;
	private ArrayList<Types> types;
	private int star;
	private int cost;
	private int taste;
	private int env;
	private int service;
	private LocationData locationData;
	private String address;
	private String phone;
	private City city;
	private AreaData area;
	private ArrayList<String> labels;
	private ArrayList<Items> items;
	private ArrayList<Specials> specials;
	private ArrayList<String> photos;
	private ArrayList<Bitmap> bitmaps;
	private Banner banner;
	
	public void initFromString(String entity){
		JSONTokener tokener = new JSONTokener(entity);
		try {
			JSONObject object = (JSONObject)tokener.nextValue();
			this.id = object.getString("id");
			this.name = object.getString("name");
			this.types = new ArrayList<Types>();
			JSONArray array = object.getJSONArray("types");
			for(int i = 0;i<array.length();i++){
				JSONObject type = (JSONObject)array.get(i);
				Types typeEntity = new Types();
				typeEntity.initFromJSON(type);
				this.types.add(typeEntity);
			}
			this.star = object.getInt("star");
			this.cost = object.getInt("cost");
			this.taste = object.getInt("taste");
			this.env = object.getInt("env");
			this.service = object.getInt("service");
			this.address = object.getString("address");
			this.phone = object.getString("phone");
			JSONObject cityObject = object.getJSONObject("city");
			this.city = new City(cityObject.getInt("id"), cityObject.getString("name"));
			this.area = new AreaData(object.getJSONObject("area").getInt("id"), object.getJSONObject("area").getString("name"));
			this.locationData = new LocationData(object.getJSONObject("coordinate").getLong("lng"), object.getJSONObject("coordinate").getLong("lat"));
			JSONArray labesJsonArray = object.getJSONArray("labels");
			this.labels = new ArrayList<>();
			for(int i = 0; i<labesJsonArray.length();i++){
				String labesObject = labesJsonArray.getString(i);
				this.labels.add(labesObject);
			}
			if(object.has("items")){
				JSONArray itemsArray = object.getJSONArray("items");
				this.items = new ArrayList<Items>();
				for(int i = 0;i<itemsArray.length();i++){
					JSONObject itemObject = itemsArray.getJSONObject(i);
					Items items = new Items();
					items.initFromJSON(itemObject);
					this.items.add(items);
				}
			}
			
			if(object.has("specials")){
				JSONArray specialsArray = object.getJSONArray("specials");
				this.specials = new ArrayList<Specials>();
				for(int i = 0;i<specialsArray.length();i++){
					JSONObject specialObject = specialsArray.getJSONObject(i);
					Specials specials = new Specials();
					specials.initFromJson(specialObject);
					this.specials.add(specials);
				}
			}
			
			if(object.has("photos")){
				JSONArray photoArray = object.getJSONArray("photos");
				this.photos = new ArrayList<String>();
				for(int i = 0; i<photoArray.length();i++)
				{
					JSONObject photoObject = photoArray.getJSONObject(i);
					String url = photoObject.getString("url");
					this.photos.add(url);
				}
			}
			if(object.has("image")){
				this.banner = new Banner();
				this.banner.initFromJSON(object.getJSONObject("image"));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void initFromJSON(JSONObject object){
		try {
			this.id = object.getString("id");
			this.name = object.getString("name");
			this.types = new ArrayList<Types>();
			JSONArray array = object.getJSONArray("types");
			for(int i = 0;i<array.length();i++){
				JSONObject type = (JSONObject)array.get(i);
				Types typeEntity = new Types();
				typeEntity.initFromJSON(type);
				this.types.add(typeEntity);
			}
			this.star = object.getInt("stars");
			this.cost = object.getInt("cost");
			this.taste = object.getInt("taste");
			this.env = object.getInt("env");
			this.service = object.getInt("service");
			this.address = object.getString("address");
			this.phone = object.getString("phone");
			JSONObject cityObject = object.getJSONObject("city");
			this.city = new City(cityObject.getInt("id"), cityObject.getString("name"));
			this.area = new AreaData(object.getJSONObject("area").getInt("id"), object.getJSONObject("area").getString("name"));
			this.locationData = new LocationData((float)object.getJSONObject("coordinate").getDouble("lng"), (float)object.getJSONObject("coordinate").getDouble("lat"));
			Log.e("Location", locationData.lat+" "+locationData.lng);
			JSONArray labesJsonArray = object.getJSONArray("labels");
			this.labels = new ArrayList<>();
			for(int i = 0; i<labesJsonArray.length();i++){
				String labesObject = labesJsonArray.getString(i);
				this.labels.add(labesObject);
			}
			if(object.has("items")){
				JSONArray itemsArray = object.getJSONArray("items");
				this.items = new ArrayList<Items>();
				for(int i = 0;i<itemsArray.length();i++){
					JSONObject itemObject = itemsArray.getJSONObject(i);
					Items items = new Items();
					items.initFromJSON(itemObject);
					this.items.add(items);
				}
			}

			this.specials = new ArrayList<Specials>();
			if(object.has("specials")){
				JSONArray specialsArray = object.getJSONArray("specials");
				for(int i = 0;i<specialsArray.length();i++){
					JSONObject specialObject = specialsArray.getJSONObject(i);
					Specials specials = new Specials();
					specials.initFromJson(specialObject);
					this.specials.add(specials);
				}
			}
			
			if(object.has("photos")){
				JSONArray photoArray = object.getJSONArray("photos");
				this.photos = new ArrayList<String>();
				for(int i = 0; i<photoArray.length();i++)
				{
					JSONObject photoObject = photoArray.getJSONObject(i);
					String url = photoObject.getString("url");
					this.photos.add(url);
				}
			}
			if(object.has("image")){
				this.banner = new Banner();
				this.banner.initFromJSON(object.getJSONObject("image"));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void partInitFromJSON(JSONObject object){
		try {
			this.id = object.getString("id");
			this.name = object.getString("name");
			this.types = new ArrayList<Types>();
			JSONArray array = object.getJSONArray("types");
			for(int i = 0;i<array.length();i++){
				JSONObject type = (JSONObject)array.get(i);
				Types typeEntity = new Types();
				typeEntity.initFromJSON(type);
				this.types.add(typeEntity);
			}
			this.star = object.getInt("stars");
			this.cost = object.getInt("cost");
			this.taste = object.getInt("taste");
			this.env = object.getInt("env");
			this.service = object.getInt("service");
			this.address = object.getString("address");
			this.phone = object.getString("phone");
			JSONObject cityObject = object.getJSONObject("city");
			this.city = new City(cityObject.getInt("id"), cityObject.getString("name"));
			this.area = new AreaData(object.getJSONObject("area").getInt("id"), object.getJSONObject("area").getString("name"));
			JSONArray labesJsonArray = object.getJSONArray("labels");
			this.labels = new ArrayList<>();
			for(int i = 0; i<labesJsonArray.length();i++){
				String labesObject = labesJsonArray.getString(i);
				this.labels.add(labesObject);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getId(){
		return this.id;
	}
	
	public String getName(){
		return this.name;
	}
	
	public ArrayList<Types> getTypes(){
		return this.types;
	}
	
	public int getStar(){
		return this.star;
	}
	
	public int getCost(){
		return this.cost;
	}
	
	public int getEnv(){
		return this.env;
	}
	
	public int getTaste(){
		return this.taste;
	}
	
	public int getSevice(){
		return this.service;
	}
	
	public String getAddress(){
		return this.address;
	}
	
	public String getPhoneNumber(){
		return this.phone;
	}
	
	public City getCity(){
		return this.city;
	}
	
	public AreaData getArea(){
		return this.area;
	}
	
	public ArrayList<String> getLabels(){
		return this.labels;
	}
	
	public ArrayList<Items> getItems(){
		return this.items;
	}
	
	public ArrayList<String> getPhoto(){
		return this.photos;
	}

	public void addImage(Bitmap bitmap) {
		// TODO Auto-generated method stub
		if(bitmaps==null)
			this.bitmaps = new ArrayList<>();
		this.bitmaps.add(bitmap);
	}
	public ArrayList<Bitmap> getImage()
	{
		return this.bitmaps;
	}
	
	public ArrayList<Specials> getSpecials(){
		return this.specials;
	}
	
	public LocationData getLocationData(){
		return this.locationData;
	}
	
	public Banner getBanner(){
		return this.banner;
	}
}
