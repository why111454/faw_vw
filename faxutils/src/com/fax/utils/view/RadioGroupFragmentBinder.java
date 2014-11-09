package com.fax.utils.view;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.util.SparseArray;
import android.widget.CompoundButton;
import android.widget.RadioGroup;

/**
 * Created by linfaxin on 2014/8/3 003.
 * Email: linlinfaxin@163.com
 * 绑定RadioGroup与Fragment
 */
public abstract class RadioGroupFragmentBinder implements RadioGroup.OnCheckedChangeListener{
    private FragmentManager fm;
    private int containId;
    private SparseArray<Fragment> bindMap = new SparseArray<Fragment>();
    protected RadioGroupFragmentBinder(FragmentManager fm, int containId) {
        this.fm = fm;
        this.containId = containId;
    }

    @Override
    public final void onCheckedChanged(RadioGroup group, int checkedId) {
        if(!((CompoundButton)group.findViewById(checkedId)).isChecked()) return;//avoid check off callback
        if(fm==null) return;
        FragmentTransaction ft=fm.beginTransaction();

        if(fm.getFragments()!=null)//防止Activity被回收后再出现
            for(Fragment fragment:fm.getFragments()){
                if(fragment!=null && fragment.getId()==containId) ft.hide(fragment);
            }

        for(int i=0,size = bindMap.size();i<size;i++){//hide all fragments
            ft.hide(bindMap.valueAt(i));
        }
        Fragment fragment = getFragment(checkedId);
        if(!fragment.isAdded()) ft.add(containId, fragment, checkedId+"");
        ft.show(fragment).commitAllowingStateLoss();

        onChecked(checkedId, fragment);
    }
    public void onChecked(int checkedId, Fragment fragment){

    }
    public Fragment getFragment(int checkedId){
        Fragment fragment = bindMap.get(checkedId);
        if(fragment==null) fragment = fm.findFragmentByTag(checkedId+"");
        if(fragment==null){
            fragment = instanceFragment(checkedId);
            bindMap.put(checkedId, fragment);
        }
        return fragment;
    }
    public abstract Fragment instanceFragment(int checkedId);
    public boolean containFragment(int checkedId){
        Fragment fragment = bindMap.get(checkedId);
        if(fragment==null) fragment = fm.findFragmentByTag(checkedId+"");
        return fragment != null;
    }
    public void removeFragment(int... checkedIds){
        FragmentTransaction ft = fm.beginTransaction();
        for(int checkedId : checkedIds){
            Fragment fragment = bindMap.get(checkedId);
            bindMap.remove(checkedId);
            if(fragment==null) continue;
            ft.remove(fragment);
        }
        try {
            ft.commitAllowingStateLoss();
        } catch (Exception ignore) {
        }
    }

    public static void bindFragment(RadioGroup radioGroup, RadioGroupFragmentBinder binder){
        bindFragment(radioGroup, binder,radioGroup.getChildCount()>0 ? radioGroup.getChildAt(0).getId(): 0);
    }
    public static void bindFragment(RadioGroup radioGroup, RadioGroupFragmentBinder binder, int checkedId){
        radioGroup.setOnCheckedChangeListener(binder);
        try {
            if(checkedId>0) ((CompoundButton)radioGroup.findViewById(checkedId)).setChecked(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
