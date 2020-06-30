package com.example.smartgreenhouse;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class RestarterBroadcastReceiver extends BroadcastReceiver {

    private final static String TAG = RestarterBroadcastReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "RestarterBroadcastReceiver.onReceive");
        context.startService(new Intent(context, MyService.class));
    }
}