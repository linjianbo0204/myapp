/*
 * 瀹樼綉鍦扮珯:http://www.mob.com
 * 鎶�湳鏀寔QQ: 4006852216
 * 瀹樻柟寰俊:ShareSDK   锛堝鏋滃彂甯冩柊鐗堟湰鐨勮瘽锛屾垜浠皢浼氱涓�椂闂撮�杩囧井淇″皢鐗堟湰鏇存柊鍐呭鎺ㄩ�缁欐偍銆傚鏋滀娇鐢ㄨ繃绋嬩腑鏈変换浣曢棶棰橈紝涔熷彲浠ラ�杩囧井淇′笌鎴戜滑鍙栧緱鑱旂郴锛屾垜浠皢浼氬湪24灏忔椂鍐呯粰浜堝洖澶嶏級
 *
 * Copyright (c) 2013骞�mob.com. All rights reserved.
 */

package cn.sharesdk.onekeyshare;

import static com.mob.tools.utils.BitmapHelper.captureView;
import static com.mob.tools.utils.R.getStringRes;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import cn.sharesdk.framework.CustomPlatform;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import com.mob.tools.utils.UIHandler;

/**
 * 蹇嵎鍒嗕韩鐨勫叆鍙� * <p>
 * 閫氳繃涓嶅悓鐨剆etter璁剧疆鍙傛暟锛岀劧鍚庤皟鐢▄@link #show(Context)}鏂规硶鍚姩蹇嵎鍒嗕韩
 */
public class OnekeyShare implements PlatformActionListener, Callback {
	private static final int MSG_TOAST = 1;
	private static final int MSG_ACTION_CCALLBACK = 2;
	private static final int MSG_CANCEL_NOTIFY = 3;

	private HashMap<String, Object> shareParamsMap;
	private ArrayList<CustomerLogo> customers;
	private boolean silent;
	private PlatformActionListener callback;
	private ShareContentCustomizeCallback customizeCallback;
	private boolean dialogMode = false;
	private boolean disableSSO;
	private HashMap<String, String> hiddenPlatforms;
	private View bgView;
	private OnekeyShareTheme theme;

	private Context context;
	private PlatformListFakeActivity.OnShareButtonClickListener onShareButtonClickListener;

	public OnekeyShare() {
		shareParamsMap = new HashMap<String, Object>();
		customers = new ArrayList<CustomerLogo>();
		callback = this;
		hiddenPlatforms = new HashMap<String, String>();
	}

	public void show(Context context) {
		ShareSDK.initSDK(context);
		this.context = context;

		// 鎵撳紑鍒嗕韩鑿滃崟鐨勭粺璁�		
		ShareSDK.logDemoEvent(1, null);

		// 鏄剧ず鏂瑰紡鏄敱platform鍜宻ilent涓や釜瀛楁鎺у埗鐨�		// 濡傛灉platform璁剧疆浜嗭紝鍒欐棤椤绘樉绀轰節瀹牸锛屽惁鍒欓兘浼氭樉绀猴紱
		// 濡傛灉silent涓簍rue锛岃〃绀轰笉杩涘叆缂栬緫椤甸潰锛屽惁鍒欎細杩涘叆銆�		// 鏈被鍙垽鏂璸latform锛屽洜涓轰節瀹牸鏄剧ず浠ュ悗锛屼簨浠朵氦缁橮latformGridView鎺у埗
		// 褰損latform鍜宻ilent閮戒负true锛屽垯鐩存帴杩涘叆鍒嗕韩锛�		// 褰損latform璁剧疆浜嗭紝浣嗘槸silent涓篺alse锛屽垯鍒ゆ柇鏄惁鏄�浣跨敤瀹㈡埛绔垎浜�鐨勫钩鍙帮紝
		// 鑻ヤ负鈥滀娇鐢ㄥ鎴风鍒嗕韩鈥濈殑骞冲彴锛屽垯鐩存帴鍒嗕韩锛屽惁鍒欒繘鍏ョ紪杈戦〉闈�		
		if (shareParamsMap.containsKey("platform")) {
			String name = String.valueOf(shareParamsMap.get("platform"));
			Platform platform = ShareSDK.getPlatform(name);

			if (silent
					|| ShareCore.isUseClientToShare(name)
					|| platform instanceof CustomPlatform
					) {
				HashMap<Platform, HashMap<String, Object>> shareData
						= new HashMap<Platform, HashMap<String,Object>>();
				shareData.put(ShareSDK.getPlatform(name), shareParamsMap);
				share(shareData);
				return;
			}
		}

		PlatformListFakeActivity platformListFakeActivity;
		try {
			if(OnekeyShareTheme.SKYBLUE == theme){
				platformListFakeActivity = (PlatformListFakeActivity) Class.forName("cn.sharesdk.onekeyshare.theme.skyblue.PlatformListPage").newInstance();
			}else{
				platformListFakeActivity = (PlatformListFakeActivity) Class.forName("cn.sharesdk.onekeyshare.theme.classic.PlatformListPage").newInstance();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		platformListFakeActivity.setDialogMode(dialogMode);
		platformListFakeActivity.setShareParamsMap(shareParamsMap);
		platformListFakeActivity.setSilent(silent);
		platformListFakeActivity.setCustomerLogos(customers);
		platformListFakeActivity.setBackgroundView(bgView);
		platformListFakeActivity.setHiddenPlatforms(hiddenPlatforms);
		platformListFakeActivity.setOnShareButtonClickListener(onShareButtonClickListener);
		platformListFakeActivity.setThemeShareCallback(new ThemeShareCallback() {

			@Override
			public void doShare(HashMap<Platform, HashMap<String, Object>> shareData) {
				share(shareData);
			}
		});
		if (shareParamsMap.containsKey("platform")) {
			String name = String.valueOf(shareParamsMap.get("platform"));
			Platform platform = ShareSDK.getPlatform(name);
			platformListFakeActivity.showEditPage(context, platform);
			return;
		}
		platformListFakeActivity.show(context, null);
	}

	public void setTheme(OnekeyShareTheme theme) {
		this.theme = theme;
	}

	/** address鏄帴鏀朵汉鍦板潃锛屼粎鍦ㄤ俊鎭拰閭欢浣跨敤锛屽惁鍒欏彲浠ヤ笉鎻愪緵 */
	public void setAddress(String address) {
		shareParamsMap.put("address", address);
	}

	/**
	 * title鏍囬锛屽湪鍗拌薄绗旇銆侀偖绠便�淇℃伅銆佸井淇★紙鍖呮嫭濂藉弸銆佹湅鍙嬪湀鍜屾敹钘忥級銆�	 * 鏄撲俊锛堝寘鎷ソ鍙嬨�鏈嬪弸鍦堬級銆佷汉浜虹綉鍜孮Q绌洪棿浣跨敤锛屽惁鍒欏彲浠ヤ笉鎻愪緵
	 */
	public void setTitle(String title) {
		shareParamsMap.put("title", title);
	}

	/** titleUrl鏄爣棰樼殑缃戠粶閾炬帴锛屼粎鍦ㄤ汉浜虹綉鍜孮Q绌洪棿浣跨敤锛屽惁鍒欏彲浠ヤ笉鎻愪緵 */
	public void setTitleUrl(String titleUrl) {
		shareParamsMap.put("titleUrl", titleUrl);
	}

	/** text鏄垎浜枃鏈紝鎵�湁骞冲彴閮介渶瑕佽繖涓瓧娈�*/
	public void setText(String text) {
		shareParamsMap.put("text", text);
	}

	/** 鑾峰彇text瀛楁鐨勫� */
	public String getText() {
		return shareParamsMap.containsKey("text") ? String.valueOf(shareParamsMap.get("text")) : null;
	}

	/** imagePath鏄湰鍦扮殑鍥剧墖璺緞锛岄櫎Linked-In澶栫殑鎵�湁骞冲彴閮芥敮鎸佽繖涓瓧娈�*/
	public void setImagePath(String imagePath) {
		if(!TextUtils.isEmpty(imagePath))
			shareParamsMap.put("imagePath", imagePath);
	}

	/** imageUrl鏄浘鐗囩殑缃戠粶璺緞锛屾柊娴井鍗氥�浜轰汉缃戙�QQ绌洪棿鍜孡inked-In鏀寔姝ゅ瓧娈�*/
	public void setImageUrl(String imageUrl) {
		if (!TextUtils.isEmpty(imageUrl))
			shareParamsMap.put("imageUrl", imageUrl);
	}

	/** url鍦ㄥ井淇★紙鍖呮嫭濂藉弸銆佹湅鍙嬪湀鏀惰棌锛夊拰鏄撲俊锛堝寘鎷ソ鍙嬪拰鏈嬪弸鍦堬級涓娇鐢紝鍚﹀垯鍙互涓嶆彁渚�*/
 	public void setUrl(String url) {
		shareParamsMap.put("url", url);
	}

	/** filePath鏄緟鍒嗕韩搴旂敤绋嬪簭鐨勬湰鍦拌矾鍔诧紝浠呭湪寰俊锛堟槗淇★級濂藉弸鍜孌ropbox涓娇鐢紝鍚﹀垯鍙互涓嶆彁渚�*/
	public void setFilePath(String filePath) {
		shareParamsMap.put("filePath", filePath);
	}

	/** comment鏄垜瀵硅繖鏉″垎浜殑璇勮锛屼粎鍦ㄤ汉浜虹綉鍜孮Q绌洪棿浣跨敤锛屽惁鍒欏彲浠ヤ笉鎻愪緵 */
	public void setComment(String comment) {
		shareParamsMap.put("comment", comment);
	}

	/** site鏄垎浜鍐呭鐨勭綉绔欏悕绉帮紝浠呭湪QQ绌洪棿浣跨敤锛屽惁鍒欏彲浠ヤ笉鎻愪緵 */
	public void setSite(String site) {
		shareParamsMap.put("site", site);
	}

	/** siteUrl鏄垎浜鍐呭鐨勭綉绔欏湴鍧�紝浠呭湪QQ绌洪棿浣跨敤锛屽惁鍒欏彲浠ヤ笉鎻愪緵 */
	public void setSiteUrl(String siteUrl) {
		shareParamsMap.put("siteUrl", siteUrl);
	}

	/** foursquare鍒嗕韩鏃剁殑鍦版柟鍚�*/
	public void setVenueName(String venueName) {
		shareParamsMap.put("venueName", venueName);
	}

	/** foursquare鍒嗕韩鏃剁殑鍦版柟鎻忚堪 */
	public void setVenueDescription(String venueDescription) {
		shareParamsMap.put("venueDescription", venueDescription);
	}

	/** 鍒嗕韩鍦扮含搴︼紝鏂版氮寰崥銆佽吘璁井鍗氬拰foursquare鏀寔姝ゅ瓧娈�*/
	public void setLatitude(float latitude) {
		shareParamsMap.put("latitude", latitude);
	}

	/** 鍒嗕韩鍦扮粡搴︼紝鏂版氮寰崥銆佽吘璁井鍗氬拰foursquare鏀寔姝ゅ瓧娈�*/
	public void setLongitude(float longitude) {
		shareParamsMap.put("longitude", longitude);
	}

	/** 鏄惁鐩存帴鍒嗕韩 */
	public void setSilent(boolean silent) {
		this.silent = silent;
	}

	/** 璁剧疆缂栬緫椤电殑鍒濆鍖栭�涓钩鍙�*/
	public void setPlatform(String platform) {
		shareParamsMap.put("platform", platform);
	}

	/** 璁剧疆KakaoTalk鐨勫簲鐢ㄤ笅杞藉湴鍧�*/
	public void setInstallUrl(String installurl) {
		shareParamsMap.put("installurl", installurl);
	}

	/** 璁剧疆KakaoTalk鐨勫簲鐢ㄦ墦寮�湴鍧�*/
	public void setExecuteUrl(String executeurl) {
		shareParamsMap.put("executeurl", executeurl);
	}

	/** 璁剧疆寰俊鍒嗕韩鐨勯煶涔愮殑鍦板潃 */
	public void setMusicUrl(String musicUrl) {
		shareParamsMap.put("musicUrl", musicUrl);
	}

	/** 璁剧疆鑷畾涔夌殑澶栭儴鍥炶皟 */
	public void setCallback(PlatformActionListener callback) {
		this.callback = callback;
	}

	/** 杩斿洖鎿嶄綔鍥炶皟 */
	public PlatformActionListener getCallback() {
		return callback;
	}

	/** 璁剧疆鐢ㄤ簬鍒嗕韩杩囩▼涓紝鏍规嵁涓嶅悓骞冲彴鑷畾涔夊垎浜唴瀹圭殑鍥炶皟 */
	public void setShareContentCustomizeCallback(ShareContentCustomizeCallback callback) {
		customizeCallback = callback;
	}

	/** 杩斿洖鑷畾涔夊垎浜唴瀹圭殑鍥炶皟 */
	public ShareContentCustomizeCallback getShareContentCustomizeCallback() {
		return customizeCallback;
	}

	/** 璁剧疆鑷繁鍥炬爣鍜岀偣鍑讳簨浠讹紝鍙互閲嶅璋冪敤娣诲姞澶氭 */
	public void setCustomerLogo(Bitmap enableLogo,Bitmap disableLogo, String label, OnClickListener ocListener) {
		CustomerLogo cl = new CustomerLogo();
		cl.label = label;
		cl.enableLogo = enableLogo;
		cl.disableLogo = disableLogo;
		cl.listener = ocListener;
		customers.add(cl);
	}

	/** 璁剧疆涓�釜鎬诲紑鍏筹紝鐢ㄤ簬鍦ㄥ垎浜墠鑻ラ渶瑕佹巿鏉冿紝鍒欑鐢╯so鍔熻兘 */
 	public void disableSSOWhenAuthorize() {
		disableSSO = true;
	}

	/** 璁剧疆缂栬緫椤甸潰鐨勬樉绀烘ā寮忎负Dialog妯″紡 */
	public void setDialogMode() {
		dialogMode = true;
		shareParamsMap.put("dialogMode", dialogMode);
	}

	/** 娣诲姞涓�釜闅愯棌鐨刾latform */
	public void addHiddenPlatform(String platform) {
		hiddenPlatforms.put(platform, platform);
	}

	/** 璁剧疆涓�釜灏嗚鎴浘鍒嗕韩鐨刅iew , surfaceView鏄埅涓嶄簡鍥剧墖鐨�/
	public void setViewToShare(View viewToShare) {
		try {
			Bitmap bm = captureView(viewToShare, viewToShare.getWidth(), viewToShare.getHeight());
			shareParamsMap.put("viewToShare", bm);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	/** 鑵捐寰崥鍒嗕韩澶氬紶鍥剧墖 */
	public void setImageArray(String[] imageArray) {
		shareParamsMap.put("imageArray", imageArray);
	}

	public void setEditPageBackground(View bgView) {
		this.bgView = bgView;
	}

	public void setOnShareButtonClickListener(PlatformListFakeActivity.OnShareButtonClickListener onShareButtonClickListener) {
		this.onShareButtonClickListener = onShareButtonClickListener;
	}

	/** 寰幆鎵ц鍒嗕韩 */
	public void share(HashMap<Platform, HashMap<String, Object>> shareData) {
		boolean started = false;
		for (Entry<Platform, HashMap<String, Object>> ent : shareData.entrySet()) {
			Platform plat = ent.getKey();
			plat.SSOSetting(disableSSO);
			String name = plat.getName();

//			boolean isGooglePlus = "GooglePlus".equals(name);
//			if (isGooglePlus && !plat.isValid()) {
//				Message msg = new Message();
//				msg.what = MSG_TOAST;
//				int resId = getStringRes(context, "google_plus_client_inavailable");
//				msg.obj = context.getString(resId);
//				UIHandler.sendMessage(msg, this);
//				continue;
//			}

			boolean isKakaoTalk = "KakaoTalk".equals(name);
			if (isKakaoTalk && !plat.isClientValid()) {
				Message msg = new Message();
				msg.what = MSG_TOAST;
				int resId = getStringRes(context, "kakaotalk_client_inavailable");
				msg.obj = context.getString(resId);
				UIHandler.sendMessage(msg, this);
				continue;
			}

			boolean isKakaoStory = "KakaoStory".equals(name);
			if (isKakaoStory && !plat.isClientValid()) {
				Message msg = new Message();
				msg.what = MSG_TOAST;
				int resId = getStringRes(context, "kakaostory_client_inavailable");
				msg.obj = context.getString(resId);
				UIHandler.sendMessage(msg, this);
				continue;
			}

			boolean isLine = "Line".equals(name);
			if (isLine && !plat.isClientValid()) {
				Message msg = new Message();
				msg.what = MSG_TOAST;
				int resId = getStringRes(context, "line_client_inavailable");
				msg.obj = context.getString(resId);
				UIHandler.sendMessage(msg, this);
				continue;
			}

			boolean isWhatsApp = "WhatsApp".equals(name);
			if (isWhatsApp && !plat.isClientValid()) {
				Message msg = new Message();
				msg.what = MSG_TOAST;
				int resId = getStringRes(context, "whatsapp_client_inavailable");
				msg.obj = context.getString(resId);
				UIHandler.sendMessage(msg, this);
				continue;
			}

			boolean isPinterest = "Pinterest".equals(name);
			if (isPinterest && !plat.isClientValid()) {
				Message msg = new Message();
				msg.what = MSG_TOAST;
				int resId = getStringRes(context, "pinterest_client_inavailable");
				msg.obj = context.getString(resId);
				UIHandler.sendMessage(msg, this);
				continue;
			}

			if ("Instagram".equals(name) && !plat.isClientValid()) {
				Message msg = new Message();
				msg.what = MSG_TOAST;
				int resId = getStringRes(context, "instagram_client_inavailable");
				msg.obj = context.getString(resId);
				UIHandler.sendMessage(msg, this);
				continue;
			}

			boolean isLaiwang = "Laiwang".equals(name);
			boolean isLaiwangMoments = "LaiwangMoments".equals(name);
			if(isLaiwang || isLaiwangMoments){
				if (!plat.isClientValid()) {
					Message msg = new Message();
					msg.what = MSG_TOAST;
					int resId = getStringRes(context, "laiwang_client_inavailable");
					msg.obj = context.getString(resId);
					UIHandler.sendMessage(msg, this);
					continue;
				}
			}

			boolean isYixin = "YixinMoments".equals(name) || "Yixin".equals(name);
			if (isYixin && !plat.isClientValid()) {
				Message msg = new Message();
				msg.what = MSG_TOAST;
				int resId = getStringRes(context, "yixin_client_inavailable");
				msg.obj = context.getString(resId);
				UIHandler.sendMessage(msg, this);
				continue;
			}

			HashMap<String, Object> data = ent.getValue();
			int shareType = Platform.SHARE_TEXT;
			String imagePath = String.valueOf(data.get("imagePath"));
			if (imagePath != null && (new File(imagePath)).exists()) {
				shareType = Platform.SHARE_IMAGE;
				if (imagePath.endsWith(".gif")) {
					shareType = Platform.SHARE_EMOJI;
				} else if (data.containsKey("url") && !TextUtils.isEmpty(data.get("url").toString())) {
					shareType = Platform.SHARE_WEBPAGE;
					if (data.containsKey("musicUrl") && !TextUtils.isEmpty(data.get("musicUrl").toString())) {
						shareType = Platform.SHARE_MUSIC;
					}
				}
			} else {
				Bitmap viewToShare = (Bitmap) data.get("viewToShare");
				if (viewToShare != null && !viewToShare.isRecycled()) {
					shareType = Platform.SHARE_IMAGE;
					if (data.containsKey("url") && !TextUtils.isEmpty(data.get("url").toString())) {
						shareType = Platform.SHARE_WEBPAGE;
						if (data.containsKey("musicUrl") && !TextUtils.isEmpty(data.get("musicUrl").toString())) {
							shareType = Platform.SHARE_MUSIC;
						}
					}
				} else {
					Object imageUrl = data.get("imageUrl");
					if (imageUrl != null && !TextUtils.isEmpty(String.valueOf(imageUrl))) {
						shareType = Platform.SHARE_IMAGE;
						if (String.valueOf(imageUrl).endsWith(".gif")) {
							shareType = Platform.SHARE_EMOJI;
						} else if (data.containsKey("url") && !TextUtils.isEmpty(data.get("url").toString())) {
							shareType = Platform.SHARE_WEBPAGE;
							if (data.containsKey("musicUrl") && !TextUtils.isEmpty(data.get("musicUrl").toString())) {
								shareType = Platform.SHARE_MUSIC;
							}
						}
					}
				}
			}
			data.put("shareType", shareType);

			if (!started) {
				started = true;
//				if (this == callback) {
					int resId = getStringRes(context, "sharing");
					if (resId > 0) {
						showNotification(context.getString(resId));
					}
//				}
			}
			plat.setPlatformActionListener(callback);
			ShareCore shareCore = new ShareCore();
			shareCore.setShareContentCustomizeCallback(customizeCallback);
			shareCore.share(plat, data);
		}
	}

	public void onComplete(Platform platform, int action,
			HashMap<String, Object> res) {
		Message msg = new Message();
		msg.what = MSG_ACTION_CCALLBACK;
		msg.arg1 = 1;
		msg.arg2 = action;
		msg.obj = platform;
		UIHandler.sendMessage(msg, this);
	}

	public void onError(Platform platform, int action, Throwable t) {
		t.printStackTrace();

		Message msg = new Message();
		msg.what = MSG_ACTION_CCALLBACK;
		msg.arg1 = 2;
		msg.arg2 = action;
		msg.obj = t;
		UIHandler.sendMessage(msg, this);

		// 鍒嗕韩澶辫触鐨勭粺璁�		ShareSDK.logDemoEvent(4, platform);
	}

	public void onCancel(Platform platform, int action) {
		Message msg = new Message();
		msg.what = MSG_ACTION_CCALLBACK;
		msg.arg1 = 3;
		msg.arg2 = action;
		msg.obj = platform;
		UIHandler.sendMessage(msg, this);
	}

	public boolean handleMessage(Message msg) {
		switch(msg.what) {
			case MSG_TOAST: {
				String text = String.valueOf(msg.obj);
				Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
			}
			break;
			case MSG_ACTION_CCALLBACK: {
				switch (msg.arg1) {
					case 1: {
						// 鎴愬姛
						int resId = getStringRes(context, "share_completed");
						if (resId > 0) {
							showNotification(context.getString(resId));
						}
					}
					break;
					case 2: {
						// 澶辫触
						String expName = msg.obj.getClass().getSimpleName();
						if ("WechatClientNotExistException".equals(expName)
								|| "WechatTimelineNotSupportedException".equals(expName)
								|| "WechatFavoriteNotSupportedException".equals(expName)) {
							int resId = getStringRes(context, "wechat_client_inavailable");
							if (resId > 0) {
								showNotification(context.getString(resId));
							}
						} else if ("GooglePlusClientNotExistException".equals(expName)) {
							int resId = getStringRes(context, "google_plus_client_inavailable");
							if (resId > 0) {
								showNotification(context.getString(resId));
							}
						} else if ("QQClientNotExistException".equals(expName)) {
							int resId = getStringRes(context, "qq_client_inavailable");
							if (resId > 0) {
								showNotification(context.getString(resId));
							}
						} else if ("YixinClientNotExistException".equals(expName)
								|| "YixinTimelineNotSupportedException".equals(expName)) {
							int resId = getStringRes(context, "yixin_client_inavailable");
							if (resId > 0) {
								showNotification(context.getString(resId));
							}
						} else if ("KakaoTalkClientNotExistException".equals(expName)) {
							int resId = getStringRes(context, "kakaotalk_client_inavailable");
							if (resId > 0) {
								showNotification(context.getString(resId));
							}
						}else if ("KakaoStoryClientNotExistException".equals(expName)) {
							int resId = getStringRes(context, "kakaostory_client_inavailable");
							if (resId > 0) {
								showNotification(context.getString(resId));
							}
						}else if("WhatsAppClientNotExistException".equals(expName)){
							int resId = getStringRes(context, "whatsapp_client_inavailable");
							if (resId > 0) {
								showNotification(context.getString(resId));
							}
						}else {
							int resId = getStringRes(context, "share_failed");
							if (resId > 0) {
								showNotification(context.getString(resId));
							}
						}
					}
					break;
					case 3: {
						// 鍙栨秷
						int resId = getStringRes(context, "share_canceled");
						if (resId > 0) {
							showNotification(context.getString(resId));
						}
					}
					break;
				}
			}
			break;
			case MSG_CANCEL_NOTIFY: {
				NotificationManager nm = (NotificationManager) msg.obj;
				if (nm != null) {
					nm.cancel(msg.arg1);
				}
			}
			break;
		}
		return false;
	}

	// 鍦ㄧ姸鎬佹爮鎻愮ず鍒嗕韩鎿嶄綔
	private void showNotification(String text) {
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}

	/** 鏄惁鏀寔QQ,QZone鎺堟潈鐧诲綍鍚庡彂寰崥 */
	public void setShareFromQQAuthSupport(boolean shareFromQQLogin)
	{
		shareParamsMap.put("isShareTencentWeibo", shareFromQQLogin);
	}
}
