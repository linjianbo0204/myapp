package com.aifengqiang.main;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import javax.security.auth.PrivateCredentialPermission;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.R.integer;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.JsonToken;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.aifengqiang.data.GlobalData;
import com.aifengqiang.entity.CommentEntity;
import com.aifengqiang.entity.Flavor;
import com.aifengqiang.entity.Items;
import com.aifengqiang.entity.OrderAllResponseInfo;
import com.aifengqiang.entity.RestaurantInfo;
import com.aifengqiang.network.ConnectionClient;
import com.aifengqiang.ui.LabelIconView;
import com.aifengqiang.ui.NavigationButton;
import com.aifengqiang.ui.NavigationView;
import com.aifengqiang.ui.ShopStarView;
import com.aifengqiang.ui.WaitingDialog;

public class ShopDetailActivity extends Activity{
	private NavigationView nv;
	private ShopDetailActivity point = this;
	private LinearLayout commentListView;
	private TextView phoneTextView;
	private TextView titleTextView;
	private ShopStarView shopStarView;
	private TextView costTextView;
	private TextView evaluaTextView;
	private TextView classTextView;
	private TextView addressTextView;
	private LabelIconView labelIconView;
	private TextView foodTextView;
	private TextView reviewTextView;
	private LinearLayout locationLinearLayout;
	private LinearLayout foodPicLinearLayout;
	private LinearLayout envPicLinearLayout;
	private RestaurantInfo restaurantInfo;
	private LinearLayout foodList;
	private HashMap<Integer,Bitmap> foodBitmaps;
	private HashMap<Integer,Bitmap> envBitmaps;
	private ArrayList<CommentEntity> commentEntities;
	private ImageView restaurantImage;
	private int beginIndex;
	private int length;
	private Handler handler;
	private int commentCount;
	private int itemCount;
	private int count;
	private Dialog ad;
	@Override
	protected void onCreate(Bundle onSavedInstance){
	super.onCreate(onSavedInstance);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_shop_detail);
		nv = (NavigationView)findViewById(R.id.detail_nav_view);
		nv.setLeftButton(getString(R.string.back),NavigationButton.NAVIGATIONIMAGELEFT, R.drawable.icon_back_nor);
		//nv.setRightButton("确定", NavigationButton.NAVIGATIONIMAGENONE, 0);
		nv.setTitle(getString(R.string.app_title));
		Intent it = getIntent();
		if(!it.hasExtra("choose")){
			nv.setRightButton(getString(R.string.choose),NavigationButton.NAVIGATIONIMAGENONE, 0);
		}
		NavigationView.NavigationViewListener nvl = new NavigationView.NavigationViewListener() {
			@Override
			public void OnNavigationButtonClick(int id) {
				// TODO Auto-generated method stub
				if(id == NavigationView.NAVIGATION_BUTTON_LEFT){
					finish();
				}
				else if(id==NavigationView.NAVIGATION_BUTTON_RIGHT){
					showDialog("确定选择这个餐馆吗？");
				}
			}
		};
		nv.setNavigationViewListener(nvl);
		
		phoneTextView = (TextView)findViewById(R.id.shop_detail_phoneNumber);
		titleTextView = (TextView)findViewById(R.id.detail_shop_name);
		shopStarView = (ShopStarView)findViewById(R.id.detail_shop_star);
		costTextView = (TextView)findViewById(R.id.detail_shop_cost);
		evaluaTextView = (TextView)findViewById(R.id.detail_shop_evaluate);
		classTextView = (TextView)findViewById(R.id.detail_shop_class);
		labelIconView = (LabelIconView)findViewById(R.id.detail_shop_discount);
		reviewTextView = (TextView)findViewById(R.id.detail_shop_review);
		foodPicLinearLayout = (LinearLayout)findViewById(R.id.detail_shop_food_pic);
		envPicLinearLayout = (LinearLayout)findViewById(R.id.detail_shop_env_pic);
		foodList = (LinearLayout)findViewById(R.id.shop_detail_food_list);
		commentListView = (LinearLayout)findViewById(R.id.shop_detail_comment_list);
		addressTextView = (TextView)findViewById(R.id.detail_shop_address);
		locationLinearLayout = (LinearLayout)findViewById(R.id.detail_shop_location);
		foodTextView = (TextView)findViewById(R.id.detail_shop_food_text);
		restaurantImage = (ImageView)findViewById(R.id.detail_shop_pic);
		
		foodBitmaps = new HashMap<>();
		envBitmaps = new HashMap<>();
		beginIndex = 0;
		length = 5;
		handler = new Handler(){
			@Override
			public void handleMessage(Message message){
				switch (message.what) {
				case 0:
					refreshComnent();
					break;
				case 1:
					Log.d("ShopDetail","refresh pics");
					refreshPic();
					break;
				case 3:
 					showDialog("网络连接错误，请检查！", false);
 					break;
 				case 4:
 					showDialog("请求错误！", false);
 					break;
 				case 5:
 					showDialog("服务器错误！",false);
 					break;
 				case 6:
 					restaurantImage.setImageBitmap(restaurantInfo.getBanner().getBitmap());
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
		commentEntities = new ArrayList<>();

		restaurantInfo = GlobalData.getIntance().getRestaurantInfo();
		count = restaurantInfo.getItems().size()>3?3:restaurantInfo.getItems().size();
		setViewWithRestaurantInfo();
	}
	
	public void setViewWithRestaurantInfo(){
		if(restaurantInfo!=null){
			phoneTextView.setText(restaurantInfo.getPhoneNumber());
			phoneTextView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Uri uri = Uri.parse("tel:"+phoneTextView.getText());  
					Intent it = new Intent(Intent.ACTION_DIAL, uri);   
					startActivity(it); 
				}
			});
			
			String shopName = restaurantInfo.getName();
			if(shopName.length()>=10){
				shopName = shopName.substring(0, 10)+"...";
			}
			titleTextView.setText(shopName);
			shopStarView.setStar(restaurantInfo.getStar());
			costTextView.setText(Html.fromHtml("<div>人均<span><font color=\"#f88727\">￥"+restaurantInfo.getCost()+"</span></div>"));
			evaluaTextView.setText(Html.fromHtml("<div>口味：<span><font color=\"#f88727\">"+restaurantInfo.getTaste()+"</span>  环境：<span><font color=\"#f88727\">"+restaurantInfo.getEnv()+"</span>  服务：<span><font color=\"#f88727\">"+restaurantInfo.getSevice()+"</span></div>"));
			String classTextString = "";
			for(int i = 0;i<restaurantInfo.getTypes().size();i++){
				classTextString+=restaurantInfo.getTypes().get(i).getString();
			}
			classTextView.setText(classTextString);
			addressTextView.setText(restaurantInfo.getAddress());
			locationLinearLayout.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent it = new Intent(point, MapActivity.class);
					it.putExtra("lat", restaurantInfo.getLocationData().lat);
					it.putExtra("lng", restaurantInfo.getLocationData().lng);
					it.putExtra("address", restaurantInfo.getAddress());
					startActivity(it);
				}
			});
			labelIconView.setLabels(restaurantInfo.getLabels());
			itemCount = restaurantInfo.getItems().size();
			foodTextView.setText("菜品("+itemCount+")");
			for(int i = 0;i<itemCount;i++){
				ImageView imageView = new ImageView(this);
				LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams((int)(100*(GlobalData.getIntance().getScale())),(int)(100*(GlobalData.getIntance().getScale())));
				layoutParams.rightMargin = 20;
				imageView.setLayoutParams(layoutParams);
				if(foodBitmaps.get(i)!=null)
					imageView.setImageBitmap(foodBitmaps.get(i));
				else {
					imageView.setBackgroundResource(R.drawable.background_color);
				}
				foodPicLinearLayout.addView(imageView);
			}
			foodList.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent it = new Intent(point, FoodListActivity.class);
					it.putExtra("name", ((TextView)findViewById(R.id.detail_shop_name)).getText());
					startActivity(it);
				}
			});
			
			for(int i = 0;i<restaurantInfo.getPhoto().size();i++){
				ImageView imageView = new ImageView(this);
				LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams((int)(100*(GlobalData.getIntance().getScale())),(int)(100*(GlobalData.getIntance().getScale())));
				layoutParams.rightMargin = 20;
				imageView.setLayoutParams(layoutParams);
				if(envBitmaps.get(i)!=null)
					imageView.setImageBitmap(envBitmaps.get(i));
				else {
					imageView.setBackgroundResource(R.drawable.background_color);
				}
				envPicLinearLayout.addView(imageView);
			}
			new Thread(){
				@Override
				public void run(){
					loadPic();
				}
			}.start();
			
			new Thread(){
				@Override
				public void run(){
					loadComment();
				}
			}.start();
		}
	}
	
	public View getView(int position, View convertView, ArrayList<CommentEntity> cEntities) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = LayoutInflater.from(point);
		convertView = inflater.inflate(R.layout.shop_detail_comment_item, null);
		
		ImageView touxiang= (ImageView)convertView.findViewById(R.id.commen_item_picture);
		TextView name = (TextView)convertView.findViewById(R.id.comment_item_name);
		TextView date = (TextView)convertView.findViewById(R.id.comment_item_date);
		TextView score = (TextView)convertView.findViewById(R.id.comment_item_score);
		ShopStarView ssv = (ShopStarView)convertView.findViewById(R.id.comment_item_star);
		TextView cost = (TextView)convertView.findViewById(R.id.comment_item_cost);
		TextView comment = (TextView)convertView.findViewById(R.id.comment_item_comment_text);
		TextView highLigh = (TextView)convertView.findViewById(R.id.comment_item_highlight);
		
		CommentEntity cEntity = cEntities.get(position);
		touxiang.setImageResource(R.drawable.touxiang);
		name.setText(cEntity.getCustomer().mobile.substring(0, 3)+"****"+cEntity.getCustomer().mobile.substring(8));
		String dateString = cEntity.getDate();
		String[] splitStrings = dateString.split("T");
		ssv.setStar(cEntity.getStar());
		date.setText(splitStrings[0]);
		score.setText(cEntity.getScore());
		cost.setText(Html.fromHtml(cEntity.getCost()));
		comment.setText(cEntity.getComment());
		highLigh.setText(cEntity.getHighight());
		return convertView;
	}
	
	public void loadPic(){
		URL uri;
		try {
			if(restaurantInfo.getBanner()!=null){
				uri = new URL(ConnectionClient.connectionImage+restaurantInfo.getBanner().getUrl());
				HttpURLConnection conn = (HttpURLConnection) uri.openConnection();   
				conn.setDoInput(true);   
				conn.connect();   
				InputStream is = conn.getInputStream();
				Bitmap bitmap = BitmapFactory.decodeStream(is);
				restaurantInfo.getBanner().setImage(bitmap);
				is.close();
				Message msg = new Message();
				msg.what = 6;
				handler.sendMessage(msg);
			}
			for(int i = 0;i<itemCount;i++){
				Items items = restaurantInfo.getItems().get(i);
				if(items.getImage()!=null){
					uri = new URL(ConnectionClient.connectionImage+items.getImage().getUrl());
					HttpURLConnection conn = (HttpURLConnection) uri.openConnection();   
					conn.setDoInput(true);   
					conn.connect();   
					InputStream is = conn.getInputStream();
					Bitmap bitmap = BitmapFactory.decodeStream(is);
					items.getImage().setImage(bitmap);
					foodBitmaps.put(i, bitmap);
					is.close();
					Message msg = new Message();
					msg.what = 1;
					handler.sendMessage(msg);
				}
			}
			for(int i = 0;i<restaurantInfo.getPhoto().size();i++){
				uri = new URL(ConnectionClient.connectionImage+restaurantInfo.getPhoto().get(i));
				HttpURLConnection conn = (HttpURLConnection) uri.openConnection();   
				conn.setDoInput(true);   
				conn.connect();   
				InputStream is = conn.getInputStream();
				Bitmap bitmap = BitmapFactory.decodeStream(is);
				restaurantInfo.addImage(bitmap);
				envBitmaps.put(i, bitmap);
				is.close();
				Message msg = new Message();
				msg.what = 1;
				handler.sendMessage(msg);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void loadComment(){
		Message m = new Message();
		boolean networkConnected = ConnectionClient.isNetworkConnected(point);
		if(networkConnected){
			HttpParams params = new BasicHttpParams();
			params.setIntParameter("skip", beginIndex);
			params.setIntParameter("take", length);
			HttpResponse response = ConnectionClient.connServerForResultGetWithParams("/restaurants/"+restaurantInfo.getId()+"/reviews", params);
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
						object = (JSONObject) token.nextValue();
						JSONArray array = object.getJSONArray("items");
						commentCount = object.getInt("total");
						commentEntities.clear();
						for(int i=0;i<array.length();i++){
							CommentEntity commentEntity = new CommentEntity();
							commentEntity.initFromJSON(array.getJSONObject(i));
							commentEntities.add(commentEntity);
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					beginIndex+=commentEntities.size();
					m.what = 0;
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
	
	public void refreshPic(){
		for(int i = 0;i<itemCount;i++){
			ImageView imageView = (ImageView)foodPicLinearLayout.getChildAt(i);
			if(foodBitmaps.get(i)!=null)
				imageView.setImageBitmap(foodBitmaps.get(i));
			else {
				imageView.setBackgroundResource(R.drawable.background_color);
			}
		}
		foodPicLinearLayout.invalidate();
		for(int i = 0;i<restaurantInfo.getPhoto().size();i++){
			ImageView imageView = (ImageView)envPicLinearLayout.getChildAt(i);
			if(envBitmaps.get(i)!=null){
				imageView.setImageBitmap(envBitmaps.get(i));
				Log.d("envPic","refresh"+i);
			}
			else {
				imageView.setBackgroundResource(R.drawable.background_color);
			}
		}
		Log.d("envPic","refresh");
		envPicLinearLayout.invalidate();
	}
	
	public void refreshComnent(){
		reviewTextView.setText("点评("+commentCount+")");
		for(int i = 0;i<commentEntities.size();i++){
			View item = new View(point);
			item = getView(i, item, commentEntities);
			commentListView.addView(item);
		}
		commentListView.invalidate();
	}
	

	public void showDialog(String text, final boolean finished){
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
				if(!finished)
					ad.dismiss();
				else {
	 				ad.dismiss();
	 				setResult(1007);
	 				finish();
				}
			}
		});
		ad.show();
	}
	
	public void showDialog(String textString){
		if(ad!=null&&ad.isShowing())
			ad.dismiss();
		ad = new Dialog(point,R.style.MyDialog);
		ad.setCanceledOnTouchOutside(false);
		ad.setContentView(R.layout.dialog_confirm_cancel);
		TextView tv = (TextView)ad.findViewById(R.id.dialog_text_view);
		tv.setText(textString);
		Button btnConfirm = (Button)ad.findViewById(R.id.dialog_confirm);
		btnConfirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ad.dismiss();
				ad = new WaitingDialog(point, R.style.MyDialog);

				ad.show();
				new Thread(){
					@Override
					public void run(){
						selectRetaurant(restaurantInfo.getId());				
					}
				}.start();
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
}
