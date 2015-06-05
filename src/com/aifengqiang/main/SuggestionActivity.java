package com.aifengqiang.main;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.aifengqiang.network.ConnectionClient;
import com.aifengqiang.ui.NavigationButton;
import com.aifengqiang.ui.NavigationView;
import com.aifengqiang.ui.WaitingDialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SuggestionActivity extends Activity{
	private NavigationView nav;
	private EditText etxt;
	private Button btn;
	private boolean send;
	private Handler handler;
	private SuggestionActivity point = this;
	private Dialog ad;
	private boolean finish;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_suggestion);
		nav = (NavigationView) findViewById(R.id.activity_suggestion_nav);
		nav.setLeftButton(getString(R.string.back), NavigationButton.NAVIGATIONIMAGELEFT, R.drawable.navigation_back_bg);
		nav.setRightButton(getString(R.string.send), NavigationButton.NAVIGATIONIMAGENONE, 0);
		nav.setTitle(getString(R.string.app_title));
		NavigationView.NavigationViewListener nvl = new NavigationView.NavigationViewListener() {
			@Override
			public void OnNavigationButtonClick(int id) {
				if(!send){
					if(id == NavigationView.NAVIGATION_BUTTON_LEFT){
						System.exit(0);
					}
					else
					{
						sendSuggestion();
					}
				}
			}
		};
		nav.setNavigationViewListener(nvl);
		
		etxt = (EditText)findViewById(R.id.activity_suggestion_text);
		btn = (Button) findViewById(R.id.activity_suggestion_btn);
		send = false;
		finish = false;
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				sendSuggestion();
			}
		});
		handler = new Handler(){
			@Override
			public void handleMessage(Message m){
				send = false;
				switch (m.what) {
				case 0:
					showDialog("写点意见吧！");
					break;
				case 2:
					finish = true;
					showDialog("您的意见已发表成功！");
					break;
				case 3:
					showDialog("网络连接不给力哦，检查网络设置后再尝试吧");
					break;
				case 4:
					showDialog("网络连接错误，请检查！");
					break;
				case 5:
					showDialog("服务器错误！");
					break;
				default:
					break;
				}
			}
		};
	}
	
	private void sendSuggestion(){
		send = true;
		ad = new WaitingDialog(point, R.style.MyDialog);

		ad.show();
		new Thread(){
			public void run(){
				Message m = new Message();
				String content = etxt.getText().toString();
				if(content.length()==0){
					m.what = 0;
					handler.sendMessage(m);
					return;
				}
				JSONObject json = new JSONObject();
				try {
					json.put("content", content);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				boolean networkConnected = ConnectionClient.isNetworkConnected(point);
				if(networkConnected){
					HttpResponse response = ConnectionClient.connServerForResultPost("/suggestions", json);
					if(response!=null){
						String result = "";
						try {
							result = EntityUtils.toString(response.getEntity(),"UTF-8");
							Log.e("HttpConnect",result);
						} catch (ParseException | IOException e1) {
							e1.printStackTrace();
						}
						int status = response.getStatusLine().getStatusCode();
						if(status==HttpStatus.SC_OK){
							m.what = 2;
							handler.sendMessage(m);
						}
						else{
							m.what = status/100;
							handler.sendMessage(m);
						}
					}
					else{
						m.what = 4;
						handler.sendMessage(m);
					}
				}
				else {
					m.what = 3;
					handler.sendMessage(m);
				}
			}
		}.start();
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
				ad.dismiss();
				if(finish)
					finish();
			}
		});

		ad.show();
	}

}
