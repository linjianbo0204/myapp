package com.aifengqiang.main;

import com.aifengqiang.ui.NavigationButton;
import com.aifengqiang.ui.NavigationView;
import com.aifengqiang.wheel.NumericWheelAdapter;
import com.aifengqiang.wheel.WheelView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
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
	private TextView genderMaleText;
	private TextView genderFemaleText;
	private TextView needBoxText;
	private TextView styleText;
	private TextView costText;
	private LinearLayout boxLinear;
	private LinearLayout styleLinear;
	private LinearLayout costLinear;

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
		
		gender = true;
		box = false;
		needBox = false;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v == orderButton){
			Intent it = new Intent(this, WaitingActivity.class);
			startActivity(it);
			finish();
			int version = Integer.valueOf(android.os.Build.VERSION.SDK);
			if(version >= 5) {
				overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
				//overridePendingTransition(android.R.anim.decelerate_interpolator,android.R.anim.decelerate_interpolator);    
				//overridePendingTransition(android.R.anim.overshoot_interpolator,android.R.anim.linear_interpolator);  
			}
		}
		else if(v == genderMaleButton){
			gender = true;
			genderMaleText.setTextColor(0xfff88727);
			genderFemaleText.setTextColor(0xff666666);
		}
		else if(v == genderFemaleButton){
			gender = false;
			genderFemaleText.setTextColor(0xfff88727);
			genderMaleText.setTextColor(0xff666666);
		}
		else if(v == boxButton){
			if(box){
				box = false;
				boxButton.setChecked(false);
				boxLinear.setVisibility(View.GONE);
			}
			else
			{
				box = true;
				boxLinear.setVisibility(View.VISIBLE);
			}
		}
		else if(v == needBoxButton){
			if(needBox){
				needBox = false;
				needBoxButton.setChecked(false);
				needBoxText.setTextColor(0xff666666);
			}else{
				needBox = true;
				needBoxText.setTextColor(0xfff88727);
			}
		}
		else if(v == styleLinear){
			Intent it = new Intent(this, FoodStyleListChooseActivity.class);
			startActivity(it);
		}
		else if(v == costLinear){
			Dialog ad = new Dialog(this,R.style.MyDialog);
			ad.setContentView(R.layout.dialog_cost);
			WheelView wv = (WheelView)ad.findViewById(R.id.dialog_cost_wheel);
			wv.setAdapter(new NumericWheelAdapter(0, 23));
			wv.setLabel("元");
			ad.show();
		}
	}

}
