package com.aifengqiang.main;

import com.aifengqiang.main.R;
import com.aifengqiang.ui.FoodKindListView;
import com.aifengqiang.ui.GalleryAdapter;
import com.aifengqiang.ui.MenuView;
import com.aifengqiang.ui.NavigationButton;
import com.aifengqiang.ui.NavigationView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnLayoutChangeListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Gallery;
import android.widget.ScrollView;

public class MainActivity extends Activity {
	public int activityHeight;
	public int navViewHeight;
	public int menuViewHeight;
	MainActivity point = this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		NavigationView nv = (NavigationView)findViewById(R.id.main_nav_view);
		nv.setLeftButton(getString(R.string.location),NavigationButton.NAVIGATIONIMAGERIGHT, R.drawable.icon_back_nor_down);
		nv.setRightButton(getString(R.string.show),NavigationButton.NAVIGATIONIMAGENONE, 0);
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
		mv.setItem(0, R.drawable.home_pre, 1, "首页", 0);
		mv.setItem(1, R.drawable.order_nor, 1, "订单", 0);
		mv.setItem(2, R.drawable.icon_tab_qiang, 2, "抢我",R.drawable.tab_qiang_bg_nor);
		mv.setItem(3, R.drawable.me_nor, 1, "我的",0);
		mv.setItem(4, R.drawable.more_nor, 1, "更多",0);
		
		MenuView.MenuViewListener mnl = new MenuView.MenuViewListener() {
			
			@Override
			public void OnMenuViewClick(int id) {
				// TODO Auto-generated method stub
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
				if(id==3){
					Intent it = new Intent(point, UserCenterActivity.class);
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
		
		Gallery gallery = (Gallery)findViewById(R.id.main_gallery_view);
		gallery.setAdapter(new GalleryAdapter(this));
		gallery.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		
		FoodKindListView fklv = (FoodKindListView)findViewById(R.id.main_list_view);
		fklv.addItem("", "本帮菜", "asjacbjkascbakajcbsjak");
		fklv.addItem("", "本帮菜", "asjacbjkascbakajcbsjak");
		fklv.addItem("", "本帮菜", "asjacbjkascbakajcbsjak");
		fklv.addItem("", "本帮菜", "asjacbjkascbakajcbsjak");
		fklv.addItem("", "本帮菜", "asjacbjkascbakajcbsjak");
	}
}
