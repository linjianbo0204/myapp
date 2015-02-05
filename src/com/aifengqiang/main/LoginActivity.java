package com.aifengqiang.main;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.aifengqiang.network.ConnectionClient;
import com.aifengqiang.ui.NavigationButton;
import com.aifengqiang.ui.NavigationView;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
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
                //“˛≤ÿ»Ìº¸≈Ã
//                imm.hideSoftInputFromWindow(tv.getWindowToken(), 0);
                //œ‘ æ»Ìº¸≈Ã
                imm.showSoftInputFromInputMethod(phoneNumber.getWindowToken(), 0);
                //«–ªª»Ìº¸≈Ãµƒœ‘ æ”Î“˛≤ÿ
//                imm.toggleSoftInputFromWindow(phoneNumber.getWindowToken(), 0, InputMethodManager.HIDE_NOT_ALWAYS);
                //ªÚ’ﬂ
//                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
			}
			
		});
		phoneNumber.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				// TODO Auto-generated method stub
				if(actionId == EditorInfo.IME_ACTION_NEXT){
					InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
	                //“˛≤ÿ»Ìº¸≈Ã
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
                //“˛≤ÿ»Ìº¸≈Ã
//                imm.hideSoftInputFromWindow(tv.getWindowToken(), 0);
                //œ‘ æ»Ìº¸≈Ã
                imm.showSoftInputFromInputMethod(checkNumber.getWindowToken(), 0);
                //«–ªª»Ìº¸≈Ãµƒœ‘ æ”Î“˛≤ÿ
//                imm.toggleSoftInputFromWindow(phoneNumber.getWindowToken(), 0, InputMethodManager.HIDE_NOT_ALWAYS);
                //ªÚ’ﬂ
//                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
			}
			
		});
		checkNumber.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				// TODO Auto-generated method stub
				if(actionId == EditorInfo.IME_ACTION_DONE){
					InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
	                //“˛≤ÿ»Ìº¸≈Ã
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
				Thread th = new Thread(){
					int min = 0;
					@Override
					public void run(){
						String mobileNumber = phoneNumber.getText().toString();
						JSONObject json = new JSONObject();
						try {
							json.put("mobile", mobileNumber);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						String result = ConnectionClient.connServerForResultPost("customer/verify", json);
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
						while(min<30){
							min++;
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
				th.start();
				
			}
			
		});
		
		submit = (Button)findViewById(R.id.login_submit);
		submit.requestFocus();
		submit.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Thread th = new Thread(){
					@Override
					public void run(){
						String inputVerifyCode = checkNumber.getText().toString();
						if(inputVerifyCode.equals(verifyCode)){
							Message m = new Message();
							m.what = 1;
							handler.sendMessage(m);
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
					if(msg.arg1!=30){
						checkNumberBtn.setClickable(false);
						checkNumberBtn.setText("("+msg.arg1+")√Î∫Û÷ÿ–¬ªÒ»°");
					}
					else
					{
						checkNumberBtn.setClickable(true);
						checkNumberBtn.setText("ªÒ»°—È÷§¬Î");
					}
					break;
				case 1:
					loginContext.finish();
					break;
				default:
					break;
				}
			}
		};
	}

}
