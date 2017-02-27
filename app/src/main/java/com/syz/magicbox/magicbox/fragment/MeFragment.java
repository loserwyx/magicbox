package com.syz.magicbox.magicbox.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.syz.magicbox.magicbox.R;
import com.syz.magicbox.magicbox.activity.MyGoodsActivity;
import com.syz.magicbox.magicbox.activity.RegisterActivity;
import com.syz.magicbox.magicbox.utils.ToastUtil;

import android.app.AlertDialog;
import android.content.Intent;
import android.util.SparseArray;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
/**
 * Created by chx on 2016/12/2.
 */

public class MeFragment extends BaseFragment{


    @Override
    public View initView() {
        View view = View.inflate(mActivity,R.layout.fragment_me, null);
        return view;
    }

    @Override
    protected void loadData() {

    }
}
