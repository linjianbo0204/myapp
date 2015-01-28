package com.aifengqiang.main;

import com.aifengqiang.ui.MenuView;
import com.aifengqiang.ui.NavigationButton;
import com.aifengqiang.ui.NavigationView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

public class UserCenterActivity extends Activity{
	private UserCenterActivity point = this;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_user_center);
		NavigationView nv = (NavigationView)findViewById(R.id.user_nav_view);
		nv.setLeftButton(getString(R.string.location),NavigationButton.NAVIGATIONIMAGERIGHT, R.drawable.icon_back_nor_down);
		nv.setTitle(getString(R.string.app_title));
		
		MenuView mv = (MenuView)findViewById(R.id.user_menu_view);
		mv.setItem(0, R.drawable.home_nor, 1, "首页", 0);
		mv.setItem(1, R.drawable.order_nor, 1, "订单", 0);
		mv.setItem(2, R.drawable.icon_tab_qiang, 2, "抢我",R.drawable.tab_qiang_bg_nor);
		mv.setItem(3, R.drawable.me_pre, 1, "我的",0);
		mv.setItem(4, R.drawable.more_nor, 1, "更多",0);
		
		MenuView.MenuViewListener mnl = new MenuView.MenuViewListener() {
			
			@Override
			public void OnMenuViewClick(int id) {
				// TODO Auto-generated method stub
				if(id==0){
					finish();
				}
				if(id==2)
				{
					Intent it = new Intent(point, OrderActivity.class);
					startActivity(it);
					int version = Integer.valueOf(android.os.Build.VERSION.SDK);
					if(version >= 5) {
						overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
						//overridePendingTransition(android.R.anim.decelerate_interpolator,android.R.anim.decelerate_interpolator);    
						//overridePendingTransition(android.R.anim.overshoot_interpolator,android.R.anim.linear_interpolator);  
					}
				}
			}
		};
		mv.setMenuViewListener(mnl);
	}
}
