package com.aifengqiang.entity;

public class ShopMessageInfo {
	public String name;
	public int distance;
	public String address;
	public String discountMessage;
	public String tokenIdString;
	public String id;
	
	public String getName(){
		return this.name;
	}
	
	public String getDistance(){
		if(distance/1000>0){
			return String.format("%.2fǧ��",((float)distance)/1000);
		}
		else
			return distance+"��";
	}
	
	public String getAddress(){
		return address;
	}
	
	public String getDiscountMessage(){
		return discountMessage;
	}
}
