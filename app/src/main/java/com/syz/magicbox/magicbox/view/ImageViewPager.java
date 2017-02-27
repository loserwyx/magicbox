package com.syz.magicbox.magicbox.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by chx on 2017/2/27.
 */

public class ImageViewPager extends ViewPager{
    private int startX;
    private int startY;

    public ImageViewPager(Context context) {
        super(context);
    }

    public ImageViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        getParent().requestDisallowInterceptTouchEvent(true);
        switch (ev.getAction()){

            case MotionEvent.ACTION_DOWN:
                startX = (int) ev.getX();
                startY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int endX = (int) ev.getX();
                int endY = (int) ev.getY();

                int dX = endX - startX;
                int dY = endY - startY;
                if (Math.abs(dY) < Math.abs(dX)){
                    if (dX > 0){
                        //向右滑动
                        if (getCurrentItem() == 0){
                            //第一个条目向左滑拦截，即不下发给本ViewPager消费
                            getParent().requestDisallowInterceptTouchEvent(false);
                        }
                    }
                    if (dX < 0){
                        //向左滑动
                        int count = getAdapter().getCount(); // 条目总数量
                        if (getCurrentItem() == count - 1){
                            //向左滑动最后一个条目进行拦截,不下发给本ViewPager消费
                            getParent().requestDisallowInterceptTouchEvent(false);
                        }
                    }
                }else {
                    //上下滑动拦截，父View不分发给本view消费
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return super.onTouchEvent(ev);
    }
}
