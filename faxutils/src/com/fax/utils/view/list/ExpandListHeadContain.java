package com.fax.utils.view.list;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;

/**可以把ExpandList包裹进来，头上会有当前显示的组的View，同时有被下个group推上去的赶脚 */
public class ExpandListHeadContain extends FrameLayout {

	public ExpandListHeadContain(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public ExpandListHeadContain(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ExpandListHeadContain(Context context) {
		super(context);
	}

	public ExpandListHeadContain(Context context, ExpandableListView listView) {
		super(context);
		setListView(listView);
	}

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if(getChildCount()>0 && getChildAt(0) instanceof ExpandableListView){
            setListView((ExpandableListView) getChildAt(0));
        }
    }

    ExpandableListView listView;
	View groupHead;
	LayoutParams params = new LayoutParams(-1, -2, Gravity.TOP);
	boolean isGroupHeadClickAble = true;//头栏是否允许点击
	public void setGroupHeadClickAble(boolean isGroupHeadClickAble) {
		this.isGroupHeadClickAble = isGroupHeadClickAble;
	}

	int showingGroupPosition = -1;
	public void setListView(ExpandableListView expandableListView){
		if(listView == expandableListView || expandableListView==null) return;
        if(listView!=null) removeView(listView);
        if(expandableListView.getParent() != null){
            ((ViewGroup)expandableListView.getParent()).removeView(expandableListView);
        }
        super.addView(expandableListView, -1, -1);
		this.listView = expandableListView;
		listView.setOnScrollListener(new AbsListView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				int groupPosition = getGroupPositionFromItemPosition(firstVisibleItem);
				if(groupPosition == -1){
					showingGroupPosition = -1;
					if(groupHead != null) groupHead.setVisibility(View.INVISIBLE);
					return;
				}else if(groupHead != null) groupHead.setVisibility(View.VISIBLE);
				
				//改变groupHead位置。思路：遍历所有显示的Item，判断是否是group然后得到Top移动groupHead
				if(groupHead != null){
					if(groupHead.getHeight() == 0) refreshGroupHead();//初始化后竟然没有高度？需要再次
					for(int i = 1;i<visibleItemCount;i++){
						try {
							View itemView = listView.getChildAt(i);
							if(itemView.getTop() - listView.getDividerHeight()>=groupHead.getHeight()){//没有与Head就交叉部分，中断遍历
								if(params.topMargin != 0){
									params.topMargin = 0;
									groupHead.setLayoutParams(params);
								}
								break;
							}
							if(getGroupPositionFromItemPosition(firstVisibleItem+i)>groupPosition){
								//这个位置是下一个group，同时与head有交叉部分
								params.topMargin = itemView.getTop() - groupHead.getHeight() - listView.getDividerHeight();//留出位置给Divider
								groupHead.setLayoutParams(params);
								break;
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
					
					
				
				//改变groupHead
				if(showingGroupPosition != groupPosition){
					showingGroupPosition = groupPosition;
					refreshGroupHead();
					
					if(groupHead != null && isGroupHeadClickAble){
                        groupHead.setOnTouchListener(new OnTouchListener() {
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                if(event.getAction() == MotionEvent.ACTION_UP){
                                    if(groupHead.isClickable()){//走group的click事件
                                        groupHead.performClick();
                                    }else{
                                        boolean isCollapse = listView.isGroupExpanded(showingGroupPosition);
                                        listView.performItemClick(v, getItemPositionFromGroupPosition(showingGroupPosition), 0);
                                        if(isCollapse && showingGroupPosition <listView.getExpandableListAdapter().getGroupCount()-1)
                                            listView.setSelectedGroup(showingGroupPosition);
                                        refreshGroupHead();
                                    }
                                }
                                return true;
                            }
                        });
					}
					
				}
			}
		});
	}

	private int getItemPositionFromGroupPosition(int groupPosition){
		return listView.getFlatListPosition(ExpandableListView.getPackedPositionForGroup(groupPosition));
	}
	
	private int getGroupPositionFromItemPosition(int itemPosition){
		return ExpandableListView.getPackedPositionGroup(listView.getExpandableListPosition(itemPosition));
	}
	
	private void refreshGroupHead(){
		if(showingGroupPosition >=0 && listView.getExpandableListAdapter()!=null){
			groupHead = listView.getExpandableListAdapter().getGroupView(showingGroupPosition,
					listView.isGroupExpanded(showingGroupPosition), groupHead, listView);
			if(groupHead!=null && indexOfChild(groupHead)==-1){
				addView(groupHead, params);
			}
		}
	}

}
