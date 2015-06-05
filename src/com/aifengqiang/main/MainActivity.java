package com.aifengqiang.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.aifengqiang.data.GlobalData;
import com.aifengqiang.entity.AreaData;
import com.aifengqiang.entity.City;
import com.aifengqiang.entity.EventInfo;
import com.aifengqiang.entity.Flavor;
import com.aifengqiang.entity.OrderAllResponseInfo;
import com.aifengqiang.main.R;
import com.aifengqiang.main.LocationCityActivity.MyLocationListener;
import com.aifengqiang.main.MainFragment.EventPagerAdapter;
import com.aifengqiang.main.R.drawable;
import com.aifengqiang.network.ConnectionClient;
import com.aifengqiang.ui.FoodKindListView;
import com.aifengqiang.ui.GalleryAdapter;
import com.aifengqiang.ui.MenuView;
import com.aifengqiang.ui.NavigationButton;
import com.aifengqiang.ui.NavigationView;
import com.aifengqiang.ui.WaitingDialog;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;

import android.R.integer;
import android.R.transition;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.usage.UsageEvents.Event;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.graphics.AvoidXfermode.Mode;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.camera2.CameraCharacteristics.Key;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnLayoutChangeListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainActivity extends Activity{
	public int activityHeight;
	public int navViewHeight;
	public int menuViewHeight;
	MainActivity point = this;
	private FragmentManager fManager;
	private FragmentTransaction fTransaction;
	private MainFragment fragment;
	private NavigationView nv;
	private MenuView mv;
	private Fragment current=null;
	private FrameLayout frameLayout;
	private ArrayList<EventInfo> eventInfos;
	static public Handler handler;
	private Dialog ad;
	private boolean orderWaiting;
	public LocationClient mLocationClient = null;
	public BDLocationListener myListener = new MyLocationListener();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		nv = (NavigationView)findViewById(R.id.main_nav_view);
		
		DisplayMetrics metric = new DisplayMetrics();
		this.getWindowManager().getDefaultDisplay().getMetrics(metric);
		float scale = metric.density;
		GlobalData.getIntance().setScale(scale);
		
		mv = (MenuView)findViewById(R.id.main_menu_view);

		mv.setItem(0, R.drawable.home_pre, 1, "首页", 0);
		mv.setItem(1, R.drawable.order_nor, 1, "订单", 0);
		mv.setItem(2, R.drawable.icon_tab_qiang, 2, "抢我",R.drawable.tab_qiang_bg_nor);
		mv.setItem(3, R.drawable.me_nor, 1, "我的",0);
		mv.setItem(4, R.drawable.more_nor, 1, "更多",0);
		
		frameLayout = (FrameLayout)findViewById(R.id.main_fragment);
		orderWaiting = false;
		
		GlobalData.getIntance().setLocationCity(new City(1, "上海"));
		MenuView.MenuViewListener mnl = new MenuView.MenuViewListener() {

			@Override
			public void OnMenuViewClick(int id) {
				// TODO Auto-generated method stub
				Fragment newFragment = null;
				if(id == 0){
					if(fragment.isHidden()){
						resetMenu(0);
						fManager = getFragmentManager();
						fTransaction = fManager.beginTransaction();
						fTransaction.remove(current);
						fTransaction.show(fragment);
						current = fragment;
						fTransaction.commit();
					}
					changeLocation();
					nv.setRightButton(getString(R.string.show),NavigationButton.NAVIGATIONIMAGENONE, 0);
					nv.setTitle(getString(R.string.app_title));

					NavigationView.NavigationViewListener nvl = new NavigationView.NavigationViewListener() {
						@Override
						public void OnNavigationButtonClick(int id) {
							// TODO Auto-generated method stub
							if(id == NavigationView.NAVIGATION_BUTTON_LEFT){
								Intent it = new Intent(point, LocationCityActivity.class);
								startActivityForResult(it, 1004);
							}
							else
							{
								Intent it = new Intent(point, GuideActivity.class);
								startActivity(it);
							}
						}
					};
					nv.setNavigationViewListener(nvl);
					return;
				}
				else if(id==1){
					if(current instanceof OrderListFragment)
						return;
					if(GlobalData.getIntance().getId(point)==null){
						Intent it = new Intent(point, LoginActivity.class);
						startActivityForResult(it, 1004);
					}
					else{
						resetMenu(1);
						nv.clearButton(NavigationView.NAVIGATION_BUTTON_LEFT);
						nv.clearButton(NavigationView.NAVIGATION_BUTTON_RIGHT);
						newFragment = new OrderListFragment();
					}
					//Intent it = new Intent(point, OrderListActivity.class);
					//startActivity(it);
				}
				else if(id==2)
				{
					if(!orderWaiting){
						orderWaiting = true;
						ad = new WaitingDialog(point, R.style.MyDialog);
						ad.show();
						new Thread(){
							@Override
							public void run(){
								getCurrentOrder();
							}
						}.start();
					}
					return;/*
					int version = Integer.valueOf(android.os.Build.VERSION.SDK);
					if(version >= 5) {
						overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
						//overridePendingTransition(android.R.anim.decelerate_interpolator,android.R.anim.decelerate_interpolator);    
						//overridePendingTransition(android.R.anim.overshoot_interpolator,android.R.anim.linear_interpolator);  
					}*/
				}
				else if(id==3){
					resetMenu(3);
					if(current instanceof UserCenterFragment)
						return;
					newFragment = new UserCenterFragment();

					changeLocation();
					nv.clearButton(NavigationView.NAVIGATION_BUTTON_RIGHT);
					nv.setTitle(getString(R.string.app_title));
					
					NavigationView.NavigationViewListener nvl = new NavigationView.NavigationViewListener() {
						@Override
						public void OnNavigationButtonClick(int id) {
							// TODO Auto-generated method stub
							if(id == NavigationView.NAVIGATION_BUTTON_LEFT){
								Intent it = new Intent(point, LocationCityActivity.class);
								startActivityForResult(it, 1004);
							}
						}
					};
					nv.setNavigationViewListener(nvl);
					//Intent it = new Intent(point, UserCenterActivity.class);
					//startActivity(it);
				}
				else if(id==4){
					resetMenu(4);
					if(current instanceof MoreFragment)
						return;
					nv.clearButton(NavigationView.NAVIGATION_BUTTON_LEFT);
					nv.clearButton(NavigationView.NAVIGATION_BUTTON_RIGHT);
					newFragment = new MoreFragment();
					//Intent it = new Intent(point, MoreActivity.class);
					//startActivity(it);
				}
				if(newFragment != null){
					fManager = getFragmentManager();
					fTransaction = fManager.beginTransaction();
					if(current == fragment ){
						fTransaction.hide(fragment);
						fTransaction.add(R.id.main_fragment, newFragment);
					}
					else{
						fTransaction.remove(current);
						fTransaction.add(R.id.main_fragment,newFragment);
					}
					current = newFragment;
					fTransaction.commit();
				}
			}
		};
		mv.setMenuViewListener(mnl);
		setDefaultFragment();
		current = fragment;

		handler = new Handler(){
			@Override
			public void handleMessage(Message m){
				super.handleMessage(m);
				switch (m.what) {
				case 0:
					mv.changeCount(m.arg1);
					break;
				case 11:
					((MainFragment)fragment).setAdapter();
					break;
				case 12:
					Log.d("Flavor","MainActivity "+"refresh");
					//((MainFragment)fragment).setFlavorAdapter();
					break;
				case 3:
					showDialog("网络连接错误，请检查！");
					break;
				case 4:
					showDialog("请求错误！");
					break;
				case 5:
					showDialog("服务器错误！");
					break;
				case 6:
					changeLocation();
					break;
				case 10:
					startOrderFragment();
					break;
				case 15:
					if(ad!=null&&ad.isShowing()){
						ad.dismiss();
					}
					orderWaiting = false;
					Intent it = new Intent(point, OrderActivity.class);
					startActivity(it);
					break;
				case 16:
					if(ad!=null&&ad.isShowing()){
						ad.dismiss();
					}
					orderWaiting = false;
					Log.e("Order", m.arg1+"");
					Intent it1 = new Intent(point, WaitingActivity.class);
					it1.putExtra("seconds", m.arg1);
					it1.putExtra("request", m.arg2);
					startActivity(it1);
					break;
				default:
					break;
				}
			}
		};

		downloadAllMessage();

		saveIcon();
		
		startLocation();
	}

	public void setDefaultFragment(){
		changeLocation();
		nv.setRightButton(getString(R.string.show),NavigationButton.NAVIGATIONIMAGENONE, 0);
		nv.setTitle(getString(R.string.app_title));

		NavigationView.NavigationViewListener nvl = new NavigationView.NavigationViewListener() {
			@Override
			public void OnNavigationButtonClick(int id) {
				// TODO Auto-generated method stub
				if(id == NavigationView.NAVIGATION_BUTTON_LEFT){
					Intent it = new Intent(point, LocationCityActivity.class);
					startActivityForResult(it, 1004);
				}
				else
				{
					Intent it = new Intent(point, GuideActivity.class);
					startActivity(it);
				}
			}
		};
		nv.setNavigationViewListener(nvl);
		
		fManager = getFragmentManager();
		fTransaction = fManager.beginTransaction();
		fragment = new MainFragment();
		fTransaction.add(R.id.main_fragment, fragment);	
		fTransaction.commit();
	}
	
	private void changeLocation(){
		String locationString = getSharedPreferences("AFQ", Activity.MODE_PRIVATE).getString("city", "定位");
		nv.setLeftButton(locationString, NavigationButton.NAVIGATIONIMAGERIGHT, R.drawable.icon_back_nor_down);
	}
	
	private void startLocation(){
		mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
	    mLocationClient.registerLocationListener( myListener );    //注册监听函数
	    LocationClientOption option = new LocationClientOption();

	    option.setOpenGps(true);

	    option.setCoorType("bd09ll");

	    option.setScanSpan(2000);

	    option.setLocationMode(LocationMode.Hight_Accuracy);

	    option.setIsNeedAddress(true);

	    option.setNeedDeviceDirect(true);

	    mLocationClient.setLocOption(option);

	    mLocationClient.start();

	    mLocationClient.requestLocation();
	}
	
	public void getCurrentOrder(){
		Message message = new Message();
		boolean networkConnected = ConnectionClient.isNetworkConnected(point);
		if(networkConnected){
			HttpResponse response = ConnectionClient.connServerForResultGetWithHeader("/customer/orders/current", "X-Customer-Token",GlobalData.getIntance().getId(point));
			if(response!=null){
				String result = "";
				try {
					result = EntityUtils.toString(response.getEntity(),"UTF-8");
					Log.e("HttpConnect",result + " " +GlobalData.getIntance().getId(point));
				} catch (ParseException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				int status = response.getStatusLine().getStatusCode();
				if(status==HttpStatus.SC_OK){
					JSONTokener token = new JSONTokener(result);
					JSONObject object;
					try {
						object = (JSONObject)token.nextValue();
						JSONArray jsonArray = object.getJSONArray("requests");
						String id  = object.getString("id");
						GlobalData.getIntance().setCurrentOrderId(id);
						String timeString = object.getString("timestamp");
						String updateTime = object.getString("updated");
						String[] splitStrings = timeString.split("T");
						String[] splitUpdateTimes = updateTime.split("T");
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						Log.d("Order",timeString);
						Log.d("Updated time", updateTime);
						Date date = new Date();
						Date newDate = sdf.parse(splitStrings[0]+" "+splitStrings[1]);
						Date updateDate = sdf.parse(splitUpdateTimes[0]+" "+splitUpdateTimes[1]);
						long seconds = date.getTime()-newDate.getTime();
						long updateSeconds = date.getTime()-updateDate.getTime();
						Log.d("Order","Result "+seconds);
						message.arg2 = jsonArray.length();
						if(seconds<0){
							message.what = 15;
							handler.sendMessage(message);
							return;
						}
						String statusString = object.getString("status");
						Log.d("Status",statusString);
						if(!(statusString.equals("SENT")||statusString.equals("ACCEPTED"))){
							message.what = 15;
							handler.sendMessage(message);
							return;
						}
						else if(updateSeconds!=seconds && updateSeconds<300000){
							message.what = 16;
							message.arg1 = (int) ((updateSeconds/1000)+180);
							handler.sendMessage(message);
							return;
						}
						else if(updateSeconds==seconds && seconds<180000){
							message.what = 16;
							message.arg1 = (int)(seconds/1000);
							handler.sendMessage(message);
							return;
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (java.text.ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		message.what = 15;
		handler.sendMessage(message);
		return;
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig){
		super.onConfigurationChanged(newConfig);
		//Log.d("Configuration","Changed");
	}
	
	@Override
	protected void onDestroy(){
		super.onDestroy();
		if(ad!=null&&ad.isShowing())
			ad.dismiss();
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCost, Intent it){
		if(requestCode==1004&&resultCost==1005){
			changeLocation();
			//Log.d("Result"," "+locationString);
		}
		if(requestCode ==1004&&resultCost==1001){
			String id = it.getStringExtra("TokenId");
			nv.clearButton(NavigationView.NAVIGATION_BUTTON_LEFT);
			nv.clearButton(NavigationView.NAVIGATION_BUTTON_RIGHT);
			Fragment newFragment = new OrderListFragment();
			fManager = getFragmentManager();
			fTransaction = fManager.beginTransaction();
			if(current == fragment ){
				fTransaction.hide(fragment);
				fTransaction.add(R.id.main_fragment, newFragment);
			}
			else{
				fTransaction.remove(current);
				fTransaction.add(R.id.main_fragment,newFragment);
			}
			current = newFragment;
			fTransaction.commit();
			resetMenu(1);
		}
	}

	public void resetMenu(int index){
		mv.changeBg(0, R.drawable.home_nor);
		mv.changeBg(1, R.drawable.order_nor);
		mv.changeBg(3, R.drawable.me_nor);
		mv.changeBg(4, R.drawable.more_nor);
		
		switch (index) {
		case 0:
			mv.changeBg(index, R.drawable.home_pre);
			break;
		case 1:
			mv.changeBg(index, R.drawable.order_prer);
			break;
		case 3:
			mv.changeBg(index, R.drawable.me_pre);
			break;
		case 4:
			mv.changeBg(index, R.drawable.more_pre);
			break;
		default:
			break;
		}
		
	}
	
	public void showDialog(String text){
		if(ad!=null&&ad.isShowing())
			ad.dismiss();
		ad = new Dialog(point,R.style.MyDialog);
		ad.setCancelable(false);
		ad.setCanceledOnTouchOutside(false);
		ad.setContentView(R.layout.dialog_login);
		TextView tv = (TextView)ad.findViewById(R.id.dialog_text);
		tv.setText(text);
		Button btn = (Button)ad.findViewById(R.id.dialog_button_one_sure);
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ad.dismiss();
			}
		});
		ad.show();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event){
		if(keyCode == KeyEvent.KEYCODE_BACK){
			if(ad!=null&&ad.isShowing())
				ad.dismiss();
			ad = new Dialog(point,R.style.MyDialog);
			ad.setContentView(R.layout.dialog_confirm_cancel);
			TextView tv = (TextView)ad.findViewById(R.id.dialog_text_view);
			tv.setText("确定要退出系统？");
			Button btnConfirm = (Button)ad.findViewById(R.id.dialog_confirm);
			btnConfirm.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					ad.dismiss();
					finish();
				}
			});
			Button btnCancel = (Button)ad.findViewById(R.id.dialog_cancel);
			btnCancel.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					ad.dismiss();
				}
			});
			ad.show();
		}
		return false;
	}
	
	public void startOrderFragment(){
		Fragment newFragment = null;
		if(current instanceof OrderListFragment)
			return;
		if(GlobalData.getIntance().getId(point)==null){
			Intent it = new Intent(point, LoginActivity.class);
			startActivityForResult(it, 1004);
		}
		else{
			resetMenu(1);
			nv.clearButton(NavigationView.NAVIGATION_BUTTON_LEFT);
			nv.clearButton(NavigationView.NAVIGATION_BUTTON_RIGHT);
			newFragment = new OrderListFragment();
		}
		if(newFragment != null){
			fManager = getFragmentManager();
			fTransaction = fManager.beginTransaction();
			if(current == fragment ){
				fTransaction.hide(fragment);
				fTransaction.add(R.id.main_fragment, newFragment);
			}
			else{
				fTransaction.remove(current);
				fTransaction.add(R.id.main_fragment,newFragment);
			}
			current = newFragment;
			fTransaction.commit();
		}
	}
	
	@Override
	public void onResume(){
		super.onResume();
		SharedPreferences preferences = getSharedPreferences("Order", MODE_PRIVATE);
		boolean ordered = preferences.getBoolean("Ordered", false);
		if(ordered){
			startOrderFragment();
			new Thread(){
				@Override
				public void run(){
					initOrderList();
				}
			}.start();
		}
		Editor editor = preferences.edit();
		editor.putBoolean("Ordered", false);
		editor.commit();
		
	}
	
	public void initOrderList(){
		Message m = new Message();
		boolean networkConnected = ConnectionClient.isNetworkConnected(point);
		if(networkConnected){
			HttpResponse response = ConnectionClient.connServerForResultGetWithHeader("/customer/orders", "X-Customer-Token",GlobalData.getIntance().getId(point));
			if(response!=null){
				String result = "";
				int status = response.getStatusLine().getStatusCode();
				if(status==HttpStatus.SC_OK){
					int count = 0;
					try {
						result = EntityUtils.toString(response.getEntity(),"UTF-8");
						Log.e("HttpConnect",result);
						ArrayList<OrderAllResponseInfo> orders = OrderAllResponseInfo.parseJSON(result);
						Log.d("OderCount",""+orders.size());
						for(OrderAllResponseInfo allResponseInfo:orders){
							String orderStatus = allResponseInfo.getStatus();
							if(orderStatus.equals("CONFIRMED")||orderStatus.equals("RESERVED"))
								count++;
						}
					} catch (ParseException | IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					m.what = 0;
					m.arg1 = count;
					handler.sendMessage(m);
				}
				else {
					m.what = status/100;
					handler.sendMessage(m);
				}
			}
			else{
				m.what = 3;
				handler.sendMessage(m);
			}
		}
		else {
			m.what = 3;
			handler.sendMessage(m);
		}
	}
	
	public void saveIcon(){
		File directory = new File("mnt/sdcard/Fandian/");
		File file = new File("mnt/sdcard/Fandian/icon.jpg");
		if(!directory.exists()){  
            directory.mkdir();//没有目录先创建目录  
        }
		if(!file.exists()){
			try{
				file.createNewFile();
				FileOutputStream fOut = null;
				fOut = new FileOutputStream(file);
				Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon);
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
				fOut.flush();
				fOut.close();
			}
			catch (Exception e){
				e.printStackTrace();
			}
			
		}
	}
	
	public void downloadAllMessage(){
		Log.e("MainActivity","Resume And DownLoad");
		Thread thread = new Thread(){
			@Override
			public void run(){
				boolean networkConnected = ConnectionClient.isNetworkConnected(point);
				if(networkConnected){
					HttpResponse response = ConnectionClient.connServerForResultGet("/flavors");
					if(response!=null){
						String result = "";
						try {
							result = EntityUtils.toString(response.getEntity(),"UTF-8");
							Log.e("HttpConnect",result);
						} catch (ParseException | IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						int status = response.getStatusLine().getStatusCode();
						if(status==HttpStatus.SC_OK){
							JSONTokener token = new JSONTokener(result);
							JSONArray array;
							ArrayList<Flavor> flavors = new ArrayList<Flavor>();
							try {
								array = (JSONArray)token.nextValue();
								for(int i = 0; i<array.length();i++){
									JSONObject object = array.getJSONObject(i);
									Flavor flavor = new Flavor();
									flavor.initFromJSON(object);
									flavors.add(flavor);
								}
								GlobalData.getIntance().setFlavors(flavors);
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							Message message= new Message();
							message.what = 12;
							handler.sendMessage(message);
						}
						else{
							Message m = new Message();
							m.what = status/100;
							handler.sendMessage(m);
						}
					}
					else{
						Message m = new Message();
						m.what = 3;
						handler.sendMessage(m);
					}
				}
				else {
					Message m = new Message();
					m.what = 3;
					handler.sendMessage(m);
				}
			}
		};
		thread.start();
		
		Thread threadLoadCity = new Thread(){
			@Override
			public void run(){
				boolean networkConnected = ConnectionClient.isNetworkConnected(point);
				if(networkConnected){
					HttpResponse response = ConnectionClient.connServerForResultGet("/cities");
					if(response!=null){
						String result = "";
						try {
							result = EntityUtils.toString(response.getEntity(),"UTF-8");
							Log.e("HttpConnect",result);
						} catch (ParseException | IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						int status = response.getStatusLine().getStatusCode();
						if(status==HttpStatus.SC_OK){
							JSONTokener token = new JSONTokener(result);
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
								FileOutputStream fos = openFileOutput("city.info", point.MODE_PRIVATE);
								fos.write(result.getBytes());
								fos.close();
								GlobalData.getIntance().setCity(cities);
							} catch (JSONException | IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							Message message= new Message();
							message.what = 0;
							handler.handleMessage(message);
						}
						else{
							Message m = new Message();
							m.what = status/100;
							handler.sendMessage(m);
						}
					}
					else{
						Message m = new Message();
						m.what = 3;
						handler.sendMessage(m);
					}
				}
				else {
					Message m = new Message();
					m.what = 3;
					handler.sendMessage(m);
				}
			}
		};
		if(GlobalData.getIntance().getCities(this)==null)
			threadLoadCity.start();
		
		Thread threadLoadZone = new Thread(){
			@Override
			public void run(){
				boolean networkConnected = ConnectionClient.isNetworkConnected(point);
				if(networkConnected){
					HttpResponse response = ConnectionClient.connServerForResultGet("/areas?city=1");
					
					if(response!=null){
						String result = "";
						try {
							result = EntityUtils.toString(response.getEntity(),"UTF-8");
							Log.e("HttpConnect",result);
						} catch (ParseException | IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						int status = response.getStatusLine().getStatusCode();
						if(status==HttpStatus.SC_OK){
							JSONTokener token = new JSONTokener(result);
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
								FileOutputStream fos = point.openFileOutput("area.info", point.MODE_PRIVATE);
								fos.write(result.getBytes());
								fos.close();
								GlobalData.getIntance().setArea(area);
							} catch (JSONException | IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							Message message= new Message();
							message.what = 0;
							handler.handleMessage(message);
						}
						else{
							Message m = new Message();
							m.what = status/100;
							handler.sendMessage(m);
						}
					}
					else{
						Message m = new Message();
						m.what = 3;
						handler.sendMessage(m);
					}
				}
				else {
					Message m = new Message();
					m.what = 3;
					handler.sendMessage(m);
				}
			}
		};
		if(GlobalData.getIntance().getArea(this)==null)
			threadLoadZone.start();
		
		Thread threadEvents = new Thread(){
			@Override
			public void run(){
				boolean networkConnected = ConnectionClient.isNetworkConnected(point);
				if(networkConnected){
					HttpResponse response = ConnectionClient.connServerForResultGet("/events");
					if(response!=null){
						String result = "";
						try {
							result = EntityUtils.toString(response.getEntity(),"UTF-8");
							Log.e("HttpConnect",result);
						} catch (ParseException | IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						int status = response.getStatusLine().getStatusCode();
						if(status==HttpStatus.SC_OK){
							JSONTokener token = new JSONTokener(result);
							JSONArray array;
							ArrayList<EventInfo> eventInfos = new ArrayList<EventInfo>();
							try {
								array = (JSONArray)token.nextValue();
								for(int i = 0; i<array.length();i++){
									JSONObject object = array.getJSONObject(i);
									EventInfo eventInfo = new EventInfo();
									eventInfo.initFromJSON(object);
									eventInfos.add(eventInfo);
									Log.d("Fragment", "refresh");
								}
								GlobalData.getIntance().setEventInfos(eventInfos);
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							Message message= new Message();
							message.what = 11;
							handler.sendMessage(message);
						}
						else{
							Message m = new Message();
							m.what = status/100;
							handler.sendMessage(m);
						}
					}
					else{
						Message m = new Message();
						m.what = 3;
						handler.sendMessage(m);
					}
				}
				else {
					Message m = new Message();
					m.what = 3;
					handler.sendMessage(m);
				}
			}
		};
		threadEvents.start();

		new Thread(){
			@Override
			public void run(){
				initOrderList();
			}
		}.start();
	}

	public class MyLocationListener implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null)
		        return;
			StringBuffer sb = new StringBuffer(256);
			sb.append("time : ");
			sb.append(location.getTime());
			sb.append("\nerror code : ");
			sb.append(location.getLocType());
			sb.append("\nlatitude : ");
			sb.append(location.getLatitude());
			sb.append("\nlontitude : ");
			sb.append(location.getLongitude());
			sb.append("\nradius : ");
			sb.append(location.getRadius());
			if (location.getLocType() == BDLocation.TypeGpsLocation){
				sb.append("\nspeed : ");
				sb.append(location.getSpeed());
				sb.append("\nsatellite : ");
				sb.append(location.getSatelliteNumber());
			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation){
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
			} 
 
			Log.d("Location",sb.toString());
			String locationString = location.getCity();
			if(locationString!=null){
				GlobalData.getIntance().setLocationCity(locationString.substring(0, locationString.length()-1));
				SharedPreferences preferences = getSharedPreferences("AFQ", Activity.MODE_PRIVATE);
				preferences.edit().putString("city", GlobalData.getIntance().getLocationCity().name).commit();
				mLocationClient.stop();
				Message message = new Message();
				message.what = 6;
				handler.sendMessage(message);
			}
		}
	}
}
