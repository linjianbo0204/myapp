package com.aifengqiang.entity;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.aifengqiang.main.R;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class CommentEntity {
	private Bitmap picBitmap;
	private String name;
	private int star;
	private int taste;
	private int env;
	private int sev;
	private float cost;
	private String dateString;
	private String comment;
	private String highlight;
	private String id;
	private Order order;
	private Customer customer;
	
	public CommentEntity(){
		
	}
	
	public void init(Resources res){
		picBitmap = BitmapFactory.decodeResource(res, R.drawable.touxiang);
		name ="微笑蓝天";
		star = 3;
		taste = 3;
		env = 3;
		sev = 3;
		cost = 128f;
		dateString = "2015-01-19";
		comment = "味道不错，下次还来，味道不错，下次还来，味道不错，下次还来，味道不错，下次还来";
		highlight="宫保鸡丁，松鼠桂鱼";
	}
	
	public void initFromString(String entity){
		JSONTokener tokener = new JSONTokener(entity);
		try {
			JSONObject object = (JSONObject) tokener.nextValue();
			this.id = object.getString("id");
			this.order = new Order();
			this.order.id = object.getJSONObject("order").getString("id");
			this.order.number = object.getJSONObject("order").getString("number");
			this.customer = new Customer();
			this.customer.id = object.getJSONObject("customer").getString("id");
			this.customer.mobile = object.getJSONObject("customer").getString("mobile");
			this.star = object.getInt("stars");
			this.cost = object.getInt("cost");
			this.taste = object.getInt("taste");
			this.env = object.getInt("env");
			this.sev = object.getInt("sev");
			this.comment = object.getString("comment");
			this.dateString = object.getString("timestamp");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void initFromJSON(JSONObject object){
		try {
			this.id = object.getString("id");
			this.order = new Order();
			this.order.id = object.getJSONObject("order").getString("id");
			this.order.number = object.getJSONObject("order").getString("number");
			this.customer = new Customer();
			this.customer.id = object.getJSONObject("customer").getString("id");
			this.customer.mobile = object.getJSONObject("customer").getString("mobile");
			this.star = object.getInt("stars");
			this.cost = object.getInt("cost");
			this.taste = object.getInt("taste");
			this.env = object.getInt("env");
			this.sev = object.getInt("service");
			this.comment = object.getString("comment");
			this.dateString = object.getString("timestamp");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getName(){
		return this.name;
	}
	
	public Bitmap getPic(){
		return picBitmap;
	}
	
	public int getStar(){
		return this.star;
	}
	
	public int getTaste(){
		return this.taste;
	}
	
	public int getEnv(){
		return this.env;
	}
	
	public int getSev(){
		return this.sev;
	}
	public String getScore(){
		return "味道:"+taste+" 环境:"+env+" 服务:"+sev;
	}
	
	public String getCost(){
		return "<div>人均：<span><font color=\"#f88727\">"+cost+"</span>元</div>";
	}
	
	public String getHighight(){
		return "推荐菜："+(highlight==null?"":highlight);
	}
	
	public String getComment(){
		return this.comment;
	}
	
	public String getDate(){
		return this.dateString;
	}
	
	public Order getOrder(){
		return this.order;
	}
	
	public Customer getCustomer(){
		return this.customer;
	}
}
