package com.syz.magicbox.magicbox.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.syz.magicbox.magicbox.R;

/**
 * Created by chx on 2016/12/2.
 */

public class PublishFragment extends BaseFragment{


    @Override
    public View initView() {
        View view = View.inflate(mActivity,R.layout.fragment_publish,null);
        return view;
    }

    @Override
    protected void loadData() {

    }
}
