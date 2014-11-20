package com.fax.faw_vw.views;

import java.util.ArrayList;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils.TruncateAt;
import android.text.method.ScrollingMovementMethod;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.fax.faw_vw.MyApp;
import com.fax.faw_vw.R;

public class AppDialogBuilder implements OnClickListener, DialogInterface{
	private CharSequence title;
    private Integer titleGravity;
    private Integer titleBg;
	private Drawable icon;
	private CharSequence btnOkStr;
	private CharSequence btnMidStr;
	private CharSequence btnCancleStr;
	private View contentView;
	public View getContentView(){return contentView;}
	private DialogInterface.OnClickListener positiveClickListener;
	private DialogInterface.OnClickListener neutralClickListener;
	private DialogInterface.OnClickListener negativeClickListener;
	private DialogInterface.OnCancelListener onCancelListener;
	private DialogInterface.OnDismissListener onDismissListener;
	protected Context context;
	private boolean isDismissOnClick=true;
	public Context getContext(){return context;}
	public AppDialogBuilder(Context context){
		this.context=context;
	}
	public AppDialogBuilder setTitle(CharSequence title){
		this.title=title;
		return this;
	}
    public AppDialogBuilder setTitle(CharSequence title, int gravity){
        this.title=title;
        this.titleGravity = gravity;
        return this;
    }
    public AppDialogBuilder setTitleBg(int color){
        titleBg = color;
        return this;
    }
	public AppDialogBuilder setTitle(int resId){
		return setTitle(context.getString(resId));
	}
	public AppDialogBuilder setIcon(int resId){
		Drawable icon=context.getResources().getDrawable(resId);
		return setIcon(icon);
	}
	public AppDialogBuilder setIcon(Drawable icon){
		if(icon!=null){
			icon.setBounds(0, 0, icon.getMinimumWidth(), icon.getMinimumHeight());
			this.icon=icon;
		}
		return this;
	}
	public AppDialogBuilder setView(View view){
		this.contentView =view;
		return this;
	}
	public AppDialogBuilder setMessage(CharSequence message){
		TextView tv= new TextView(context);
		tv.setText(message);
		tv.setGravity(Gravity.CENTER);
		tv.setTextSize(20);
		tv.setTextColor(context.getResources().getColor(R.color.dialog_message_color));
		tv.setVerticalScrollBarEnabled(true);
		tv.setMovementMethod(ScrollingMovementMethod.getInstance());
		this.contentView =tv;
		return this;
	}
	public AppDialogBuilder setMessage(int resId){
		return setMessage(context.getString(resId));
	}
	public AppDialogBuilder setCheckBox(CharSequence message){
		CheckBox tv=new CheckBox(context);
		tv.setText(message);
		tv.setTextSize(18);
		tv.setTextColor(context.getResources().getColor(R.color.dialog_message_color));
		this.contentView =tv;
//		tv.setButtonDrawable(R.drawable.checkbox_white);
		return this;
	}
	public AppDialogBuilder setCheckBox(int textResId){
		return setCheckBox(context.getString(textResId));
	}
	public AppDialogBuilder setChecked(boolean isChecked){
		if(contentView instanceof CheckBox){
			((CheckBox) contentView).setChecked(isChecked);
		}
		return this;
	}
	public boolean isChecked(){
		if(contentView instanceof CheckBox){
			return ((CheckBox) contentView).isChecked();
		}
		return false;
	}
	public AppDialogBuilder setEditText(CharSequence message,CharSequence hint){
		EditText tv=new EditText(context);
		tv.setText(message);
		tv.setHint(hint);
		this.contentView =tv;
		return this;
	}
	public AppDialogBuilder setEditText(CharSequence message,CharSequence hint,int inputType){
		EditText tv=new EditText(context);
		tv.setText(message);
		tv.setInputType(inputType);
		tv.setHint(hint);
		this.contentView =tv;
		return this;
	}
	public String getEditTextValue(){
		if(contentView instanceof EditText){
			return ((EditText) contentView).getText().toString();
		}
		return null;
	}
	public AppDialogBuilder setItems(int itemsRes, final DialogInterface.OnClickListener listener){
		return setItems(context.getResources().getStringArray(itemsRes), listener);
	}
	public AppDialogBuilder setItems(CharSequence[] items, final DialogInterface.OnClickListener listener){
		DialogListView listView=new DialogListView(context,listener, items);
		setView(listView);
		return this;
	}
    /**设置items，textView都是singleLine的 */
    public AppDialogBuilder setSingleLineItems(CharSequence[] items, final DialogInterface.OnClickListener listener){
        DialogListView listview=new DialogListView(context,listener, items){
            public TextView getView(int position, View convertView, ViewGroup parent) {
                TextView tv= super.getView(position, convertView, parent);
                tv.setTextSize(14);
                tv.setSingleLine(true);
                tv.setEllipsize(TruncateAt.END);
                return tv;
            }
        };
        setView(listview);
        return this;
    }
    public AppDialogBuilder setDismissOnClick(boolean enable){
        isDismissOnClick=enable;
        return this;
    }

    public AppDialogBuilder setSingleChoiceItems(CharSequence[] items,final int defaultIndex, final DialogInterface.OnClickListener listener){
        DialogListView listview=new DialogListView(context,listener, items){
            @Override
            public TextView getView(int position, View convertView, ViewGroup parent) {
                TextView tv= super.getView(position, convertView, parent);
                if (defaultIndex==position) {
                    tv.setTextColor(Color.WHITE);
                    tv.setTypeface(Typeface.DEFAULT_BOLD);
                }else{
                    tv.setTextColor(context.getResources().getColor(R.color.dialog_message_color));
                    tv.setTypeface(Typeface.DEFAULT);
                }
                return tv;
            }
        };
        setView(listview);
        return this;
    }
    public AppDialogBuilder setMultiChoiceItems(int itemsRes,boolean[] defaultIndexs, final OnMultiChoiceClickListener listener){
        return setMultiChoiceItems(context.getResources().getStringArray(itemsRes), defaultIndexs, listener);
    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public AppDialogBuilder setMultiChoiceItems(CharSequence[] items,final boolean[] defaultIndexs, final OnMultiChoiceClickListener listener){
        final DialogListView listView=new DialogListView(context, null, items){
            @Override
            protected TextView newTextView() {
                return new CheckedTextView(context);
            }
        };
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        for(int i=0,length=items.length; i<length; i++){
            if(i>=defaultIndexs.length) break;
            listView.setItemChecked(i, defaultIndexs[i]);
        }
        setView(listView);
        setPositiveButton(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onCheckFinish(dialog, listView.getCheckedItemPositions());
            }
        });
        return this;
    }

    public AppDialogBuilder setPositiveButton(DialogInterface.OnClickListener clickListener){
        return setPositiveButton(android.R.string.ok, clickListener);
    }
    public AppDialogBuilder setPositiveButton(int resId,DialogInterface.OnClickListener clickListener){
        return setPositiveButton(context.getString(resId), clickListener);
    }
    public AppDialogBuilder setPositiveButton(CharSequence btnText,DialogInterface.OnClickListener clickListener){
        this.btnOkStr=btnText;
        this.positiveClickListener=clickListener;
        return this;
    }
    public AppDialogBuilder setNegativeButton(DialogInterface.OnClickListener clickListener){
        return setNegativeButton(android.R.string.cancel, clickListener);
    }
    public AppDialogBuilder setNegativeButton(boolean dismissOnClick){
        if (dismissOnClick) {
            return setNegativeButton(new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }
        return setNegativeButton(null);
    }
    public AppDialogBuilder setNegativeButton(int resId,DialogInterface.OnClickListener clickListener){
        return setNegativeButton(context.getString(resId),clickListener);
    }
    public AppDialogBuilder setNegativeButton(CharSequence btnText,DialogInterface.OnClickListener clickListener){
        this.btnCancleStr=btnText;
        this.negativeClickListener=clickListener;
        return this;
    }
    public AppDialogBuilder setNeutralButton(int resId,DialogInterface.OnClickListener clickListener){
        return setNeutralButton(context.getString(resId),clickListener);
    }
    public AppDialogBuilder setNeutralButton(CharSequence btnText,DialogInterface.OnClickListener clickListener){
        this.btnMidStr=btnText;
        this.neutralClickListener=clickListener;
        return this;
    }
    private ArrayList<View> titleMoreViews = new ArrayList<View>();
    public AppDialogBuilder addTitleRightMore(View titleMoreView, final DialogInterface.OnClickListener listener){
        titleMoreView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(AppDialogBuilder.this, -1);
            }
        });
        this.titleMoreViews.add(titleMoreView);
        return this;
    }
    public AppDialogBuilder addTitleRightMore(int drawableRes, DialogInterface.OnClickListener listener){
        ImageButton icon = new ImageButton(getContext());
        int padding = (int) MyApp.convertToDp(10);
        icon.setPadding(padding, padding, padding, padding);
        icon.setImageResource(drawableRes);
        icon.setBackgroundResource(R.drawable.common_btn_in_white);
        return addTitleRightMore(icon, listener);
    }
    public AppDialogBuilder addTitleRightCloseDialog(int drawableRes){
        return addTitleRightMore(drawableRes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    }
    public AppDialogBuilder setOnCancelListener(DialogInterface.OnCancelListener onCancelListener) {
        this.onCancelListener = onCancelListener;
        return this;
    }
    public AppDialogBuilder setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
        return this;
    }
    //	public AppDialogBuilder create(){//更符合alertDialog的用法,,,- -、
//		return this;
//	}
//	public void show(){
//		show(activity.getSupportFragmentManager(), "basicDialog");
//	}
    public View createView(){
        View view=View.inflate(context, R.layout.app_dialog, null);

        TextView titleTv = ((TextView) view.findViewById(R.id.dialog_title));
        if (title!=null&&title.length()>0) {
            if (icon != null) {
                titleTv.setCompoundDrawables(icon, null, null, null);
            }
            titleTv.setText(title);
            if(titleGravity!=null) titleTv.setGravity(titleGravity);
        }else{
            titleTv.setVisibility(View.GONE);
            view.findViewById(R.id.dialog_title_context_div).setVisibility(View.GONE);
        }
        ViewGroup titleMoreWidget=((ViewGroup) view.findViewById(R.id.dialog_title_more_widget));
        if(titleMoreViews.size()==0){
            titleMoreWidget.removeAllViews();
            titleMoreWidget.setVisibility(View.GONE);
        }else{
            titleMoreWidget.setVisibility(View.VISIBLE);
            for(View titleMoreView : titleMoreViews){
                titleMoreWidget.addView(titleMoreView, new ViewGroup.LayoutParams(-2, -1));
            }
        }
        if(titleBg!=null) view.findViewById(R.id.dialog_title_contain).setBackgroundColor(titleBg);

        ViewGroup contextContain = (ViewGroup) view.findViewById(R.id.dialog_context_contain);
        if(contentView == null){
        	contextContain.setVisibility(View.GONE);
        	
        }else {
            if(!(contentView instanceof TextView)) {//如果不是setMessage（自定义View）就要更改默认布局
                //不要顶栏分割线的margin
                ((LinearLayout.LayoutParams) view.findViewById(R.id.dialog_title_context_div).getLayoutParams()).setMargins(0, 0, 0, 0);

                //调正content的Padding
                if(contentView instanceof ListView){//如果是setItems了，那么就不要padding
                    contextContain.setPadding(0, 0, 0, 0);

                }else{//如果是其他的任何自定义View，padding 10dp
                    int padding = (int) context.getResources().getDisplayMetrics().density * 10;
                    contextContain.setPadding(padding, padding, padding, padding);

                }
            }

            if(contentView instanceof AbsListView|| contentView instanceof ScrollView
                    || contentView instanceof TextView|| contentView instanceof WebView){//自带滚动类型的View
                contextContain.addView(contentView, -1, -1);

            }else{//不能自己滚动那就包裹ScrollView,允许滚动
                ScrollView scrollView=new ScrollView(context);
                scrollView.addView(contentView);
                contextContain.addView(scrollView, -1, -1);
            }
        }


        Button btnCancel=(Button) view.findViewById(R.id.dialog_btn_cancel);
        btnCancel.setOnClickListener(this);
        if(btnCancleStr!=null) btnCancel.setText(btnCancleStr);
        else{
            btnCancel.setVisibility(View.GONE);
            view.findViewById(R.id.dialog_btn_div).setVisibility(View.GONE);
        }
        Button btnOk=(Button) view.findViewById(R.id.dialog_btn_ok);
        btnOk.setOnClickListener(this);
        if(btnOkStr!=null) btnOk.setText(btnOkStr);
        else{
            btnOk.setVisibility(View.GONE);
            view.findViewById(R.id.dialog_btn_div2).setVisibility(View.GONE);
        }
        Button btnMid=(Button) view.findViewById(R.id.dialog_btn_mid);
        btnMid.setOnClickListener(this);
        if(btnMidStr!=null) btnMid.setText(btnMidStr);
        else{
            btnMid.setVisibility(View.GONE);
            if(btnOkStr!=null&&btnCancleStr!=null){//左右两边按钮都有内容，那么显示一条div
            	view.findViewById(R.id.dialog_btn_div2).setVisibility(View.GONE);
            }else{//左右有一个为空，那么隐藏两条
            	view.findViewById(R.id.dialog_btn_div).setVisibility(View.GONE);
                view.findViewById(R.id.dialog_btn_div2).setVisibility(View.GONE);
            }
        }

        if(btnOkStr==null&&btnMidStr==null&&btnCancleStr==null){
            view.findViewById(R.id.dialog_context_btn_div).setVisibility(View.GONE);
        }
        return view;
    }
    private Dialog dialog;
    public Dialog create() {
        View view=createView();
        dialog=new Dialog(context,R.style.DialogTheme);
        dialog.setContentView(view);
        dialog.setOnCancelListener(onCancelListener);
        dialog.setOnDismissListener(onDismissListener);
        dialog.getWindow().setGravity(Gravity.CENTER);
        return dialog;
    }
    public void show(){
        create().show();
    }
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.dialog_btn_ok:
                if(positiveClickListener!=null){
                    positiveClickListener.onClick(this, -1);
                }
                break;
            case R.id.dialog_btn_cancel:
                if(negativeClickListener!=null){
                    negativeClickListener.onClick(this, -1);
                }
                break;
            case R.id.dialog_btn_mid:
                if(neutralClickListener!=null){
                    neutralClickListener.onClick(this, -1);
                }
                break;
        }
        if(dialog!=null&&isDismissOnClick) dialog.dismiss();
    }

    @Override
    public void cancel() {
        if(dialog!=null) dialog.cancel();
    }
    @Override
    public void dismiss() {
        if(dialog!=null) dialog.dismiss();
    }

    //	public void show(FragmentManager manager, DialogInterface.OnClickListener positiveClickListener) {
//		this.positiveClickListener=positiveClickListener;
//		show(manager, "basicDialog");
//	}
//	public void show(FragmentManager manager) {
//		show(manager, "basicDialog");
//	}
    public interface OnMultiChoiceClickListener{
        public void onCheckFinish(DialogInterface dialog, SparseBooleanArray checkResult);
    }
    class DialogListView extends ListView{
        public DialogListView(Context context,final DialogInterface.OnClickListener listener,final CharSequence[] items) {
            super(context);
            setSelector(android.R.color.transparent);
            setDivider(context.getResources().getDrawable(R.color.dialog_message_color));
            setDividerHeight(2);
            setCacheColorHint(Color.TRANSPARENT);
            setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                    if(listener!=null) listener.onClick(AppDialogBuilder.this, position);
                    if(isDismissOnClick) dialog.dismiss();
                }
            });
            setAdapter(new BaseAdapter() {
                @Override
                public int getCount() {
                    return items.length;
                }
                @Override
                public CharSequence getItem(int position) {
                    return items[position];
                }
                @Override
                public long getItemId(int position) {
                    return 0;
                }
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    return DialogListView.this.getView(position, convertView, parent);
                }
            });
        }

        public TextView getView(int position, View convertView, ViewGroup parent) {
            TextView textView = newTextView();
            textView.setId(android.R.id.text1);
            textView.setTextColor(getResources().getColor(R.color.dialog_message_color));
            textView.setTextSize(18);
            float dip = getResources().getDisplayMetrics().density;
            textView.setPadding((int) (20*dip), 0, 0, 0);
            textView.setGravity(Gravity.CENTER_VERTICAL);
            textView.setMinimumHeight((int) (60 * dip));
            return textView;
        }
        protected TextView newTextView(){
            return new TextView(context);
        }
    }
}
