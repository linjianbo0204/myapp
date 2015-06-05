package com.aifengqiang.entity;

public class Customer {
	public String id;
	public String mobile;
	public String token;
	
	public Customer(){
		
	}
	public Customer(String id, String mobile, String token){
		this.id = id;
		this.mobile = mobile;
		this.token = token;
	}
}
