package com.aifengqiang.main;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import java.util.zip.Inflater;

import net.sourceforge.pinyin4j.PinyinHelper;

import com.aifengqiang.data.GlobalData;
import com.aifengqiang.entity.City;
import com.aifengqiang.tool.HanziToPinyin;
import com.aifengqiang.ui.AFQGridView;
import com.aifengqiang.ui.NavigationButton;
import com.aifengqiang.ui.NavigationView;
import com.aifengqiang.ui.SlideBar;
import com.aifengqiang.ui.SlideBar.OnTouchLetterChangeListenner;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class LocationCityActivity extends Activity{
	private NavigationView nv;
	private LocationCityActivity point = this;
	private ListView listView;
	private SlideBar slideBar;
	private EditText editText;
	private CityListViewAdapter adapter;
	private ArrayList<City> cities;
	private ArrayList<String> cityNames;
	private SharedPreferences preferences;
	public LocationClient mLocationClient = null;
	public BDLocationListener myListener = new MyLocationListener();
	public Handler handler;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loaction);
		nv = (NavigationView)findViewById(R.id.location_nav_view);
		nv.setLeftButton(getString(R.string.back), NavigationButton.NAVIGATIONIMAGELEFT, R.drawable.navigation_back_bg);
		nv.setTitle(getString(R.string.app_title));
		
		preferences = getSharedPreferences("AFQ", Activity.MODE_PRIVATE);
		
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
		
		listView = (ListView)findViewById(R.id.location_list);
		cities = GlobalData.getIntance().getCities(point);
		if(cities==null||cities.size()==0){
			cities = new ArrayList<>();
			cities.add(new City(1, "上海"));
		}
		cityNames = getSortedCityNames();
		adapter = new CityListViewAdapter(this, cityNames);
		listView.setAdapter(adapter);
		
		slideBar = (SlideBar)findViewById(R.id.location_slide);
		slideBar.setOnTouchLetterChangeListenner(new OnTouchLetterChangeListenner() {
			
			@Override
			public void onTouchLetterChange(MotionEvent event, String s) {
				// TODO Auto-generated method stub
				int position = 0;
				Log.e("LocationCity",s);
				if(s!="#"){
					position = getLetterPosition(s.charAt(0));
				}
				if(position !=-1)
					listView.setSelection(position+2);
				else {
					listView.setSelection(0);
				}
			}
		});
		
		editText = (EditText)findViewById(R.id.location_city_edit);
		editText.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				// TODO Auto-generated method stub
				if (actionId==EditorInfo.IME_ACTION_SEARCH ||actionId==EditorInfo.IME_ACTION_SEND||(event!=null&&(event.getKeyCode()== KeyEvent.KEYCODE_SEARCH||event.getKeyCode()==KeyEvent.KEYCODE_ENTER))) 
				{                
					//do something;
					String textString = editText.getText().toString();
					int position = 0;
					if(textString!=null){
						for(int i = 0; i<cityNames.size(); i++){
							if(cityNames.get(i).contains(textString)){
								position = i;
								break;
							}
						}
						//Log.d("Location","position:"+position);
						if(position !=-1)
							listView.setSelection(position+2);
						else {
							listView.setSelection(0);
						}
					}
					return true;             
				}               
				return false; 
			}
		});
		
		//cities = GlobalData.getIntance().getCities(point);
		mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
	    mLocationClient.registerLocationListener( myListener );    //注册监听函数
	    LocationClientOption option = new LocationClientOption();

	    option.setOpenGps(true);

	    option.setCoorType("bd09ll");

	    option.setScanSpan(2000);

	    option.setLocationMode(LocationMode.Hight_Accuracy);

	    option.setIsNeedAddress(true);

	    option.setNeedDeviceDirect(true);

	    mLocationClient.setLocOption(option);

	    mLocationClient.start();

	    mLocationClient.requestLocation();
	   // mLocationClient.requestOfflineLocation();
	    handler = new Handler(){
	    	@Override
	    	public void handleMessage(Message message){
	    		switch (message.what) {
				case 0:
					listView.setAdapter(adapter);
					break;

				default:
					break;
				}
	    	}
	    };
	}
	
	public int getLetterPosition(char s){
		int position = -1;
		for(int i = 0; i<cityNames.size();i++){
			Log.d("Search",cityNames.get(i)+" "+s);
			if(cityNames.get(i).charAt(0)==s)
				position = i;
		}
		if(position==-1&&s!='Z'){
			return getLetterPosition((char)(s+1));
		}
		return position;
	}
	
	public ArrayList<String> getSortedCityNames(){
		TreeSet<String> firstStrings = new TreeSet<String>();
		String[] nameContainer = new String[cities.size()];
		ArrayList<String> nameStrings = new ArrayList<String>();
		if(cities!=null){
			int i=0;
			for(City city : cities){
				firstStrings.add(PinyinHelper.toHanyuPinyinStringArray(city.name.charAt(0))[0].substring(0,1));
				nameContainer[i] = city.name;
				i++;
			}
			Arrays.sort(nameContainer,Collator.getInstance(java.util.Locale.CHINA));
			Iterator it=firstStrings.iterator();
			String firstString = (String) it.next();
			nameStrings.add(firstString.toUpperCase());
			for(String nameString : nameContainer){
				if(!PinyinHelper.toHanyuPinyinStringArray(nameString.charAt(0))[0].substring(0,1).equals(firstString)){
					firstString = (String) it.next();
					nameStrings.add(firstString.toUpperCase());
				}
				nameStrings.add(nameString);
				//Log.d("CityName", nameString+"");
			}
			return nameStrings;
		}
		return null;
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		if(mLocationClient.isStarted())
			mLocationClient.stop();
	}
	
	
	public class CityListViewAdapter extends BaseAdapter{
		private Context mContext;
		private ArrayList<String> citynameList;
		private ArrayList<String> mostUseList;
		private GridAdapter gridAdapter;
		public int selectIndex;
		
		public CityListViewAdapter(Context context, ArrayList<String> citynames){
			this.mContext = context;
			this.citynameList = citynames;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return citynameList.size()+2;
		}
		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			if(position<2)
				return "0";
			return citynameList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			LayoutInflater inflater = LayoutInflater.from(mContext);
			final int select = position;
			if(position==0){
				convertView = inflater.inflate(R.layout.list_item_head, null);
				TextView textView = (TextView) convertView.findViewById(R.id.list_item_head_text);
				textView.setText(GlobalData.getIntance().getLocationCity()!=null?GlobalData.getIntance().getLocationCity().name+"(GPS定位)":"定位中...");
				convertView.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						City city = GlobalData.getIntance().getLocationCity();
						preferences.edit().putString("city", city==null?"":city.name).commit();
						setResult(1005);
						finish();
					}
				});
			}
			else if(position==1){
				convertView = inflater.inflate(R.layout.list_item_grid, null);
				AFQGridView gridView = (AFQGridView)convertView.findViewById(R.id.list_item_grid_view);
				ArrayList<String> nameArrayList = new ArrayList<>();
				nameArrayList.add("上海");
				nameArrayList.add("苏州");
				gridAdapter = new GridAdapter(mContext, nameArrayList);
				gridView.setAdapter(gridAdapter);
				//button.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
			}
			else 
			{
				if(citynameList.get(position-2).length()==1){
					convertView = inflater.inflate(R.layout.list_item_letter, null);
					TextView textView = (TextView)convertView.findViewById(R.id.list_item_letter_text);
					textView.setText(citynameList.get(position-2).toUpperCase());
				}
				else {
					convertView = inflater.inflate(R.layout.list_item_tail, null);
					TextView textView = (TextView)convertView.findViewById(R.id.list_item_tail_text);
					textView.setText(citynameList.get(position-2));
					convertView.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							preferences.edit().putString("city", citynameList.get(select-2)).commit();
							setResult(1005);
							finish();
						}
					});
				}
			}
			return convertView;
		}
		
	}
	
	public class GridAdapter extends BaseAdapter{
		private Context mContext;
		private ArrayList<String> nameList;
		private int selectPosition;
		
		public GridAdapter(Context mContext, ArrayList<String> name){
			this.mContext = mContext;
			this.nameList = name;
			selectPosition = -1;
		}
		
		public void setSelectIndex(int index){
			this.selectPosition = index;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return nameList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return nameList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			LayoutInflater inflater = LayoutInflater.from(mContext);
			convertView = inflater.inflate(R.layout.grid_item_button, null); 
			Button button = (Button)convertView.findViewById(R.id.grid_item_button);
			final int select = position;
			button.setText(nameList.get(select));
			button.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					preferences.edit().putString("city", nameList.get(select)).commit();
					setResult(1005);
					finish();
				}
			});
			return convertView;
		}
		
	}
	
	public class MyLocationListener implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null)
		        return;
			StringBuffer sb = new StringBuffer(256);
			sb.append("time : ");
			sb.append(location.getTime());
			sb.append("\nerror code : ");
			sb.append(location.getLocType());
			sb.append("\nlatitude : ");
			sb.append(location.getLatitude());
			sb.append("\nlontitude : ");
			sb.append(location.getLongitude());
			sb.append("\nradius : ");
			sb.append(location.getRadius());
			if (location.getLocType() == BDLocation.TypeGpsLocation){
				sb.append("\nspeed : ");
				sb.append(location.getSpeed());
				sb.append("\nsatellite : ");
				sb.append(location.getSatelliteNumber());
			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation){
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
			} 
 
			Log.d("Location",sb.toString());
			String locationString = location.getCity();
			if(locationString!=null){
				GlobalData.getIntance().setLocationCity(locationString.substring(0, locationString.length()-1));
				mLocationClient.stop();
				Message message = new Message();
				message.what = 0;
				handler.sendMessage(message);
			}
		}
	}
	
}