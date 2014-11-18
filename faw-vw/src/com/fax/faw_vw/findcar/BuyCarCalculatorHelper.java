package com.fax.faw_vw.findcar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fax.faw_vw.model.CarModelList.CarModel;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.text.TextUtils;

//购车计算器的一个页面
public class BuyCarCalculatorHelper {
	int carPrice;
	float displacement = 1.5f;
	TotalPayCarPage totalPayCarPage;
	LoanCarPage loanCarPage;
	InsureCarPage insureCarPage;

	public BuyCarCalculatorHelper() {
		totalPayCarPage = new TotalPayCarPage();
		loanCarPage = new LoanCarPage();
		insureCarPage = new InsureCarPage();
	}
	
	public void setCarModel(CarModel carModel) {
		if(carModel!=null){
			this.carPrice = Integer.valueOf(carModel.getPrice());

			//猜测排量
			String[] mayDisplacement = new String[]{
					"1.0","1.1","1.2","1.3","1.4","1.5","1.6","1.7","1.8","1.9",
					"2.0","2.1","2.2","2.3","2.4","2.5","2.6","2.7","2.8","2.9",
					"3.0","3.1","3.2","3.3","3.4","3.5","3.6","3.7","3.8","3.9",
					"4.0","4.1","4.2","4.3","4.4","4.5","4.6","4.7","4.8","4.9",
			};
			String modelName = carModel.getModel_name();
			for(String dis:mayDisplacement){
				if(modelName.contains(dis)){
					displacement = Float.valueOf(dis);
					break;
				}
			}
			totalPayCarPage = new TotalPayCarPage();
			loanCarPage = new LoanCarPage();
			insureCarPage = new InsureCarPage();
		}
	}
	public int getCarPrice() {
		return carPrice;
	}
	public void setCarPrice(int carPrice) {
		this.carPrice = carPrice;
	}
	public Page[] getPages(){
		return new Page[]{totalPayCarPage, loanCarPage, insureCarPage};
	}
	

	//贷款明细
	Item shouFuKuan = new Item("首付款", "首付额度：", "30%", "40%", "50%", "60%"){
		float[] percents = new float[]{.3f, .4f, .5f, .6f};
		public int getValue() {
			return (int) (carPrice * percents[choosedValueIndex]);
		}
	};
	Item daiKuanE = new Item("贷款额"){
		public int getValue() {
			return (int) (carPrice - shouFuKuan.getValue());
		}
	};
	Item yueFuEDu = new Item("月付额度", "还款年限：", "1年", "2年", "3年", "4年", "5年"){
		public int getValue() {
			int bankLoan = daiKuanE.getValue();
	        int loanYears = choosedValueIndex + 1;
	        int months = loanYears * 12;
	        double rate = 0;
	        if (loanYears == 1) {
	            rate = 0.0656 / 12;
	        } else if (loanYears == 2) {
	            rate = 0.0665 / 12;
	        } else if (loanYears == 3) {
	            rate = 0.0665 / 12;
	        } else if (loanYears == 4) {
	            rate = 0.069 / 12;
	        } else if (loanYears == 5) {
	            rate = 0.069 / 12;
	        }
	        return (int) (bankLoan * ((rate * Math.pow(1 + rate, months)) / (Math.pow(1 + rate, months) - 1)));
		}
	};
	
	Item shouQiFiKuanE = new Item("首期付款额"){
		public int getValue() {
			return shouFuKuan.getValue() + loanCarPage.getAllPartSum();
		}
	};
	//必要花费
	Item zhiGouShui = new Item("购置税"){
		public int getValue() {
	        return (int) ((carPrice / 1.17) * 0.1);
		}
	};
	Item  shangPaiFei = new Item("上牌费用"){
		public int getValue() {
	        return 500;
		}
	};
	Item cheChuanShiYongShui = new Item("车船使用税", "", 
			"1.0L（含）以下", "1.0-1.6L（含）", "1.6-2.0L(含）", 
			"2.0-2.5L（含）", "2.5-3.0L（含）", "3.0-4.0L（含）", "4.0以上"){
		public int getValue() {
			if (displacement <= 1.0) {
	            return 300;
	        } else if (displacement > 1.0 && displacement <= 1.6) {
	            return 420;
	        } else if (displacement > 1.6 && displacement <= 2.0) {
	            return 480;
	        } else if (displacement > 2.0 && displacement <= 2.5) {
	            return 900;
	        } else if (displacement > 2.5 && displacement <= 3.0) {
	            return 1920;
	        } else if (displacement > 3.0 && displacement <= 4.0) {
	            return 3480;
	        } else if (displacement > 4.0) {
	            return 5280;
	        }
	        return 480;
		}
		float[] displacementValues = new float[]{1.0f, 1.6f, 2.0f, 2.5f, 3.0f, 4.0f, 9.9f};
		@Override
		public String getSummary() {
			for(int i=0,length=displacementValues.length;i<length;i++){
				if(displacement <= displacementValues[i]){
					choosedValueIndex = i;
					break;
				}
			}
			return super.getSummary();
		}
		@Override
		public void showChooseValue(Context context,final OnClickListener listener) {
			super.showChooseValue(context, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					displacement = displacementValues[which];
					if(listener!=null){
						listener.onClick(dialog, which);
					}
				}
			});
		}
	};
	Item jiaoTongShiGu = new Item("交通事故责任强制保险", "", "家用6座以下", "家用6座及以上"){
		public int getValue() {
			if(choosedValueIndex==1) return 1100;
	        return 950;
		}
	};
	//商业保险
	Item diSanZhe = new Item("第三者责任险", "赔付额度：", 
			"5万", "10万", "20万", "50万", "100万"){
		public int getValue() {
			int[] values = new int[]{516, 746, 924, 1252, 1630};
	        return values[choosedValueIndex];
		}
	};
	Item cheLiangShunShi = new Item("车辆损失险"){
		public int getValue() {
	        int base = 459;
	        if (jiaoTongShiGu.choosedValueIndex==1) {
	            base = 550;
	        }
	        return (int) (base + carPrice * 0.01088);
		}
	};
	Item quanCheDaoQiang = new Item("全车盗抢险"){
		public int getValue() {
	        if (jiaoTongShiGu.choosedValueIndex==1) {
	            return (int) (119 + carPrice * 0.00374);
	        } else {
	            return (int) (102 + carPrice * 0.004505);
	        }
		}
	};
	Item boLiDanDuPoSui = new Item("玻璃单独破碎险", "", "进口", "国产"){
		public int getValue() {
	        if (choosedValueIndex == 0) {
	            return (int) (carPrice * 0.0025);
	        } else {
	            return (int) (carPrice * 0.0015);
	        }
		}
	};
	Item ziRanShunShi = new Item("自燃损失险"){
		public int getValue() {
	        return (int) (carPrice * 0.0015);
		}
	};
	Item buJiMianPeiTeYueXian = new Item("不计免赔特约险"){
		public int getValue() {
	        int damageInsurance = cheLiangShunShi.getValue();
            int thirdInsurance = diSanZhe.getValue();
            return (int) ((damageInsurance + thirdInsurance) * 0.2);
		}
	};
	Item wuGuoZeRen = new Item("无过责任险"){
		public int getValue() {
            int thirdInsurance = diSanZhe.getValue();
            return (int) ((thirdInsurance) * 0.2);
		}
	};
	Item cheShangRenYuan = new Item("车上人员责任险"){
		public int getValue() {
			return 50;
		}
	};
	Item cheShenHuaHeng = new Item("车身划痕险","赔付额度：","2千","5千","1万","2万"){
		int[] values = new int[]{400, 570, 760, 1140};
		public int getValue() {
			return values[choosedValueIndex];
		}
	};
	public abstract class Page{
		String pageTitle;
		Map<String, ItemGroup> pagePart = new LinkedHashMap<String, ItemGroup>();
		private Page(String pageTitle){
			this.pageTitle = pageTitle;
		}
		public abstract String getPageTitleFormat();
		public abstract int getPageTotalValue();
		public Map<String, ItemGroup> getPagePart() {
			return pagePart;
		}
		int getAllPartSum(){
	        int total = 0;
			for(ItemGroup itemGroup : pagePart.values()){
				total += itemGroup.getSum();
			}
			return total;
		}
		

	}
	//全款购车页
	class TotalPayCarPage extends Page{
		public TotalPayCarPage() {
			super("全款购车预计花费总额%d元");
			ItemGroup mustUse = new ItemGroup(true,zhiGouShui,shangPaiFei,cheChuanShiYongShui,jiaoTongShiGu);
			pagePart.put("必要花费", mustUse);
			ItemGroup businessInsurance = new ItemGroup(true,diSanZhe,cheLiangShunShi,
					quanCheDaoQiang,boLiDanDuPoSui, ziRanShunShi, buJiMianPeiTeYueXian, 
					wuGuoZeRen, cheShangRenYuan, cheShenHuaHeng);
			pagePart.put("商业保险", businessInsurance);
		}
		@Override
		public String getPageTitleFormat() {
	        return String.format(pageTitle, getPageTotalValue());
		}
		@Override
		public int getPageTotalValue() {
	        int total = carPrice;
			for(ItemGroup itemGroup : pagePart.values()){
				total += itemGroup.getSum();
			}
			return total;
		}
	}
	
	//贷款购车页
	class LoanCarPage extends Page{
		public LoanCarPage() {
			super("贷款按%d年计算，\n//S14您需要首付%d元+月供%d元（%d个月）//\n=总共花费%d元\n//S12比全款购车多花费%d元");
			ItemGroup detailGroup = new ItemGroup(false,shouFuKuan,daiKuanE,yueFuEDu,shouQiFiKuanE);
			pagePart.put("贷款明细", detailGroup);
			ItemGroup mustUse = new ItemGroup(true,zhiGouShui,shangPaiFei,cheChuanShiYongShui,jiaoTongShiGu);
			pagePart.put("必要花费", mustUse);
			ItemGroup businessInsurance = new ItemGroup(true,diSanZhe,cheLiangShunShi,
					quanCheDaoQiang,boLiDanDuPoSui, ziRanShunShi, buJiMianPeiTeYueXian, 
					wuGuoZeRen, cheShangRenYuan, cheShenHuaHeng);
			pagePart.put("商业保险", businessInsurance);
		}
		@Override
		public String getPageTitleFormat() {
	        int loanYears = yueFuEDu.choosedValueIndex + 1;
	        int shouQiValue = shouQiFiKuanE.getValue();
	        int monthValue = yueFuEDu.getValue();
	        int months = loanYears * 12;
	        int total = shouQiValue + monthValue * months;
	        int diff = total - totalPayCarPage.getPageTotalValue();
			return String.format(pageTitle, loanYears, shouQiValue, monthValue, months, total, diff);
		}
		@Override
		public int getPageTotalValue() {
	        int loanYears = yueFuEDu.choosedValueIndex + 1;
	        int shouQiValue = shouQiFiKuanE.getValue();
	        int monthValue = yueFuEDu.getValue();
	        int months = loanYears * 12;
	        int total = shouQiValue + monthValue * months;
			return total;
		}
	}

	//保险计算页
	class InsureCarPage extends Page{
		public InsureCarPage() {
			super("新车保险指导价%d元\n//S13新车保险优惠%d元");
			ItemGroup mustUse = new ItemGroup(true,jiaoTongShiGu);
			pagePart.put("强制保险", mustUse);
			ItemGroup businessInsurance = new ItemGroup(true,diSanZhe,cheLiangShunShi,
					quanCheDaoQiang,boLiDanDuPoSui, ziRanShunShi, buJiMianPeiTeYueXian, 
					wuGuoZeRen, cheShangRenYuan, cheShenHuaHeng);
			pagePart.put("商业保险", businessInsurance);
		}
		@Override
		public String getPageTitleFormat() {
			int total = getPageTotalValue();
			return String.format(pageTitle, total, (int)(total*.9f));
		}
		@Override
		public int getPageTotalValue() {
			return getAllPartSum();
		}
	}

	@SuppressWarnings("serial")
	public class ItemGroup extends ArrayList<Item>{
		private boolean isSum;//是否显示条目的总和
		public ItemGroup(boolean isSum, Item... items) {
			super();
			this.isSum = isSum;
			addAll(Arrays.asList(items));
		}
		public int getSum(){
			if(!isSum) return 0;
			int sum = 0;
			for(Item item : this){
				sum += item.getValue();
			}
			return sum;
		}
		public boolean isSum() {
			return isSum;
		}
	}
	public abstract class Item{
		String title;
		String chooseTitle;//选择项目时显示的Titl
		String[] chooseValues;
		int choosedValueIndex;
		public Item(String title) {
			super();
			this.title = title;
		}
		public Item(String title, String chooseTitle, String... chooseValues) {
			super();
			this.title = title;
			if(chooseTitle==null) chooseTitle = "";
			this.chooseTitle = chooseTitle;
			this.chooseValues = chooseValues;
		}
		public void showChooseValue(Context context,final DialogInterface.OnClickListener listener){
			if(chooseValues==null||chooseValues.length==0){
				return;
			}
			AlertDialog.Builder build = new AlertDialog.Builder(context)
				.setItems(chooseValues, new DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int which) {
						choosedValueIndex = which;
						if(listener!=null){
							listener.onClick(dialog, which);
						}
					}
				});
			if(!TextUtils.isEmpty(chooseTitle)){
				build.setTitle(chooseTitle);
			}
			build.show();
		}
		public String getTitle() {
			return title;
		}
		public String getSummary() {
			if(chooseValues==null || chooseValues.length==0){
				return null;
			}
			return chooseTitle + chooseValues[choosedValueIndex];
		}
		public abstract int getValue();
	}
}
