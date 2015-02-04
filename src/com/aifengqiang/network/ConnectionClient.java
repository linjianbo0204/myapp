package com.aifengqiang.network;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionClient {
	public String connectionString;
	public static String connectionAddress="http://106.186.31.144:8080/api/";
	
	public ConnectionClient(String address){
		connectionAddress = address;	
	}
	
	public static String connServerForResultGet(String strUrl) { 
        // HttpGet对象 
        HttpGet httpRequest = new HttpGet(connectionAddress+strUrl); 
        String strResult = ""; 
        try { 
            // HttpClient对象 
            HttpClient httpClient = new DefaultHttpClient(); 
            // 获得HttpResponse对象 
            HttpResponse httpResponse = httpClient.execute(httpRequest); 
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) { 
                // 取得返回的数据 
                strResult = EntityUtils.toString(httpResponse.getEntity()); 
            } 
        } catch (ClientProtocolException e) { 
            //tvJson.setText("protocol error"); 
            e.printStackTrace(); 
        } catch (IOException e) { 
            //tvJson.setText("IO error"); 
            e.printStackTrace(); 
        } 
        return strResult; 
    }
	
	public static String connServerForResultPost(String strUrl, JSONObject param) { 
        // HttpPost对象 
        HttpPost httpRequest = new HttpPost(connectionAddress+strUrl);
        String strResult = ""; 
        try { 
            // HttpClient对象 
            HttpClient httpClient = new DefaultHttpClient();
            StringEntity se = new StringEntity(param.toString());
            httpRequest.setEntity(se);
            // 获得HttpResponse对象 
            HttpResponse httpResponse = httpClient.execute(httpRequest); 
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) { 
                // 取得返回的数据 
            	strResult = EntityUtils.toString(httpResponse.getEntity());
                return strResult;
            } 
        } catch (ClientProtocolException e) { 
            //tvJson.setText("protocol error"); 
            e.printStackTrace(); 
        } catch (IOException e) { 
            //tvJson.setText("IO error"); 
            e.printStackTrace(); 
        } 
        return strResult; 
    }
	
	public static String connServerForResultPut(String strUrl, JSONObject param) { 
        // HttpPost对象 
        HttpPut httpRequest = new HttpPut(connectionAddress+strUrl);
        String strResult = ""; 
        try { 
            // HttpClient对象 
            HttpClient httpClient = new DefaultHttpClient();
            StringEntity se = new StringEntity(param.toString());
            httpRequest.setEntity(se);
            // 获得HttpResponse对象 
            HttpResponse httpResponse = httpClient.execute(httpRequest); 
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) { 
                // 取得返回的数据 
            	strResult = EntityUtils.toString(httpResponse.getEntity());
                return strResult;
            } 
        } catch (ClientProtocolException e) { 
            //tvJson.setText("protocol error"); 
            e.printStackTrace(); 
        } catch (IOException e) { 
            //tvJson.setText("IO error"); 
            e.printStackTrace(); 
        } 
        return strResult; 
    }
	
	//判断网络是否连接
	public static boolean isNetworkConnected(Context context){
		if(context != null){
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
			if(mNetworkInfo != null){
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}
	
	//判断wifi是否连接
	public static boolean isWifiConnected(Context context){
		if(context != null){
			ConnectivityManager mConnectivityNanager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mWifiNetWorkInfo = mConnectivityNanager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if(mWifiNetWorkInfo != null){
				return mWifiNetWorkInfo.isAvailable();
			}
		}
		return false;
	}
	
	//判断是否手机通信网络连接
	public static boolean isMobileConnected(Context context){
		if(context != null){
			ConnectivityManager mConnectivityNanager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mMobileNetWorkInfo = mConnectivityNanager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			if(mMobileNetWorkInfo != null){
				return mMobileNetWorkInfo.isAvailable();
			}
		}
		return false;
	}
	
	//获得网络连接状态
	public static int getConnectedType(Context context){
		if(context != null){
			ConnectivityManager mConnectivityNanager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetWorkInfo = mConnectivityNanager.getActiveNetworkInfo();
			if(mNetWorkInfo != null && mNetWorkInfo.isAvailable()){
				return mNetWorkInfo.getType();
			}
		}
		return -1;
	}
	
}
