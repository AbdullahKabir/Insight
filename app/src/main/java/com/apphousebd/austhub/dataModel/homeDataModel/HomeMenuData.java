/*
 * Created by Asif Imtiaz Shaafi
 *     Email: a15shaafi.209@gmail.com
 *     Date: 2, 2018
 *
 * Copyright (c) 2018, AppHouseBD. All rights reserved.
 *
 * Last Modified on 2/27/18 1:33 PM
 * Modified By: shaafi
 */

package com.apphousebd.austhub.dataModel.homeDataModel;


import android.content.Context;

import com.apphousebd.austhub.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SayefReyadh on 12/26/2016.
 */

public class HomeMenuData {

    private static final int[] icons = {R.drawable.icon_routine, R.drawable.icon_calculator
            , R.drawable.icon_reminder, R.drawable.icon_results
            , R.drawable.icon_settings, R.drawable.icon_aboutus};


    public static List<HomeMenuListItem> getListData(Context context) {

        String[] titles = context.getResources().getStringArray(R.array.home_title_array);

        List<HomeMenuListItem> data = new ArrayList<>();

        for (int i = 0; i < icons.length; i++) {
            HomeMenuListItem item = new HomeMenuListItem();
            item.setImageResId(icons[i]);
            item.setTitle(titles[i]);
            data.add(item);
        }

        return data;
    }

}
