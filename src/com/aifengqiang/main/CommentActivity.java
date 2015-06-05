package com.aifengqiang.main;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.aifengqiang.data.GlobalData;
import com.aifengqiang.network.ConnectionClient;
import com.aifengqiang.ui.NavigationButton;
import com.aifengqiang.ui.NavigationView;
import com.aifengqiang.ui.ShopStarView;
import com.aifengqiang.ui.ShopStarView.OnStarChangeListener;
import com.aifengqiang.ui.WaitingDialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class CommentActivity extends Activity{
	private NavigationView nv;
	private ShopStarView envStarView;
	private TextView envTextView;
	private ShopStarView sevStarView;
	private TextView sevTextView;
	private ShopStarView flaStarView;
	private TextView flaTextView;
	private ShopStarView starView;
	private EditText commentEditText;
	private EditText costEditText;
	private EditText highlightText;
	private TextView textView;
	private String orderId;
	private String restaurantId;
	private Handler handler;
	private Dialog ad;
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment);
		Intent it = getIntent();
		orderId = it.getStringExtra("orderId");
		restaurantId = it.getStringExtra("resId");
		nv = (NavigationView)findViewById(R.id.comment_nav_view);
		nv.setLeftButton(getString(R.string.back), NavigationButton.NAVIGATIONIMAGELEFT, R.drawable.navigation_back_bg);
		nv.setTitle(getString(R.string.comment));
		nv.setRightButton("发表", NavigationButton.NAVIGATIONIMAGENONE, 0);
		NavigationView.NavigationViewListener nvl = new NavigationView.NavigationViewListener() {
			@Override
			public void OnNavigationButtonClick(int id) {
				// TODO Auto-generated method stub
				if(id == NavigationView.NAVIGATION_BUTTON_LEFT){
					finish();
				}
				else
				{
					ad = new WaitingDialog(CommentActivity.this, R.style.MyDialog);
					ad.show();
					new Thread(){
						@Override
						public void run(){
							sendComment();
						}
					}.start();
				}
			}
		};
		nv.setNavigationViewListener(nvl);
		
		starView = (ShopStarView)findViewById(R.id.comment_res_star);
		textView = (TextView)findViewById(R.id.comment_res_text);
		starView.setOnStarChangeListener(new OnStarChangeListener() {
			
			@Override
			public void onStarChange(MotionEvent event, int index) {
				// TODO Auto-generated method stub
				starView.setStar(index);
				textView.setText(starView.getStar()+"分");
			}
		});
		envStarView = (ShopStarView)findViewById(R.id.comment_env_star);
		envTextView = (TextView)findViewById(R.id.comment_env_text);
		envStarView.setOnStarChangeListener(new OnStarChangeListener() {
			
			@Override
			public void onStarChange(MotionEvent event, int index) {
				// TODO Auto-generated method stub
				envStarView.setStar(index);
				envTextView.setText(envStarView.getStar()+"分");
			}
		});
		sevStarView = (ShopStarView)findViewById(R.id.comment_sev_star);
		sevTextView = (TextView)findViewById(R.id.comment_sev_text);
		sevStarView.setOnStarChangeListener(new OnStarChangeListener() {
			
			@Override
			public void onStarChange(MotionEvent event, int index) {
				// TODO Auto-generated method stub
				sevStarView.setStar(index);
				sevTextView.setText(sevStarView.getStar()+"分");
			}
		});
		flaStarView = (ShopStarView)findViewById(R.id.comment_flavor_star);
		flaTextView = (TextView)findViewById(R.id.comment_flavor_text);
		flaStarView.setClickable(false);
		flaStarView.setOnStarChangeListener(new OnStarChangeListener() {
			
			@Override
			public void onStarChange(MotionEvent event, int index) {
				// TODO Auto-generated method stub
				flaStarView.setStar(index);
				flaTextView.setText(flaStarView.getStar()+"分");
			}
		});
		costEditText = (EditText)findViewById(R.id.comment_cost_text);
		commentEditText = (EditText)findViewById(R.id.comment_edit_text);
		highlightText = (EditText)findViewById(R.id.comment_highlight_food_text);
		
		handler = new Handler(){
			@Override
			public void handleMessage(Message message){
				switch (message.what) {
				case 0:
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
		        case 10:{
		        	//alert confirm success
		        	showDialog("评论成功！",true);
		        	break;
		        }
		        case 11:{
		        	showDialog("评论空啦！多少写一点嘛！掌柜多谢啦！",false);
		        	break;
		        }
		        case 12:{
		        	showDialog("人均消费需为整数！", false);
		        }
		        default:
		            break;
				}
			}
		};
	}
	
	public void sendComment(){
		Message m = new Message();
		boolean networkConnected = ConnectionClient.isNetworkConnected(this);
		if(networkConnected){
			JSONObject commentObject = new JSONObject();
			try {
				commentObject.put("stars", starView.getStar());
				commentObject.put("taste", flaStarView.getStar());
				commentObject.put("env", envStarView.getStar());
				commentObject.put("service", sevStarView.getStar());
				int cost = Integer.parseInt(costEditText.getText().toString());
				commentObject.put("cost", cost);
				Log.e("Comment",commentObject.toString());
				if(commentEditText.getText().toString()!=null)
					commentObject.put("comment", commentEditText.getText().toString());
				else {
					m.what = 11;
					handler.sendMessage(m);
					return;
				}
			}
			catch(NumberFormatException e){
				m.what = 12;
				handler.sendMessage(m);
				return;
			}
			catch (Exception e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
				return;
			}
			HttpResponse response = ConnectionClient.connServerForResultPutWithHeader("/customer/orders/"+orderId+"/review", commentObject, "X-Customer-Token",GlobalData.getIntance().getId(this));
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

	public void showDialog(String text, final boolean finished){
		if(ad!=null&&ad.isShowing())
			ad.dismiss();
		ad = new Dialog(this,R.style.MyDialog);
		ad.setCanceledOnTouchOutside(false);
		ad.setCancelable(false);
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
					finish();
				}
			}
		});
		ad.show();
	}
}
