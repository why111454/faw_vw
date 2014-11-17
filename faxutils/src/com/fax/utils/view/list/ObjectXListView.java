package com.fax.utils.view.list;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.fax.utils.task.ActivityAsyncTask;
import com.fax_utils.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

/**方便数据后台载入与展示的ListView，主要是setAdapter(ObjectXAdapter)方法 */
public class ObjectXListView extends XListView {
	InnerAdapter innerAdapter;
    String errorToast;
	public ObjectXListView(Context context) {
		super(context);
		init(context);
	}

	public ObjectXListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public ObjectXListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(Context context) {
		setPullRefreshEnable(true);
		setPullLoadEnable(true);
	}

    /**
     * 移除Item
     * @param position 计入HeadView数量的position
     */
    public void removeItemAtPosition(int position){
        try {
            innerAdapter.objects.remove(position);
            innerAdapter.notifyDataSetChanged();
        }catch (Exception ignore){
        }
    }
    /**
     * 设置Item到列表中，确保equals方法与列表中对象成立
     * @param o 对象Item
     */
    public void setItemAtPosition(Object o){
        if(innerAdapter == null) return ;
        setItemAtPosition(innerAdapter.objects.indexOf(o), o);
    }
    /**
     * 设置Item
     * @param position 计入HeadView数量的position
     * @param o 对象Item
     */
    public void setItemAtPosition(int position, Object o){
        if(position < 0) return ;
        try {
            innerAdapter.objects.set(position, o);
            innerAdapter.notifyDataSetChanged();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    /**清空数据 */
	public void clear(){
        if(innerAdapter!=null){
            innerAdapter.clear();
        }
    }
    public boolean isLoading(){
        if(innerAdapter!=null && innerAdapter.loadDataTask!=null && innerAdapter.loadDataTask.isLoading) return true;
        return false;
    }
	/**从第一页开始从新加载 */
	public void reload(){
        if(isLoading()) return;
        clear();
        stopLoadMore();
        stopRefresh();
        if(!isPullLoadEnable()) setPullLoadEnable(true);
		startLoadMore();
	}
    public void notifyDataSetChanged(){
        if(innerAdapter!=null) innerAdapter.notifyDataSetChanged();
    }

    public void setErrorToast(String errorToast) {
        this.errorToast = errorToast;
    }
    @Override
    public void stopRefresh() {
        super.stopRefresh();
        if(innerAdapter!=null) innerAdapter.stopLoading();
    }

    @Override
    public void stopLoadMore() {
        super.stopLoadMore();
        if(innerAdapter!=null) innerAdapter.stopLoading();
    }

    public <T> void setAdapter(final ObjectXAdapter<T> adapter) {
		clear();
		if(innerAdapter==null){
            innerAdapter=new InnerAdapter<T>(adapter);
            super.setAdapter(innerAdapter);
            setXListViewListener(innerAdapter);
        }else{
            innerAdapter.adapter = adapter;
            innerAdapter.notifyDataSetChanged();
        }

        //根据不同的adapter设置listView的不同属性

        if( adapter instanceof ObjectXAdapter.GridPagesAdapter ){
        	//GridPagesAdapter情况下的onItem不主动设置监听，adapter中会自动设置onClick到onItem事件
        	
        }else{//普通情况下的onItem监听
        	super.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    T t = (T) parent.getItemAtPosition(position);
                    if (t != null) adapter.onItemClick(t, view, position, id);
                    if(onItemClickListener!=null) onItemClickListener.onItemClick(parent, view, position, id);
                }
            });
        }
        
        if(adapter instanceof ObjectXAdapter.SinglePageInterface && ((ObjectXAdapter.SinglePageInterface) adapter).isDynamicHeight()){
            setOverScrollLoadEnable(false);//动态高度不允许上下过度滑动
        }
        if(adapter instanceof ObjectXAdapter.PagesAdapter){
            if(((ObjectXAdapter.PagesAdapter) adapter).isAutoLoadAfterInit()) startLoadMore();
            ((ObjectXAdapter.PagesAdapter) adapter).setListView(this);
        }
	}
    OnItemClickListener onItemClickListener;
    @Override
    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = (listener);
    }

	private ItemViewTypeControl itemViewTypeControl;
	public void setItemViewTypeControl(ItemViewTypeControl itemViewTypeControl) {
		this.itemViewTypeControl = itemViewTypeControl;
	}
	public interface ItemViewTypeControl{
		public int getItemViewType(int position);
		public int getViewTypeCount();
	}
	
	boolean mDrawSelectorOnTop;
	@Override
	public void setDrawSelectorOnTop(boolean onTop) {
		super.setDrawSelectorOnTop(onTop);
		mDrawSelectorOnTop = onTop;
	}
	public boolean isDrawSelectorOnTop() {
		return mDrawSelectorOnTop;
	}
	class InnerAdapter<T> extends BaseAdapter implements IXListViewListener{
		int nextPage=1;
		ArrayList<T> objects=new ArrayList<T>();
		ObjectXAdapter<T> adapter;

		public InnerAdapter(ObjectXAdapter<T> adapter) {
			this.adapter=adapter;
		}
		@Override
		public int getItemViewType(int position) {
			return itemViewTypeControl==null?super.getItemViewType(position):itemViewTypeControl.getItemViewType(position);
		}
		@Override
		public int getViewTypeCount() {
			return itemViewTypeControl==null?super.getViewTypeCount():itemViewTypeControl.getViewTypeCount();
		}
		@Override
		public int getCount() {
			return objects.size();
		}

		@Override
		public T getItem(int position) {
			return objects.get(position);
		}

		@Override
		public long getItemId(int position) {
			return adapter.getItemId(position);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			return adapter.bindView(getItem(position), position, convertView);
		}
		public void onRefresh(){
			nextPage=1;
			onLoadMore();
		}
		public void onLoadMore() {
            stopLoading();
			loadDataTask=new LoadDataTask();
			loadDataTask.execute();
		}
        private void stopLoading(){
            if(loadDataTask!=null){
                loadDataTask.cancel(true);
            }
        }
		public void clear(){
			objects.clear();
			stopLoading();
			nextPage=1;
			notifyDataSetChanged();
		}
		LoadDataTask loadDataTask;
		class LoadDataTask extends ActivityAsyncTask<List<T>>{
            boolean isLoading;
			public LoadDataTask() {
				super(ObjectXListView.this.getContext());
			}
			@Override
			protected List<T> doInBackground(Void... params) {
                isLoading = true;
				try {
					return adapter.instanceNewList(nextPage);
				} catch (Exception e) {
                    e.printStackTrace();
				}
				return null;
			}

			@Override
			protected void onPostExecute(List<T> list) {
                isLoading = false;
				super.onPostExecute(list);
				stopRefresh();
				stopLoadMore();
				if (list == null) {
                    if(errorToast==null)
					    Toast.makeText(getContext(), R.string.xlistview_load_error, Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(getContext(), errorToast, Toast.LENGTH_SHORT).show();
					return;
				}

				if (nextPage == 1) {
					objects.clear();// 载入的是第一页，那么就清空所有
				}
				adapter.onLoadSuc(list);
				if (list.size() == 0) {// 加载完毕
					showNoMore();
					if(!(adapter instanceof ObjectXAdapter.SinglePageInterface)){//SinglePageAdapter会在底下调用onLoadFinish
						adapter.onLoadFinish(objects);
					}
				} else {
					objects.addAll(list);
				}
				notifyDataSetChanged();
				if(nextPage!=1) setDataSetFalse();
				nextPage++;
				
				if(adapter instanceof ObjectXAdapter.SinglePageInterface){
					if(list.size() > 0) setPullLoadEnable(false);//在读取到内容后隐藏按钮，没读到内容会显示加载完毕
					adapter.onLoadFinish(objects);
                    if(((ObjectXAdapter.SinglePageInterface) adapter).isDynamicHeight()){
                        setListViewHeightBasedOnChildren(ObjectXListView.this);
						setOverScrollLoadEnable(false);//动态高度不允许上下过度滑动
                    }
				}
			}
            @Override
            protected void onCancelled() {
                super.onCancelled();
                stopRefresh();
                stopLoadMore();
            }

			@SuppressLint("NewApi")
		    public void execute(){
				isLoading = true;
		        if(android.os.Build.VERSION.SDK_INT<11) super.execute();
		        else executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		    }
		}
	}

	/**
	 * 设置adapterView的一个mDataChanged属性，系统默认开启。
     * 组件里默认关闭，关闭后不会再次getView当前已显示的item，可以解决item的重复getView的问题
	 */
	public void setDataSetFalse(){
		try {
			Field mDataChanged=AdapterView.class.getDeclaredField("mDataChanged");
			mDataChanged.setAccessible(true);
			mDataChanged.set(this, false);
		} catch (Exception e) {
//			e.printStackTrace();
		}
	}
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = (int) (1 * listView.getResources().getDisplayMetrics().density);

        for (int i = 0, count = listAdapter.getCount(); i < count; i++) {
            View listItem = listAdapter.getView(i, null, listView);
            measureItem(listView, listItem);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
    /**
     * Measure a particular list child.
     * @param child The child.
     */
    private static void measureItem(ListView listView , View child) {
        ViewGroup.LayoutParams p = child.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0, p.width);
        int lpHeight = p.height;
        int childHeightSpec;
        if (lpHeight > 0) {
            childHeightSpec = View.MeasureSpec.makeMeasureSpec(lpHeight, View.MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }
}



