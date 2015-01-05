package com.aifengqiang.main;

import com.aifengqiang.main.R;
import com.aifengqiang.ui.MenuView;
import com.aifengqiang.ui.NavigationView;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		NavigationView nv = (NavigationView)findViewById(R.id.main_nav_view);
		nv.setLeftButton(getString(R.string.location));
		nv.setRightButton(getString(R.string.show));
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
		
		MenuView mv = (MenuView)findViewById(R.id.main_menu_view);
		mv.setItem(0, R.drawable.ic_launcher, 1);
		mv.setItem(1, R.drawable.ic_launcher, 1);
		mv.setItem(2, R.drawable.ic_launcher, 2);
		mv.setItem(3, R.drawable.ic_launcher, 1);
		mv.setItem(4, R.drawable.ic_launcher, 1);
		
		MenuView.MenuViewListener mnl = new MenuView.MenuViewListener() {
			
			@Override
			public void OnMenuViewClick(int id) {
				// TODO Auto-generated method stub
				finish();
			}
		};
		mv.setMenuViewListener(mnl);
	}
}
