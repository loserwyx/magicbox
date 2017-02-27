package com.syz.magicbox.magicbox.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.syz.magicbox.magicbox.R;
import com.syz.magicbox.magicbox.adapter.PublishItemAdapter;
import com.syz.magicbox.magicbox.domain.LocalImage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chx on 2016/12/2.
 */

public class PublishFragment extends BaseFragment{
    @ViewInject(R.id.rv_publish)
    private RecyclerView rv_publish;

    private static final String TAG = "PublishFragment";

    private PublishItemAdapter publishItemAdapter;
    private List<LocalImage> publishItems;

    private String[] titles;
    private int[] imgId;
    @Override
    public View initView() {
        View view = View.inflate(mActivity,R.layout.fragment_publish,null);
        return view;
    }

    @Override
    protected void loadData() {

        Log.i(TAG, "loadData: "+"sucess");
        titles = new String[]{"手机","电脑","数码",
                "服饰","运动","日用", "珠宝","书籍", "车辆","其他"};
        imgId = new int[]{R.drawable.phone,R.drawable.computer,R.drawable.digit,
        R.drawable.clothes,R.drawable.sprot,R.drawable.dayil,R.drawable.jewelry,
        R.drawable.books,R.drawable.car,R.drawable.other};

        publishItems = new ArrayList<LocalImage>();
        for (int i = 0;i < titles.length;i++){
            LocalImage img = new LocalImage(titles[i],imgId[i]);
            publishItems.add(img);
        }
        if (publishItems != null){
            publishItemAdapter = new PublishItemAdapter(publishItems);

            //给RecylerView设置布局管理，设置为2列
            GridLayoutManager layoutManager = new GridLayoutManager(mActivity,2);
            rv_publish.setLayoutManager(layoutManager);
            rv_publish.setAdapter(publishItemAdapter);
        }
    }

}
