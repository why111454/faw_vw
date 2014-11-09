package com.fax.faw_vw.model;

import java.io.Serializable;

import com.fax.faw_vw.R;

public class PersonalizedItem implements Serializable{
	public static final PersonalizedItem[] Items = new PersonalizedItem[]{
		new PersonalizedItem("A 千万次模仿别人，不如一次超越自己", ShowCarItem.SHOW_CAR_ITEMS[5], "新生派", "以新我，超自我", R.drawable.personalized_choose_car_0),
		new PersonalizedItem("B 科技创造智能生活", ShowCarItem.SHOW_CAR_ITEMS[1], "睿智派", "智臻成就，辉映人生", R.drawable.personalized_choose_car_1),
		new PersonalizedItem("C 人生，以快乐为本", ShowCarItem.SHOW_CAR_ITEMS[4], "乐趣派", "时刻趣动生活", R.drawable.personalized_choose_car_2),
		new PersonalizedItem("D 梦想就是人生的驱动力", ShowCarItem.SHOW_CAR_ITEMS[3], "专属派", "唯你，为我", R.drawable.personalized_choose_car_3),
		new PersonalizedItem("E 低碳生活", ShowCarItem.ShowCarItemSagitarBlue, "环保派", "蓝驱科技，关爱环境", R.drawable.personalized_choose_car_4),
		new PersonalizedItem("F 优雅是一生中的崇高境界", ShowCarItem.SHOW_CAR_ITEMS[0], "优雅派", "心蕴优雅，瞬绽光华", R.drawable.personalized_choose_car_5),
		new PersonalizedItem("G 制定标准者定天下", ShowCarItem.SHOW_CAR_ITEMS[2], "质感派", "质造新标准", R.drawable.personalized_choose_car_6),
		
	};
	
	
	String question;
	ShowCarItem carItem;
	String resultType;
	String resultExtra;
	int imgRes;
	public PersonalizedItem(String question, ShowCarItem carItem, String resultType, String resultExtra, int imgRes) {
		super();
		this.question = question;
		this.carItem = carItem;
		this.resultType = resultType;
		this.resultExtra = resultExtra;
		this.imgRes = imgRes;
	}
	public String getQuestion() {
		return question;
	}
	public ShowCarItem getCarItem() {
		return carItem;
	}
	public String getResultType() {
		return resultType;
	}
	public String getResultExtra() {
		return resultExtra;
	}
	public int getImgRes(){
		return imgRes;
	}
}
