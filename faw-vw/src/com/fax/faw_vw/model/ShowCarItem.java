package com.fax.faw_vw.model;

import java.io.Serializable;

import com.fax.faw_vw.R;

public class ShowCarItem implements Serializable{
	private static final ShowCarItemRes ShowCarCC = new ShowCarItemRes(R.drawable.showcar_detail_cc_360,
			new ShowCarItemRes.ShowCarDetailHead(0, R.drawable.showcar_detail_cc_head_1, "设计"),
			new ShowCarItemRes.ShowCarDetailHead(0, R.drawable.showcar_detail_cc_head_2, "动力"),
			new ShowCarItemRes.ShowCarDetailHead(0, R.drawable.showcar_detail_cc_head_3, "操控"),
			new ShowCarItemRes.ShowCarDetailHead(0, R.drawable.showcar_detail_cc_head_4, "安全"),
			new ShowCarItemRes.ShowCarDetailHead(0, R.drawable.showcar_detail_cc_head_5, "装置"));
	
	private static final ShowCarItemRes ShowCarMagotan = new ShowCarItemRes(R.drawable.showcar_detail_magotan_360,
			new ShowCarItemRes.ShowCarDetailHead(0, R.drawable.showcar_detail_magotan_head_1, "设计"),
			new ShowCarItemRes.ShowCarDetailHead(0, R.drawable.showcar_detail_magotan_head_2, "动力"),
			new ShowCarItemRes.ShowCarDetailHead(0, R.drawable.showcar_detail_magotan_head_3, "装置"),
			new ShowCarItemRes.ShowCarDetailHead(0, R.drawable.showcar_detail_magotan_head_4, "安全"));
	
	private static final ShowCarItemRes ShowCarSAGITAR_NORMAL = new ShowCarItemRes(R.drawable.showcar_detail_sagitar_360,
			new ShowCarItemRes.ShowCarDetailHead(0, R.drawable.showcar_detail_sagitar_head_1, "设计"),
			null,
			new ShowCarItemRes.ShowCarDetailHead(0, R.drawable.showcar_detail_sagitar_head_2, "驾趣"),
			new ShowCarItemRes.ShowCarDetailHead(0, R.drawable.showcar_detail_sagitar_head_3, "科技") );
	
	private static final ShowCarItemRes ShowCarSAGITAR_GLI = new ShowCarItemRes(-1,
			new ShowCarItemRes.ShowCarDetailHead(0, R.drawable.showcar_detail_sagitar_gli_head_1, "设计"),
			null,
			null,
			new ShowCarItemRes.ShowCarDetailHead(0, R.drawable.showcar_detail_sagitar_gli_head_2, "科技"));
	
	private static final ShowCarItemRes ShowCarSAGITAR_BLUE = new ShowCarItemRes(-1,
			new ShowCarItemRes.ShowCarDetailHead(0, R.drawable.showcar_detail_sagitar_blue_head_1, "设计"),
			null,
			null,
			new ShowCarItemRes.ShowCarDetailHead(0, R.drawable.showcar_detail_sagitar_blue_head_2, "科技"));
	
	private static final ShowCarItemRes ShowCarSAGITAR = new ShowCarItemRes(
			R.drawable.showcar_detail_sagitar_360,
			new ShowCarItemRes.CarItemChild[]{
					new ShowCarItemRes.CarItemChild(R.drawable.showcar_detail_sagitar, "速腾", "SAGITAR", ShowCarSAGITAR_NORMAL),
					new ShowCarItemRes.CarItemChild(R.drawable.showcar_detail_sagitar_gti, "速腾GLI", "SAGITAR GLI", ShowCarSAGITAR_GLI),
					new ShowCarItemRes.CarItemChild(R.drawable.showcar_detail_sagitar_blue, "速腾蓝驱", "Sagitar BlueMotion", ShowCarSAGITAR_BLUE),
			});
	
	private static final ShowCarItemRes ShowCarGolf = new ShowCarItemRes(R.drawable.showcar_detail_golf_360,
			new ShowCarItemRes.ShowCarDetailHead(0, R.drawable.showcar_detail_golf_head_1, "出众设计"),
			new ShowCarItemRes.ShowCarDetailHead(0, R.drawable.showcar_detail_golf_head_2, "完美品质"),
			null,
			new ShowCarItemRes.ShowCarDetailHead(0, R.drawable.showcar_detail_golf_head_3, "激昂动力") );
	
	private static final ShowCarItemRes ShowCarBora_Normal = new ShowCarItemRes(R.drawable.showcar_detail_bora_360,
			new ShowCarItemRes.ShowCarDetailHead(0, R.drawable.showcar_detail_bora_head_1, "观"),
			new ShowCarItemRes.ShowCarDetailHead(0, R.drawable.showcar_detail_bora_head_2, "动"),
			new ShowCarItemRes.ShowCarDetailHead(0, R.drawable.showcar_detail_bora_head_3, "安"),
			new ShowCarItemRes.ShowCarDetailHead(0, R.drawable.showcar_detail_bora_head_4, "质"));
	private static final ShowCarItemRes ShowCarBora_Sportline = new ShowCarItemRes(-1,
			new ShowCarItemRes.ShowCarDetailHead(0, R.drawable.showcar_detail_bora_sportline_head_1, "观"),
			null,
			null,
			new ShowCarItemRes.ShowCarDetailHead(0, R.drawable.showcar_detail_bora_sportline_head_2, "质"));
	private static final ShowCarItemRes ShowCarBora = new ShowCarItemRes(
			R.drawable.showcar_detail_bora_360,
			new ShowCarItemRes.CarItemChild[]{
					new ShowCarItemRes.CarItemChild(R.drawable.showcar_detail_bora, "宝来", "BORA", ShowCarBora_Normal),
					new ShowCarItemRes.CarItemChild(R.drawable.showcar_detail_bora_sportline, "宝来Sportline", "BORA Sportline", ShowCarBora_Sportline),
			});

	private static final ShowCarItemRes ShowCarJETTA_Normal = new ShowCarItemRes(R.drawable.showcar_detail_jetta_360,
			new ShowCarItemRes.ShowCarDetailHead(0, R.drawable.showcar_detail_jetta_head_1, "时尚设计"),
			new ShowCarItemRes.ShowCarDetailHead(0, R.drawable.showcar_detail_jetta_head_2, "卓越驾乘"),
			new ShowCarItemRes.ShowCarDetailHead(0, R.drawable.showcar_detail_jetta_head_3, "贴心防护"),
			new ShowCarItemRes.ShowCarDetailHead(0, R.drawable.showcar_detail_jetta_head_4, "实用配置"));
	private static final ShowCarItemRes ShowCarJETTA_Sportline = new ShowCarItemRes(-1,
			new ShowCarItemRes.ShowCarDetailHead(0, R.drawable.showcar_detail_jetta_sportline_head_1, "动感外型"),
			null,
			new ShowCarItemRes.ShowCarDetailHead(0, R.drawable.showcar_detail_jetta_sportline_head_2, "动心内饰"),
			new ShowCarItemRes.ShowCarDetailHead(0, R.drawable.showcar_detail_jetta_sportline_head_3, "动力操控"));
	private static final ShowCarItemRes ShowCarJETTA = new ShowCarItemRes(
			R.drawable.showcar_detail_jetta_360,
			new ShowCarItemRes.CarItemChild[]{
					new ShowCarItemRes.CarItemChild(R.drawable.showcar_detail_jetta, "捷达", "JETTA", ShowCarJETTA_Normal),
					new ShowCarItemRes.CarItemChild(R.drawable.showcar_detail_jetta_sportline, "捷达Sportline", "JETTA Sportline", ShowCarJETTA_Sportline),
			});

	public static final ShowCarItem ShowCarItemSagitarBlue =
			new ShowCarItem("9", "速腾蓝驱", "SAGITAR", R.drawable.showcar_list_sagitar, ShowCarSAGITAR_BLUE);
	public static final ShowCarItem ShowCarItemSagitarGLI =
			new ShowCarItem("9", "GLI", "SAGITAR", R.drawable.showcar_list_sagitar, ShowCarSAGITAR_GLI);

	public static final ShowCarItem SHOW_CAR_ITEM_CC = new ShowCarItem("15", "CC", "CC", R.drawable.showcar_list_cc, ShowCarCC);
	public static final ShowCarItem SHOW_CAR_ITEM_MAGOTAN = new ShowCarItem("8", "迈腾", "MAGOTAN", R.drawable.showcar_list_magotan, ShowCarMagotan);
	public static final ShowCarItem SHOW_CAR_ITEM_SAGITAR = new ShowCarItem("9", "速腾", "SAGITAR", R.drawable.showcar_list_sagitar, ShowCarSAGITAR);
	public static final ShowCarItem SHOW_CAR_ITEM_GOLF = new ShowCarItem("16", "全新高尔夫", "GOLF", R.drawable.showcar_list_golf, ShowCarGolf);
	public static final ShowCarItem SHOW_CAR_ITEM_BORA = new ShowCarItem("10", "宝来", "BORA", R.drawable.showcar_list_bora, ShowCarBora);
	public static final ShowCarItem SHOW_CAR_ITEM_JETTA = new ShowCarItem("13", "捷达", "JETTA", R.drawable.showcar_list_jetta, ShowCarJETTA);
	public static final ShowCarItem[] SHOW_CAR_ITEMS = new ShowCarItem[]{
		SHOW_CAR_ITEM_CC,
		SHOW_CAR_ITEM_MAGOTAN,
		SHOW_CAR_ITEM_SAGITAR,
		SHOW_CAR_ITEM_GOLF,
		SHOW_CAR_ITEM_BORA,
		SHOW_CAR_ITEM_JETTA,
	};
	
	String id;
	String model_cn;
	String model_en;
	int resId;
	ShowCarItemRes detailRes;

	public ShowCarItem(String id, String brand_cn, String brand_en, int resId, ShowCarItemRes detail) {
		this.id = id;
		this.model_cn = brand_cn;
		this.model_en = brand_en;
		this.resId = resId;
		this.detailRes = detail;
	}
	public String getModel_cn() {
		return model_cn;
	}
	public String getModel_en() {
		return model_en;
	}
	public int getResId() {
		return resId;
	}
	public ShowCarItemRes getDetailRes() {
		return detailRes;
	}
	public void setDetailRes(ShowCarItemRes detailRes) {
		this.detailRes = detailRes;
	}
	public String getId() {
		return id;
	}
	
	public String get360ZipFileName(){
		return getModel_en().toLowerCase() +".zip";
	}

}
