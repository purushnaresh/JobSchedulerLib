package com.volgup.androidsmartjobscheduler;

import android.content.Context;

import androidx.annotation.NonNull;

import com.volgup.jobschedulerlib.Job;
import com.volgup.jobschedulerlib.JobCreator;
import com.volgup.jobschedulerlib.JobManager;

/**
 * @author VolgUpTech
 */
public class DemoJobCreator implements JobCreator {

    @Override
    public Job create(@NonNull String tag) {
        switch (tag) {
            case DemoSyncJob.TAG:
                return new DemoSyncJob();
            default:
                return null;
        }
    }

    public static final class AddReceiver extends AddJobCreatorReceiver {
        @Override
        protected void addJobCreator(@NonNull Context context, @NonNull JobManager manager) {
            // manager.addJobCreator(new DemoJobCreator());
        }
    }
}
