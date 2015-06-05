package com.aifengqiang.main;

import java.io.IOException;
import java.util.ArrayList;
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
import com.aifengqiang.entity.LocationData;
import com.aifengqiang.entity.RestaurantInfo;
import com.aifengqiang.entity.ShopDespatch;
import com.aifengqiang.entity.ShopMessageInfo;
import com.aifengqiang.entity.Specials;
import com.aifengqiang.network.ConnectionClient;
import com.aifengqiang.ui.NavigationButton;
import com.aifengqiang.ui.NavigationView;
import com.aifengqiang.ui.WaitingDialog;

import android.R.integer;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.tv.TvView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;
import android.widget.AbsoluteLayout;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

public class WaitingActivity extends Activity implements OnClickListener{
	private NavigationView nv;
	private TextView munites;
	private TextView seconds;
	private TextView waitingList;
	int secondCount;
    private Handler handler;
    private boolean waiting;
    private int returnCount;
    private WaitingActivity point = this;
    private FrameLayout frameLayout;
    private ListView applyListView;
    private ShopMessageAdapter adapter;
    private ArrayList<ShopMessageInfo> infos;
    private HashMap<Integer, RestaurantInfo> restaurants;
    private int count;
    private Dialog ad;
    private boolean activityExist;
    private LocationData location;
	
	@Override
	protected void onCreate(Bundle onSavedInstance){
		super.onCreate(onSavedInstance);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_waiting);
		nv = (NavigationView)findViewById(R.id.waiting_nav_view);
		nv.setLeftButton(getString(R.string.back),NavigationButton.NAVIGATIONIMAGELEFT, R.drawable.icon_back_nor);
		nv.setTitle(getString(R.string.app_title));
		NavigationView.NavigationViewListener nvl = new NavigationView.NavigationViewListener() {
			@Override
			public void OnNavigationButtonClick(int id) {
				// TODO Auto-generated method stub
				if(id == NavigationView.NAVIGATION_BUTTON_LEFT){
					finish();
				}
			}
		};
		nv.setNavigationViewListener(nvl);
		activityExist = true;
		
		munites = (TextView)findViewById(R.id.waiting_munites);
		seconds = (TextView)findViewById(R.id.waiting_seconds);
		
		waitingList = (TextView)findViewById(R.id.waiting_shop_text);
		secondCount = 180;
		returnCount = 0;
		waiting = true;
		
		frameLayout = (FrameLayout)findViewById(R.id.waiting_frame);
   	 	applyListView = (ListView)findViewById(R.id.waiting_shop_list);
   	 	infos = new ArrayList<>();
   	 	adapter = new ShopMessageAdapter(point, infos);
   	 	applyListView.setAdapter(adapter);
   	 	
   	 	restaurants = new HashMap<>();
   	 	count = 0;
		
   	 	Intent it = getIntent();
   	 	this.secondCount = it.getIntExtra("seconds", 0);
   	 	if(secondCount>=180){
   	 		this.returnCount = it.getIntExtra("return", 1);
   	 		this.secondCount = 480-secondCount;
   	 		new Thread(){
   	 			@Override
   	 			public void run(){
   	 				loadOrderInfo();
   	 			}
   	 		}.start();
   	 		waiting = false;
   	 	}
   	 	else {
   	 		this.secondCount = 180-secondCount;
		}
   	 	if(!it.hasExtra("seconds"))
   	 		init();
   	 	else
   	   	 	waitingList.setText(Html.fromHtml("<div>请等待，已有<span><font color=\"#f88727\">"+it.getIntExtra("request", 10)+"</span>家商户收到您的订单，3分钟内抢你的商户会陆续发出邀请！</div>"));
		
		handler=new Handler(){
			@Override
	        public void handleMessage(Message msg) {
	             switch (msg.what) {
	                 case 11: {
	                	 //Alert no shop accept this order, Order again
	                	 showDialog("没有商户响应此订单，请重新下单", true);
	                 }
	                 case 1:{
	                	 if(msg.arg1<=176)
	                		 for(int k = 2; k<frameLayout.getChildCount();k++){
	                			 frameLayout.removeViewAt(2);
	                		 }
	                	 int i = msg.arg1/60;
	                	 if(i<10){
	                		 munites.setText("0"+i);
	                	 }
	                	 else{
	                		 munites.setText(i+"");
	                	 }
	                	 i = msg.arg1%60;
	                	 if(i<10){
	                		 seconds.setText("0"+i);
	                	 }
	                	 else{
	                		 seconds.setText(i+"");
	                	 }
	                	 if(returnCount>0){
	                		 if(frameLayout.getChildCount()>1){
		                		 for(int k = 1; k<frameLayout.getChildCount();k++){
		                			 frameLayout.removeViewAt(1);
		                		 }
	                		 }
		                	 //frameLayout.removeAllViews();
	                		 applyListView.setVisibility(View.VISIBLE);
	                		 applyListView.requestFocus();
		                	 applyListView.setDivider(null);
		                	 applyListView.setBackgroundDrawable(null);
		                	 adapter.notifyDataSetChanged();
//		                	 //adapter.notifyDataSetChanged();
//		                	 //frameLayout.addView(applyListView);
	                	 }
	                	 break;
	                 }
	                 case 12:{
	                	 showDialog("您没有选择餐馆，请重新下单", true);
	                	 break;
	                 }
	                 case 3:
	 					showDialog("网络连接错误，请检查！", false);
	 					break;
	 				case 4:
	 					showDialog("请求错误！", false);
	 					break;
	 				case 5:
	 					showDialog("服务器错误！",false);
	 					break;
	                 case 10:{
	                	 //alert confirm success
		 				showDialog("下单成功，请前往订单页面查看详情！",true);
	                	break;
	                 }
	                 default:
	                     break;
	             }
	         }
		};
		Thread th = new Thread(){
			@Override
			public void run(){
				while(secondCount>=0&&activityExist){
					Message m = new Message();
					m.what = 1;
					m.arg1 = secondCount;
					secondCount--;
					if(returnCount>0&&waiting){
						waiting = false;
						secondCount = 300;
					}
					else if(secondCount==0&&waiting){
						m.what=11;
					}
					else if(secondCount==0&&!waiting){
						m.what = 12;
					}
					handler.sendMessage(m);
					try {
						sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};
		th.start();
		Thread netThread = new Thread(){
			@Override
			public void run(){
				while(secondCount!=0&&activityExist){
					try {
						loadOrderInfo();
						sleep(5000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};
		netThread.start();
	}
	
	public void init(){

		ArrayList<String> respondRestaurants = GlobalData.getIntance().getRestaurants();
		if(!(respondRestaurants==null||respondRestaurants.isEmpty())){
   	 	waitingList.setText(Html.fromHtml("<div>请等待，已有<span><font color=\"#f88727\">"+respondRestaurants.size()+"</span>家商户收到您的订单，3分钟内抢你的商户会陆续发出邀请！</div>"));
		for(String string : respondRestaurants){
   		 	AnimationSet st = new AnimationSet(true);
   		 	ShopDespatch sd = new ShopDespatch(string);
   		 	TextView tView = new TextView(point);
   		 	LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
   		 	lParams.topMargin = -40;
   		 	lParams.leftMargin = sd.start_x;
   		 	tView.setLayoutParams(lParams);
   		 	tView.setTextColor(sd.color);
   		 	tView.setText(sd.name);
   		 	frameLayout.addView(tView);
   		 	TranslateAnimation ts = new TranslateAnimation(sd.start_x, sd.end_x, sd.start_y, 800*GlobalData.getIntance().getScale());
   		 	ts.setDuration(30000);
   		 	ts.setStartOffset((int)(sd.delay*1000));
   		 	ts.setFillAfter(true);
   		 	st.addAnimation(ts);
//   		 	AlphaAnimation alphaAnimation1 = new AlphaAnimation(0, 1);
//   		 	alphaAnimation1.setDuration((int)(sd.delay*1000));
//   		 	alphaAnimation1.setStartOffset(0);
//   		 	st.addAnimation(alphaAnimation1);
   		 	tView.startAnimation(st);
			//Log.e("Restaurants", string);
		}
		}
	}
	
	public void loadOrderInfo(){
		Message m = new Message();
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
						JSONObject lObject = object.getJSONObject("location");
						location = new LocationData((float)lObject.getDouble("lng"), (float)lObject.getDouble("lat"));
						JSONArray jsonArray = object.getJSONArray("requests");
						for(int k = 0;k<jsonArray.length();k++){
						//for(int k = 0;k<2;k++){
							JSONObject jsonObject = jsonArray.getJSONObject(k);
							String tokenIdString = jsonObject.getString("id");
							JSONObject restaurant = jsonObject.getJSONObject("restaurant");
							String resIdString = restaurant.getString("id");
							String statusString = jsonObject.getString("status");
							//String resIdString = "19d551a7-d161-499b-bfff-7cbad199b5c3";
							boolean inCache = false;
							for(int i = 0;i<restaurants.size();i++){
								if(restaurants.get(i).getId().equals(resIdString)){
									inCache = true;
								}
							}
							int count = 0;
							if(!inCache && statusString.equals("ACCEPTED")){
								getRestaurant(resIdString, tokenIdString);
								count++;
							}
							Log.e("Restaurant",count +"个餐馆回应");
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
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

	protected void getRestaurant(String resIdString, String tokenIdString) {
		// TODO Auto-generated method stub
		Message m = new Message();
		boolean networkConnected = ConnectionClient.isNetworkConnected(point);
		if(networkConnected){
			HttpResponse response = ConnectionClient.connServerForResultGet("/restaurants/"+resIdString);
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
					JSONObject object;
					try {
						object = (JSONObject)token.nextValue();
						RestaurantInfo restaurantInfo = new RestaurantInfo();
						restaurantInfo.initFromJSON(object);
						restaurants.put(count, restaurantInfo);
						count++;
		                ShopMessageInfo sInfo = new ShopMessageInfo();
		                sInfo.address = restaurantInfo.getAddress();
		                sInfo.name =restaurantInfo.getName();
		                Log.e("Restaurant","Name"+sInfo.name);
		                LocationData resLocation = restaurantInfo.getLocationData();
		                sInfo.distance = (int)LocationData.GetDistance(location.lat, location.lng, resLocation.lat, resLocation.lng);
		                String discountMessageString = "";
		                for(Specials specials : restaurantInfo.getSpecials()){
		                	discountMessageString += "<p>";
		                	if(specials.getTypes().getString()!=null){
		                		discountMessageString+=specials.getTypes().getString();
		                	}
		            		if(specials.getDetail()!=null){
		            			discountMessageString+=":"+specials.getDetail();;
		            		}
		            		discountMessageString+="<span><font color=\"#f88727\">(从";
		            		if(specials.getBegin()!=null){
		            			discountMessageString+=specials.getBegin();
		            		}
		        			discountMessageString+="到";
		            		if(specials.getEnd()!=null){
		            			discountMessageString+=specials.getEnd();
		            		}
		                	discountMessageString+="期间有效)</span></p>";
		                }
		                sInfo.discountMessage = discountMessageString;
		                sInfo.tokenIdString = tokenIdString;
		                sInfo.id = restaurantInfo.getId();
		                boolean loaded = false;
		                for(ShopMessageInfo ss : infos){
		                	if(ss.id.equals(sInfo.id))
		                		loaded = true;
		                }
	                	if(!loaded){
	                		infos.add(sInfo);
	                		returnCount++;
	                	}
					}
					catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
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
	
	public void selectRetaurant(String idString){
		Message m = new Message();
		boolean networkConnected = ConnectionClient.isNetworkConnected(point);
		if(networkConnected){
			JSONObject restaurantJsonObject = new JSONObject();
			JSONObject idJsonObject = new JSONObject();
			try {
				idJsonObject.put("id", idString);
				restaurantJsonObject.put("restaurant", idJsonObject);
			} catch (JSONException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			HttpResponse response = ConnectionClient.connServerForResultPostWithHeader("/customer/orders/"+GlobalData.getIntance().getCurrentOrderId()+"/confirm", restaurantJsonObject, "X-Customer-Token",GlobalData.getIntance().getId(point));
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
					m.what = 10;
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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
	}
	
	public class ShopMessageAdapter extends BaseAdapter{
		ArrayList<ShopMessageInfo> shops;
		Context mContext;
		private int selectedPosition=-1;//list中选定的餐馆
		private int choosePosition = -1;//选择消费的餐馆
		
		public ShopMessageAdapter(Context context, ArrayList<ShopMessageInfo> infos){
			mContext = context;
			shops = infos;
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return shops.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return shops.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public void setSelectedPosition(int position){
			this.selectedPosition = position;
		}
		
		public int getSelectedIndex(){
			return selectedPosition;
		}
		
		public void setChoosePosition(int position){
			this.choosePosition = position;
		}
		
		public int getChoosePositon(){
			return this.choosePosition;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			LayoutInflater inflater = LayoutInflater.from(mContext);
			convertView = inflater.inflate(R.layout.shop_message_item, null);
			TextView nameTextView = (TextView)convertView.findViewById(R.id.shop_message_name);
			TextView addressView = (TextView)convertView.findViewById(R.id.shop_message_address);
			TextView discountView = (TextView)convertView.findViewById(R.id.shop_message_discount_message);
			TextView showAllText = (TextView)convertView.findViewById(R.id.shop_message_showall);
			String name = shops.get(position).name;
			if(name.length()>=10){
				name = name.substring(0, 10)+"...";
			}
			nameTextView.setText(name);
			String distanceStr = ("<span><font color=\"#f88727\">（"+shops.get(position).getDistance()+"）<span></div>");
			String address = shops.get(position).address;
			addressView.setText(Html.fromHtml("<div>"+address+distanceStr));
			discountView.setText(Html.fromHtml(shops.get(position).discountMessage));
			LinearLayout nameLayout = (LinearLayout)convertView.findViewById(R.id.shop_message_name_linear);

			final int select = position;
			if(position!=selectedPosition){
				discountView.setLines(2);
			}
			else {
				discountView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			}
			Button chooseButton = (Button)convertView.findViewById(R.id.shop_message_choose);
			LinearLayout allTextView = (LinearLayout)convertView.findViewById(R.id.shop_message_all);
			if(position==selectedPosition){
				showAllText.setText("隐藏全部");
			}
			else {
				showAllText.setText("显示全部");
			}
			chooseButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Alert
					showDialog("确定选择这个餐馆？");
					choosePosition = select;
				}
			});
			nameLayout.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					GlobalData.getIntance().setRestaurant(restaurants.get(select));
					Intent it = new Intent(point, ShopDetailActivity.class);
					startActivityForResult(it, 1006);
				}
			});
			
			allTextView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(selectedPosition==select)
						selectedPosition = -1;
					else {
						selectedPosition = select;
					}
					notifyDataSetChanged();
				}
			});
			return convertView;
		}
		
	}

	public void showDialog(final String text, final boolean finished){
		if(ad!=null&&ad.isShowing())
			ad.dismiss();
		ad = new Dialog(point,R.style.MyDialog);
		ad.setContentView(R.layout.dialog_login);
		ad.setCancelable(false);
		TextView tv = (TextView)ad.findViewById(R.id.dialog_text);
		tv.setText(text);
		Button btn = (Button)ad.findViewById(R.id.dialog_button_one_sure);
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!finished)
					ad.dismiss();
				else if(!text.contains("请重新下单")){
					ad.dismiss();
					Message message = new Message();
					message.what = 10;
					SharedPreferences preferences = getSharedPreferences("Order", MODE_PRIVATE);
					Editor editor = preferences.edit();
					editor.putBoolean("Ordered", true);
					editor.commit();
					finish();
				}
				else{
					ad.dismiss();
					Intent it = new Intent(point, OrderActivity.class);
					startActivity(it);
					finish();
				}
			}
		});
		if(activityExist)
			ad.show();
	}
	
	public void showDialog(String textString){
		if(ad!=null&&ad.isShowing())
			ad.dismiss();
		ad = new Dialog(point,R.style.MyDialog);
		ad.setCanceledOnTouchOutside(false);
		ad.setCancelable(false);
		ad.setContentView(R.layout.dialog_confirm_cancel);
		TextView tv = (TextView)ad.findViewById(R.id.dialog_text_view);
		tv.setText(textString);
		Button btnConfirm = (Button)ad.findViewById(R.id.dialog_confirm);
		btnConfirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new Thread(){
					@Override
					public void run(){
						selectRetaurant(infos.get(adapter.getChoosePositon()).id);				
					}
				}.start();
				ad.dismiss();
				ad = new WaitingDialog(point, R.style.MyDialog);
				ad.show();
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
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent it){
		if(requestCode==1006&&resultCode==1007){
			SharedPreferences preferences = getSharedPreferences("Order", MODE_PRIVATE);
			Editor editor = preferences.edit();
			editor.putBoolean("Ordered", true);
			editor.commit();
			finish();
		}
	}
	@Override
	public void onDestroy(){
		super.onDestroy();
		activityExist = false;
	}
}
