package com.sonbaty.applicationtask.utils;

import android.content.Context;
import android.widget.EditText;

import com.sonbaty.applicationtask.R;

public class Validation {

    public static boolean setEmailValidation(Context context, EditText editText) {

        String emailPattern = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        if (!editText.getText().toString().matches(emailPattern)) {
            editText.setTextColor(context.getResources().getColorStateList(R.color.error));
            editText.requestFocus();
            return false;
        } else {
            editText.setTextColor(context.getResources().getColorStateList(R.color.black));
            return true;
        }

    }

}
