package com.santint.colorformula.pswkeyboard;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.santint.colorformula.R;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;



/**
 * 虚拟键盘
 */
public class VirtualKeyboardView extends RelativeLayout {

    Context context;

    //因为就6个输入框不会变了，用数组内存申请固定空间，比List省空间（自己认为）
    private final GridView gridView;    //用GrideView布局键盘，其实并不是真正的键盘，只是模拟键盘的功能

    private final ArrayList<Map<String, String>> valueList;    //有人可能有疑问，为何这里不用数组了？
    //因为要用Adapter中适配，用数组不能往adapter中填充

    private RelativeLayout layoutBack;
    private final EditText editText;
    private TextView tvZero;
    private ImageView ivDel;

    public VirtualKeyboardView(Context context) {
        this(context, null);
    }

    public VirtualKeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.context = context;

        View view = View.inflate(context, R.layout.layout_virtual_keyboard, null);

        valueList = new ArrayList<>();

        //layoutBack = (RelativeLayout) view.findViewById(R.id.layoutBack);

        gridView = view.findViewById(R.id.gv_keybord);
        editText = view.findViewById(R.id.id_edit);
        tvZero = view.findViewById(R.id.id_tv_zero);
        ivDel = view.findViewById(R.id.id_iv_del);
        disableShowSoftInput(editText);
        initValueList();

        setupView();

        addView(view);      //必须要，不然不显示控件
    }
    /**
     * 禁止Edittext弹出软件盘，光标依然正常显示。
     */
    public void disableShowSoftInput(EditText editText)
    {
        if (android.os.Build.VERSION.SDK_INT <= 10)
        {
            editText.setInputType(InputType.TYPE_NULL);
        }
        else {
            Class<EditText> cls = EditText.class;
            Method method;
            try {
                method = cls.getMethod("setShowSoftInputOnFocus",boolean.class);
                method.setAccessible(true);
                method.invoke(editText, false);
            }catch (Exception e) {
            }

            try {
                method = cls.getMethod("setSoftInputShownOnFocus",boolean.class);
                method.setAccessible(true);
                method.invoke(editText, false);
            }catch (Exception e) {
            }
        }
    }
    public void setSelectionEnd(EditText editText) {
        Editable b = editText.getText();
        editText.setSelection(b.length());
    }

    public RelativeLayout getLayoutBack() {
        return layoutBack;
    }

    public ArrayList<Map<String, String>> getValueList() {
        return valueList;
    }

    private void initValueList() {

        for (int i = 1; i < 10; i++) {
            Map<String, String> map = new HashMap<String, String>();
            if (i < 10) {
                map.put("name", String.valueOf(i));
            }
//            else if (i == 10) {
//                map.put("name", "取消");
//            } else if (i == 11) {
//                map.put("name", String.valueOf(0));
//            } else if (i == 12) {
//                map.put("name", "确定");
//            }else if(i == 13){
//                map.put("name","");
//            }
            valueList.add(map);
        }
    }

    public GridView getGridView() {
        return gridView;
    }
    public EditText getEditText(){
        return editText;
    }
    public TextView getTextView(){
        return tvZero;
    }
    public ImageView getImageView(){
        return ivDel;
    }

    private void setupView() {

        KeyBoardAdapter keyBoardAdapter = new KeyBoardAdapter(context, valueList);
        gridView.setAdapter(keyBoardAdapter);
    }
}
