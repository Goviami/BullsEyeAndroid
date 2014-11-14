package com.goviami.bullseye.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by subbu on 11/14/2014.
 */
public class ServiceBroadcastReceiver extends BroadcastReceiver{

    private Context context;

    public ServiceBroadcastReceiver(Context context){
        this.context = context;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        String message = intent.getStringExtra("message");
        Toast toast = Toast.makeText(context, message , Toast.LENGTH_SHORT);
        toast.show();
    }
}
