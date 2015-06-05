package com.aifengqiang.main;

import java.util.logging.Logger;

import com.aifengqiang.data.GlobalData;
import com.aifengqiang.main.R;
import com.aifengqiang.main.LocationCityActivity.MyLocationListener;
import com.aifengqiang.ui.NavigationButton;
import com.aifengqiang.ui.NavigationView;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.navi.BaiduMapAppNotSupportNaviException;
import com.baidu.mapapi.navi.BaiduMapNavigation;
import com.baidu.mapapi.navi.NaviPara;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MapActivity extends Activity{
	private MapView mMapView;
	private BaiduMap mMap;
	private GeoCoder mSearch;
	//private RoutePlanSearch mRouteSearch;
	private LatLng startLatLng;
	private LatLng endLatLng;
	private LocationClient mLocationClient;
	private BDLocationListener myListener = new MyLocationListener();
	private String locationString;
	private boolean loaded = false;
	private NavigationView nv;

	@Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);   
        //��ʹ��SDK�����֮ǰ��ʼ��context��Ϣ������ApplicationContext  
        //ע��÷���Ҫ��setContentView����֮ǰʵ��  
        SDKInitializer.initialize(getApplicationContext());  
        setContentView(R.layout.acitivity_map);
        mMapView = (MapView) findViewById(R.id.map_bmapView);
        nv = (NavigationView)findViewById(R.id.map_nav_view);
		nv.setLeftButton(getString(R.string.back),NavigationButton.NAVIGATIONIMAGELEFT, R.drawable.icon_back_nor);
		nv.setRightButton(getString(R.string.navigation), NavigationButton.NAVIGATIONIMAGENONE, 0);
		nv.setTitle(getString(R.string.app_title));
		NavigationView.NavigationViewListener nvl = new NavigationView.NavigationViewListener() {
			@Override
			public void OnNavigationButtonClick(int id) {
				// TODO Auto-generated method stub
				if(id == NavigationView.NAVIGATION_BUTTON_LEFT){
					finish();
				}
				else{
					startNavi();
				}
			}
		};
		nv.setNavigationViewListener(nvl);
        mLocationClient = new LocationClient(getApplicationContext());     //����LocationClient��
	    mLocationClient.registerLocationListener( myListener );    //ע���������
	    
	    mMap = mMapView.getMap();
	    Intent intent = getIntent();
	    endLatLng = new LatLng(intent.getFloatExtra("lat", 31),intent.getFloatExtra("lng", 121));
	    Log.e("Address", endLatLng.latitude+" "+endLatLng.longitude);
	    BitmapDescriptor bitMap = BitmapDescriptorFactory.fromResource(R.drawable.location);
	    OverlayOptions options = new MarkerOptions().position(endLatLng).icon(bitMap);
	    MapStatus mMapStatus = new MapStatus.Builder().target(endLatLng).zoom(18).build();
	    MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
	    mMap.animateMapStatus(mMapStatusUpdate);
	    mMap.addOverlay(options);
	    LocationClientOption option = new LocationClientOption();
	    
	    Intent it = getIntent();
	    locationString = it.getStringExtra("address");

	    option.setOpenGps(true);

	    option.setCoorType("bd09ll");

	    option.setScanSpan(2000);

	    option.setLocationMode(LocationMode.Hight_Accuracy);

	    option.setIsNeedAddress(true);

	    option.setNeedDeviceDirect(true);

	    mLocationClient.setLocOption(option);

	    mLocationClient.start();

	    mLocationClient.requestLocation();
//        mRouteSearch = RoutePlanSearch.newInstance();
//        OnGetRoutePlanResultListener routelistener = new OnGetRoutePlanResultListener() {  
//            public void onGetWalkingRouteResult(WalkingRouteResult result) {  
//                //��ȡ������·�滮��� 
//            	
//            }  
//            public void onGetTransitRouteResult(TransitRouteResult result) {  
//                //��ȡ��������·���滮���  
//            }  
//            public void onGetDrivingRouteResult(DrivingRouteResult result) {  
//                //��ȡ�ݳ���·�滮���  
//            }  
//        };
//        mRouteSearch.setOnGetRoutePlanResultListener(routelistener);
//        PlanNode stNode = PlanNode.withLocation(new LatLng(0, 0));  
//        PlanNode enNode = PlanNode.withCityNameAndPlaceName("����", "����");
//        mRouteSearch.drivingSearch((new DrivingRoutePlanOption())  
//        	    .from(stNode)  
//        	    .to(enNode));
//        NaviPara para = new NaviPara(); 
//        para.startPoint = new LatLng(0, 0);
//        BaiduMapNavigation.openBaiduMapNavi(para, this);
//        mRouteSearch.destroy();
    }
	
	public void startNavi(View view) {
	    // ���� ��������  
	    NaviPara para = new NaviPara();  
	    para.startPoint = startLatLng;  
	    para.startName = "�����￪ʼ";  
	    para.endPoint = endLatLng;  
	    para.endName = "���������";  
	    try {  
	        BaiduMapNavigation.openBaiduMapNavi(para, this);  
	    } catch (BaiduMapAppNotSupportNaviException e) {  
	        e.printStackTrace();  
	        AlertDialog.Builder builder = new AlertDialog.Builder(this);  
	        builder.setMessage("����δ��װ�ٶȵ�ͼapp��app�汾���ͣ����ȷ�ϰ�װ��");  
	        builder.setTitle("��ʾ");  
	        builder.setPositiveButton("ȷ��", new OnClickListener() {  
	            @Override  
	            public void onClick(DialogInterface dialog, int which) {  
	            	dialog.dismiss();  
	            	BaiduMapNavigation.getLatestBaiduMapApp(getApplicationContext()); 
	            }  
	        });  
	        builder.setNegativeButton("ȡ��", new OnClickListener() {  
	            @Override  
	            public void onClick(DialogInterface dialog, int which) {  
	            	dialog.dismiss();  
	            }  
	        });  
	        builder.create().show();  
	    }  
	}
	@Override 
    protected void onDestroy() {  
        super.onDestroy();  
        //��activityִ��onDestroyʱִ��mMapView.onDestroy()��ʵ�ֵ�ͼ�������ڹ���  
        mMapView.onDestroy(); 
        mLocationClient.stop();
    }
	
    @Override  
    protected void onResume() {  
        super.onResume();  
        //��activityִ��onResumeʱִ��mMapView. onResume ()��ʵ�ֵ�ͼ�������ڹ���  
        mMapView.onResume();
    }
    
    @Override  
    protected void onPause() {  
        super.onPause();  
        //��activityִ��onPauseʱִ��mMapView. onPause ()��ʵ�ֵ�ͼ�������ڹ���  
        mMapView.onPause();  
    }  
    
    public class MyLocationListener implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			Log.d("Location","started"+location.getLatitude());
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
			if(!loaded){
				mSearch = GeoCoder.newInstance();
	        
	        	OnGetGeoCoderResultListener listener = new OnGetGeoCoderResultListener() {  
	        		public void onGetGeoCodeResult(GeoCodeResult result) {  
	                	if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {  
	                		//û�м��������  
	                	}  
	                	//��ȡ���������  
	            	}  
	         
	            	@Override  
	            	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {  
	                	if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {  
	                    	//û���ҵ�������� 
	                		AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());  
	        	        	builder.setMessage("��ͼ��δ�ҵ��ĵ�ַ��");  
	        	        	builder.setTitle("��ʾ");
	        	        	builder.setNeutralButton("ȷ��", new OnClickListener() {
							
								@Override
								public void onClick(DialogInterface dialog, int which) {
									// TODO Auto-generated method stub
									dialog.dismiss();
								}
							});
	        	        	builder.create().show();
	                	}
	                	endLatLng = result.getLocation();
	            	}  
	        	};
	        
	        
	        	mSearch.setOnGetGeoCodeResultListener(listener);
	       		mSearch.geocode(new GeoCodeOption()  
	        		.city("����")  
	        		.address(locationString));
	        	mSearch.destroy();
			}

			loaded = true;
			startLatLng = new LatLng(location.getLatitude(), location.getLongitude());
			if(endLatLng!=null){
				//startNavi(mMapView);
			}
		}
	}
    
    public void startNavi(){
		Uri mUri = Uri.parse("geo:"+endLatLng.latitude+","+endLatLng.longitude);
		Log.e("URI", "geo:"+endLatLng.latitude+","+endLatLng.longitude);
		Intent mIntent = new Intent(Intent.ACTION_VIEW,mUri);
		startActivity(mIntent);
    }
}

