package com.aifengqiang.main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.util.EncodingUtils;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.aifengqiang.data.GlobalData;
import com.aifengqiang.entity.UserDetailInfo;
import com.aifengqiang.network.ConnectionClient;
import com.aifengqiang.ui.MenuView;
import com.aifengqiang.ui.NavigationButton;
import com.aifengqiang.ui.NavigationView;

import android.R.integer;
import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class UserCenterFragment extends Fragment implements OnClickListener{
	private Activity point;
	private Button loginButton;
	private String idString;
	private UserDetailInfo userDetailInfo;
	private Handler handler;
	private ImageView touxiang;
	private ImageButton uploadTouxiang;
	private TextView nameTextView;
	private ImageButton nameEditButton;
	private TextView remarkTextView;
	private TextView pointTextView;
	private LinearLayout nameLinearLayout, logoutLinearLayout;
	private Dialog ad;
	private EditText text;
	private String editName;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,  
            Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		point = getActivity();
		View content = inflater.inflate(R.layout.fragment_usercenter, container, false);
		
		loginButton = (Button)content.findViewById(R.id.user_login);
		loginButton.setOnClickListener(this);
		
		handler = new Handler(){
			@Override
			public void handleMessage(Message m){
				switch(m.what){
				case 0:
					setViewByLoginStatus(true);
					break;
				case 1:
					nameTextView.setText(editName);
					break;
				case 2:
					showDialog("昵称不能为空，请重新输入");
					break;
				case 3:
					showDialog("网络连接错误,请检查网络连接后再次尝试");
					break;
				case 4:
					showDialog("请求错误！");
					break;
				case 5:
					showDialog("服务器错误，请联系相关工作人员");
					break;
					default:
						break;
				}
			}
		};

		touxiang = (ImageView)content.findViewById(R.id.user_center_photo);
		uploadTouxiang = (ImageButton)content.findViewById(R.id.user_center_upload_photo);
		uploadTouxiang.setOnClickListener(this);
		nameTextView = (TextView)content.findViewById(R.id.user_name);
		nameEditButton = (ImageButton)content.findViewById(R.id.user_name_edit);
		nameEditButton.setOnClickListener(this);
		remarkTextView = (TextView)content.findViewById(R.id.user_center_remark);
		pointTextView = (TextView)content.findViewById(R.id.user_center_point);
		nameLinearLayout = (LinearLayout)content.findViewById(R.id.user_name_logged);
		logoutLinearLayout = (LinearLayout)content.findViewById(R.id.user_center_logout);
		logoutLinearLayout.setOnClickListener(this);
		
		idString = GlobalData.getIntance().getId(point);
		if(idString!=null){
			userDetailInfo = getDetailInfo();
			setViewByLoginStatus(true);
		}
		else {
			setViewByLoginStatus(false);
		}
		
		return content;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v == loginButton){
			Intent it = new Intent(point, LoginActivity.class);
			startActivityForResult(it, 1000);
			int version = Integer.valueOf(android.os.Build.VERSION.SDK);
//			if(version >= 5) {
//				overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
//				//overridePendingTransition(android.R.anim.decelerate_interpolator,android.R.anim.decelerate_interpolator);    
//				//overridePendingTransition(android.R.anim.overshoot_interpolator,android.R.anim.linear_interpolator);  
//			}
		}
		if(v == logoutLinearLayout){
			setViewByLoginStatus(false);
			try {
				FileOutputStream fos = point.openFileOutput("user.info", Context.MODE_PRIVATE);
				String string = "";
				fos.write(string.getBytes());
				fos.close();
				GlobalData.getIntance().setId("");
				Message message = new Message();
				message.what = 0;
				message.arg1 = 0;
				MainActivity.handler.sendMessage(message);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if(v == nameEditButton){
			ad = new Dialog(point,R.style.MyDialog);
			ad.setContentView(R.layout.dialog_edit_name);
			text = (EditText)ad.findViewById(R.id.user_center_dialog_name_edit);
			Button clearButton = (Button)ad.findViewById(R.id.user_center_dialog_clear);
			clearButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					text.setText("");
				}
			});
			Button confirmButton = (Button)ad.findViewById(R.id.user_center_dialog_confirm);
			confirmButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Thread thread = new Thread(){
						@Override
						public void run(){
							editName = text.getText().toString();
							if(editName.length()==0){
								Message message = new Message();
								message.what = 2;
								handler.sendMessage(message);
								return;
							}
							if(ConnectionClient.isNetworkConnected(point)){
								JSONObject jsonObject = new JSONObject();
								try {
									jsonObject.put("name", editName);
									jsonObject.put("gender", "MALE");
									jsonObject.put("birthday", "2015-01-28");
									//Log.e("Edit", editName);
								} catch (JSONException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
								HttpResponse response = ConnectionClient.connServerForResultPutWithHeader("/customer/profile", jsonObject, "X-Customer-Token", idString);
								if(response!=null){
									int status = response.getStatusLine().getStatusCode();
									Log.e("Connect","Login "+status+" "+idString);
									if(status==HttpStatus.SC_NO_CONTENT){
										userDetailInfo = new UserDetailInfo();
										try {
											FileInputStream fis = point.openFileInput("userDetail.txt");
											byte[] read = new byte[fis.available()];
											fis.read(read);
											String readInfo = EncodingUtils.getString(read, "UTF-8");
											userDetailInfo.getUserDetailInfoFromJson(readInfo);
											String nameString = userDetailInfo.getName();
											fis.close();
											String newInfoString="";
											if(nameString!=null){
												newInfoString = readInfo.replace(nameString, editName);
											}
											else {
												
											}
											//Log.e("File",newInfoString);
											FileOutputStream foStream = point.openFileOutput("userDetail.txt",Context.MODE_PRIVATE);
											foStream.write(newInfoString.getBytes());
											foStream.close();
											userDetailInfo.getUserDetailInfoFromJson(newInfoString);
											Message message = new Message();
											message.what = 1;
											handler.sendMessage(message);
										} catch (ParseException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										} catch (IOException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									}
									else{
										Message message = new Message();
										message.what = status/100;
										handler.sendMessage(message);
									}
								}
							}
							else{
								Message message = new Message();
								message.what = 3;
								handler.sendMessage(message);
							}
						}
					};
					ad.dismiss();
					thread.start();
				}
			});
			Button backButton = (Button)ad.findViewById(R.id.user_center_dialog_back);
			backButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					ad.dismiss();
				}
			});
			ad.show();
		}
	}
	
	public UserDetailInfo getDetailInfo(){
		UserDetailInfo info = new UserDetailInfo();
		try {
			FileInputStream fis = point.openFileInput("userDetail.txt");
			byte[] userString = new byte[fis.available()];
			fis.read(userString);
			String string = EncodingUtils.getString(userString, "UTF-8");
			Log.e("TAG","File "+string);
			info.getUserDetailInfoFromJson(string);
			fis.close();
			return info;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public void setViewByLoginStatus(boolean login){
		if(login&&userDetailInfo!=null){
			nameTextView.setText(userDetailInfo.getName());
			remarkTextView.setText(userDetailInfo.getMobile());
			pointTextView.setText(userDetailInfo.getPoints()+"分");
			nameLinearLayout.setVisibility(View.VISIBLE);
			loginButton.setVisibility(View.GONE);
			uploadTouxiang.setVisibility(View.GONE);
			touxiang.setBackgroundResource(R.drawable.touxiang);
		}
		else {
			nameTextView.setText("");
			remarkTextView.setText("登陆后你可享受云同步及更多权利");
			pointTextView.setText("");
			nameLinearLayout.setVisibility(View.GONE);
			loginButton.setVisibility(View.VISIBLE);
			uploadTouxiang.setVisibility(View.GONE);
			touxiang.setBackgroundResource(R.drawable.default0);
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==1000&&resultCode==1001){
			idString = data.getStringExtra("TokenId");
			getUserInfo(idString);
		}
	}
	
	public void showDialog(String text){
		ad = new Dialog(point,R.style.MyDialog);
		ad.setCanceledOnTouchOutside(false);
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
	
	@Override
	public void onResume(){
		super.onResume();
		idString = GlobalData.getIntance().getId(point);
		if(idString!=null){
			getUserInfo(idString);
			userDetailInfo = getDetailInfo();
			setViewByLoginStatus(true);
		}
		else {
			setViewByLoginStatus(false);
		}
	}
	
	public void getUserInfo(final String idString){
		Thread thread = new Thread(){
			@Override
			public void run(){
				if(ConnectionClient.isNetworkConnected(point)){
					HttpResponse response = ConnectionClient.connServerForResultGetWithHeader("/customer/profile", "X-Customer-Token", idString);
					if(response!=null){
						int status = response.getStatusLine().getStatusCode();
						Log.e("Connect","Login "+status+" "+idString);
						if(status==HttpStatus.SC_OK){
							userDetailInfo = new UserDetailInfo();
							try {
								String userInfo = EntityUtils.toString(response.getEntity(),"UTF-8");
								FileOutputStream foStream = point.openFileOutput("userDetail.txt",Context.MODE_PRIVATE);
								foStream.write(userInfo.getBytes());
								foStream.close();
								userDetailInfo.getUserDetailInfoFromJson(userInfo);
								Message message = new Message();
								message.what = 0;
								handler.sendMessage(message);
								
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						else{
							Message message = new Message();
							message.what = status/100;
							handler.sendMessage(message);
						}
					}
				}
				else{
					Message message = new Message();
					message.what = 3;
					handler.sendMessage(message);
				}
			}
				
		};
		thread.start();
	}
}
