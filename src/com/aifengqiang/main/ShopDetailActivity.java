package com.aifengqiang.main;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import com.aifengqiang.ui.NavigationButton;
import com.aifengqiang.ui.NavigationView;

public class ShopDetailActivity extends Activity{
	private NavigationView nv;
	
	@Override
	protected void onCreate(Bundle onSavedInstance){
	super.onCreate(onSavedInstance);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_shop_detail);
		nv = (NavigationView)findViewById(R.id.detail_nav_view);
		nv.setLeftButton(getString(R.string.back),NavigationButton.NAVIGATIONIMAGELEFT, R.drawable.icon_back_nor);
		nv.setTitle(getString(R.string.app_title));
		nv.setRightButton(getString(R.string.choose),NavigationButton.NAVIGATIONIMAGENONE, 0);
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
	}
}
