package com.aifengqiang.main;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.aifengqiang.data.GlobalData;
import com.aifengqiang.entity.EventInfo;
import com.aifengqiang.entity.Flavor;
import com.aifengqiang.network.ConnectionClient;
import com.aifengqiang.ui.AFQListView;
import com.aifengqiang.ui.FoodKindListView;
import com.aifengqiang.ui.GalleryAdapter;
import com.baidu.platform.comapi.map.m;

import android.R.integer;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

public class MainFragment extends Fragment{

	public int activityHeight;
	public int navViewHeight;
	public int menuViewHeight;
	//private LinearLayout galleryIcon;
	private Dialog ad;
	//private ViewPager viewPager;
	private ArrayList<EventInfo> eventInfos;
	//private EventPagerAdapter adapter;
	private Handler handler;
	private int selectedIndex;
	//private AFQListView fklv;
	//private FlavorStyleListAdapter flavorAdapter;
	private ImageView imageView;
	private TextView textView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,  
            Bundle savedInstanceState){
		super.onCreateView(inflater, container, savedInstanceState);
		View content = inflater.inflate(R.layout.fragment_main, container, false);
		//viewPager = (ViewPager)content.findViewById(R.id.main_viewPage_view);
		
		//galleryIcon = (LinearLayout)content.findViewById(R.id.main_gallery_radio);
		imageView = (ImageView)content.findViewById(R.id.fragment_main_image);
		textView = (TextView)content.findViewById(R.id.fragment_main_text);
		handler = new Handler(){
			@Override
			public void handleMessage(Message message){
				switch (message.what) {
				case 0:
					textView.setVisibility(View.GONE);
					imageView.setVisibility(View.VISIBLE);
					Drawable drawable = new BitmapDrawable(eventInfos.get(0).getBanner().getBitmap());
					imageView.setBackgroundDrawable(drawable);
					//adapter.notifyDataSetChanged();
					//viewPager.setCurrentItem(selectedIndex);
					break;
				case 1:
					//flavorAdapter.notifyDataSetChanged();
					break;
				default:
					break;
				}
			}
		};

		//fklv = (AFQListView)content.findViewById(R.id.main_list_view);
		
		return content;
	}
	
	public void setAdapter(){
		eventInfos = GlobalData.getIntance().getEventInfos();
		if(eventInfos!=null){
			//adapter = new EventPagerAdapter(getActivity(), eventInfos);
			//viewPager.setAdapter(adapter);
			new Thread(){
    			@Override
    			public void run(){
    				URL uri;
    				try {
    	    			for(int i = 0;i<1;i++){
    	    				EventInfo eventInfo = eventInfos.get(i);
    						uri = new URL(ConnectionClient.connectionImage+eventInfo.getBanner().getUrl());
    						HttpURLConnection conn = (HttpURLConnection) uri.openConnection();   
    						conn.setDoInput(true);   
    						conn.connect();   
    						InputStream is = conn.getInputStream();
    						Bitmap bitmap = BitmapFactory.decodeStream(is); 
    						eventInfo.getBanner().setImage(bitmap);
    						is.close();
    						Message msg = new Message();
    						msg.what = 0;
    						handler.sendMessage(msg);
    	    			}
    				} catch (IOException e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				}
    			}
    		}.start();
//			for(int i = 0; i< 1 ;i++)
//			{
//				ImageView radio = new ImageView(getActivity());
//				LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
//				layoutParams.rightMargin = 10;
//				radio.setLayoutParams(layoutParams);
//				if(i==0)
//					radio.setImageResource(R.drawable.dot_pre);
//				else {
//					radio.setImageResource(R.drawable.dot_nor);
//				}
//				//galleryIcon.addView(radio);
//			}
		}
//		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
//			
//			@Override
//			public void onPageSelected(int arg0) {
//				// TODO Auto-generated method stub
//				selectedIndex = arg0;
//				for(int i = 0;i<eventInfos.size();i++){
//					if(arg0 == i){
//						ImageView icon = (ImageView)galleryIcon.getChildAt(i);
//						icon.setImageResource(R.drawable.dot_pre);
//					}
//					else{
//						ImageView icon = (ImageView)galleryIcon.getChildAt(i);
//						icon.setImageResource(R.drawable.dot_nor);
//					}
//				}
//			}
//			
//			@Override
//			public void onPageScrolled(int arg0, float arg1, int arg2) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			@Override
//			public void onPageScrollStateChanged(int arg0) {
//				// TODO Auto-generated method stub
//			}
//		});

	}
	
//	public void setFlavorAdapter(){
//		final ArrayList<Flavor> flavors = GlobalData.getIntance().getFlavors();
//		if(flavors!=null&&flavors.size()>0){
//			flavorAdapter = new FlavorStyleListAdapter(getActivity(), flavors);
//			fklv.setAdapter(flavorAdapter);
//		}
//		Log.d("Flavor","refresh");
//		new Thread(){
//			@Override
//			public void run(){
//				URL uri;
//				try {
//	    			for(int i = 0;i<flavors.size();i++){
//	    				Flavor flavor = flavors.get(i);
//						uri = new URL(ConnectionClient.connectionImage+flavor.banner.getUrl());
//						HttpURLConnection conn = (HttpURLConnection) uri.openConnection();   
//						conn.setDoInput(true);   
//						conn.connect();   
//						InputStream is = conn.getInputStream();
//						Bitmap bitmap = BitmapFactory.decodeStream(is); 
//						flavor.banner.setImage(bitmap);
//						is.close();
//						Message msg = new Message();
//						msg.what = 1;
//						handler.sendMessage(msg);
//	    			}
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		}.start();
//	}
	
	public void showDialog(String text){
		ad = new Dialog(getActivity(),R.style.MyDialog);
		ad.setContentView(R.layout.dialog_login);
		TextView tv = (TextView)ad.findViewById(R.id.dialog_text);
		tv.setText(text);
		Button btn = (Button)ad.findViewById(R.id.dialog_button_one_sure);
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ad.dismiss();
			}
		});
		ad.show();
	}
	
	public class EventPagerAdapter extends PagerAdapter {
		
		private ArrayList<EventInfo> eventInfos;
		private Context mContext;
		private int mChildCount;
		
		public EventPagerAdapter(Context mContext, ArrayList<EventInfo> eventInfos){
			this.eventInfos = eventInfos;
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
			return eventInfos.size();
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
        	ImageView imageView = new ImageView(getActivity());
        	imageView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    		final EventInfo eventInfo = eventInfos.get(position);
        	if(eventInfo.getBanner().getBitmap()==null){
        		
        	}
        	else
        		imageView.setImageBitmap(eventInfos.get(position).getBanner().getBitmap());
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
	
	@Override
	public void onConfigurationChanged(Configuration newConfig){
		super.onConfigurationChanged(newConfig);
		//adapter = new EventPagerAdapter(getActivity(), eventInfos);
		//viewPager.setAdapter(adapter);
		//Log.d("Configuration","Changed");
	}
	
	public class FlavorStyleListAdapter extends BaseAdapter{
		
		private ArrayList<Flavor> flavors;
		private Context mContext;

		public FlavorStyleListAdapter(Context mContext , ArrayList<Flavor> flavors){
			this.mContext = mContext;
			this.flavors = flavors;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return flavors.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return flavors.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			LayoutInflater inflater = LayoutInflater.from(mContext);
			convertView = inflater.inflate(R.layout.food_kind_show_list, null);
			
			ImageView image = (ImageView)convertView.findViewById(R.id.food_list_image);
			Bitmap sourceBmp = flavors.get(position).banner.getBitmap();
			String title = flavors.get(position).name;
			String detail = flavors.get(position).description;
			if(sourceBmp!=null)
				image.setImageBitmap(sourceBmp);
			
			TextView titleText = (TextView) convertView.findViewById(R.id.food_list_title);
			titleText.setText(title);
			
			TextView detailText = (TextView) convertView.findViewById(R.id.food_list_detail);
			detailText.setText(detail);
			return convertView;
		}
		
	}
}
