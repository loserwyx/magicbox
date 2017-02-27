package com.syz.magicbox.magicbox.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.syz.magicbox.magicbox.R;

import java.util.List;




public class ListDropDownAdapter extends BaseAdapter {

    private Context context;
    private List<String> list;
    private int checkItemPosition = 0;

    public void setCheckItem(int position) {
        checkItemPosition = position;
        notifyDataSetChanged();
    }

    public ListDropDownAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_list_drop_down, null);
        }
        fillValue(position,convertView);
        return convertView;
    }

    private void fillValue(int position,View convertView) {
    	TextView tv_itme = (TextView) ViewHolder.getView(convertView, R.id.tv_ldd_item);
    	tv_itme.setText(list.get(position));
        if (checkItemPosition != -1) {
            if (checkItemPosition == position) {
            	Drawable drawable = context.getResources().getDrawable(R.drawable.drop_down_checked);
            	drawable.setBounds(0, 0, drawable.getIntrinsicHeight(), drawable.getIntrinsicWidth());
            	tv_itme.setTextColor(context.getResources().getColor(R.color.drop_down_selected));
            	tv_itme.setBackgroundResource(R.color.check_bg);
            	tv_itme.setCompoundDrawables(null, null, drawable, null);
            } else {
            	tv_itme.setTextColor(context.getResources().getColor(R.color.drop_down_unselected));
            	tv_itme.setBackgroundResource(R.color.white);
            	tv_itme.setCompoundDrawables(null, null, null, null);
            }
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
}
