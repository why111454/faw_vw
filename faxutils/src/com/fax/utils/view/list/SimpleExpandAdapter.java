package com.fax.utils.view.list;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by linfaxin on 2014/8/25 025.
 * Email: linlinfaxin@163.com
 */
public abstract class SimpleExpandAdapter<GroupT,ChildT> extends BaseExpandableListAdapter{
    public interface MapAdapter<GroupT,ChildT>{
        int getGroupCount();
        public int getChildrenCount(int groupPosition);
        public GroupT getGroup(int groupPosition);
        public ChildT getChild(int groupPosition, int childPosition);
    }

    MapAdapter<GroupT,ChildT> map;
    public SimpleExpandAdapter(final Map<GroupT, List<ChildT>> map){
        this.map = new MapAdapter<GroupT, ChildT>() {
            @Override
            public int getGroupCount() {
                return map.size();
            }
            @Override
            public int getChildrenCount(int groupPosition) {
                return map.get(getGroup(groupPosition)).size();
            }
            @Override
            public GroupT getGroup(int groupPosition) {
                return new ArrayList<GroupT>(map.keySet()).get(groupPosition);
            }

            @Override
            public ChildT getChild(int groupPosition, int childPosition) {
                return map.get(getGroup(groupPosition)).get(childPosition);
            }
        };
    }
    public SimpleExpandAdapter(MapAdapter<GroupT,ChildT> map){
        this.map = map;
    }
    @Override
    public int getGroupCount() {
        return map.getGroupCount();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return map.getChildrenCount(groupPosition);
    }

    @Override
    public GroupT getGroup(int groupPosition) {
        return map.getGroup(groupPosition);
    }

    @Override
    public ChildT getChild(int groupPosition, int childPosition) {
        return map.getChild(groupPosition, childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        return getGroupView(groupPosition, isExpanded, convertView, getGroup(groupPosition));
    }
    public abstract View getGroupView(int groupPosition, boolean isExpanded, View convertView, GroupT group);

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        return getChildView(groupPosition, childPosition, isLastChild, convertView, getChild(groupPosition, childPosition));
    }
    public abstract View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ChildT child);

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
