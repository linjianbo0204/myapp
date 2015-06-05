package com.aifengqiang.network;

import java.io.IOException;
import java.net.URLEncoder;

import org.apache.http.HttpConnection;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class ConnectionClient {
	public String connectionString;
	public static String connectionAddress="http://106.186.31.144:8211/api";
	public static String connectionImage="http://106.186.31.144:8211";
	
	public ConnectionClient(String address){
		connectionAddress = address;	
	}
	
	public static HttpResponse connServerForResultGet(String strUrl) { 
        // HttpGet���� 
        HttpGet httpRequest = new HttpGet(connectionAddress+strUrl); 
        String strResult = ""; 
        try { 
            // HttpClient���� 
            HttpClient httpClient = new DefaultHttpClient();
            HttpParams params = httpClient.getParams();
            HttpConnectionParams.setConnectionTimeout(params, 6000);
            HttpConnectionParams.setSoTimeout(params, 30000);
            // ���HttpResponse���� 
            HttpResponse httpResponse = httpClient.execute(httpRequest); 
            return httpResponse;
            //
            /*if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) { 
                // ȡ�÷��ص����� 
                strResult = EntityUtils.toString(httpResponse.getEntity()); 
            }*/ 
        } catch (ClientProtocolException e) { 
            //tvJson.setText("protocol error"); 
            e.printStackTrace(); 
        } catch (IOException e) { 
            //tvJson.setText("IO error"); 
            e.printStackTrace(); 
        } 
        return null; 
    }
	
	public static HttpResponse connServerForResultGetWithHeader(String strUrl, String headerName, String headerParems) { 
        // HttpGet���� 
        HttpGet httpRequest = new HttpGet(connectionAddress+strUrl); 
        String strResult = ""; 
        try { 
            // HttpClient���� 
            HttpClient httpClient = new DefaultHttpClient(); 
            HttpParams params = httpClient.getParams();
            HttpConnectionParams.setConnectionTimeout(params, 6000);
            HttpConnectionParams.setSoTimeout(params, 30000);
            httpRequest.addHeader(headerName, headerParems);
            // ���HttpResponse���� 
            HttpResponse httpResponse = httpClient.execute(httpRequest); 
            return httpResponse;
            //
            /*if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) { 
                // ȡ�÷��ص����� 
                strResult = EntityUtils.toString(httpResponse.getEntity()); 
            }*/ 
        } catch (ClientProtocolException e) { 
            //tvJson.setText("protocol error"); 
            e.printStackTrace(); 
        } catch (IOException e) { 
            //tvJson.setText("IO error"); 
            e.printStackTrace(); 
        } 
        return null; 
    }
	
	public static HttpResponse connServerForResultGetWithParams(String strUrl, HttpParams param) { 
        // HttpGet���� 
        HttpGet httpRequest = new HttpGet(connectionAddress+strUrl); 
        String strResult = ""; 
        try { 
            // HttpClient���� 
            HttpClient httpClient = new DefaultHttpClient();
            HttpParams params = httpClient.getParams();
            HttpConnectionParams.setConnectionTimeout(params, 6000);
            HttpConnectionParams.setSoTimeout(params, 30000);
            httpRequest.setParams(param);
            // ���HttpResponse���� 
            HttpResponse httpResponse = httpClient.execute(httpRequest); 
            return httpResponse;
            //
            /*if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) { 
                // ȡ�÷��ص����� 
                strResult = EntityUtils.toString(httpResponse.getEntity()); 
            }*/ 
        } catch (ClientProtocolException e) { 
            //tvJson.setText("protocol error"); 
            e.printStackTrace(); 
        } catch (IOException e) { 
            //tvJson.setText("IO error"); 
            e.printStackTrace(); 
        } 
        return null; 
    }
	
	public static HttpResponse connServerForResultPost(String strUrl, JSONObject param) { 
        // HttpPost���� 
        HttpPost httpRequest = new HttpPost(connectionAddress+strUrl);
        String strResult = ""; 
        try { 
            // HttpClient���� 
            HttpClient httpClient = new DefaultHttpClient();
            HttpParams params = httpClient.getParams();
            HttpConnectionParams.setConnectionTimeout(params, 6000);
            HttpConnectionParams.setSoTimeout(params, 30000);
            httpRequest.setHeader(HTTP.CONTENT_TYPE, "application/json");
            String encoderJson = param.toString();
            StringEntity se = new StringEntity(encoderJson,"UTF-8");
            Log.e("HttpRequest",""+encoderJson);
            httpRequest.setEntity(se);
            // ���HttpResponse���� 
            HttpResponse httpResponse = httpClient.execute(httpRequest);
            Log.e("HttpConnect",httpResponse.getStatusLine().getStatusCode()+"");
            return httpResponse;
            /*if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) { 
                // ȡ�÷��ص����� 
            	strResult = EntityUtils.toString(httpResponse.getEntity());
                return strResult;
            } */
        } catch (ClientProtocolException e) { 
            //tvJson.setText("protocol error"); 
            e.printStackTrace(); 
        } catch (IOException e) { 
            //tvJson.setText("IO error"); 
            e.printStackTrace(); 
        } 
        return null; 
    }
	
	public static HttpResponse connServerForResultPostWithHeader(String strUrl, JSONObject param, String paramName, String paramString) { 
        // HttpPost���� 
        HttpPost httpRequest = new HttpPost(connectionAddress+strUrl);
        String strResult = ""; 
        try { 
            // HttpClient���� 
            HttpClient httpClient = new DefaultHttpClient();
            HttpParams params = httpClient.getParams();
            HttpConnectionParams.setConnectionTimeout(params, 6000);
            HttpConnectionParams.setSoTimeout(params, 30000);
            httpRequest.addHeader(paramName, paramString);
            if(param!=null){
            	httpRequest.setHeader(HTTP.CONTENT_TYPE, "application/json");
            	String encoderJson = param.toString();
            	StringEntity se = new StringEntity(encoderJson,"UTF-8");
                Log.e("HttpRequest",""+encoderJson);
                httpRequest.setEntity(se);
            }
            // ���HttpResponse���� 
            HttpResponse httpResponse = httpClient.execute(httpRequest);
            Log.e("HttpConnect",httpResponse.getStatusLine().getStatusCode()+"");
            return httpResponse;
            /*if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) { 
                // ȡ�÷��ص����� 
            	strResult = EntityUtils.toString(httpResponse.getEntity());
                return strResult;
            } */
        } catch (ClientProtocolException e) { 
            //tvJson.setText("protocol error"); 
            e.printStackTrace(); 
        } catch (Exception e) { 
            //tvJson.setText("IO error"); 
            e.printStackTrace(); 
        } 
        return null; 
    }
	
	public static HttpResponse connServerForResultPut(String strUrl, JSONObject param) { 
        // HttpPost���� 
        HttpPut httpRequest = new HttpPut(connectionAddress+strUrl);
        String strResult = ""; 
        try { 
            // HttpClient���� 
            HttpClient httpClient = new DefaultHttpClient();
            HttpParams params = httpClient.getParams();
            HttpConnectionParams.setConnectionTimeout(params, 6000);
            HttpConnectionParams.setSoTimeout(params, 30000);
            StringEntity se = new StringEntity(param.toString(),"UTF-8");
            httpRequest.setEntity(se);
            // ���HttpResponse���� 
            HttpResponse httpResponse = httpClient.execute(httpRequest); 
            return httpResponse;
            /*if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) { 
                // ȡ�÷��ص����� 
            	strResult = EntityUtils.toString(httpResponse.getEntity());
                return strResult;
            }*/ 
        } catch (ClientProtocolException e) { 
            //tvJson.setText("protocol error"); 
            e.printStackTrace(); 
        } catch (IOException e) { 
            //tvJson.setText("IO error"); 
            e.printStackTrace(); 
        } 
        return null; 
    }
	
	public static HttpResponse connServerForResultPutWithHeader(String strUrl, JSONObject param,String headerName, String headerParems) { 
        // HttpPost���� 
        HttpPut httpRequest = new HttpPut(connectionAddress+strUrl);
        String strResult = ""; 
        try { 
            // HttpClient���� 
            HttpClient httpClient = new DefaultHttpClient();
            HttpParams params = httpClient.getParams();
            HttpConnectionParams.setConnectionTimeout(params, 6000);
            HttpConnectionParams.setSoTimeout(params, 30000);
            httpRequest.setHeader(HTTP.CONTENT_TYPE, "application/json");
            httpRequest.addHeader(headerName, headerParems);
            StringEntity se = new StringEntity(param.toString(),"UTF-8");
            httpRequest.setEntity(se);
            // ���HttpResponse���� 
            HttpResponse httpResponse = httpClient.execute(httpRequest); 
            return httpResponse;
            /*if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) { 
                // ȡ�÷��ص����� 
            	strResult = EntityUtils.toString(httpResponse.getEntity());
                return strResult;
            }*/ 
        } catch (ClientProtocolException e) { 
            //tvJson.setText("protocol error"); 
            e.printStackTrace(); 
        } catch (IOException e) { 
            //tvJson.setText("IO error"); 
            e.printStackTrace(); 
        } 
        return null; 
    }
	
	//�ж������Ƿ�����
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
	
	//�ж�wifi�Ƿ�����
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
	
	//�ж��Ƿ��ֻ�ͨ����������
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
	
	//�����������״̬
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
