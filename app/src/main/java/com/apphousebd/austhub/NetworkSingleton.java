/*
 * Created by Asif Imtiaz Shaafi
 *     Email: a15shaafi.209@gmail.com
 *     Date: 2, 2018
 *
 * Copyright (c) 2018, AppHouseBD. All rights reserved.
 *
 * Last Modified on 2/27/18 1:42 PM
 * Modified By: shaafi
 */

package com.apphousebd.austhub;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class NetworkSingleton {
    private static final NetworkSingleton ourInstance = new NetworkSingleton();

    private static OkHttpClient sClient;

    private NetworkSingleton() {
        if (sClient == null) {
            sClient = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(5, TimeUnit.SECONDS)
                    .writeTimeout(5, TimeUnit.SECONDS)
                    .build();
        }
    }

    public static NetworkSingleton getInstance() {
        return ourInstance;
    }

    public static OkHttpClient getClient() {
        return sClient;
    }
}
