package com.aifengqiang.entity;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class UserInfo {
	private String id;
	private String principalId;
	private String principalRole;
	
	public String getId(){
		return this.id;
	}
	
	public String getPrincipalId(){
		return this.principalId;
	}
	
	public String getPrincipalRole(){
		return this.principalRole;
	}
	
	public boolean getUserInfoFromJson(String responseBody){
		try{
			JSONTokener jsonParser = new JSONTokener(responseBody);
			JSONObject userInfo = (JSONObject) jsonParser.nextValue();
			this.id = userInfo.getString("id");
			JSONObject principal = userInfo.getJSONObject("principal");
			principalId = principal.getString("id");
			principalRole = principal.getString("role");
			return true;
		}
		catch(JSONException ex){
			ex.printStackTrace();
			return false;
		}
	}
	
}
