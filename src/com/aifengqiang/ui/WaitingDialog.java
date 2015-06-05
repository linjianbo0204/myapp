package com.aifengqiang.ui;

import com.aifengqiang.main.R;

import android.app.Dialog;
import android.content.Context;
import android.widget.ProgressBar;

public class WaitingDialog extends Dialog{
	
	private ProgressBar arrow;

	public WaitingDialog(Context context, int theme) {
		super(context, theme);
		// TODO Auto-generated constructor stub

		setCanceledOnTouchOutside(false);
		setCancelable(false);
		setContentView(R.layout.dialog_waiting);
		arrow = (ProgressBar)findViewById(R.id.dialog_waiting_image);
	}

}
