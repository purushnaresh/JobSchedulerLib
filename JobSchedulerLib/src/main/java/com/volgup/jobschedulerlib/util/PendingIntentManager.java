package com.volgup.jobschedulerlib.util;

import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

public class PendingIntentManager {

    public static int FLAG_ONE_SHOT() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            return PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_MUTABLE;
        }else {
           return PendingIntent.FLAG_ONE_SHOT;
        }
    }
    public static int FLAG_UPDATE_CURRENT(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                return PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE;
        }else {
            return  PendingIntent.FLAG_UPDATE_CURRENT;

        }
    }public static int FLAG_NO_CREATE(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                return PendingIntent.FLAG_NO_CREATE | PendingIntent.FLAG_MUTABLE;
        }else {
            return  PendingIntent.FLAG_NO_CREATE;

        }
    }

}
