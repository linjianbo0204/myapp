package com.aifengqiang.main;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

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
import com.aifengqiang.entity.LocationData;
import com.aifengqiang.entity.OrderRequest;
import com.aifengqiang.entity.UserInfo;
import com.aifengqiang.network.ConnectionClient;
import com.aifengqiang.ui.NavigationButton;
import com.aifengqiang.ui.NavigationView;
import com.aifengqiang.ui.WaitingDialog;
import com.aifengqiang.wheel.ArrayWheelAdapter;
import com.aifengqiang.wheel.NumericWheelAdapter;
import com.aifengqiang.wheel.OnWheelChangedListener;
import com.aifengqiang.wheel.WheelAdapter;
import com.aifengqiang.wheel.WheelView;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

public class OrderActivity extends Activity implements OnClickListener{
	private Button orderButton;
	private boolean gender;
	private boolean box;
	private boolean needBox;
	private RadioButton genderMaleButton;
	private RadioButton genderFemaleButton;
	private RadioButton boxButton;
	private RadioButton needBoxButton;
	private TextView dateTextView;
	private TextView timeTextView;
	private TextView peopleTextView;
	private TextView distanceTextView;
	private TextView genderMaleText;
	private TextView genderFemaleText;
	private TextView needBoxText;
	private TextView styleText;
	private TextView costText;
	private TextView disTextView;
	private TextView cirTextView;
	private TextView remarkTextView;
	private TextView firstNameTextView;
	private LinearLayout boxLinear;
	private LinearLayout styleLinear;
	private LinearLayout costLinear;
	private LinearLayout dtpLinear;
	private LinearLayout disLinear;
	private OrderRequest finalRequest;
	private Handler handler;
	private OrderActivity point = this;
	private Dialog ad;
	private int dateIndex, timeIndex, peopleIndex, distanceIndex, costIndex, circleIndex, districtIndex;
	String[] dateStrings = new String[30];
	String[] timeStrings;
	String[] timeTodayStrings;
	ArrayList<String> timeStringsArrayList = new ArrayList<>();
	String[] peopleStrings = new String[60];
	String[] costStrings = new String[40];
	String[] distanceStrings = new String[]{"100米","200米","300米","500米","800米","1000米","1500米","2000米","3000米","5000米"};
	String[] zoneStrings;
	String[] districtStrings;
	private boolean locationStatus;
	private HashMap<String,ArrayList<AreaData>> areaCache;
	private String idString;
	private boolean todayTimeOut;
	public LocationClient mLocationClient = null;
	public BDLocationListener myListener = new MyLocationListener();

	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_order);
		
		finalRequest = new OrderRequest();
		
		NavigationView nv = (NavigationView)findViewById(R.id.order_nav_view);
		nv.setLeftButton(getString(R.string.back), NavigationButton.NAVIGATIONIMAGELEFT, R.drawable.navigation_back_bg);
		nv.setTitle(getString(R.string.app_title));
		nv.setRightButton(getString(R.string.reset), NavigationButton.NAVIGATIONIMAGENONE, 0);
		NavigationView.NavigationViewListener nvl = new NavigationView.NavigationViewListener() {
			@Override
			public void OnNavigationButtonClick(int id) {
				// TODO Auto-generated method stub
				if(id == NavigationView.NAVIGATION_BUTTON_LEFT){
					finish();
				}
				else if(id==NavigationView.NAVIGATION_BUTTON_RIGHT)
				{
					initData();
				}
			}
		};
		nv.setNavigationViewListener(nvl);
		
		dtpLinear = (LinearLayout)findViewById(R.id.order_dtp_linear);
		dtpLinear.setOnClickListener(this);
		
		orderButton = (Button)findViewById(R.id.order_submit);
		orderButton.setOnClickListener(this);
		
		disLinear = (LinearLayout)findViewById(R.id.order_distance_linear);
		disLinear.setOnClickListener(this);
		
		boxLinear = (LinearLayout)findViewById(R.id.order_box_agree);
		
		styleLinear = (LinearLayout)findViewById(R.id.order_style_linear);
		styleLinear.setOnClickListener(this);
		styleText = (TextView)findViewById(R.id.order_style_text);
		
		genderMaleText = (TextView)findViewById(R.id.order_man_text);
		genderMaleButton = (RadioButton)findViewById(R.id.order_man_button);
		genderMaleButton.setOnClickListener(this);
		
		genderFemaleText = (TextView)findViewById(R.id.order_woman_text);
		genderFemaleButton = (RadioButton)findViewById(R.id.order_woman_button);
		genderFemaleButton.setOnClickListener(this);
		
		boxButton = (RadioButton)findViewById(R.id.order_box);
		boxButton.setOnClickListener(this);
		
		needBoxButton = (RadioButton) findViewById(R.id.order_box_button);
		needBoxText = (TextView) findViewById(R.id.order_box_text);
		needBoxButton.setOnClickListener(this);
		
		costLinear = (LinearLayout)findViewById(R.id.order_cost_linear);
		costText = (TextView)findViewById(R.id.order_cost);
		costLinear.setOnClickListener(this);
		
		dateTextView = (TextView)findViewById(R.id.order_date);
		timeTextView = (TextView)findViewById(R.id.order_time);
		peopleTextView = (TextView)findViewById(R.id.order_people_count);
		distanceTextView = (TextView)findViewById(R.id.order_distance);
		
		disTextView = (TextView)findViewById(R.id.order_distance_text);
		cirTextView = (TextView)findViewById(R.id.order_zone);
		cirTextView.setOnClickListener(this);
		locationStatus = true;
		
		remarkTextView = (TextView)findViewById(R.id.order_remark);
		firstNameTextView = (TextView)findViewById(R.id.order_firstname);
		
		gender = true;
		box = false;
		needBox = false;
		handler = new Handler(){
			@Override
			public void handleMessage(Message message){
				orderButton.setEnabled(true);
				switch (message.what) {
				case 0:
					Intent it = new Intent(point, WaitingActivity.class);
					startActivity(it);
					finish();
					break;
				case 3:
					showDialog("网络连接错误,请检查网络连接后再次尝试");
					break;
				case 4:
					showDialog("请求错误！");
					break;
				case 5:
					showDialog("服务器错误，请联系相关工作人员");
					break;	
				case 6:
					showDialog("找不到对应的餐馆，请修改后重新下单");
					break;
				default:
					break;
				}
			}
		};

		Calendar calendar = Calendar.getInstance();
		Date dateNow = new Date();
		Log.e("Date", dateNow.toString());
		calendar.setTime(dateNow);
		int mCurrentDay = calendar.get(Calendar.DAY_OF_YEAR);
		int mHour = calendar.get(Calendar.HOUR_OF_DAY);
		if(mHour == 23){
			todayTimeOut = true;
		}
		else {
			todayTimeOut = false;
		}
		for(int i=0;i<30;i++){
			calendar.set(Calendar.DAY_OF_YEAR, mCurrentDay+i+(todayTimeOut?1:0));
			Log.e("Date", calendar.toString());
			int mNextDay = calendar.get(Calendar.DAY_OF_MONTH);
			int mNextMonth = calendar.get(Calendar.MONTH)+1;
			String week = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.CHINA);
			dateStrings[i]=String.format("%02d-%02d %s", mNextMonth, mNextDay, week);
		}

		timeTodayStrings = new String[2*(24-mHour-1)];
		
		for(int i = 0;i<24-mHour-1;i++){
			timeTodayStrings[i*2] = String.format("%02d:%02d", mHour+i+1, 0);
			timeTodayStrings[i*2+1] = String.format("%02d:%02d", mHour+i+1, 30);
		}

		timeStrings = new String[48];
		for(int i = 0;i<24;i++){
			timeStrings[i*2] = String.format("%02d:%02d", i, 0);
			timeStrings[i*2+1] = String.format("%02d:%02d", i, 30);
		}

		for(int i = 1; i<61 ;i++){
			peopleStrings[i-1]= i+"人";
		}
		
		for(int i = 0; i<40 ;i++){
			costStrings[i]= i*100+"元";
		}
		
		HashMap<Integer, HashMap<String, ArrayList<AreaData>>> areas = GlobalData.getIntance().getArea(point);
		if(areas!=null){
			City city = GlobalData.getIntance().getLocationCity();
			areaCache = GlobalData.getIntance().getArea(point).get(city.id);
			districtStrings = new String[areaCache.size()];
			int i=0;
			for(String key : areaCache.keySet()){
				ArrayList<AreaData> areaDatas = areaCache.get(key);
				districtStrings[i] = areaDatas.get(0).district.name;
				Log.d("District",""+districtStrings[i]);
				i++;
			}
		}
		else {
			districtStrings = new String[]{"(定位失败)"};
		}
		initData();
		
		idString = GlobalData.getIntance().getId(point);
		new Thread(){
			
		}.start();
	}
	
	private void getLocation() {
		//cities = GlobalData.getIntance().getCities(point);
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

	public void initData(){
		dateTextView.setText(dateStrings[1]);
		dateIndex = 1;
		timeTextView.setText(timeStrings[24]);
		timeIndex = 24;
		peopleTextView.setText(peopleStrings[1]);
		peopleIndex = 1;
		costText.setText(costStrings[0]);
		costIndex = 0;
		distanceTextView.setText(distanceStrings[5]);
		distanceIndex = 5;
		circleIndex = 0;
		districtIndex = 0;
		disTextView.setText("距离");
		cirTextView.setText("商圈");
		locationStatus = true;
		styleText.setText("本帮菜");
		costText.setText("0元");
		boxButton.setChecked(false);
		boxLinear.setVisibility(View.GONE);
		remarkTextView.setText("");
		genderMaleButton.setChecked(true);
		gender = true;
		genderMaleText.setTextColor(0xfff88727);
		genderFemaleText.setTextColor(0xff666666);
		finalRequest.gender = "MALE";
		firstNameTextView.setText("");
		needBox = false;
		needBoxButton.setChecked(false);
		needBoxText.setTextColor(0xff666666);
		
		finalRequest.area = null;
		finalRequest.budget = 0;
		finalRequest.diner = "";
		Calendar calendar = Calendar.getInstance();
		int mCurrentDay = calendar.get(Calendar.DAY_OF_MONTH);
		calendar.set(Calendar.DAY_OF_MONTH, mCurrentDay+dateIndex+(todayTimeOut?1:0));
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH)+1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		finalRequest.due = String.format("%d-%02d-%02dT%s:00", year, month, day, timeStrings[timeIndex]);
		finalRequest.flavors = new ArrayList<>();
		finalRequest.flavors.add(3);
		finalRequest.gender = "MALE";
		finalRequest.labels = new ArrayList<>();
		finalRequest.location = new LocationData(0, 0);
		finalRequest.people = 2;
		finalRequest.range = 1000;
		finalRequest.remark = "";
		finalRequest.city = GlobalData.getIntance().getLocationCity();
		getLocation();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v == orderButton){
			orderButton.setEnabled(false);
			finalRequest.diner=firstNameTextView.getText().toString();
			if(finalRequest.diner.length()==0){
				showDialog("请输入您的姓氏");
				orderButton.setEnabled(true);
				return;
			}
			finalRequest.remark=remarkTextView.getText().toString();
			GlobalData.getIntance().setRestaurants(null);
			ad = new WaitingDialog(this, R.style.MyDialog);
			ad.show();
			Thread thread = new Thread(){
				@Override
				public void run(){
					boolean networkConnected = ConnectionClient.isNetworkConnected(point);
					if(networkConnected){
						HttpResponse response = ConnectionClient.connServerForResultPostWithHeader("/customer/orders", finalRequest.getEntity(), "X-Customer-Token", GlobalData.getIntance().getId(point));
						if(response!=null){
							String result = null;
							int status = 0;
							try {
								result = EntityUtils.toString(response.getEntity(),"UTF-8");
								status = response.getStatusLine().getStatusCode();
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							Log.e("HttpConnect",result);
							if(status==HttpStatus.SC_OK){
								JSONObject object;
								try {
									JSONTokener tokener = new JSONTokener(result);
									object = (JSONObject)tokener.nextValue();
									String orderIdString = object.getString("id");
									GlobalData.getIntance().setCurrentOrderId(orderIdString);
									if(object.has("requests")){
										JSONArray requestsArray = object.getJSONArray("requests");
										ArrayList<String> names = new ArrayList<String>();
										for(int i = 0; i<requestsArray.length();i++){
											JSONObject jsonObject = requestsArray.getJSONObject(i);
											String nameString = jsonObject.getJSONObject("restaurant").getString("name");
											names.add(nameString);
										}
										GlobalData.getIntance().setRestaurants(names);
									}
									else{
										Message message = new Message();
										message.what = 6;
										handler.sendMessage(message);
										return;
									}
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								Message m = new Message();
								m.what = 0;
								handler.sendMessage(m);
							}
							else{
								Message m = new Message();
								m.what = status/100;
								handler.sendMessage(m);
							}
						}
						else {
							 Message message = new Message();
							 message.what = 3;
							 handler.sendMessage(message);
						}
					}
					else{
						Message m = new Message();
						m.what = 3;
						handler.sendMessage(m);
					}
				}
			};

//			Intent it = new Intent(point, WaitingActivity.class);
//			startActivity(it);
//			finish();
			if(GlobalData.getIntance().getId(point)!=null)
				thread.start();
			else {
				orderButton.setEnabled(true);
				Intent it = new Intent(point, LoginActivity.class);
				startActivity(it);
			}
		}
		else if(v == genderMaleButton){
			gender = true;
			genderMaleText.setTextColor(0xfff88727);
			genderFemaleText.setTextColor(0xff666666);
			finalRequest.gender = "MALE";
		}
		else if(v == genderFemaleButton){
			gender = false;
			genderFemaleText.setTextColor(0xfff88727);
			genderMaleText.setTextColor(0xff666666);
			finalRequest.gender = "FEMALE";
		}
		else if(v == boxButton){
			if(box){
				box = false;
				boxButton.setChecked(false);
				boxLinear.setVisibility(View.GONE);
				finalRequest.labels.clear();
			}
			else
			{
				box = true;
				boxLinear.setVisibility(View.VISIBLE);
				finalRequest.labels.clear();
				finalRequest.labels.add("ROOM");
			}
		}
		else if(v == needBoxButton){
			if(needBox){
				needBox = false;
				needBoxButton.setChecked(false);
				needBoxText.setTextColor(0xff666666);
				finalRequest.labels.remove("HALL");
			}else{
				needBox = true;
				needBoxText.setTextColor(0xfff88727);
				finalRequest.labels.add("HALL");
			}
		}
		else if(v == styleLinear){
			Intent it = new Intent(this, FoodStyleListChooseActivity.class);
			startActivityForResult(it, 1002);
		}
		else if(v == costLinear){
			ad = new Dialog(this,R.style.MyDialog);
			ad.setContentView(R.layout.dialog_cost);
			WheelView wv = (WheelView)ad.findViewById(R.id.dialog_cost_wheel);
			wv.setAdapter(new ArrayWheelAdapter<>(costStrings));
			wv.setCurrentItem(costIndex);
			wv.addChangingListener(new OnWheelChangedListener() {
				
				@Override
				public void onChanged(WheelView wheel, int oldValue, int newValue) {
					// TODO Auto-generated method stub
					costIndex = newValue;
				}
			});
			ad.show();
			Button confirmButton = (Button)ad.findViewById(R.id.dialog_cost_confirm);
			confirmButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					costText.setText(costStrings[costIndex]);
					finalRequest.budget = Integer.valueOf(costStrings[costIndex].substring(0, costStrings[costIndex].length()-1));
					ad.dismiss();
				}
			});
			Button backButton = (Button)ad.findViewById(R.id.dialog_cost_back);
			backButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					ad.dismiss();
				}
			});
		}
		else if(v == dtpLinear){
			ad = new Dialog(this,R.style.MyDialog);
			ad.setContentView(R.layout.dialog_date_time_people);
			WheelView dateWheelView = (WheelView)ad.findViewById(R.id.dialog_date_wheel);
			final WheelView timeWheelView = (WheelView)ad.findViewById(R.id.dialog_time_wheel);
			WheelView peopleWheelView = (WheelView)ad.findViewById(R.id.dialog_people_wheel);
			dateWheelView.setAdapter(new ArrayWheelAdapter<>(dateStrings));
			dateWheelView.setCurrentItem(dateIndex);
			dateWheelView.addChangingListener(new OnWheelChangedListener() {
				
				@Override
				public void onChanged(WheelView wheel, int oldValue, int newValue) {
					// TODO Auto-generated method stub
					dateIndex = newValue;
					if(!todayTimeOut&&dateIndex==0){
						timeWheelView.setAdapter(new ArrayWheelAdapter<>(timeTodayStrings));
						timeWheelView.setCurrentItem(0);
						timeIndex = 0;
					}
					else{
						timeWheelView.setAdapter(new ArrayWheelAdapter<>(timeStrings));
						timeWheelView.setCurrentItem(timeIndex);
					}
				}
			});
			if(!todayTimeOut&&dateIndex==0)
				timeWheelView.setAdapter(new ArrayWheelAdapter<>(timeTodayStrings));
			else {
				timeWheelView.setAdapter(new ArrayWheelAdapter<>(timeStrings));
			}
			timeWheelView.setCurrentItem(timeIndex);
			timeWheelView.addChangingListener(new OnWheelChangedListener() {
				
				@Override
				public void onChanged(WheelView wheel, int oldValue, int newValue) {
					// TODO Auto-generated method stub
					timeIndex = newValue;
				}
			});
			
			peopleWheelView.setAdapter(new ArrayWheelAdapter<>(peopleStrings));
			peopleWheelView.setCurrentItem(peopleIndex);
			peopleWheelView.addChangingListener(new OnWheelChangedListener() {
				
				@Override
				public void onChanged(WheelView wheel, int oldValue, int newValue) {
					// TODO Auto-generated method stub
					peopleIndex = newValue;
				}
			});
			
			Button confirmButton = (Button)ad.findViewById(R.id.dialog_dtp_confirm);
			confirmButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					ad.dismiss();
					dateTextView.setText(dateStrings[dateIndex]);
					if(!todayTimeOut&&dateIndex==0){
						timeTextView.setText(timeTodayStrings[timeIndex]);
					}
					else {
						timeTextView.setText(timeStrings[timeIndex]);
					}
					peopleTextView.setText(peopleStrings[peopleIndex]);
					Calendar calendar = Calendar.getInstance();
					int mCurrentDay = calendar.get(Calendar.DAY_OF_MONTH);
					calendar.set(Calendar.DAY_OF_MONTH, mCurrentDay+dateIndex+(todayTimeOut?1:0));
					int year = calendar.get(Calendar.YEAR);
					int month = calendar.get(Calendar.MONTH)+1;
					int day = calendar.get(Calendar.DAY_OF_MONTH);
					if(!todayTimeOut&&dateIndex==0){
						finalRequest.due = String.format("%d-%02d-%02dT%s:00", year, month, day, timeTodayStrings[timeIndex]);
						Log.e("Due", finalRequest.due);
					}
					else {
						finalRequest.due = String.format("%d-%02d-%02dT%s:00", year, month, day, timeStrings[timeIndex]);
					}
					Log.e("PeopleIndex", peopleIndex+"");
					String peopleString = peopleStrings[peopleIndex];
					finalRequest.people = Integer.valueOf(peopleString.substring(0, peopleString.length()-1));
				}
			});
			
			Button backButton = (Button)ad.findViewById(R.id.dialog_dtp_back);
			backButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					ad.dismiss();
				}
			});
			ad.show();
		}
		else if(v == cirTextView){
			if(locationStatus){
				locationStatus = false;
				disTextView.setText("商圈");
				cirTextView.setText("距离");
				distanceTextView.setText("");
				showCircleDialog();
			}
			else{
				locationStatus = true;
				disTextView.setText("距离");
				cirTextView.setText("商圈");
				distanceTextView.setText("1000米");
				showDistanceDialog();
			}
		}
		else if(v==disLinear){
			if(locationStatus){
				showDistanceDialog();
			}
			else {
				showCircleDialog();
			}
		}
	}
	
	
	@Override
	public void onActivityResult(int requestCode, int resultCost, Intent it){
		if(requestCode == 1002 && resultCost ==1003){
			Bundle bundle = it.getBundleExtra("bundle");
			String idString = bundle.getString("id");
			String nameString = bundle.getString("name");
			String textshowString = nameString.substring(0, nameString.length()-1);
			styleText.setText(textshowString);
			String[] idSplit = idString.substring(0, idString.length()-1).split(",");
			finalRequest.flavors.clear();
			for(String s: idSplit){
				finalRequest.flavors.add(Integer.valueOf(s));
			}
		}
	}
	
	public void showDistanceDialog(){
		ad = new Dialog(this,R.style.MyDialog);
		ad.setContentView(R.layout.dialog_cost);
		ad.setCanceledOnTouchOutside(false);
		WheelView wv = (WheelView)ad.findViewById(R.id.dialog_cost_wheel);
		wv.setAdapter(new ArrayWheelAdapter<>(distanceStrings));
		wv.setCurrentItem(distanceIndex);
		wv.addChangingListener(new OnWheelChangedListener() {
			
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				// TODO Auto-generated method stub
				distanceIndex = newValue;
			}
		});
		ad.show();
		Button confirmButton = (Button)ad.findViewById(R.id.dialog_cost_confirm);
		confirmButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				distanceTextView.setText(distanceStrings[distanceIndex]);
				ad.dismiss();
				finalRequest.range = Integer.valueOf(distanceStrings[distanceIndex].substring(0,distanceStrings[distanceIndex].length()-1));
			}
		});
		Button backButton = (Button)ad.findViewById(R.id.dialog_cost_back);
		backButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ad.dismiss();
			}
		});
	}
	
	public void showCircleDialog(){

		ad = new Dialog(this,R.style.MyDialog);
		ad.setCanceledOnTouchOutside(false);
		ad.setContentView(R.layout.dialog_circle);
		WheelView districtWv = (WheelView)ad.findViewById(R.id.dialog_district_wheel);
		final WheelView zoneWv = (WheelView)ad.findViewById(R.id.dialog_zone_wheel);
		districtWv.setAdapter(new ArrayWheelAdapter<>(districtStrings));
		districtWv.setCurrentItem(districtIndex);
		districtWv.addChangingListener(new OnWheelChangedListener() {
			
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				// TODO Auto-generated method stub
				districtIndex = newValue;
				ArrayList<AreaData> zones = areaCache.get(districtStrings[districtIndex]);
				zoneStrings = new String[zones.size()];
				int i = 0;
				for(AreaData tempAreaData : zones){
					zoneStrings[i] = tempAreaData.name;
					i++;
				}
				zoneWv.setAdapter(new ArrayWheelAdapter<>(zoneStrings));
				circleIndex = 0;
				zoneWv.setCurrentItem(circleIndex);
			}
		});
		
		if(!districtStrings[districtIndex].equals("(定位失败)")){
			ArrayList<AreaData> zones = areaCache.get(districtStrings[districtIndex]);
			zoneStrings = new String[zones.size()];
			int i = 0;
			for(AreaData tempAreaData : zones){
				zoneStrings[i] = tempAreaData.name;
				i++;
			}
			zoneWv.setAdapter(new ArrayWheelAdapter<>(zoneStrings));
			zoneWv.setCurrentItem(circleIndex);
			zoneWv.addChangingListener(new OnWheelChangedListener() {
			
				@Override
				public void onChanged(WheelView wheel, int oldValue, int newValue) {
					// TODO Auto-generated method stub
					circleIndex = newValue;
				}
			});
		}
		ad.show();
		Button confirmButton = (Button)ad.findViewById(R.id.dialog_circle_confirm);
		confirmButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!districtStrings[districtIndex].equals("(定位失败)")){
					distanceTextView.setText(zoneStrings[circleIndex]);
					ad.dismiss();
					for(AreaData areaData:areaCache.get(districtStrings[districtIndex])){
						if(zoneStrings[circleIndex].equals(areaData.name)){
							finalRequest.area = new AreaData(areaData.id,areaData.name);
						}
					}
				}
				else {
					ad.dismiss();
				}
			}
		});
		Button backButton = (Button)ad.findViewById(R.id.dialog_circle_back);
		backButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ad.dismiss();
			}
		});
	}
	
	public void showDialog(String text){
		if(ad!=null&&ad.isShowing())
			ad.dismiss();
		ad = new Dialog(point,R.style.MyDialog);
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
 
			Log.d("Location",sb.toString()+mLocationClient.isStarted());
			finalRequest.location = new LocationData((float)location.getLongitude(),(float)(location.getLatitude()));
			GlobalData.getIntance().setLocationCity(location.getCity());
			finalRequest.city = GlobalData.getIntance().getLocationCity();
			Log.e("location",finalRequest.city.name+":"+location.getCity());
			mLocationClient.unRegisterLocationListener(myListener);
			if(finalRequest.city.id!=-1)
				mLocationClient.stop();	
		}
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		if(mLocationClient.isStarted()){
			mLocationClient.stop();
		}
	}

}
