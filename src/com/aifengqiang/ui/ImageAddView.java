package com.aifengqiang.ui;

import java.util.zip.Inflater;

import com.aifengqiang.main.R;
import com.aifengqiang.main.R.bool;

import android.R.anim;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

public class ImageAddView extends FrameLayout{
	private Context mContext;
	private ImageView foreView;
	private ImageButton deleteButton;
	private boolean deleted;
	
	public ImageAddView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.mContext = context;
		init();
	}

	public ImageAddView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.mContext = context;
		init();
	}
	
	public void init(){
		deleted = true;
		LayoutInflater inflater =LayoutInflater.from(mContext);
		View iView = inflater.inflate(R.layout.image_add_button, null);
		foreView = (ImageView)iView.findViewById(R.id.image_add_show_view);
		foreView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				deleteButton.setVisibility(VISIBLE);
				foreView.setImageResource(R.drawable.cai);
				deleted = false;
			}
		});
		deleteButton = (ImageButton)iView.findViewById(R.id.image_add_delete_button);
		deleteButton.setVisibility(GONE);
		deleteButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				foreView.setImageDrawable(mContext.getResources().getDrawable(android.R.color.transparent));
				deleteButton.setVisibility(GONE);
				deleted = true;
			}
		});
		this.addView(iView);
	}
	
	
}
