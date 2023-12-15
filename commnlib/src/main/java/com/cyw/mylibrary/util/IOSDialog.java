package com.cyw.mylibrary.util;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TextView;

import com.cyw.mylibrary.R;


/**
 * 仿IOS选择弹出框
 *
 * @author cyw
 * @version v1.0 2015-9-9
 */
public class IOSDialog extends Dialog {

    public IOSDialog(Context context) {
        super(context);
    }

    public IOSDialog(Context context, int theme) {
        super(context, theme);
    }

    /**
     * 当弹出选择框时，点击返回键无效
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    public static class Builder {
        private final Context context; // 上下文对象
        private String title; // 对话框标题
        private String message; // 对话框内容
        private String confirm_btnText; // 按钮名称“确定”
        private String cancel_btnText; // 按钮名称“取消”
        private String neutral_btnText; // 按钮名称“隐藏”
        private View contentView; // 对话框中间加载的其他布局界面
        private int color, colorBtn;
        /* 按钮坚挺事件 */
        private OnClickListener confirm_btnClickListener;
        private OnClickListener cancel_btnClickListener;
        private OnClickListener neutral_btnClickListener;
        private boolean linearVisible = false;

        public Builder(Context context) {
            this.context = context;
        }
		public Builder setLinerVisible(boolean visible){
			linearVisible = visible;
			return this;
		}

        /* 设置对话框信息 */
        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setMessage(int message) {
            this.message = (String) context.getText(message);
            return this;
        }

        /**
         * @param color 自定义message颜色
         * @return
         */
        public Builder setColor(int color) {
            this.color = color;
            return this;
        }

        public Builder setBtnColor(int color) {
            this.colorBtn = color;
            return this;
        }

        public Builder setTitle(int title) {
            this.title = (String) context.getText(title);
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }

        public Builder setPositiveButton(int confirm_btnText,
                                         OnClickListener listener) {
            this.confirm_btnText = (String) context.getText(confirm_btnText);
            this.confirm_btnClickListener = listener;
            return this;
        }

        public Builder setPositiveButton(String confirm_btnText,
                                         OnClickListener listener) {
            this.confirm_btnText = confirm_btnText;
            this.confirm_btnClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(int cancel_btnText,
                                         OnClickListener listener) {
            this.cancel_btnText = (String) context.getText(cancel_btnText);
            this.cancel_btnClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(String cancel_btnText,
                                         OnClickListener listener) {
            this.cancel_btnText = cancel_btnText;
            this.cancel_btnClickListener = listener;
            return this;
        }

        public Builder setNeutralButton(int neutral_btnText,
                                        OnClickListener listener) {
            this.neutral_btnText = (String) context.getText(neutral_btnText);
            this.neutral_btnClickListener = listener;
            return this;
        }


        public Builder setNeutralButton(String neutral_btnText,
                                        OnClickListener listener) {
            this.neutral_btnText = neutral_btnText;
            this.neutral_btnClickListener = listener;
            return this;
        }

        public IOSDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            final IOSDialog dialog = new IOSDialog(context,
                    R.style.ios_dialog_style);
            dialog.setCanceledOnTouchOutside(false);
            View layout = inflater.inflate(R.layout.ios_dialog, null);
            dialog.addContentView(layout, new LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

            if (title == null || title.trim().length() == 0) {
                TextView textView = ((TextView) layout.findViewById(R.id.message));
                textView.setGravity(Gravity.CENTER);
                ((TextView) layout.findViewById(R.id.message))
                        .setTextColor(color);
            }

            if (neutral_btnText != null && confirm_btnText != null
                    && cancel_btnText != null) {
                ((Button) layout.findViewById(R.id.confirm_btn))
                        .setText(confirm_btnText);
                ((Button) layout.findViewById(R.id.confirm_btn))
                        .setTextColor(colorBtn);
                if (neutral_btnClickListener != null) {
                    layout.findViewById(R.id.neutral_btn)
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    neutral_btnClickListener.onClick(dialog,
                                            DialogInterface.BUTTON_NEUTRAL);
                                }
                            });
                } else {
                    layout.findViewById(R.id.neutral_btn)
                            .setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });
                }
            } else {
                layout.findViewById(R.id.neutral_btn).setVisibility(View.GONE);
                layout.findViewById(R.id.single_line).setVisibility(View.GONE);
            }
            if (confirm_btnText != null) {
                ((Button) layout.findViewById(R.id.confirm_btn))
                        .setText(confirm_btnText);
                ((Button) layout.findViewById(R.id.confirm_btn))
                        .setTextColor(colorBtn);
                if (confirm_btnClickListener != null) {
                    layout.findViewById(R.id.confirm_btn)
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    confirm_btnClickListener.onClick(dialog,
                                            DialogInterface.BUTTON_POSITIVE);
                                    dialog.dismiss();/////新增、、、、、、、、、
                                }
                            });
                } else {
                    layout.findViewById(R.id.confirm_btn)
                            .setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });
                }
            } else {
                layout.findViewById(R.id.confirm_btn).setVisibility(View.GONE);
                layout.findViewById(R.id.second_line).setVisibility(View.GONE);
                layout.findViewById(R.id.cancel_btn).setBackgroundResource(
                        R.drawable.ios_dialog_single_btn_select);
            }
            if (cancel_btnText != null) {
                ((Button) layout.findViewById(R.id.cancel_btn))
                        .setText(cancel_btnText);
                if (cancel_btnClickListener != null) {
                    layout.findViewById(R.id.cancel_btn)
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    cancel_btnClickListener.onClick(dialog,
                                            DialogInterface.BUTTON_NEGATIVE);
                                }
                            });
                } else {
                    layout.findViewById(R.id.cancel_btn)
                            .setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });
                }
            } else {
                layout.findViewById(R.id.cancel_btn).setVisibility(View.GONE);
                layout.findViewById(R.id.second_line).setVisibility(View.GONE);
                layout.findViewById(R.id.confirm_btn).setBackgroundResource(
                        R.drawable.ios_dialog_single_btn_select);
            }
            if (message != null) {
                ((TextView) layout.findViewById(R.id.message)).setText(message);
                // ((TextView) layout.findViewById(R.id.message))
                // .setTextColor(color);
            } // else if (contentView != null) {
            // ((LinearLayout) layout.findViewById(R.id.message))
            // .removeAllViews();
            // ((LinearLayout) layout.findViewById(R.id.message)).addView(
            // contentView, new LayoutParams(
            // LayoutParams.WRAP_CONTENT,
            // LayoutParams.WRAP_CONTENT));
            // }
//            if(linearVisible){
//                ((LinearLayout)layout.findViewById(R.id.linear)).setVisibility(View.VISIBLE);
//            }else{
//                ((LinearLayout)layout.findViewById(R.id.linear)).setVisibility(View.GONE);
//            }
            dialog.setContentView(layout);
            return dialog;
        }
    }
}
