package com.aifengqiang.main;

import java.io.File;

import android.app.Activity;
import android.app.Fragment;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView.FindListener;
import android.widget.LinearLayout;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.aifengqiang.ui.MenuView;
import com.aifengqiang.ui.NavigationButton;
import com.aifengqiang.ui.NavigationView;

public class MoreFragment extends Fragment{
	private Activity point;
	private LinearLayout aboutUs;
	private LinearLayout share;
	private LinearLayout suggestion;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,  
            Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		point = getActivity();
		View contentView = inflater.inflate(R.layout.fragment_more, container, false);
		aboutUs = (LinearLayout)contentView.findViewById(R.id.fragment_more_aboutus);
		aboutUs.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent it = new Intent(point, AboutUsActivity.class);
				startActivity(it);
			}
		});
		share = (LinearLayout)contentView.findViewById(R.id.fragment_more_share);
		share.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				Resources r = point.getResources();
//				Uri uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
//					    + r.getResourcePackageName(R.drawable.icon) + "/"
//					    + r.getResourceTypeName(R.drawable.icon) + "/"
//					    + r.getResourceEntryName(R.drawable.icon)+".png");
//				String filePath = getAbsoluteImagePath(getActivity(), uri);
//				String filePath = "mnt/sdcard/Fandian/icon.jpg";
//				Log.e("Share", filePath); 
//				File file = new File(filePath);
//				if(file.exists()){
//					Intent intent=new Intent(Intent.ACTION_SEND);    
//	                intent.setType("image/*");    
//	                intent.putExtra(Intent.EXTRA_SUBJECT, "Share");    
//	                intent.putExtra(Intent.EXTRA_TEXT, "I have successfully share my message through my app (分享自city丽人馆)");
//	                intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file)); 
//	                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);    
//	                startActivity(Intent.createChooser(intent, point.getTitle()));
//				}
//				else {
//					Intent intent=new Intent(Intent.ACTION_SEND);    
//	                intent.setType("text/*");    
//	                intent.putExtra(Intent.EXTRA_SUBJECT, "Share");    
//	                intent.putExtra(Intent.EXTRA_TEXT, "I have successfully share my message through my app (分享自city丽人馆)");
//	                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);    
//	                startActivity(Intent.createChooser(intent, point.getTitle()));
//				}
				ShareSDK.initSDK(point);
				OnekeyShare oks = new OnekeyShare();
				oks.setDialogMode();
				// 分享时Notification的图标和文字
				//oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
				oks.setText("我是分享文本");
				oks.setImageUrl("http://dashboard.mob.com/Uploads/24d2adc08f21b41fb5994bff1f80a7d3.png"); 
				// 启动分享GUI
				oks.show(point);
			}
		});
		suggestion = (LinearLayout)contentView.findViewById(R.id.fragment_more_suggestion);
		suggestion.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent it = new Intent(getActivity(), SuggestionActivity.class);
				startActivity(it);
			}
		});
		return contentView;
	}
	
	protected static String getAbsoluteImagePath(Context context, Uri uri) 
	{
	// can post image
		String [] proj={MediaStore.Images.Media.DATA};
		Cursor cursor = context.getContentResolver().query( uri,
				proj, // Which columns to return
				null, // WHERE clause; which rows to return (all rows)
				null, // WHERE clause selection arguments (none)
				null); // Order-by clause (ascending by name)

		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();

		return cursor.getString(column_index);
	}
	
}
