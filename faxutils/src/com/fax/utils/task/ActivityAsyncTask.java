package com.fax.utils.task;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.app.Application.ActivityLifecycleCallbacks;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Bundle;

/**
 * Created by linfaxin on 2014/9/10 010.
 * Email: linlinfaxin@163.com
 * 与Activity生命周期相关的Task
 */
@SuppressLint("NewApi")
public abstract class ActivityAsyncTask<T> extends AsyncTask<Void, Void, T>{
    protected Context context;
    public ActivityAsyncTask(Context context){
        this.context = context;
        registerActivityLifecycleCallbacks();
    }
    public Context getContext(){
        return context;
    }

    Object lifecycleCallbacks;

    /**子类可以重写return false以防止Activity在finish时被cancel */
    protected boolean isActivityLifeRelative(){
        return true;
    }
    private void registerActivityLifecycleCallbacks(){
    	if( VERSION.SDK_INT>=14 && context instanceof Activity && isActivityLifeRelative()){
        	if(lifecycleCallbacks==null){
        		lifecycleCallbacks = new Application.ActivityLifecycleCallbacks() {
        	        @Override
        	        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        	        }
        	        @Override
        	        public void onActivityStarted(Activity activity) {
        	        }
        	        @Override
        	        public void onActivityResumed(Activity activity) {
        	        }
        	        @Override
        	        public void onActivityPaused(Activity activity) {
        	        }
        	        @Override
        	        public void onActivityStopped(Activity activity) {
        	        }
        	        @Override
        	        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        	        }
        	        @Override
        	        public void onActivityDestroyed(Activity activity) {
        	            if(activity==context) performCancel(true);
        	        }
        	    };
        	}
        	
            ((Activity) context).getApplication().registerActivityLifecycleCallbacks((ActivityLifecycleCallbacks) lifecycleCallbacks);
    	}
        
    }
    private void unregisterActivityLifecycleCallbacks(){
        if(context instanceof Activity && VERSION.SDK_INT>=14 && lifecycleCallbacks!=null){
            ((Activity) context).getApplication().unregisterActivityLifecycleCallbacks((ActivityLifecycleCallbacks) lifecycleCallbacks);
        }
    }
    /**代替默认的cancel方法，子类重写以完成资源清理等 */
    protected boolean performCancel(boolean mayInterruptIfRunning){
        return cancel(mayInterruptIfRunning);
    }

    @Override
    protected void onPostExecute(T result) {
        unregisterActivityLifecycleCallbacks();
    }
    
    @Override
	protected void onCancelled() {
		super.onCancelled();
        unregisterActivityLifecycleCallbacks();
	}
}
