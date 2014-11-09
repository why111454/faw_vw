package com.fax.utils.task;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.client.methods.HttpRequestBase;

/**
 * Created by linfaxin on 2014/7/15 015.
 * Email: linlinfaxin@163.com
 * 直接gson实例化掉http获得的数据。
 * 注意：可能不能使用List<T>作为泛型传入，有待测试
 */
public abstract class GsonAsyncTask<T> extends HttpAsyncTask<T>{
    private static Gson gson=new Gson();

    public GsonAsyncTask(Context context, String url) {
        super(context, url);
    }

    public GsonAsyncTask(Context context, HttpRequestBase request) {
        super(context, request);
    }

    @Override
    protected T instanceObject(String json) throws Exception {
    	Type genType = getClass().getGenericSuperclass();   
    	Type[] params = ((ParameterizedType) genType).getActualTypeArguments();   
    	Class<T> entityClass =  (Class<T>)params[0];
        return gson.fromJson(json, entityClass);
    }
//    protected abstract Class<T> instanceClass();
}
