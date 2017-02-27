package com.syz.magicbox.magicbox.activity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.syz.magicbox.magicbox.R;
import com.syz.magicbox.magicbox.adapter.ListDropDownAdapter;
import com.syz.magicbox.magicbox.domain.GoodsInfo;
import com.syz.magicbox.magicbox.utils.ToastUtil;
import com.syz.magicbox.magicbox.view.DropDownMenu;

public class MyGoodsActivity extends Activity implements AdapterView.OnItemClickListener {

	private DropDownMenu mDropDownMenu;
	private ImageButton ib_back;

	private String[] titles = {"物品价格","上架时间"};
	private List<View> popupViews = new ArrayList<View>();

	private ListDropDownAdapter piceAdapter;
	private ListDropDownAdapter timeAdapter;

	private String[] piceFiltrate = {"不限","价格从低到高","价格从高到低"};
	private String[] timeFiltrate = {"不限","日期从早到晚","日期从晚到早"};
	private ListView piceView;
	private ListView timeView;
	private  ListView contentView;

	private List<GoodsInfo> mGoodList;
	private MyGoodsAdapter myGoodsAdapter;
	private Handler mHandler = new Handler(){

		public void handleMessage(Message msg) {
			if (mGoodList != null) {
				if (msg.arg1 >= 0) {
					compareGoods(msg.arg1, new MyPiceComparator());
				}
		         if (msg.arg2 >= 0) {
		        	 compareGoods(msg.arg2,new MyTimeComparator());
				}
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mygoods);
		initUI();
		initData();
		initEvent();
	}
	private void compareGoods(int index,Comparator<GoodsInfo> comparator) {
		switch (index) {
		case 0:
			Collections.shuffle(mGoodList);
			if (myGoodsAdapter != null) {
				myGoodsAdapter.notifyDataSetChanged();
			}
			break;
		case 1:
			Collections.sort(mGoodList, comparator);
			if (myGoodsAdapter != null) {
				myGoodsAdapter.notifyDataSetChanged();
			}
			break;
		case 2:
			Collections.sort(mGoodList, comparator);
			Collections.reverse(mGoodList);
			if (myGoodsAdapter != null) {
				myGoodsAdapter.notifyDataSetChanged();
			}
		default:
			break;
		}

	}
	private void initData() {
		//测试用数据
		mGoodList = new ArrayList<GoodsInfo>();
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.me_headimg);
		List<Bitmap> bitmaps = new ArrayList<Bitmap>();
		bitmaps.add(bitmap);
		GoodsInfo mInfo;
		for (int i = 0; i < 10; i++) {
			//保留两位小数
			DecimalFormat df = new DecimalFormat("###.00");
			mInfo = new GoodsInfo();
			mInfo.goodsBitmaps = bitmaps;
			mInfo.goodsDes = "我是测试商品" + i;
			mInfo.goodsMaster = "Master";
			//保留两位小数，返回字符串
			mInfo.goodsPice = df.format(i * 10.8);
			mInfo.goodsTime = "2016-10-"+ i;
			mInfo.goodsTitle = "商品 "+ i;
			mGoodList.add(mInfo);
		}

		myGoodsAdapter = new MyGoodsAdapter(mGoodList);
		if (contentView != null) {
			contentView.setAdapter(myGoodsAdapter);
		}
	}
	private void initEvent() {
		piceView.setOnItemClickListener(this);
		timeView.setOnItemClickListener(this);
		ib_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
	}
	private void initUI() {
		ib_back = (ImageButton) findViewById(R.id.ib_mygoods_back);
		mDropDownMenu = (DropDownMenu) findViewById(R.id.ddm_mygoodstitle);

		piceView = new ListView(this);
		piceAdapter = new ListDropDownAdapter(this, Arrays.asList(piceFiltrate));
		piceView.setDividerHeight(0);
		piceView.setAdapter(piceAdapter);

		timeView = new ListView(this);
		timeAdapter = new ListDropDownAdapter(this, Arrays.asList(timeFiltrate));
		timeView.setDividerHeight(0);
		timeView.setAdapter(timeAdapter);

		popupViews.add(piceView);
		popupViews.add(timeView);

        contentView = new ListView(this);
        contentView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

		mDropDownMenu.setDropDownMenu(Arrays.asList(titles), popupViews, contentView);


	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onBackPressed() {
		if (mDropDownMenu.isShowing()) {
			mDropDownMenu.closeMenu();
		}else {
			super.onBackPressed();
		}
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Message msg = Message.obtain();
		if (parent == piceView) {
			piceAdapter.setCheckItem(position);
			mDropDownMenu.setTabText(position == 0?titles[0]:piceFiltrate[position]);
			mDropDownMenu.closeMenu();
			if (position != 0) {
				msg.arg1 = position;
				msg.arg2 = -1;
			}
			mHandler.sendMessage(msg);
		}else if (parent == timeView) {
			timeAdapter.setCheckItem(position);
			mDropDownMenu.setTabText(position == 0?titles[1]:timeFiltrate[position]);
			mDropDownMenu.closeMenu();
			if (position != 0) {
				msg.arg2 = position;
				msg.arg1 = -1;
			}
			mHandler.sendMessage(msg);
		}
	}

	class MyGoodsAdapter extends BaseAdapter{

	    private List<GoodsInfo> list;
	    private int checkItemPosition = 0;

	    public void setCheckItem(int position) {
	        checkItemPosition = position;
	        notifyDataSetChanged();
	    }
	    public MyGoodsAdapter(List<GoodsInfo> list) {
	    	this.list = list;
		}

	    @Override
	    public int getViewTypeCount() {
	    	return super.getViewTypeCount() + 1;
	    }
	    @Override
	    public int getItemViewType(int position) {
	    	if (position == list.size()) {
				return 1;
			}else {
				return 0;
			}
	    }
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size() + 1;
		}

		@Override
		public GoodsInfo getItem(int position) {
			if (position != list.size()) {
				return list.get(position);
			}else {
				return null;
			}
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			return convertView;
		}
	}

	static class ViewHolder{

		public static View getView(View convertView,int id) {
			SparseArray<View> viewHold = (SparseArray<View>) convertView.getTag();
			if (viewHold == null) {
				viewHold = new SparseArray<View>();
				convertView.setTag(viewHold);
			}
			View child = viewHold.get(id);
			if (child == null) {
				child = (View) convertView.findViewById(id);
				viewHold.put(id, child);
			}
			return child;
		}
	}

	/**
	 * @author chx
	 *  比较价格高低
	 */
	class MyPiceComparator implements Comparator<GoodsInfo>{

		@Override
		public int compare(GoodsInfo lhs, GoodsInfo rhs) {
			GoodsInfo goodsL = lhs;
			GoodsInfo goodsH = rhs;
			double d1 = Double.valueOf(goodsL.getGoodsPice());
			double d2 = Double.valueOf(goodsH.getGoodsPice());
			return (int) (d1 - d2);
		}

	}
	/**
	 * @author chx
	 * 比较上架日期早晚
	 */
	class MyTimeComparator implements Comparator<GoodsInfo>{

		@Override
		public int compare(GoodsInfo lhs, GoodsInfo rhs) {
			GoodsInfo goodsL = lhs;
			GoodsInfo goodsH = rhs;
            String[] l = goodsL.goodsTime.split("-");
            String[] h = goodsH.goodsTime.split("-");
            for (int i = 0; i < h.length; i++) {
				if (Integer.valueOf(l[i]) > Integer.valueOf(h[i])) {
					return 1;
				}else if (Integer.valueOf(l[i]) < Integer.valueOf(h[i])) {
					return -1;
				}else {
					continue;
				}
			}
            return 1;
		}

	}
}
