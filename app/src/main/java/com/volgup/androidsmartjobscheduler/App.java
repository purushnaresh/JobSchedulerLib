package com.volgup.androidsmartjobscheduler;

import android.app.Application;
import android.os.StrictMode;

import com.volgup.jobschedulerlib.JobManager;

/**
 * @author VolgUpTech
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        JobManager.create(this).addJobCreator(new DemoJobCreator());
    }
}

