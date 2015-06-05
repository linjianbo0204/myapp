package com.aifengqiang.ui;

import android.widget.GridView;

public class AFQGridView extends GridView  
{  
    public AFQGridView(android.content.Context context,  
            android.util.AttributeSet attrs)  
    {  
        super(context, attrs);  
    }  

  /** 
     * ���ò����� 
     */  
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec)  
    {  
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,  
                MeasureSpec.AT_MOST);  
        super.onMeasure(widthMeasureSpec, expandSpec);  
  
    }  
}  
