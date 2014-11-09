package com.fax.utils.view.pager;

import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by linfaxin on 2014/7/15 015.
 * Email: linlinfaxin@163.com
 * 每个页卡都是相同的，可以重用回收的View
 */
public abstract class SamePagerAdapter<T> extends BasePagerAdapter<T>{
    private ArrayList<View> convertViews=new ArrayList<View>();

    protected SamePagerAdapter(T... tArray) {
        super(tArray);
    }
    protected SamePagerAdapter(List<T> list) {
        super(list);
    }

    protected void onItemDestroyed(View view,T t){
    	super.onItemDestroyed(view, t);
        convertViews.add(view);
    }

    @Override
    public View getView(ViewGroup container, int position) {
        View convertView = null;
        if(convertViews.size()>0) convertView=convertViews.remove(0);
        return getView(getItemAtPosition(position), position, convertView);
    }
    public abstract View getView(T t, int position, View convertView);
}
