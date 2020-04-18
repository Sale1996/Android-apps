package com.example.sale1996.fire_message.service

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService(){

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if(remoteMessage.notification != null){
            //TODO: show notification only if we are not in the chat channel from which the incoming message was sent

            Log.v("FCM", remoteMessage.data.toString())
        }
    }
}