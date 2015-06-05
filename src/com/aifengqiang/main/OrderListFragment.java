package com.aifengqiang.main;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.util.EncodingUtils;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.aifengqiang.data.GlobalData;
import com.aifengqiang.entity.OrderAllResponseInfo;
import com.aifengqiang.entity.RestaurantInfo;
import com.aifengqiang.entity.ShopMessageInfo;
import com.aifengqiang.entity.Specials;
import com.aifengqiang.entity.UserDetailInfo;
import com.aifengqiang.network.ConnectionClient;
import com.aifengqiang.ui.MenuView;
import com.aifengqiang.ui.NavigationView;
import com.aifengqiang.ui.RefreshableView;
import com.aifengqiang.ui.RefreshableView.PullToRefreshListener;
import com.aifengqiang.ui.WaitingDialog;
import com.baidu.android.bbalbs.common.a.c;
import com.baidu.mapapi.map.InfoWindow;

import android.R.integer;
import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class OrderListFragment extends Fragment{
	private Activity point;
	private RefreshableView refreshableView;
	private OrderListAdapter adapter;
	private ArrayList<OrderAllResponseInfo> undoOrderList;
	private ArrayList<OrderAllResponseInfo> allOrderList;
	private TextView undoList, allList;
	private View undoLine, allLine;
	private ListView listView;
	private OrderListAdapter undOrderListAdapter, allOrderListAdapter;
	private Handler handler;
	private Dialog ad;
	private OrderAllResponseInfo responseInfo;
	private boolean firstLoad = true;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,  
            Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		point = getActivity();
		View content = inflater.inflate(R.layout.fragment_order, container, false);
		undoOrderList = new ArrayList<OrderAllResponseInfo>();
		allOrderList = new ArrayList<OrderAllResponseInfo>();
//		for(int i = 0;i<10;i++){
//			OrderAllResponseInfo item = new OrderAllResponseInfo();
//			item.init(true);
//			allOrderList.add(item);
//		}
//		for(int i = 0;i<10;i++){
//			OrderAllResponseInfo item = new OrderAllResponseInfo();
//			item.init(false);
//			undoOrderList.add(item);
//			allOrderList.add(item);
//		}
		undoList = (TextView)content.findViewById(R.id.order_list_undo_text);
		allList = (TextView)content.findViewById(R.id.order_list_all_text);
		undoLine = (View)content.findViewById(R.id.order_list_undo_line);
		allLine = (View)content.findViewById(R.id.order_list_line);

		refreshableView = (RefreshableView)content.findViewById(R.id.orderlist_refresh_view);
		
		listView = (ListView)content.findViewById(R.id.orderlist_list_view);
		
		handler = new Handler(){
			@Override
			public void handleMessage(Message m){
				switch (m.what) {
				case 0:
					if(ad.isShowing())
						ad.dismiss();
	                refreshableView.finishRefreshing();
					((OrderListAdapter)listView.getAdapter()).notifyDataSetChanged();
					break;
				case 1:
					if(ad.isShowing())
						ad.dismiss();
					showDialog("投诉成功！", false);
					break;
				case 3:
					showDialog("网络连接错误，请检查！", false);
					break;
				case 4:
					showDialog("请求错误！", false);
					break;
				case 5:
					showDialog("服务器错误！",false);
					break;
		        case 10:{
					if(ad.isShowing())
						ad.dismiss();
		        	if(responseInfo != null){
		        		undoOrderList.remove(responseInfo);
		        		responseInfo.setStatus("CANCELED");
		        	}
		        	showDialog("取消订单成功！",false);
		        	break;
		        }
		        case 11:{
					if(ad.isShowing())
						ad.dismiss();
		        	Intent it = new Intent(point, ShopDetailActivity.class);
		        	it.putExtra("choose", true);
		        	startActivity(it);
		        	break;
		        }
		        case 12:
					if(ad.isShowing())
						ad.dismiss();
					showDialog("确定要取消这个订单吗？");;
					break;
		        case 13:
					if(ad.isShowing())
						ad.dismiss();
		        	showDialog("该订单已经超时未消费，无法取消",false);
		        	break;
				default:
					break;
				}
			}
		};
		init();
		return content;
	}
	
	public void init(){
		undOrderListAdapter= new OrderListAdapter(point,undoOrderList);
		allOrderListAdapter = new OrderListAdapter(point, allOrderList);
		undoList.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				undoList.setTextColor(0xfff88727);
				allList.setTextColor(0xff333333);
				undoLine.setBackgroundColor(0xfff88727);
				allLine.setBackgroundColor(0xffdedede);
				undoLine.invalidate();
				allLine.invalidate();
				listView.setAdapter(undOrderListAdapter);
			}
		});
		allList.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				listView.setAdapter(allOrderListAdapter);
				allList.setTextColor(0xfff88727);
				undoList.setTextColor(0xff333333);
				allLine.setBackgroundColor(0xfff88727);
				undoLine.setBackgroundColor(0xffdedede);
				undoLine.invalidate();
				allLine.invalidate();
			}
		});
		
		refreshableView.setOnRefreshListener(new PullToRefreshListener() {  
            @Override  
            public void onRefresh() {  
                new Thread(){
					@Override
					public void run(){
						initOrderList();
					}
				}.start();  
            }  
        }, 0);

		listView.setAdapter(undOrderListAdapter);

//		ad = new WaitingDialog(point, R.style.MyDialog);
//		ad.show();
//		new Thread(){
//			@Override
//			public void run(){
//				initOrderList();
//			}
//		}.start();
	}
	
	public void initOrderList(){
		Message m = new Message();
		boolean networkConnected = ConnectionClient.isNetworkConnected(point);
		if(networkConnected){
			HttpResponse response = ConnectionClient.connServerForResultGetWithHeader("/customer/orders", "X-Customer-Token",GlobalData.getIntance().getId(point));
			if(response!=null){
				String result = "";
				int status = response.getStatusLine().getStatusCode();
				if(status==HttpStatus.SC_OK){
					try {
						result = EntityUtils.toString(response.getEntity(),"UTF-8");
						Log.e("HttpConnect",result);
						ArrayList<OrderAllResponseInfo> orders = OrderAllResponseInfo.parseJSON(result);
						allOrderList.clear();
						undoOrderList.clear();
						Log.d("OderCount",""+orders.size());
						for(OrderAllResponseInfo allResponseInfo:orders){
							allOrderList.add(allResponseInfo);
							String orderStatus = allResponseInfo.getStatus();
							if(orderStatus.equals("CONFIRMED")||orderStatus.equals("RESERVED"))
								undoOrderList.add(allResponseInfo);
						}
					} catch (ParseException | IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					Message message = new Message();
					message.what = 0;
					message.arg1 = undoOrderList.size();
					MainActivity.handler.sendMessage(message);
					m.what = 0;
					handler.sendMessage(m);
				}
				else {
					m.what = status/100;
					handler.sendMessage(m);
				}
			}
			else{
				m.what = 3;
				handler.sendMessage(m);
			}
		}
		else {
			m.what = 3;
			handler.sendMessage(m);
		}
	}
	
	public void cancelOrder(String id){
		Message m = new Message();
		m.arg1 = ((OrderListAdapter)listView.getAdapter()).getSelect();
		boolean networkConnected = ConnectionClient.isNetworkConnected(point);
		if(networkConnected){
			HttpResponse response = ConnectionClient.connServerForResultPostWithHeader("/customer/orders/"+id+"/cancel",null, "X-Customer-Token",GlobalData.getIntance().getId(point));
			if(response!=null){
				String result = "";
				try {
					result = EntityUtils.toString(response.getEntity(),"UTF-8");
					Log.e("HttpConnect","CancelOrder"+result);
				}
				catch(Exception e){
					e.printStackTrace();
				}
				int status = response.getStatusLine().getStatusCode();
				if(status==HttpStatus.SC_OK){
					m.what = 10;
					handler.sendMessage(m);
				}
				else {
					m.what = status/100;
					handler.sendMessage(m);
				}
			}
			else{
				m.what = 3;
				handler.sendMessage(m);
			}
		}
		else {
			m.what = 3;
			handler.sendMessage(m);
		}
	}
	

	protected void getRestaurant(String resIdString) {
		// TODO Auto-generated method stub
		Message m = new Message();
		boolean networkConnected = ConnectionClient.isNetworkConnected(point);
		if(networkConnected){
			HttpResponse response = ConnectionClient.connServerForResultGet("/restaurants/"+resIdString);
			if(response!=null){
				String result = "";
				try {
					result = EntityUtils.toString(response.getEntity(),"UTF-8");
					Log.e("Restaurant",result);
				} catch (ParseException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				int status = response.getStatusLine().getStatusCode();
				if(status==HttpStatus.SC_OK){
					JSONTokener token = new JSONTokener(result);
					JSONObject object;
					try {
						object = (JSONObject)token.nextValue();
						RestaurantInfo restaurantInfo = new RestaurantInfo();
						restaurantInfo.initFromJSON(object);
						GlobalData.getIntance().setRestaurant(restaurantInfo);
					}
					catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return;
					}
					m.what = 11;
					handler.sendMessage(m);
				}
				else {
					m.what = status/100;
					handler.sendMessage(m);
				}
			}
			else{
				m.what = 3;
				handler.sendMessage(m);
			}
		}
		else {
			m.what = 3;
			handler.sendMessage(m);
		}
	}
	
	@Override
	public void onStart(){
		super.onStart();
	}

	public class OrderListAdapter extends BaseAdapter{
		
		private Context mContext;
		private ArrayList<OrderAllResponseInfo> listInfo;
		private int selectIndex;

		public OrderListAdapter(Context mContext){
			this.mContext = mContext;
		}
		
		public OrderListAdapter(Context mContext, ArrayList<OrderAllResponseInfo> info){
			this.mContext = mContext;
			this.listInfo = info;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return listInfo.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return listInfo.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}
		
		public int getSelect(){
			return selectIndex;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			LayoutInflater inflater  = LayoutInflater.from(mContext);
			convertView = inflater.inflate(R.layout.order_lists_item, null);
			LinearLayout linearLayout = (LinearLayout)convertView.findViewById(R.id.order_lists_item_first_linear);
			TextView diner = (TextView)convertView.findViewById(R.id.order_lists_item_name);
			TextView id = (TextView)convertView.findViewById(R.id.order_lists_item_ordernum);
			TextView date = (TextView)convertView.findViewById(R.id.order_lists_item_date);
			TextView time = (TextView)convertView.findViewById(R.id.order_lists_item_time);
			TextView count = (TextView)convertView.findViewById(R.id.order_lists_item_count);
			TextView room = (TextView)convertView.findViewById(R.id.order_lists_item_room);
			TextView remark = (TextView)convertView.findViewById(R.id.order_lists_item_mark);
			LinearLayout giveUpButton = (LinearLayout)convertView.findViewById(R.id.order_lists_item_giveup);
			Button complainButton = (Button)convertView.findViewById(R.id.order_lists_item_complain);
			Button commentButton = (Button)convertView.findViewById(R.id.order_lists_item_comment);
			TextView resTextView = (TextView)convertView.findViewById(R.id.order_lists_res);
			TextView addressTextView = (TextView)convertView.findViewById(R.id.order_lists_address);
			LinearLayout layout = (LinearLayout)convertView.findViewById(R.id.order_lists_item_res_detail);
			LinearLayout reslayout = (LinearLayout)convertView.findViewById(R.id.order_lists_res_linear);
			LinearLayout addlayout = (LinearLayout)convertView.findViewById(R.id.order_lists_address_linear);
			LinearLayout discountLayout = (LinearLayout)convertView.findViewById(R.id.order_lists_discount_linear);
			TextView discountText = (TextView)convertView.findViewById(R.id.order_lists_discount);
			final OrderAllResponseInfo info = listInfo.get(position);
			diner.setText(info.getDiner());
			id.setText(info.getNumber());
			date.setText(info.getDate());
			time.setText(info.getTime());
			count.setText(info.getPeople()+"人");
			remark.setText("备注："+info.getRemark());
			giveUpButton.setFocusable(false);
			commentButton.setFocusable(false);
			complainButton.setFocusable(false);
			final int select = position;
			String roomLabel = "";
			if(info.getLabels().size()>0){
				if(info.getLabels().contains("ROOM"))
					roomLabel += "包房";
				if(info.getLabels().contains("HALL"))
					roomLabel +="(可大厅)";
			}
			else{
				roomLabel = "无包房信息";
			}
			room.setText(roomLabel);
			
			if(info.getRestaurant()!=null){
				layout.setVisibility(View.VISIBLE);
				resTextView.setText("店名："+info.getRestaurant().getName());
				reslayout.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						ad = new WaitingDialog(point, R.style.MyDialog);
						Log.e("Waiting","getRestaurant");
						ad.show();
						new Thread(){
							@Override
							public void run(){
								getRestaurant(info.getRestaurant().getId());
							}
						}.start();
					}
				});
				addressTextView.setText("地址："+info.getRestaurant().getAddress());
				addlayout.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent it = new Intent(point, MapActivity.class);
						Log.e("Location",info.getRestaurant().getLocationData().lng+ " " +info.getRestaurant().getLocationData().lat);
						it.putExtra("lng", info.getRestaurant().getLocationData().lng);
						it.putExtra("lat", info.getRestaurant().getLocationData().lat);
						it.putExtra("address", info.getRestaurant().getAddress());
						startActivity(it);
					}
				});
			}
			else {
				layout.setVisibility(View.GONE);
			}
			
			if(info.getStatus().equals("SENT")||info.getStatus().equals("ACCEPTED")||
					info.getStatus().equals("CONFIRMED")||info.getStatus().equals("RESERVED")){
				giveUpButton.setVisibility(View.VISIBLE);
				String discountMessageString = "";
				//Log.d("Specials Size", info.getRestaurant().getSpecials().size()+"");
                for(Specials specials : info.getRestaurant().getSpecials()){
                	discountMessageString += "<p>";
                	if(specials.getTypes().getString()!=null){
                		discountMessageString+=specials.getTypes().getString();
                	}
            		if(specials.getDetail()!=null){
            			discountMessageString+=":"+specials.getDetail();;
            		}
            		discountMessageString+="<span><font color=\"#f88727\">(从";
            		if(specials.getBegin()!=null){
            			discountMessageString+=specials.getBegin();
            		}
        			discountMessageString+="到";
            		if(specials.getEnd()!=null){
            			discountMessageString+=specials.getEnd();
            		}
                	discountMessageString+="期间有效)</span></p>";
                }
				discountText.setText(Html.fromHtml("<b>优惠信息：</b>"+discountMessageString));
				discountLayout.setVisibility(View.VISIBLE);
				commentButton.setVisibility(View.GONE);
				complainButton.setVisibility(View.GONE);
			}
			else if(info.getStatus().equals("COMPLETED")){
				giveUpButton.setVisibility(View.GONE);
				discountLayout.setVisibility(View.GONE);
				complainButton.setVisibility(View.VISIBLE);
				if(info.getReview())
					commentButton.setVisibility(View.GONE);
				else {
					commentButton.setVisibility(View.VISIBLE);
				}
			}
			else{
				discountLayout.setVisibility(View.GONE);
				giveUpButton.setVisibility(View.GONE);
				commentButton.setVisibility(View.GONE);
				complainButton.setVisibility(View.GONE);
			}
			linearLayout.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
				}
			});
			giveUpButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					selectIndex = select;
					new Thread(){
						@Override
						public void run(){
							responseInfo = listInfo.get(select);
							Message message = new Message();
							message.what = 12;
							handler.sendMessage(message);
						}
					}.start();
				}
			});
			commentButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent it = new Intent(point, CommentActivity.class);
					it.putExtra("orderId", info.getId());
					it.putExtra("resId", info.getRestaurant().getId());
					startActivityForResult(it, 1008);
				}
			});
			complainButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					ad = new Dialog(point,R.style.MyDialog);
					ad.setContentView(R.layout.dialog_edit_name);
					final EditText text = (EditText)ad.findViewById(R.id.user_center_dialog_name_edit);
					TextView titleTextView = (TextView)ad.findViewById(R.id.title);
					titleTextView.setText("投诉餐馆");
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
							ad.dismiss();
							ad = new WaitingDialog(point, R.style.MyDialog);
							Log.e("Waiting","complain");
							ad.show();
							Thread thread = new Thread(){
								@Override
								public void run(){
									String complainString = text.getText().toString();
									if(complainString.length()==0){
										Message message = new Message();
										message.what = 2;
										handler.sendMessage(message);
										return;
									}
									if(ConnectionClient.isNetworkConnected(point)){
										JSONObject jsonObject = new JSONObject();
										try {
											jsonObject.put("content", complainString);
										} catch (JSONException e1) {
											e1.printStackTrace();
										}
										HttpResponse response = ConnectionClient.connServerForResultPost("/complaints", jsonObject);
										if(response!=null){
											int status = response.getStatusLine().getStatusCode();
											if(status==HttpStatus.SC_OK){
												Message message = new Message();
												message.what = 1;
												handler.sendMessage(message);
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
			});
			return convertView;
		}

	}
	
	@Override
	public void onResume(){
		super.onResume();
		if(ad!=null&&ad.isShowing())
			ad.dismiss();
		ad = new WaitingDialog(point, R.style.MyDialog);
		Log.e("Waiting","Resume");
		ad.show();
		new Thread(){
			@Override
			public void run(){
				initOrderList();
			}
		}.start();
	}
	

	public void showDialog(String text, final boolean finished){
		if(ad!=null&&ad.isShowing())
			ad.dismiss();
		ad = new Dialog(getActivity(),R.style.MyDialog);
		ad.setCancelable(false);
		ad.setContentView(R.layout.dialog_login);
		TextView tv = (TextView)ad.findViewById(R.id.dialog_text);
		tv.setText(text);
		Button btn = (Button)ad.findViewById(R.id.dialog_button_one_sure);
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!finished){
					ad.dismiss();
					((OrderListAdapter)listView.getAdapter()).notifyDataSetChanged();
					Message message = new Message();
					message.what = 0;
					message.arg1 = undoOrderList.size();
					MainActivity.handler.sendMessage(message);
				}
			}
		});
		ad.show();
	}
	
	public void showDialog(String textString){
		if(ad!=null&&ad.isShowing())
			ad.dismiss();
		ad = new Dialog(getActivity(),R.style.MyDialog);
		ad.setCanceledOnTouchOutside(false);
		ad.setCancelable(false);
		ad.setContentView(R.layout.dialog_confirm_cancel);
		TextView tv = (TextView)ad.findViewById(R.id.dialog_text_view);
		tv.setText(textString);
		Button btnConfirm = (Button)ad.findViewById(R.id.dialog_confirm);
		btnConfirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Calendar calendar = Calendar.getInstance();
				try {
					calendar.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(responseInfo.getDateTime()));
					//Log.e("time",calendar.get(Calendar.YEAR)+":"+calendar.get(Calendar.MONTH)+":"+calendar.get(Calendar.DATE));
				} catch (java.text.ParseException e) {
					e.printStackTrace();
				}
				long dueMills = calendar.getTimeInMillis();
				calendar.setTime(new Date());
				//Log.e("time",calendar.get(Calendar.YEAR)+":"+calendar.get(Calendar.MONTH)+":"+calendar.get(Calendar.DATE));
				long timeNow = calendar.getTimeInMillis();
				//Log.e("seconds", timeNow - dueMills+"");
				if(timeNow-dueMills>30*60*1000){
					Message message = new Message();
					message.what = 13;
					handler.sendMessage(message);
					ad.dismiss();
					return;
				}
				ad.dismiss();
				ad = new WaitingDialog(point, R.style.MyDialog);
				Log.e("Waiting","cancel");
				ad.show();
				new Thread(){
					@Override
					public void run(){
						cancelOrder(responseInfo.getId());
					}
				}.start();
			}
		});
		Button btnCancel = (Button)ad.findViewById(R.id.dialog_cancel);
		btnCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ad.dismiss();
			}
		});
		ad.show();
	}
	
}
