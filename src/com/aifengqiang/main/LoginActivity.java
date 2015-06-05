package com.aifengqiang.main;

import java.io.FileOutputStream;
import java.io.IOException;
import java.security.PublicKey;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.aifengqiang.data.GlobalData;
import com.aifengqiang.entity.UserInfo;
import com.aifengqiang.network.ConnectionClient;
import com.aifengqiang.ui.NavigationButton;
import com.aifengqiang.ui.NavigationView;
import com.aifengqiang.ui.WaitingDialog;
import com.baidu.location.ad;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends Activity{
	
	private EditText phoneNumber;
	private EditText checkNumber;
	private Button submit;
	private Button checkNumberBtn;
	private Handler handler;
	private LoginActivity loginContext = this;
	private String verifyCode;
	private Dialog ad;
	private String tokenIdString;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		NavigationView nv = (NavigationView)findViewById(R.id.login_nav_view);
		nv.setLeftButton(getString(R.string.back), NavigationButton.NAVIGATIONIMAGELEFT, R.drawable.navigation_back_bg);
		nv.setTitle(getString(R.string.app_title));
		NavigationView.NavigationViewListener nvl = new NavigationView.NavigationViewListener() {
			@Override
			public void OnNavigationButtonClick(int id) {
				// TODO Auto-generated method stub
				if(id == NavigationView.NAVIGATION_BUTTON_LEFT){
					finish();
				}
				else
				{
					finish();
				}
			}
		};
		nv.setNavigationViewListener(nvl);
		
		phoneNumber = (EditText) findViewById(R.id.login_tel_num_text);
		phoneNumber.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                //���������
//                imm.hideSoftInputFromWindow(tv.getWindowToken(), 0);
                //��ʾ�����
                imm.showSoftInputFromInputMethod(phoneNumber.getWindowToken(), 0);
                //�л�����̵���ʾ������
//                imm.toggleSoftInputFromWindow(phoneNumber.getWindowToken(), 0, InputMethodManager.HIDE_NOT_ALWAYS);
                //����
//                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
			}
			
		});
		phoneNumber.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				// TODO Auto-generated method stub
				if(actionId == EditorInfo.IME_ACTION_NEXT){
					InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
	                //���������
	                imm.hideSoftInputFromWindow(phoneNumber.getWindowToken(), 0);
					return true;
				}
				else
					return false;
			}
		});
		//phoneNumber.setInputType(EditorInfo.TYPE_CLASS_PHONE);
		checkNumber = (EditText) findViewById(R.id.login_check_num_text);
		//checkNumber.setInputType(EditorInfo.TYPE_CLASS_PHONE);
		checkNumber.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                //���������
//                imm.hideSoftInputFromWindow(tv.getWindowToken(), 0);
                //��ʾ�����
                imm.showSoftInputFromInputMethod(checkNumber.getWindowToken(), 0);
                //�л�����̵���ʾ������
//                imm.toggleSoftInputFromWindow(phoneNumber.getWindowToken(), 0, InputMethodManager.HIDE_NOT_ALWAYS);
                //����
//                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
			}
			
		});
		checkNumber.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				// TODO Auto-generated method stub
				if(actionId == EditorInfo.IME_ACTION_DONE){
					InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
	                //���������
	                imm.hideSoftInputFromWindow(checkNumber.getWindowToken(), 0);
					return true;
				}
				else
					return false;
			}
		});
		
		checkNumberBtn = (Button)findViewById(R.id.login_check_num_btn);
		checkNumberBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Thread th1 = new Thread(){
					int min = 60;
					@Override
					public void run(){
						while(min>0){
							min--;
							try{
								Message m = new Message();
								m.what = 0;
								m.arg1 = min;
								handler.sendMessage(m);
								sleep(1000);
							}
							catch(Exception ex){
								ex.printStackTrace();
							}
						}
					}
				};
				th1.start();
				Thread th = new Thread(){
					@Override
					public void run(){
						String mobileNumber = phoneNumber.getText().toString();
						if(mobileNumber.length()!=11){
							Message m = new Message();
							m.what = 7;
							handler.sendMessage(m);
							return;
						}
						JSONObject json = new JSONObject();
						try {
							json.put("mobile", mobileNumber);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						boolean networkConnected = ConnectionClient.isNetworkConnected(loginContext);
						if(networkConnected){
							HttpResponse response = ConnectionClient.connServerForResultPost("/customer/verify", json);
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
										verifyCode = object.getString("verifyCode");
										Log.e("VerfyCode",""+verifyCode);
									} catch (JSONException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									Log.e("Connect","Result"+result);
								}
								else if(status/100==4){
									Message m = new Message();
									m.what = 2;
									handler.sendMessage(m);
								}
								else if(status/100==3){
									Message m = new Message();
									m.what = 4;
									handler.sendMessage(m);
								}
								else if(status/100==5){
									Message m = new Message();
									m.what = 5;
									handler.sendMessage(m);
								}
							}
							else{
								Message m = new Message();
								m.what = 4;
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
				th.start();
				
			}
			
		});
		
		submit = (Button)findViewById(R.id.login_submit);
		submit.requestFocus();
		submit.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ad = new WaitingDialog(loginContext, R.style.MyDialog);
				ad.show();
				Thread th = new Thread(){
					@Override
					public void run(){
						String telephoneNumber = phoneNumber.getText().toString();
						String inputVerifyCode = checkNumber.getText().toString();
						if(inputVerifyCode.length()!=6){
							Message message = new Message();
							message.what = 6;
							handler.sendMessage(message);
							return;
						}
						if(telephoneNumber.length()!=11){
							Message m = new Message();
							m.what = 7;
							handler.sendMessage(m);
							return;
						}
						JSONObject object = new JSONObject();
						try{
							object.put("mobile", telephoneNumber);
							object.put("verifyCode", inputVerifyCode);
							boolean networkConnected = ConnectionClient.isNetworkConnected(loginContext);
							if(networkConnected){
								HttpResponse response = ConnectionClient.connServerForResultPost("/customer/authenticate", object);
								if(response!=null){
									String result = EntityUtils.toString(response.getEntity(),"UTF-8");
									Log.e("HttpConnect",result);
									int status = response.getStatusLine().getStatusCode();
									if(status==HttpStatus.SC_OK){
										UserInfo userInfo = new UserInfo();
										userInfo.getUserInfoFromJson(result);
										Log.e("UserInfo",""+userInfo.getId());
										tokenIdString = userInfo.getId();
										GlobalData.getIntance().setId(tokenIdString);
										FileOutputStream fos = openFileOutput("user.info", Context.MODE_PRIVATE);
										fos.write(result.getBytes());
										fos.close();
										Message m = new Message();
										m.what = 1;
										handler.sendMessage(m);
									}
									else if(4==(status/100)){
										Message m = new Message();
										m.what = 2;
										handler.sendMessage(m);
									}
									else if(5==(status/100)){
										Message message = new Message();
										message.what = 5;
										handler.sendMessage(message);
									}
									else if(3==(status/100))
									{
										Message message = new Message();
										message.what = 4;
									 handler.sendMessage(message);
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
						catch(Exception e){
							e.printStackTrace();
						}
					}
				};
				th.start();
			}
			
		});
		
		handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				switch(msg.what){
				case 0:
					if(msg.arg1!=0){
						checkNumberBtn.setClickable(false);
						checkNumberBtn.setText("("+msg.arg1+")������»�ȡ");
						//checkNumber.setText(verifyCode);
					}
					else
					{
						checkNumberBtn.setClickable(true);
						checkNumberBtn.setText("��ȡ��֤��");
					}
					break;
				case 1:
					if(ad!=null&&ad.isShowing()){
						ad.dismiss();
					}
					Intent intent = new Intent();
					intent.putExtra("TokenId", tokenIdString);
					setResult(1001, intent);
					finish();
					break;
				case 2:
					showDialog("��֤��������������������������»�ȡ");
					break;
				case 3:
					showDialog("�������Ӳ�����Ŷ������������ú��ٳ��԰�");
					break;
				case 4:
					showDialog("�������Ӵ������飡");
					break;
				case 5:
					showDialog("����������");
					break;
				case 6:
					showDialog("��֤�볤�ȴ�����������������");
					break;
				case 7:
					showDialog("�ֻ��������������������������");
					break;
				default:
					break;
				}
			}
		};
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig){
		super.onConfigurationChanged(newConfig);
		Log.d("Configuration","Changed");
	}
	
	public void showDialog(String text){
		if(ad!=null&&ad.isShowing())
			ad.dismiss();
		ad = new Dialog(loginContext,R.style.MyDialog);
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
				ad.dismiss();
			}
		});
		ad.show();
	}

}
