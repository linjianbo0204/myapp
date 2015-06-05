package com.aifengqiang.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.aifengqiang.data.GlobalData;
import com.aifengqiang.entity.Flavor;
import com.aifengqiang.network.ConnectionClient;
import com.aifengqiang.ui.NavigationButton;
import com.aifengqiang.ui.NavigationView;
import com.aifengqiang.ui.StyleListView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ScrollView;
import android.widget.TextView;

public class FoodStyleListChooseActivity extends Activity implements OnClickListener{
	private Handler handler;
	private HashMap<Integer, String> foodList;
	private HashMap<Integer, String> selectedfoodList;
	private FoodStyleListChooseActivity pointActivity = this;
	private StyleListView slv;
	private Button submitButton;
	private Dialog ad;
	
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

		foodList = new HashMap<Integer, String>();
		selectedfoodList = new HashMap<Integer, String>();
		slv = (StyleListView)findViewById(R.id.empty_list);
		slv.setContext(pointActivity);
		submitButton = (Button)findViewById(R.id.style_list_submit);
		submitButton.setOnClickListener(pointActivity);
		
		handler = new Handler(){
			@Override
			public void handleMessage(Message m){
				super.handleMessage(m);
				switch (m.what) {
				case 0:
					if(foodList.size()!=0)
						slv.setList(foodList);
					break;
				case 3:
					showDialog("ÍøÂçÁ¬½Ó´íÎó£¬Çë¼ì²é£¡");
					break;
				case 4:
					showDialog("ÇëÇó´íÎó£¡");
					break;
				case 5:
					showDialog("·þÎñÆ÷´íÎó£¡");
					break;
				default:
					break;
				}
			}
		};
		
		ArrayList<Flavor> flavors = GlobalData.getIntance().getFlavors();
		if(flavors!=null&&flavors.size()!=0){
			for(Flavor flavor:flavors){
				foodList.put(flavor.id, flavor.name);
			}
			slv.setList(foodList);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v instanceof CheckBox){
			if(((CheckBox)v).isChecked()){
				int key = (Integer)v.getTag();
				selectedfoodList.put(key, foodList.get(key));
			}
			else{
				int key = (Integer)v.getTag();
				selectedfoodList.remove(key);
			}
		}
		if(v==submitButton){
			Intent it = new Intent();
			String idString ="";
			String styleString="";
			if(selectedfoodList.size()>0){
				for(Integer key:selectedfoodList.keySet()){
					idString += key+",";
					styleString += selectedfoodList.get(key)+",";
				}
				Bundle bundle = new Bundle();
				bundle.putString("id", idString);
				bundle.putString("name", styleString);
				it.putExtra("bundle", bundle);
				setResult(1003,it);
				finish();
			}
			else {
				finish();
			}
		}
	}
	
	public void showDialog(String text){
		ad = new Dialog(pointActivity,R.style.MyDialog);
		ad.setContentView(R.layout.dialog_login);
		ad.setCanceledOnTouchOutside(false);
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
}
