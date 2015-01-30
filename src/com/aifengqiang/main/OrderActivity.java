package com.aifengqiang.main;

import com.aifengqiang.ui.NavigationButton;
import com.aifengqiang.ui.NavigationView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

public class OrderActivity extends Activity implements OnClickListener{
	private Button orderButton;

	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_order);
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
				else
				{
					finish();
				}
			}
		};
		nv.setNavigationViewListener(nvl);
		
		orderButton = (Button)findViewById(R.id.order_submit);
		orderButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v == orderButton){
			Intent it = new Intent(this, WaitingActivity.class);
			startActivity(it);
			int version = Integer.valueOf(android.os.Build.VERSION.SDK);
			if(version >= 5) {
				overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
				//overridePendingTransition(android.R.anim.decelerate_interpolator,android.R.anim.decelerate_interpolator);    
				//overridePendingTransition(android.R.anim.overshoot_interpolator,android.R.anim.linear_interpolator);  
			}
		}
	}
}
