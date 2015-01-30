package com.aifengqiang.main;

import com.aifengqiang.ui.NavigationButton;
import com.aifengqiang.ui.NavigationView;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class WaitingActivity extends Activity implements OnClickListener{
	private NavigationView nv;
	private TextView munites;
	private TextView seconds;
	private TextView waitingList;
	int secondCount;
    private Handler handler;  
	
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
		
		munites = (TextView)findViewById(R.id.waiting_munites);
		seconds = (TextView)findViewById(R.id.waiting_seconds);
		
		waitingList = (TextView)findViewById(R.id.waiting_shop_text);
		secondCount = 0;
		
		
		handler=new Handler(){
			@Override
	        public void handleMessage(Message msg) {
	             switch (msg.what) {
	                 case 0: {
	                 }
	                 case 1:{
	                	 int i = msg.arg1/60;
	                	 if(i<10){
	                		 munites.setText("0"+i);
	                	 }
	                	 else
	                	 {
	                		 munites.setText(i+"");
	                	 }
	                	 i = msg.arg1%60;
	                	 if(i<10){
	                		 seconds.setText("0"+i);
	                	 }
	                	 else
	                	 {
	                		 seconds.setText(i+"");
	                	 }
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
				while(true){
					Message m = new Message();
					m.what = 1;
					m.arg1 = secondCount;
					handler.sendMessage(m);
					secondCount++;
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
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
	
}
