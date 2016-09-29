package com.nk.hhImg.user.util;

import android.content.Context;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.widget.EditText;

import com.nk.framework.baseUtil.HhToast;


/**
 * Created by dax on 2015/9/25 0025.
 */
public class MaxLengthWatcher implements TextWatcher {
    private int maxLen = 0;
    private EditText editText = null;
    private Context context;

    public MaxLengthWatcher(Context context, int maxLen, EditText editText) {
        this.maxLen = maxLen;
        this.editText = editText;
        this.context = context;
    }

    public void afterTextChanged(Editable arg0) {
    }

    public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                  int arg3) {
    }

    public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        Editable editable = editText.getText();
        int len = editable.length();
        if (len > maxLen) {
            HhToast.showShort(context, "最多只能输入" + maxLen + "个字哦！");
            int selEndIndex = Selection.getSelectionEnd(editable);
            String str = editable.toString();
            //截取新字符串
            String newStr = str.substring(0, maxLen);
            editText.setText(newStr);
            editable = editText.getText();
            //新字符串的长度
            int newLen = editable.length();
            //旧光标位置超过字符串长度
            if (selEndIndex > newLen) {
                selEndIndex = editable.length();
            }
            //设置新光标所在的位置
            Selection.setSelection(editable, selEndIndex);
        }
    }
}
