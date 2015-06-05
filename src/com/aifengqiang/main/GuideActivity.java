package com.aifengqiang.main;

import java.util.ArrayList;

import com.aifengqiang.entity.EventInfo;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class GuideActivity extends Activity{
	private LinearLayout galleryIcon;
	private ViewPager viewPager;
	private GuideEventPagerAdapter adapter;
	private Button close;
	private int selectedIndex;
	int[] sourcesImage = new int[]{
		R.drawable.showhome, R.drawable.showlogin, R.drawable.showusercenterpre,
		R.drawable.showusercenter, R.drawable.showedituser, R.drawable.showorder,
		R.drawable.showwaitingpre, R.drawable.showwaiting, //R.drawable.showrestaurant,
		R.drawable.showfood, R.drawable.showorderlist, R.drawable.showcomment,
		R.drawable.showlocation, R.drawable.showmore, R.drawable.showaboutus
	};
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide);

		viewPager = (ViewPager)findViewById(R.id.activity_guide_view);
		close = (Button)findViewById(R.id.activity_guide_close);
		galleryIcon = (LinearLayout)findViewById(R.id.activity_guide_icon);

		for(int i = 0; i< sourcesImage.length ;i++)
		{
			ImageView radio = new ImageView(this);
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
			layoutParams.rightMargin = 10;
			radio.setLayoutParams(layoutParams);
			if(i==0)
				radio.setImageResource(R.drawable.dot_pre);
			else {
				radio.setImageResource(R.drawable.dot_nor);
			}
			galleryIcon.addView(radio);
		}

		adapter = new GuideEventPagerAdapter(this);
		viewPager.setAdapter(adapter);
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				selectedIndex = arg0;
				for(int i = 0;i<sourcesImage.length;i++){
					if(arg0 == i){
						ImageView icon = (ImageView)galleryIcon.getChildAt(i);
						icon.setImageResource(R.drawable.dot_pre);
					}
					else{
						ImageView icon = (ImageView)galleryIcon.getChildAt(i);
						icon.setImageResource(R.drawable.dot_nor);
					}
				}
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
			}
		});
		
		close.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
	
	private class GuideEventPagerAdapter extends PagerAdapter {
	
		private Context mContext;
		private int mChildCount;
		
		public GuideEventPagerAdapter(Context mContext){
			this.mContext = mContext;
		}

	     @Override
	     public void notifyDataSetChanged() {         
	           mChildCount = getCount();
	           super.notifyDataSetChanged();
	     }
	 
	     @Override
	     public int getItemPosition(Object object)   {          
	           if ( mChildCount > 0) {
	        	   mChildCount --;
	        	   return POSITION_NONE;
	           }
	           return super.getItemPosition(object);
	     }
		
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return sourcesImage.length;
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
        	ImageView imageView = new ImageView(mContext);
        	imageView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        	imageView.setBackgroundResource(sourcesImage[position]);
        	imageView.setOnClickListener(new OnClickListener() {
				
        		@Override
        		public void onClick(View v) {
        			// TODO Auto-generated method stub
					
        		}
        	});
        	container.addView(imageView);
        	return imageView;  
        }
         
	}
}
