package com.aifengqiang.main;

import java.util.prefs.Preferences;

import com.aifengqiang.data.GlobalData;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.Preference;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class WelcomeActivity extends Activity{
	private ViewPager viewPager;
	private WelcomeAdapter adapter;
	private WelcomeNFirstAdapter nAdapter;
	private WelcomeActivity point = this;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		WindowManager wm = this.getWindowManager();
		int width = wm.getDefaultDisplay().getWidth();
		int height = wm.getDefaultDisplay().getHeight();
		GlobalData.getIntance().setWidthAndHeight(width, height);
		viewPager = (ViewPager)findViewById(R.id.welcome_viewPage_view);
		SharedPreferences preferences = getSharedPreferences("AFQ", Activity.MODE_PRIVATE);
		boolean firstLoad = preferences.getBoolean("FirstLoad", true);
		if(firstLoad){
			adapter = new WelcomeAdapter();
			viewPager.setAdapter(adapter);
			adapter.notifyDataSetChanged();
			Editor edit = preferences.edit();
			edit.putBoolean("FirstLoad", false);
			edit.commit();
		}
		else{
			//nAdapter = new WelcomeNFirstAdapter();
			//viewPager.setAdapter(nAdapter);
			//nAdapter.notifyDataSetChanged();
			Intent it = new Intent(WelcomeActivity.this, MainActivity.class);
			startActivity(it);
			point.finish();
		}
	}
	
	class WelcomeAdapter extends PagerAdapter{

		int[] ids = {R.drawable.yindao13x, R.drawable.yindao23x, R.drawable.yindao33x};
		
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0==arg1;
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 3;
		}
		
		@Override  
        public void destroyItem(ViewGroup container, int position,  
                Object object) {
        }  

        @Override  
        public CharSequence getPageTitle(int position) {  
        	return null;
        } 
		
		@Override  
        public Object instantiateItem(ViewGroup container, int position) {
        	ImageView imageView = new ImageView(point);
        	imageView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        	
        	imageView.setBackgroundResource(ids[position]);
        	if(position == 2){
        		imageView.setOnTouchListener(new OnTouchListener() {
					
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						// TODO Auto-generated method stub
						int width = GlobalData.getIntance().getWidth();
						int height = GlobalData.getIntance().getHeight();
						float touchPositionX = event.getX();
						float touchPositionY = event.getY();
						//Log.e("Position","TouchX: "+touchPositionX+" TouchY: "+touchPositionY+" WindowW: "+width+" WindowH: "+height);
						if(touchPositionX/width >0.2&&touchPositionX/width<0.8&&touchPositionY/height>0.75&&touchPositionY/height<0.90){
							Intent it = new Intent(WelcomeActivity.this, MainActivity.class);
							startActivity(it);
							point.finish();
						}
						return false;
					}
				});
        	}
        	container.addView(imageView);
        	return imageView;  
        }
	}

	class WelcomeNFirstAdapter extends PagerAdapter{

		int[] ids = {R.drawable.yindao32x1};
		
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0==arg1;
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 1;
		}
		
		@Override  
        public void destroyItem(ViewGroup container, int position,  
                Object object) {
        }  

        @Override  
        public CharSequence getPageTitle(int position) {  
        	return null;
        } 
		
		@Override  
        public Object instantiateItem(ViewGroup container, int position) {
        	ImageView imageView = new ImageView(point);
        	imageView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        	
        	imageView.setBackgroundResource(ids[position]);
        	container.addView(imageView);
        	return imageView;  
        }
	}
}
