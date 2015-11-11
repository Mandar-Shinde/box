package com.elekso.potfix.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.elekso.potfix.BackgroundService;

public class BootCompleteReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
    context.startService(new Intent(context, BackgroundService.class));

    }
}
