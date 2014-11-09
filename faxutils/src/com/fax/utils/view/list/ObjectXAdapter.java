package com.fax.utils.view.list;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Build.VERSION;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.fax.utils.http.HttpUtils;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;

/**
 * Created by linfaxin on 2014/8/7 007.
 * Email: linlinfaxin@163.com
 * 给ObjectXListView用的数据的载入适配器
 * TODO 把listView中的数据的载入部分放到这个类里，让适配器真正控制列表数据
 */
public interface ObjectXAdapter<T>{
    public View bindView(T t, int position, View convertView);
    public List<T> instanceNewList(int page) throws Exception;
    public void onLoadSuc(List<T> list);
	public long getItemId(int position);
    public void onLoadFinish(List<T> allList);
    public void onItemClick(T t, View view, int position, long id);

    /**单页载入的 */
    public interface SinglePageInterface{
        public String getUrl();
        /**ListView的高度是否是随着item高度可变的（嵌入在线性布局中很有用） */
        public boolean isDynamicHeight();
    }

    /**网络多页数据的载入适配器 */
    public static abstract class PagesAdapter<T> implements ObjectXAdapter<T> {
    	ObjectXListView listView;
    	public void setListView(ObjectXListView listView){
    		this.listView = listView;
    	}
        
        public List<T> instanceNewList(int page) throws Exception{
            String result = getJsonData(page);
            try {
                return instanceNewList(result);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        /**从网络获取json数据(耗时操作) */
        public String getJsonData(int page){
            return HttpUtils.executeForString(getRequest(page));
        }
        public HttpRequestBase getRequest(int page){
        	return new HttpGet(getUrl(page));
        }
        public abstract String getUrl(int page);
        public abstract List<T> instanceNewList(String json) throws Exception;

        public void onLoadSuc(List<T> list){
        }
        public void onLoadFinish(List<T> allList){
        }
        public void onItemClick(T t, View view, int position, long id){
        }
        //setAdapter之后自动载入
        protected boolean isAutoLoadAfterInit(){
            return true;
        }
    	public long getItemId(int position){
    		return 0;
    	}
    }
    /**网络单页数据的载入适配器 */
    public static abstract class SinglePageAdapter<T> extends PagesAdapter<T> implements SinglePageInterface{
        @Override
        public String getUrl(int page) {
            return getUrl();
        }
        @Override
        public boolean isDynamicHeight() {
            return false;
        }
    }
    /**本地单页数据的载入适配器 */
    public static abstract class SingleLocalPageAdapter<T> extends SinglePageAdapter<T>{
        public String getUrl(){
            return null;
        }
        @Override
        public List<T> instanceNewList(int page) throws Exception {
            return instanceNewList();
        }
        @Override
        public List<T> instanceNewList(String json) throws Exception {
            return null;
        }
        public abstract List<T> instanceNewList() throws Exception;
    }
    
    /**模仿GridView样式的adapter */
    public static abstract class GridPagesAdapter<T> extends PagesAdapter<T>{
    	int column;
    	ArrayList<T[]> tArray = new ArrayList<T[]>();
    	
    	public GridPagesAdapter(int column){
    		this.column = column;
    	}
    	Drawable selector;
    	@Override
		public void setListView(ObjectXListView listView) {
			super.setListView(listView);
			listView.setDividerHeight(getDividerHeight());
			selector = listView.getSelector();
			listView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		}
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, -1, 1);
		@SuppressLint("NewApi")
		@Override
		public View bindView(T lineFirstT, int row, View rowContain) {
			if(row<0||row>=tArray.size()) return new View(listView.getContext());
			
			LinearLayout linear = (LinearLayout) rowContain;
			if(linear == null){
				linear = new LinearLayout(listView.getContext());
				for(int i=0;i<column;i++){
					FrameLayout grid = new FrameLayout(listView.getContext());
					if(selector!=null){
						Drawable drawable = selector.mutate().getConstantState().newDrawable();
						if(listView.isDrawSelectorOnTop()){
							grid.setForeground(drawable);
						}else{
							if(Build.VERSION.SDK_INT>=16) grid.setBackground(drawable);
							else grid.setBackgroundDrawable(drawable);
						}
					}
					linear.addView(grid, params);
				}
			}
			int dividerHeight = getDividerHeight();
			if(dividerHeight>0 && Build.VERSION.SDK_INT>=11){
				linear.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
				linear.setDividerDrawable(listView.getDivider());
			}
			
			T[] gridArray = tArray.get(row);
			for(int i=0; i<column; i++){
				FrameLayout grid = (FrameLayout) linear.getChildAt(i);
				if(gridArray[i]==null){
					grid.setVisibility(View.INVISIBLE);
					grid.setOnClickListener(null);
				}else{
					grid.setVisibility(View.VISIBLE);
					View convert = ( grid.getChildCount()==0 ? null: grid.getChildAt(1) );
					final int position = row * column + i;
					final T t =gridArray[i];
					grid.removeAllViews();
					grid.addView(bindGridView(grid, t, position, convert));
					grid.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							onItemClick(t, v, position, getItemId(position));
						}
					});
				}
			}
			
			return linear;
		}
		protected int getDividerHeight(){
			return 0;
		}
		/**
		 * 获得View
		 * @param t 数据实体
		 * @param position 位置
		 * @param convertView 
		 * @return
		 */
		protected abstract View bindGridView(ViewGroup contain, T t, int position, View convertView);

		@Override
        public void onItemClick(T t, View view, int position, long id){
            if(listView!=null && listView.onItemClickListener!=null)
            	listView.onItemClickListener.onItemClick(listView, view, position, id);
        }
		
		@SuppressWarnings("unchecked")
		@Override
		public final List<T> instanceNewList(int page) throws Exception {
			if(page<=1){//第一页就清空所有
				tArray.clear();
			}
			List<T> list = instanceGridList(page);
			ArrayList<T> temp = new ArrayList<T>(column);
			ArrayList<T> firstItemList = new ArrayList<T>();
			
			for(T t : list){
				if(temp.size() == 0) firstItemList.add(t);//一行的首个，作为开头记录
				
				temp.add(t);
				
				if(temp.size() >= column){//一行满了换下一行
					tArray.add((T[]) temp.toArray( new Object[column] ));
					temp.clear();
				}
			}
			//最后一行可能没填满的
			if(temp.size()>0) tArray.add((T[]) temp.toArray( new Object[column] ));
			
			return firstItemList;
		}
		protected List<T> instanceGridList(int page) throws Exception{
			return super.instanceNewList(page);
		}
    }

    /**gridView的单页配置器 */
    public static abstract class SingleGridPageAdapter<T> extends GridPagesAdapter<T> implements SinglePageInterface{
        public SingleGridPageAdapter(int column) {
            super(column);
        }
        @Override
        public String getUrl(int page) {
            return getUrl();
        }
        @Override
        public boolean isDynamicHeight() {
            return false;
        }
    }/**gridView的本地单页数据的载入适配器 */
    public static abstract class SingleLocalGridPageAdapter<T> extends SingleGridPageAdapter<T>{
        public SingleLocalGridPageAdapter(int column) {
			super(column);
		}
		public String getUrl(){
            return null;
        }
        @Override
        public List<T> instanceGridList(int page) throws Exception {
            return instanceNewList();
        }
        @Override
        public List<T> instanceNewList(String json) throws Exception {
            return null;
        }
        public abstract List<T> instanceNewList() throws Exception;
    }
}
