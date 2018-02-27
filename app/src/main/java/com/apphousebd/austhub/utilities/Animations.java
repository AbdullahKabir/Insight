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

package com.apphousebd.austhub.utilities;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.View;

/**
 * Created by Asif Imtiaz on 01,2017.
 * Email: a15shaafi.209@gmail.com
 */

public class Animations {

    public static void setScaleXAnimation(View view)
    {
        ObjectAnimator animator =
                ObjectAnimator.ofFloat(view, "scaleX", 1f, 1.1f);
        animator.setDuration(1000)
                .setRepeatCount(ValueAnimator.INFINITE);
        animator.setRepeatMode(ValueAnimator.REVERSE);
        animator.start();
    }

    public static void setAlphaXAnimation(View view)
    {
        ObjectAnimator animator =
                ObjectAnimator.ofFloat(view, "alpha", 1f, 0.5f);
        animator.setDuration(1000)
                .setRepeatCount(ValueAnimator.INFINITE);
        animator.setRepeatMode(ValueAnimator.REVERSE);
        animator.start();
    }



}
