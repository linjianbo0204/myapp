package com.aifengqiang.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.jar.Attributes.Name;

import com.aifengqiang.data.GlobalData;
import com.aifengqiang.entity.FoodListItemDetail;
import com.aifengqiang.entity.Items;
import com.aifengqiang.ui.NavigationView;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class FoodListActivity extends Activity{
	private NavigationView navigationView;
	private ListView firstLevel, secondLevel;
	private boolean withPic;
	private HashMap<String, ArrayList<Items>> foodListHashMap;
	private FoodListActivity point = this;
	private FoodListFirstLevelAdapter firstAdapter;
	private Handler handler;
	private int length;
	@Override
	protected void onCreate(Bundle savedInstanceBundle){
		super.onCreate(savedInstanceBundle);
		setContentView(R.layout.activity_food_list);
		
		String title = this.getIntent().getExtras().getString("name");
		navigationView = (NavigationView)findViewById(R.id.food_list_nav_view);
		navigationView.setLeftButton(getString(R.string.back), NavigationView.NAVIGATION_BUTTON_LEFT, R.drawable.icon_back_nor);
		navigationView.setTitle(title);
		navigationView.setRightButton(getString(R.string.pic), NavigationView.NAVIGATION_BUTTON_RIGHT, R.drawable.icon_forward_pre);
		NavigationView.NavigationViewListener nvl = new NavigationView.NavigationViewListener() {
			@Override
			public void OnNavigationButtonClick(int id) {
				// TODO Auto-generated method stub
				if(id == NavigationView.NAVIGATION_BUTTON_LEFT){
					finish();
				}
				else if(id == NavigationView.NAVIGATION_BUTTON_RIGHT)
				{
					navigationView.setRightButton(withPic?getString(R.string.pic):getString(R.string.nopic), NavigationView.NAVIGATION_BUTTON_RIGHT, R.drawable.icon_forward_pre);
					withPic = !withPic;
					if(length!=0)
						init();
				}
			}
		};
		navigationView.setNavigationViewListener(nvl);
		
		ArrayList<Items> secondLevelDetail = new ArrayList<Items>();
		foodListHashMap = new HashMap<>();
		secondLevelDetail = GlobalData.getIntance().getRestaurantInfo().getItems();
		for(Items items : secondLevelDetail){
			if(!foodListHashMap.containsKey(items.getCategory().getName())){
				ArrayList<Items> items2 = new ArrayList<>();
				items2.add(items);
				foodListHashMap.put(items.getCategory().getName(), items2);
			}
			else{
				foodListHashMap.get(items.getCategory().getName()).add(items);
			}
		}
		
		firstLevel = (ListView)findViewById(R.id.food_list_first_level);
		secondLevel = (ListView)findViewById(R.id.food_list_second_level);
		ArrayList<String> firstLevelStrings = new ArrayList<>(foodListHashMap.keySet());
		//firstLevelStrings.add("Μπµγ");
		//foodListHashMap.put("Μπµγ", new ArrayList<Items>());
		firstAdapter = new FoodListFirstLevelAdapter(this, firstLevelStrings);
		firstLevel.setAdapter(firstAdapter);
		length = firstLevelStrings.size();
		if(length!=0)
			init();
		handler = new Handler(){
			@Override
			public void handleMessage(Message m){
				switch(m.what){
				case 0:
					((FoodSecondFirstLevelPicAdapter)secondLevel.getAdapter()).notifyDataSetChanged();
					break;
					default:break;
				}
			}
		};
	}
	
	public void init(){
		if(withPic){
			firstLevel.setSelection(0);
			firstLevel.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					((FoodListFirstLevelAdapter)firstLevel.getAdapter()).setSelected(position);
					((FoodListFirstLevelAdapter)firstLevel.getAdapter()).notifyDataSetChanged();
					
					FoodSecondFirstLevelPicAdapter secondAdapter = new FoodSecondFirstLevelPicAdapter(point, foodListHashMap.get(firstAdapter.getSelected()));
					secondLevel.setAdapter(secondAdapter);
				}
			});
			
			FoodSecondFirstLevelPicAdapter secondAdapter = new FoodSecondFirstLevelPicAdapter(this, foodListHashMap.get(firstAdapter.getSelected()));
			secondLevel.setAdapter(secondAdapter);
			new Thread(){
				@Override
				public void run(){
					while(withPic){
						Message message = new Message();
						message.what = 0;
						handler.sendMessage(message);
						try{
							sleep(3000);
						}
						catch(Exception e){
							
						}
					}
				}
			}.start();
		}
		else{
			firstLevel.setSelection(0);
			firstLevel.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					((FoodListFirstLevelAdapter)firstLevel.getAdapter()).setSelected(position);
					((FoodListFirstLevelAdapter)firstLevel.getAdapter()).notifyDataSetChanged();
					FoodSecondFirstLevelNoPicAdapter secondAdapter = new FoodSecondFirstLevelNoPicAdapter(point, foodListHashMap.get(firstAdapter.getSelected()));
					secondLevel.setAdapter(secondAdapter);
				}
			});
			FoodSecondFirstLevelNoPicAdapter secondAdapter = new FoodSecondFirstLevelNoPicAdapter(this, foodListHashMap.get(firstAdapter.getSelected()));
			secondLevel.setAdapter(secondAdapter);
		}
	}
	
	public class FoodListFirstLevelAdapter extends BaseAdapter{
		private Context mContext;
		private ArrayList<String> firstLevelNames;
		private int selected=0;
		public FoodListFirstLevelAdapter(Context mContext){
			this.mContext = mContext;
		}
		
		public FoodListFirstLevelAdapter(Context mContext, ArrayList<String> names){
			this.mContext = mContext;
			this.firstLevelNames = names;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return firstLevelNames.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return firstLevelNames.get(position);
		}
		
		public String getSelected(){
			return firstLevelNames.get(selected);
		}
		
		public void setSelected(int selected){
			this.selected = selected;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			LayoutInflater inflater  = LayoutInflater.from(mContext);
			convertView = inflater.inflate(R.layout.food_list_first_level_item, null);
			TextView name = (TextView)convertView.findViewById(R.id.food_list_first_level_name);
			if(position==selected){
				name.setTextColor(Color.parseColor("#f88727"));
			}
			name.setText(firstLevelNames.get(position));
			return convertView;
		}
		
	}
	
	public class FoodSecondFirstLevelPicAdapter extends BaseAdapter{
		private Context mContext;
		private ArrayList<Items> secondLevelItems;
		public FoodSecondFirstLevelPicAdapter(Context mContext){
			this.mContext = mContext;
		}
		
		public FoodSecondFirstLevelPicAdapter(Context mContext, ArrayList<Items> names){
			this.mContext = mContext;
			this.secondLevelItems = names;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return secondLevelItems.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return secondLevelItems.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			LayoutInflater inflater  = LayoutInflater.from(mContext);
			convertView = inflater.inflate(R.layout.food_list_second_level_item_pic, null);
			TextView name = (TextView)convertView.findViewById(R.id.food_list_second_level_item_name);
			TextView cost = (TextView)convertView.findViewById(R.id.food_list_second_level_item_cost);
			ImageView imageView = (ImageView)convertView.findViewById(R.id.food_list_second_level_item_img);
			
			name.setText(secondLevelItems.get(position).getName());
			cost.setText(secondLevelItems.get(position).getPrice());
			if(secondLevelItems.get(position).getImage().getBitmap()!=null)
				imageView.setImageBitmap(secondLevelItems.get(position).getImage().getBitmap());
			return convertView;
		}
		
	}
	public class FoodSecondFirstLevelNoPicAdapter extends BaseAdapter{
		private Context mContext;
		private ArrayList<Items> secondLevelItems;
		public FoodSecondFirstLevelNoPicAdapter(Context mContext){
			this.mContext = mContext;
		}
		
		public FoodSecondFirstLevelNoPicAdapter(Context mContext, ArrayList<Items> names){
			this.mContext = mContext;
			this.secondLevelItems = names;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return secondLevelItems.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return secondLevelItems.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			LayoutInflater inflater  = LayoutInflater.from(mContext);
			convertView = inflater.inflate(R.layout.food_list_second_level_item_nopic, null);
			TextView name = (TextView)convertView.findViewById(R.id.food_list_second_level_name_nopic);
			TextView cost = (TextView)convertView.findViewById(R.id.food_list_second_level_cost_nopic);
			
			name.setText(secondLevelItems.get(position).getName());
			cost.setText(secondLevelItems.get(position).getPrice());
			return convertView;
		}
		
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		withPic = false;
	}
}
