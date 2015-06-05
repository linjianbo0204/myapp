package com.aifengqiang.main;

import com.aifengqiang.ui.NavigationButton;
import com.aifengqiang.ui.NavigationView;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;

public class AboutUsActivity extends Activity{
	private NavigationView navigationView;
	private TextView versionTextView;
	private TextView appTextView;
	private TextView rightTextView;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about_us);
		navigationView = (NavigationView)findViewById(R.id.about_us_nav_view);
		navigationView.setLeftButton(getString(R.string.back), NavigationButton.NAVIGATIONIMAGELEFT, R.drawable.navigation_back_bg);
		navigationView.setTitle(getString(R.string.app_title));
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
		navigationView.setNavigationViewListener(nvl);
		
		versionTextView = (TextView)findViewById(R.id.about_us_version);
		String versionString = "";
		String version = getString(R.string.version_detail);
		String[] versionsStrings = version.split("\\.");
		//Log.e("AboutUs",version +" "+versionsStrings.length);
		for(String s:versionsStrings){
			versionString += "<p>"+"-"+s+"</p>";
		}
		versionTextView.setText(Html.fromHtml(versionString));
		
		appTextView = (TextView)findViewById(R.id.about_us_app_detail);
		appTextView.setText(Html.fromHtml("  "+getString(R.string.app_detail)));
		
		rightTextView = (TextView)findViewById(R.id.about_us_right);
		rightTextView.setText("@2015 All Right Reserved");
	}

}
