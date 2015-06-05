/*
 * 瀹樼綉鍦扮珯:http://www.mob.com
 * 鎶�湳鏀寔QQ: 4006852216
 * 瀹樻柟寰俊:ShareSDK   锛堝鏋滃彂甯冩柊鐗堟湰鐨勮瘽锛屾垜浠皢浼氱涓�椂闂撮�杩囧井淇″皢鐗堟湰鏇存柊鍐呭鎺ㄩ�缁欐偍銆傚鏋滀娇鐢ㄨ繃绋嬩腑鏈変换浣曢棶棰橈紝涔熷彲浠ラ�杩囧井淇′笌鎴戜滑鍙栧緱鑱旂郴锛屾垜浠皢浼氬湪24灏忔椂鍐呯粰浜堝洖澶嶏級
 *
 * Copyright (c) 2013骞�mob.com. All rights reserved.
 */


package cn.sharesdk.onekeyshare;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.DisplayMetrics;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import com.mob.tools.FakeActivity;

/** 鏌ョ湅缂栬緫椤甸潰涓浘鐗囩殑渚嬪瓙 */
public class PicViewer extends FakeActivity implements OnTouchListener {
	private ImageView ivViewer;
	private Bitmap pic;

	Matrix matrix = new Matrix();
    Matrix savedMatrix = new Matrix();
    DisplayMetrics dm;

    /** 鏈�皬缂╂斁姣斾緥*/
    float minScaleR = 1f;
    /** 鏈�ぇ缂╂斁姣斾緥*/
    static final float MAX_SCALE = 10f;

    /** 鍒濆鐘舵�*/
    static final int NONE = 0;
    /** 鎷栧姩*/
    static final int DRAG = 1;
    /** 缂╂斁*/
    static final int ZOOM = 2;

    /** 褰撳墠妯″紡*/
    int mode = NONE;

    PointF prev = new PointF();
    PointF mid = new PointF();
    float dist = 1f;

	/** 璁剧疆鍥剧墖鐢ㄤ簬娴忚 */
	public void setImageBitmap(Bitmap pic) {
		this.pic = pic;
		if (ivViewer != null) {
			ivViewer.setImageBitmap(pic);
		}
	}

	public void onCreate() {
		ivViewer = new ImageView(activity);
		ivViewer.setScaleType(ScaleType.MATRIX);
		ivViewer.setBackgroundColor(0xc0000000);
		ivViewer.setOnTouchListener(this);
		if (pic != null && !pic.isRecycled()) {
			ivViewer.setImageBitmap(pic);
		}
		dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);// 鑾峰彇鍒嗚鲸鐜�        minZoom();
        CheckView();
        ivViewer.setImageMatrix(matrix);
	    activity.setContentView(ivViewer);

	}



	/**
     * 瑙﹀睆鐩戝惉
     */
    public boolean onTouch(View v, MotionEvent event) {

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
        // 涓荤偣鎸変笅
        case MotionEvent.ACTION_DOWN:
            savedMatrix.set(matrix);
            prev.set(event.getX(), event.getY());
            mode = DRAG;
            break;
        // 鍓偣鎸変笅
        case MotionEvent.ACTION_POINTER_DOWN:
            dist = spacing(event);
            // 濡傛灉杩炵画涓ょ偣璺濈澶т簬10锛屽垯鍒ゅ畾涓哄鐐规ā寮�           
            if (spacing(event) > 10f) {
                savedMatrix.set(matrix);
                midPoint(mid, event);
                mode = ZOOM;
            }
            break;
        case MotionEvent.ACTION_UP:
        case MotionEvent.ACTION_POINTER_UP:
            mode = NONE;
            break;
        case MotionEvent.ACTION_MOVE:
            if (mode == DRAG) {
                matrix.set(savedMatrix);
                matrix.postTranslate(event.getX() - prev.x, event.getY()
                        - prev.y);
            } else if (mode == ZOOM) {
                float newDist = spacing(event);
                if (newDist > 10f) {
                    matrix.set(savedMatrix);
                    float tScale = newDist / dist;
                    matrix.postScale(tScale, tScale, mid.x, mid.y);
                }
            }
            break;
        }
        ivViewer.setImageMatrix(matrix);
        CheckView();
        return true;
    }

    /**
     * 闄愬埗鏈�ぇ鏈�皬缂╂斁姣斾緥锛岃嚜鍔ㄥ眳涓�     */
    private void CheckView() {
        float p[] = new float[9];
        matrix.getValues(p);
        if (mode == ZOOM) {
            if (p[0] < minScaleR) {
//                Log.d("", "褰撳墠缂╂斁绾у埆:"+p[0]+",鏈�皬缂╂斁绾у埆:"+minScaleR);
                matrix.setScale(minScaleR, minScaleR);
            }
            if (p[0] > MAX_SCALE) {
//                Log.d("", "褰撳墠缂╂斁绾у埆:"+p[0]+",鏈�ぇ缂╂斁绾у埆:"+MAX_SCALE);
                matrix.set(savedMatrix);
            }
        }
        center();
    }

    /**
     * 鏈�皬缂╂斁姣斾緥锛屾渶澶т负100%
     */
    private void minZoom() {
        minScaleR = Math.min(
                (float) dm.widthPixels / (float) pic.getWidth(),
                (float) dm.heightPixels / (float) pic.getHeight());
        //浠ユ渶灏忕缉鏀炬瘮渚嬫樉绀�        matrix.setScale(minScaleR, minScaleR);
    }

    private void center() {
        center(true, true);
    }

    /**
     * 妯悜銆佺旱鍚戝眳涓�     */
    protected void center(boolean horizontal, boolean vertical) {

    	 Matrix m = new Matrix();
         m.set(matrix);
         RectF rect = new RectF(0, 0, pic.getWidth(), pic.getHeight());
         m.mapRect(rect);

         float height = rect.height();
         float width = rect.width();

         float deltaX = 0, deltaY = 0;

         if (vertical) {
             // 鍥剧墖灏忎簬灞忓箷澶у皬锛屽垯灞呬腑鏄剧ず銆傚ぇ浜庡睆骞曪紝涓婃柟鐣欑┖鍒欏線涓婄Щ锛屼笅鏂圭暀绌哄垯寰�笅绉�            
        	 int screenHeight = dm.heightPixels;
             if (height < screenHeight) {
                 deltaY = (screenHeight - height) / 2 - rect.top;
             } else if (rect.top > 0) {
                 deltaY = -rect.top;
             } else if (rect.bottom < screenHeight) {
                 deltaY = ivViewer.getHeight() - rect.bottom;
             }
         }

         if (horizontal) {
             int screenWidth = dm.widthPixels;
             if (width < screenWidth) {
                 deltaX = (screenWidth - width) / 2 - rect.left;
             } else if (rect.left > 0) {
                 deltaX = -rect.left;
             } else if (rect.right < screenWidth) {
                 deltaX = ivViewer.getWidth() - rect.right;
             }
         }
         matrix.postTranslate(deltaX, deltaY);
    }

    /**
     * 涓ょ偣鐨勮窛绂�     */
    @SuppressLint("FloatMath")
	private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return FloatMath.sqrt(x * x + y * y);
    }

    /**
     * 涓ょ偣鐨勪腑鐐�     */
    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

}
