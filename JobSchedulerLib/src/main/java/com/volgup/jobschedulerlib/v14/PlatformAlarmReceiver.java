/*
 * Copyright (C) 2018 Evernote Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.volgup.jobschedulerlib.v14;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;

import com.volgup.jobschedulerlib.JobConfig;
import com.volgup.jobschedulerlib.JobProxy;
import com.volgup.jobschedulerlib.util.JobCat;

/**
 * @author VolgUpTech
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
public class PlatformAlarmReceiver extends BroadcastReceiver {

    /*package*/ static final String EXTRA_JOB_ID = "EXTRA_JOB_ID";
    /*package*/ static final String EXTRA_JOB_EXACT = "EXTRA_JOB_EXACT";
    /*package*/ static final String EXTRA_TRANSIENT_EXTRAS = "EXTRA_TRANSIENT_EXTRAS";

    /*package*/ static Intent createIntent(Context context, int jobId, boolean exact, @Nullable Bundle transientExtras) {
        Intent intent = new Intent(context, PlatformAlarmReceiver.class).putExtra(EXTRA_JOB_ID, jobId).putExtra(EXTRA_JOB_EXACT, exact);
        if (transientExtras != null) {
            intent.putExtra(EXTRA_TRANSIENT_EXTRAS, transientExtras);
        }
        return intent;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
       new Handler(Looper.getMainLooper()).post(() -> {
           try {
               if (intent != null && intent.hasExtra(EXTRA_JOB_ID) && intent.hasExtra(EXTRA_JOB_EXACT)) {
                   int jobId = intent.getIntExtra(EXTRA_JOB_ID, -1);
                   Bundle transientExtras = intent.getBundleExtra(EXTRA_TRANSIENT_EXTRAS);

                   if (intent.getBooleanExtra(EXTRA_JOB_EXACT, false)) {
                       if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
                           Intent serviceIntent = PlatformAlarmServiceExact.createIntent(context, jobId, transientExtras);
                           JobProxy.Common.startWakefulService(context, serviceIntent);
                       }else {
                    /*intent.putExtra(PlatformAlarmReceiver.EXTRA_JOB_ID, jobId);
                    if (transientExtras != null) {
                        intent.putExtra(PlatformAlarmReceiver.EXTRA_TRANSIENT_EXTRAS, transientExtras);
                    }*/
                           startJobScheduler(intent,context);
                       }
                   } else {
                       PlatformAlarmService.start(context, jobId, transientExtras);
                   }
               }
           } catch (Exception e) {
               e.printStackTrace();
           }
       });
    }
    private static final JobCat CAT = new JobCat("PlatformAlarmReceiver");
    void startJobScheduler(Intent intent,Context context){
        JobConfig.getExecutorService().execute(() -> {
            try {
                PlatformAlarmService.runJob(intent, context, CAT);
            } finally {
                // call here, our own wake lock could be acquired too late
                JobProxy.Common.completeWakefulIntent(intent);
            }
        });
    }
}
