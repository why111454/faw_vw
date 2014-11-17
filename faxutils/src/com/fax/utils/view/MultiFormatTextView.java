package com.fax.utils.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.AttributeSet;
import android.widget.TextView;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import com.fax_utils.R;

/**
 * Created by linfaxin on 2014/8/13 013.
 * <p>
 * Email: linlinfaxin@163.com
 * <p>
 * 颜色：//#ffffff
 * <p>
 * 类似这样规定颜色样式的开始
 * <p>
 * android:text="//#ff5ec353好评//：%d条   //#f99c2e中评//：%d条   //#e75d5d差评//：%d条"
 * <p>
 * 字体大小：//S16
 * <p>
 * 类似这样规定额外的字体大小样式的开始，单位DP
 * <p>
 * android:text="//s12好评//：1条   //s13中评//：1条   //s14差评//：1条"
 * <p>
 * 下划线： //u
 * 删除线： //l
 * 加粗： //b
 * <p>
 * 可以用//#，//S，//U不带内容的来标识对应样式的结束，用//标志所有样式的结束
 * <p>
 * 混合以上的使用
 * <p>
 * TODO 支持从html标签中解析出font样式
 */
public class MultiFormatTextView extends TextView{
    private ColorStateList[] colors = new ColorStateList[6];
    private int[] sizes = new int[3];
    String format;

    public MultiFormatTextView(Context context) {
        super(context);
        init(null);
    }

    public MultiFormatTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public MultiFormatTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(AttributeSet attrs){
        if(attrs!=null){
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.MultiFormatTextView);
            colors[0]= a.getColorStateList(R.styleable.MultiFormatTextView_color1);
            colors[1]= a.getColorStateList(R.styleable.MultiFormatTextView_color2);
            colors[2]= a.getColorStateList(R.styleable.MultiFormatTextView_color3);
            colors[3]= a.getColorStateList(R.styleable.MultiFormatTextView_color4);
            colors[4]= a.getColorStateList(R.styleable.MultiFormatTextView_color5);
            colors[5]= a.getColorStateList(R.styleable.MultiFormatTextView_color6);
            for(int i=0,length=colors.length; i<length; i++){
                if(colors[i]==null) colors[i] = getTextColors();
            }

            sizes[0]= a.getDimensionPixelSize(R.styleable.MultiFormatTextView_size1, 14);
            sizes[1]= a.getDimensionPixelSize(R.styleable.MultiFormatTextView_size2, 14);
            sizes[2]= a.getDimensionPixelSize(R.styleable.MultiFormatTextView_size3, 14);

            format = a.getString(R.styleable.MultiFormatTextView_format);
        }
        CharSequence text = getText();
        if(text!=null && text.length() > 0){
            setTextMulti(text.toString());
        }else if(format != null ) setTextMulti(format);

        if(getHint()!=null && getHint().length()>0 ) setHint(convertToMulti(getHint().toString()));
        requestLayout();
    }
    public void setColors(int... colors) {
        if(colors==null || colors.length==0) return;
        for(int i=0,length=colors.length;i<length;i++){
            this.colors[i] = ColorStateList.valueOf(colors[i]);
        }
    }
    public void setColors(ColorStateList... colors) {
        if(colors==null || colors.length==0) return;
        this.colors = colors;
    }

    private String getFormattedText(Object... args){
        try {
            return String.format(format, args);
        } catch (Exception e) {
            //更好的兼容性的format
            try {
                if(format!=null){
                    int wantArgsLength = format.split("%").length -1;
                    Object[] fixArgs = Arrays.copyOf(args , wantArgsLength);

                    for(int i=0; i<wantArgsLength; i++){
                        Object arg = fixArgs[i];
                        if(arg instanceof Number){
                            fixArgs[i] = arg+"";
                        }
                    }
                    return String.format(format.replace("%d","%s").replace("%f","%s"), fixArgs);
                }
            } catch (Exception ignore) {
            }
            e.printStackTrace();
        }
        return format;
    }
    public void formatText(Object... args){
        if(format==null) format = getText().toString();
        setTextMulti(getFormattedText(args));
    }
    public void setTextFormat(String format, Object... args){
        this.format = format;
        if(args.length>0) setTextMulti(getFormattedText(args));
        else setTextMulti(format);
    }
    public void setTextMulti(String text){
        setText(convertToMulti(text));
    }

    public Spannable convertToMulti(String text){
        //解析出多个部分
        String[] parts = text.split("//");//用//来规定后面的字体的格式
        ArrayList<StyleText> styleTexts = new ArrayList<StyleText>();
        for(String part : parts){
            styleTexts.add(parseStyleText(part));
        }

        //获得所有过滤掉符号的内容
        StringBuilder sb = new StringBuilder();
        for(StyleText colorText : styleTexts){
            colorText.start = sb.length();
            sb.append(colorText.text);
        }
        Spannable spannable = new SpannableString(sb);

        //开始设置效果
        HashMap<Class, StyleText> lastSetStyleMap = new HashMap<Class, StyleText>();
        for(StyleText styleText : styleTexts){
            int nowIndex = styleText.start;
            if(styleText instanceof DefaultStyleText){//之前设置的所有style截止到这里
                for(StyleText lastStyle : lastSetStyleMap.values()){
                    lastStyle.setSpan(spannable, nowIndex);
                }
                lastSetStyleMap.clear();

            }else{
                StyleText lastStyle = lastSetStyleMap.get(styleText.getClass());
                if(lastStyle!=null){
                    lastStyle.setSpan(spannable, nowIndex);
                }
                lastSetStyleMap.put(styleText.getClass(), styleText);
            }
        }
        //效果设置到最后
        int endIndex = spannable.length();
        for(StyleText lastStyle : lastSetStyleMap.values()){
            lastStyle.setSpan(spannable, endIndex);
        }

        return spannable;
    }


    /**解析出一个style的部分 */
    private StyleText parseStyleText(String part){
        if(part.length() <= 1) return new DefaultStyleText(part);

        String formatType = part.substring(0,1);//取得首个formatType：#号或者S...
        part = part.substring(1);//先去掉首个可能的formatType

        Integer refIndex = null;
        if(part.startsWith("@")){//引用，解析@1,@3这样的形式。base-1
            try {
                refIndex = Integer.valueOf(part.substring(1, 2))-1;
                part = part.substring(2);
            } catch (Exception ignore) {
            }
        }

        if(formatType.equals("#")){//颜色
            ColorStateList textColor = null;
            if(refIndex !=null && colors!=null && colors.length>0){//引用
                textColor = colors[refIndex % colors.length];
                if(textColor==null) textColor = getTextColors();

            }else{
                try {
                    textColor = ColorStateList.valueOf(Color.parseColor("#" + part.substring(0, 8).trim()));//前8个字符是颜色代码，代表字体颜色
                    part = part.substring(8);
                } catch (Exception ignore) {
                }
                if(textColor==null){
                    try {
                        textColor = ColorStateList.valueOf(Color.parseColor("#" + part.substring(0, 6).trim()));//前6个字符是颜色代码，代表字体颜色
                        part = part.substring(6);
                    } catch (Exception ignore) {
                    }
                }
            }

            return new ColorText(part, textColor);

        }else if(formatType.equalsIgnoreCase("s")){//字体大小
            Integer size = null;
            if(refIndex !=null && sizes!=null && sizes.length>0){//引用
                size = sizes[refIndex % sizes.length];

            }else{
                try {
                    size = Integer.valueOf(part.substring(0, 2).trim());//前两个字符是数字，代表字体大小
                    part = part.substring(2);
                } catch (Exception ignore) {
                }
            }
            return (new SizeText(part, size));
        }else if(formatType.equalsIgnoreCase("u")){//下划线
            return new UnderlineStyle(part);

        }else if(formatType.equalsIgnoreCase("l")){//删除线
            return new LineThroughStyle(part);

        }else if(formatType.equalsIgnoreCase("b")){//删除线
            return new BoldStyle(part);

        }else{//formatType不被识别，就放在正文里
            return new DefaultStyleText(formatType + part);
        }
    }
    public void removeAllStyle(){
        if(getText()!=null) setText(getText().toString());
        if(getHint()!=null) setHint(getHint().toString());
    }

    public void removeAllStyle(Class<? extends StyleText> c){
        try {
            SpannableString spannable = new SpannableString(getText());
            for(Object whatSpan : getAllSpans(spannable, c)){
                if(whatSpan!=null) spannable.removeSpan(whatSpan);
            }
            setText(spannable);

            if(getHint()!=null){
                spannable = new SpannableString(getHint());
                for(Object whatSpan : getAllSpans(spannable, c)){
                    if(whatSpan!=null) spannable.removeSpan(whatSpan);
                }
                setHint(spannable);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Object[] getAllSpans(Spannable spannable, Class<? extends StyleText> c){
        return spannable.getSpans(0, spannable.length(), StyleText.getSpanClass(c));
    }


    private abstract static class StyleText{
        String text;
        int start;
        public StyleText(String text){
            this.text =text;
        }
        public abstract void setSpan(Spannable span, int end);

        public abstract Class getSpanClass();

        private static HashSet<StyleText> styles = new HashSet<StyleText>();
        public static Class getSpanClass(Class<? extends StyleText> c){
            for(StyleText styleText : styles){
                if(c.isInstance(styleText)){
                    return styleText.getSpanClass();
                }
            }
            try {
                Constructor<StyleText> constructor = (Constructor<StyleText>) c.getDeclaredConstructors()[0];
                int paramLength = constructor.getParameterTypes().length;
                StyleText styleText = constructor.newInstance(new Object[paramLength]);
                styles.add(styleText);
                return styleText.getSpanClass();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return Object.class;
        }
    }
    //无样式
    private static class DefaultStyleText extends StyleText{
        public DefaultStyleText(String text) {
            super(text);
        }
        @Override
        public void setSpan(Spannable span, int end) {
        }
        @Override
        public Class getSpanClass() {
            return null;
        }
    }
    //字体颜色
    public static class ColorText extends StyleText{
        ColorStateList colorStateList;
        ColorText(String text, ColorStateList colorStateList) {
            super(text);
            this.colorStateList = colorStateList;
        }
        @Override
        public void setSpan(Spannable span, int end) {
            if(span==null || colorStateList==null) return;
            span.setSpan(new ForegroundColorListSpan(colorStateList), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        @Override
        public Class getSpanClass() {
            return ForegroundColorListSpan.class;
        }

        class ForegroundColorListSpan extends ForegroundColorSpan{
            ColorStateList colorStateList;
            public ForegroundColorListSpan(ColorStateList colorStateList) {
                super(colorStateList.getDefaultColor());
                this.colorStateList = colorStateList;
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                if(colorStateList==null) ds.setColor(getForegroundColor());
                else ds.setColor(colorStateList.getColorForState(ds.drawableState, colorStateList.getDefaultColor()));
            }
        }
    }
    //字体大小
    public static class SizeText extends StyleText{
        Integer sizeInDp;
        public SizeText(String text, Integer sizeInDp) {
            super(text);
            this.sizeInDp = sizeInDp ;
        }
        @Override
        public void setSpan(Spannable span, int end) {
            if(span==null || sizeInDp==null) return;
            span.setSpan(new AbsoluteSizeSpan(sizeInDp, true), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        @Override
        public Class getSpanClass() {
            return AbsoluteSizeSpan.class;
        }
    }
    //下划线
    public static class UnderlineStyle extends StyleText{
        public UnderlineStyle(String text){
            super(text);
        }
        @Override
        public void setSpan(Spannable span, int end) {
            if(span==null) return;
            span.setSpan(new UnderlineSpan(), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        @Override
        public Class getSpanClass() {
            return UnderlineSpan.class;
        }
    }
    //删除线
    public static class LineThroughStyle extends StyleText{
        public LineThroughStyle(String text){
            super(text);
        }
        @Override
        public void setSpan(Spannable span, int end) {
            if(span==null) return;
            span.setSpan(new StrikethroughSpan(), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        @Override
        public Class getSpanClass() {
            return StrikethroughSpan.class;
        }
    }
    //粗体
    public static class BoldStyle extends StyleText{
        public BoldStyle(String text){
            super(text);
        }
        @Override
        public void setSpan(Spannable span, int end) {
            if(span==null) return;
            span.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        @Override
        public Class getSpanClass() {
            return StyleSpan.class;
        }
    }

}
