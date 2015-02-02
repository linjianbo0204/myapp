package com.aifengqiang.main;

import java.util.ArrayList;

import com.aifengqiang.ui.NavigationButton;
import com.aifengqiang.ui.NavigationView;
import com.aifengqiang.ui.StyleListView;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ScrollView;

public class FoodStyleListChooseActivity extends Activity implements OnClickListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activty_style_list);
		NavigationView nv = (NavigationView)findViewById(R.id.empty_nav_view);
		nv.setLeftButton(getString(R.string.back),NavigationButton.NAVIGATIONIMAGELEFT, R.drawable.icon_back_nor);
		nv.setTitle(getString(R.string.food_style));
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
		
		StyleListView slv = (StyleListView)findViewById(R.id.empty_list);
		ArrayList<String> strs = new ArrayList<String>();
		strs.add("´¨²Ë");
		strs.add("´¨²Ë");
		strs.add("´¨²Ë");
		strs.add("´¨²Ë");
		strs.add("´¨²Ë");
		strs.add("´¨²Ë");
		strs.add("´¨²Ë");
		slv.setList(strs);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
}
