/*
 * Created by Asif Imtiaz Shaafi
 *     Email: a15shaafi.209@gmail.com
 *     Date: 2, 2018
 *
 * Copyright (c) 2018, AppHouseBD. All rights reserved.
 *
 * Last Modified on 2/27/18 1:41 PM
 * Modified By: shaafi
 */

package com.apphousebd.austhub.utilities;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

/**
 * Created by Asif Imtiaz Shaafi, on 9/3/2016.
 * Email: a15shaafi.209@gmail.com
 */
public class MyTextWatcher implements TextWatcher {

    private EditText currentView;
    private View nextView;
    private int size;

    public MyTextWatcher(EditText currentView, View nextView, int size) {
        this.currentView = currentView;
        this.nextView = nextView;
        this.size = size;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (currentView.getText().toString().length() == size) {
            nextView.requestFocus();
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
