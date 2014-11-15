package com.fax.faw_vw.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import android.R.anim;

import com.fax.faw_vw.model.Specs.Item;


public class Specs extends Respon implements Serializable{
	public final int Type_All = android.R.id.button1;
	public final int Type_Same = android.R.id.button2;
	public final int Type_Diff = android.R.id.button3;
	
	LinkedHashMap<String, Item[]>[] msg;
	public String getGroup(int position){
		return new ArrayList<String>(msg[0].keySet()).get(position);
	}
	public int getGroupCount() {
		return msg[0].keySet().size();
	}
	public int getChildrenCount(int groupPosition) {
		return msg[0].get(getGroup(groupPosition)).length;
	}
	public Item getChild(int groupPosition, int childPosition) {
		return msg[0].get(getGroup(groupPosition))[childPosition];
	}
	public Specs(Specs specs1 , Specs specs2, int type){
		LinkedHashMap<String, Item[]> map1 = 
				specs1==null?new LinkedHashMap<String, Specs.Item[]>():specs1.msg[0];
		LinkedHashMap<String, Item[]> map2 = 
				specs2==null?new LinkedHashMap<String, Specs.Item[]>():specs2.msg[0];
				
		LinkedHashMap<String, CompareItem[]> newMap = new LinkedHashMap<String, Specs.CompareItem[]>();
		for(Entry<String, Item[]> entry : map1.entrySet()){
			String key = entry.getKey();
			Item[] itemArray1 = entry.getValue();
			Item[] itemArray2 = map2.get(key);
			if(itemArray1 == null) itemArray1 = new Item[0];
			if(itemArray2 == null) itemArray2 = new Item[0];
			
			LinkedHashMap<String, CompareItem> itemMap = new LinkedHashMap<String, Specs.CompareItem>();
			for(Item item : itemArray1){
				itemMap.put(item.title, new CompareItem(item.title, item.value));
			}
			for(Item item : itemArray2){
				CompareItem compareItem = itemMap.get(item.title);
				if(compareItem == null){
					compareItem = new CompareItem(item.title, item.value);
					itemMap.put(item.title, compareItem);
				}else{
					compareItem.compareValue = item.value;
				}
				if(type == Type_Same){
					if(compareItem.value == null || !compareItem.value.equals(compareItem.compareValue)){
						itemMap.remove(item.title);
					}
				}else if(type == Type_Diff){
					if(compareItem.value != null && compareItem.value.equals(compareItem.compareValue)){
						itemMap.remove(item.title);
					}
				}
			}
			newMap.put(key, itemMap.values().toArray(new CompareItem[itemMap.size()]));
		}
		msg = new LinkedHashMap[]{newMap};
	}
	
	public static class Item implements Serializable{
		String title;
		String value;
		
		public Item(String title, String value) {
			super();
			this.title = title;
			this.value = value;
		}
		
		String titlecode;
		String category;
		public String getTitle() {
			return title;
		}
		public String getValue() {
			return value;
		}
		public String getTitlecode() {
			return titlecode;
		}
		public String getCategory() {
			return category;
		}
	}
	public static class CompareItem extends Item{
		String compareValue;
		
		public CompareItem(String title, String value) {
			super(title, value);
		}


		public String getCompareValue() {
			return compareValue;
		}
		
	}
}
