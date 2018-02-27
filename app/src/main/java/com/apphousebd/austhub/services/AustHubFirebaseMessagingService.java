/*
 * Created by Asif Imtiaz Shaafi
 *     Email: a15shaafi.209@gmail.com
 *     Date: 2, 2018
 *
 * Copyright (c) 2018, AppHouseBD. All rights reserved.
 *
 * Last Modified on 2/27/18 1:43 PM
 * Modified By: shaafi
 */

package com.apphousebd.austhub.services;

import com.apphousebd.austhub.utilities.AustHubNotification;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by Asif Imtiaz Shaafi on 11, 2017.
 * Email: a15shaafi.209@gmail.com
 */

public class AustHubFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (remoteMessage != null && remoteMessage.getNotification() != null)
            AustHubNotification.notify(getApplicationContext(),
                    remoteMessage.getNotification().getTitle(),
                    remoteMessage.getNotification().getBody(), 0);

    }
}
