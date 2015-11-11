package com.elekso.potfix.utils;

import android.os.Handler;
import android.os.Looper;

import com.squareup.otto.Bus;


public class EventManager extends Bus {

    private static EventManager eventUtil;
    private Handler handler = new Handler(Looper.getMainLooper());

    private EventManager(){}

    public static EventManager getInstance() {
        synchronized (EventManager.class){
            if(eventUtil == null) {
                eventUtil = new EventManager();
            }
        }
        return eventUtil;
    }

    @Override
    public void post(final Object event) {
        if(Looper.myLooper() == Looper.getMainLooper()) {
            super.post(event);
            return;
        }

        handler.post(new Runnable() {
            @Override
            public void run() {
                EventManager.super.post(event);
            }
        });
    }
}
