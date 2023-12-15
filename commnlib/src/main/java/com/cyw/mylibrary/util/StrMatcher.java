package com.cyw.mylibrary.util;


import android.text.InputFilter;
import android.text.Spanned;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class StrMatcher {
    public static String getPattenStr(String desStr) {
        String regex = "^[0]*(.+)$";
        Pattern re = Pattern.compile(regex);
        ;
        Matcher mMatcher = re.matcher(desStr);
        if (mMatcher.find()) {
            return mMatcher.group(1);
        }
        return "";
    }

    //	public static boolean isJson(String content) {
////		try {
////			JSONObject object = new JSONObject();
////			;
////			return true;
////		} catch (Exception e) {
////			return false;
////		}
//		JSONObject object = new JSONObject();
//		object.
//		return false;
//	}
//禁止输入空格
    public static void setEditTextInhibitInputSpace(EditText editText) {
        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                if (" ".equals(source)) {
                    return "";
                } else {
                    return null;
                }
            }
        };
        editText.setFilters(new InputFilter[]{filter});
    }
    ///添加配方奶茶名称
	public static String stringFilter(String str) throws PatternSyntaxException {
// 只允许字母、数字和汉字其余的还可以随时添加比如下划线什么的，但是注意引文符号和中文符号区别
		String regEx = "^[a-zA-Z0-9\u4E00-\u9FA5()]";//正则表达式   "[^\u4E00-\u9FA5a-zA-Z0-9()]"这样不行，放在中括号中表示反向字符集？【存有疑问】
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.replaceAll("").trim();
	}
	///添加配方动作描述
	public static String stringFilter2(String str) throws PatternSyntaxException {
// 只允许字母、数字和汉字其余的还可以随时添加比如下划线什么的，但是注意引文符号和中文符号区别
        //$结束符 前面只有那么多内容，没有多余的
        //^开始符 用在【】里面表示非
        //+ 代表{1，}  //最少1个
        //?可选
        //【5】匹配5
        //{5}出现次数为5次
        //问题：我.你 和1.1都可以
		String regEx = "([\u4E00-\u9FA5a-zA-Z0-9].d)";//正则表达式   "[^\u4E00-\u9FA5a-zA-Z0-9()]"这样不行，放在中括号中表示反向字符集^d+(.d+)?
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.replaceAll("").trim();
	}

}
