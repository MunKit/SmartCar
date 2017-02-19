package com.example.munkit.smartcar;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;



public class receivealarm extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //Log.e("receiver","receiver!");

        //fetch extra string from the intent
        String get_string = intent.getExtras().getString("extra");

        Intent service_intent = new Intent(context, RingtonePlayingService.class);

        service_intent.putExtra("extra",get_string);
        //start the ringtone service
        context.startService(service_intent);
    }
}
