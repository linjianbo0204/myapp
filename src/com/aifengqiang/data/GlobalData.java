package com.aifengqiang.data;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.util.EncodingUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.aifengqiang.entity.AreaData;
import com.aifengqiang.entity.City;
import com.aifengqiang.entity.EventInfo;
import com.aifengqiang.entity.Flavor;
import com.aifengqiang.entity.LocationData;
import com.aifengqiang.entity.RestaurantInfo;
import com.aifengqiang.entity.UserInfo;

import android.R.color;
import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.util.Log;

public class GlobalData {
	private static GlobalData globalData;
	private ArrayList<Bitmap> mainGalleryImage;
	private float scale;
	private ArrayList<Flavor> flavors;
	private ArrayList<City> cities;
	private HashMap<Integer, HashMap<String, ArrayList<AreaData>>> area;
	private String currentOrderId;
	private ArrayList<String> orderRestaurantsName;
	private RestaurantInfo restaurantDetail;
	private City locationCity;
	private String userId;
	private ArrayList<EventInfo> eventsInfo;
	private int width;
	private int height;
	
	private GlobalData(){
		mainGalleryImage = new ArrayList<Bitmap>();
	}
	
	public static GlobalData getIntance(){
		if(globalData == null)
			globalData = new GlobalData();
		return globalData;
	}
	
	public Bitmap getMainGalleryImageByIndex(int index){
		if(mainGalleryImage.size()==0)
			loadMainGalleryImage();
		if(index < mainGalleryImage.size())
			return mainGalleryImage.get(index);
		else
			return null;
	}
	
	public ArrayList<Bitmap> getMainGalleryImage(){
		if(mainGalleryImage.size()==0)
			loadMainGalleryImage();
		return mainGalleryImage;
	}
	
	public void loadMainGalleryImage(){
		
	}
	
	public String getId(Context context){
		if(userId!=null&&userId!=""){
			return userId;
		}
		else{
			try {
				FileInputStream fis = context.openFileInput("user.info");
				byte[] userString = new byte[fis.available()];
				fis.read(userString);
				String string = EncodingUtils.getString(userString, "UTF-8");
				if(string.length()>1){
					UserInfo userInfo = new UserInfo();
					userInfo.getUserInfoFromJson(string);
					this.userId = userInfo.getId();
					fis.close();
					return this.userId;
				}
				else {
					fis.close();
					return null;
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}
	}
	
	public void setId(String name){
		this.userId = name;
	}
	
	public float getScale(){
		return scale;
	}
	
	public void setScale(float a){
		this.scale = a;
	}
	
	public void setFlavors(ArrayList<Flavor> flavors){
		this.flavors = flavors;
	}
	
	public ArrayList<Flavor> getFlavors(){
		return this.flavors;
	}
	
	public void setCity(ArrayList<City> cities){
		this.cities = cities;
	}
	
	public ArrayList<City> getCities(Context mContext){
		if(cities==null||cities.size()==0){
			try {
				FileInputStream fis = mContext.openFileInput("city.info");
				byte[] readBytes = new byte[fis.available()];
				fis.read(readBytes);
				String cityString = EncodingUtils.getString(readBytes, "UTF-8");
				fis.close();
				JSONTokener token = new JSONTokener(cityString);
				JSONArray array;
				ArrayList<City> cities = new ArrayList<City>();
				try {
					array = (JSONArray)token.nextValue();
					for(int i = 0; i<array.length();i++){
						JSONObject object = array.getJSONObject(i);
						int id = object.getInt("id");
						String name = object.getString("name");
						City city = new City(id, name);
						cities.add(city);
					}
					this.cities = cities;
				}
				catch(JSONException e){
					e.printStackTrace();
				}
			} catch ( IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return this.cities;
	}
	
	public void setArea(HashMap<Integer, HashMap<String, ArrayList<AreaData>>> aMap){
		this.area = aMap;
	}
	
	public HashMap<Integer, HashMap<String, ArrayList<AreaData>>> getArea(Context context){
		if(area==null||area.size()==0){
			try {
				FileInputStream fis = context.openFileInput("area.info");
				byte[] readBytes = new byte[fis.available()];
				fis.read(readBytes);
				String areaString = EncodingUtils.getString(readBytes, "UTF-8");
				Log.d("Area",""+areaString);
				fis.close();
				JSONTokener token = new JSONTokener(areaString);
				JSONArray array;
				HashMap<Integer, HashMap<String, ArrayList<AreaData>>> area = new HashMap<Integer, HashMap<String, ArrayList<AreaData>>>();
				try {
					array = (JSONArray)token.nextValue();
					for(int i = 0; i<array.length();i++){
						JSONObject object = array.getJSONObject(i);
						JSONObject cityInfObject = object.getJSONObject("city");
						int cityId = cityInfObject.getInt("id");
						HashMap<String, ArrayList<AreaData>> daData = area.get(cityId);
						if(daData==null){
							JSONObject districtObject = object.getJSONObject("district");
							String districtName = districtObject.getString("name");
							ArrayList<AreaData> areaDatas = new ArrayList<AreaData>();
							daData = new HashMap<>();
							AreaData areaData = new AreaData();
							areaData.initFromJSON(object);
							areaDatas.add(areaData);
							daData.put(districtName, areaDatas);
							area.put(cityId, daData);
						}
						else{
							JSONObject districtObject = object.getJSONObject("district");
							String districtName = districtObject.getString("name");
							if(daData.get(districtName)==null){
								ArrayList<AreaData> areaDatas = new ArrayList<AreaData>();
								AreaData areaData = new AreaData();
								areaData.initFromJSON(object);
								areaDatas.add(areaData);
								daData.put(districtName, areaDatas);
							}
							else{
								AreaData areaData = new AreaData();
								areaData.initFromJSON(object);
								area.get(cityId).get(districtName).add(areaData);
							}
						}
					}
					this.area = area;
				}
				catch(JSONException e){
					e.printStackTrace();
				}
			} catch ( IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return this.area;
	}
	
	public void setCurrentOrderId(String id){
		this.currentOrderId = id;
	}
	
	public String getCurrentOrderId(){
		return this.currentOrderId;
	}
	
	public void setRestaurants(ArrayList<String> orderRestaurants){
		this.orderRestaurantsName = orderRestaurants;
	}
	
	public ArrayList<String> getRestaurants(){
		return this.orderRestaurantsName;
	}
	
	public void setRestaurant(RestaurantInfo res){
		this.restaurantDetail = res;
	}
	
	public RestaurantInfo getRestaurantInfo(){
		return this.restaurantDetail;
	}

	public City getLocationCity() {
		// TODO Auto-generated method stub
		if(locationCity != null)
			return locationCity;
		else 
			return null;
	}

	
	public void setLocationCity(City city){
		this.locationCity = city;
	}
	
	public void setLocationCity(String city) {
		// TODO Auto-generated method stub
		locationCity = new City(-1, city);
		if(cities!=null&&city!=null){
			for(City city2 : cities){
				if(city.contains(city2.name)){
					locationCity.id= city2.id;
					locationCity.name = city2.name;
				}
			}
		}
	}

	public void setEventInfos(ArrayList<EventInfo> eventInfos) {
		// TODO Auto-generated method stub
		this.eventsInfo = eventInfos;
	}

	public ArrayList<EventInfo> getEventInfos() {
		// TODO Auto-generated method stub
		return this.eventsInfo;
	}
	
	public void setWidthAndHeight(int width, int height){
		this.width = width;
		this.height = height;
	}
	
	public int getWidth(){
		return this.width;
	}
	
	public int getHeight(){
		return this.height;
	}

}
